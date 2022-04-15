package money.neowise.controller;

import money.neowise.entity.Transaction;
import money.neowise.exception.ApiException;
import money.neowise.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<Transaction> getTransactionById(@PathVariable UUID transactionId) throws ApiException {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction newTransaction) throws ApiException {
        Transaction processedTransaction = transactionService.processNewTransaction(newTransaction);
        return ResponseEntity.ok(processedTransaction);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Boolean> deleteTransaction(@PathVariable UUID transactionId) throws ApiException {
        Boolean deleteSuccessful = transactionService.reverseOldTransaction(transactionId);
        return ResponseEntity.ok(deleteSuccessful);
    }

}
