package money.neowise.service;

import money.neowise.entity.Transaction;
import money.neowise.entity.User;
import money.neowise.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public Transaction processNewTransaction(UUID senderId, UUID receiverId, Double amount, String details) {
        if (!userService.userExistsById(senderId) || !userService.userExistsById(receiverId)) {
            return null;
        }

        User sender = userService.getUserById(senderId);
        Double senderBalanceAfterTransaction = sender.getBalance() - amount;

        if (senderBalanceAfterTransaction < 0) {
            return null;
        }

        sender.setBalance(senderBalanceAfterTransaction);
        userService.updateUser(sender);

        User receiver = userService.getUserById(receiverId);
        Double receiverBalanceAfterTransaction = receiver.getBalance() + amount;
        receiver.setBalance(receiverBalanceAfterTransaction);
        userService.updateUser(receiver);

        Transaction newTransaction = new Transaction(senderId, receiverId, amount, details);
        newTransaction = transactionRepository.save(newTransaction);

        return newTransaction;

    }

    public Boolean reverseOldTransaction (UUID transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            return false;
        }

        Transaction oldTransaction = getTransactionById(transactionId);

        Double amount = oldTransaction.getAmount();

        User sender = userService.getUserById(oldTransaction.getSenderId());
        Double newSenderBalance = sender.getBalance() + amount;
        sender.setBalance(newSenderBalance);
        userService.updateUser(sender);

        User receiver = userService.getUserById(oldTransaction.getReceiverId());
        Double newReceiverBalance = receiver.getBalance() - amount;
        receiver.setBalance(newReceiverBalance);
        userService.updateUser(receiver);

        transactionRepository.delete(oldTransaction);

        return true;

    }

}
