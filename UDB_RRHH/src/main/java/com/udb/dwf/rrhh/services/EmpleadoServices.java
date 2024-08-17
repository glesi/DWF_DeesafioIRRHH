package com.udb.dwf.rrhh.services;

import com.udb.dwf.rrhh.pojos.Empleado;
import com.udb.dwf.rrhh.repository.EmpleadoRepository;

import java.util.List;

public class EmpleadoServices {

    private final EmpleadoRepository empleadoRepository = new EmpleadoRepository();

    public EmpleadoServices() {}

    public List<Empleado> obtenerEmpleados() {
        return this.empleadoRepository.obtenerEmpleado();
    }

    public Empleado obtenerEmpleado(int id) {
        return this.empleadoRepository.obtenerEmpleadoPorId(id);
    }

    public Empleado agregarEmpleado(Empleado empleado) {
        return this.empleadoRepository.crearEmpleado(empleado);
    }

    public boolean actualizarEmpleado(Empleado empleado) {
        return this.empleadoRepository.actualizarEmpleado(empleado);
    }

    public boolean eliminarEmpleado(int idEmpleado) {
        return this.empleadoRepository.eliminarEmpleado(idEmpleado);
    }
}
