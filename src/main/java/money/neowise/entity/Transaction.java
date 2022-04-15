package money.neowise.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "details")
    private String details;

    @NotNull(message = "transaction amount cannot be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "transaction amount cannot be zero or negative")
    @Column(name = "amount")
    private Double amount;

    @NotNull(message = "senderId cannot be null")
    @Column(name = "sender_id")
    private UUID senderId;

    @NotNull(message = "receiverId cannot be null")
    @Column(name = "receiver_id")
    private UUID receiverId;

    public Transaction() {
    }

    public Transaction(UUID senderId, UUID receiverId, Double amount, String details) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.details = details;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }
}
