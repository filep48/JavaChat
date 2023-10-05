package com.projects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConexionServer {

    public static void startServer() throws IOException {
        System.out.println("Iniciando servidor");

        ServerSocket servidorSocket = new ServerSocket(8100); // Escuchará en el puerto 8100
        Socket clienteSocket = servidorSocket.accept(); // Acepta una conexión

        InputStream entrance = clienteSocket.getInputStream();
        OutputStream exit = clienteSocket.getOutputStream();

        byte datoLeido = (byte) entrance.read();

        System.out.println("Dato leído: " + datoLeido);

        // Enviamos una respuesta al cliente junto con la cantidad de bytes recibidos
        String messageResponse = "Conexión exitosa. Bytes recibidos: " + 1;
        exit.write(messageResponse.getBytes());

        clienteSocket.close(); // Cerramos la conexión con el cliente
        servidorSocket.close(); // Cerramos el servidor
    }
}
