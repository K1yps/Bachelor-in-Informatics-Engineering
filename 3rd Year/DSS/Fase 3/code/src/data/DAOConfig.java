package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOConfig {
    static final String USERNAME = "user";                       // Actualizar
    static final String PASSWORD = "user";                       // Actualizar
    private static final String DATABASE = "armazem?autoReconnect=true&useSSL=false";          // Actualizar
    //private static final String DRIVER = "jdbc:mariadb";        // Usar para MariaDB
    private static final String DRIVER = "jdbc:mysql";        // Usar para MySQL
    private static final String URL = DRIVER + "://localhost:3306/" + DATABASE;

    private static Connection connection;

    public static void startConnection(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url + "&user=" + user + "&password=" + password);
        } catch (SQLException e) {
        }
    }

    public static Connection getConnection() {
        if (connection == null) DAOConfig.startConnection(URL, USERNAME, PASSWORD);
        return connection;
    }
}

