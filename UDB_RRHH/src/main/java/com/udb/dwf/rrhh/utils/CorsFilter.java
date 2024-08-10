package com.udb.dwf.rrhh.utils;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.logging.Logger;

//Clase que realiza el filtro CORS
/*
    @Provider = Anotación que define que la clase es un Proveedor
    @PreMatching =
 */

@Provider
@PreMatching
public class CorsFilter implements ContainerResponseFilter {

    //Función que recibe las peticiones antes de entregarlas al Controller
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        String origin = containerRequestContext.getHeaderString("Origin");
        if ((origin != null)
                && (origin.startsWith("http://localhost:4200") || origin.startsWith("http://localhost:8080"))) {
            allowExceptionCors(containerRequestContext, containerResponseContext, origin);
        }
    }

    //Función que añade atributos a la petición para ser aceptada por el CORS
    private void allowExceptionCors(ContainerRequestContext requestContext, ContainerResponseContext responseContext, String origin) {
        String methodHeader = requestContext.getHeaderString("Access-Control-Request-Method");
        String requestHeaders = requestContext.getHeaderString("Access-Control-Request-Headers");
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.putSingle("Access-Control-Allow-Origin", origin);
        headers.putSingle("Access-Control-Allow-Credentials", "true");
        headers.putSingle("Access-Control-Allow-Methods", methodHeader);
        headers.putSingle("Access-Control-Allow-Headers", "x-requested-with," + (requestHeaders == null ? "" : requestHeaders));
    }
}
