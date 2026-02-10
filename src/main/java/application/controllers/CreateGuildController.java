package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import application.services.GuildService;
import java.sql.SQLException;

public class CreateGuildController {
    @FXML private TextField guildNameField;
    @FXML private Label statusLabel;

    private GuildService guildService = new GuildService();

    @FXML
    private void createGuild() {
        String guildName = guildNameField.getText().trim();

        if (guildName.isEmpty()) {
            statusLabel.setText("Nazwa gildii nie może być pusta!");
            return;
        }

        try {
            boolean success = guildService.addGuild(guildName);
            if (success) {
                statusLabel.setText("Gildia " + guildName + " utworzona!");
                guildNameField.clear();
            }
        } catch (SQLException e) {
            statusLabel.setText("Błąd bazy danych: " + e.getMessage());
        }
    }
}