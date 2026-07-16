package com.example.orn.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.orn.dto.DashboardDTO;
import com.example.orn.dto.RecentOrnDTO;
import com.example.orn.model.ExcelUpload;
import com.example.orn.model.OrnEntry;
import com.example.orn.repository.ExcelUploadRepository;
import com.example.orn.repository.ExpenseRepository;
import com.example.orn.repository.OrnRepository;
import com.example.orn.security.SecurityUtils;
import com.example.orn.util.OrnMatchUtils;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrnRepository ornRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private ExcelUploadRepository excelrepo;

    @Override
    public DashboardDTO getDashboard() {

        DashboardDTO dto = new DashboardDTO();

        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        String currentUser = SecurityUtils.getCurrentUsername();

        List<ExcelUpload> allExcel = isAdmin ? excelrepo.findAll() : excelrepo.findByCreatedBy(currentUser);
        List<OrnEntry> allManual = isAdmin ? ornRepo.findAll() : ornRepo.findByCreatedBy(currentUser);

        dto.setTotalORN((long) allManual.size());
        dto.setExcelRecords((long) allExcel.size());

        // ---- Excel side: normalised-ORN -> occurrence count (HashMap, single pass) ----
        Map<String, Long> excelOrnCount = allExcel
                .stream()
                .filter(e -> e.getOrn() != null && !e.getOrn().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        e -> OrnMatchUtils.normalise(e.getOrn()),
                        Collectors.counting()
                ));

        Set<String> excelOrnSet = excelOrnCount.keySet();

        // ---- Manual side: normalised-ORN -> occurrence count (HashMap, single pass) ----
        Map<String, Long> manualOrnCount = allManual
                .stream()
                .filter(o -> o.getOrnNo() != null && !o.getOrnNo().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        o -> OrnMatchUtils.normalise(o.getOrnNo()),
                        Collectors.counting()
                ));

        List<OrnEntry> matchedList   = new java.util.ArrayList<>();
        List<OrnEntry> duplicateList = new java.util.ArrayList<>();
        List<OrnEntry> unmatchedList = new java.util.ArrayList<>();

        // CHANGED: Matched/Unmatched/Duplicate is now decided ONLY by the ORN number
        // (Rules 1-3 from the Matching Engine) — amount/date comparison removed.
        // This keeps the Dashboard counts always in sync with the Matching page.
        for (OrnEntry o : allManual) {

            if (o.getOrnNo() == null || o.getOrnNo().trim().isEmpty()) {
                unmatchedList.add(o);
                continue;
            }

            String key = OrnMatchUtils.normalise(o.getOrnNo());

            boolean inExcel   = excelOrnSet.contains(key);
            boolean dupExcel  = excelOrnCount.getOrDefault(key, 0L)  > 1;
            boolean dupManual = manualOrnCount.getOrDefault(key, 0L) > 1;

            if (!inExcel) {
                unmatchedList.add(o);              // Rule 2 - Unmatched
            } else if (dupExcel || dupManual) {
                duplicateList.add(o);              // Rule 3 - Duplicate
            } else {
                matchedList.add(o);                // Rule 1 - Matched
            }
        }

        dto.setMatchedORN(matchedList);
        dto.setUnmatchedORN(unmatchedList);
        dto.setDuplicateORN(duplicateList);

        dto.setMatchedCount(matchedList.size());
        dto.setDuplicateCount(duplicateList.size());
        dto.setUnmatchedCount(unmatchedList.size());

        dto.setTotalExpense(isAdmin
                ? expenseRepo.getTotalExpense()
                : expenseRepo.getTotalExpenseByCreatedBy(currentUser));

        List<RecentOrnDTO> recentList = (isAdmin
                ? ornRepo.findTop5ByOrderByTransactionDateDesc()
                : ornRepo.findTop5ByCreatedByOrderByTransactionDateDesc(currentUser))
                .stream()
                .map(o -> {
                    String key = o.getOrnNo() == null ? ""
                            : OrnMatchUtils.normalise(o.getOrnNo());

                    // CHANGED: same ORN-number-only rule applied to the "Recent Entries"
                    // status badge, instead of the old amount/date "Partial" logic.
                    String matchStatus;
                    if (!excelOrnSet.contains(key)) {
                        matchStatus = "Unmatched";
                    } else if (excelOrnCount.getOrDefault(key, 0L) > 1
                            || manualOrnCount.getOrDefault(key, 0L) > 1) {
                        matchStatus = "Duplicate";
                    } else {
                        matchStatus = "Matched";
                    }

                    return new RecentOrnDTO(
                            o.getOrnNo(),
                            o.getCustomerName(),
                            o.getTransactionDate() != null
                                    ? o.getTransactionDate().toString() : "",
                            matchStatus   // ← show matching status, not transaction status
                    );
                })
                .toList();

        dto.setRecentEntries(recentList);

        return dto;
    }
}