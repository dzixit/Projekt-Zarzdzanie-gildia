package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import application.models.Player;
import application.models.Permission;
import application.services.GuildService;
import java.sql.SQLException;

public class PermissionsController {
    @FXML private TableView<Player> playersTableView;
    @FXML private ComboBox<Permission> ranksComboBox;
    @FXML private Label statusLabel;

    private final GuildService guildService = new GuildService();

    @FXML
    public void initialize() {
        try {
            refreshData();
        } catch (SQLException e) {
            statusLabel.setText("Błąd ładowania danych: " + e.getMessage());
        }
    }

    private void refreshData() throws SQLException {
        playersTableView.setItems(guildService.getAllPlayersWithRanks());
        ranksComboBox.setItems(guildService.getAllPermissions());
    }

    @FXML
    private void updateRank() {
        Player selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
        Permission selectedRank = ranksComboBox.getValue();

        if (selectedPlayer == null || selectedRank == null) {
            statusLabel.setText("Wybierz gracza i rangę!");
            return;
        }

        try {
            boolean success = guildService.updatePlayerRank(
                    selectedPlayer.getPlayerId(),
                    selectedRank.getRankId()
            );

            if (success) {
                statusLabel.setText("Ranga zaktualizowana!");
                refreshData();
            }
        } catch (SQLException e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }
    @FXML
    public void handleRefresh() {
        try {
            refreshData();
            statusLabel.setText("Dane odświeżone!");
        } catch (SQLException e) {
            statusLabel.setText("Błąd odświeżania: " + e.getMessage());
        }
    }

}