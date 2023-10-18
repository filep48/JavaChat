package srv.proyecto.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* public class DatabaseConnection {
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
}*/


public class DatabaseConnection {
    // Los datos de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tu_base_de_datos";
    private static final String DB_USER = "tu_usuario";
    private static final String DB_PASSWORD = "tu_contraseña";

    private static Connection connection = null;

    // Constructor privado para evitar instanciación externa
    private DatabaseConnection() {
    }

    // Método para obtener la instancia única de la conexión a la base de datos
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Establece la conexión a la base de datos
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}