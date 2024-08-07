package com.udb.dwf.rrhh;

import com.udb.dwf.rrhh.pojos.Empleado;
import com.udb.dwf.rrhh.repository.EmpleadosDAO;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EmpleadosDAO empleadoDAO = new EmpleadosDAO();
        List<Empleado> emp = empleadoDAO.getEmployees();

        if(((List<?>) emp).isEmpty()){
            System.out.println("No se encontraron empleados contratados");
        }else{
            System.out.println("Empleados:");
            for(Empleado e : emp){
                System.out.println(e.getIdEmpleado());
                System.out.println(e.getNombrePersona());
                System.out.println(e.getNumeroDui());
                System.out.println(e.getNumeroTelefono());
                System.out.println(e.getUsuario());
                System.out.println(e.getCorreoInstitucional());
                System.out.println(e.getFechaNacimiento());
                System.out.println("------------------------------------");
            }
        }
    }
}