package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.Contrataciones;
import com.udb.dwf.rrhh.repository.ContratacionesRepository;

import java.util.List;

public class ContratacionesServices {
    private final ContratacionesRepository contratacionesRepository = new ContratacionesRepository();

    public List<Contrataciones> obtenerContratacion() {
        return contratacionesRepository.obtenerContratacion();
    }
}
