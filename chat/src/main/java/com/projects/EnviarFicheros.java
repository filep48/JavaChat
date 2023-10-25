package com.projects;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.projects.config.ConfiguracionCliente;


/**
 * Envía un archivo al servidor.
 *
 * @param socket       El socket a través del cual se establece la conexión con el servidor.
 * @param filePath     La ruta al archivo que se va a enviar.
 * @param writer       El flujo de salida de datos utilizado para enviar información al servidor.
 * @param permisos     Los permisos asociados al archivo (1: público, 2: privado, 3: protegido).
 * @param nombreGrupo  El nombre del grupo al que se va a enviar el archivo.
 * @throws IOException  Si ocurre un error en la comunicación con el servidor o en la lectura/escritura de archivos.
 */
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
