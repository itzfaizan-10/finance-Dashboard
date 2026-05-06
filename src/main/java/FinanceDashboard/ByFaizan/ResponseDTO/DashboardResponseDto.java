package FinanceDashboard.ByFaizan.ResponseDTO;

import FinanceDashboard.ByFaizan.Entity.Transaction;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponseDto {
    private Double totalBalance;
    private Double totalIncome;
    private Double totalExpense;
    private Double monthlyChange;
    private List<Transaction> recentTransactions;
    private Map<Integer, Double> categoryBreakdown;
}
