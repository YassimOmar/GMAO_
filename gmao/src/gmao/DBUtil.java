package gmao;

//package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe utilitaire pour gérer la connexion à la base de données Oracle
public class DBUtil {
    // URL de connexion JDBC
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // Mettez à jour l'URL si nécessaire
    // Nom d'utilisateur de la base de données
    private static final String DB_USER = "gmao";
    // Mot de passe de la base de données
    private static final String DB_PASSWORD = "ic2024";

    // Méthode pour obtenir une connexion à la base de données
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
}

