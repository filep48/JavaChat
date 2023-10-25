package srv.proyecto;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import srv.proyecto.clases.Fichero;
import srv.proyecto.clases.Usuario;
import srv.proyecto.config.ConfiguracionServer;
import srv.proyecto.functions.FuncionesServer;
import srv.proyecto.functions.ControladorDescargasCliente;
import srv.proyecto.functions.ControladorFicheros;
import srv.proyecto.functions.DatabaseConnection;
import srv.proyecto.functions.FuncionesSQL;

/**
 * Servidor de la aplicación que maneja las conexiones de los clientes, procesa
 * sus solicitudes
 * y coordina la comunicación entre los usuarios.
 * Crea hilos individuales para manejar a cada cliente.
 * La comunicación entre el servidor y los clientes se realiza a través de
 * flujos de entrada y salida
 * de datos, y se utiliza una base de datos para almacenar información de
 * usuarios y grupos.
 * 
 * @author Gerard Albesa,Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */
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

            try {
                Connection cn = DatabaseConnection.getConnection();
            } catch (SQLException e) {

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
                if (e instanceof EOFException) {
                    System.out.println("El cliente cerró la conexión." + e.getMessage());
                } else {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /**
         * Procesa una solicitud del cliente y realiza la acción correspondiente.
         *
         * @param usuario El objeto `Usuario` que representa al cliente.
         * @param input   La cadena de entrada que contiene la solicitud del cliente.
         * @param writer  El flujo de salida para enviar una respuesta al cliente.
         * @param reader  El flujo de entrada para recibir datos del cliente.
         * @param nombre  El nombre del usuario.
         * @return `true` si la solicitud se procesa correctamente, `false` si hay un
         *         error.
         * @throws IOException Si ocurre un error de entrada/salida al comunicarse con
         *                     el cliente.
         * @throws SQLException
         */
        private boolean processInput(Usuario usuario, String input, DataOutputStream writer, DataInputStream reader,
                String nombre)
                throws IOException, SQLException {
            System.out.println("Procesando entrada: " + input);

            String[] mensaje = FuncionesServer.slplitMensaje(input);
            if (mensaje.length > 0) {
                String comando = mensaje[0];
                switch (comando) {
                    case "iniciarSesion":
                    case "registrarse":
                        boolean ComprobacionUsuarioCreado = FuncionesSQL.splitDatosUsuario(writer, reader, input);
                        if (ComprobacionUsuarioCreado) {
                            usuariosExistentes.putIfAbsent(nombre, usuario); // Solo añade si no existe previamente
                            usuariosConectados.put(nombre, usuario);
                            System.out.println("Usuario añadido a la lista de usuarios conectados: " + nombre);
                        }
                        break;
                    case "listarGrupos":
                        String resultado = FuncionesSQL.llistarGruposCreados(usuario);
                        writer.writeUTF(resultado);
                        break;
                    case "listarUsuarios":
                        resultado = FuncionesSQL.listarUsuariosCreados(usuario);
                        writer.writeUTF(resultado);
                        break;
                    case "listarUsuariosConectados":
                        resultado = FuncionesServer.listarUsuariosConectados();
                        writer.writeUTF(resultado);
                        break;
                    case "crearGrupo":
                        boolean resultadoBoolean = FuncionesSQL.creacionGruposBBDD(usuario, mensaje);
                        writer.writeBoolean(resultadoBoolean);
                        if (resultadoBoolean) {
                            writer.writeUTF("Grupo creado correctamente");
                        } else {
                            writer.writeUTF("Error al crear el grupo");
                        }
                        break;
                    case "enviarMensaje" :
                        FuncionesSQL.enviarMensaje(usuario, mensaje[1], mensaje[2], reader);
                        break;
                    case "listarMensajes":
                        writer.writeUTF(FuncionesSQL.listarMensajes(usuario, mensaje[1],mensaje[2], reader));
                        break;
                    case "administrarGrupo":
                        // Lógica para administrar el grupo
                        break;
                    case "eliminarGrupo":
                        writer.writeUTF(FuncionesSQL.eliminarGrupo(usuario, mensaje[1], reader));
                        break;
                    case "AñadirUsuarioAGrupo":
                        writer.writeUTF(FuncionesSQL.anadirUsuarioAGrupo(usuario, mensaje[1], mensaje[2], reader));
                        break;
                    case "listarMiembrosGrupo":
                        writer.writeUTF(FuncionesSQL.listarMiembrosGrupo(usuario, mensaje[1], reader));
                        break;
                    case "eliminarMiembro":
                        writer.writeUTF(FuncionesSQL.eliminarMiebro(usuario, mensaje[1], mensaje[2], reader));
                        break;
                    case "darseDeBaja":
                        String usuarioEliminado = usuario.getNombreUsuario();
                        boolean darseDeBaja = (FuncionesSQL.darseDeBajaUsuario(usuario));
                        if (darseDeBaja) {
                            writer.writeUTF(usuarioEliminado + " dado de baja correctamente");
                        } else {
                            writer.writeUTF("Error al dar de baja el usuario");
                        }
                        break;
                    case "enviarFichero":
                        Fichero fichero = new Fichero();
                        fichero.setNombreFichero(ControladorFicheros.obtenerNombreArchivoUnico() + mensaje[3]);
                        fichero.setRutaFichero(ConfiguracionServer.getDescargasServer());
                        fichero.setTipodeArchivo(mensaje[1]);
                        fichero.setIdPropietario(usuario.getId());
                        fichero.setIdGrupoPropietario(FuncionesSQL.obtenerIdGrupo(mensaje[2]));
                        ControladorFicheros.RecibirFicheros(clientSocket, fichero.getRutaFichero(),
                                fichero.getNombreFichero());
                        ControladorFicheros.enviarFicherosBBDD(fichero);
                        break;
                    case "descargar":
                        String ruta = ConfiguracionServer.getDescargasServer()+"\\" + mensaje[2];
                        ControladorDescargasCliente.descargar(clientSocket, ruta, writer, mensaje[1], mensaje[2]);
                        break;
                    case "cerrarSesion":
                        FuncionesServer.desconectarUsuario(nombre);
                        break;
                    default:
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
