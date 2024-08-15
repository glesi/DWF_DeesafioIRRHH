package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.Cargo;
import com.udb.dwf.rrhh.services.CargosServices;
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

@WebServlet(name = "CargoController", urlPatterns = {"/cargo/*"})
public class CargoController extends HttpServlet {
    // Instancia del servicio para gestionar las operaciones relacionadas con 'Cargo'
    private final CargosServices cargoService = new CargosServices();

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
        String action = request.getParameter("accion");

        // Verifica el método HTTP y procesa la solicitud en consecuencia
        if ("POST".equals(method)) {
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada");
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
            throw new ServletException("Ruta inválida.");
        }
        try {
            return Integer.parseInt(pathInfo.substring(1));  // Extrae el ID de la URL
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido");
        }
    }

    private void processPostRequest(String action, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = null;

        if (!"insertar".equalsIgnoreCase(action)) {
            id = extractIdFromPathInfo(pathInfo);
        }

        switch (action) {
            case "insertar":
                insertCargo(request, response);
                break;
            case "actualizar":
                updateCargo(id, request, response);
                break;
            case "eliminar":
                if (id == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteCargo(id, request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción desconocida");
        }
    }

    private void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listCargo(response);
        } else {
            try {
                Integer id = extractIdFromPathInfo(pathInfo);
                getCargoById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    private void getCargoById(Integer id, HttpServletResponse response)
            throws IOException {
        Cargo cargo = cargoService.obtenerCargoPorId(id);
        if (cargo != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(cargo));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cargo no encontrado");
        }
    }

    private void listCargo(HttpServletResponse response)
            throws IOException {
        List<Cargo> cargoList = cargoService.obtenerCargos();
        JSONArray json = new JSONArray(cargoList);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(json);
        out.flush();
    }

    private void insertCargo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        JSONObject jsonObject = new JSONObject(data);
        String cargoStr = jsonObject.getString("cargo");
        String descripcionCargoStr = jsonObject.getString("descripcionCargo");
        boolean jefatura = jsonObject.getBoolean("jefatura");

        Cargo newCargo = new Cargo(0, cargoStr, descripcionCargoStr, jefatura);
        cargoService.crearCargo(newCargo);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Cargo creado exitosamente\"}");
    }

    private void updateCargo(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        JSONObject jsonObject = new JSONObject(data);
        String cargoStr = jsonObject.getString("cargo");
        String descripcionCargoStr = jsonObject.getString("descripcionCargo");
        boolean jefatura = jsonObject.getBoolean("jefatura");

        Cargo cargo = new Cargo(id, cargoStr, descripcionCargoStr, jefatura);
        cargoService.actualizarCargo(cargo);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Cargo actualizado exitosamente\"}");
    }

    private void deleteCargo(int id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        cargoService.eliminarCargo(id);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Cargo eliminado exitosamente\"}");
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
