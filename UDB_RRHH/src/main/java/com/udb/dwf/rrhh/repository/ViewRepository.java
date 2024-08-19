package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.config.Conexion;
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
                        String nombreDepartamento = rs.getString("nombreDepartamento");
                        Date fechaContratacion = rs.getDate("fechaContratacion");
                        double salario = rs.getDouble("salario");
                        Date fechaNacimiento = rs.getDate("fechaNacimiento");
                        String tipoContratacion = rs.getString("tipoContratacion");
                        View viewObj = new View(idEmpleado,numeroDui,nombrePersona,numeroTelefono,correoInstitucional,cargo,nombreDepartamento, fechaContratacion,salario,fechaNacimiento, tipoContratacion);
                        views.add(viewObj);
                    }
                }catch (SQLException e){
                    System.out.println("Error al procesar la consulta."+e.getMessage());
                }
            }catch (SQLException e) {
                System.out.println("Error al llamar el stored procedure. Reason: "+e.getMessage());
            }
        } finally {
            try{
                if (con != null) con.close();
            }catch (SQLException e){
                System.err.println("Error al cerrar la consulta."+e.getMessage());
            }
        }
        return views;
    }
    public View getViewById(int id) {
        View viewObj = null;
        try (Connection con = Conexion.getConexion()) {
            String query = "{call ObtenerEmpleadoById(?)}";
            try (CallableStatement sp = con.prepareCall(query)) {
                sp.setInt(1, id);
                try (ResultSet rs = sp.executeQuery()) {
                    while (rs.next()) {
                        int idEmpleado = rs.getInt("idEmpleado");
                        String numeroDui = rs.getString("numeroDui");
                        String nombrePersona = rs.getString("nombrePersona");
                        String numeroTelefono = rs.getString("numeroTelefono");
                        String correoInstitucional = rs.getString("correoInstitucional");
                        String cargo = rs.getString("cargo");
                        String nombreDepartamento = rs.getString("nombreDepartamento");
                        Date fechaContratacion = rs.getDate("fechaContratacion");
                        double salario = rs.getDouble("salario");
                        Date fechaNacimiento = rs.getDate("fechaNacimiento");
                        String tipoContratacion = rs.getString("tipoContratacion");

                        viewObj = new View(idEmpleado, numeroDui, nombrePersona, numeroTelefono, correoInstitucional, cargo, nombreDepartamento, fechaContratacion, salario, fechaNacimiento, tipoContratacion);
                    }
                } catch (SQLException e) {
                    System.out.println("Error al intentar obtener registro de empleado " + id + "." + e.getMessage());
                }

            } catch (SQLException e) {
                System.out.println("Error al intentar ejecutar stored procedure" + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al manejar conexion" + e.getMessage());
            throw new RuntimeException(e);
        }
        return viewObj;

    }

    public boolean eliminaViewElements(int idEmpleado){
        Connection con = null;

        try{
            con = Conexion.getConexion();
            String query = "{call deleteContratacionAndEmpleadoByIdEmpleado(?)}";
            try (CallableStatement sp = con.prepareCall(query)){
                sp.setInt(1, idEmpleado);
                int affectedRows = sp.executeUpdate();
                    return affectedRows>0;
                }catch (SQLException e){
                    System.out.println("Error al intentar eliminar registro: "+idEmpleado+e.getMessage());
                    return false;
                }

        }finally{
            try{
                if (con != null) con.close();
            }catch (SQLException e){
                System.err.println("Error al intentar eliminar registro: "+idEmpleado+e.getMessage());
            }

        }
    }

}
