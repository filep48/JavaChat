package com.projects;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class EnviarFicheros {
    public static void sendFile(Socket socket, String filePath, DataOutputStream writer, int permisos,
            String nombreGrupo) throws IOException {
        File fileToSend = new File(filePath);
        String fileName = fileToSend.getName();
        String mensaje = "enviarFichero;" + permisos + ";" + nombreGrupo + ";" + fileName;
        writer.writeUTF(mensaje);
        try {
            FileInputStream fis = new FileInputStream(fileToSend);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            System.out.println(fileName);
            // Paso 1: Enviar el tamaño del archivo
            long fileSize = fileToSend.length();
            dos.writeLong(fileSize);

            // Paso 2: Enviar el archivo
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            // consigue el nombre del archivo

            dos.flush(); // Asegúrate de que todos los datos se envíen
            System.out.println("Archivo enviado al servidor!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
