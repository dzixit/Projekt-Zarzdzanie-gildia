package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import application.services.GuildService;
import java.sql.SQLException;

public class CreateClassController {
    @FXML private TextField classNameField;
    @FXML private Label statusLabel;

    private GuildService guildService = new GuildService();

    @FXML
    private void createClass() {
        String className = classNameField.getText().trim();

        if (className.isEmpty()) {
            statusLabel.setText("Nazwa klasy nie może być pusta!");
            return;
        }

        try {
            boolean success = guildService.addPlayerClass(className);
            if (success) {
                statusLabel.setText("Klasa " + className + " dodana!");
                classNameField.clear();
            }
        } catch (SQLException e) {
            statusLabel.setText("Błąd bazy danych: " + e.getMessage());
        }
    }
}