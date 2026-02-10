package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import application.services.GuildService;
import application.models.ClassAbility;
import application.models.PlayerAchievementStat;
import java.sql.SQLException;
import java.time.LocalDate;

public class ViewsController {
    @FXML private TableView<ClassAbility> classAbilitiesTable;
    @FXML private TableView<PlayerAchievementStat> playerStatsTable;

    private final GuildService guildService = new GuildService();

    @FXML
    public void initialize() {
        try {
            setupClassAbilitiesTable();
            setupPlayerStatsTable();
        } catch (SQLException e) {
            System.err.println("Błąd ładowania danych:");
            e.printStackTrace();
        }
    }

    private void setupClassAbilitiesTable() throws SQLException {
        TableColumn<ClassAbility, String> classCol = new TableColumn<>("Klasa");
        classCol.setCellValueFactory(new PropertyValueFactory<>("className"));

        TableColumn<ClassAbility, String> abilityCol = new TableColumn<>("Umiejętność");
        abilityCol.setCellValueFactory(new PropertyValueFactory<>("abilityName"));

        classAbilitiesTable.getColumns().setAll(classCol, abilityCol);
        classAbilitiesTable.setItems(guildService.getClassAbilities());
    }

    private void setupPlayerStatsTable() throws SQLException {
        // Kolumna ID Gracza
        TableColumn<PlayerAchievementStat, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("playerId"));

        // Kolumna Nazwa Gracza
        TableColumn<PlayerAchievementStat, String> nameCol = new TableColumn<>("Gracz");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Kolumna Osiągnięcie
        TableColumn<PlayerAchievementStat, String> achievementCol = new TableColumn<>("Osiągnięcie");
        achievementCol.setCellValueFactory(new PropertyValueFactory<>("achievementName"));

        // Kolumna Data Osiągnięcia
        TableColumn<PlayerAchievementStat, LocalDate> dateCol = new TableColumn<>("Data");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateAchieved"));

        // Kolumna Poziom
        TableColumn<PlayerAchievementStat, Integer> levelCol = new TableColumn<>("Poziom");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));

        // Kolumna Statystyka
        TableColumn<PlayerAchievementStat, String> statCol = new TableColumn<>("Statystyka");
        statCol.setCellValueFactory(new PropertyValueFactory<>("statName"));

        // Kolumna Punkty
        TableColumn<PlayerAchievementStat, Integer> pointsCol = new TableColumn<>("Punkty");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        playerStatsTable.getColumns().setAll(
                idCol,
                nameCol,
                achievementCol,
                dateCol,
                levelCol,
                statCol,
                pointsCol
        );

        playerStatsTable.setItems(guildService.getPlayerAchievementStats());
    }
}