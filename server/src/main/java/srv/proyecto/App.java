package srv.proyecto;

import java.io.*;
import java.net.*;

import srv.proyecto.functions.functionsSQL;

public class App {
    public static void main(String[] args) {
        // SERVER
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Inicia un hilo para manejar al cliente
                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            System.out.println("Ejecutando hilo del cliente...");
            try (DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())) {

                while (true) {
                    String inputLine = reader.readUTF();
                    System.out.println("Recibido del cliente: " + inputLine);

                    boolean inicioSesionExitoso = processInput(inputLine, writer, reader);

                    if (inicioSesionExitoso) {
                        writer.writeUTF("Inicio de sesión exitoso. ¡Bienvenido!");
                    } else {
                        writer.writeUTF("Error al iniciar sesión. ¿Quieres registrarte?");
                    }

                    //------------------------ Continuar con otras solicitudes del cliente
                }
            } catch (IOException e) {
                System.out.println(
                        "Error con el cliente: " + clientSocket.getInetAddress() + ". Error: " + e.getMessage());
            }
        }

        /**
         * Procesa una entrada del cliente para realizar el inicio de sesión.
         *
         * @param input  La cadena de entrada que viene del menu del cliente
         *               En el formato
         *               "iniciarSesion;nombreUsuario;contrasena".
         * @param writer El flujo de salida para enviar una respuesta al cliente.
         * @param reader El flujo de entrada para recibir datos del cliente.
         * @return `true` si el inicio de sesión es exitoso, `false` si falla.
         * @throws IOException Si ocurre un error de entrada/salida al comunicarse con
         *                     el cliente.
         */
        private boolean processInput(String input, DataOutputStream writer, DataInputStream reader) throws IOException {
            System.out.println("Procesando entrada: " + input);
        
            String[] partes = input.split(";");
            if (partes.length > 0) {
                String comando = partes[0];
        
                if ("iniciarSesion".equals(comando)) {
                    functionsSQL.splitDatosUsuario(writer, reader, input);
                } else {
                    System.out.println("Comando desconocido: " + comando);
                    return false;
                }
            } else {
                System.out.println("Mensaje de inicio de sesión incorrecto.");
                return false;
            }
            return true;
        }

    }
}
