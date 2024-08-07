package com.udb.dwf.rrhh.pojos;

public class Cargos {
    private int idCargo;
    private String cargo;
    private String descripcionCargo;
    private boolean jefatura;

    public Cargos( int idCargo, String cargo , String descripcionCargo , boolean jefatura ) {
        this.idCargo = idCargo;
        this.cargo = cargo;
        this.descripcionCargo = descripcionCargo;
        this.jefatura = jefatura;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(int idCargo) {
        this.idCargo = idCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescripcionCargo() {
        return descripcionCargo;
    }

    public void setDescripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
    }

    public boolean isJefatura() {
        return jefatura;
    }

    public void setJefatura(boolean jefatura) {
        this.jefatura = jefatura;
    }
}
