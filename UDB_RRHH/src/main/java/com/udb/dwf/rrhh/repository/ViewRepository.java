package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.View;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewRepository {
    public List<View> getViews() {
        List<View> views = new ArrayList<View>();

        Connection con = null;
        try{
            con = Conexion.getConexion();
            String query = "{call ObtenerDatosEmpleados()}";
            try (CallableStatement sp = con.prepareCall(query)) {
                try (ResultSet rs = sp.executeQuery()) {
                    while (rs.next()) {
                        int idEmpleado = rs.getInt("idEmpleado");
                        String numeroDui = rs.getString("numeroDui");
                        String nombrePersona = rs.getString("nombrePersona");
                        String numeroTelefono = rs.getString("numeroTelefono");
                        String correoInstitucional = rs.getString("correoInstitucional");
                        String cargo = rs.getString("cargo");
                        Date fechaContratacion = rs.getDate("fechaNacimiento");
                        double salario = rs.getDouble("salario");
                        Date fechaNacimiento = rs.getDate("fechaNacimiento");
                        View viewObj = new View(idEmpleado,numeroDui,nombrePersona,numeroTelefono,correoInstitucional,cargo,fechaContratacion,salario,fechaNacimiento);
                        views.add(viewObj);
                    }
                }catch (SQLException e){
                    System.out.println("Error al procesar la consulta."+e.getMessage());
                }
            }catch (SQLException e) {
                System.out.println("Error al llamar el stored procedure. Reason: "+e.getMessage());
            }
        } finally {
            if (con != null) Conexion.desconectar();
        }

        return views;

    }

    public View getViewById(int id) {
        View viewObj = null;
        Connection con = null;
        try{
            con = Conexion.getConexion();
            String query = "{call ObtenerDatosEmpleadosById(idEmpleado)}";
            try(CallableStatement sp = con.prepareCall(query)){
                try(ResultSet rs= sp.executeQuery()){
                    while (rs.next()) {
                        int idEmpleado = rs.getInt("idEmpleado");
                        String numeroDui = rs.getString("numeroDui");
                        String nombrePersona = rs.getString("nombrePersona");
                        String numeroTelefono = rs.getString("numeroTelefono");
                        String correoInstitucional = rs.getString("correoInstitucional");
                        String cargo = rs.getString("cargo");
                        Date fechaContratacion = rs.getDate("fechaNacimiento");
                        double salario = rs.getDouble("salario");
                        Date fechaNacimiento = rs.getDate("fechaNacimiento");
                        viewObj = new View(idEmpleado,numeroDui,nombrePersona,numeroTelefono,correoInstitucional,cargo,fechaContratacion,salario,fechaNacimiento);
                    }
                }catch(SQLException e){
                    System.out.println("Error al intentar obtener registro de empleado "+id+"."+e.getMessage());
                }

            }catch (SQLException e) {
                System.out.println("Error al intentar ejecutar storeed procedure"+e.getMessage());
            }
        }finally {
            if (con != null) Conexion.desconectar();
        }
        return viewObj;

    }
}
