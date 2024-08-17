package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.stream.Collectors;

@WebServlet(name = "EmpleadosController", urlPatterns = {"/empleados/*"})
//Servlet que maneja el CRUD de la Tabla TipoContratacion
public class EmpleadosController extends HttpServlet {
    //Instancia de la Clase de Servicios
    private final EmpleadoServices empleadoServices = new EmpleadoServices();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd") .create();
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
        //Se obtiene el método que se ocupará
        String method = request.getMethod();
        //Realiza acciones dependiendo del metodo, POST o GET y da error si no se reconoce el metodo
        if ("POST".equals(method)) {
            //Obtener datos del body de la peticion
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //Transformar body a JSON
            JSONObject bodyJson = new JSONObject(requestData);
            //Obtener accion a realizar
            String action = bodyJson.getString("accion");
            //Se obtiene el JSON a usar
            String jsonString = bodyJson.getJSONObject("json").toString();
            //Se transforma el json a un objeto de tipo Empleado
            Empleado object = gson.fromJson(jsonString, Empleado.class);
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion no especificada");
                return;
            }
            //Llamada a la funcion que procesa la peticion post
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
            throw new ServletException("Ruta invalida.");
        }
        try {
            return Integer.parseInt(pathInfo.substring(1));  // Extrae el ID de la URL
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido");
        }
    }
    // Método para procesar solicitudes POST
    private void processPostRequest(String action, Empleado object, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertEmpleado(object,request, response);
                break;
            case "actualizar":
                updateEmpleado(id,object, request, response);
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
    private void insertEmpleado(Empleado object, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        empleadoServices.agregarEmpleado(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Empleado creado exitosamente\"}");
    }
    //Metodo que actualza un nuevo empleado
    private void updateEmpleado(int id, Empleado object, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            empleadoServices.actualizarEmpleado(object);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Empleado actualizado exitosamente\"}");
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
