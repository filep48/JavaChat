/**
 * The `ConexionClient` class represents a client that connects to a server
 * running on the localhost at port 8100. It can send data to the server and
 * receive a response from it.
 */
package srv.proyecto.functions;

import java.io.BufferedReader;
import java.io.DataInputStream;
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

import srv.proyecto.clases.DatabaseConnection;
import srv.proyecto.clases.Usuario;

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
            Connection cn = DatabaseConnection.getConnection();
        ) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(inputLine);
                String nombre = tokenizer.nextToken();
                String contrasena = tokenizer.nextToken();

                if (validarCredenciales(cn, nombre, contrasena)) {
                    writer.println("Acceso concedido");
                } else {
                    writer.println("Acceso denegado");
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validarCredenciales(Connection cn, String nombre, String contrasena) throws SQLException {
        Usuario datos = functionsSQL.datosUsuario(cn, nombre, contrasena);
        if (datos != null) {
            String query = "SELECT COL1,COL2 FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            try (PreparedStatement preparedStatement = cn.prepareStatement(query)) {
                preparedStatement.setString(1, datos.getNombreUsuarioo());
                preparedStatement.setString(2, datos.getContrasena());
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        }
        return false;
    }
}