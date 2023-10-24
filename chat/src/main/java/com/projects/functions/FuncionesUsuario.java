package com.projects.functions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import com.projects.AppCliente;
import com.projects.CrearConexionCliente;

/**
 * Clase que contiene funciones para interactuar con el servidor y realizar
 * operaciones
 * relacionadas con usuarios y grupos.
 * 
 * @author Gerard Albesa,Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */
public class FuncionesUsuario {

    /**
     * Registra a un usuario en el servidor utilizando un socket para comunicación.
     *
     * Esta función toma un nombre de usuario y una contraseña del usuario y los
     * envía al servidor para su registro.
     * Luego, verifica la respuesta del servidor y muestra un
     * mensaje de éxito o error.
     *
     * @param writer El flujo de salida de datos utilizado para enviar mensajes al
     *               servidor.
     * @param reader El flujo de entrada de datos utilizado para recibir respuestas
     *               del servidor.
     * @param socket El socket que se utiliza para la comunicación con el servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */

    public static void registrarse(DataOutputStream writer, DataInputStream reader, Socket socket) throws IOException {
        boolean contrasenaValida = false;
        while (!contrasenaValida) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce tu nombre de usuario deseado: ");
            String nombreUsuario = scanner.next();
            System.out.println("(La contraseña debe ser de al menos 6 caracteres y maximo 32 caracteres)");
            System.out.print("Introduce tu contraseña deseada: ");
            String contrasena = new String(System.console().readPassword());
            String mensaje = "registrarse;" + nombreUsuario + ";" + contrasena;
            writer.writeUTF(mensaje);
            boolean registroExitoso = reader.readBoolean();
            if (registroExitoso) {
                System.out.println("Registro exitoso.");
                contrasenaValida = true;
                AppCliente.menuSesionIniciada(nombreUsuario, scanner, writer, reader, socket);
            } else {
                System.out.println(
                        "Error al registrarse. El nombre o la contraseña no son. Inténtalo de nuevo.");
            }
        }
    }

    /**
     * Inicia sesión en el servidor utilizando un socket para comunicación con
     * nombre y contraseña.
     * Luego, verifica la respuesta del servidor y muestra un
     * mensaje de éxito o error.
     * 
     * @param writer El flujo de salida de datos utilizado para enviar mensajes al
     *               servidor.
     * @param reader El flujo de entrada de datos utilizado para recibir respuestas
     *               del servidor.
     * @param socket El socket que se utiliza para la comunicación con el servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void iniciarSesion(DataOutputStream writer, DataInputStream reader, Socket socket)
            throws IOException {
        boolean contrasenaValida = false;
        while (!contrasenaValida) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce tu nombre de usuario: ");
            String nombreUsuario = scanner.next();
            System.out.print("Introduce tu contraseña: ");
            String contrasena = new String(System.console().readPassword());
            String mensaje = "iniciarSesion;" + nombreUsuario + ";" + contrasena;
            writer.writeUTF(mensaje);
            boolean inicioSesionCorrecto = reader.readBoolean();
            if (inicioSesionCorrecto) {
                System.out.println("Inicio de sesión exitoso.");
                if (!socket.isConnected()) {
                    CrearConexionCliente.iniciarCliente();
                } else {
                    contrasenaValida = true;
                    AppCliente.menuSesionIniciada(nombreUsuario, scanner, writer, reader, socket);
                }
            } else {
                System.out.println("Error al iniciar sesión. Inténtalo de nuevo.");
            }
        }
    }

    /**
     * Crea un grupo en el servidor utilizando un socket para comunicación.
     * Permite al usuario crear un nuevo grupo especificando su nombre. Luego, envía
     * una solicitud al servidor para crear el grupo y muestra un mensaje de éxito o
     * error según la respuesta del servidor.
     * del servidor.
     * 
     * @param writer El flujo de salida de datos utilizado para enviar mensajes al
     *               servidor.
     * @param reader El flujo de entrada de datos utilizado para recibir respuestas
     *               del servidor.
     * @param socket El socket que se utiliza para la comunicación con el servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void creacionGrupo(DataOutputStream writer, DataInputStream reader, Socket socket)
            throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del grupo: ");
        String nombreGrupo = scanner.next();
        String mensaje = "crearGrupo;" + nombreGrupo;
        writer.writeUTF(mensaje);
        boolean creacionGrupoCorrecto = reader.readBoolean();
        System.out.println(reader.readUTF());
        if (creacionGrupoCorrecto) {
            System.out.println("Creación de grupo exitosa.");
            AppCliente.menuSesionIniciada(nombreGrupo, scanner, writer, reader, socket);
        } else {
            System.out.println("Error al crear el grupo. Inténtalo de nuevo.");
        }
    }

    /**
     * Elimina un grupo específico en el servidor utilizando un socket para
     * comunicación.
     *
     * Esta función permite al usuario eliminar un grupo existente especificando su
     * nombre. Luego, envía
     * una solicitud al servidor para eliminar el grupo y muestra la respuesta del
     * servidor.
     *
     * @param nombreGrupo El nombre del grupo que se desea eliminar.
     * @param writer      El flujo de salida de datos utilizado para enviar mensajes
     *                    al servidor.
     * @param reader      El flujo de entrada de datos utilizado para recibir
     *                    respuestas del servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void eliminarGrupo(String nombreGrupo, DataOutputStream writer, DataInputStream reader)
            throws IOException {

        String mensaje = "eliminarGrupo;" + nombreGrupo;
        writer.writeUTF(mensaje);
        System.out.println(reader.readUTF());

    }

    /**
     * Obtiene la lista de usuarios registrados en el servidor y la muestra por
     * pantalla.
     *
     * Esta función envía una solicitud al servidor para obtener la lista de
     * usuarios registrados
     * y luego muestra la lista por pantalla. Cada usuario en la lista se muestra en
     * una nueva línea.
     *
     * @param writer El flujo de salida de datos utilizado para enviar mensajes al
     *               servidor.
     * @param reader El flujo de entrada de datos utilizado para recibir la lista de
     *               usuarios desde el servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void listarUsuarios(DataOutputStream writer, DataInputStream reader) throws IOException {
        String mensaje = "listarUsuarios";
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();
        System.out.println("Llista de usuarios: "
                + "\n" + serverResponse);
    }

    /**
     * Envía un mensaje a un grupo específico en el servidor utilizando un socket
     * para comunicación.
     *
     * Esta función permite al usuario especificar el nombre del grupo al que desea
     * enviar un mensaje.
     * Luego, envía una solicitud al servidor para llevar a cabo esta acción y
     * muestra la respuesta del servidor.
     *
     * @param nombreUsuario El nombre del usuario que desea enviar el mensaje.
     * @param writer        El flujo de salida de datos utilizado para enviar
     *                      mensajes al servidor.
     * @param reader        El flujo de entrada de datos utilizado para recibir
     *                      respuestas del servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void enviarMensaje(String nombreGrupo, String nombreUsuario, DataOutputStream writer,
            DataInputStream reader, Socket socket)
            throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce el mensaje que quieres enviar al chat " + nombreGrupo + ": ");
        String mensajeChat = scanner.next();
        String mensaje = "enviarMensaje;" + nombreGrupo + ";" + mensajeChat;
        writer.writeUTF(mensaje);
        if (reader.readBoolean()) {
            System.out.println("Mensaje enviado correctamente. Quieres enviar otro mensaje? (S/N)");
            String respuesta = scanner.next();
            if (respuesta.equals("S")) {
                enviarMensaje(nombreGrupo, nombreUsuario, writer, reader, socket);
            } else {
                AppCliente.menuSesionIniciada(nombreUsuario, scanner, writer, reader, socket);
            }
        } else {
            System.out.println("Error al enviar el mensaje. Inténtalo de nuevo.");
            enviarMensaje(nombreUsuario, nombreUsuario, writer, reader, socket);
        }
    }

    /**
     * Envía una solicitud al servidor para leer mensajes de un chat específico y
     * muestra la respuesta.
     *
     * @param nombreGrupo   El nombre del grupo de chat del cual se quieren leer los
     *                      mensajes.
     * @param nombreUsuario El nombre del usuario que realiza la solicitud.
     * @param writer        El flujo de datos de salida para enviar información al
     *                      servidor.
     * @param reader        El flujo de datos de entrada para recibir información
     *                      del servidor.
     * @throws IOException Si hay un error al enviar o recibir datos a través del
     *                     socket.
     */
    public static void leerMensajes(String nombreGrupo, String nombreUsuario, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        String mensaje = "leerMensajes;" + nombreGrupo + ";" + nombreUsuario;
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();
        System.out.println("Mensajes del chat " + nombreGrupo + ": "
                + "\n" + serverResponse);

    }

    /**
     * Obtiene la lista de usuarios actualmente conectados al servidor y la muestra
     * por pantalla.
     *
     * Esta función envía una solicitud al servidor para obtener la lista de
     * usuarios que están
     * actualmente conectados y luego muestra esta lista por pantalla. Cada usuario
     * en la lista se muestra
     * en una nueva línea.
     *
     * @param writer El flujo de salida de datos utilizado para enviar mensajes al
     *               servidor.
     * @param reader El flujo de entrada de datos utilizado para recibir la lista de
     *               usuarios conectados desde el servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void listarUsuariosConectados(DataOutputStream writer, DataInputStream reader) throws IOException {
        String mensaje = "listarUsuariosConectados";
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();
        System.out.println("Llista de usuarios conectados: "
                + "\n" + serverResponse);
    }

    /**
     * Obtiene la lista de grupos creados por el usuario y la muestra por pantalla.
     *
     * Esta función envía una solicitud al servidor para obtener la lista de grupos
     * creados
     * y luego muestra esta lista por pantalla. Cada grupo en la lista se muestra
     * en una nueva línea.
     *
     * @param nombreUsuario El nombre del usuario que desea obtener la lista de
     *                      grupos creados.
     * @param writer        El flujo de salida de datos utilizado para enviar
     *                      mensajes al servidor.
     * @param reader        El flujo de entrada de datos utilizado para recibir la
     *                      lista de grupos creados desde el servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void listarGruposCreados(String nombreUsuario, DataOutputStream writer, DataInputStream reader)
            throws IOException {
        String mensaje = "listarGrupos;" + nombreUsuario;
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();

        System.out.println("Llista de chats: "
                + "\n" + serverResponse);

    }

    /**
     * Añade un usuario a un grupo específico en el servidor utilizando un socket
     * para comunicación.
     *
     * Esta función permite al usuario especificar el nombre del usuario que desea
     * añadir a un grupo existente.
     * Luego, envía una solicitud al servidor para llevar a cabo esta acción y
     * muestra la respuesta del servidor.
     * Además, llama a la función listarMiembrosGrupo para mostrar la lista
     * actualizada de miembros en el grupo.
     *
     * @param nombreUsuario El nombre del usuario que se desea añadir al grupo.
     * @param writer        El flujo de salida de datos utilizado para enviar
     *                      mensajes al servidor.
     * @param reader        El flujo de entrada de datos utilizado para recibir
     *                      respuestas del servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void anadirUsuarioAGrupo(String nombreGrupo, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del usuario que quieres añadir: ");
        String nombreUsuario = scanner.next();
        String mensaje = "AñadirUsuarioAGrupo;" + nombreUsuario + ";" + nombreGrupo;
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();
        System.out.println(serverResponse);
        listarMiembrosGrupo(nombreGrupo, writer, reader);
    }

    /**
     * Obtiene la lista de miembros de un grupo específico y la muestra por
     * pantalla.
     * 
     * @param writer El flujo de salida de datos utilizado para enviar
     *               mensajes al
     *               servidor.
     * 
     * @param reader El flujo de entrada de datos utilizado para recibir respuestas
     *               del servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void listarMiembrosGrupo(String nombreGrupo, DataOutputStream writer, DataInputStream reader)
            throws IOException {
        String mensaje = "listarMiembrosGrupo;" + nombreGrupo;
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();

        System.out.println("Llista de miembros del grupo: "
                + "\n" + serverResponse);

    }

    /**
     * Esta función permite al usuario eliminar un miembro especificado de un grupo
     * existente en el servidor.
     * Se solicita el nombre del usuario a eliminar y se envía una solicitud al
     * servidor para realizar la operación.
     *
     * @param nombreUsuario El nombre del usuario que se desea eliminar del grupo.
     * @param nombreGrupo   El nombre del grupo del que se eliminará el usuario.
     * @param writer        El flujo de salida de datos utilizado para enviar
     *                      mensajes al servidor.
     * @param reader        El flujo de entrada de datos utilizado para recibir
     *                      respuestas del servidor.
     * @throws IOException Si hay un problema con la entrada o salida de datos.
     */
    public static void eliminarMiembro(String nombreUsuario, String nombreGrupo, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        listarMiembrosGrupo(nombreGrupo, writer, reader);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce el nombre del usuario que quieres eliminar: ");
        nombreUsuario = scanner.next();
        String mensaje = "eliminarMiembro;" + nombreUsuario + ";" + nombreGrupo;
        writer.writeUTF(mensaje);
        String serverResponse = reader.readUTF();
        System.out.println(serverResponse);

    }

    /**
     * Desconecta a un usuario del servidor y cierra los flujos y el socket de
     * comunicación.
     *
     * Esta función se encarga de desconectar a un usuario del servidor. Se envía
     * una solicitud al servidor
     * para cerrar la sesión del usuario especificado. Luego, se cierran los flujos
     * de entrada y salida de datos,
     * así como el socket de comunicación.
     *
     * @param nombreUsuario El nombre del usuario que se desconectará del servidor.
     * @param writer        El flujo de salida de datos utilizado para enviar
     *                      mensajes al servidor.
     * @param reader        El flujo de entrada de datos utilizado para recibir
     *                      respuestas del servidor.
     * @param socket        El socket de comunicación con el servidor.
     */
    public static void desconectarUsuario(String nombreUsuario, DataOutputStream writer,
            Socket socket) {
        try {
            String mensaje = "cerrarSesion;" + nombreUsuario;
            writer.writeUTF(mensaje);
            System.out.println("¡Hasta luego, " + nombreUsuario + "!");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void darseDeBajaUsuario(DataOutputStream writer, DataInputStream reader, Socket socket) {
        try {
            System.out.println("¿Estás seguro de que quieres darte de baja? (S/N)");
            Scanner scanner = new Scanner(System.in);
            String respuesta = scanner.next();
            if (respuesta.equals("N")) {
                System.out.println("Operación cancelada.");
                AppCliente.menuSesionIniciada("", scanner, writer, reader, socket);
            } else {
                String mensaje = "darseDeBaja;";
                writer.writeUTF(mensaje);
                String serverResponse = reader.readUTF();
                System.out.println(serverResponse);
                writer.close();
                reader.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
