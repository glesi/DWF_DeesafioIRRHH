package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.Contrataciones;
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

    //Función que manda la lista de las contrataciones
    private void listContrataciones(HttpServletResponse response)
            throws IOException {
        //De la instancia de la Clase de Servicios, obtenemos las contrataciones
        List<Contrataciones> contrataciones = services.obtenerContrataciones();
        //Pasamos la lista de las contrataciones a un objeto JSONArray
        JSONArray json = new JSONArray(contrataciones);
        //Obtenemos de la respuesta el writer que nos ayudará a editar la respuesta
        PrintWriter out = response.getWriter();
        //Configuramos la respuesta que el contenido sea "APPLICATION/JSON" y que la codificación sea UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //Y finalizamos imprimiendo en la respuesta el JSON con la lista de las contrataciones
        out.println(json);
        out.flush();
    }


    //Función que extrae el ID del URL
    private Integer extractIdFromPathInfo(String pathInfo) throws ServletException {
        /*
            Se identifica si el URL no contiene algún ID,
            si no lo tiene, retorna nulo
         */
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ServletException("Ruta invalida.");
        }
        /*
            Realiza el cambio de string a Integer,
            si no lo tiene, entonces da un NumberFormatException.
         */
        try {
            return Integer.parseInt(pathInfo.substring(1));  // Extrae el ID de la URL
        } catch (NumberFormatException e) {
            throw new ServletException("ID inválido");
        }
    }

    //Función que obtiene la contratación seleccionado por ID
    private void getContratacionById(Integer id, HttpServletResponse response)
            throws IOException {
        /*
            Se llama al servicio para obtener con el parámetro del ID al Contratación
            que se necesita
         */
        Contrataciones contratacion = services.obtenerContratacionPorId(id);
        /*
            Verifica si la contratación es distinto de nulo,
            si es distinto de nulo, se obtiene el writer de la respuesta para enviar
            la contratación, si no,
            envía un error con el mensaje que no se encontró la contratación deseada
         */
        if (contratacion != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(contratacion));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Contratación no encontrado");
        }
    }

    //Función que añade una contratación nueva
    private void insertContratacion(Contrataciones object, HttpServletResponse response)
            throws IOException {
        //Se llama al servicio con la función de crearContratacion enviando el objeto como parámetro
        services.crearContratacion(object);
        //Se crea el json del mensaje a enviar que se ha creado exitosamente
        JSONObject json = new JSONObject();
        json.put("message", "Contratación creada exitosamente");
        response.getWriter().println(json);
    }

    //Función que actualiza una contratación agregado anteriormente
    private void updateContratacion(Contrataciones object, HttpServletResponse response)
            throws IOException {
        //Se llama al servicio con la función de actualizarContratacion enviando el objeto como parámetro
        services.actualizarContratacion(object);
        //Se crea el json del mensaje a enviar que se ha actualizado exitosamente
        JSONObject json = new JSONObject();
        json.put("message", "Contratación actualizada exitosamente");
        response.getWriter().println(json);
    }

    //Función que elimina una contratación
    private void deleteContratacion(int id, HttpServletResponse response)
            throws IOException {
        //Se llama al servicio con la función de eliminarContratacion enviando el ID como parámetro
        services.eliminarContratacion(id);
        //Se crea el json del mensaje a enviar que se ha eliminado exitosamente
        JSONObject json = new JSONObject();
        json.put("message", "Contratación eliminada exitosamente");
        response.getWriter().println(json);
    }

    //Función que procesa las peticiones en general
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Se inserta los headers necesarios para evitar error CORS
        setAccessControlHeaders(response);
        //Se obtiene el método que se ocupará
        String method = request.getMethod();
        /*
            Verifica que método se ocupará, si es POST realizará el procedimiento para la petición POST,
            si es GET, realizara el procedimiento para la petición GET,
            si no, tirará error que no existe algún método implementado
         */
        if ("POST".equals(method)) {
            //Se obtiene los datos del Body
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //Transforma el Body a JSON
            JSONObject bodyJSON = new JSONObject(requestData);
            //Se obtiene la acción que se ejecutará
            String action = bodyJSON.getString("action");
            //Se obtiene el JSON a utilizar y luego lo transforma al objeto Contratacion
            String jsonString = bodyJSON.getJSONObject("json").toString();
            Contrataciones object = gson.fromJson(jsonString, Contrataciones.class);
            /*
                Si la acción es nula, tirará el error de que no sé específico la acción
             */
            if (action == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada");
                return;
            }
            //Se llama la función que realizará el procedimiento de la petición POST
            processPostRequest(action, object, response);
        } else if ("GET".equals(method)) {
            //Se llama la función que realizará el procedimiento de la petición GET
            processGetRequest(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Método no implementado");
        }
    }


    //Función que procesa una petición GET
    private void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Obtiene los parámetros de la URL
        String pathInfo = request.getPathInfo();
        /*
            Si no existe algún parámetro llama a la función para obtener todas las contrataciones,
            sino, se llama la función para obtener la contratación por ID
         */
        if (pathInfo == null || pathInfo.equals("/")) {
            listContrataciones(response);
        } else {
            /*
                Se realiza un try para verificara que lo que trae el parámetro sea un número,
                si no trae un número, dará error NumberFormatException
             */
            try {
                //Se llama la función extractIdFromPathInfo para obtener el ID
                Integer id = extractIdFromPathInfo(pathInfo);
                //Se llama la función para obtener la contratación
                getContratacionById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }


    //Función que procesa una petición POST
    private void processPostRequest(String action, Contrataciones object, HttpServletResponse response)
            throws IOException {

        //Se obtiene el ID de la contratación
        int id = object.getIdContratacion();
        /*
            Se realiza un switch con la acción que se realizará,
            si la acción es "insertar", se llamará la función para añadir la contratación,
            si la acción es "actualizar", se llamará la función para actualizar la contratación,
            si la acción es "eliminar", se llamará la función para eliminar la contratación,
            y si no obtiene alguno de los datos anteriores, tirará error de que es una acción desconocida
         */
        switch (action) {
            case "insertar" -> insertContratacion(object, response);
            case "actualizar" -> {
                /*
                    Verifica que el ID sea cero,
                    si es cero, tira un error de que no se ha proporcionado un ID
                 */
                if (id == 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }
                updateContratacion(object, response);
            }
            case "eliminar" -> {
                /*
                    Verifica que el ID sea cero,
                    si es cero, tira un error de que no se ha proporcionado un ID
                 */
                if (id == 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para eliminar");
                    return;
                }
                deleteContratacion(id, response);
            }
            default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción desconocida");
        }
    }

    //Función que inserta los headers para evitar error de CORS
    private void setAccessControlHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
    }


    //Función que recibe las peticiones GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    //Función que recibe las peticiones POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    //Función que recibe las peticiones OPTIONS
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
