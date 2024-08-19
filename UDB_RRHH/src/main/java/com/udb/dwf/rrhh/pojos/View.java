package com.udb.dwf.rrhh.pojos;

import java.sql.Date;

public class View {
    private int idEmpleado;
    private String numeroDui;
    private String nombrePersona;
    private String numeroTelefono;
    private String correoInstitucional;
    private String cargo;
    private String nombreDepartamento;
    private Date fechaContratacion;
    private double salario;
    private Date fechaNacimiento;
    private String tipoContratacion;


    public View (int idEmpleado, String numeroDui, String nombrePersona, String numeroTelefono, String correoInstitucional, String cargo,String nombreDepartamento, Date fechaContratacion, double salario, Date fechaNacimiento, String tipoContratacion) {
        this.idEmpleado = idEmpleado;
        this.numeroDui = numeroDui;
        this.nombrePersona = nombrePersona;
        this.numeroTelefono = numeroTelefono;
        this.correoInstitucional = correoInstitucional;
        this.cargo = cargo;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.fechaNacimiento = fechaNacimiento;
        this.nombreDepartamento = nombreDepartamento;
        this.tipoContratacion = tipoContratacion;
    }

    public String getTipoContratacion() {
        return tipoContratacion;
    }

    public void setTipoContratacion(String tipoContratacion) {
        this.tipoContratacion = tipoContratacion;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNumeroDui() {
        return numeroDui;
    }

    public void setNumeroDui(String numeroDui) {
        this.numeroDui = numeroDui;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
