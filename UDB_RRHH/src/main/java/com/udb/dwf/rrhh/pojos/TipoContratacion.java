package com.udb.dwf.rrhh.pojos;

public class TipoContratacion {
    private int idTipoContratacion;
    private String tipoContratacion;

    public TipoContratacion(int idTipoContratacion, String tipoContratacion) {
        this.idTipoContratacion = idTipoContratacion;
        this.tipoContratacion = tipoContratacion;
    }

    public int getIdTipoContratacion() {
        return idTipoContratacion;
    }

    public void setIdTipoContratacion(int idTipoContratacion) {
        this.idTipoContratacion = idTipoContratacion;
    }

    public String getTipoContratacion() {
        return tipoContratacion;
    }

    public void setTipoContratacion(String tipoContratacion) {
        this.tipoContratacion = tipoContratacion;
    }
}
