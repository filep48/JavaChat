package com.projects.functions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class functionsSQL {

    public static Statement IniciarSession(Connection cn, Statement st) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/fromjava", "root", "Naydler007");
        st = cn.createStatement();
        return st;
    }
}
