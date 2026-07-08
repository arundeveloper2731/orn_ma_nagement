package com.example.orn.controller;

import com.example.orn.dto.ComparisonDTO;
import com.example.orn.dto.MatchingResultDTO;
import com.example.orn.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins="*")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @PostMapping("/run")
    public ResponseEntity<List<MatchingResultDTO>> runMatching(
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String manualOrn) {

        List<MatchingResultDTO> result =
                matchingService.runMatching(manualOrn, fileName);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/details/{orn}")
    public ResponseEntity<ComparisonDTO> getComparison(
            @PathVariable String orn,
            @RequestParam(required = false) String fileName) {

        ComparisonDTO dto =
                matchingService.getComparison(orn, fileName);

        return ResponseEntity.ok(dto);
    }

}
