/**
 * The `clientMain` class serves as the entry point for the client application.
 * It connects to a server using the `ConexionClient` class and displays the
 * response received from the server.
 */
package com.projects;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class clientMain {

    public static void main(String[] args) throws IOException {
        System.out.println("Inicia cliente");

        Socket sk = new Socket("localhost", 8100); // Conectar al servidor

        InputStream is = sk.getInputStream();
        OutputStream os = sk.getOutputStream();

        // Envía el nombre de usuario y contraseña al servidor
        String usuario = "nombre_de_usuario";
        String contrasena = "contrasena_secreta";

        // Construye un mensaje que incluye el nombre de usuario y la contraseña
        String mensaje = usuario + ":" + contrasena;

        // Convierte el mensaje en un array de bytes
        byte[] mensajeBytes = mensaje.getBytes();

        // Envía la longitud del mensaje
        os.write(mensajeBytes.length);

        // Envía el mensaje
        os.write(mensajeBytes);

        System.out.println("Enviado usuario y contraseña: " + usuario);

        // Espera la respuesta del servidor
        int respuestaLength = is.read(); // Lee la longitud de la respuesta

        byte[] respuestaBytes = new byte[respuestaLength];

        // Lee la respuesta del servidor
        int bytesRead = is.read(respuestaBytes);

        String respuesta = new String(respuestaBytes, 0, bytesRead);
        System.out.println("Respuesta del servidor: " + respuesta);

        sk.close(); // Cierra la conexión
    }
}
