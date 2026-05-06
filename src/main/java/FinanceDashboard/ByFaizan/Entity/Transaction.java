package FinanceDashboard.ByFaizan.Entity;
import FinanceDashboard.ByFaizan.Entity.TransactionType.EnumTransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private EnumTransactionType type; // INCOME / EXPENSE

    private String category;

    @Column(name = "category_id")
    private Integer categoryId;

    private String description;

    private LocalDate transactiondate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
}
