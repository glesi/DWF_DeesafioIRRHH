package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.Contrataciones;
import com.udb.dwf.rrhh.repository.ContratacionesRepository;

import java.util.List;

public class ContratacionesServices {

    private final ContratacionesRepository contratacionesRepository;

    public ContratacionesServices() {
        this.contratacionesRepository = new ContratacionesRepository();
    }

    // Método para crear una nueva contratación
    public Contrataciones crearContratacion(Contrataciones contratacion) {
        return contratacionesRepository.crearContratacion(contratacion);
    }

    // Método para obtener todas las contrataciones
    public List<Contrataciones> obtenerContrataciones() {
        return contratacionesRepository.obtenerContrataciones();
    }

    // Método para obtener una contratación por ID
    public Contrataciones obtenerContratacionPorId(int idContratacion) {
        return contratacionesRepository.obtenerContratacionPorId(idContratacion);
    }

    // Método para actualizar una contratación
    public boolean actualizarContratacion(Contrataciones contratacion) {
        return contratacionesRepository.actualizarContratacion(contratacion);
    }

    // Método para eliminar una contratación
    public boolean eliminarContratacion(int idContratacion) {
        return contratacionesRepository.eliminarContratacion(idContratacion);
    }
}
