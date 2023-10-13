package srv.proyecto.functions;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    /**
     * Función que recoge datos por teclado del usuario y la envia a función q
     * valida la regex,
     */
    public static Usuario datosUsuario() {
        while (true) {
            String nombreUsuario = JOptionPane.showInputDialog(null, "Introduce tu nombre de usuario");
            String contrasenaUsuario = JOptionPane.showInputDialog(null, "Introduce tu contraseña");

            if (nombreUsuario != null && !nombreUsuario.isEmpty() && contrasenaUsuario != null
                    && !contrasenaUsuario.isEmpty()) {
                try {
                    FuncionesServer.validarContrasena(contrasenaUsuario);
                    return new Usuario(nombreUsuario, contrasenaUsuario);
                } catch (FuncionesServer.ContrasenaInvalidaException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre de usuario y contraseña válidos.");
            }
        }
    }

    /** Función que comprueba en bbbdd si existe o no y devuelve un booleano */
    public static void consultaBbddUsuarioExiste(Usuario datos, Connection cn) {
        try {
            String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
            PreparedStatement pst = cn.prepareStatement(strSql);

            ResultSet rs = pst.executeQuery();
            boolean usuarioExiste = false;
            while (rs.next() && !usuarioExiste) {
                System.out.println(rs.getString("nombre_usuario") + " " + rs.getString("contrasena"));
                if (rs.getString("nombre_usuario").equals(datos.getNombreUsuarioo())
                        && rs.getString("contrasena").equals(datos.getContrasena())) {
                    System.out.println("Usuario existe");
                    usuarioExiste = true;
                }
            }
            if (usuarioExiste == false) {
                System.out.println("Usuario o contraseña no existen");
            }

        } catch (SQLException ex) {
            Logger.getLogger(FuncionesServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // *********************************************
    public static void creacionGruposBBDD(Connection cn) {
        try {
            String strSql = "insert into grupos (nombre_grupo) values (?)";
            PreparedStatement pst = cn.prepareStatement(strSql);
            String nombreGrupo = JOptionPane.showInputDialog(null, "Introduce el nombre del grupo");
            pst.setString(1, nombreGrupo);
            pst.executeUpdate();
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
                    if (usuarioExiste(new Usuario(nombreUsuario, contrasenaUsuario), cn)) {
                        JOptionPane.showMessageDialog(null,
                                "El nombre de usuario ya está en uso. Por favor, elige otro.");
                    } else {
                        // El nombre de usuario no existe
                        FuncionesServer.validarContrasena(contrasenaUsuario);
                        return new Usuario(nombreUsuario, contrasenaUsuario);
                    }
                } catch (FuncionesServer.ContrasenaInvalidaException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            } else {
                try {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Deseas salir?", "Salir",
                            JOptionPane.YES_NO_OPTION);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        // Salir
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Función que comprueba en bbbdd si existe o no y devuelve un booleano.
     * si no existe, llama a la función darAltaUsuario.
     */

    public static boolean usuarioExiste(Usuario datos, Connection cn) {
        try {
            String strSql = "SELECT nombre_usuario, contrasena FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            PreparedStatement pst = cn.prepareStatement(strSql);
            pst.setString(1, datos.getNombreUsuarioo());
            pst.setString(2, datos.getContrasena());

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
    public static boolean darAltaUsuario(Usuario usuarioDatos, Connection cn) {
        if (JOptionPane.showConfirmDialog(null, "Quieres dar de alta el Usuario?") == JOptionPane.YES_OPTION) {
            try {

                String strSql = "INSERT INTO usuarios (nombre_usuario, contrasena) VALUES (?, ?)";
                PreparedStatement pst = cn.prepareStatement(strSql);
                pst.setString(1, usuarioDatos.getNombreUsuarioo());
                pst.setString(2, usuarioDatos.getContrasena());

                // Ejecutar la sentencia SQL
                int filasAfectadas = pst.executeUpdate();

                // Verificar si se insertó
                if (filasAfectadas == 1) {
                    System.out.println("LLAMAR AQUI A MENU PARA ENTRAR AL PROGRAMA");
                    return true;
                }
                return false;
            } catch (SQLException ex) {

                ex.printStackTrace();
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se ha dado de alta el usuario");
            return false;
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
        usuarioExiste(datos, connection);
        login(datos, connection);
    }

}
