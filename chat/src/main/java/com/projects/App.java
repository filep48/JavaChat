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
                String message = "";
                menuPrincipal(message, option, writer, reader);

                writer.writeUTF(message);
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

    private static void menuPrincipal(String message, int option, DataOutputStream writer, DataInputStream reader)
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
                message = "Registrarse";
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
                message = "Comando no reconocido";
                break;
        }
    }

    private static void iniciarSesion(DataOutputStream writer, DataInputStream reader) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre de usuario: ");
        String username = scanner.next();
        System.out.print("Introduce tu contraseña: ");
        String password = new String(System.console().readPassword());
        String message = "iniciarSesion;" + username + ";" + password;
        writer.writeUTF(message);
        boolean success = reader.readBoolean();
        if (success) {
            menuSesionIniciada(username);
        } else {
            System.out.println("Error al iniciar sesión. Inténtalo de nuevo.");
        }
    }

    private static void menuSesionIniciada(String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenido " + username + "!");
        System.out.println("Seleccione una opción:");
        System.out.println("1. Llistar usuarios."
                + "\n2. Llistar usuarios conectados."
                + "\n3. Crear un grupo."
                + "\n4. Eliminar un grupo."
                + "\n5. Administrar un grupo."
                + "\n6. Leer mensajes."
                + "\n7. Llistar archivos."
                + "\n8. Descargar archivo."
                + "\n9. Cerrar sesión (Sign out).");

        int option = scanner.nextInt(); // Aquí deberías obtener la opción del usuario, por ejemplo usando
                                        // userInput.readLine();

        switch (option) {
            case 1:
                // Lógica para listar usuarios
                break;
            case 2:
                // Lógica para listar usuarios conectados
                break;
            case 3:
                // Lógica para crear un grupo
                break;
            case 4:
                // Lógica para eliminar un grupo
                break;
            case 5:
                // Lógica para administrar un grupo
                break;
            case 6:
                // Lógica para leer mensajes
                break;
            case 7:
                // Lógica para listar archivos
                break;
            case 8:
                // Lógica para descargar un archivo
                break;
            case 9:
                // Lógica para cerrar sesión
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }

    private static void menuGrupo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Estás en un grupo. Selecciona una opción:");
        System.out.println("1. Enviar un archivo al grupo."
                + "\n2. Enviar un mensaje al grupo."
                + "\n3. Salir del grupo.");

        int option = scanner.nextInt();// Aquí deberías obtener la opción del usuario, por ejemplo usando
                                       // userInput.readLine();

        switch (option) {
            case 1:
                // Lógica para enviar un archivo al grupo
                break;
            case 2:
                // Lógica para enviar un mensaje al grupo
                break;
            case 3:
                // Lógica para salir del grupo
                break;
            default:
                System.out.println("Comando no reconocido");
                break;
        }
    }
}
