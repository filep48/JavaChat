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
    static final String COMANDO_NO_RECONOCIDO = "Comando no reconocido";

    public static void main(String[] args) {

        // CLIENTE
        // se establece el servidor al que se conecta el cliente y se inicializa el
        // flujo de comunicacion E/S.
        CrearConexionCliente.iniciarCliente();

    }

    static void menuPrincipal(Scanner scanner, DataOutputStream writer, DataInputStream reader, Socket socket)
            throws IOException {
        System.out.println("==================\nBienvenido a la app de chat.");
        System.out.println("Seleccione una opción:");
        System.out.println("1. Registrarse (Sign up)."
                + "\n2. Iniciar sesión (Sign in)."
                + "\n3. Salir");

        int opcion = scanner.nextInt();
        switch (opcion) {
            case 1:
                FuncionesUsuario.registrarse(writer, reader, socket);
                break;
            case 2:
                FuncionesUsuario.iniciarSesion(writer, reader, socket);
                break;
            case 3:
                System.exit(0);

                break;
            default:
                System.out.println(COMANDO_NO_RECONOCIDO);
                break;
        }

    }

    public static void menuSesionIniciada(String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader, Socket socket) throws IOException {
        boolean condicion = true;
        while (condicion) {
            System.out.println("==================\nHas entrado como  " + nombreUsuario + ".");
            System.out.println("Seleccione una opción:");
            System.out.println("1. Listar y seleccionar chats."
                    + "\n2. Listar usuarios."
                    + "\n3. Listar usuarios conectados."
                    + "\n4. Crear un chat."
                    + "\n5. Administrar un chat."
                    + "\n6. Darse de baja."
                    + "\n7. Cerrar sesión.");

            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    // Listar chats
                    System.out.println("==================");
                    FuncionesUsuario.listarGruposCreados(nombreUsuario, writer, reader);
                    System.out.println("Selecciona el chat que quieres abrir: ");
                    String nombreGrupo = scanner.next();
                    menuGrupo(nombreGrupo, nombreUsuario, scanner, writer, reader, socket);
                    break;
                case 2:
                    // Lógica para listar grupos y al seleccionar uno
                    // entra en el.
                    FuncionesUsuario.listarUsuarios(writer, reader);
                    break;
                case 3:
                    // Lógica para listar usuarios conectados
                    FuncionesUsuario.listarUsuariosConectados(writer, reader);
                    break;
                case 4:
                    // Lógica para crear un grupo
                    FuncionesUsuario.creacionGrupo(nombreUsuario, writer, reader, socket);
                    break;
                case 5:
                    // Lógica para administrar un grupo
                    menuAdministrarGrupo(nombreUsuario, scanner, writer, reader);
                    break;
                case 6:
                    // Lógica para darse de baja
                    FuncionesUsuario.darseDeBajaUsuario(writer, reader, socket);
                    condicion = false;
                    menuPrincipal(scanner, writer, reader, socket);
                    break;
                case 7:
                    // logica para cerrar sesion
                    FuncionesUsuario.desconectarUsuario(nombreUsuario, writer);
                    condicion = false;
                    menuPrincipal(scanner, writer, reader, socket);
                    break;
                default:
                    System.out.println(COMANDO_NO_RECONOCIDO);
                    break;
            }
            System.out.println();
        }
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
                    FuncionesUsuario.listarUsuarios(writer, reader);
                    FuncionesUsuario.anadirUsuarioAGrupo(nombreGrupo, writer, reader);
                    break;
                case 3:
                    FuncionesUsuario.eliminarMiembro(nombreUsuario, nombreGrupo, writer, reader);
                    break;
                case 4:
                
                    // Lógica para salir del chat
                    FuncionesUsuario.salirGrupo(nombreGrupo, writer, reader);
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

    public static void menuGrupo(String nombreGrupo, String nombreUsuario, Scanner scanner, DataOutputStream writer,
            DataInputStream reader, Socket socket) throws IOException {
        boolean salir = false;
        while (!salir) {
            FuncionesUsuario.leerMensajes(nombreGrupo, writer, reader);
            System.out.println("==================\nEstás en el chat " + nombreGrupo + ". Selecciona una opción:");
            System.out.println("1. Enviar mensaje."
                    + "\n2. Descargar archivos."
                    + "\n3. Enviar archivos."
                    + "\n4. Actualizar mensajes."
                    + "\n4. Salir del chat."
                    + "\n==================");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    // Lógica para enviar mensajes
                    FuncionesUsuario.enviarMensaje(nombreGrupo, nombreUsuario, writer, reader, socket);
                    break;
                case 2:
                    FuncionesUsuario.listarFicherosBBDD(nombreGrupo, writer, reader);
                    break;
                case 3:
                    FuncionesUsuario.enviarFichero(nombreGrupo, socket, reader);
                    break;
                case 4:
                    // Lógica para actualizar mensajes
                    FuncionesUsuario.leerMensajes(nombreGrupo, writer, reader);
                    break;
                case 5:
                    salir=true;
                    menuSesionIniciada(nombreUsuario, scanner, writer, reader, socket);
                    break;
                default:
                    System.out.println(COMANDO_NO_RECONOCIDO);
                    break;
            }
        }
    }
}
