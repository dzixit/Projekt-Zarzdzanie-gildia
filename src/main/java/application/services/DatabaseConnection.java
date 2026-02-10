package application.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        reconnect();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnect();
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania połączenia:");
            e.printStackTrace();
        }
        return connection;
    }

    private void reconnect() {
        try {

            String url = "jdbc:mariadb://localhost:3306/projekt_gildia" +
                    "?autoReconnect=true" +
                    "&useSSL=false" +
                    "&maxReconnects=10";
            this.connection = DriverManager.getConnection(url, "root", "");
            System.out.println("Połączenie z bazą danych nawiązane!");
        } catch (SQLException e) {
            System.err.println("Błąd połączenia z bazą danych:");
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to database", e);
        }
    }


}
