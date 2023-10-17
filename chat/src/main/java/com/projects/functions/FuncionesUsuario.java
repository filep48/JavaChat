package com.projects.functions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import com.projects.AppCliente;

public class FuncionesUsuario {

    public static void registrarse(DataOutputStream writer, DataInputStream reader) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre de usuario deseado: ");
        String nombreUsuario = scanner.next();
        System.out.print("Introduce tu contraseña deseada: ");
        String contrasena = new String(System.console().readPassword());
        String mensaje = "registrarse;" + nombreUsuario + ";" + contrasena;
        writer.writeUTF(mensaje);
        boolean registroExitoso = reader.readBoolean();
        if (registroExitoso) {
            System.out.println("Registro exitoso.");
            AppCliente.menuSesionIniciada(nombreUsuario, scanner, writer, reader);
        } else {
            System.out.println(
                    "Error al registrarse. El nombre de usuario puede estar en uso o hubo un problema con el servidor. Inténtalo de nuevo.");
        }
    }

    public static void iniciarSesion(DataOutputStream writer, DataInputStream reader) throws IOException {
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
            AppCliente.menuSesionIniciada(nombreUsuario, scanner, writer, reader);
        } else {
            System.out.println("Error al iniciar sesión. Inténtalo de nuevo.");
        }
    }
    public static void eliminarGrupo(String nombreUsuario,DataOutputStream writer, DataInputStream reader) throws IOException{
        Scanner scanner = new Scanner(System.in);
        listarGrupo(nombreUsuario,writer, reader);
        System.out.print("Introduce el nombre del grupo que deseas eliminar: ");
        String nombreGrupo = scanner.next();
        String mensaje = "eliminarGrupo;" + nombreUsuario + ";" + nombreGrupo;
        writer.writeUTF(mensaje);
        boolean eliminarGrupo = reader.readBoolean();
        if (eliminarGrupo) {
            System.out.println("Grupo eliminado.");
        } else {
            System.out.println("Error al eliminar el grupo. Inténtalo de nuevo.");
        }
    }

    public static void listarGrupo(String nombreUsuario,DataOutputStream writer, DataInputStream reader) throws IOException{
        String mensaje = "listarGrupos;" + nombreUsuario;
        writer.writeUTF(mensaje);
        String grupos = reader.readUTF();
        System.out.println("Grupos: " + grupos);

    }
}
