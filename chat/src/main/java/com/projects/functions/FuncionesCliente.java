/**
 * The `ConexionClient` class represents a client that connects to a server
 * running on the localhost at port 8100. It can send data to the server and
 * receive a response from it.
 */
package com.projects.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.projects.clases.Usuario;

public class FuncionesCliente implements Runnable {
    private final Socket clientSocket;

    public FuncionesCliente(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(inputLine);
                String nombre = tokenizer.nextToken();
                String contrasena = tokenizer.nextToken();

                if (validarCredenciales()) {
                    writer.println("Acceso concedido");
                } else {
                    writer.println("Acceso denegado");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/* ----------------------- antigua de gerad-----------
    private boolean validarCredenciales(String nombre, String contrasena) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatpro", "root", "troll")) {
            String query = "SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, contrasena);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
*/
    /* ----------------------- modificacion de la original donde se usa la entrada de datos-----------
     * --- he realizado las siguientes modificaciones :
     * 1.se cambia array por objeto Usuario.
     * 2.se cambia la query
    */

    /**funcion que conecta y valida si el usuario existe o no en la bbdd y le envia el resultado a la funcion run(); */
    
    public static boolean validarCredenciales() {
        Usuario datos = functionsSQL.datosUsuario();
        
        if (datos != null) {
            String nombreUsuario = datos.getNombreUsuarioo();
            String contrasenaUsuario = datos.getContrasena();

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "troll")) {
                String query = "SELECT COL1,COL2 FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, nombreUsuario);
                    preparedStatement.setString(2, contrasenaUsuario);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        return false;
    }

    //Crear la funcion enviar un mensaje al servidor, le mensaje tiene que mandar el id del usuario y el mensaje

}
