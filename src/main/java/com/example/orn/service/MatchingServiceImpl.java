package com.example.orn.service;

import com.example.orn.dto.ComparisonDTO;
import com.example.orn.dto.MatchingResultDTO;
import com.example.orn.model.ExcelUpload;
import com.example.orn.model.OrnEntry;
import com.example.orn.repository.ExcelUploadRepository;
import com.example.orn.repository.OrnRepository;
import com.example.orn.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingServiceImpl implements MatchingService {

    @Autowired
    private OrnRepository ornRepository;

    @Autowired
    private ExcelUploadRepository excelUploadRepository;

    
    // ------------------------------------------------------------------
    // Backward-compatible overload (old callers keep working unchanged).
    // Delegates to the new manualOrn-aware implementation with "all".
    // ------------------------------------------------------------------
    @Override
    public List<MatchingResultDTO> runMatching(String fileName) {
        return runMatching("all", fileName);
    }

    // ------------------------------------------------------------------
    // CHANGED SECTION: core matching engine rewritten to
    //   1) actually add rows to the result list (the previous version built
    //      each MatchingResultDTO but never called result.add(dto) for the
    //      manual side, so Matched/Unmatched/Duplicate rows never appeared),
    //   2) implement Rule 1-4 exactly as specified (ORN-number-only matching,
    //      duplicate detection on EITHER side, missing-manual detection),
    //   3) support the new "Manual ORN Filter" (single ORN or "All"),
    //   4) use HashMap/HashSet only, with each list scanned exactly once —
    //      no nested/duplicate loops.
    // ------------------------------------------------------------------
    @Override
    public List<MatchingResultDTO> runMatching(String manualOrn, String fileName) {

        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        String currentUser = SecurityUtils.getCurrentUsername();

        List<OrnEntry> manualList = isAdmin
                ? ornRepository.findAll()
                : ornRepository.findByCreatedBy(currentUser);

        List<ExcelUpload> excelList;
        if (fileName == null || fileName.isBlank() || fileName.equalsIgnoreCase("all")) {
            excelList = isAdmin ? excelUploadRepository.findAll() : excelUploadRepository.findByCreatedBy(currentUser);
        } else {
            excelList = isAdmin
                    ? excelUploadRepository.findByFileName(fileName)
                    : excelUploadRepository.findByFileNameAndCreatedBy(fileName, currentUser);
        }

        // ---- Pass 1: build Excel lookup (normalised-ORN -> first record) + counts ----
        Map<String, ExcelUpload> excelFirstMap = new HashMap<>();
        Map<String, Integer>     excelCountMap = new HashMap<>();

        for (ExcelUpload excel : excelList) {
            String raw = excel.getOrn();
            if (raw == null || raw.trim().isEmpty()) continue;

            String key = normalise(raw);
            excelCountMap.merge(key, 1, Integer::sum);
            excelFirstMap.putIfAbsent(key, excel);
        }

        // ---- Pass 2: build Manual lookup (normalised-ORN -> first record) + counts ----
        Map<String, OrnEntry> manualFirstMap = new HashMap<>();
        Map<String, Integer>  manualCountMap = new HashMap<>();

        for (OrnEntry manual : manualList) {
            String raw = manual.getOrnNo();
            if (raw == null || raw.trim().isEmpty()) continue;

            String key = normalise(raw);
            manualCountMap.merge(key, 1, Integer::sum);
            manualFirstMap.putIfAbsent(key, manual);
        }

        // ---- Apply Manual ORN Filter: "all" -> every manual ORN, else just the selected one ----
        boolean     allManual        = (manualOrn == null || manualOrn.isBlank() || manualOrn.equalsIgnoreCase("all"));
        Set<String> manualKeysInScope = allManual
                ? manualFirstMap.keySet()
                : (manualFirstMap.containsKey(normalise(manualOrn))
                        ? Collections.singleton(normalise(manualOrn))
                        : Collections.emptySet());

        List<MatchingResultDTO> result = new ArrayList<>();

        // ---- Rule 1 / Rule 2 / Rule 3 : evaluate every manual ORN in scope (single pass) ----
        for (String key : manualKeysInScope) {

            OrnEntry     manual = manualFirstMap.get(key);
            ExcelUpload  excel  = excelFirstMap.get(key);

            MatchingResultDTO dto = new MatchingResultDTO();
            dto.setOrn(manual.getOrnNo());
            dto.setManualOrn(manual.getOrnNo());
            dto.setCustomerName(manual.getCustomerName());
            dto.setMobileNumber(String.valueOf(manual.getMobileNumber()));

            if (excel == null) {
                // Rule 2 - Unmatched: manual ORN not present in the selected Excel file
                dto.setExcelOrn("-");
                dto.setStatus("Unmatched");
                dto.setMatchPercentage(0);
            } else {
                dto.setExcelOrn(excel.getOrn());

                boolean duplicate = manualCountMap.getOrDefault(key, 0) > 1
                        || excelCountMap.getOrDefault(key, 0) > 1;

                if (duplicate) {
                    // Rule 3 - Duplicate: ORN repeats on either side (never shown as Matched)
                    dto.setStatus("Duplicate");
                } else {
                    // Rule 1 - Matched: exists exactly once on both sides
                    dto.setStatus("Matched");
                }
                dto.setMatchPercentage(100);
            }

            result.add(dto);
        }

        // ---- Rule 4 : Excel ORNs that have no Manual counterpart at all ----
        // Only relevant when viewing the full Manual list ("All ORNs") — a specific
        // single-ORN selection is, by definition, an ORN that exists in Manual Entry.
        if (allManual) {
            for (Map.Entry<String, ExcelUpload> entry : excelFirstMap.entrySet()) {
                String key = entry.getKey();
                if (manualFirstMap.containsKey(key)) continue; // already handled above

                ExcelUpload excel = entry.getValue();

                MatchingResultDTO dto = new MatchingResultDTO();
                dto.setOrn(excel.getOrn());
                dto.setManualOrn("-");
                dto.setExcelOrn(excel.getOrn());
                dto.setCustomerName("-");
                dto.setMobileNumber("-");
                dto.setStatus("Missing Manual");
                dto.setMatchPercentage(0);

                result.add(dto);
            }
        }

        result.sort(Comparator.comparing(
                d -> (d.getOrn() == null ? "" : d.getOrn().trim().toUpperCase())
        ));

        return result;
    }

    private String normalise(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }

    // private boolean amountsEqual(double manualAmount, Double excelPrice) {
    //     if (excelPrice == null) return manualAmount == 0.0;
    //     return Math.abs(manualAmount - excelPrice) < 0.01;
    // }

    // private boolean datesEqual(java.time.LocalDate manualDate, java.time.LocalDate excelDate) {
    //     if (manualDate == null && excelDate == null) return true;
    //     if (manualDate == null || excelDate == null) return false;
    //     return manualDate.isEqual(excelDate);
    // }


    @Override
    public ComparisonDTO getComparison(String orn, String fileName) {

        ComparisonDTO dto = new ComparisonDTO();

        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        String currentUser = SecurityUtils.getCurrentUsername();

        Optional<OrnEntry> manualOpt = ornRepository.findByOrnNo(orn);
        if (manualOpt.isPresent() && !isAdmin) {
            String owner = manualOpt.get().getCreatedBy();
            if (owner == null || !owner.equals(currentUser)) {
                manualOpt = Optional.empty();
            }
        }

        List<ExcelUpload> excelList = isAdmin
                ? excelUploadRepository.findAllByOrn(orn)
                : excelUploadRepository.findAllByOrnAndCreatedBy(orn, currentUser);

        if (fileName != null && !fileName.isBlank() && !fileName.equalsIgnoreCase("all")) {
            excelList = excelList.stream()
                    .filter(e -> fileName.equals(e.getFileName()))
                    .collect(java.util.stream.Collectors.toList());
        }

        dto.setManual(manualOpt.orElse(null));
        dto.setExcel(excelList.isEmpty() ? null : excelList.get(0));
        dto.setDuplicateExcelRecords(excelList.size() > 1 ? excelList.subList(1, excelList.size()) : List.of());

        return dto;
    }
}