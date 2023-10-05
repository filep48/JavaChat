package com.projects;

import java.io.IOException;

public class clientMain {
    public static void main(String[] args) throws IOException {
        String messageResponse = ConexionClient.startCliente();
        System.out.println("Mensaje recibido del servidor: " + messageResponse);
    }
}