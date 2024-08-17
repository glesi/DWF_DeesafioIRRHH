package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.Departamento;
import com.udb.dwf.rrhh.services.DepartamentoServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
// Anotación que define este Servlet con el nombre "DepartamentoController"
@WebServlet(name = "DepartamentoController", urlPatterns = "/departamento/*")
// Clase que utilizaremos para manejar solicitudes HTTP en la aplicación
public class DepartamentoController extends HttpServlet {
    // Instancia de la clase DepartamentoServices para interactuar con la lógica de negocio
    private final DepartamentoServices departamentoServices = new DepartamentoServices();
    //Creador GSON para crear objetos desde un JSON
    Gson gson = new GsonBuilder().create();
    // Método que maneja solicitudes GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    // Método que maneja solicitudes POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    // Método que maneja solicitudes OPTIONS
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    // Método común para procesar tanto solicitudes GET como POST
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        //Se obtiene el método que se ocupará
        String method = request.getMethod();
        /*
            Verifica que método se ocupará, si es POST realizará el procedimiento para la petición POST,
            si es GET, realizara el procedimiento para la petición GET,
            si no, tirará error que no existe algún método implementado
         */
    // Verifica si el método es POST
        if ("POST".equals(method)) {
            //Se obtiene los datos del Body
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //Transforma el Body a JSON
            JSONObject bodyJSON = new JSONObject(requestData);
            //Se obtiene la acción que se ejecutará
            String action = bodyJSON.getString("accion");
            //Se obtiene el JSON a utilizar y luego lo transforma al objeto Departamento
            String jsonString = bodyJSON.getJSONObject("json").toString();
            Departamento object = gson.fromJson(jsonString, Departamento.class);
            /*
                Si la acción es nula, tirará el error de que no sé específico la acción
             */
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada");
            }
            //Se llama la función que realizará el procedimiento de la petición POST
            processPostRequest(action, object,request, response);

        } else if ("GET".equals(method)) {
            processGetRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Método no implementado");
        }
    }
    // Método para procesar solicitudes POST
    private void processPostRequest(String action, Departamento object, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertDepartamento(object,request, response);
                break;
            case "actualizar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }
                updateDepartamento(id, object, request, response);
                break;
            case "eliminar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteDepartamento(id, request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción desconocida");
        }
    }
    // Método para procesar solicitudes GET
    private void processGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listDepartamentos(response);
        } else {
            try {
                Integer id = extractIdFromPathInfo(pathInfo);
                getDepartamentoById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }
    // Método que extrae el ID de la información de la ruta en la URL
    private Integer extractIdFromPathInfo(String pathInfo) throws ServletException {
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ServletException("Ruta inválida.");
        }
        try {
            return Integer.parseInt(pathInfo.substring(1)); // Extrae el ID de la URL
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido");
        }
    }
    // Método que lista todos los departamentos y los envía en formato JSON como respuesta
    private void listDepartamentos(HttpServletResponse response) throws IOException {
        List<Departamento> listaDepartamentos = departamentoServices.obtenerDepartamentos();
        JSONArray json = new JSONArray(listaDepartamentos);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();
    }
    // Método que obtiene un departamento por su ID y lo envía en formato JSON como respuesta
    private void getDepartamentoById(Integer id, HttpServletResponse response) throws IOException {
        Departamento departamento = departamentoServices.obtenerDepartamento(id);
        if (departamento != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(departamento));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Departamento no encontrado");
        }
    }
    // Método que inserta un nuevo departamento
    private void insertDepartamento(Departamento object,HttpServletRequest request, HttpServletResponse response) throws IOException {
//        StringBuilder buffer = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            buffer.append(line);
//        }
//        String data = buffer.toString();
//
//        JSONObject jsonObject = new JSONObject(data);
//        String nombreDepartamento = jsonObject.getString("nombreDepartamento");
//        String descripcionDepartamento = jsonObject.getString("descripcionDepartamento");
//
//        Departamento departamento = new Departamento();
//        departamento.setNombreDepartamento(nombreDepartamento);
//        departamento.setDescripcionDepartamento(descripcionDepartamento);

        departamentoServices.agregarDepartamento(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Departamento creado exitosamente\"}");
    }
    // Método que actualiza un departamento existente
    private void updateDepartamento(int id, Departamento object, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        departamentoServices.actualizarDepartamento(object);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Departamento actualizado exitosamente\"}");
    }
    // Método que elimina un departamento existente
    private void deleteDepartamento(int id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        departamentoServices.eliminarDepartamento(id);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Departamento eliminado exitosamente\"}");
    }
    // Método que configura los encabezados CORS para permitir solicitudes desde cualquier origen
    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
