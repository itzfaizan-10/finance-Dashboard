package FinanceDashboard.ByFaizan.Services;

import FinanceDashboard.ByFaizan.DTO.TransactionRequest;
import FinanceDashboard.ByFaizan.Entity.Budget;
import FinanceDashboard.ByFaizan.Entity.Transaction;
import FinanceDashboard.ByFaizan.Entity.TransactionType.EnumTransactionType;
import FinanceDashboard.ByFaizan.Entity.User;
import FinanceDashboard.ByFaizan.Repositary.BudgetRepository;
import FinanceDashboard.ByFaizan.Repositary.TranscationRepositary;
import FinanceDashboard.ByFaizan.Repositary.UserRepositary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TranscationRepositary transactionRepository;
    private final UserRepositary userRepository;
    private final BudgetRepository budgetRepository;

    @Transactional
    public Transaction addTransaction(TransactionRequest dto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found " + userId));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setCategoryId(dto.getCategoryId());
        transaction.setDescription(dto.getDescription());
        transaction.setTransactiondate(dto.getDate() != null ? dto.getDate() : LocalDate.now());
        transaction.setUser(user);


        Transaction saved = transactionRepository.save(transaction);

        return saved;
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found " + id));

        transactionRepository.delete(transaction);
    }

}
