package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de las consultas de mortalidad
 * @author Feisar Moreno
 * @date 15/02/2013
 */
public class Mortalidad {
    private String tipoDefun;
    private String gruEd1;
    private String gruEd2;
    private String sexo;
    private String cod770;
    private String cod667;
    private String codDep;
    private String codMun;
    private long cantidadT;
    
    public Mortalidad() {
    }
    
    public Mortalidad(ResultSet rs) {
        try {
            this.tipoDefun = rs.getString("TIPO_DEFUN");
        } catch (SQLException e) {
            this.tipoDefun = "";
        }
        
        try {
            this.gruEd1 = rs.getString("GRU_ED1");
        } catch (SQLException e) {
            this.gruEd1 = "";
        }
        
        try {
            this.gruEd2 = rs.getString("GRU_ED2");
        } catch (SQLException e) {
            this.gruEd2 = "";
        }
        
        try {
            this.sexo = rs.getString("SEXO");
        } catch (SQLException e) {
            this.sexo = "";
        }
        
        try {
            this.cod667 = rs.getString("COD667");
        } catch (SQLException e) {
            this.cod667 = "";
        }
        
        try {
            this.cod770 = rs.getString("COD770");
        } catch (SQLException e) {
            this.cod770 = "";
        }
        
        try {
            this.codDep = rs.getString("CODDEP");
        } catch (SQLException e) {
            this.codDep = "";
        }
        
        try {
            this.codMun = rs.getString("CODMUN");
        } catch (SQLException e) {
            this.codMun = "";
        }
        
        try {
            this.cantidadT = rs.getLong("CANTIDADT");
        } catch (SQLException e) {
            this.cantidadT = 0L;
        }
    }
    
    public String getTipoDefun() {
        return tipoDefun;
    }
    
    public String getGruEd1() {
        return gruEd1;
    }
    
    public String getGruEd2() {
        return gruEd2;
    }
    
    public String getSexo() {
        return sexo;
    }
    
    public String getCod667() {
        return cod667;
    }
    
    public String getCod770() {
        return cod770;
    }
    
    public String getCodDep() {
        return codDep;
    }
    
    public String getCodMun() {
        return codMun;
    }
    
    public long getCantidadT() {
        return cantidadT;
    }
}
