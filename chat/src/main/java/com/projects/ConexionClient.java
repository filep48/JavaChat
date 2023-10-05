package com.projects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConexionClient {

    public static String startCliente() throws IOException {
        System.out.println("Iniciando cliente");
        Socket clienteSocket = new Socket("localhost", 8100); // Conectar a localhost en el puerto 8100

        InputStream entrance = clienteSocket.getInputStream();
        OutputStream exit = clienteSocket.getOutputStream();

        byte datoAEnviar = 3;

        exit.write(datoAEnviar);

        System.out.println("Enviado: " + datoAEnviar);

        // Leer la respuesta del servidor
        byte[] buffer = new byte[1024];
        int bytesRecibidos = entrance.read(buffer);
        String mensajeRespuesta = new String(buffer, 0, bytesRecibidos);

        clienteSocket.close(); // Cerrar la conexión con el servidor

        return mensajeRespuesta; // Devolver el mensaje recibido del servidor
    }
}
