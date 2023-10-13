package srv.proyecto;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import srv.proyecto.functions.functionsSQL;

public class App {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/chatpro";
    private static final String USER = "root";
    private static final String PASS = "1234";

    private static Connection cn;
     
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection(DB_URL, USER, PASS);  // Inicializar la conexión

        functionsSQL.IniciarSession(cn);
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
        } finally {
            if (cn != null && !cn.isClosed()) {
                cn.close();  // Cerrar la conexión
            }
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String inputLine = reader.readUTF();
                    String response = processInput(inputLine);
                    writer.writeUTF(response);
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + clientSocket.getInetAddress());
            }
        }

        private String processInput(String input) {
            switch (input) {
                case "Inicia sesion":
                    // Aquí puedes enviar una consulta SQL a la base de datos y obtener el resultado.
                    String result = functionsSQL.llistarUsuariosCreados(cn);  // Pasar la conexión como argumento
                    return result;
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
