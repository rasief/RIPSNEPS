package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla NDP
 * @author Feisar Moreno
 * @date 18/12/2012
 */
public class NDP {
    private String codNDP;
    private String nomNDP;
    
    public NDP() {
    }
    
    public NDP(String codNDP, String nomNDP) {
        this.codNDP = codNDP;
        this.nomNDP = nomNDP;
    }
    
    public NDP(ResultSet rs) {
        try {
            codNDP = rs.getString("CODNDP");
        } catch (SQLException e) {
            codNDP = "";
        }
        
        try {
            nomNDP = rs.getString("NOMNDP");
        } catch (SQLException e) {
            nomNDP = "";
        }
    }
    
    public String getCodNDP() {
        return this.codNDP;
    }
    
    public String getNomNDP() {
        return this.nomNDP;
    }
}
