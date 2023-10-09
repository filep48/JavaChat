package com.projects.functions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.projects.functions.FuncionesServer;



public class functionsSQL {

    public static PreparedStatement IniciarSession(Connection cn) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "troll");

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
    public static String[] datosUsuario() {
        while (true) {
            String nombreUsuario = JOptionPane.showInputDialog(null, "Introduce tu nombre de usuario");
            String contrasenaUsuario = JOptionPane.showInputDialog(null, "Introduce tu contraseña");
    
            if (nombreUsuario != null && !nombreUsuario.isEmpty() && contrasenaUsuario != null && !contrasenaUsuario.isEmpty()) {
                try {
                    FuncionesServer.validarContrasena(contrasenaUsuario);
                    return new String[]{nombreUsuario, contrasenaUsuario};
                } catch (FuncionesServer.ContrasenaInvalidaException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un nombre de usuario y contraseña válidos.");
            }
        }
    }

}
