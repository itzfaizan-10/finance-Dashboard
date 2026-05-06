package FinanceDashboard.ByFaizan.Services;


import FinanceDashboard.ByFaizan.Entity.Budget;
import FinanceDashboard.ByFaizan.Entity.User;
import FinanceDashboard.ByFaizan.Repositary.BudgetRepository;
import FinanceDashboard.ByFaizan.Repositary.TranscationRepositary;
import FinanceDashboard.ByFaizan.Repositary.UserRepositary;
import FinanceDashboard.ByFaizan.ResponseDTO.BudgetResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class budgetService {


    private final BudgetRepository budgetRepository;
    private final UserRepositary userRepositary;
    private final TranscationRepositary transcationRepositary;


    @Transactional
    public Budget add(Long userId, int categoryId,Double limit,int month,int year) {
     User user =  userRepositary.findById(userId).orElseThrow(()-> new RuntimeException("User not found "+userId ));
        Budget b = new Budget();
        b.setUser(user);
        b.setCategoryId(categoryId);
        b.setLimitAmount(limit);
        b.setMonth(month);
        b.setYear(year);
        return budgetRepository.save(b);
    }

    // Get budgets for current month (no params)
    public List<BudgetResponseDto> getBudget(Long userId) {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return getBudget(userId, month, year);
    }

    // Get budgets for specific month/year
    public List<BudgetResponseDto> getBudget(Long userId, int month, int year) {
        List<Budget> budgets = budgetRepository.findByUser_IdAndMonthAndYear(
                userId,month,year);
        return budgets.stream().map(this::convertToDto).collect(Collectors.toList());
    }



    public String checkBudgetStatus(Budget b, Double spentAmount) {
        if (b.getLimitAmount() == 0) return "NO_LIMIT";
        double percent = (spentAmount / b.getLimitAmount()) * 100;
        if (percent >= 100) return "EXCEEDED";
        if (percent >= 80) return "NEAR_LIMIT";
        return "ON_TRACK";
    }

    // Update budget limit
    @Transactional
    public BudgetResponseDto updateLimit(Long budgetId, double newLimit) {
        Budget b = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        if (newLimit <= 0) {
            throw new RuntimeException("Limit must be greater than 0");
        }
        b.setLimitAmount(newLimit);
        budgetRepository.save(b);
        return convertToDto(b);
    }

    // for delete the budgte
    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new RuntimeException("Budget not found");
        }

        budgetRepository.deleteById(id);
    }
    // Calculate spent amount from transactions
    private Double calculateSpentAmount(Long userId, Integer categoryId, int month, int year) {
        System.out.println("Calculating spent for - UserId: " + userId +
                ", CategoryId: " + categoryId +
                ", Month: " + month +
                ", Year: " + year);

        Double spent = transcationRepositary.sumExpensesByUserAndCategoryAndMonth(
                userId, categoryId, month, year
        );

        System.out.println("Found spent amount: " + spent);
        return spent != null ? spent : 0.0;
    }

    // Add this to budgetService.java
    public List<Budget> getBudgetsByUserAndMonth(Long userId, int month, int year) {
        return budgetRepository.findByUser_IdAndMonthAndYear(userId, month, year);
    }

    // Helper: convert Entity -> Response DTO
    public BudgetResponseDto convertToDto(Budget b,Long userId, int month, int year) {
        Double spent = calculateSpentAmount(userId, b.getCategoryId(), month, year);
        double limit = b.getLimitAmount() != null ? b.getLimitAmount() : 0.0;
        double remaining = limit - spent;
        int percent = limit > 0 ? (int) ((spent / limit) * 100) : 0;
        String status = checkBudgetStatus(b,spent);

        return BudgetResponseDto.builder()
                .id(b.getId())
                .categoryId((long) b.getCategoryId())
                .category("Category " + b.getCategoryId())
                .limitAmount(limit)
                .spentAmount(spent)
                .remainingAmount(remaining)
                .percentageUsed(percent)
                .month(b.getMonth())
                .year(b.getYear())
                .status(status)
                .build();
    }

   //helper method
    public BudgetResponseDto convertToDto(Budget b) {
        // Get userId from the budget's user
        Long userId = b.getUser().getId();
        int month = b.getMonth();
        int year = b.getYear();

        return convertToDto(b, userId, month, year);
    }
}

