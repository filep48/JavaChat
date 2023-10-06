package com.projects.functions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class functionsSQL {

    public static PreparedStatement IniciarSession(Connection cn) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "Naydler007");
        
        // Utiliza PreparedStatement en lugar de Statement
        String strSql = "SELECT nombre_usuario, contrasena FROM usuarios";
        PreparedStatement pst = cn.prepareStatement(strSql);
        
        return pst;
    }

    public static void llistarUsuariosCreados(PreparedStatement pst) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            
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
}
