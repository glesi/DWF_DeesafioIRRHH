package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

//Servlet que maneja el CRUD de la Tabla TipoContratacion
public class TipoContratacionController extends HttpServlet {

    Gson gson = new GsonBuilder().create();


    //Instancia de la Clase de Servicios de la Tabla TipoContratacion
    private final TipoContratacionesServices tipoContratacionService = new TipoContratacionesServices();

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        String method = request.getMethod(); // Parámetro de consulta para la acción
        String requestData = request.getReader().lines().collect(Collectors.joining());
        JSONObject bodyJSON = new JSONObject(requestData);
        String action = bodyJSON.getString("action");
        String jsonString = bodyJSON.getJSONObject("json").toString();
        TipoContratacion object = gson.fromJson(jsonString, TipoContratacion.class);
        if ("POST".equals(method)) {
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion no especificada");
                return;
            }
            processPostRequest(action, object, request, response);
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
            throws IOException {

        int id = object.getIdTipoContratacion();

        switch (action) {
            case "insertar" -> insertTipoContratacion(object, response);
            case "actualizar" -> {
                if (id == 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }
                updateTipoContratacion(object, response);
            }
            case "eliminar" -> {
                if (id == 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteTipoContratacion(id, response);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion desconocida");
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

    private void insertTipoContratacion(TipoContratacion object, HttpServletResponse response)
            throws IOException {
        TipoContratacion newTipoContratacion = new TipoContratacion(0, object.getTipoContratacion());
        tipoContratacionService.crearTipoContratacion(newTipoContratacion);
        JSONObject json = new JSONObject();
        json.put("message", "Tipo de contratación creado exitosamente");
        response.getWriter().println(json);
    }

    private void updateTipoContratacion(TipoContratacion object, HttpServletResponse response)
            throws IOException {
        tipoContratacionService.actualizarTipoContratacion(object);
        JSONObject json = new JSONObject();
        json.put("message", "Tipo de contratación actualizado exitosamente");
        response.getWriter().println(json);
    }

    private void deleteTipoContratacion(int id, HttpServletResponse response)
            throws IOException {
        tipoContratacionService.eliminarTipoContratacion(id);
        JSONObject json = new JSONObject();
        json.put("message", "Tipo de contratación eliminado exitosamente");
        response.getWriter().println(json);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}