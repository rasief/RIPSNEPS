package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de las tablas departamentos y municipios
 * @author Feisar Moreno
 * @date 15/02/2013
 */
public class DepMuni {
    private String codDep;
    private String nomDep;
    private String codMun;
    private String nomMun;
    private String codProv;
    private String codNDP;
    private String codDANE;
    
    public DepMuni() {
    }
    
    public DepMuni(ResultSet rs) {
        try {
            this.codDep = rs.getString("CODDEP");
        } catch (SQLException e) {
            this.codDep = "";
        }
        
        try {
            this.nomDep = rs.getString("NOMDEP");
        } catch (SQLException e) {
            this.nomDep = "";
        }
        
        try {
            this.codMun = rs.getString("CODMUN");
        } catch (SQLException e) {
            this.codMun = "";
        }
        
        try {
            this.nomMun = rs.getString("NOMMUN");
        } catch (SQLException e) {
            this.nomMun = "";
        }
        
        try {
            this.codProv = rs.getString("CODPROV");
        } catch (SQLException e) {
            this.codProv = "";
        }
        
        try {
            this.codNDP = rs.getString("CODNDP");
        } catch (SQLException e) {
            this.codNDP = "";
        }
        
        try {
            this.codDANE = rs.getString("CODDANE");
        } catch (SQLException e) {
            this.codDANE = "";
        }
    }
    
    public String getCodDep() {
        return codDep;
    }
    
    public String getNomDep() {
        return nomDep;
    }
    
    public String getCodMun() {
        return codMun;
    }
    
    public String getNomMun() {
        return nomMun;
    }
    
    public String getCodProv() {
        return codProv;
    }
    
    public String getCodNDP() {
        return codNDP;
    }
    
    public String getCodDANE() {
        return codDANE;
    }
}
