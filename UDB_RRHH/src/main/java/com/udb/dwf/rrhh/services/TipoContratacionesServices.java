package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.repository.TipoContratacionRepository;
import java.util.List;
public class TipoContratacionesServices {

    private final TipoContratacionRepository tipoContratacionRepository = new TipoContratacionRepository();

    // Create
    public TipoContratacion crearTipoContratacion(TipoContratacion tipoContratacion) {
        return tipoContratacionRepository.crearTipoContratacion(tipoContratacion);
    }

    //read
    public List<TipoContratacion> obtenerTodosTipoContratacion() {
        return tipoContratacionRepository.obtenerTipoContrataciones();
    }

    // Update
    public boolean actualizarTipoContratacion(TipoContratacion tipoContratacion) {
        return tipoContratacionRepository.actualizarTipoContratacion(tipoContratacion);
    }

    // Delete
    public boolean eliminarTipoContratacion(int idTipoContratacion) {
        return tipoContratacionRepository.eliminarTipoContratacion(idTipoContratacion);
    }

    // Obtener TipoContratacion por ID
    public TipoContratacion obtenerTipoContratacionPorId(int idTipoContratacion) {
        return tipoContratacionRepository.obtenerTipoContratacionPorId(idTipoContratacion);
    }

}

