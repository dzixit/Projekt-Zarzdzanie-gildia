package application.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.models.Permission;
import application.models.Player;
import application.models.Player_Class;
import application.services.GuildService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlayersController implements Initializable {
    @FXML private TableView<Player> playersTableView;
    @FXML private PieChart classDistributionChart;
    @FXML private Button deletePlayerButton;
    @FXML private ComboBox<Permission> rankComboBox;

    private GuildService guildService = new GuildService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setupClassDistributionChart();
            setupPlayersTable();
            setupDeleteButton();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Błąd inicjalizacji", "Nie udało się załadować danych graczy");
        }
    }

    private void setupClassDistributionChart() throws SQLException {
        classDistributionChart.setTitle("Rozkład klas graczy");
        classDistributionChart.getData().clear();

        for (Player_Class playerClass : guildService.getAllClasses()) {
            int count = guildService.getClassCount(playerClass.getClassId());
            classDistributionChart.getData().add(new PieChart.Data(
                    playerClass.getClassName(),
                    count
            ));
        }
    }

    private void setupPlayersTable() throws SQLException {
        TableColumn<Player, String> nameCol = new TableColumn<>("Nazwa");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Player, String> classCol = new TableColumn<>("Klasa");
        classCol.setCellValueFactory(new PropertyValueFactory<>("className"));

        TableColumn<Player, String> guildCol = new TableColumn<>("Gildia");
        guildCol.setCellValueFactory(new PropertyValueFactory<>("guildName"));

        TableColumn<Player, String> joinDateCol = new TableColumn<>("Data dołączenia");
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));

        playersTableView.getColumns().setAll(nameCol, classCol, guildCol, joinDateCol);
        playersTableView.setItems(FXCollections.observableArrayList(guildService.getAllPlayers()));
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupDeleteButton() {
        deletePlayerButton.setOnAction(e -> {
            Player selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
            if (selectedPlayer != null) {
                try {
                    boolean success = guildService.deletePlayer(selectedPlayer.getPlayerId());
                    if (success) {
                        refreshPlayersTable();
                        setupClassDistributionChart();
                    }
                } catch (SQLException ex) {
                    showErrorAlert("Błąd", "Nie udało się usunąć gracza: " + ex.getMessage());
                }
            } else {
                showErrorAlert("Błąd", "Wybierz gracza do usunięcia");
            }
        });
    }

    void refreshPlayersTable() throws SQLException {
        playersTableView.setItems(FXCollections.observableArrayList(guildService.getAllPlayers()));
        setupClassDistributionChart();
    }
    @FXML
    private void handleRefreshButton() {
        try {
            refreshPlayersTable();
            setupClassDistributionChart();
        } catch (SQLException e) {
            showErrorAlert("Błąd odświeżania", "Nie udało się odświeżyć danych: " + e.getMessage());
        }
    }




}