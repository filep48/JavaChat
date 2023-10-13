package com.projects;

import java.io.*;
import java.net.*;

public class App {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Cambia esto a la dirección IP o nombre de host del servidor si es
                                            // necesario
        int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort);
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor en " + serverAddress + ":" + serverPort);
            menu();
            int opcion = Integer.parseInt(userInput.readLine());
            String mensaje;
            switch (opcion) {
                case 1:
                    mensaje = "Inicia sesion";
                    break;
                case 2:
                    mensaje = "registrate";
                    break;
                case 3:
                    mensaje = "Salir";
                    break;
                default:
                    mensaje = "Comando no reconocido";
                    break;
            }
            writer.writeUTF(mensaje);
            String serverResponse = reader.readUTF();
            System.out.println("Mensaje del servidor:" + serverResponse);

        } catch (UnknownHostException e) {
            System.err.println("No se pudo encontrar el host " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("No se pudo obtener la entrada/salida para la conexión con " + serverAddress);
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