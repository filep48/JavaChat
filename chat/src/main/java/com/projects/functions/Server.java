/**
 * The `ConexionServer` class represents a server that listens on port 8100
 * and accepts incoming client connections. It reads a byte from the client,
 * sends a response, and then closes the connection.
 */
package com.projects.functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Server {

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

                // Leer la longitud del mensaje
                int mensajeLength = is.read();

                // Leer el mensaje del cliente
                byte[] mensajeBytes = new byte[mensajeLength];
                int bytesRead = is.read(mensajeBytes);

                // Convertir el mensaje en una cadena
                String mensaje = new String(mensajeBytes, 0, bytesRead);

                // Procesar el mensaje y generar una respuesta
                String respuesta = procesarMensaje(mensaje);

                // Enviar la longitud de la respuesta
                os.write(respuesta.length());

                // Enviar la respuesta al cliente
                os.write(respuesta.getBytes());

                // Cerrar la conexión con el cliente
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Función para procesar el mensaje y generar una respuesta
    private static String procesarMensaje(String mensaje) {
        // Realiza el procesamiento del mensaje aquí
        // Puede ser un proceso de autenticación, lógica de negocio, etc.

        // Ejemplo: Si el mensaje contiene "usuario:contrasena", responder con
        // "Autenticado"
        String[] partes = mensaje.split(":");
        if (partes.length == 2) {
            String usuario = partes[0];
            String contrasena = partes[1];
        }
        return "Autenticado";

    }

    public static void main(String[] args) throws IOException {
        Connection cn = null;
        PreparedStatement pst = null;

        try {

            cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "1234");
            System.out.println("Llistar grupos creados");
            functionsSQL.llistarGruposCreados(cn);
            System.out.println("Eliminar grupos creados");
            functionsSQL.EliminacionGruposBBDD(cn);
            System.out.println("Crear grupos");
            functionsSQL.creacionGruposBBDD(cn);
            functionsSQL.llistarGruposCreados(cn);
            functionsSQL.llistarUsuariosCreados(cn);
            functionsSQL.EnviarMensajesBBDD(cn, "HolaQuetal");
            System.out.println("Enviar mensajes");
            pst = functionsSQL.IniciarSession(cn);
            functionsSQL.llistarUsuariosCreados(cn);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Asegúrate de cerrar la conexión y el Statement en el bloque finally
            try {
                if (pst != null) {
                    pst.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}