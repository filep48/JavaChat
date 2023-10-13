package srv.proyecto;

import java.io.*;
import java.net.*;

import srv.proyecto.functions.functionsSQL;
import srv.proyecto.clases.DatabaseConnection;
import srv.proyecto.clases.Usuario;

public class App {
    public static void main(String[] args) {
        //SERVER
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
            System.out.println("Ejecutando hilo del cliente...");
            try (DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                 DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())) {

                while (true) {
                    String inputLine = reader.readUTF();
                    System.out.println("Recibido del cliente: " + inputLine);
                    //String response = processInput(inputLine);
                    String response = inputLine;
                    processInput(inputLine, writer, reader);
                    writer.writeUTF(response);
                }
            } catch (IOException e) {
                System.out.println("Error con el cliente: " + clientSocket.getInetAddress() + ". Error: " + e.getMessage());
            }
        }

        private void processInput(String input, DataOutputStream writer, DataInputStream reader ) throws IOException {
            System.out.println("Procesando entrada: " + input);
            try {
                switch (input) {
                    case "Inicia sesion":
                        functionsSQL.datosUsuario(DatabaseConnection.getConnection(), reader);
                        break;  
                }
            } catch (Exception e) {
                System.out.println("Error al procesar entrada: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
