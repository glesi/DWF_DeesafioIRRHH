package com.udb.dwf.rrhh.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import com.udb.dwf.rrhh.pojos.Empleado;
import com.udb.dwf.rrhh.services.EmpleadoServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EmpleadoController", urlPatterns = {"/EmpleadoController"})
public class EmpleadoController extends HttpServlet {

    private final EmpleadoServices empleadoServices = new EmpleadoServices();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        String action = request.getParameter("action");

        switch (action) {
            case "listar":
                listarEmpleados(request, response);
                break;
            case "crear":
                crearEmpleado(request, response);
                break;
            case "editar":
                editarEmpleado(request, response);
                break;
            case "actualizar":
                actualizarEmpleado(request, response);
                break;
            case "eliminar":
                eliminarEmpleado(request, response);
                break;
            default:
                listarEmpleados(request, response);
                break;
        }
    }

    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Empleado> empleados = empleadoServices.obtenerEmpleados();
        request.setAttribute("empleados", empleados);
        request.getRequestDispatcher("empleados.jsp").forward(request, response);
    }

    private void crearEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombrePersona");
        String usuario = request.getParameter("usuario");
        String dui = request.getParameter("numeroDui");
        String telefono = request.getParameter("numeroTelefono");
        String correo = request.getParameter("correoInstitucional");
        String fechaNacimiento = request.getParameter("fechaNacimiento");

        Empleado empleado = new Empleado(0, dui, nombre, usuario, telefono, correo, java.sql.Date.valueOf(fechaNacimiento));
        empleadoServices.agregarEmpleado(empleado);
        listarEmpleados(request, response);
    }

    private void editarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idEmpleado = Integer.parseInt(request.getParameter("id"));
        Empleado empleado = empleadoServices.obtenerEmpleado(idEmpleado);
        request.setAttribute("empleado", empleado);
        request.getRequestDispatcher("editarEmpleado.jsp").forward(request, response);
    }

    private void actualizarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        String nombre = request.getParameter("nombrePersona");
        String usuario = request.getParameter("usuario");
        String dui = request.getParameter("numeroDui");
        String telefono = request.getParameter("numeroTelefono");
        String correo = request.getParameter("correoInstitucional");
        String fechaNacimiento = request.getParameter("fechaNacimiento");

        Empleado empleado = new Empleado(idEmpleado, dui, nombre, usuario, telefono, correo, java.sql.Date.valueOf(fechaNacimiento));
        empleadoServices.actualizarEmpleado(empleado);
        listarEmpleados(request, response);
    }

    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idEmpleado = Integer.parseInt(request.getParameter("id"));
        empleadoServices.eliminarEmpleado(idEmpleado);
        listarEmpleados(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
