package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class EmpleadoRepository {

    //create
    public Empleado crearEmpleado(Empleado empleado) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = Conexion.getConexion();
            String query = "INSERT INTO Empleados (numeroDui,nombrePersona,usuario,numeroTelefono,correoInstitucional,fechaNacimiento) VALUES (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, empleado.getNumeroDui());
            preparedStatement.setString(2, empleado.getNombrePersona());
            preparedStatement.setString(3, empleado.getUsuario());
            preparedStatement.setString(4, empleado.getNumeroTelefono());
            preparedStatement.setString(5, empleado.getCorreoInstitucional());
            preparedStatement.setDate(6, empleado.getFechaNacimiento());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    empleado.setIdEmpleado(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear el registro de Empleado. Reason: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }

        return empleado;
    }


    //read
    public List<Empleado> obtenerEmpleado(){
        List<Empleado> empleadoList = new ArrayList<Empleado>();

        Connection connection= null;
        PreparedStatement preparedStatement=  null;
        ResultSet resultSet= null;

        try{
            connection= Conexion.getConexion();
            String query= "SELECT idEmpleado, numeroDui, nombrePersona, usuario, numeroTelefono, correoInstitucional, fechaNacimiento FROM Empleados";
            preparedStatement= connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int idEmpleado = resultSet.getInt("idEmpleado");
                String numeroDui = resultSet.getString("numeroDui");
                String nombrePersona = resultSet.getString("nombrePersona");
                String usuario = resultSet.getString("usuario");
                String numeroTelefono = resultSet.getString("numeroTelefono");
                String correoInstitucional = resultSet.getString("correoInstitucional");
                Date fechaNacimiento = resultSet.getDate("fechaNacimiento");
                Empleado empleadoObj= new Empleado(idEmpleado,numeroDui,nombrePersona,usuario,numeroTelefono, correoInstitucional, fechaNacimiento );
                empleadoList.add(empleadoObj);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener los registros de Empleado. Reason: " + e.getMessage());

        } finally{
            try{
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            }catch (SQLException e){
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
        return empleadoList;

    }

    //Metodo para actualizar Empleado
    public boolean actualizarEmpleado(Empleado empleado) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Conexion.getConexion();
            String query = "UPDATE Empleados SET numeroDui = ?, nombrePersona = ?, usuario = ?, numeroTelefono = ?, correoInstitucional = ?, fechaNacimiento = ? WHERE idepartamento = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, empleado.getNumeroDui());
            preparedStatement.setString( 2, empleado.getNombrePersona());
            preparedStatement.setString( 3, empleado.getUsuario());
            preparedStatement.setString( 4, empleado.getNumeroTelefono());
            preparedStatement.setString( 5, empleado.getCorreoInstitucional());
            preparedStatement.setDate( 6, empleado.getFechaNacimiento());
            preparedStatement.setInt(7, empleado.getIdEmpleado());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el registro de Empleado. Reason: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
    }


    // Metodo para eliminar Empleado
    public boolean eliminarEmpleado(int idEmpleado) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Conexion.getConexion();
            String query = "DELETE FROM Empleados WHERE idEmpleado = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idEmpleado);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el registro de Empleado. Reason: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
    }

    public Empleado obtenerEmpleadoPorId(int idEmpleado) {
        Empleado empleado = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Conexion.getConexion();
            String query = "SELECT idEmpleado, numeroDui,nombrePersona,usuario,numeroTelefono,correoInstitucional,fechaNacimiento FROM Empleados WHERE idEmpleado = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idEmpleado);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idEmpleadoRes = resultSet.getInt("idEmpleado");
                String numeroDui = resultSet.getString("numeroDui");
                String nombrePersona = resultSet.getString("nombrePersona");
                String usuario = resultSet.getString("usuario");
                String numeroTelefono = resultSet.getString("numeroTelefono");
                String correoInstitucional = resultSet.getString("correoInstitucional");
                Date fechaNacimiento = resultSet.getDate("fechaNacimiento");


                empleado = new Empleado(idEmpleadoRes,numeroDui,nombrePersona,usuario,numeroTelefono,correoInstitucional,fechaNacimiento );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el registro de Empleado por ID. Reason: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
        return empleado;
    }

}
