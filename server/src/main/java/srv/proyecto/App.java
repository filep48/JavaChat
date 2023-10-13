package srv.proyecto;

import java.io.*;
import java.net.*;
import srv.proyecto.functions.*;

public class App {
    // SERVER
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
// añado un try catch 
        private String processInput(String input) {
            try {
                switch (input) {
                    case "Inicia sesion":
                        return "Inicia sesion";
                        functionsSQL.datosUsuario(cn);
                    case "registrate":
                        return "registrate";
                    case "Salir":
                        return "Salir";
                    default:
                        return "Comando no reconocido";
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }
}
