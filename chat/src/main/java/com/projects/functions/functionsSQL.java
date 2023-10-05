package com.projects.functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class functionsSQL {

    public static Statement IniciarSession(Connection cn, Statement st) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/chatpro", "root", "Naydler007");
        st = cn.createStatement();
        return st;
    }

    public static void llistarUsuariosCreados(Statement st) {
        try {
            System.out.println("Listado de usuarios creados");
            System.out.println();
            // codi SQL
            String strSql = "SELECT nombre_usuario,contrasena FROM usuarios";

            // resultats de la consulta
            ResultSet rs = st.executeQuery(strSql);
            while (rs.next()) {
                System.out.println(rs.getString("nombre_usuario") + " " + rs.getString("contrasena"));
            }

            rs.close();
            System.out.println("---------");

        } catch (SQLException ex) {
            // Logger.getLogger(ToMySQL01.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: " + ex.toString());
            return;
        }
    }
}
