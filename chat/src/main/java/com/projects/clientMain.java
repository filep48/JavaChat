/**
 * The `clientMain` class serves as the entry point for the client application.
 * It connects to a server using the `ConexionClient` class and displays the
 * response received from the server.
 */
package com.projects;

import java.io.IOException;

import com.projects.functions.ConexionClient;

public class clientMain {
    /**
     * The main method of the client application. It connects to the server using
     * `ConexionClient` and prints the server's response
     */
    public static void main(String[] args) throws IOException {
        String messageResponse = ConexionClient.startCliente();
        System.out.println("Mensaje recibido del servidor: " + messageResponse);
    }
}
