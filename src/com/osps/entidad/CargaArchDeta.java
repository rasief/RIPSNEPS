package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Clase que representa un registro de la tabla carga_arch_deta
 * @author Feisar Moreno
 * @date 24/02/2012
 */
public class CargaArchDeta {
    private long idCargaDeta;
    private long idCarga;
    private String nomArch;
    private String codTabla;
    private int numRegistros;
    private int idEstado;
    private Date fechIni;
    private Date fechFin;
    private String nomEstado;
    
    public CargaArchDeta() {
    }
    
    public CargaArchDeta(ResultSet rs) {
        try {
            idCargaDeta = rs.getLong("ID_CARGA_DETA");
        } catch (SQLException e) {
            idCargaDeta = 0L;
        }
        
        try {
            idCarga = rs.getLong("ID_CARGA");
        } catch (SQLException e) {
            idCarga = 0L;
        }
        
        try {
            nomArch = rs.getString("NOM_ARCH");
        } catch (SQLException e) {
            nomArch = "";
        }
        
        try {
            codTabla = rs.getString("COD_TABLA");
        } catch (SQLException e) {
            codTabla = "";
        }
        
        try {
            numRegistros = rs.getInt("NUM_REGISTROS");
        } catch (SQLException e) {
            numRegistros = 0;
        }
        
        try {
            idEstado = rs.getInt("ID_ESTADO");
        } catch (SQLException e) {
            idEstado = 0;
        }
        
        try {
            fechIni = rs.getDate("FECH_INI");
        } catch (SQLException e) {
            fechIni = null;
        }
        
        try {
            fechFin = rs.getDate("FECH_FIN");
        } catch (SQLException e) {
            fechFin = null;
        }
        
        try {
            nomEstado = rs.getString("NOM_ESTADO");
        } catch (SQLException e) {
            nomEstado = "";
        }
    }
    
    public long getIdCargaDeta() {
        return idCargaDeta;
    }
    
    public long getIdCarga() {
        return idCarga;
    }
    
    public String getNomArch() {
        return nomArch;
    }
    
    public String getCodTabla() {
        return codTabla;
    }
    
    public int getNumRegistros() {
        return numRegistros;
    }
    
    public int getIdEstado() {
        return idEstado;
    }
    
    public Date getFechIni() {
        return fechIni;
    }
    
    public Date getFechFin() {
        return fechFin;
    }
    
    public String getNomEstado() {
        return nomEstado;
    }
}
