package srv.proyecto;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import srv.proyecto.clases.DatabaseConnection;
import srv.proyecto.clases.Usuario;
import srv.proyecto.functions.FuncionesServer;
import srv.proyecto.functions.FuncionesSQL;

public class AppServer {
    public static HashMap<String, Usuario> usuariosConectados = new HashMap<>();
    public static HashMap<String, Usuario> usuariosExistentes = new HashMap<>();
    // public static FuncionesServer funcionesServer = new
    // FuncionesServer(usuariosConectados);

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
            // Obtener una conexión a la base de datos
            Connection cn;
            cn = null;
            try {
                cn = DatabaseConnection.getConnection();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Ejecutando hilo del cliente...");
            try (DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream())) {
                // Procesar el inicio de sesión del cliente
                String inputLine = reader.readUTF();
                System.out.println("Recibido del cliente: " + inputLine);
                // recoge el nombre del usuario
                String nombre = inputLine.split(";")[1];
                Usuario usuario = new Usuario();
                boolean inicioSesionExitoso = processInput(usuario, inputLine, writer, reader, nombre);
                usuario.setNombreUsuario(nombre);
                usuario.setId(FuncionesSQL.obtenerIdUsuario(nombre));
                usuario.setConectado(inicioSesionExitoso);
                usuariosConectados.put(nombre, usuario);
                // Fin del inicio de sesión

                // Continuar con otras solicitudes del cliente
                while ((inputLine = reader.readUTF()) != null) {
                    System.out.println("Recibido del cliente: " + inputLine);
                    boolean comandoProcesado = processInput(usuario, inputLine, writer, reader, nombre);

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
        private boolean processInput(Usuario usuario, String input, DataOutputStream writer, DataInputStream reader,
                String nombre)
                throws IOException {
            System.out.println("Procesando entrada: " + input);

            String[] mensaje = FuncionesServer.slplitMensaje(input);
            if (mensaje.length > 0) {
                String comando = mensaje[0];
                if ("iniciarSesion".equals(comando) || "registrarse".equals(comando)) {
                    FuncionesSQL.splitDatosUsuario(writer, reader, input);
                    usuariosExistentes.putIfAbsent(nombre, usuario); // Solo añade si no existe previamente
                    usuariosConectados.put(nombre, usuario);
                } else if ("listarGrupos".equals(comando)) {
                    String resultado = FuncionesSQL.llistarGruposCreados(usuario);
                    writer.writeUTF(resultado);
                } else if ("listarUsuarios".equals(comando)) {
                    String resultado = FuncionesSQL.listarUsuariosCreados(usuario);
                    writer.writeUTF(resultado);
                } else if ("listarUsuariosConectados".equals(comando)) {
                    String resultado = FuncionesServer.listarUsuariosConectados();
                    writer.writeUTF(resultado);
                } else if ("crearGrupo".equals(comando)) {
                    boolean resultado = FuncionesSQL.creacionGruposBBDD(usuario, mensaje, reader);
                    writer.writeBoolean(resultado);
                    if (resultado) {
                        writer.writeUTF("Grupo creado correctamente");
                    } else {
                        writer.writeUTF("Error al crear el grupo");
                    }
                } else if ("administrarGrupo".equals(comando)) {

                }else if ("eliminarGrupo".equals(comando)){
                    writer.writeUTF(FuncionesSQL.eliminarGrupo(usuario, mensaje[1], reader));
                } else if ("AñadirUsuarioAGrupo".equals(comando)) {
                    writer.writeUTF(FuncionesSQL.anadirUsuarioAGrupo(usuario, mensaje[1], mensaje[2], reader));
                } else if ("listarMiembrosGrupo".equals(comando)){
                    writer.writeUTF(FuncionesSQL.listarMiembrosGrupo(usuario, mensaje[1], reader));
                }else if ("eliminarMiembro".equals(comando)){
                    writer.writeUTF(FuncionesSQL.eliminarMiebro(usuario, mensaje[1], mensaje[2], reader)); 
                }else if ("cerrarSesion".equals(comando)) {
                    FuncionesServer.desconectarUsuario(nombre);
                    String resultado = "cerrarSesion";
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
