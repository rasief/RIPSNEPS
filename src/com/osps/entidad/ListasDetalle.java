package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de tabla listas_detalle
 * @author Feisar Moreno
 * @date 12/12/2012
 */
public class ListasDetalle {
    private int idLista;
    private String codDetalle;
    private String nomDetalle;
    private int orden;
    
    public ListasDetalle() {
    }
    
    public ListasDetalle(ResultSet rs) {
        try {
            this.idLista = rs.getInt("IDLISTA");
        } catch (SQLException e) {
            this.idLista = 0;
        }
        
        try {
            this.codDetalle = rs.getString("CODDETALLE");
        } catch (SQLException e) {
            this.codDetalle = "";
        }
        
        try {
            this.nomDetalle = rs.getString("NOMDETALLE");
        } catch (SQLException e) {
            this.nomDetalle = "";
        }
        
        try {
            this.orden = rs.getInt("ORDEN");
        } catch (SQLException e) {
            this.orden = 0;
        }
    }
    
    public int getIdLista() {
        return idLista;
    }
    
    public String getCodDetalle() {
        return codDetalle;
    }
    
    public String getNomDetalle() {
        return nomDetalle;
    }
    
    public int getOrden() {
        return orden;
    }
}
