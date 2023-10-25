package com.projects.functions;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class EnviarFicheroCliente {

    
    Socket sk;
    FileInputStream fileInput;
    BufferedInputStream bufferInput;
    File fichero;
    OutputStream out;
    String nombreFichero;


    static final int METODO_ENVIO = 2;
    static final long TAMANO_MAXIMO = 5000 * 1024 * 1024; // 5GB

    public static void enviarArchivo(Socket socket, String rutaArchivo, String tipoAcceso) {
        File archivo = new File(rutaArchivo);

        if (archivo.length() > TAMANO_MAXIMO) {
            System.out.println("El archivo es demasiado grande. Tamaño máximo permitido: 5000 MB");
        } else {
            switch (METODO_ENVIO) {
                case 1:
                    enviar(socket, rutaArchivo, tipoAcceso);
                    break;
                case 2:
                    enviarBloques(socket, rutaArchivo, tipoAcceso);
                    break;
            }
        }
    }

    static void enviar(Socket socket, String rutaArchivo, String tipoAcceso) {
        try {
            File archivo = new File(rutaArchivo);
            FileInputStream fileInput = new FileInputStream(archivo);
            BufferedInputStream bufferInput = new BufferedInputStream(fileInput);

            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

            dataOutput.writeUTF(tipoAcceso);

            dataOutput.writeUTF(archivo.getName());

            long longitudArchivo = archivo.length();
            dataOutput.writeLong(longitudArchivo);

            byte buffer[] = new byte[1024];
            int leidos;

            while ((leidos = bufferInput.read(buffer)) != -1) {
                dataOutput.write(buffer, 0, leidos);
            }

            bufferInput.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void enviarBloques(Socket socket, String rutaArchivo, String tipoAcceso) {
        try {
            File archivo = new File(rutaArchivo);
            FileInputStream fileInput = new FileInputStream(archivo);
            BufferedInputStream bufferInput = new BufferedInputStream(fileInput);
            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
    
            dataOutput.writeUTF(tipoAcceso);
            dataOutput.writeUTF(archivo.getName());
    
            long longitudArchivo = archivo.length();
            dataOutput.writeLong(longitudArchivo);
    
            byte buffer[] = new byte[1024];
            int leidos;
            int contador = 0;
    
            while ((leidos = bufferInput.read(buffer)) != -1) {
                dataOutput.write(buffer, 0, leidos);
                contador++;
    
                // Imprimir asteriscos 
                if (contador % 50 == 0) {
                    System.out.print("********");
                }
            }
    
            bufferInput.close();
            socket.close();
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


public static void tipoPrivacidad(Socket socket) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Por favor, ingrese la ruta del archivo local que desea enviar: ");
        String rutaArchivo = scanner.nextLine();

        System.out.println("Elija el tipo de acceso (público, privado o protegido): ");
        String tipoAcceso = scanner.nextLine();

        enviarArchivo(socket, rutaArchivo, tipoAcceso);
    }












}// cierre de clase
