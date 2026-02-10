package application.models;

import java.time.LocalDate;

public class PlayerAchievementStat {
    private int playerId;
    private String username;
    private String achievementName;
    private LocalDate dateAchieved;
    private int level;
    private String statName;
    private int points;

    public PlayerAchievementStat(int playerId, String username, String achievementName,
                                 LocalDate dateAchieved, int level, String statName, int points) {
        this.playerId = playerId;
        this.username = username;
        this.achievementName = achievementName;
        this.dateAchieved = dateAchieved;
        this.level = level;
        this.statName = statName;
        this.points = points;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public LocalDate getDateAchieved() {
        return dateAchieved;
    }

    public int getLevel() {
        return level;
    }

    public String getStatName() {
        return statName;
    }

    public int getPoints() {
        return points;
    }
}