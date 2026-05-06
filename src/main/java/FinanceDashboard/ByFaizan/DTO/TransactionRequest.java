package FinanceDashboard.ByFaizan.DTO;
import FinanceDashboard.ByFaizan.Entity.TransactionType.EnumTransactionType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRequest {
    private Double amount;
    private EnumTransactionType type; // INCOME / EXPENSE
    private String category;
    private Integer categoryId;
    private String description;
    private LocalDate date;

}


