package FinanceDashboard.ByFaizan.Services;

import FinanceDashboard.ByFaizan.Entity.Transaction;
import FinanceDashboard.ByFaizan.Entity.TransactionType.EnumTransactionType;
import FinanceDashboard.ByFaizan.Repositary.TranscationRepositary;
//import jakarta.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TranscationRepositary transactionRepository;

    public Map<String, Double> getSummary(Long userId) {
        List<Transaction> list = transactionRepository.findByUserId(userId);

        double income = list.stream()
                .filter(t -> t.getType() == EnumTransactionType.INCOME)
                .mapToDouble(Transaction::getAmount).sum();

        double expense = list.stream()
                .filter(t -> t.getType() == EnumTransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount).sum();

        double balance = income - expense;

        Map<String, Double> result = new HashMap<>();
        result.put("income", income);
        result.put("expense", expense);
        result.put("balance", balance);

        return result;
    }
}
