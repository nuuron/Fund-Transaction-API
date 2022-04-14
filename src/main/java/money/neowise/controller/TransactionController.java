package money.neowise.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import money.neowise.entity.Transaction;
import money.neowise.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> allTransactions = transactionService.getAllTransaction();
        return ResponseEntity.ok(allTransactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable UUID transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction newTransaction) {
        Transaction processedTransaction = transactionService.processNewTransaction(newTransaction);
        return ResponseEntity.ok(processedTransaction);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Boolean> deleteTransaction(@PathVariable UUID transactionId) {
        Boolean deleteSuccessful = transactionService.reverseOldTransaction(transactionId);
        return ResponseEntity.ok(deleteSuccessful);
    }

    @PostMapping("/check")
    public ResponseEntity<Transaction> check(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transaction);
    }

}
