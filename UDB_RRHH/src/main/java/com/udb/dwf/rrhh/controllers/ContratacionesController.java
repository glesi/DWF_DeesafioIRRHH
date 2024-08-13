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
import java.util.List;

//Servlet que maneja el CRUD de la Tabla Contrataciones
@WebServlet(name = "ContratacionesController", urlPatterns = {"/contrataciones/*"})
public class ContratacionesController extends HttpServlet {

    //Instancia de la Clase de Servicios de la Tabla Contrataciones
    private final ContratacionesServices contratacionesService = new ContratacionesServices();

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

    private void processPostRequest(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = extractIdFromPathInfo(pathInfo);

        switch (action) {
            case "insertar":
                insertContratacion(request, response);
                break;
            case "actualizar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }
                updateContratacion(id, request, response);
                break;
            case "eliminar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteContratacion(id, request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion desconocida");
        }
    }

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

    private void listContrataciones(HttpServletResponse response)
            throws IOException {
        List<Contrataciones> contratacionesList = contratacionesService.obtenerContrataciones();
        JSONArray json = new JSONArray(contratacionesList);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();
    }

    private void insertContratacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
                jsonObject.getInt("idEmpleada"),
                jsonObject.getInt("idCargo"),
                jsonObject.getInt("idTipoContratacion"),
                new java.util.Date(jsonObject.getLong("fechaContratacion")),
                jsonObject.getDouble("salario"),
                jsonObject.getBoolean("estado")
        );

        contratacionesService.crearContratacion(nuevaContratacion);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Contratación creada exitosamente\"}");
    }

    private void updateContratacion(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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
                    jsonObject.getInt("idEmpleada"),
                    jsonObject.getInt("idCargo"),
                    jsonObject.getInt("idTipoContratacion"),
                    new java.util.Date(jsonObject.getLong("fechaContratacion")),
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

    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
