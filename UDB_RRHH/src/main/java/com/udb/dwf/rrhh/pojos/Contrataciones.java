package com.udb.dwf.rrhh.pojos;

public class Contrataciones {
    private int idContratacion;
    private int idDepartamento;
    private int idEmpleada;
    private int idCargo;
    private int idTipoContratacion;
    private int fechaContratacion;
    private double salario;
    private boolean estado;


    public Contrataciones(int idContratacion, int idEmpleada, int idCargo, int idTipoContratacion, int fechaContratacion, Double salario,boolean estado){
        this.idContratacion = idContratacion;
        this.idDepartamento = idDepartamento;
        this.idEmpleada = idEmpleada;
        this.idCargo = idCargo;
        this.idTipoContratacion = idTipoContratacion;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.estado = estado;

    }

    public int getIdContratacion() {
        return idContratacion;
    }

    public void setIdContratacion(int idContratacion) {
        this.idContratacion = idContratacion;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdEmpleada() {
        return idEmpleada;
    }

    public void setIdEmpleada(int idEmpleada) {
        this.idEmpleada = idEmpleada;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public int getIdTipoContratacion() {
        return idTipoContratacion;
    }

    public void setIdTipoContratacion(int idTipoContratacion) {
        this.idTipoContratacion = idTipoContratacion;
    }

    public int getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(int fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
