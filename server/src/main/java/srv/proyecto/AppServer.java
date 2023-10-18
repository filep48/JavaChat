package srv.proyecto;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import srv.proyecto.clases.DatabaseConnection;
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
        private String nombre;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            // Obtener una conexión a la base de datos una sola vez al inicio del hilo
            Connection cn = null;
            try {
                cn = DatabaseConnection.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                return; // Salir del hilo si no se pudo obtener la conexión a la base de datos
            }

            System.out.println("Ejecutando hilo del cliente...");
            try (DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())) {
                String inputLine;

                // Continuar con solicitudes del cliente mientras no se cierre la conexión
                while ((inputLine = reader.readUTF()) != null) {
                    System.out.println("Recibido del cliente: " + inputLine);
                    String[] partes = inputLine.split(";");
                    if (partes.length > 0) {
                        String comando = partes[0];
                        boolean comandoProcesado = false;

                        // Procesar comandos
                        // Procesar comandos
                        if ("iniciarSesion".equals(comando) || "registrarse".equals(comando)) {
                            Usuario usuario = functionsSQL.splitDatosUsuario(writer, reader, inputLine);
                            if (usuario != null) {
                                // AQUI METEMOS AL HASMAP LO Q QUERAMOS
                                usuariosConectados.put(nombre, usuario);
                                writer.writeUTF("Inicio de sesión exitoso.");
                            } else {
                                writer.writeUTF("Error al iniciar sesión. ¿Quieres registrarte?");
                            }
                        } else if ("listarGrupos".equals(comando)) {
                            String resultado = functionsSQL.llistarGruposCreados();
                            writer.writeUTF(resultado);
                            comandoProcesado = true;
                        } else if ("listarUsuarios".equals(comando)) {
                            String resultado = functionsSQL.llistarUsuariosCreados();
                            writer.writeUTF(resultado);
                            comandoProcesado = true;
                        } else if ("listarUsuariosConectados".equals(comando)) {
                            String resultado = funcionesServer.listarUsuariosConectados();
                            writer.writeUTF(resultado);
                            comandoProcesado = true;
                        } else if ("eliminarGrupo".equals(comando)) {
                            String resultado = functionsSQL.eliminarGrupo(cn, inputLine);
                            if (resultado != null) {
                                writer.writeUTF(resultado);
                            }
                            comandoProcesado = true;
                        } else if ("CerrarSession".equals(comando)) {
                            FuncionesServer.desconectarUsuario(nombre);
                            writer.writeUTF("CerrarSession");
                            comandoProcesado = true;
                        }

                        if (!comandoProcesado) {
                            writer.writeUTF("Comando no reconocido o error en el procesamiento.");
                        }
                    } else {
                        System.out.println("Mensaje de inicio de sesión incorrecto.");
                        writer.writeUTF("Mensaje de inicio de sesión incorrecto.");
                    }
                }
            } catch (IOException e) {
                System.out.println(
                        "Error con el cliente: " + clientSocket.getInetAddress() + ". Error: " + e.getMessage());
            } finally {
                // Cerrar la conexión y eliminar el usuario de la lista cuando el cliente se
                // desconecte
                Usuario usuarioDesconectado = usuariosConectados.remove(nombre);
                if (usuarioDesconectado != null) {
                    System.out.println("Cliente desconectado: " + nombre);
                }
            }
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
    private boolean processInput(Connection cn, String input, DataOutputStream writer, DataInputStream reader,
            String nombre)
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
            } else if ("eliminarGrupo".equals(comando)) {
                String resultado = functionsSQL.eliminarGrupo(cn, input);
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
