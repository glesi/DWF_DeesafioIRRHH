package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


public class TipoContratacionController extends HttpServlet {
    private final TipoContratacionesServices tipoContratacionService = new TipoContratacionesServices();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "insert":
                insertTipoContratacion(request, response);
                break;
            case "delete":
                deleteTipoContratacion(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "update":
                updateTipoContratacion(request, response);
                break;
            default:
                listTipoContratacion(request, response);
                break;
        }
    }

    private void listTipoContratacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TipoContratacion> listTipoContratacion = tipoContratacionService.obtenerTodosTipoContratacion();
        request.setAttribute("listTipoContratacion", listTipoContratacion);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/tipoContratacion.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/tipoContratacionForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        TipoContratacion existingTipoContratacion = tipoContratacionService.obtenerTipoContratacionPorId(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/tipoContratacionForm.jsp");
        request.setAttribute("tipoContratacion", existingTipoContratacion);
        dispatcher.forward(request, response);
    }

    private void insertTipoContratacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String tipoContratacionName = request.getParameter("tipoContratacion");
        TipoContratacion newTipoContratacion = new TipoContratacion(0, tipoContratacionName);
        tipoContratacionService.crearTipoContratacion(newTipoContratacion);
        response.sendRedirect("tipoContratacion");
    }

    private void updateTipoContratacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String tipoContratacionName = request.getParameter("tipoContratacion");

        TipoContratacion tipoContratacion = new TipoContratacion(id, tipoContratacionName);
        tipoContratacionService.actualizarTipoContratacion(tipoContratacion);
        response.sendRedirect("tipoContratacion");
    }

    private void deleteTipoContratacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        tipoContratacionService.eliminarTipoContratacion(id);
        response.sendRedirect("tipoContratacion");
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
}