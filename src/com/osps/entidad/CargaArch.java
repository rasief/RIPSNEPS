package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla carga_arch
 * @author Feisar Moreno
 * @date 12/09/2019
 */
public class CargaArch {
    private long idCarga;
    private String rutaCarga;
    private String codEntAdm;
    private int anio;
    private int mes;
    
    public CargaArch() {
    }
    
    public CargaArch(ResultSet rs) {
        try {
            idCarga = rs.getLong("ID_CARGA");
        } catch (SQLException e) {
            idCarga = 0L;
        }
        
        try {
            rutaCarga = rs.getString("RUTA_CARGA");
        } catch (SQLException e) {
            rutaCarga = "";
        }
        
        try {
            codEntAdm = rs.getString("COD_ENTO_ADM");
        } catch (SQLException e) {
            codEntAdm = "";
        }
        
        try {
            anio = rs.getInt("ANIO");
        } catch (SQLException e) {
            anio = 0;
        }
        
        try {
            mes = rs.getInt("MES");
        } catch (SQLException e) {
            mes = 0;
        }
    }
    
    public long getIdCarga() {
        return idCarga;
    }
    
    public String getRutaCarga() {
        return rutaCarga;
    }
    
    public String getCodEntAdm() {
        return codEntAdm;
    }
    
    public int getAnio() {
        return anio;
    }
    
    public int getMes() {
        return mes;
    }
    
}
