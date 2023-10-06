package com.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.projects.functions.functionsSQL;



/**
 * The `App` class serves as the entry point for your server application. When
 * executed, it starts the `ConexionServer` to listen for incoming client
 * connections.
 */
public class App {
    public static void main(String[] args) throws IOException {
        Connection cn = null;
        PreparedStatement pst = null;

        try {
            //TODO EL CODIGO AQUI

            pst = functionsSQL.IniciarSession(cn);
            functionsSQL.llistarUsuariosCreados(pst);

    
            
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