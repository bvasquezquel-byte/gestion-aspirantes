package edu.ingsoft.bvasquez.quel.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/reclutamiento_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "BA08262009vq"; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver de MySQL no encontrado en el classpath.");
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("=== ERROR REAL DE CONEXIÓN A LA BASE DE DATOS ===");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Código: " + e.getErrorCode());
            e.printStackTrace();
            throw e;
        }
    }
}