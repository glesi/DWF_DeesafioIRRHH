package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargosRepository {

    // Metodo para crear Cargo
    public Cargo crearCargo(Cargo cargo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = Conexion.getConexion();
            String query = "INSERT INTO cargos (cargo, descripcionCargo, jefatura) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cargo.getCargo());
            preparedStatement.setString(2, cargo.getDescripcionCargo());
            preparedStatement.setBoolean(3, cargo.isJefatura());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cargo.setIdCargo(generatedKeys.getInt(1));
                }
            }
            return cargo;
        } catch (SQLException e) {
            System.err.println("Error al insertar el cargo. Reason: " + e.getMessage());
            return null;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    //Metodo para leer todos los cargos
    public List<Cargo> obtenerCargos(){
        List<Cargo> cargosList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection= Conexion.getConexion();
            String query = "select idCargo, cargo, descripcionCargo, jefatura from cargos";
            preparedStatement= connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int idCargo = resultSet.getInt("idCargo");
                String cargo= resultSet.getString("cargo");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                boolean jefatura = resultSet.getBoolean("jefatura");
                Cargo cargoObj= new Cargo(idCargo,cargo,descripcionCargo,jefatura);
                cargosList.add(cargoObj);
            }


        }catch (SQLException e){
            System.err.println("Error al obtener los registros de Cargos.Reason: " + e.getMessage());
        }finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            }catch (SQLException e){
                System.err.println("Error al desconectar la conexion: " + e.getMessage());
            }
        }
        return cargosList;
    }

    public boolean actualizarCargo(Cargo cargo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Conexion.getConexion();
            String query = "UPDATE cargos SET cargo = ?, descripcionCargo = ?, jefatura = ? WHERE idCargo = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cargo.getCargo());
            preparedStatement.setString(2, cargo.getDescripcionCargo());
            preparedStatement.setBoolean(3, cargo.isJefatura());
            preparedStatement.setInt(4, cargo.getIdCargo());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el cargo. Reason: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }
    //Metodo para eliminar Cargo
    public boolean eliminarCargo(int idCargo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = Conexion.getConexion();
            String query = "DELETE FROM cargos WHERE idCargo = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idCargo);

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el cargo. Reason: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    //Metodo para obtener cargo por id
    public Cargo obtenerCargoPorId(int idCargo) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Conexion.getConexion();
            String query = "SELECT idCargo, cargo, descripcionCargo, jefatura FROM cargos WHERE idCargo = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idCargo);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String cargo = resultSet.getString("cargo");
                String descripcionCargo = resultSet.getString("descripcionCargo");
                boolean jefatura = resultSet.getBoolean("jefatura");
                return new Cargo(idCargo, cargo, descripcionCargo, jefatura);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el cargo. Reason: " + e.getMessage());
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

}

