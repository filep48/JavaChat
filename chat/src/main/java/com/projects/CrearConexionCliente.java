package com.projects;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import com.projects.config.ConfiguracionCliente;
/**
 * Cliente que se conecta al servidor para interactuar con él. Contine menus
 * para mostrar al usuario y controla la logica de las acciones
 * 
 * @Author Gerard Albesa,Kevin Felipe Vasquez, Vanessa Pedrola.
 * @version 1.0
 */

public class CrearConexionCliente {

    // Utiliza la clase ConfiguracionCliente para obtener los valores
    static ConfiguracionCliente config = new ConfiguracionCliente();
    final static String DIRECIONSERVER = config.getDireccionServer();
    final static int PUERTOSERVER = config.getPuertoServer();

    public static void iniciarCliente() {
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(DIRECIONSERVER, PUERTOSERVER);
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Conectado al servidor en " + DIRECIONSERVER + ":" + PUERTOSERVER);
            AppCliente.menuPrincipal(scanner, writer, reader, socket);
        } catch (UnknownHostException e) {
            System.err.println("No se pudo encontrar el host " + DIRECIONSERVER);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error de comunicación con el servidor: " + e.getMessage());
            System.exit(1);
        } finally {
            scanner.close();
        }
    }

    public static void cerrarPrograma(Socket socket, DataInputStream reader, DataOutputStream writer) {
        try {
            socket.close();
            reader.close();
            writer.close();

        } catch (IOException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
