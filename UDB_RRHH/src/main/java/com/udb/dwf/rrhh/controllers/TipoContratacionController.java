package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "TipoContratacionController", urlPatterns = {"/tipoContratacion/*"})
public class TipoContratacionController extends HttpServlet {
    // Se instancia el servicio para gestionar las operaciones relacionadas con 'TipoContratacion'
    private final TipoContratacionesServices tipoContratacionService = new TipoContratacionesServices();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Llama al método 'processRequest' para manejar las solicitudes GET
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Llama al método 'processRequest' para manejar las solicitudes POST
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
     // Obtiene el método HTTP de la solicitud (GET, POST, etc.)
        String method = request.getMethod();
//        String action = request.getParameter("accion");
     //Verifica el método HTTP y procesa la solicitud en consecuencia
        if ("POST".equals(method)) {
            //Se obtiene los datos del Body
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //Transforma el body del request en JSON
            JSONObject bodyJson = new JSONObject(requestData);
            //Obtener propiedad accion del request
            String action = bodyJson.getString("accion");
            //Se obtiene el JSON a utilizar
            String jsonString = bodyJson.getJSONObject("json").toString();
            //Se transforma el contenido del JSON a un objeto tipo TipoContratacion
            TipoContratacion object = gson.fromJson(jsonString, TipoContratacion.class);
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion no especificada");
                return;
            }
            processPostRequest(action,object, request, response);
        } else if ("GET".equals(method)) {
            processGetRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Método no implementado");
        }
    }

    private Integer extractIdFromPathInfo(String pathInfo) throws ServletException {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ServletException("Ruta invalida.");
        }
        try {
            return Integer.parseInt(pathInfo.substring(1));  // Extrae el ID de la URL
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido");
        }
    }

    private void processPostRequest(String action, TipoContratacion object, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertTipoContratacion(object,request, response);
                break;
            case "actualizar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }
                updateTipoContratacion(id,object, request, response);
                break;
            case "eliminar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteTipoContratacion(id, request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion desconocida");
        }
    }

    private void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listTipoContratacion(response);
        } else {
            try {
                Integer id = extractIdFromPathInfo(pathInfo);
                getTipoContratacionById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    private void getTipoContratacionById(Integer id, HttpServletResponse response)
            throws IOException {
        TipoContratacion tipoContratacion = tipoContratacionService.obtenerTipoContratacionPorId(id);
        if (tipoContratacion != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(tipoContratacion));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tipo de contratación no encontrado");
        }
    }

    private void listTipoContratacion(HttpServletResponse response)
            throws IOException {
        List<TipoContratacion> tipoContratacionList = tipoContratacionService.obtenerTodosTipoContratacion();
        JSONArray json = new JSONArray(tipoContratacionList);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();
    }

    private void insertTipoContratacion(TipoContratacion object, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        tipoContratacionService.crearTipoContratacion(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Tipo de contratación creado exitosamente\"}");
    }

    private void updateTipoContratacion(int id, TipoContratacion object, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        tipoContratacionService.actualizarTipoContratacion(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Tipo de contratación actualizado exitosamente\"}");
    }

    private void deleteTipoContratacion(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        tipoContratacionService.eliminarTipoContratacion(id);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Tipo de contratación eliminado exitosamente\"}");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
