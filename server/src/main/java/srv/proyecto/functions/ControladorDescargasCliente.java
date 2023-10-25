package srv.proyecto.functions;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ControladorDescargasCliente {
    public static void descargar(Socket socket, String archivoDescargas, DataOutputStream writer, String grupo,
            String nombreGrupo) throws IOException {

        File descargar = new File(archivoDescargas);
        writer.writeUTF("Iniciando descarga...");
        try {
            FileInputStream fis = new FileInputStream(descargar);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println(archivoDescargas);
            // Paso 1: Enviar el tamaño del archivo
            long fileSize = descargar.length();
            dos.writeLong(fileSize);

            // Paso 2: Enviar el archivo
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            // consigue el nombre del archivo

            dos.flush();
            System.out.println("Archivo enviado al cliente!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
