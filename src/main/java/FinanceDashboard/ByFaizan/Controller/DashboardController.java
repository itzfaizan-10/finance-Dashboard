package FinanceDashboard.ByFaizan.Controller;
import FinanceDashboard.ByFaizan.Entity.Budget;
import FinanceDashboard.ByFaizan.Entity.Transaction;
import FinanceDashboard.ByFaizan.Entity.TransactionType.EnumTransactionType;
import FinanceDashboard.ByFaizan.ResponseDTO.DashboardResponseDto;
import FinanceDashboard.ByFaizan.Services.TransactionService;
import FinanceDashboard.ByFaizan.Services.budgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// DashboardController.java
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TransactionService transactionService;
    private final budgetService budgetService;

    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponseDto> getDashboard(@PathVariable Long userId) {

        // Get current month/year
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        // Get all transactions for the user
        List<Transaction> transactions = transactionService.getUserTransactions(userId);

        // Calculate totals
        Double totalIncome = transactions.stream()
                .filter(t -> t.getType() == EnumTransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        Double totalExpense = transactions.stream()
                .filter(t -> t.getType() == EnumTransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        Double totalBalance = totalIncome - totalExpense;

        // Get budgets for spending analysis
        List<Budget> budgets = budgetService.getBudgetsByUserAndMonth(userId, month, year);

        // Calculate spent per category
        Map<Integer, Double> spentByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType() == EnumTransactionType.EXPENSE && t.getCategoryId() != null) {
                spentByCategory.merge(t.getCategoryId(), t.getAmount(), Double::sum);
            }
        }

        // Get recent transactions (last 5)
        List<Transaction> recentTransactions = transactions.stream()
                .sorted((t1, t2) -> t2.getTransactiondate().compareTo(t1.getTransactiondate()))
                .limit(5)
                .collect(Collectors.toList());

        return ResponseEntity.ok(DashboardResponseDto.builder()
                .totalBalance(totalBalance)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .monthlyChange(calculateMonthlyChange(userId))
                .recentTransactions(recentTransactions)
                .categoryBreakdown(spentByCategory)
                .build());
    }

    private Double calculateMonthlyChange(Long userId) {
        // Calculate percentage change from last month
        // Implementation depends on your needs
        return 12.5; // Example
    }
}