package srv.proyecto.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import srv.proyecto.config.ConfiguracionServer;

/**
 * Esta clase proporciona una conexión a la base de datos MySQL utilizada por la
 * aplicación.
 * 
 * @author Gerard Albesa,Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */
public class DatabaseConnection {

    private static ConfiguracionServer config = new ConfiguracionServer();
    private static final String DB_URL = config.getDbUrl();
    private static final String USER = config.getUser();
    private static final String PASS = config.getPass();

    private static Connection cn;

    public static Connection getConnection() throws SQLException {
        if (cn == null || cn.isClosed()) {
            cn = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return cn;
    }
}