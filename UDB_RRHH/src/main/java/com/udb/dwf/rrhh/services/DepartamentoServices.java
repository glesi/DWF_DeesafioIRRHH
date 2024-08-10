package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.Departamento;
import com.udb.dwf.rrhh.repository.DepartamentoRepository;

import java.util.List;

public class DepartamentoServices {

    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    // Constructor utilizado
    public DepartamentoServices() {}

    // Método para obtener todos los departamentos
    public List<Departamento> obtenerDepartamentos() {
        return this.departamentoRepository.obtenerDepartamento();
    }

    // Método utilizado para obtener un departamento por su ID
    public Departamento obtenerDepartamento(int id) {
        return this.departamentoRepository.obtenerDepartamentoPorId(id);
    }

    // Método para agregar un nuevo departamento
    public Departamento agregarDepartamento(Departamento departamento) {
        return this.departamentoRepository.crearContratacion(departamento);
    }

    // Método para actualizar un departamento existente
    public boolean actualizarDepartamento(Departamento departamento) {
        return this.departamentoRepository.actualizarDepartamento(departamento);
    }

    // Método para eliminar un departamento por su ID
    public boolean eliminarDepartamento(int idDepartamento) {
        return this.departamentoRepository.eliminarDepartamento(idDepartamento);
    }
}
