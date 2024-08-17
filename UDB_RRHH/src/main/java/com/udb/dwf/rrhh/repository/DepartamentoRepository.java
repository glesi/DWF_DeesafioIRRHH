package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoRepository {

    // Metodo para crear un departamento

    public Departamento crearContratacion(Departamento departamento) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = Conexion.getConexion();
            String query = "INSERT INTO Departamento (nombreDepartamento, descripcionDepartamento) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, departamento.getNombreDepartamento());
            preparedStatement.setString(2, departamento.getDescripcionDepartamento());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    departamento.setIdDepartamento(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear el registro de Departamento. Reason: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }

        return departamento;
    }
    //lista para almacenar los objetos Departamento que se obtendrán
    public List<Departamento> obtenerDepartamento(){
        List<Departamento> departamentoList = new ArrayList<Departamento>();

        Connection connection= null;
        PreparedStatement preparedStatement=  null;
        ResultSet resultSet= null;

        try{
            connection= Conexion.getConexion();
            String query= "SELECT idDepartamento, nombreDepartamento,descripcionDepartamento FROM Departamento";
            preparedStatement= connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int idDepartamento = resultSet.getInt("idDepartamento");
                String nombreDepartamento = resultSet.getString("nombreDepartamento");
                String descripcionDepartamento = resultSet.getString("descripcionDepartamento");
                Departamento departamentoObj= new Departamento(idDepartamento, nombreDepartamento,descripcionDepartamento);
                departamentoList.add(departamentoObj);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener los registros de Departamento. Reason: " + e.getMessage());

        } finally{
            try{
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            }catch (SQLException e){
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
        return departamentoList;

    }

    // Metodo para actualizar Departamento

    public boolean actualizarDepartamento(Departamento departamento) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Conexion.getConexion();
            String query = "UPDATE Departamento SET idDepartamento =?,nombreDepartamento=?,descripcionDepartamento = ? WHERE idDepartamento = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, departamento.getIdDepartamento());
            preparedStatement.setString(2, departamento.getNombreDepartamento());
            preparedStatement.setString( 3, departamento.getDescripcionDepartamento());
            preparedStatement.setInt( 4, departamento.getIdDepartamento());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el registro de Departamento. Reason: " + e.getMessage());
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

    // Metodo para eliminar Departamento

    public boolean eliminarDepartamento(int idDepartamento) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Conexion.getConexion();
            String query = "DELETE FROM Departamento WHERE idDepartamento = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idDepartamento);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el registro de Departamento. Reason: " + e.getMessage());
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
    // Metodo para obtener departamento por Id
    public Departamento obtenerDepartamentoPorId(int idDepartamento) {
        Departamento departamento = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // Se maneja cualquier error que lanze la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "SELECT idDepartamento,nombreDepartamento,descripcionDepartamento FROM Departamento WHERE idDepartamento = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idDepartamento);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idDepartamentoRes = resultSet.getInt("idDepartamento");
                String departamentoName = resultSet.getString("nombreDepartamento");
                String descripcionDepartamento = resultSet.getString("descripcionDepartamento");

                departamento = new Departamento(idDepartamentoRes, departamentoName, descripcionDepartamento);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el registro de Departamento por ID. Reason: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
        return departamento;
    }

}



