package FinanceDashboard.ByFaizan.Repositary;

import FinanceDashboard.ByFaizan.Entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser_IdAndMonthAndYear(Long userId, int month, int year);
}

