package application.models;
import java.time.LocalDate;

public class FinancialTransaction {
    private String transactionType;
    private int transactionId;
    private String username;
    private int goldAmount;
    private LocalDate transactionDate;
    private String guildName;

    public FinancialTransaction(String transactionType, int transactionId,
                                String username, int goldAmount,
                                LocalDate transactionDate, String guildName) {
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.username = username;
        this.goldAmount = goldAmount;
        this.transactionDate = transactionDate;
        this.guildName = guildName;
    }

    public String getTransactionType() { return transactionType; }
    public int getTransactionId() { return transactionId; }
    public String getUsername() { return username; }
    public int getGoldAmount() { return goldAmount; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getGuildName() { return guildName; }
}
