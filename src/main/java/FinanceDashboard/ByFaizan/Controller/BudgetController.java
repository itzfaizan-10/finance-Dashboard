package FinanceDashboard.ByFaizan.Controller;

import FinanceDashboard.ByFaizan.DTO.BudgetRequestDto;
import FinanceDashboard.ByFaizan.Entity.Budget;
import FinanceDashboard.ByFaizan.ResponseDTO.BudgetResponseDto;
import FinanceDashboard.ByFaizan.Services.budgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final budgetService bugservice;

    @PostMapping("/{userId}")
    public ResponseEntity<BudgetResponseDto> setBudget(@PathVariable Long userId,@RequestBody BudgetRequestDto budget) {

        int month = budget.getMonth() != null ? budget.getMonth() : LocalDate.now().getMonthValue();
        int year = budget.getYear() != null ? budget.getYear() : LocalDate.now().getYear();

        Budget saved = bugservice.add(
                userId,
                budget.getCategoryId(),
                budget.getLimitAmount(),
                month,year
        );
        BudgetResponseDto response = bugservice.convertToDto(saved,userId,month,year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
        public ResponseEntity<List<BudgetResponseDto>> getBudget( @PathVariable Long userId,
                                                                  @RequestParam(required = false) Integer month,
                                                                  @RequestParam(required = false) Integer year) {

        if (month == null || year == null) {
            // No month/year provided → use current month/year
            return ResponseEntity.ok(bugservice.getBudget(userId));
        } else {
            // Specific month/year provided
            return ResponseEntity.ok(bugservice.getBudget(userId, month, year));
        }
        }

    @GetMapping("/status")
        public ResponseEntity<List<BudgetResponseDto>> checkBudgetStatus(
                @RequestParam Long userId,
                @RequestParam(required = false) Integer month,
                @RequestParam(required = false) Integer year) {

            if (month == null) month = LocalDate.now().getMonthValue();
            if (year == null) year = LocalDate.now().getYear();

            // Reuse getBudget which already calculates status in response DTO
            List<BudgetResponseDto> budgets = bugservice.getBudget(userId, month, year);
            return ResponseEntity.ok(budgets);
        }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<?> delete(@PathVariable Long budgetId) {
        bugservice.deleteBudget(budgetId);
        return ResponseEntity.ok("Budget deleted");
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponseDto> updateBudgetLimit(@PathVariable Long budgetId, @RequestParam double newLimit) {
            return ResponseEntity.ok(bugservice.updateLimit(budgetId, newLimit));
    }
}
