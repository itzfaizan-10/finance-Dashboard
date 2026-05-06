package FinanceDashboard.ByFaizan.Controller;


import FinanceDashboard.ByFaizan.DTO.TransactionRequest;
import FinanceDashboard.ByFaizan.Entity.Transaction;
import FinanceDashboard.ByFaizan.Services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{userId}")
    public ResponseEntity<Transaction> add(@PathVariable Long userId, @RequestBody TransactionRequest tx) {
        return ResponseEntity.ok(transactionService.addTransaction(tx,userId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> get(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<?> delete(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Deleted");
    }
}
