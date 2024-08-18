package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.Contrataciones;
import com.udb.dwf.rrhh.services.ContratacionesServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

//Servlet que maneja el CRUD de la Tabla Contrataciones
@WebServlet(name = "ContratacionesController", urlPatterns = {"/contrataciones/*"})
public class ContratacionesController extends HttpServlet {

    //Instancia de la Clase de Servicios de la Tabla Contrataciones
    private final ContratacionesServices contratacionesService = new ContratacionesServices();

    //Creacion de gson capaz de serializar la fecha.
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    // Método que maneja solicitudes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    // Método común para procesar tanto solicitudes GET como POST
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        //Se obtiene el metodo de la peticion
        String method = request.getMethod();
        //verifica el metodo para procesar la peticion
        if ("POST".equals(method)) {
            //Obtener los datos del body de la request
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //Transformar el body a JSON
            JSONObject bodyJSON = new JSONObject(requestData);
            //Obtener la accion a ejecutar
            String action = bodyJSON.getString("accion");
            //Obtencion del JSON con la informacion de la contratacion
            String jsonString = bodyJSON.getJSONObject("json").toString();
            //Creacion del objeto de tipo Contrataciones
            Contrataciones object = gson.fromJson(jsonString, Contrataciones.class);
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
    // Metodo para extraer Id por ruta
    private Integer extractIdFromPathInfo(String pathInfo) throws ServletException {
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        try {
            return Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido");
        }
    }
    // Método para procesar solicitudes POST
    private void processPostRequest(String action, Contrataciones object, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertContratacion(object,request, response);
                break;
            case "actualizar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }

                // Llama al método para actualizar la contratación con el ID proporcionado
                updateContratacion(id,object, request, response);
                break;
            case "eliminar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }

                // Llama al método para eliminar la contratación con el ID proporcionado
                deleteContratacion(id, request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion desconocida");
        }
    }
    // Método para procesar solicitudes GET
    private void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listContrataciones(response);
        } else {
            try {
                Integer id = extractIdFromPathInfo(pathInfo);
                getContratacionById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    private void getContratacionById(Integer id, HttpServletResponse response)
            throws IOException {
        Contrataciones contratacion = contratacionesService.obtenerContratacionPorId(id);
        if (contratacion != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(contratacion));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contratación no encontrada");
        }
    }
    //Función que manda la lista de las contrataciónes
    private void listContrataciones(HttpServletResponse response)
            throws IOException {
        List<Contrataciones> contratacionesList = contratacionesService.obtenerContrataciones();
        JSONArray json = new JSONArray(contratacionesList);
        //Obtenemos de la respuesta el escritor que nos ayudará a editar la respuesta
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();
    }
    // Método que inserta una nueva Contratacion
    private void insertContratacion(Contrataciones object, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ParseException {
        contratacionesService.crearContratacion(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Contratación creada exitosamente\"}");
    }
    // Método que actualiza una nueva Contratacion
    private void updateContratacion(int id, Contrataciones object, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ParseException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            contratacionesService.actualizarContratacion(object);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Contratación actualizada exitosamente\"}");

    }

    private java.sql.Date obtieneFechaFormateada(String fecha) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  // Define el formato esperado
        java.util.Date parsedDate = dateFormat.parse(fecha);
        return new java.sql.Date(parsedDate.getTime());
    }

    // Método que elimina una nueva Contratacion
    private void deleteContratacion(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            contratacionesService.eliminarContratacion(id);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Contratación eliminada exitosamente\"}");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    // Método que configura los encabezados CORS para permitir solicitudes desde cualquier origen
    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
