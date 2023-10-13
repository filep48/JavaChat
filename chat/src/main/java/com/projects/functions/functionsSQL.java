package com.projects.functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.projects.clases.Usuario;

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

    /**
     * Función que recoge datos por teclado del usuario y la envia a función q
     * valida la regex,
     */
    public static Usuario datosUsuario() {
        while (true) {
            String nombreUsuario = JOptionPane.showInputDialog(null, "Introduce tu nombre de usuario");
            String contrasenaUsuario = JOptionPane.showInputDialog(null, "Introduce tu contraseña");
    
            if (nombreUsuario != null && !nombreUsuario.isEmpty() && contrasenaUsuario != null && !contrasenaUsuario.isEmpty()) {
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
    /**Función que comprueba en bbbdd si existe o no y devuelve un booleano */
    public static void consultaBbddUsuarioExiste(Usuario datos,Connection cn ) {
        try {
            String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
            PreparedStatement pst = cn.prepareStatement(strSql);
            
            ResultSet rs = pst.executeQuery();
            boolean usuarioExiste = false;
            while (rs.next() && !usuarioExiste) {
                System.out.println(rs.getString("nombre_usuario") + " " + rs.getString("contrasena"));
                if (rs.getString("nombre_usuario").equals(datos.getNombreUsuarioo()) && rs.getString("contrasena").equals(datos.getContrasena())){
                    System.out.println("Usuario existe");
                    usuarioExiste = true;
                }
        }
        if (usuarioExiste == false){
            System.out.println("Usuario o contraseña no existen");
        }
        
        } catch (SQLException ex) {
            Logger.getLogger(FuncionesServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // ********************************************* 
    public static void creacionGruposBBDD(Connection cn){
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
    public static void EliminacionGruposBBDD(Connection cn){
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
}
