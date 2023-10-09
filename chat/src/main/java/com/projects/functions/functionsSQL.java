package com.projects.functions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.projects.clases.Usuario;

public class functionsSQL {

    public static PreparedStatement IniciarSession(Connection cn) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "Naydler007");

        // Utiliza PreparedStatement en lugar de Statement
        String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
        PreparedStatement pst = cn.prepareStatement(strSql);

        return pst;
    }

    public static HashMap<Integer, Usuario> llistarUsuariosCreados(Connection cn) {
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
            return null;

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

}
