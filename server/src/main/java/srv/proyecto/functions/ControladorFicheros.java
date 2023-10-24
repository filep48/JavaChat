package srv.proyecto.functions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class ControladorFicheros {

    public static void RecibirFicheros(Socket clientSocket, String savePath, String fileName) {
    try {
        // Combine savePath and fileName to form the full path
        File receivedFile = new File(savePath + File.separator + fileName);

        byte[] buffer = new byte[4096];
        InputStream is = clientSocket.getInputStream();
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
        System.out.println("Archivo recibido y guardado como " + fileName);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

    public static String obtenerNombreArchivoUnico() {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fecha = formatoFecha.format(new Date(timestamp));
        return fecha;
    }
}