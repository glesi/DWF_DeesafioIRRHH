package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class TipoContratacionController extends HttpServlet {
    private final TipoContratacionesServices tipoContratacionService = new TipoContratacionesServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TipoContratacion> tipoContratacionList = tipoContratacionService.obtenerTodosTipoContratacion();
        request.setAttribute("tipoContrataciones", tipoContratacionList);
        request.getRequestDispatcher("/WEB-INF/views/tipoContrataciones.jsp").forward(request, response);
    }
}