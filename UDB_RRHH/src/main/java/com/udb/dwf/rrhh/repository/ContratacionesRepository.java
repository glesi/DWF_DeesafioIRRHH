package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.Contrataciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContratacionesRepository {
    public List<Contrataciones> obtenerContratacion(){
        List<Contrataciones> contrataciones = new ArrayList<>();

        Connection connection= null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            connection= Conexion.getConexion();
            String query="select idDepartamento, idEmpleado, idCargo,idTipoContratacion, fechaContratacion, salario, estado from Contrataciones";
            preparedStatement= connection.prepareStatement(query);
            resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                int idDepartamento=resultSet.getInt("idDepartamento");
                int idEmpleado=resultSet.getInt("idEmpleado");
                int idCargo=resultSet.getInt("idCargo");
                int idTipoContratacion= resultSet.getInt("idTipoContratacion");
                Date fechaContratacion=resultSet.getDate("fechaContratacion");
                double salario=resultSet.getDouble("salario");
                boolean estado=resultSet.getBoolean("estado");
                Contrataciones contratacionesObj=new Contrataciones(idDepartamento, idEmpleado, idCargo,idTipoContratacion, fechaContratacion, salario, estado);
                contrataciones.add(contratacionesObj);

            }

        }catch (SQLException e){
            System.err.println("Error al obtener los registros de Contrataciones.Reason: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) Conexion.desconectar();
            }catch (SQLException e){
                System.err.println("Error al desconectar la conexion: " + e.getMessage());
            }
        }

        return contrataciones;
    }

}
