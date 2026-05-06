package FinanceDashboard.ByFaizan.ResponseDTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class BudgetResponseDto {
    private Long id;
    private String category;
    private Long categoryId;
    private Double limitAmount;
    private Double spentAmount;
    private Double remainingAmount;
    private Integer percentageUsed;
    private Integer  month;
    private Integer year;
    private String status;
}
