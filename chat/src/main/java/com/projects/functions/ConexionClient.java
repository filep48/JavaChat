/**
 * The `ConexionClient` class represents a client that connects to a server
 * running on the localhost at port 8100. It can send data to the server and
 * receive a response from it.
 */
package com.projects.functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConexionClient {

    /**
     * Starts the client and establishes a connection to the server.
     *
     * @return The response received from the server as a String.
     * @throws IOException If an I/O error occurs while communicating with the
     *                     server.
     */
    public static String startCliente() throws IOException {
        System.out.println("Iniciando cliente");
        Socket clienteSocket = new Socket("localhost", 8100); // Connect to localhost on port 8100

        InputStream input = clienteSocket.getInputStream();
        OutputStream output = clienteSocket.getOutputStream();

        byte dateToSend = 3;

        output.write(dateToSend);

        System.out.println("Enviado: " + dateToSend);

        // Read the response from the server
        byte[] buffer = new byte[1024];
        int bytesReceived = input.read(buffer);
        String messageResponse = new String(buffer, 0, bytesReceived);

        clienteSocket.close(); // Close the connection to the server

        return messageResponse; // Return the message received from the server
    }
}