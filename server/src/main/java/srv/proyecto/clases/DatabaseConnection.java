package srv.proyecto.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/chatpro";
    private static final String USER = "root";
    private static final String PASS = "1234";

    private static Connection cn;

    public static Connection getConnection() throws SQLException {
        if (cn == null || cn.isClosed()) {
            cn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return cn;
    }
}