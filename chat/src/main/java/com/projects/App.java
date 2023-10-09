package com.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.projects.clases.Usuario;
import com.projects.functions.FuncionesServer;
import com.projects.functions.functionsSQL;

/**
 * The `App` class serves as the entry point for your server application. When
 * executed, it starts the `ConexionServer` to listen for incoming client
 * connections.
 */
public class App {
    public static final String JDBC_URL_STRING = "jdbc:mysql://localhost:3307/chatpro";
    public static final String JDBC_URL_USER= "root";
    public static final String JDBC_URL_PASSWORD ="troll";
    public static void main(String[] args) throws IOException {
        Connection cn = null;
        PreparedStatement pst = null;

        try {
            cn = DriverManager.getConnection(JDBC_URL_STRING,JDBC_URL_USER,JDBC_URL_PASSWORD);
            // TODO EL CODIGO AQUI
            functionsSQL.llistarUsuariosCreados(cn);
            functionsSQL.EnviarMensajesBBDD(cn, "HolnQuetal");
            pst = functionsSQL.IniciarSession(cn);
            Usuario datos = functionsSQL.datosUsuario();
            functionsSQL.consultaBbddUsuarioExiste(datos, cn);
            //functionsSQL.llistarUsuariosCreados(cn);


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
