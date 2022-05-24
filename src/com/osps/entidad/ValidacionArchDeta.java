package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Clase que representa un registro de la tabla validacion_arch_deta
 * @author Feisar Moreno
 * @date 09/03/2012
 */
public class ValidacionArchDeta {
    private long idValida;
    private String codArch;
    private long numRegistros;
    private long totalRegistros;
    private Date fechIni;
    private Date fechFin;
    
    public ValidacionArchDeta() {
    }
    
    public ValidacionArchDeta(ResultSet rs) {
        try {
            idValida = rs.getLong("IDVALIDA");
        } catch (SQLException e) {
            idValida = 0L;
        }
        
        try {
            codArch = rs.getString("CODARCH");
        } catch (SQLException e) {
            codArch = "";
        }
        
        try {
            numRegistros = rs.getLong("NUMREGISTROS");
        } catch (SQLException e) {
            numRegistros = 0L;
        }
        
        try {
            totalRegistros = rs.getLong("TOTALREGISTROS");
        } catch (SQLException e) {
            totalRegistros = 0L;
        }
        
        try {
            fechIni = rs.getDate("FECHINI");
        } catch (SQLException e) {
            fechIni = null;
        }
        
        try {
            fechFin = rs.getDate("FECHFIN");
        } catch (SQLException e) {
            fechFin = null;
        }
    }
    
    public long getIdValida() {
        return idValida;
    }
    
    public String getCodArch() {
        return codArch;
    }
    
    public long getNumRegistros() {
        return numRegistros;
    }
    
    public long getTotalRegistros() {
        return totalRegistros;
    }
    
    public Date getFechIni() {
        return fechIni;
    }
    
    public Date getFechFin() {
        return fechFin;
    }
}
