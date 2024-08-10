package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.Departamento;
import com.udb.dwf.rrhh.services.DepartamentoServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.List;

@WebServlet("/departamento")
public class DepartamentoController extends HttpServlet {

    private final DepartamentoServices departamentoServices = new DepartamentoServices();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "edit":
                mostrarFormularioEdicion(request, response);
                break;
            case "delete":
                eliminarDepartamento(request, response);
                break;
            case "insert":
                insertarDepartamento(request, response);
                break;
            case "update":
                actualizarDepartamento(request, response);
                break;
            default:
                listarDepartamentos(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void listarDepartamentos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Departamento> listaDepartamentos = departamentoServices.obtenerDepartamentos();
        request.setAttribute("departamentos", listaDepartamentos);
        request.getRequestDispatcher("/departamentos.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idDepartamento = Integer.parseInt(request.getParameter("id"));
        Departamento departamento = departamentoServices.obtenerDepartamento(idDepartamento);
        request.setAttribute("departamento", departamento);
        request.getRequestDispatcher("/editarDepartamento.jsp").forward(request, response);
    }

    private void insertarDepartamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombreDepartamento = request.getParameter("nombreDepartamento");
        String descripcionDepartamento = request.getParameter("descripcionDepartamento");

        Departamento departamento = new Departamento();
        departamento.setNombreDepartamento(nombreDepartamento);
        departamento.setDescripcionDepartamento(descripcionDepartamento);

        departamentoServices.agregarDepartamento(departamento);
        response.sendRedirect("departamento?action=list");
    }

    private void actualizarDepartamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idDepartamento = Integer.parseInt(request.getParameter("id"));
        String nombreDepartamento = request.getParameter("nombreDepartamento");
        String descripcionDepartamento = request.getParameter("descripcionDepartamento");

        Departamento departamento = new Departamento();
        departamento.setIdDepartamento(idDepartamento);
        departamento.setNombreDepartamento(nombreDepartamento);
        departamento.setDescripcionDepartamento(descripcionDepartamento);

        departamentoServices.actualizarDepartamento(departamento);
        response.sendRedirect("departamento?action=list");
    }

    private void eliminarDepartamento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idDepartamento = Integer.parseInt(request.getParameter("id"));
        departamentoServices.eliminarDepartamento(idDepartamento);
        response.sendRedirect("departamento?action=list");
    }
}
