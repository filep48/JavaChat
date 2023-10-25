package com.projects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.projects.config.ConfiguracionCliente;

public class DescargarFicheros {
    public static void descargar(Socket socket, String nombreArchivoServidor) throws IOException {
        try {

            String direccionDestino = ConfiguracionCliente.getDirecionDescargas();
            File receivedFile = new File(direccionDestino + File.separator + nombreArchivoServidor);

            byte[] buffer = new byte[4096];
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            FileOutputStream fos = new FileOutputStream(receivedFile);

            // Paso 1: Leer el tamaño del archivo
            long fileSize = dis.readLong();
            long bytesReceived = 0;

            // Paso 2: Leer el archivo
            int bytesRead;
            while (bytesReceived < fileSize) {
                bytesRead = is.read(buffer);
                fos.write(buffer, 0, bytesRead);
                bytesReceived += bytesRead;
            }

            fos.flush();
            System.out.println("Archivo recibido y guardado como " + receivedFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
