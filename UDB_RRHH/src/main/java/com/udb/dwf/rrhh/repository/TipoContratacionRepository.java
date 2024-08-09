package com.udb.dwf.rrhh.repository;

import com.udb.dwf.rrhh.pojos.TipoContratacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoContratacionRepository {

    public List<TipoContratacion> obtenerTipoContrataciones(){
        List<TipoContratacion> tipoContratacionList = new ArrayList<TipoContratacion>();

        Connection connection= null;
        PreparedStatement preparedStatement=  null;
        ResultSet resultSet= null;

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
}
