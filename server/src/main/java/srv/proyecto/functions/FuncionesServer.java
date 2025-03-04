package srv.proyecto.functions;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import srv.proyecto.AppServer;
import srv.proyecto.clases.Usuario;

public class FuncionesServer {
    // Eliminada la variable estática 'usuarios'
    // private static HashMap<String, Usuario> usuarios;

    public static String[] slplitMensaje(String mensaje) {
        String[] mensajeSplit = mensaje.split(";");
        return mensajeSplit;
    }

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

    /**
     * Función para validar la contraseña y lanzar una excepción personalizada si no
     * cumple
     */

    static boolean validarContrasena(String contrasena) throws ContrasenaInvalidaException {
        if (contrasena == null || !contrasena.matches("^.{6,32}$")) {
            throw new ContrasenaInvalidaException("La contraseña no cumple con los requisitos.");
        } else {
            return true;
        }
    }

    /**
     * Excepción personalizada para manejar contraseñas inválidas
     * lanza mensaje predefenido por nosotros en validarContrasena
     */
    public static class ContrasenaInvalidaException extends Exception {
        public ContrasenaInvalidaException(String mensaje) {
            super(mensaje);
        }
    }

    // Eliminado el constructor que recibe el HashMap
    /*
     * public FuncionesServer(HashMap<String, Usuario> usuariosConectados) {
     * FuncionesServer.usuarios = usuariosConectados;
     * }
     */

    public static void conectarUsuario(String nombreUsuario, String contrasena) {
        Usuario nuevoUsuario = new Usuario(nombreUsuario, contrasena);
        nuevoUsuario.setConectado(true);
        AppServer.usuariosConectados.put(nombreUsuario, nuevoUsuario);
    }

    public static void desconectarUsuario(String nombreUsuario) {
        if (AppServer.usuariosConectados.containsKey(nombreUsuario)) {
            AppServer.usuariosConectados.get(nombreUsuario).setConectado(false);
            AppServer.usuariosConectados.remove(nombreUsuario);
        }
    }

    // crea una funcion que lee el hashmap y inidica los usuarios conectados en una
    // string
    public static String listarUsuariosConectados() {
        StringBuilder usuariosConectadosStr = new StringBuilder();
        for (Usuario usuario : AppServer.usuariosConectados.values()) {
            if (usuario.isConectado()) {
                usuariosConectadosStr.append(usuario.getNombreUsuario()).append("\n");
            }
        }
        return usuariosConectadosStr.toString();
    }
}
