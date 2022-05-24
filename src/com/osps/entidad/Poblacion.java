package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la poblacion
 * @author Feisar Moreno
 * @date 21/02/2013
 */
public class Poblacion {
    private String codDep;
    private String codMun;
    private String codMunDane;
    private int ano;
    private String gEdad;
    private String sexo;
    private long habitantes;
    
    public Poblacion() {
    }
    
    public Poblacion(ResultSet rs) {
        try {
            this.codDep = rs.getString("COD_DEP");
        } catch (SQLException e) {
            this.codDep = "";
        }
        
        try {
            this.codMun = rs.getString("COD_MUN");
        } catch (SQLException e) {
            this.codMun = "";
        }
        
        try {
            this.codMunDane = rs.getString("COD_MUN_DANE");
        } catch (SQLException e) {
            this.codMunDane = "";
        }
        
        try {
            this.ano = rs.getInt("ANO");
        } catch (SQLException e) {
            this.ano = 0;
        }
        
        try {
            this.gEdad = rs.getString("GEDAD");
        } catch (SQLException e) {
            this.gEdad = "";
        }
        
        try {
            this.sexo = rs.getString("SEXO");
        } catch (SQLException e) {
            this.sexo = "";
        }
        
        try {
            this.habitantes = rs.getLong("HABITANTES");
        } catch (SQLException e) {
            this.habitantes = 0L;
        }
    }
    
    public String getCodDep() {
        return codDep;
    }
    
    public String getCodMun() {
        return codMun;
    }
    
    public String getCodMunDane() {
        return codMunDane;
    }
    
    public int getAno() {
        return ano;
    }
    
    public String getGEdad() {
        return gEdad;
    }
    
    public String getSexo() {
        return sexo;
    }
    
    public long getHabitantes() {
        return habitantes;
    }
}
