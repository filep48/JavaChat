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

    public static void enviarMensaje(DataOutputStream writer, DataInputStream reader, String nombreUsuario)
            throws IOException {
        Scanner scanner = new Scanner(System.in);
        listarUsuarios(writer, reader);
        System.out.print("Introduce el nombre de usuario al que le quieres enviar un mensaje: ");
        String nombreUsuarioDestino = scanner.next();
        crearGrupo(writer, reader, nombreUsuario, nombreUsuarioDestino);
        System.out.print("Introduce el mensaje que quieres enviar: ");
        String mensaje = scanner.next();
        String mensajeAEnviar = "enviarMensaje;" + nombreUsuario + ";" + nombreUsuarioDestino + ";" + mensaje;
        writer.writeUTF(mensajeAEnviar);
        boolean mensajeEnviado = reader.readBoolean();
        if (mensajeEnviado) {
            System.out.println("Mensaje enviado.");
        } else {
            System.out.println("Error al enviar el mensaje. Inténtalo de nuevo.");
        }
    }
    
    public static void listarUsuarios(DataOutputStream writer, DataInputStream reader) throws IOException {
        String mensaje = "listarUsuarios";
        writer.writeUTF(mensaje);
        String usuarios = reader.readUTF();
        System.out.println("Usuarios conectados: " + usuarios);
    }

    public static void crearGrupo(DataOutputStream writer, DataInputStream reader, String nombreUsuario,
            String nombreUsuarioDestino) throws IOException {
        String mensaje = "crearGrupo;" + nombreUsuario + ";" + nombreUsuarioDestino;
        writer.writeUTF(mensaje);
        boolean grupoCreado = reader.readBoolean();
        if (grupoCreado) {
            System.out.println("Grupo creado.");
        } else {
            System.out.println("Error al crear el grupo. Inténtalo de nuevo.");
        }
    }

    public static void listarGrupos(String nombreUsuario, DataOutputStream writer, DataInputStream reader) throws IOException {
        String mensaje = "listarGrupos;" + nombreUsuario;
        writer.writeUTF(mensaje);
        String grupos = reader.readUTF();
        System.out.println("Grupos: " + grupos);
    }
}
