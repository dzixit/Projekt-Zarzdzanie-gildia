package application;

import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class test {
    public static void main(String[] args) {
        // Test połączenia przed uruchomieniem FX
        try {
            Connection testConn = (Connection) DriverManager.getConnection("jdbc:mariadb://localhost:3306/projekt_gildia", "root", "");
            System.out.println("Test połączenia udany!");
            testConn.close();
        } catch (SQLException e) {
            System.err.println("Test połączenia nieudany:");
            e.printStackTrace();
            return;
        }

        launch(args);
    }
}
