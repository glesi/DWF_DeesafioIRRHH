package com.udb.dwf.rrhh.controllers;

import com.udb.dwf.rrhh.pojos.View;
import com.udb.dwf.rrhh.services.ViewServices;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name= "ViewController",urlPatterns="/viewController")
public class ViewController extends HttpServlet {

    private final ViewServices viewService = new ViewServices ();
    protected void listViewController( HttpServletResponse response)
            throws IOException {
        List<View> viewList = viewService.getViews();
    }
}
