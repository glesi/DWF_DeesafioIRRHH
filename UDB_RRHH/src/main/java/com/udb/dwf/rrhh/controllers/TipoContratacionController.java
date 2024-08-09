package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
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

    //Función que realiza el GET del Servlet TipoContratacion
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
}