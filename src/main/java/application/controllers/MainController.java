package application.controllers;

import application.models.*;
import application.services.GuildService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainController {
    @FXML
    private ListView<String> guildListView;
    @FXML
    private BarChart<String, Number> membersChart;
    @FXML
    private PieChart fundsPieChart;
    @FXML
    private TextField playerNameField;
    @FXML
    private ComboBox<Player_Class> classComboBox;
    @FXML
    private ComboBox<Guild> guildComboBox;
    @FXML
    private Button addPlayerButton;
    @FXML
    private Label statusLabel;
    @FXML
    private TableView<PlayerActivity> activityTable;
    @FXML
    private TableView<FinancialTransaction> transactionTable;

    private GuildService guildService = new GuildService();

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        try {
            loadGuilds();
            setupCharts();
            setupAddPlayerForm();
            setupActivityAndTransactionViews();
        } catch (Exception e) {
            showErrorDialog("Błąd inicjalizacji", "Nie udało się załadować danych: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadGuilds() throws SQLException {
        guildListView.getItems().clear();
        List<Guild> guilds = guildService.getAllGuilds();
        if (guilds.isEmpty()) {
            throw new SQLException("Nie znaleziono żadnych gildii");
        }
        guilds.forEach(guild -> guildListView.getItems().add(guild.getName()));
    }

    private void setupCharts() throws SQLException {
        membersChart.getData().clear();
        fundsPieChart.getData().clear();

        NumberAxis yAxis = (NumberAxis) membersChart.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);
        yAxis.setLabel("Liczba członków");

        CategoryAxis xAxis = (CategoryAxis) membersChart.getXAxis();
        xAxis.setLabel("Gildie");

        XYChart.Series<String, Number> membersSeries = new XYChart.Series<>();
        membersSeries.setName("Liczba członków");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        List<Guild> guilds = guildService.getAllGuilds();
        if (guilds.isEmpty()) {
            throw new SQLException("Brak gildii do wyświetlenia");
        }

        for (Guild guild : guilds) {
            int memberCount = guildService.getGuildMemberCount(guild.getGuildId());
            if (memberCount > 0) {
                membersSeries.getData().add(new XYChart.Data<>(guild.getName(), memberCount));
                double funds = guildService.getGuildFunds(guild.getGuildId());
                pieChartData.add(new PieChart.Data(guild.getName(), funds));
            }
        }

        membersChart.getData().add(membersSeries);
        fundsPieChart.setData(pieChartData);

        for (XYChart.Series<String, Number> series : membersChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: #3f51b5; -fx-bar-width: 30px;");
                }
            }
        }
    }

    private void setupAddPlayerForm() throws SQLException {
        guildComboBox.setItems(FXCollections.observableArrayList(guildService.getAllGuilds()));
        classComboBox.setItems(FXCollections.observableArrayList(guildService.getAllClasses()));


        addPlayerButton.setOnAction(e -> {
            try {
                addNewPlayer();
            } catch (Exception ex) {
                showErrorDialog("Błąd", ex.getMessage());
            }
        });
    }

    private void addNewPlayer() throws SQLException {
        String username = playerNameField.getText().trim();
        Player_Class selectedClass = classComboBox.getValue();
        Guild selectedGuild = guildComboBox.getValue();

        if (username.isEmpty()) {
            throw new IllegalArgumentException("Nazwa gracza nie może być pusta");
        }
        if (selectedClass == null) {
            throw new IllegalArgumentException("Proszę wybrać klasę");
        }
        if (selectedGuild == null) {
            throw new IllegalArgumentException("Proszę wybrać gildię");
        }

        boolean success = guildService.addNewPlayer(
                username,
                selectedClass.getClassId(),
                selectedGuild.getGuildId()
        );

        if (success) {
            statusLabel.setText("Gracz " + username + " został dodany!");
            playerNameField.clear();
            refreshCharts();
            loadGuilds();
        } else {
            throw new SQLException("Błąd podczas dodawania gracza do bazy danych");
        }
    }

    private void refreshCharts() {
        try {
            setupCharts();
        } catch (SQLException e) {
            showErrorDialog("Błąd", "Nie udało się odświeżyć wykresów: " + e.getMessage());
        }
    }

    private void setupActivityAndTransactionViews() throws SQLException {
        TableColumn<PlayerActivity, String> usernameCol = new TableColumn<>("Gracz");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<PlayerActivity, String> guildCol = new TableColumn<>("Gildia");
        guildCol.setCellValueFactory(new PropertyValueFactory<>("guildName"));

        TableColumn<PlayerActivity, Integer> actionsCol = new TableColumn<>("Aktywność");
        actionsCol.setCellValueFactory(new PropertyValueFactory<>("totalActions"));

        TableColumn<PlayerActivity, LocalDateTime> lastActionCol = new TableColumn<>("Ostatnia akcja");
        lastActionCol.setCellValueFactory(new PropertyValueFactory<>("lastActionDate"));

        activityTable.getColumns().setAll(usernameCol, guildCol, actionsCol, lastActionCol);
        activityTable.setItems(guildService.getPlayerActivities());

        TableColumn<FinancialTransaction, String> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

        TableColumn<FinancialTransaction, Integer> amountCol = new TableColumn<>("Kwota");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("goldAmount"));

        TableColumn<FinancialTransaction, String> userCol = new TableColumn<>("Gracz");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<FinancialTransaction, LocalDate> dateCol = new TableColumn<>("Data");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionTable.getColumns().setAll(typeCol, amountCol, userCol, dateCol);
        transactionTable.setItems(guildService.getFinancialTransactions());
    }


    private void refreshPlayersTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/players_view.fxml"));
            loader.load();
            PlayersController playersController = loader.getController();
            playersController.refreshPlayersTable();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshPermissionsTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/manage_permissions_view.fxml"));
            loader.load();
            PermissionsController permissionsController = loader.getController();
            permissionsController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @FXML
        private void refreshAllData () {
            try {
                loadGuilds();          // Odśwież listę gildii
                setupCharts();         // Aktualizuj wykresy
                setupAddPlayerForm();  // Ponownie załaduj comboboxy (klasy i gildie)
                setupActivityAndTransactionViews(); // Odśwież tabele aktywności i transakcji
                refreshPlayersTab();
                refreshPermissionsTab();
                statusLabel.setText("Dane zostały odświeżone!");
            } catch (SQLException e) {
                showErrorDialog("Błąd odświeżania", "Nie udało się załadować danych: " + e.getMessage());
            }
        }

}