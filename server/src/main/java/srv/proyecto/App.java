package srv.proyecto;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import srv.proyecto.functions.functionsSQL;
import srv.proyecto.clases.DatabaseConnection;

public class App {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/chatpro";
    private static final String USER = "root";
    private static final String PASS = "1234";

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Inicia un hilo para manejar al cliente
                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())) {

                while (true) {
                    String inputLine = reader.readUTF();
                    String response = processInput(inputLine);
                    writer.writeUTF(response);
                }
            } catch (IOException | SQLException e) {
                System.out.println("Error con el cliente: " + clientSocket.getInetAddress() + ". Error: " + e.getMessage());
            }
        }

        private String processInput(String input) throws SQLException {
            switch (input) {
                case "Inicia sesion":
                    return functionsSQL.llistarUsuariosCreados(DatabaseConnection.getConnection());
                case "registrate":
                    // Lógica para registrar en la base de datos
                    return "Registro exitoso"; // O cualquier otra respuesta
                case "Salir":
                    // Cerrar la conexión y salir del programa
                    return "Hasta luego";
                default:
                    return "Comando no reconocido";
            }
        }
    }
}