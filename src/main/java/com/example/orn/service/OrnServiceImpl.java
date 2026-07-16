package com.example.orn.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.orn.dto.OrnRequestDTO;
import com.example.orn.dto.OrnResponseDTO;
import com.example.orn.model.OrnEntry;
import com.example.orn.repository.OrnRepository;
import com.example.orn.security.SecurityUtils;

@Service
public class OrnServiceImpl implements OrnService
{
    private final OrnRepository repository;

    public OrnServiceImpl(OrnRepository repository){
        this.repository = repository;
    }

    @Override
    public OrnResponseDTO saveOrn(OrnRequestDTO dto)
    {
        OrnEntry orn;

        if(repository.existsByOrnNo(dto.getOrnNo()))
        {
            orn = repository.findByOrnNo(dto.getOrnNo()).get();
            // This ORN number already exists — treat this as an update.
            // A non-admin may only update an ORN they created themselves.
            assertOwnedByCurrentUserOrAdmin(orn);
        }
        else{
             orn = new OrnEntry();
             orn.setOrnNo((dto.getOrnNo()));
             // Tag the new entry with its owner so it only shows up for
             // this user later (ADMIN can still see every entry).
             orn.setCreatedBy(SecurityUtils.getCurrentUsername());

        }

        orn.setCustomerName(dto.getCustomerName());
        orn.setMobileNumber(dto.getMobileNumber());
        orn.setLocation(dto.getLocation());
        orn.setAmount(dto.getAmount());
        orn.setTransactionDate(dto.getTransactionDate());
        orn.setStatus(dto.getStatus());

        OrnEntry saved = repository.save(orn);
        OrnResponseDTO response = new OrnResponseDTO();

        response.setId(saved.getId());
        response.setOrnNo(saved.getOrnNo());
        response.setCustomerName(saved.getCustomerName());
        response.setMobileNumber(saved.getMobileNumber());
        response.setLocation(saved.getLocation());
        response.setAmount(saved.getAmount());
        response.setTransactionDate(saved.getTransactionDate());
        response.setStatus(saved.getStatus());
        response.setCreatedBy(saved.getCreatedBy());

        if(repository.existsByOrnNo(dto.getOrnNo()) && saved.getId() != null){
            response.setMessage("ORN Updated Successfully");
        }
        else{
            response.setMessage("ORN Saved Successfully");
        }
        
        return response;
    }

    @Override
    public List<OrnResponseDTO> getAllOrn() {

        List<OrnEntry> source = SecurityUtils.isCurrentUserAdmin()
                ? repository.findAll()
                : repository.findByCreatedBy(SecurityUtils.getCurrentUsername());

        return source
                .stream().map(orn -> {
                    OrnResponseDTO dto = new OrnResponseDTO();

                    dto.setId(orn.getId());
                    dto.setOrnNo(orn.getOrnNo());
                    dto.setCustomerName(orn.getCustomerName());
                    dto.setMobileNumber(orn.getMobileNumber());
                    dto.setLocation(orn.getLocation());
                    dto.setAmount(orn.getAmount());
                    dto.setTransactionDate(orn.getTransactionDate());
                    dto.setStatus(orn.getStatus());
                    dto.setCreatedBy(orn.getCreatedBy());

                    return dto;
                }).collect(Collectors.toList());
    }
    public List<OrnEntry> filter(LocalDate fromDate,LocalDate toDate,String status){

        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        String currentUser = SecurityUtils.getCurrentUsername();

        if(fromDate!=null && toDate!=null && status!=null && !status.isEmpty()){

        return isAdmin
                ? repository.findByTransactionDateBetweenAndStatus(fromDate,toDate,status)
                : repository.findByCreatedByAndTransactionDateBetweenAndStatus(currentUser,fromDate,toDate,status);

        }

        if(fromDate!=null && toDate!=null){

        return isAdmin
                ? repository.findByTransactionDateBetween(fromDate,toDate)
                : repository.findByCreatedByAndTransactionDateBetween(currentUser,fromDate,toDate);

        }

        if(status!=null && !status.isEmpty()){

        return isAdmin
                ? repository.findByStatus(status)
                : repository.findByCreatedByAndStatus(currentUser,status);

        }

        return isAdmin
                ? repository.findAll()
                : repository.findByCreatedBy(currentUser);

        }

    @Override
    public OrnResponseDTO getById(Long id) 
    {
        OrnEntry orn = repository.findById(id).orElseThrow(() -> new RuntimeException("ORN Not found"));

        assertOwnedByCurrentUserOrAdmin(orn);

        OrnResponseDTO dto = new OrnResponseDTO();

        dto.setId(orn.getId());
        dto.setOrnNo(orn.getOrnNo());
        dto.setCustomerName(orn.getCustomerName());
        dto.setMobileNumber(orn.getMobileNumber());
        dto.setLocation(orn.getLocation());
        dto.setAmount(orn.getAmount());
        dto.setTransactionDate(orn.getTransactionDate());
        dto.setStatus(orn.getStatus());
        dto.setCreatedBy(orn.getCreatedBy());

        return dto;
    }

    @Override
    public OrnResponseDTO update(Long id, OrnRequestDTO dto) {
        
        OrnEntry orn = repository.findById(id).orElseThrow(() -> new RuntimeException("ORN Not Found"));

        assertOwnedByCurrentUserOrAdmin(orn);

        orn.setOrnNo(dto.getOrnNo());
        orn.setCustomerName(dto.getCustomerName());
        orn.setMobileNumber(dto.getMobileNumber());
        orn.setLocation(dto.getLocation());
        orn.setAmount(dto.getAmount());
        orn.setTransactionDate(dto.getTransactionDate());
        orn.setStatus(dto.getStatus());

        OrnEntry updated = repository.save(orn);

        OrnResponseDTO response = new OrnResponseDTO();

        response.setId(updated.getId());
        response.setOrnNo(updated.getOrnNo());
        response.setCustomerName(updated.getCustomerName());
        response.setMobileNumber(updated.getMobileNumber());
        response.setLocation(updated.getLocation());
        response.setAmount(updated.getAmount());
        response.setTransactionDate(updated.getTransactionDate());
        response.setStatus(updated.getStatus());
        response.setCreatedBy(updated.getCreatedBy());

        response.setMessage("ORN Updated Successfully");

        return response;
    }

    @Override
    public void delete(Long id) {
         OrnEntry orn = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ORN Not Found"));

         assertOwnedByCurrentUserOrAdmin(orn);

    repository.delete(orn);


        }

    /**
     * Ensures the entry belongs to the currently logged-in user, unless that
     * user is an ADMIN (who can view/edit/delete every entry). Legacy rows
     * created before this fix have createdBy = null and are treated as
     * ADMIN-only, matching the "no owner assigned" choice made for the
     * migration.
     *
     * Throws Spring Security's AccessDeniedException, which the existing
     * GlobalExceptionHandler already maps to HTTP 403 Forbidden.
     */
    private void assertOwnedByCurrentUserOrAdmin(OrnEntry orn) {
        if (SecurityUtils.isCurrentUserAdmin()) {
            return;
        }
        String currentUser = SecurityUtils.getCurrentUsername();
        if (orn.getCreatedBy() == null || !orn.getCreatedBy().equals(currentUser)) {
            throw new AccessDeniedException("You do not have permission to access this ORN record");
        }
    }
        
    
}
