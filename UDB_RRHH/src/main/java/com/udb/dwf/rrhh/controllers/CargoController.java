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

//Servlet que maneja el CRUD de la Tabla TipoContratacion
@WebServlet(name = "CargoController", urlPatterns = "/cargos/*")
public class CargoController extends HttpServlet {

    private final CargosServices cargosServices = new CargosServices();

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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setAccessControlHeaders(response);

        String method = request.getMethod();
        String pathInfo = request.getPathInfo();

        if ("/".equals(pathInfo)) pathInfo = null;

        try {
            switch (method) {
                case "GET":
                    if (pathInfo == null) {
                        listCargos(response);
                    } else {
                        int idCargo = extractIdFromPathInfo(pathInfo);
                        getCargoById(idCargo, response);
                    }
                    break;
                case "POST":
                    if (pathInfo == null) {
                        createCargo(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operación POST no soportada en la URL especificada");
                    }
                    break;
                case "PUT":
                    if (pathInfo != null) {
                        int idCargo = extractIdFromPathInfo(pathInfo);
                        updateCargo(idCargo, request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL debe incluir ID del cargo a actualizar");
                    }
                    break;
                case "DELETE":
                    if (pathInfo != null) {
                        int idCargo = extractIdFromPathInfo(pathInfo);
                        deleteCargo(idCargo, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL debe incluir ID del cargo a eliminar");
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Método no implementado");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        }
    }

    private int extractIdFromPathInfo(String pathInfo) {
        return Integer.parseInt(pathInfo.substring(1)); // Extrae el ID de la URL
    }

    private void listCargos(HttpServletResponse response) throws IOException {
        List<Cargo> cargos = cargosServices.obtenerCargos();
        JSONArray jsonArray = new JSONArray(cargos);
        sendJsonArrayResponse(response, jsonArray);
    }

    private void getCargoById(int idCargo, HttpServletResponse response) throws IOException {
        Cargo cargo = cargosServices.obtenerCargoPorId(idCargo);
        if (cargo != null) {
            sendJsonResponse(response, new JSONObject(cargo));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cargo no encontrado");
        }
    }

    private void createCargo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = parseJsonRequest(request);
        Cargo cargo = new Cargo(
                0,
                jsonObject.getString("cargo"),
                jsonObject.getString("descripcionCargo"),
                jsonObject.getBoolean("jefatura")
        );
        Cargo createdCargo = cargosServices.crearCargo(cargo);
        if (createdCargo != null) {
            sendJsonResponse(response, new JSONObject(createdCargo));
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo crear el cargo");
        }
    }

    private void updateCargo(int idCargo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = parseJsonRequest(request);
        Cargo cargo = new Cargo(
                idCargo,
                jsonObject.getString("cargo"),
                jsonObject.getString("descripcionCargo"),
                jsonObject.getBoolean("jefatura")
        );
        boolean updated = cargosServices.actualizarCargo(cargo);
        if (updated) {
            sendJsonResponse(response, new JSONObject(cargo));
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo actualizar el cargo");
        }
    }

    private void deleteCargo(int idCargo, HttpServletResponse response) throws IOException {
        boolean deleted = cargosServices.eliminarCargo(idCargo);
        if (deleted) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo eliminar el cargo");
        }
    }

    private JSONObject parseJsonRequest(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return new JSONObject(buffer.toString());
    }

    private void sendJsonResponse(HttpServletResponse response, JSONObject jsonObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
    }

    private void sendJsonArrayResponse(HttpServletResponse response, JSONArray jsonArray) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(jsonArray.toString());
        out.flush();
    }

    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
