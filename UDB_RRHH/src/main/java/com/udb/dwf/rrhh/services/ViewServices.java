package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.View;
import com.udb.dwf.rrhh.repository.ViewRepository;

import java.util.List;

public class ViewServices {
    private final ViewRepository viewRepository = new ViewRepository();

    //read
    public List<View> getViews(){
        return viewRepository.getViews();
    }

    //Obtener detalles de empleado por ID
    public View getViewById(int idEmpleado){
        return viewRepository.getViewById(idEmpleado);
    }
}
