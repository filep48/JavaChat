package com.projects;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import com.projects.functions.FuncionesUsuario;

public class AppCliente {
    // CLIENTE
    public static void main(String[] args) {

        // CLIENTE
        String serverAddress = "localhost";
        int serverPort = 12345;
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(serverAddress, serverPort);
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());) {

            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);

            menuPrincipal(scanner, writer, reader);

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

    private static void menuPrincipal(Scanner scanner, DataOutputStream writer, DataInputStream reader)
            throws IOException {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Registrarse (Sign up)."
                + "\n2. Iniciar sesión (Sign in)."
                + "\n3. Configuración del cliente."
                + "\n4. Salir.");

        int option1 = scanner.nextInt();
        String mensaje = "";

        switch (option1) {
            case 1:
                FuncionesUsuario.registrarse(writer, reader);
                break;
            case 2:
                FuncionesUsuario.iniciarSesion(writer, reader);
                break;
            case 3:
                // Lógica para configurar el cliente
                break;
            case 4:
                System.exit(0);
                break;
            default:
                mensaje = "Comando no reconocido";
                break;
        }

    }

    public static void menuSesionIniciada(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        boolean condition = true;
        while (condition) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Enviar mensaje."
                    + "\n2. Listar chats."
                    + "\n3. Listar usuarios."
                    + "\n4. Listar usuarios conectados."
                    + "\n5. Crear un chat."
                    + "\n6. Eliminar un chat."
                    + "\n7. Cerrar sesión.");

            int option2 = scanner.nextInt();
            String mensaje = "";
            String serverResponse = "";
            switch (option2) {
                case 1:
                    // Lógica para enviar mensaje
                    break;
                case 2:
                    // Listar chats

                    break;
                case 3:
                    // Lógica para listar grupos y al seleccionar uno
                    // entra en el.
                    mensaje = "listarUsuarios";
                    writer.writeUTF(mensaje);
                    serverResponse = reader.readUTF();
                    System.out.println("Llista de usuarios: "
                            + "\n" + serverResponse);
                    break;
                case 4:
                    // Lógica para listar usuarios conectados
                    mensaje = "listarUsuariosConectados";
                    writer.writeUTF(mensaje);
                    serverResponse = reader.readUTF();
                    System.out.println("Llista de usuarios conectados: "
                            + "\n" + serverResponse);
                    break;
                case 5:
                    FuncionesUsuario.creacionGrupo(writer, reader);
                    // Lógica para crear un grupo
                    break;
                case 6:
                    // Lógica para eliminar un grupo
                    break;
                case 7:
                    mensaje = "CerrarSession";
                    writer.writeUTF(mensaje);
                    serverResponse = reader.readUTF();
                    condition = false;
                    break;
                default:
                    System.out.println("Comando no reconocido");
                    break;
            }
            System.out.println();
        }
    }

    private static void menuCrearGrupo(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        System.out.println("==================\nEstás creando un chat. Selecciona una opción:");
        System.out.println("1. Listar usuarios."
                + "\n2. Listar usuarios conectados."
                + "\n3. Administrar un chat."
                + "\n4. Volver al menú principal.");

        int option3 = scanner.nextInt();
        switch (option3) {
            case 1:
                // Lógica para listar usuarios
                break;
            case 2:
                // Lógica para listar usuarios conectados
                break;
            case 3:
                menuGrupo(nombreUsuario, scanner, writer, reader);
                break;
            case 4:
                menuSesionIniciada(nombreUsuario, scanner, writer, reader); // Aquí deberías pasar el nombre del usuario
                                                                            // actual
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }

    private static void menuGrupo(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader) throws IOException {
        System.out.println("==================\nEstás en un chat. Selecciona una opción:");
        System.out.println("1. Administrar chat."
                + "\n2. Eliminar chat."
                + "\n3. Descargar archivos."
                + "\n4. Leer mensajes (automático)."
                + "\n5. Salir del chat.");

        int opcion4 = scanner.nextInt();

        switch (opcion4) {
            case 1:
                // Lógica para administrar el grupo
                break;
            case 2:
                // Lógica para eliminar el grupo
                break;
            case 3:
                // Lógica para descargar archivos
                break;
            case 4:
                // Lógica para leer mensajes
                break;
            case 5:
                menuSesionIniciada(nombreUsuario, scanner, writer, reader); // Aquí deberías pasar el nombre del usuario
                                                                            // actual
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }
}
