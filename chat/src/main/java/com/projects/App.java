package com.projects;

import java.io.IOException;

import com.projects.functions.ConexionServer;

/**
 * The `App` class serves as the entry point for your server application. When
 * executed, it starts the `ConexionServer` to listen for incoming client
 * connections.
 */
public class App {
    /**
     * The main method of the server application. It starts the `ConexionServer` to
     * listen for incoming client connections.
     */
    public static void main(String[] args) throws IOException {
        ConexionServer.startServer();
    }
}
