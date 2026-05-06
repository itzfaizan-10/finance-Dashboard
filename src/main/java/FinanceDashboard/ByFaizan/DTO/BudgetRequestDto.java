package FinanceDashboard.ByFaizan.DTO;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BudgetRequestDto {
    private int categoryId;
    private Double limitAmount;
    private Integer  month;
    private Integer year;
}