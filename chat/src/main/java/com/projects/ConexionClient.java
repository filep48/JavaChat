package com.projects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConexionClient {

    public static String startCliente() throws IOException {
        System.out.println("Iniciando cliente");
        Socket clienteSocket = new Socket("localhost", 8100); // Conectar a localhost en el puerto 8100

        InputStream input = clienteSocket.getInputStream();
        OutputStream output = clienteSocket.getOutputStream();

        byte dateToSend = 3;

        output.write(dateToSend);

        System.out.println("Enviado: " + dateToSend);

        // Leer la respuesta del servidor
        byte[] buffer = new byte[1024];
        int bytesReceived = input.read(buffer);
        String messageResponse = new String(buffer, 0, bytesReceived);

        clienteSocket.close(); // Cerrar la conexión con el servidor

        return messageResponse; // Devolver el mensaje recibido del servidor
    }
}
