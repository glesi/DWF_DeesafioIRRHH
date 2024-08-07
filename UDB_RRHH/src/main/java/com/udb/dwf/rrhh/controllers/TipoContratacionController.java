package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContrataciones;
import com.udb.dwf.rrhh.services.TipoContracionesServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class TipoContratacionController extends HttpServlet {
    private final TipoContracionesServices tipoContratacionService = new TipoContracionesServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TipoContrataciones> tipoContratacionesList = tipoContratacionService.obtenerTodos();
        request.setAttribute("tipoContrataciones", tipoContratacionesList);
        request.getRequestDispatcher("/WEB-INF/views/tipoContrataciones.jsp").forward(request, response);
    }
}