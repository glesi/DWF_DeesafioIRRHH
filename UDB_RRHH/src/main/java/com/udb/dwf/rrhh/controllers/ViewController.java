package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.TipoContratacion;
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
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "ViewController", urlPatterns = {"/vista/*"})
//Servlet que maneja el CRUD de la Tabla View
public class ViewController extends HttpServlet {

    //Instancia de la Clase de Servicios de la Tabla View
    private final ViewServices services = new ViewServices();

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    //Función que manda la vista de los objetos
    protected void listView(HttpServletResponse response) {
        //Se obtiene del servicio la lista
        List<View> viewList = services.getViews();
        /*
            Se realiza un try, si llega a haber el error IOException, tirará un error
         */
        try {
            //Obtenemos de la respuesta el writer que nos ayudará a editar la respuesta
            PrintWriter out = response.getWriter();
            //Configuramos la respuesta que el contenido sea "APPLICATION/JSON" y que la codificación sea UTF-8
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            //Y finalizamos imprimiendo en la respuesta el JSON con la vista
            JSONArray jsonArray = new JSONArray(viewList);
            out.println(jsonArray);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Función que recibe las peticiones GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            processRequest(req, resp);
    }

    //Función que procesa las peticiones en general
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //Se inserta los headers necesarios para evitar error CORS
        setAccessControlHeaders(response);
        //Llama la función para proveer la vista de los objetos
        String method = request.getMethod();
        //verificar el metodo para la peticion
        if ("POST".equals(method)) {
            //Obtener datos del body de la request
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //Transformar el body a JSON
            JSONObject bodyJSON = new JSONObject(requestData);
            //separar el request en accion y datos de la peticion
            //primero, obtener la accion a realizar
            String action = bodyJSON.getString("accion");
            //Luego, obtener el JSON con la informacion del request
            String jsonString = bodyJSON.getJSONObject("json").toString();
            //Crear el objeto donde se guardara la informacion del json delk request
            View object = gson.fromJson(jsonString, View.class);
            //manejo de error en caso de campo "accion" nulo
            if(action==null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Accion no especificada");
                return;
            }
            proccessPostRequest(action, object, request, response);
        } else if (method.equals("GET")) {
            processGetRequest(request,response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"Metodo no soportado");
        }
    }

    private void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            listView(response);
        }else{
            try{
                Integer id = extractIdFromPathInfo(pathInfo);
                getViewById(id, response);
            }catch (NumberFormatException e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Id invalido"+e.getMessage());
            }
        }
    }
    //Metodo para procesar solicitudes POST
    private void proccessPostRequest(String action, View object, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        Integer id = null;
        if(!"insertar".equalsIgnoreCase(action)){
            id = extractIdFromPathInfo(pathInfo);
            switch (action) {
                case "eliminar":
                    if (id == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                        return;
                    }

                    // Llama al método para eliminar la contratación con el ID proporcionado
                    deleteBoth(id, request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Accion desconocida");
            }
        }
    }
    private void deleteBoth(Integer id, HttpServletRequest request, HttpServletResponse response)
            throws IOException{

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length()>1) {
            services.eliminarView(id);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Registro eliminada exitosamente\"}");
        } else{
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"ID no proporcionado");
        }

    }
    private void getViewById(Integer id, HttpServletResponse response)
            throws IOException {
        View view = services.getViewById(id);
        System.out.println("imprimiendo "+view);
        if (view != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(view));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"Empleado not found in view");
        }
    }
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

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

    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
