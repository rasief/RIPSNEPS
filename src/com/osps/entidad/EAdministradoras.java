package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla eadministradoras
 * @author Feisar Moreno
 * @date 21/02/2012
 */
public class EAdministradoras {
    private String codEntAdm;
    private String nombre;
    private long consecu;
    private String tipEnt;
    private String indConsolidado;
    
    public EAdministradoras() {
    }
    
    public EAdministradoras(ResultSet rs) {
        try {
            codEntAdm = rs.getString("CODENTADM");
        } catch (SQLException e) {
            codEntAdm = "";
        }
        
        try {
            nombre = rs.getString("NOMBRE");
        } catch (SQLException e) {
            nombre = "";
        }
        
        try {
            consecu = rs.getLong("CONSECU");
        } catch (SQLException e) {
            consecu = 0L;
        }
        
        try {
            tipEnt = rs.getString("TIPENT");
        } catch (SQLException e) {
            tipEnt = "";
        }
        
        try {
            indConsolidado = rs.getString("INDCONSOLIDADO");
        } catch (SQLException e) {
            indConsolidado = "";
        }
    }
    
    public String getCodEntAdm() {
        return codEntAdm;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public long getConsecu() {
        return consecu;
    }
    
    public String getTipEnt() {
        return tipEnt;
    }
    
    public String getIndConsolidado() {
        return indConsolidado;
    }
    
    @Override
    public String toString() {
        return this.getNombre();
    }
}
