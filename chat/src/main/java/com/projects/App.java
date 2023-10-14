package com.projects;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class App {
    // CLIENTE
    public static void main(String[] args) {
        // CLIENTE
        String serverAddress = "localhost";
        int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort);
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);

            while (true) {
                int option = 0;
                String mensaje = "";
                menuPrincipal(mensaje, option, writer, reader);

                writer.writeUTF(mensaje);
                String serverResponse = reader.readUTF();
                System.out.println("Mensaje del servidor: " + serverResponse);
            }

        } catch (UnknownHostException e) {
            System.err.println("No se pudo encontrar el host " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de comunicación con el servidor: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void menuPrincipal(String mensaje, int option, DataOutputStream writer, DataInputStream reader)
            throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione una opción:");
        System.out.println("1. Registrarse (Sign up)."
                + "\n2. Iniciar sesión (Sign in)."
                + "\n3. Configuración del cliente."
                + "\n4. Salir.");

        option = scanner.nextInt();

        switch (option) {
            case 1:
                mensaje = "Registrarse";
                // Aquí puedes agregar la lógica para registrarse
                break;
            case 2:
                iniciarSesion(writer, reader);
                break;
            case 3:
                // Configuración del cliente
                break;
            case 4:
                System.exit(0);
                break;
            default:
                mensaje = "Comando no reconocido";
                break;
        }
    }

    private static void iniciarSesion(DataOutputStream writer, DataInputStream reader) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre de usuario: ");
        String nombreUsuario = scanner.next();
        System.out.print("Introduce tu contraseña: ");
        String contrasena = new String(System.console().readPassword());
        String mensaje = "iniciarSesion;" + nombreUsuario + ";" + contrasena;
        writer.writeUTF(mensaje);
        boolean correcto = reader.readBoolean();
        if (correcto) {
            menuSesionIniciada(nombreUsuario);
        } else {
            System.out.println("Error al iniciar sesión. Inténtalo de nuevo.");
        }
    }

    private static void menuSesionIniciada(String nombreUsuario) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================\nBienvenido " + nombreUsuario + "!");
        System.out.println("Seleccione una opción:");
        System.out.println("1. Enviar mensaje."
                + "\n2. Listar chats."
                + "\n3. Listar usuarios."
                + "\n4. Listar usuarios conectados."
                + "\n5. Crear un chat."
                + "\n6. Cerrar sesión.");

        int option = scanner.nextInt();

        switch (option) {
            case 1:
                // Lógica para enviar mensaje
                break;
            case 2:
                // Lógica para listar grupos
                break;
            case 3:
                // Lógica para listar usuarios
                break;
            case 4:
                // Lógica para listar usuarios conectados
                break;
            case 5:
                menuCrearGrupo(nombreUsuario);
                break;
            case 6:
                // Lógica para cerrar sesión
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }

    private static void menuCrearGrupo(String nombreUsuario) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================\nEstás creando un chat. Selecciona una opción:");
        System.out.println("1. Listar usuarios."
                + "\n2. Listar usuarios conectados."
                + "\n3. Eliminar un chat."
                + "\n4. Administrar un chat."
                + "\n5. Volver al menú principal.");

        int option = scanner.nextInt();

        switch (option) {
            case 1:
                // Lógica para listar usuarios
                break;
            case 2:
                // Lógica para listar usuarios conectados
                break;
            case 3:
                // Lógica para eliminar un grupo
                break;
            case 4:
                menuGrupo(nombreUsuario);
                break;
            case 5:
                menuSesionIniciada(nombreUsuario); // Aquí deberías pasar el nombre del usuario actual
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }

    private static void menuGrupo(String nombreUsuario) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================\nEstás en un chat. Selecciona una opción:");
        System.out.println("1. Administrar grupo."
                + "\n2. Eliminar grupo."
                + "\n3. Descargar archivos."
                + "\n4. Leer mensajes (automático)."
                + "\n5. Salir del grupo.");

        int opcion = scanner.nextInt();

        switch (opcion) {
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
                menuSesionIniciada(nombreUsuario); // Aquí deberías pasar el nombre del usuario actual
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }
}
