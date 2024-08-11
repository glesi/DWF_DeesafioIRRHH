package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.Cargo;
import com.udb.dwf.rrhh.repository.CargosRepository;

import java.util.List;

public class CargosServices {
    private final CargosRepository cargosRepository = new CargosRepository();

    // Metodo para crear Cargos
    public Cargo crearCargo(Cargo cargo) {
        return cargosRepository.crearCargo(cargo);
    }
    // Metodo para leer Cargos
    public List<Cargo> obtenerCargos() {
        return cargosRepository.obtenerCargos();
    }
    // Metodo para actualizar Cargos
    public boolean actualizarCargo(Cargo Cargo) {
        return cargosRepository.actualizarCargo(Cargo);
    }

    // Obtener Cargos por ID
    public Cargo obtenerCargoPorId(int idCargo) {
        return cargosRepository.obtenerCargoPorId(idCargo);
    }

    public boolean eliminarCargo(int idCargo){
        return cargosRepository.eliminarCargo(idCargo);
    }

}
