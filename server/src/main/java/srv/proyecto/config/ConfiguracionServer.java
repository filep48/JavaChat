package srv.proyecto.config;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfiguracionServer {

    private String dbUrl;
    private String user;
    private String pass;

    public ConfiguracionServer() {
        Properties prop = new Properties();

        // Ajusta la ruta según la ubicación real de tu archivo servidor.properties
        try (FileInputStream input = new FileInputStream(
                "C:\\Users\\Gerard\\OneDrive\\ProyectoChat\\ULTIMIMIMIMIMISIMAPRUEBA\\projectchat\\server\\src\\main\\java\\srv\\proyecto\\resources\\server.properties")) {

            prop.load(input);

            this.dbUrl = prop.getProperty("DB_URL");
            this.user = prop.getProperty("USER");
            this.pass = prop.getProperty("PASS");

        } catch (IOException ex) {
            throw new RuntimeException("Error al cargar la configuración del servidor.", ex);
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
