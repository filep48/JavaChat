package srv.proyecto.functions;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import srv.proyecto.clases.Usuario;

public class functionsSQL {

    public static PreparedStatement IniciarSession(Connection cn) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "1234");

        // Utiliza PreparedStatement en lugar de Statement
        String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
        PreparedStatement pst = cn.prepareStatement(strSql);

        return pst;
    }

    public static void llistarUsuariosCreados(Connection cn) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("nombre_usuario") + " " + rs.getString("contrasena"));
            }
            System.out.println("---------");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

    public static void llistarGruposCreados(Connection cn) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            String strSql = "SELECT id, nombre_grupo FROM grupos";
            PreparedStatement pst = cn.prepareStatement(strSql);

            // Resultados de la consulta
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("id") + " " + rs.getString("nombre_grupo"));
            }
            System.out.println("---------");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
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
    public static Usuario datosUsuario(Connection cn, String nombre, String contrasena) {
        try {
            String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, nombre);
            pst.setString(2, contrasena);
    
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                System.out.println("Sesión iniciada con éxito.");
                return new Usuario(nombre, contrasena);
            } else {
                System.out.println("Error al iniciar sesión. Usuario o contraseña incorrectos.");
                return null;
            }
        } catch (SQLException ex) {
            System.err.println("Error al acceder a la base de datos: " + ex.getMessage());
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
     * Función que comprueba en bbbdd si existe o no y devuelve un booleano.
     * si no existe, llama a la función darAltaUsuario.
     */

    public static boolean usuarioExiste(Connection cn, Usuario datos) {
        try {
            String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, datos.getNombreUsuarioo());

            ResultSet rs = pst.executeQuery();
            return rs.next(); // Si hay resultados: el usuario existe en la base de datos
        } catch (SQLException ex) {
            Logger.getLogger(FuncionesServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Función para derivar el flujo de acciones segun si la funcion usuarioExiste
     * devuelve true o false
     */

    public static void login(Connection cn, Usuario datos) {
        if (usuarioExiste(cn, datos)) {
            System.out.println("Usuario existe LLAMAR AL MENU 2.");
            // Llamar aquí a la función de menú para entrar al programa.
        } else {
            System.out.println("Usuario o contraseña no existen.");
            darAltaUsuario(cn, datos);
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

}
