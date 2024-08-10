package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

//Servlet que maneja el CRUD de la Tabla TipoContratacion
//La anotación @WebServlet declara que esta clase es ocupada como Servlet
@WebServlet(name = "TipoContratacionController", urlPatterns = "/contratacionController")
public class TipoContratacionController extends HttpServlet {

    //Instancia de la Clase de Servicios de la Tabla TipoContratacion
    private final TipoContratacionesServices tipoContratacionService = new TipoContratacionesServices();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new" -> showNewForm(request, response);
            case "insert" -> insertTipoContratacion(request, response);
            case "delete" -> deleteTipoContratacion(request, response);
            case "edit" -> showEditForm(request, response);
            case "update" -> updateTipoContratacion(request, response);
            default -> listTipoContratacion(response);
        }
    }

    //Función que manda la lista de los tipos de contratación
    private void listTipoContratacion(HttpServletResponse response)
            throws IOException {
        //De la instancia de la Clase de Servicios, obtenemos los tipos de contratación
        List<TipoContratacion> tipoContratacionList = tipoContratacionService.obtenerTodosTipoContratacion();
        //Pasamos la lista de los tipos de contratación a un objeto JSONArray
        JSONArray json = new JSONArray(tipoContratacionList);
        //Obtenemos de la respuesta el escritor que nos ayudará a editar la respuesta
        PrintWriter out = response.getWriter();
        //Configuramos la respuesta que el contenido sea "APLICATION/JSON" y que la codificación sea UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //Y finalizamos imprimiendo en la respuesta el JSON con la lista de los tipos de contratación
        out.println(json);
        out.flush();
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

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}