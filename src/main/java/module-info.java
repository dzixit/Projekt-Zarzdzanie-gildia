module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mariadb.jdbc;

    opens application to javafx.fxml;
    opens application.controllers to javafx.fxml;
    opens application.services to javafx.fxml;

    exports application;
    exports application.controllers;
    exports application.services;
    exports application.models;
    opens application.models to javafx.fxml;
}
