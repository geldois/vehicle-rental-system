package br.edu.ifba.inf008.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL =
        "jdbc:mariadb://localhost:3307/car_rental_system";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // confirme no docker-compose.yml

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
