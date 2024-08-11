package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargosRepository {

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
}

