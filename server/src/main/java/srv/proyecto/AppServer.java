package srv.proyecto;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import srv.proyecto.clases.Usuario;
import srv.proyecto.functions.FuncionesServer;
import srv.proyecto.functions.functionsSQL;

public class AppServer {
    public static HashMap<String, Usuario> usuariosConectados = new HashMap<>();
    public static FuncionesServer funcionesServer = new FuncionesServer(usuariosConectados);

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
                // Procesar el inicio de sesión del cliente
                String inputLine = reader.readUTF();
                System.out.println("Recibido del cliente: " + inputLine);
                // recoge el nombre del usuario
                String nombre = inputLine.split(";")[1];
                boolean inicioSesionExitoso = processInput(inputLine, writer, reader, nombre);
                if (inicioSesionExitoso) {
                    writer.writeUTF("Inicio de sesión exitoso.");
                } else {
                    writer.writeUTF("Error al iniciar sesión. ¿Quieres registrarte?");
                }
                // Fin del inicio de sesión

                // Continuar con otras solicitudes del cliente
                while (true) {
                    inputLine = reader.readUTF();
                    System.out.println("Recibido del cliente: " + inputLine);
                    boolean comandoProcesado = processInput(inputLine, writer, reader, nombre);

                    if (!comandoProcesado) {
                        writer.writeUTF("Comando no reconocido o error en el procesamiento.");
                    }
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
        private boolean processInput(String input, DataOutputStream writer, DataInputStream reader, String nombre)
                throws IOException {
            System.out.println("Procesando entrada: " + input);

            String[] partes = input.split(";");
            if (partes.length > 0) {
                String comando = partes[0];
                if ("iniciarSesion".equals(comando)) {
                    functionsSQL.splitDatosUsuario(writer, reader, input);
                } else if ("registrarse".equals(comando)) {
                    functionsSQL.splitDatosUsuario(writer, reader, input);
                } else if ("listarGrupos".equals(comando)) {
                    String resultado = functionsSQL.llistarGruposCreados();
                    writer.writeUTF(resultado);
                } else if ("listarUsuarios".equals(comando)) {
                    String resultado = functionsSQL.llistarUsuariosCreados();
                    writer.writeUTF(resultado);
                } else if ("listarUsuariosConectados".equals(comando)) {
                    String resultado = funcionesServer.listarUsuariosConectados();
                    writer.writeUTF(resultado);
                } else if ("CerrarSession".equals(comando)) {
                    FuncionesServer.desconectarUsuario(nombre);
                    String resultado = "CerrarSession";
                    writer.writeUTF(resultado);
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
