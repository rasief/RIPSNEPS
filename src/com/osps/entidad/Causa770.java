package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla causas_770
 * @author Feisar Moreno
 * @date 15/02/2013
 */
public class Causa770 {
    private String codCausa;
    private String nombreCausa;
    private String codigosCausa;
    private String codPadre;
    
    public Causa770() {
    }
    
    public Causa770(ResultSet rs) {
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
    
    public String getCodigosCausa() {
        return codigosCausa;
    }
    
    public String getCodPadre() {
        return codPadre;
    }
}
