package FinanceDashboard.ByFaizan.ResponseDTO;


import FinanceDashboard.ByFaizan.Entity.TransactionType.EnumTransactionType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;
    private String description;
    private Double amount;
    private EnumTransactionType type;
    private String category;  // Category name
    private Integer categoryId;
    private LocalDateTime transactionDate;

}
