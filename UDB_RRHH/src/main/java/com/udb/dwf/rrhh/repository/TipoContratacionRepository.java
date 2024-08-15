package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.TipoContratacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoContratacionRepository {

    //Metodo para crear Tipo Contratacion
    public TipoContratacion crearTipoContratacion(TipoContratacion tipoContratacion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        // Maneja cualquier error de la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "INSERT INTO TipoContratacion (tipoContratacion) VALUES (?)";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tipoContratacion.getTipoContratacion());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    tipoContratacion.setIdTipoContratacion(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear el registro de TipoContratacion. Reason: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
        return tipoContratacion;
    }

    //Metodo para listar Tipo Contratacion
    public List<TipoContratacion> obtenerTipoContrataciones(){
        List<TipoContratacion> tipoContratacionList = new ArrayList<TipoContratacion>();

        Connection connection= null;
        PreparedStatement preparedStatement=  null;
        ResultSet resultSet= null;
        // Maneja cualquier error de la base de datos
        try{
            connection= Conexion.getConexion();
            String query= "SELECT idTipoContratacion, tipoContratacion FROM TipoContratacion";
            preparedStatement= connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int idTipoContratacion = resultSet.getInt("idTipoContratacion");
                String tipoContratacion = resultSet.getString("tipoContratacion");
                TipoContratacion tipoContratacionObj= new TipoContratacion(idTipoContratacion,tipoContratacion);
                tipoContratacionList.add(tipoContratacionObj);
            }
        } catch (SQLException e){
            System.err.println("Error al obtener los registros de TipoContratacion. Reason: " + e.getMessage());

        } finally{
            try{
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            }catch (SQLException e){
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
         return tipoContratacionList;
    }

    //Metodo para actualizar Tipo Contratacion
    public boolean actualizarTipoContratacion(TipoContratacion tipoContratacion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        // Maneja cualquier error de la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "UPDATE TipoContratacion SET tipoContratacion = ? WHERE idTipoContratacion = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tipoContratacion.getTipoContratacion());
            preparedStatement.setInt(2, tipoContratacion.getIdTipoContratacion());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el registro de TipoContratacion. Reason: " + e.getMessage());
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

    //Metodo para eliminar Tipo Contratacion
    public boolean eliminarTipoContratacion(int idTipoContratacion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        // Maneja cualquier error de la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "DELETE FROM TipoContratacion WHERE idTipoContratacion = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTipoContratacion);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar el registro de TipoContratacion. Reason: " + e.getMessage());
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

    public TipoContratacion obtenerTipoContratacionPorId(int idTipoContratacion) {
        TipoContratacion tipoContratacion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // Maneja cualquier error de la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "SELECT idTipoContratacion, tipoContratacion FROM TipoContratacion WHERE idTipoContratacion = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idTipoContratacion);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String tipoContratacionName = resultSet.getString("tipoContratacion");
                tipoContratacion = new TipoContratacion(idTipoContratacion, tipoContratacionName);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el registro de TipoContratacion por ID. Reason: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Reason: " + e.getMessage());
            }
        }
        return tipoContratacion;
    }

}
