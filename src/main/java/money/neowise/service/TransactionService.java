package money.neowise.service;

import money.neowise.entity.Transaction;
import money.neowise.entity.User;
import money.neowise.exception.TransactionException;
import money.neowise.exception.UserException;
import money.neowise.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private Validator validator;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    public Transaction getTransactionById(UUID transactionId) throws TransactionException {
        return transactionRepository.findById(transactionId)
            .orElseThrow(() -> new TransactionException(
                    HttpStatus.NOT_FOUND,
                    "No such Transaction found for transactionId: '" + transactionId + "'"
                )
            );
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public Transaction processNewTransaction(Transaction newTransaction) throws TransactionException, UserException {
        Set<ConstraintViolation<Transaction>> violations = validator.validate(newTransaction);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Transaction> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage()).append(", ");
            }
            String message = sb.substring(0, sb.lastIndexOf(", "));
            throw new TransactionException(HttpStatus.BAD_REQUEST, message);
        }

        User sender = userService.getUserById(newTransaction.getSenderId());
        Double senderBalanceAfterTransaction = sender.getBalance() - newTransaction.getAmount();

        if (senderBalanceAfterTransaction < 0) {
            throw new TransactionException(
                HttpStatus.FORBIDDEN,
                "Insufficient fund in Sender's account to process the transaction"
            );
        }

        sender.setBalance(senderBalanceAfterTransaction);
        userService.updateUser(sender);

        User receiver = userService.getUserById(newTransaction.getReceiverId());
        Double receiverBalanceAfterTransaction = receiver.getBalance() + newTransaction.getAmount();
        receiver.setBalance(receiverBalanceAfterTransaction);
        userService.updateUser(receiver);

        newTransaction = transactionRepository.save(newTransaction);

        return newTransaction;

    }

    public Boolean reverseOldTransaction(UUID transactionId) throws TransactionException, UserException {

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
