package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.Contrataciones;
import com.udb.dwf.rrhh.services.ContratacionesServices;
import com.udb.dwf.rrhh.services.ContratacionesServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

//Servlet que maneja el CRUD de la Tabla Contrataciones
public class ContratacionesController extends HttpServlet {

    //Creador GSON para crear objetos desde un JSON
    Gson gson = new GsonBuilder().create();

    //Instancia de la Clase de Servicios de la Tabla Contrataciones
    private final ContratacionesServices services = new ContratacionesServices();

}
