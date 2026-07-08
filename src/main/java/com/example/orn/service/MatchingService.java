package com.example.orn.service;

import com.example.orn.dto.ComparisonDTO;
import com.example.orn.dto.MatchingResultDTO;

import java.util.List;
public interface MatchingService 
{
     // NEW overload: supports the required "Manual ORN Filter" (single ORN or "All").
     // Existing single-argument method is kept intact and now delegates to this one,
     // so no existing caller is broken.
     List<MatchingResultDTO> runMatching(String manualOrn, String fileName);

     List<MatchingResultDTO> runMatching(String fileName);

    ComparisonDTO getComparison(String orn, String fileName);
    
}
