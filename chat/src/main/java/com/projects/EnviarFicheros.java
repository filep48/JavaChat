package com.projects;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class EnviarFicheros {
    public static void sendFile(Socket socket, String filePath) {
        File fileToSend = new File(filePath);

        try {
            FileInputStream fis = new FileInputStream(fileToSend);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Paso 1: Enviar el tamaño del archivo
            long fileSize = fileToSend.length();
            dos.writeLong(fileSize);

            // Paso 2: Enviar el archivo
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            dos.flush();  // Asegúrate de que todos los datos se envíen
            System.out.println("Archivo enviado al servidor!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
