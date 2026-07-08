package com.example.orn.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.orn.dto.OrnRequestDTO;
import com.example.orn.dto.OrnResponseDTO;
import com.example.orn.model.OrnEntry;
import com.example.orn.repository.OrnRepository;

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
        }
        else{
             orn = new OrnEntry();
             orn.setOrnNo((dto.getOrnNo()));

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

        return repository.findAll()
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

                    return dto;
                }).collect(Collectors.toList());
    }
    public List<OrnEntry> filter(LocalDate fromDate,LocalDate toDate,String status){

        if(fromDate!=null && toDate!=null && status!=null && !status.isEmpty()){

        return repository.findByTransactionDateBetweenAndStatus(fromDate,toDate,status);

        }

        if(fromDate!=null && toDate!=null){

        return repository.findByTransactionDateBetween(fromDate,toDate);

        }

        if(status!=null && !status.isEmpty()){

        return repository.findByStatus(status);

        }

        return repository.findAll();

        }

    @Override
    public OrnResponseDTO getById(Long id) 
    {
        OrnEntry orn = repository.findById(id).orElseThrow(() -> new RuntimeException("ORN Not found"));

        OrnResponseDTO dto = new OrnResponseDTO();

        dto.setId(orn.getId());
        dto.setOrnNo(orn.getOrnNo());
        dto.setCustomerName(orn.getCustomerName());
        dto.setMobileNumber(orn.getMobileNumber());
        dto.setLocation(orn.getLocation());
        dto.setAmount(orn.getAmount());
        dto.setTransactionDate(orn.getTransactionDate());
        dto.setStatus(orn.getStatus());

        return dto;
    }

    @Override
    public OrnResponseDTO update(Long id, OrnRequestDTO dto) {
        
        OrnEntry orn = repository.findById(id).orElseThrow(() -> new RuntimeException("ORN Not Found"));

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

        response.setMessage("ORN Updated Successfully");

        return response;
    }

    @Override
    public void delete(Long id) {
         OrnEntry orn = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ORN Not Found"));

    repository.delete(orn);


        }
        
    
}
