package com.projects;

import com.projects.functions.functionsSQL;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;
import com.projects.functions.ConexionServer;

/**
 * The `App` class serves as the entry point for your server application. When
 * executed, it starts the `ConexionServer` to listen for incoming client
 * connections.
 */
public class App {
    public static void main(String[] args) {
        Connection cn = null;
        Statement st = null;

        try {
            // Llama a la función IniciarSession desde DatabaseManager
            st = functionsSQL.IniciarSession(cn, st);
            functionsSQL.llistarUsuariosCreados(st);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Asegúrate de cerrar la conexión y el Statement en el bloque finally
            try {
                if (st != null) {
                    st.close();
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