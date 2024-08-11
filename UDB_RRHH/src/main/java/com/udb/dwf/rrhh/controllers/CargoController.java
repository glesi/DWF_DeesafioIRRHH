package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.Cargo;
import com.udb.dwf.rrhh.services.CargosServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CargoController extends HttpServlet {
    private final CargosServices cargosServices = new CargosServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listCargos(response);
        } else {
            try {
                int idCargo = Integer.parseInt(pathInfo.substring(1));
                getCargoById(idCargo, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            createCargo(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Operación POST no soportada en la URL especificada");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int idCargo = Integer.parseInt(pathInfo.substring(1));
                updateCargo(idCargo, request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL debe incluir ID del cargo a actualizar");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                int idCargo = Integer.parseInt(pathInfo.substring(1));
                deleteCargo(idCargo, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "URL debe incluir ID del cargo a eliminar");
        }
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
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        JSONObject jsonObject = new JSONObject(buffer.toString());
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
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        JSONObject jsonObject = new JSONObject(buffer.toString());
        Cargo cargo = new Cargo(
                idCargo,
                jsonObject.getString("cargo"),
                jsonObject.getString("descripcionCargo"),
                jsonObject.getBoolean("jefatura")
        );
        boolean updated = cargosServices.actualizarCargo(cargo);
        if (updated) {
            response.setStatus(HttpServletResponse.SC_OK);
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
        jsonArray.forEach(json -> {
            out.print(json.toString());
            out.flush();
        });
    }
}
