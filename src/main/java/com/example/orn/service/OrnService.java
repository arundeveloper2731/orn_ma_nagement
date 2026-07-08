package com.example.orn.service;

import java.time.LocalDate;
import java.util.List;

import com.example.orn.dto.OrnRequestDTO;
import com.example.orn.dto.OrnResponseDTO;
import com.example.orn.model.OrnEntry;


public interface OrnService {
    OrnResponseDTO saveOrn(OrnRequestDTO  dto);
    
    List<OrnResponseDTO> getAllOrn();

    public List<OrnEntry> filter(LocalDate fromDate,LocalDate toDate,String status);

    OrnResponseDTO getById(Long id);

    OrnResponseDTO update(Long id,OrnRequestDTO dto);

    void delete(Long id);
    
} 