package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.Contrataciones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratacionesRepository {

    // Método para crear una nueva contratación
    public Contrataciones crearContratacion(Contrataciones contratacion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
// Obtener la conexión a la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "INSERT INTO Contrataciones (idDepartamento, idEmpleado, idCargo, idTipoContratacion, fechaContratacion, salario, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, contratacion.getIdDepartamento());
            preparedStatement.setInt(2, contratacion.getIdEmpleado());
            preparedStatement.setInt(3, contratacion.getIdCargo());
            preparedStatement.setInt(4, contratacion.getIdTipoContratacion());
            preparedStatement.setDate(5, new java.sql.Date(contratacion.getFechaContratacion().getTime()));
            preparedStatement.setDouble(6, contratacion.getSalario());
            preparedStatement.setBoolean(7, contratacion.isEstado());

            // Ejecutar la consulta y verificar si se afectaron filas
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    contratacion.setIdContratacion(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear el registro de Contratación. Razón: " + e.getMessage());
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Razón: " + e.getMessage());
            }
        }

        return contratacion;
    }

    // Método para obtener todas las contrataciones
    public List<Contrataciones> obtenerContrataciones() {
        List<Contrataciones> contratacionesList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
// Obtener la conexión a la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "SELECT idContratacion, idDepartamento, idEmpleado, idCargo, idTipoContratacion, fechaContratacion, salario, estado FROM Contrataciones";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int idContratacion = resultSet.getInt("idContratacion");
                int idDepartamento = resultSet.getInt("idDepartamento");
                int idEmpleado = resultSet.getInt("idEmpleado");
                int idCargo = resultSet.getInt("idCargo");
                int idTipoContratacion = resultSet.getInt("idTipoContratacion");
                Date fechaContratacion = resultSet.getDate("fechaContratacion");
                double salario = resultSet.getDouble("salario");
                boolean estado = resultSet.getBoolean("estado");

                Contrataciones contratacion = new Contrataciones(idContratacion, idDepartamento, idEmpleado, idCargo, idTipoContratacion, fechaContratacion, salario, estado);
                contratacionesList.add(contratacion);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los registros de Contrataciones. Razón: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Razón: " + e.getMessage());
            }
        }

        return contratacionesList;
    }

    // Método para actualizar una contratación
    public boolean actualizarContratacion(Contrataciones contratacion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
// Obtener la conexión a la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "UPDATE Contrataciones SET idDepartamento = ?, idEmpleado = ?, idCargo = ?, idTipoContratacion = ?, fechaContratacion = ?, salario = ?, estado = ? WHERE idContratacion = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, contratacion.getIdDepartamento());
            preparedStatement.setInt(2, contratacion.getIdEmpleado());
            preparedStatement.setInt(3, contratacion.getIdCargo());
            preparedStatement.setInt(4, contratacion.getIdTipoContratacion());
            preparedStatement.setDate(5, new java.sql.Date(contratacion.getFechaContratacion().getTime()));
            preparedStatement.setDouble(6, contratacion.getSalario());
            preparedStatement.setBoolean(7, contratacion.isEstado());
            preparedStatement.setInt(8, contratacion.getIdContratacion());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el registro de Contratación. Razón: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Razón: " + e.getMessage());
            }
        }
    }

    // Método para eliminar una contratación
    public boolean eliminarContratacion(int idContratacion) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
// Obtener la conexión a la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "DELETE FROM Contrataciones WHERE idContratacion = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idContratacion);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el registro de Contratación. Razón: " + e.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Razón: " + e.getMessage());
            }
        }
    }

    // Método para obtener una contratación por ID
    public Contrataciones obtenerContratacionPorId(int idContratacion) {
        Contrataciones contratacion = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
// Obtener la conexión a la base de datos
        try {
            connection = Conexion.getConexion();
            String query = "SELECT idContratacion, idDepartamento, idEmpleado, idCargo, idTipoContratacion, fechaContratacion, salario, estado FROM Contrataciones WHERE idContratacion = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idContratacion);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idDepartamento = resultSet.getInt("idDepartamento");
                int idEmpleado = resultSet.getInt("idEmpleado");
                int idCargo = resultSet.getInt("idCargo");
                int idTipoContratacion = resultSet.getInt("idTipoContratacion");
                Date fechaContratacion = resultSet.getDate("fechaContratacion");
                double salario = resultSet.getDouble("salario");
                boolean estado = resultSet.getBoolean("estado");

                contratacion = new Contrataciones(idContratacion, idDepartamento, idEmpleado, idCargo, idTipoContratacion, fechaContratacion, salario, estado);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el registro de Contratación por ID. Razón: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los recursos. Razón: " + e.getMessage());
            }
        }
        return contratacion;
    }
}
