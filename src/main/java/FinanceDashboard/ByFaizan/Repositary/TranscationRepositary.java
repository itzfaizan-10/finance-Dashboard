package FinanceDashboard.ByFaizan.Repositary;

import FinanceDashboard.ByFaizan.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TranscationRepositary extends JpaRepository<Transaction, Long> {
     List<Transaction> findByUserId(Long userId);

     @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.categoryId = :categoryId AND t.type = 'EXPENSE' AND MONTH(t.transactiondate) = :month AND YEAR(t.transactiondate) = :year")
     Double sumExpensesByUserAndCategoryAndMonth(@Param("userId") Long userId,
                                                 @Param("categoryId") Integer categoryId,
                                                 @Param("month") int month,
                                                 @Param("year") int year);

}
