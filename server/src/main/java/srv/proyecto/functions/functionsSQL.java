package srv.proyecto.functions;

import java.io.Console;
import java.io.DataInputStream;
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

    public static PreparedStatement IniciarSession(Connection cn) throws ClassNotFoundException, SQLException {

        // Utiliza PreparedStatement en lugar de Statement
        String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
        PreparedStatement pst = cn.prepareStatement(strSql);

        return pst;
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
    
    public static Usuario datosUsuario(Connection cn, String nombre, String contrasena) {
        try {
            String strSql = "SELECT nombre_usuario FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, nombre);
            pst.setString(2, contrasena);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Usuario(nombre, contrasena);
            }
        } catch (SQLException ex) {
            System.err.println("Error al acceder a la base de datos: " + ex.getMessage());
        }
        return null;
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

    // TODAS MIS FUNCIONES AQUI
    /**
     * Función que recoge datos por teclado del usuario y la envia a función q
     * valida la regex,
     * 
     * @throws IOException
     */

    // ----------------cambios---------
    // 1. se añade una comprobacion de si el usuario existe o no para manejar una
    // excepcion de sql, ya q el nombre es PKunique.
    public static Usuario datosUsuario(Connection cn, DataInputStream reader) throws IOException {
        while (true) {
            String nombreUsuario = reader.readUTF();
            String contrasenaUsuario = reader.readUTF();

            if (nombreUsuario != null && !nombreUsuario.isEmpty() && contrasenaUsuario != null
                    && !contrasenaUsuario.isEmpty()) {
                try {
                    // Verificar si el nombre de usuario ya existe en la base de datos
                    if (usuarioExiste(cn, new Usuario(nombreUsuario, contrasenaUsuario))) {
                        System.out.println("El nombre de usuario ya está en uso. Por favor, elige otro.");
                        continue; // Vuelve al inicio del bucle para esperar otros datos de entrada
                    } else {
                        // El nombre de usuario no existe
                        FuncionesServer.validarContrasena(contrasenaUsuario);
                        return new Usuario(nombreUsuario, contrasenaUsuario);
                    }
                } catch (FuncionesServer.ContrasenaInvalidaException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    continue; // Vuelve al inicio del bucle si hay un error
                }
            } else {
                System.out.println("Nombre de usuario o contraseña vacíos. Por favor, intenta de nuevo.");
                continue; // Vuelve al inicio del bucle si los datos están vacíos
            }
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

    public static String login(Usuario datos, Connection cn) {
        if (usuarioExiste(cn, datos)) {
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

    public static void validacionUsuariosInicioSesion(Connection connection, DataInputStream reader) throws IOException {
        Usuario datos = datosUsuario(connection , reader);
        usuarioExiste(connection, datos);
        login(datos, connection);
    }

}
