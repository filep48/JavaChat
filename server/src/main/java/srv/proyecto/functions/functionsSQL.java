package srv.proyecto.functions;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import srv.proyecto.clases.DatabaseConnection;
import srv.proyecto.clases.Usuario;


public class functionsSQL {
/**
 * Inicia sesión de usuario basado en los datos recibidos del cliente.
 *
 * @param writer El flujo de salida para enviar una respuesta al cliente.
 * @param reader El flujo de entrada para recibir datos del cliente.
 * @throws IOException Si ocurre un error de entrada/salida al comunicarse con el cliente.
 */
private static void splitDatosUsuario(DataOutputStream writer, DataInputStream reader) {
    try {
        String message = reader.readUTF();
        String[] parts = message.split(";");
        if (parts.length == 3 && parts[0].equals("iniciarSesion")) {
            String username = parts[1];
            String password = parts[2];
            // Llamar a la función datosUsuario con los valores de nombre de usuario y contraseña
            Usuario usuario = functionsSQL.datosUsuario(username, password);
            if (usuario != null) {
                // Inicio de sesión exitoso
                writer.writeBoolean(true);
            } else {
                // Inicio de sesión fallido, ofrecer opción de registro
                writer.writeBoolean(false);
            }
        } else {
            System.out.println("Mensaje de inicio de sesión incorrecto.");
        }
    } catch (IOException e) {
        // Manejo de excepciones de entrada/salida
        System.err.println("Error de entrada/salida al comunicarse con el cliente: " + e.getMessage());
    }
}


    public static String llistarUsuariosCreados(Connection cn) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("nombre_usuario") + " " + rs.getString("contrasena") + "\n";
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    public static String llistarGruposCreados(Connection cn) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT id, nombre_grupo FROM grupos";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();
            String resultado = "";
            while (rs.next()) {
                resultado += rs.getString("id") + " " + rs.getString("nombre_grupo") + "\n";
            }
            return resultado;
        } catch (SQLException ex) {
            return "Error: " + ex.toString();
        }
    }

    // Este metodo que manda el mensaje de x cliente a la base de datos
    public static PreparedStatement EnviarMensajesBBDD(Connection cn, String mensaje) {
        try {
            String strSql = "insert into mensajes (contenido) values (?)";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, mensaje);
            pst.executeUpdate();
            return pst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Realiza el inicio de sesión de un usuario en la base de datos.
     *
     * @param nombre     El nombre de usuario.
     * @param contrasena La contraseña del usuario.
     * @return Un objeto Usuario si el inicio de sesión es exitoso.
     * @throws SQLException                 Si ocurre un error al acceder a la base
     *                                      de datos.
     * @throws InicioSesionFallidoException Si el inicio de sesión falla.
     */

    public static Usuario datosUsuario(String nombre, String contrasena) {
        try (Connection cn = DatabaseConnection.getConnection()) {
            String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            try (PreparedStatement pst = cn.prepareStatement(strSql)) {
                pst.setString(1, nombre);
                pst.setString(2, contrasena);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return new Usuario(nombre, contrasena);
                    } else {
                        System.err.println("El usuario no existe o la contraseña es incorrecta");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
            return null;
        }
    }

    // *********************************************
    public static void creacionGruposBBDD(Connection cn) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce el nombre del grupo: ");
            String nombreGrupo = scanner.nextLine();

            String strSql = "INSERT INTO grupos (nombre_grupo) VALUES (?)";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, nombreGrupo);
            pst.executeUpdate();

            System.out.println("Grupo creado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void EliminacionGruposBBDD(Connection cn) {
        try {
            String strSql = "delete from grupos where id = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            String idGrupo = JOptionPane.showInputDialog(null, "Introduce el id del grupo");
            pst.setString(1, idGrupo);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica si un usuario existe en la base de datos.
     *
     * @param cn    La conexión a la base de datos.
     * @param datos Los datos del usuario a verificar.
     * @return true si el usuario existe en la bbdd, o false si no existe.
     * @throws SQLException Si ocurre un error al acceder a la bbdd.
     */
    public static boolean usuarioExiste(Connection cn, Usuario datos) throws SQLException {
        String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement pst = cn.prepareStatement(strSql)) {
            pst.setString(1, datos.getNombreUsuarioo());

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Si hay resultados el usuario existe en la base de datos
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
            return false;
        }
    }

    /**
     * Función para derivar el flujo de acciones segun si la funcion usuarioExiste
     * devuelve true o false
     */

    public static String login(Usuario datos, Connection cn) {
        if (usuarioExiste(datos, cn)) {
            String respuesta = ("LLAMAR AQUI A MENU PARA ENTRAR AL PROGRAMA");
            JOptionPane.showMessageDialog(null, "Usuario existe");
            return respuesta;
        } else {
            String respuestaNegativa = ("Usuario o contraseña no existen");
            return respuestaNegativa;
        }
    }

    // *********************************************

    /** función que da de alta a un usuario en la bbdd */
    public static void darAltaUsuario(Connection cn, Usuario usuarioDatos) {
        Console console = System.console();
        if (console != null) {
            char[] contrasenaChars = console.readPassword("Introduce tu contraseña: ");
            String contrasena = new String(contrasenaChars);
            try {
                String strSql = "INSERT INTO Usuarios (nombre_usuario, contrasena) VALUES (?, ?)";
                PreparedStatement pst = cn.prepareStatement(strSql);
                pst.setString(1, usuarioDatos.getNombreUsuarioo());
                pst.setString(2, contrasena);

                int filasAfectadas = pst.executeUpdate();

                if (filasAfectadas == 1) {
                    System.out.println("Usuario dado de alta con éxito.");
                } else {
                    System.out.println("Error al dar de alta al usuario.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FuncionesServer.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error al dar de alta al usuario.");
            }
        } else {
            System.out.println("La consola no está disponible. No se puede ingresar la contraseña.");
        }
    }

    /**
     * Función para validar la contraseña y lanzar una excepción personalizada si no
     * cumple
     */

    static void validarContrasena(String contrasena) throws ContrasenaInvalidaException {
        if (contrasena == null || !contrasena.matches("^.{6,32}$")) {
            throw new ContrasenaInvalidaException("La contraseña no cumple con los requisitos.");
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

    public static void validacionUsuariosInicioSesion(Connection connection, DataInputStream reader)
            throws IOException {
        Usuario datos = datosUsuario(connection, reader);
        usuarioExiste(datos, connection);
        login(datos, connection);
    }

}
