package srv.proyecto.functions;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import srv.proyecto.clases.Fichero;
import srv.proyecto.clases.Usuario;


public class RecibirFicheroServer {

    static final int METODO_RECIBIR = 2; // en bloque
    static final long TAMANO_MAXIMO = 5000 * 1024 * 1024; // 5GB
    
    Socket sk;
    FileOutputStream fileOutput;
    BufferedOutputStream bufferPutput;
    File fichero;
    InputStream input;

    String nombreFichero;



    public void RecibiFicherosServidor(Socket sk,Usuario usuario, int tipo, int grupoId) {
        this.sk = sk;
        
        switch( METODO_RECIBIR ){
            case 1:
                recibe(usuario); 
                break;
            case 2:
                recibeBloques(); 
                break;
        }
    }

    void recibe(Usuario usuario) {
        try {
            input = sk.getInputStream();
    
            DataInputStream datainputStream = new DataInputStream(input);
            nombreFichero = datainputStream.readUTF();
    
            String s[] = nombreFichero.split("[\\\\/]"); 
            nombreFichero = s[s.length - 1];
    
            long longitudFichero = datainputStream.readLong();
            
            if (longitudFichero > TAMANO_MAXIMO) {
                System.out.println("El archivo excede el tamaño máximo permitido.");
                return;
            }

            String timestamp = obtenerNombreArchivoUnico();
            String nombreUnico = timestamp + "_" + nombreFichero;
            String rutaCarpeta = "server\\ArchivosChats";
            guardarArchivoEnCarpeta(rutaCarpeta, nombreUnico, input);
            //Fichero fichero = new Fichero();
            //FuncionesSQL.guardarArchivoEnBD (fichero);
    
            String nombreFicheroPrevio = "recibiendo_" + nombreUnico;
    
            fichero = new File(nombreFicheroPrevio);
            fichero.delete(); // Eliminar el archivo por si ya existe!!!!
            fileOutput = new FileOutputStream(fichero);
            bufferPutput = new BufferedOutputStream(fileOutput);
    
            byte b[] = new byte[(int) longitudFichero];
    
            int recibiendo = datainputStream.read(b);
            System.out.println("Recibidos: " + recibiendo + " faltan " + (longitudFichero - recibiendo));
    
            while (recibiendo < longitudFichero) {
                int recibidos = datainputStream.read(b, recibiendo, (int) longitudFichero - recibiendo);
                recibiendo += recibidos;
                System.out.println("Recibidos: " + recibidos + " faltan " + (longitudFichero - recibiendo));
            }
    
            bufferPutput.write(b);
            bufferPutput.close();
            
            // Renombrar el archivo con el nombre único
            File nuevoArchivo = new File("rec_" + nombreUnico);
            nuevoArchivo.delete();
            fichero.renameTo(nuevoArchivo);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    void recibeBloques() {
        int longitudBloque = 512;
    
        try {
            input = sk.getInputStream();
    
            DataInputStream dataInputStream = new DataInputStream(input);
            nombreFichero = dataInputStream.readUTF();
    
            String stringPreparar[] = nombreFichero.split("[\\\\/]"); 
            nombreFichero = stringPreparar[stringPreparar.length - 1];
    
            long longitudFichero = dataInputStream.readLong();
    
            if (longitudFichero > TAMANO_MAXIMO) {
                System.out.println("El archivo excede el tamaño máximo permitido.");
                return;
            }
    
            // Generar un nombre único para el archivo
            String timestamp = obtenerNombreArchivoUnico();
            String nombreUnico = timestamp + "_" + nombreFichero;
    
            String nomfichPrevi = "recibiendo_" + nombreUnico;

            String rutaCarpeta = "server\\ArchivosChats";

            guardarArchivoEnCarpeta(rutaCarpeta, nombreUnico, input);
            //FuncionesSQL.guardarArchivoEnBD(nombreUnico, rutaCarpeta, tipo, usuario, grupoId);;
    
            fichero = new File(nomfichPrevi);
            fichero.delete(); 
            fileOutput = new FileOutputStream(fichero);
            bufferPutput = new BufferedOutputStream(fileOutput);
            System.out.println("El fichero ocupará " + longitudFichero + " bytes");
    
            byte b[] = new byte[(int) longitudBloque];
    
            long contador = 0;
            while (contador < longitudFichero) {
                int leido;
                if (longitudFichero - contador > longitudBloque) {
                    leido = input.read(b, 0, longitudBloque);
                } else {
                    leido = input.read(b, 0, (int) (longitudFichero - contador));
                }
                bufferPutput.write(b, 0, leido);
                contador = contador + leido; 
                System.out.println("Bytes recibidos: " + leido + " llevamos: " + contador + " bytes");
                System.out.print("*****");
            }
    
            bufferPutput.close();
    
            // Renombrar el archivo con el nombre único
            File nuevoArchivo = new File("rec_" + nombreUnico); 
            nuevoArchivo.delete();
            fichero.renameTo(nuevoArchivo);
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

        String obtenerNombreArchivoUnico() {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fecha = formatoFecha.format(new Date  (timestamp)); 
            return fecha ;
        }

        public static void guardarArchivoEnCarpeta(String rutaCarpeta, String nombreArchivoUnico, InputStream inputStream) {
            try {

                String rutaCompletaArchivo = Paths.get(rutaCarpeta, nombreArchivoUnico).toString();
                Path destino = Paths.get(rutaCompletaArchivo);
                Files.copy(inputStream, destino);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        




}
