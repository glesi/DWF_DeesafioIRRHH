package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.repository.TipoContratacionRepository;

import java.util.List;

public class TipoContratacionesServices {

    private final TipoContratacionRepository tipoContratacionRepository = new TipoContratacionRepository();

    public List<TipoContratacion> obtenerTodosTipoContratacion() {
        return tipoContratacionRepository.obtenerTipoContrataciones();
    }

}
