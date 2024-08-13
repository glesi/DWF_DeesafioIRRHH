package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.View;
import com.udb.dwf.rrhh.repository.ViewRepository;

import java.util.List;

public class ViewServices {
    private final ViewRepository viewRepository = new ViewRepository();

    public List<View> getViews(){
        return viewRepository.getViews();
    }
}
