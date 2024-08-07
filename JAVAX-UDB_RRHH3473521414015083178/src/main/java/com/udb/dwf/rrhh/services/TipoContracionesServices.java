package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.TipoContrataciones;
import com.udb.dwf.rrhh.repository.TipoContratacionRepository;

import java.util.List;

public class TipoContracionesServices {

    private final TipoContratacionRepository tipoContratacionRepository = new TipoContratacionRepository();

    public List<TipoContrataciones> obtenerTodos() {
        return tipoContratacionRepository.obtenerTipoContrataciones();
    }

}
