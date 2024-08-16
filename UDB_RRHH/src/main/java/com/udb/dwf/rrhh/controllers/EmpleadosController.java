package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.Empleado;
import com.udb.dwf.rrhh.services.EmpleadoServices;
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

@WebServlet(name = "EmpleadosController", urlPatterns = {"/empleados/*"})
//Servlet que maneja el CRUD de la Tabla TipoContratacion
public class EmpleadosController extends HttpServlet {
    //Instancia de la Clase de Servicios
    private final EmpleadoServices empleadoServices = new EmpleadoServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    // Método que maneja solicitudes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    // Método común para procesar tanto solicitudes GET como POST
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
    // Metodo para extraer Id por ruta
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
    // Método para procesar solicitudes POST
    private void processPostRequest(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertEmpleado(request, response);
                break;
            case "actualizar":
                updateEmpleado(id, request, response);
                break;
            case "eliminar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteEmpleado(id, request, response);
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
            listEmpleados(response);
        } else {
            try {
                Integer id = extractIdFromPathInfo(pathInfo);
                getEmpleadoById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }
    // Método para obtener un empleado por su ID y enviar la respuesta en formato JSON
    private void getEmpleadoById(Integer id, HttpServletResponse response)
            throws IOException {
        Empleado empleado = empleadoServices.obtenerEmpleado(id);
        if (empleado != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(empleado));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Empleado no encontrado");
        }
    }
    //Función que manda la lista de los empleados
    private void listEmpleados(HttpServletResponse response)
            throws IOException {
        List<Empleado> empleadosList = empleadoServices.obtenerEmpleados();
        JSONArray json = new JSONArray(empleadosList);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();
    }
    // Método que inserta un nuevo empleado
    private void insertEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        JSONObject jsonObject = new JSONObject(data);
        String nombre = jsonObject.getString("nombrePersona");
        String usuario = jsonObject.getString("usuario");
        String dui = jsonObject.getString("numeroDui");
        String telefono = jsonObject.getString("numeroTelefono");
        String correo = jsonObject.getString("correoInstitucional");
        String fechaNacimiento = jsonObject.getString("fechaNacimiento");

        Empleado newEmpleado = new Empleado(0, dui, nombre, usuario, telefono, correo, java.sql.Date.valueOf(fechaNacimiento));
        empleadoServices.agregarEmpleado(newEmpleado);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Empleado creado exitosamente\"}");
    }
    //Metodo que actualza un nuevo empleado
    private void updateEmpleado(int id, HttpServletRequest request, HttpServletResponse response)
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
            String nombre = jsonObject.getString("nombrePersona");
            String usuario = jsonObject.getString("usuario");
            String dui = jsonObject.getString("numeroDui");
            String telefono = jsonObject.getString("numeroTelefono");
            String correo = jsonObject.getString("correoInstitucional");
            String fechaNacimiento = jsonObject.getString("fechaNacimiento");

            // Crea un objeto Empleado con los datos proporcionados
            Empleado empleado = new Empleado(id, dui, nombre, usuario, telefono, correo, java.sql.Date.valueOf(fechaNacimiento));
            empleadoServices.actualizarEmpleado(empleado);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Empleado actualizado exitosamente\"}");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado");
        }
    }
    // Método que elimina un empleado
    private void deleteEmpleado(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            empleadoServices.eliminarEmpleado(id);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Empleado eliminado exitosamente\"}");
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
