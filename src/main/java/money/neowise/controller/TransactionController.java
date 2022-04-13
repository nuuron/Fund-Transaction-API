package money.neowise.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import money.neowise.entity.Transaction;
import money.neowise.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        UUID transactionId = UUID.fromString(id);
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Map<String, Object> payload) {
        UUID senderId = UUID.fromString(payload.get("senderId").toString());
        UUID receiverId = UUID.fromString(payload.get("receiverId").toString());
        Double amount = Double.parseDouble(payload.get("amount").toString());
        String details = payload.get("details").toString();

        Transaction transaction = transactionService.processNewTransaction(senderId, receiverId, amount, details);

        return ResponseEntity.ok(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTransaction(@PathVariable String id) {
        UUID transactionId = UUID.fromString(id);

        Boolean deleteSuccessful = transactionService.reverseOldTransaction(transactionId);

        return ResponseEntity.ok(deleteSuccessful);
    }

}
