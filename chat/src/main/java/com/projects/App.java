package com.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.projects.functions.functionsSQL;

/**
 * The `App` class serves as the entry point for your server application. When
 * executed, it starts the `ConexionServer` to listen for incoming client
 * connections.
 */
public class App {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/chatpro";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Naydler007";

    public static void main(String[] args) throws IOException {
        Connection cn = null;
        PreparedStatement pst = null;

        try {
            cn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            // TODO EL CODIGO AQUI
            functionsSQL.llistarUsuariosCreados(cn);
            functionsSQL.EnviarMensajesBBDD(cn, "HolaQuetal");
            pst = functionsSQL.IniciarSession(cn);
            // functionsSQL.llistarUsuariosCreados(pst);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Asegúrate de cerrar la conexión y el Statement en el bloque finally
            try {
                if (pst != null) {
                    pst.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}