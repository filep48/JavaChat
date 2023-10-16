package srv.proyecto;

import java.io.*;
import java.net.*;
import java.sql.Connection;

import srv.proyecto.clases.ControladorArchivos;
import srv.proyecto.clases.DatabaseConnection;
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
        private Connection cn;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

        }

        public ClientHandler(Socket socket, Connection dbConnection) {
            this.clientSocket = socket;
            this.cn = dbConnection; 
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

                    // ------------------------ Continuar con otras solicitudes del cliente
                }
            } catch (IOException e) {
                System.out.println(
                        "Error con el cliente: " + clientSocket.getInetAddress() + ". Error: " + e.getMessage());
            }
        }

        /**
         * Procesa una entrada del cliente para realizar el inicio de sesión o el
         * registro.
         *
         * @param input  La cadena de entrada que viene del menú del cliente en el
         *               formato "comando;nombreUsuario;contrasena".
         * @param writer El flujo de salida para enviar una respuesta al cliente.
         * @param reader El flujo de entrada para recibir datos del cliente.
         * @return `true` si el inicio de sesión o el registro es exitoso, `false` si
         *         falla.
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
                } else if ("registrarse".equals(comando)) {
                    
                    String nombreUsuario = partes[1];
                    String contrasena = partes[2];

                    // Llamar a darAltaUsuario para registrar al usuario en la base de datos
                    boolean registroExitoso = functionsSQL.darAltaUsuario(cn, nombreUsuario, contrasena);
                    int usuarioid = functionsSQL.obtenerIdUsuario(cn, nombreUsuario);
                    ControladorArchivos.crearCarpetaServidor(cn, usuarioid);

                    // Enviar una respuesta al cliente
                    if (registroExitoso) {
                        writer.writeBoolean(true);
                        
                    } else {
                        writer.writeBoolean(false);
                    }
                } else {
                    System.out.println("Comando desconocido: " + comando);
                    return false;
                }
            } else {
                System.out.println("Mensaje de inicio de sesión o registro incorrecto.");
                return false;
            }
            return true;
        }

    }
}
