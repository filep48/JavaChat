/**
 * The `ConexionServer` class represents a server that listens on port 8100
 * and accepts incoming client connections. It reads a byte from the client,
 * sends a response, and then closes the connection.
 */
package com.projects.functions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Inicia servidor");

        ServerSocket serverSocket = new ServerSocket(8100); // Escuchará en el puerto 8100
        int n = 5;

        while (n > 0) {
            Socket clientSocket = serverSocket.accept(); // Acepta una conexión entrante
            StartServer serverThread = new StartServer(clientSocket);
            serverThread.start();
            n--;
        }

        // Cerramos el socket del servidor cuando hayamos terminado
        serverSocket.close();
    }

    public static class StartServer extends Thread {
        private Socket clientSocket;

        StartServer(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            InputStream is;
            OutputStream os;

            try {
                is = clientSocket.getInputStream();
                os = clientSocket.getOutputStream();

                String mensaje = FuncionesServer.leerMensaje(is);
                for (int i = 0; i < 10; i++) {
                    byte leido = (byte) is.read();
                    System.out.println(
                            "Leído " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " -> " + leido);
                }

                clientSocket.close(); // Cerramos la conexión con el cliente

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}