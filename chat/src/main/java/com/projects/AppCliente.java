package com.projects;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import com.projects.functions.FuncionesUsuario;

/**
 * Cliente que se conecta al servidor para interactuar con él.
 * Contine menus para mostrar al usuario y controla la logica de las acciones
 *
 * @author Gerard Albesa,Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */

public class AppCliente {
    // CLIENTE
    final static String COMANDO_NO_RECONOCIDO = "Comando no reconocido";

    public static void main(String[] args) {

        // CLIENTE
        String serverAddress = "localhost";
        int serverPort = 12345;
        Scanner scanner = new Scanner(System.in);

        // se establece el servidor al que se conecta el cliente y se inicializa el
        // flujo de comunicacion E/S.

        try (Socket socket = new Socket(serverAddress, serverPort);
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());) {

            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);

            menuPrincipal(scanner, writer, reader, socket);

            // while (true) {
            // String mensaje = menuPrincipal(scanner, writer, reader);
            // writer.writeUTF(mensaje);
            // String serverResponse = reader.readUTF();
            // System.out.println("Mensaje del servidor: " + serverResponse);
            // }

        } catch (UnknownHostException e) {
            System.err.println("No se pudo encontrar el host " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de comunicación con el servidor: " + e.getMessage());
            System.exit(1);
        } finally {
            scanner.close();
        }
    }

    private static void menuPrincipal(Scanner scanner, DataOutputStream writer, DataInputStream reader, Socket socket)
            throws IOException {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Registrarse (Sign up)."
                + "\n2. Iniciar sesión (Sign in)."
                + "\n3. Configuración del cliente."
                + "\n4. Salir.");

        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1:
                FuncionesUsuario.registrarse(writer, reader, socket);
                break;
            case 2:
                FuncionesUsuario.iniciarSesion(writer, reader, socket);
                break;
            case 3:
                // Lógica para configurar el cliente
                break;
            case 4:
                System.exit(0);
                break;
            default:
                System.out.println(COMANDO_NO_RECONOCIDO);
                break;
        }

    }

    public static void menuSesionIniciada(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader, Socket socket) throws IOException {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Enviar mensaje."
                + "\n2. Listar chats."
                + "\n3. Listar usuarios."
                + "\n4. Listar usuarios conectados."
                + "\n5. Crear un chat."
                + "\n6. Administrar un chat."
                + "\n7. Cerrar sesión.");

        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1:
                // Lógica para enviar mensaje
                System.out.println("==================");
                FuncionesUsuario.listarGruposCreados(nombreUsuario, writer, reader);
                FuncionesUsuario.enviarMensaje(nombreUsuario, writer, reader, socket);
                break;
            case 2:
                // Listar chats
                FuncionesUsuario.listarGruposCreados(nombreUsuario, writer, reader);
                break;
            case 3:
                // Lógica para listar grupos y al seleccionar uno
                // entra en el.
                FuncionesUsuario.listarUsuarios(writer, reader);
                break;
            case 4:
                // Lógica para listar usuarios conectados
                FuncionesUsuario.listarUsuariosConectados(writer, reader);
                break;
            case 5:
                // Lógica para crear un grupo
                FuncionesUsuario.creacionGrupo(writer, reader, socket);
                break;
            case 6:
                // Lógica para administrar un grupo
                menuAdministrarGrupo(nombreUsuario, scanner, writer, reader);
                break;
            case 7:
                // logica para cerrar sesion
                FuncionesUsuario.desconectarUsuario(nombreUsuario, writer, reader, socket);
                menuPrincipal(scanner, writer, reader, socket);
                break;
            default:
                System.out.println(COMANDO_NO_RECONOCIDO);
                break;
        }
        System.out.println();
    }

    private static void menuAdministrarGrupo(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        FuncionesUsuario.listarGruposCreados(nombreUsuario, writer, reader);
        String mensaje = "";
        System.out.println("Introduce el nombre del grupo que quieres administrar: ");
        String nombreGrupo = scanner.next();

        mensaje = "administrarGrupo;" + nombreGrupo;
        writer.writeUTF(mensaje);
        boolean salir = false;
        while (!salir) {
            System.out.println("==================\nEstás administrando un chat. Selecciona una opción:");
            System.out.println("1. Listar usuarios del grupo."
                    + "\n2. Añadir usuario a grupo."
                    + "\n3. Eliminar usuario del grupo"
                    + "\n4. Salir del chat."
                    + "\n5. Eliminar grupo."
                    + "\n6. Volver al menú principal.");
            int opcion = 0;
            opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    FuncionesUsuario.listarMiembrosGrupo(nombreGrupo, writer, reader);
                    break;
                case 2:
                    // Lógica para añadir usuario
                    FuncionesUsuario.AñadirUsuarioAGrupo(nombreGrupo, writer, reader);
                    break;
                case 3:
                    FuncionesUsuario.eliminarMiembro(nombreUsuario, nombreGrupo, writer, reader);
                    break;
                case 4:
                    FuncionesUsuario.listarMiembrosGrupo(nombreGrupo, writer, reader);
                    break;
                case 5:
                    // Lógica para eliminar grupo
                    FuncionesUsuario.eliminarGrupo(nombreGrupo, writer, reader);
                    break;
                case 6:
                    // Lógica para volver al menú principal
                    salir = true;
                    break;
                default:
                    System.out.println(COMANDO_NO_RECONOCIDO);
                    break;
            }
        }
    }

    private static void menuGrupo(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader, Socket socket) throws IOException {
        System.out.println("==================\nEstás en un chat. Selecciona una opción:");
        System.out.println("1. Administrar chat."
                + "\n2. Descargar archivos."
                + "\n3. Leer mensajes."
                + "\n4. Salir del chat.");

        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                // Lógica para administrar el grupo
                break;
            case 2:
                // Lógica para descargar archivos
                break;
            case 3:
                // Lógica para leer mensajes
                break;
            case 4:
                menuSesionIniciada(nombreUsuario, scanner, writer, reader, socket); // nombre usuario actual

                break;
            default:
                System.out.println(COMANDO_NO_RECONOCIDO);
                break;
        }
    }
}
