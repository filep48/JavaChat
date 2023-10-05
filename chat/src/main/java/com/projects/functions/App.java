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
        // iniciar conexion
        Connection cn;
        Statement st;

        try {
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/fromjava", "root", "Naydler007");
            st = cn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // iniciar servidor
        functionsSQL.IniciarSession(cn, st);
    }
}
