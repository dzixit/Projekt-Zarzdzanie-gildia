package application.services;

import application.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuildService {
    public List<Guild> getAllGuilds() throws SQLException {
        List<Guild> guilds = new ArrayList<>();
        String query = "SELECT guild_id, name, creation_date FROM Guild";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                guilds.add(new Guild(
                        rs.getInt("guild_id"),
                        rs.getString("name"),
                        rs.getString("creation_date")
                ));
            }
        }
        return guilds;
    }

    public int getGuildMemberCount(int guildId) throws SQLException {
        String query = "SELECT COUNT(player_id) AS member_count FROM Player WHERE guild_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, guildId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("member_count");
                }
            }
        }
        return 0;
    }

    public double getGuildFunds(int guildId) throws SQLException {
        String query = "SELECT gold_amount FROM Guild_Fund WHERE guild_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, guildId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("gold_amount");
                }
            }
        }
        return 0;
    }

    public List<Player_Class> getAllClasses() throws SQLException {
        List<Player_Class> classes = new ArrayList<>();
        String query = "SELECT class_id, class_name FROM Player_Class";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                classes.add(new Player_Class(
                        rs.getInt("class_id"),
                        rs.getString("class_name")
                ));
            }
        }
        return classes;
    }





    public boolean addNewPlayer(String username, int classId, int guildId) throws SQLException {
        if (username == null || username.trim().length() < 3 || username.trim().length() > 20) {
            throw new IllegalArgumentException("Nazwa gracza musi mieć 3-20 znaków");
        }

        String query = "INSERT INTO Player (username, join_date, guild_id, class_id, rank_id) " +
                "VALUES (?, CURDATE(), ?, ?, ?)";

        int defaultRank = 4;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username.trim());
            stmt.setInt(2, guildId);
            stmt.setInt(3, classId);
            stmt.setInt(4, defaultRank);

            return stmt.executeUpdate() > 0;
        }
    }

    public ObservableList<PlayerActivity> getPlayerActivities() throws SQLException {
        ObservableList<PlayerActivity> activities = FXCollections.observableArrayList();

        String query = "SELECT p.username, g.name AS guild_name, " +
                "COUNT(l.log_id) AS total_actions, " +
                "MAX(l.log_date) AS last_action_date " +
                "FROM Player p " +
                "LEFT JOIN Guild g ON p.guild_id = g.guild_id " +
                "LEFT JOIN Logs l ON p.player_id = l.player_id " +
                "GROUP BY p.username, g.name " +
                "ORDER BY total_actions DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Timestamp lastAction = rs.getTimestamp("last_action_date");
                activities.add(new PlayerActivity(
                        rs.getString("username"),
                        rs.getString("guild_name"),
                        rs.getInt("total_actions"),
                        lastAction != null ? lastAction.toLocalDateTime() : null,
                        0,
                        0
                ));
            }
        }
        return activities;
    }

    public ObservableList<FinancialTransaction> getFinancialTransactions() throws SQLException {
        ObservableList<FinancialTransaction> transactions = FXCollections.observableArrayList();

        String query = "SELECT 'Donation' AS transaction_type, d.donation_id AS transaction_id, " +
                "p.username, d.gold_amount, d.date_col, g.name AS guild_name " +
                "FROM Donation d " +
                "JOIN Player p ON d.player_id = p.player_id " +
                "JOIN Guild_Fund gf ON d.fund_id = gf.fund_id " +
                "JOIN Guild g ON gf.guild_id = g.guild_id " +
                "UNION ALL " +
                "SELECT 'Withdraw' AS transaction_type, w.withdraw_id AS transaction_id, " +
                "p.username, w.gold_amount, w.date_col, g.name AS guild_name " +
                "FROM Withdraw w " +
                "JOIN Player p ON w.player_id = p.player_id " +
                "JOIN Guild_Fund gf ON w.fund_id = gf.fund_id " +
                "JOIN Guild g ON gf.guild_id = g.guild_id " +
                "ORDER BY date_col DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                transactions.add(new FinancialTransaction(
                        rs.getString("transaction_type"),
                        rs.getInt("transaction_id"),
                        rs.getString("username"),
                        rs.getInt("gold_amount"),
                        rs.getDate("date_col").toLocalDate(),
                        rs.getString("guild_name")
                ));
            }
        }
        return transactions;
    }

    public ObservableList<Player> getAllPlayers() throws SQLException {
        ObservableList<Player> players = FXCollections.observableArrayList();
        String query = "SELECT p.player_id, p.username, pc.class_name, g.name AS guild_name, " +
                "p.join_date, p.rank_id " +
                "FROM Player p " +
                "JOIN Player_Class pc ON p.class_id = pc.class_id " +
                "JOIN Guild g ON p.guild_id = g.guild_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                players.add(new Player(
                        rs.getInt("player_id"),
                        rs.getString("username"),
                        rs.getString("class_name"),
                        rs.getString("guild_name"),
                        rs.getDate("join_date").toLocalDate(),
                        rs.getInt("rank_id")
                ));
            }
        }
        return players;
    }

    public int getClassCount(int classId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Player WHERE class_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }


    public boolean deletePlayer(int playerId) throws SQLException {
        String query = "DELETE FROM Player WHERE player_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addGuild(String guildName) throws SQLException {
        String query = "INSERT INTO Guild (name, creation_date) VALUES (?, CURDATE())";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, guildName);
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean addPlayerClass(String className) throws SQLException {
        String query = "INSERT INTO Player_Class (class_name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, className);
            return stmt.executeUpdate() > 0;
        }
    }
    public ObservableList<ClassAbility> getClassAbilities() throws SQLException {
        ObservableList<ClassAbility> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM Class_Abilities";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                data.add(new ClassAbility(
                        rs.getString("class_name"),
                        rs.getString("ability_name")
                ));
            }
        }
        return data;
    }

    public ObservableList<PlayerAchievementStat> getPlayerAchievementStats() throws SQLException {
        ObservableList<PlayerAchievementStat> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM Player_Achievements_Stats";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                data.add(new PlayerAchievementStat(
                        rs.getInt("player_id"),
                        rs.getString("username"),
                        rs.getString("achievement_name"),
                        rs.getDate("date_achieved").toLocalDate(),
                        rs.getInt("level"),
                        rs.getString("stat_name"),
                        rs.getInt("points")
                ));
            }
        }
        return data;
    }
    // Pobierz wszystkie uprawnienia
    public ObservableList<Permission> getAllPermissions() throws SQLException {
        List<Permission> permissions = new ArrayList<>();
        String query = "SELECT rank_id, rank_name, permission_tier FROM Permissions";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                permissions.add(new Permission(
                        rs.getInt("rank_id"),
                        rs.getString("rank_name"),
                        rs.getInt("permission_tier")
                ));
            }
        }
        return FXCollections.observableArrayList(permissions);
    }
    // Pobierz graczy z ich aktualnymi rangami
    public ObservableList<Player> getAllPlayersWithRanks() throws SQLException {
        ObservableList<Player> players = FXCollections.observableArrayList();
        String query = "SELECT p.player_id, p.username, g.name AS guild_name, " +
                "p.rank_id, pr.rank_name " +
                "FROM Player p " +
                "JOIN Permissions pr ON p.rank_id = pr.rank_id " +
                "JOIN Guild g ON p.guild_id = g.guild_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                players.add(new Player(
                        rs.getInt("player_id"),
                        rs.getString("username"),
                        rs.getString("guild_name"),
                        rs.getInt("rank_id"),
                        rs.getString("rank_name")
                ));
            }
        }
        return players;
    }

    // Aktualizuj rangę gracza
    public boolean updatePlayerRank(int playerId, int newRankId) throws SQLException {
        String query = "UPDATE Player SET rank_id = ? WHERE player_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, newRankId);
            stmt.setInt(2, playerId);
            return stmt.executeUpdate() > 0;
        }
    }
}