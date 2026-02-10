package application.models;

import java.time.LocalDate;

public class Player {
    private int playerId;
    private String username;
    private String className;
    private String guildName;
    private LocalDate joinDate;
    private int rankId;
    private String rank_name;

    public Player(int playerId, String username, String className,
                  String guildName, LocalDate joinDate, int rankId) {
        this.playerId = playerId;
        this.username = username;
        this.className = className;
        this.guildName = guildName;
        this.joinDate = joinDate;
        this.rankId = rankId;
    }
    public Player(int playerId, String username, String className,
                  String guildName, LocalDate joinDate, int rankId, String rank_name) {
        this.playerId = playerId;
        this.username = username;
        this.className = className;
        this.guildName = guildName;
        this.joinDate = joinDate;
        this.rankId = rankId;
        this.rank_name = rank_name;
    }

    public Player(int playerId, String username, String guildName, int rankId, String rankName) {
        this.playerId = playerId;
        this.username = username;
        this.guildName = guildName;
        this.rankId = rankId;
        this.rank_name = rankName;
    }


    // Gettery
    public String getUsername() { return username; }
    public String getClassName() { return className; }
    public String getGuildName() { return guildName; }
    public LocalDate getJoinDate() { return joinDate; }
    public int getPlayerId() { return playerId; }
    public int getRankId() { return rankId; }
    public String getRankName() { return rank_name; }
}

