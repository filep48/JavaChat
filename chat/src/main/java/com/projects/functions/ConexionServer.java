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

public class ConexionServer {

    /**
     * Starts the server and listens for incoming client connections. When a
     * connection is accepted, it reads a byte from the client, sends a response,
     * and then closes the connection.
     *
     */
    public static void startServer() throws IOException {
        System.out.println("Iniciando servidor");

        ServerSocket servidorSocket = new ServerSocket(8100); // Listen on port 8100
        Socket clienteSocket = servidorSocket.accept(); // Accept a connection

        InputStream input = clienteSocket.getInputStream();
        OutputStream output = clienteSocket.getOutputStream();

        byte dateRead = (byte) input.read();

        System.out.println("Dato leído: " + dateRead);

        // Send a response to the client along with the number of bytes received
        String messageResponse = "Conexión exitosa. Bytes recibidos: " + 1;
        output.write(messageResponse.getBytes());

        clienteSocket.close(); // Close the connection with the client
        servidorSocket.close(); // Close the server
    }
}
