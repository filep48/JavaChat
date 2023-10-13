package com.projects;

import java.io.*;
import java.net.*;

public class App {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort);
             DataInputStream reader = new DataInputStream(socket.getInputStream());
             DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)) ) {

            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);

            while (true) {
                menu();
                int option = Integer.parseInt(userInput.readLine());
                String message;

                switch (option) {
                    case 1:
                        message = "Inicia sesion";
                        break;
                    case 2:
                        message = "registrate";
                        break;
                    case 3:
                        message = "Salir";
                        break;
                    default:
                        message = "Comando no reconocido";
                        break;
                }

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

    private static void menu() {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
    }
}
