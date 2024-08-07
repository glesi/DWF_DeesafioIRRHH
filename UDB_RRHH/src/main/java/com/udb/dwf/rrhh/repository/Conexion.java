package com.udb.dwf.rrhh.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/UDB_RRHH?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection conexion = null;

    public static Connection getConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("1. No se ha podido cargar el driver. Reason: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("2. No se ha podido establecer la conexión. Reason: " + e.getMessage());
        }

        return conexion;
    }

    public static void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión. Reason: " + e.getMessage());
            }
        }
    }
}
