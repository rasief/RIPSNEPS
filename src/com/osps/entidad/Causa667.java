package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla causas_667
 * @author Feisar Moreno
 * @date 27/02/2013
 */
public class Causa667 {
    private String codCausa;
    private String nombreCausa;
    private String nombreCortoCausa;
    private String codigosCausa;
    private String codPadre;
    
    public Causa667() {
    }
    
    public Causa667(ResultSet rs) {
        try {
            this.codCausa = rs.getString("CODCAUSA");
        } catch (SQLException e) {
            this.codCausa = "";
        }
        
        try {
            this.nombreCausa = rs.getString("NOMBRECAUSA");
        } catch (SQLException e) {
            this.nombreCausa = "";
        }
        
        try {
            this.nombreCortoCausa = rs.getString("NOMBRECORTOCAUSA");
        } catch (SQLException e) {
            this.nombreCortoCausa = "";
        }
        
        try {
            this.codigosCausa = rs.getString("CODIGOSCAUSA");
        } catch (SQLException e) {
            this.codigosCausa = "";
        }
        
        try {
            this.codPadre = rs.getString("CODPADRE");
        } catch (SQLException e) {
            this.codPadre = "";
        }
    }
    
    public String getCodCausa() {
        return codCausa;
    }
    
    public String getNombreCausa() {
        return nombreCausa;
    }
    
    public String getNombreCortoCausa() {
        return nombreCortoCausa;
    }
    
    public String getCodigosCausa() {
        return codigosCausa;
    }
    
    public String getCodPadre() {
        return codPadre;
    }
}
