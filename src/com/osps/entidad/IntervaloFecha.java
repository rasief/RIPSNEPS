package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de intervalo de fechas
 * @author Feisar Moreno
 * @date 02/12/2019
 */
public class IntervaloFecha {
    private String fechaIni;
    private String fechaFin;
    private String fechaIniT;
    private String fechaFinT;
    
    public IntervaloFecha() {
        
    }
    
    public IntervaloFecha(ResultSet rs) {
        try {
            fechaIni = rs.getString("FECHA_INI");
        } catch (SQLException e) {
            fechaIni = "";
        }
        try {
            fechaFin = rs.getString("FECHA_FIN");
        } catch (SQLException e) {
            fechaFin = "";
        }
        try {
            fechaIniT = rs.getString("FECHA_INI_T");
        } catch (SQLException e) {
            fechaIniT = "";
        }
        try {
            fechaFinT = rs.getString("FECHA_FIN_T");
        } catch (SQLException e) {
            fechaFinT = "";
        }
    }
    
    public String getFechaIni() {
        return fechaIni;
    }
    
    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }
    
    public String getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public String getFechaIniT() {
        return fechaIniT;
    }
    
    public void setFechaIniT(String fechaIniT) {
        this.fechaIniT = fechaIniT;
    }
    
    public String getFechaFinT() {
        return fechaFinT;
    }
    
    public void setFechaFinT(String fechaFinT) {
        this.fechaFinT = fechaFinT;
    }
    
}
