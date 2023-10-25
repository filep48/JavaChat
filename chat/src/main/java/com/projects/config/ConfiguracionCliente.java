package com.projects.config;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Clase que carga la configuración del cliente desde el archivo
 * "cliente.properties".
 * @author Gerard Albesa,Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */

public class ConfiguracionCliente {

    private String direccionServer;
    private int puertoServer;
    private static String direcionDescargas;
    private int tamanoMaxArchivo;

    public ConfiguracionCliente() {
        Properties prop = new Properties();

        // Utiliza FileInputStream para leer el archivo directamente desde el sistema de
        // archivos
        try (FileInputStream input = new FileInputStream(
                "chat\\src\\main\\java\\com\\projects\\resources\\cliente.properties")) {

            prop.load(input);

            this.direccionServer = prop.getProperty("DIRECIONSERVER");
            this.puertoServer = Integer.parseInt(prop.getProperty("PUERTOSERVER"));
            this.direcionDescargas = prop.getProperty("DIRECIONDESCARGAS");
            this.tamanoMaxArchivo = Integer.parseInt(prop.getProperty("TAMANOMAXARCHIVO"));

        } catch (IOException ex) {
            throw new RuntimeException("Error al cargar la configuración del cliente.", ex);
        }
    }

    public String getDireccionServer() {
        return direccionServer;
    }

    public int getPuertoServer() {
        return puertoServer;
    }

    public static String getDirecionDescargas() {
        return direcionDescargas;
    }

    public int getTamanoMaxArchivo() {
        return tamanoMaxArchivo;
    }
}
