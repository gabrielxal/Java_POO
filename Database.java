package com.mycompany.unidadepoo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // endereço do database
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_cadastro"; 
    private static final String USER = "*****"; 
    private static final String PASSWORD = "******"; 
    
    // metodo para conexão com database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}