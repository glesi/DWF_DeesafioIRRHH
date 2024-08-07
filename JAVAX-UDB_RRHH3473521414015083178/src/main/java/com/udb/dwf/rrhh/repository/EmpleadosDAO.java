package com.udb.dwf.rrhh.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.udb.dwf.rrhh.pojos.Empleado;


public class EmpleadosDAO extends Conexion{

    public List<Empleado> getEmployees(){

    List<Empleado> empleados = new ArrayList<>();
    String query = "SELECT * FROM empleados";

    try( Connection con = Conexion.getConexion();
    PreparedStatement pst = con.prepareStatement(query);
    ResultSet rs = pst.executeQuery()){
        while(rs.next()){

            int idEmpleado = rs.getInt("idEmpleado");
            String numeroDui = rs.getString("numeroDui");
            String nombrePersona= rs.getString("nombrePersona");
            String usuario = rs.getString("usuario");
            String numeroTelefono= rs.getString("numeroTelefono");
            String correoInstitucional= rs.getString("correoInstitucional");
            Date fechaNacimiento = rs.getDate("fechaNacimiento");

            Empleado emp = new Empleado(idEmpleado,numeroDui,nombrePersona,usuario, numeroTelefono, correoInstitucional, fechaNacimiento);
            empleados.add(emp);
        }
    }catch(SQLException e){
        System.err.println("Error al obtener empleados. Reason: "+ e.getMessage());
    }

    return empleados;
    }
}
