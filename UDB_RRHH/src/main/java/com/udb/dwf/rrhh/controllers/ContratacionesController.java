package com.udb.dwf.rrhh.controllers;

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

//Servlet que maneja el CRUD de la Tabla Contrataciones
@WebServlet(name = "ContratacionesController", urlPatterns = {"/contrataciones/*"})
public class ContratacionesController extends HttpServlet {

    //Instancia de la Clase de Servicios de la Tabla Contrataciones
    private final ContratacionesServices contratacionesService = new ContratacionesServices();

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

        String method = request.getMethod();
        String action = request.getParameter("accion");

        if ("POST".equals(method)) {
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion no especificada");
                return;
            }
            processPostRequest(action, request, response);
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
    private void processPostRequest(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertContratacion(request, response);
                break;
            case "actualizar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }

                // Llama al método para actualizar la contratación con el ID proporcionado
                updateContratacion(id, request, response);
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
    private void insertContratacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ParseException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        JSONObject jsonObject = new JSONObject(data);

        Contrataciones nuevaContratacion = new Contrataciones(
                jsonObject.getInt("idDepartamento"),
                jsonObject.getInt("idEmpleado"),
                jsonObject.getInt("idCargo"),
                jsonObject.getInt("idTipoContratacion"),
                obtieneFechaFormateada(jsonObject.getString("fechaContratacion")),
                jsonObject.getDouble("salario"),
                jsonObject.getBoolean("estado")
        );

        contratacionesService.crearContratacion(nuevaContratacion);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Contratación creada exitosamente\"}");
    }
    // Método que actualiza una nueva Contratacion
    private void updateContratacion(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ParseException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String data = buffer.toString();

            JSONObject jsonObject = new JSONObject(data);
            Contrataciones contratacion = new Contrataciones(
                    id,
                    jsonObject.getInt("idDepartamento"),
                    jsonObject.getInt("idEmpleado"),
                    jsonObject.getInt("idCargo"),
                    jsonObject.getInt("idTipoContratacion"),
                    obtieneFechaFormateada(jsonObject.getString("fechaContratacion")),
                    jsonObject.getDouble("salario"),
                    jsonObject.getBoolean("estado")
            );

            contratacionesService.actualizarContratacion(contratacion);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Contratación actualizada exitosamente\"}");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado");
        }
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
