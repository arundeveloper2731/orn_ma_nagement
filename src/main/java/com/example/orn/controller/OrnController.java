package com.example.orn.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.orn.dto.OrnRequestDTO;
import com.example.orn.dto.OrnResponseDTO;
import com.example.orn.model.OrnEntry;
import com.example.orn.service.OrnService;

@RestController
@RequestMapping("/api/orn")
@CrossOrigin(origins="https://ornmanagement.netlify.app")

public class OrnController 
{
    private final OrnService service;

    public OrnController(OrnService service){
        this.service=service;
    }
    
    @PostMapping
    public OrnResponseDTO save(@RequestBody OrnRequestDTO dto)
    {
        return service.saveOrn(dto);
    }

    @GetMapping
    public List<OrnResponseDTO> getAll(){
        return service.getAllOrn();
    }

    @GetMapping("/filter")
    public List<OrnEntry> filter(@RequestParam(required=false) LocalDate fromDate,@RequestParam(required=false) LocalDate toDate,@RequestParam(required=false) String status){

    return service.filter(fromDate,toDate,status);

    }
    @GetMapping("/{id}")
    public OrnResponseDTO getById(@PathVariable Long id){

    return service.getById(id);

    }

    @PutMapping("/{id}")
    public OrnResponseDTO update(@PathVariable Long id,@RequestBody OrnRequestDTO dto){

    return service.update(id,dto);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

    service.delete(id);

    }
}
