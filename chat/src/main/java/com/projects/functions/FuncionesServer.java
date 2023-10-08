package com.projects.functions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FuncionesServer {
    static String leerMensaje(InputStream is) throws IOException {
        StringBuilder mensaje = new StringBuilder();
        int byteLeido;
        while ((byteLeido = is.read()) != -1) {
            mensaje.append((char) byteLeido);
        }
        return mensaje.toString();
    }

    static void guardarEnArchivo(String mensaje) {
        try {

            //
            // Especifica la ruta del archivo donde deseas guardar el mensaje
            String rutaArchivo = "chat\\src\\main\\java\\com\\projects\\functions\\cliente1\\mesajesEscritos.txt";
            // Abre el archivo en modo de apertura (append) para añadir contenido
            FileWriter writer = new FileWriter(rutaArchivo, true);

            // Escribe la cadena en el archivo seguida de un salto de línea
            writer.write(mensaje + "\n");

            // Cierra el archivo después de escribir
            writer.close();

            System.out.println("Mensaje guardado en el archivo " + rutaArchivo);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
