package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.pojos.View;
import com.udb.dwf.rrhh.services.ViewServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

//Servlet que maneja el CRUD de la Tabla View
public class ViewController extends HttpServlet {

    //Instancia de la Clase de Servicios de la Tabla View
    private final ViewServices services = new ViewServices();


    //Función que manda la vista de los objetos
    protected void listViewController(HttpServletResponse response) {
        //Se obtiene del servicio la lista
        List<View> viewList = services.getViews();
        /*
            Se realiza un try, si llega a haber el error IOException, tirará un error
         */
        try {
            //Obtenemos de la respuesta el writer que nos ayudará a editar la respuesta
            PrintWriter out = response.getWriter();
            //Configuramos la respuesta que el contenido sea "APPLICATION/JSON" y que la codificación sea UTF-8
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            //Y finalizamos imprimiendo en la respuesta el JSON con la vista
            JSONArray jsonArray = new JSONArray(viewList);
            out.println(jsonArray);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Función que procesa las peticiones en general
    protected void processRequest(HttpServletResponse response) {
        //Se inserta los headers necesarios para evitar error CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        //Llama la función para proveer la vista de los objetos
        listViewController(response);
    }

    //Función que recibe las peticiones GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(response);
    }
}
