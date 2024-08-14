package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.View;
import com.udb.dwf.rrhh.services.ViewServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ViewController extends HttpServlet {

    Gson gson = new GsonBuilder().create();

    private final ViewServices viewService = new ViewServices ();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException,IOException {
        resp.setHeader("Access-Control-Allow-Origin","*");
        resp.setHeader("Access-Control-Allow-Methods","GET");
        resp.setHeader("Access-Control-Max-Headers","Content-Type,Authorization");

        String method = req.getMethod();
        String requestData = req.getReader().lines().collect(Collectors.joining());
        JSONObject json = new JSONObject(requestData);
        String action = json.getString("action");
        String jsonString = json.getString("action").toLowerCase();

        View obj = gson.fromJson(jsonString, View.class);

        if("GET".equals(method)){
            processGetRequest(req,resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Método no implementado");
        }
    }

    private void processGetRequest(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null || pathInfo.equals("/")){
            listViewData(resp);
        } else {
            try {
                Integer id = Integer.parseInt(pathInfo);
                getEmpleadoById(id, resp);
            } catch (NumberFormatException e){
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,"Id invalido");
            }
        }
    }
    private void getEmpleadoById(Integer idEmpleado, HttpServletResponse resp)
        throws ServletException, IOException {
        View view = viewService.getViewById(idEmpleado);

        if(view != null){
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(view));
            out.flush();
        }else{
            resp.sendError(HttpServletResponse.SC_NOT_FOUND,"Id not found");
        }
    }

    private void listViewData(HttpServletResponse resp)
            throws IOException {
        List<View> viewList = viewService.getViews();
        JSONArray jsonArray = new JSONArray(viewList);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.println(jsonArray);
        out.flush();
    }
}
