package application.models;
import java.time.LocalDateTime;

public class PlayerActivity {
    private String username;
    private String guildName;
    private int totalActions;
    private LocalDateTime lastActionDate;
    private int achievementsCount;
    private int donationsCount;

    public PlayerActivity(String username, String guildName, int totalActions,
                          LocalDateTime lastActionDate, int achievementsCount,
                          int donationsCount) {
        this.username = username;
        this.guildName = guildName;
        this.totalActions = totalActions;
        this.lastActionDate = lastActionDate;
        this.achievementsCount = achievementsCount;
        this.donationsCount = donationsCount;
    }

    public String getUsername() { return username; }
    public String getGuildName() { return guildName; }
    public int getTotalActions() { return totalActions; }
    public LocalDateTime getLastActionDate() { return lastActionDate; }
    public int getAchievementsCount() { return achievementsCount; }
    public int getDonationsCount() { return donationsCount; }
}
