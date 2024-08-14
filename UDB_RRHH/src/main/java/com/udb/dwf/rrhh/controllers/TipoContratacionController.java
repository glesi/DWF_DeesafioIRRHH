package com.udb.dwf.rrhh.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.udb.dwf.rrhh.pojos.TipoContratacion;
import com.udb.dwf.rrhh.services.TipoContratacionesServices;
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

//Servlet que maneja el CRUD de la Tabla TipoContratacion
public class TipoContratacionController extends HttpServlet {

    //Creador GSON para crear objetos desde un JSON
    Gson gson = new GsonBuilder().create();

    //Instancia de la Clase de Servicios de la Tabla TipoContratacion
    private final TipoContratacionesServices services = new TipoContratacionesServices();

    //Función que manda la lista de los tipos de contratación
    private void listTipoContratacion(HttpServletResponse response)
            throws IOException {
        //De la instancia de la Clase de Servicios, obtenemos los tipos de contratación
        List<TipoContratacion> tipoContratacionList = services.obtenerTodosTipoContratacion();
        //Pasamos la lista de los tipos de contratación a un objeto JSONArray
        JSONArray json = new JSONArray(tipoContratacionList);
        //Obtenemos de la respuesta el writer que nos ayudará a editar la respuesta
        PrintWriter out = response.getWriter();
        //Configuramos la respuesta que el contenido sea "APPLICATION/JSON" y que la codificación sea UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //Y finalizamos imprimiendo en la respuesta el JSON con la lista de los tipos de contratación
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

    //Función que obtiene el tipo de contratación seleccionado por ID
    private void getTipoContratacionById(Integer id, HttpServletResponse response)
            throws IOException {
        /*
            Se llama al servicio para obtener con el parámetro del ID al tipo de contratación
            que se necesita
         */
        TipoContratacion tipoContratacion = services.obtenerTipoContratacionPorId(id);
        /*
            Verifica si el tipo de contratación es distinto de nulo,
            si es distinto de nulo, se obtiene el writer de la respuesta para enviar
            el tipo de contratación, si no,
            envía un error con el mensaje que no se encontró el tipo de contratación deseado
         */
        if (tipoContratacion != null) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.println(new JSONObject(tipoContratacion));
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tipo de contratación no encontrado");
        }
    }

    //Función que añade un tipo de contratación nuevo
    private void insertTipoContratacion(TipoContratacion object, HttpServletResponse response)
            throws IOException {
        //Se llama al servicio con la función de crearTipoContratación enviando el objeto como parámetro
        services.crearTipoContratacion(object);
        //Se crea el json del mensaje a enviar que se ha creado exitosamente
        JSONObject json = new JSONObject();
        json.put("message", "Tipo de contratación creado exitosamente");
        response.getWriter().println(json);
    }

    //Función que actualiza un tipo de contratación agregado anteriormente
    private void updateTipoContratacion(TipoContratacion object, HttpServletResponse response)
            throws IOException {
        //Se llama al servicio con la función de actualizarTipoContratación enviando el objeto como parámetro
        services.actualizarTipoContratacion(object);
        //Se crea el json del mensaje a enviar que se ha actualizado exitosamente
        JSONObject json = new JSONObject();
        json.put("message", "Tipo de contratación actualizado exitosamente");
        response.getWriter().println(json);
    }

    //Función que elimina un tipo de contratación
    private void deleteTipoContratacion(int id, HttpServletResponse response)
            throws IOException {
        //Se llama al servicio con la función de eliminarTipoContratacion enviando el ID como parámetro
        services.eliminarTipoContratacion(id);
        //Se crea el json del mensaje a enviar que se ha eliminado exitosamente
        JSONObject json = new JSONObject();
        json.put("message", "Tipo de contratación eliminado exitosamente");
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
            //Se obtiene el JSON a utilizar y luego lo transforma al objeto TipoContratacion
            String jsonString = bodyJSON.getJSONObject("json").toString();
            TipoContratacion object = gson.fromJson(jsonString, TipoContratacion.class);
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
            Si no existe algún parámetro llama a la función para obtener todos los tipos de contratación,
            sino, se llama la función para obtener el tipo de contratación por ID
         */
        if (pathInfo == null || pathInfo.equals("/")) {
            listTipoContratacion(response);
        } else {
            /*
                Se realiza un try para verificara que lo que trae el parámetro sea un número,
                si no trae un número, dará error NumberFormatException
             */
            try {
                //Se llama la función extractIdFromPathInfo para obtener el ID
                Integer id = extractIdFromPathInfo(pathInfo);
                //Se llama la función para obtener el tipo de contratación
                getTipoContratacionById(id, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }


    //Función que procesa una petición POST
    private void processPostRequest(String action, TipoContratacion object, HttpServletResponse response)
            throws IOException {

        //Se obtiene el ID del tipo de contratación
        int id = object.getIdTipoContratacion();
        /*
            Se realiza un switch con la acción que se realizará,
            si la acción es "insertar", se llamará la función para añadir el tipo de contratación,
            si la acción es "actualizar", se llamará la función para actualizar el tipo de contratación,
            si la acción es "eliminar", se llamará la función para eliminar el tipo de contratación,
            y si no obtiene alguno de los datos anteriores, tirará error de que es una acción desconocida
         */
        switch (action) {
            case "insertar" -> insertTipoContratacion(object, response);
            case "actualizar" -> {
                /*
                    Verifica que el ID sea cero,
                    si es cero, tira un error de que no se ha proporcionado un ID
                 */
                if (id == 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado para actualizar");
                    return;
                }
                updateTipoContratacion(object, response);
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
                deleteTipoContratacion(id, response);
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