package com.udb.dwf.rrhh.repository;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class Conexion {
    private static final String URL = "jdbc:sqlserver://JORGE-LG:50375 ;database=UDB_RRHH;integratedSecurity=true;encrypt=optional;trustServerCertificate=true";

    public static Connection conexion = null;
    public static Connection getConexion() {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            conexion= DriverManager.getConnection(URL);

        } catch(ClassNotFoundException e){
            System.err.println("1. No se ha podido establecer la conexion. Reason: "+e.getMessage());
        } catch (SQLException e){
            System.err.println("2. No se ha podido establecer la conexion. Reason: "+e.getMessage());
        }

        return conexion;
    }

    public static void desconectar(){
        if(conexion != null){
            try{
                conexion.close();
            }catch (SQLException e){
                System.err.println("Error al cerrar la conexión. Reason: "+e.getMessage());
            }
        }
    }
}
