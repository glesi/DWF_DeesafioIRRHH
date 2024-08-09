package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.Cargo;
import com.udb.dwf.rrhh.repository.CargosRepository;

import java.util.List;

public class CargosServices {
    private final CargosRepository cargosRepository = new CargosRepository();

    public List<Cargo> obtenerCargos() {
        return cargosRepository.obtenerCargos();
    }

}
