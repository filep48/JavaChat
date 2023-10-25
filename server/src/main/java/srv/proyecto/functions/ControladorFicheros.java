package srv.proyecto.functions;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import srv.proyecto.clases.Fichero;

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

    public static boolean enviarFicherosBBDD(Fichero fichero) throws SQLException {
        Connection cn = DatabaseConnection.getConnection();
        String strSql = "INSERT INTO archivos (nombre_archivo, ruta_archivo, tipo, usuario_id, grupo_id) VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement pst = cn.prepareStatement(strSql)) {
            System.out.println("Nombre del fichero:" + fichero.getNombreFichero());
            System.out.println("Ruta del fichero:" + fichero.getRutaFichero());
            System.out.println("Tipo de archivo:" + fichero.getTipodeArchivo());
            System.out.println("Id del propietario:" + fichero.getIdPropietario());
            System.out.println("Id del grupo propietario:" + fichero.getIdGrupoPropietario());

            pst.setString(1, fichero.getNombreFichero());
            pst.setString(2, fichero.getRutaFichero());
            if (fichero.getTipodeArchivo().equals("1")) {
                pst.setString(3, "publico");
            } else if (fichero.getTipodeArchivo().equals("2")) {
                pst.setString(3, "privado");
            } else if (fichero.getTipodeArchivo().equals("3")) {
                pst.setString(3, "compartido");
            } else {
                return false;
            }
            pst.setInt(4, fichero.getIdPropietario());
            pst.setInt(5, fichero.getIdGrupoPropietario());

            int affectedRows = pst.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            System.out.println("Error al insertar el fichero en la base de datos" + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Throwable e) {
            System.out.println("Error al insertar el fichero en la base de datos" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
