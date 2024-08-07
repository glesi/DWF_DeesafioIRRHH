package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContrataciones;
import com.udb.dwf.rrhh.services.TipoContracionesServices;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/tipoContrataciones")
public class TipoContratacionController extends HttpServlet {
    private TipoContracionesServices tipoContratacionService = new TipoContracionesServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TipoContrataciones> tipoContratacionesList = tipoContratacionService.obtenerTodos();
        request.setAttribute("tipoContrataciones", tipoContratacionesList);
        request.getRequestDispatcher("/WEB-INF/views/tipoContrataciones.jsp").forward(request, response);
    }
}
