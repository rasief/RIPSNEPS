package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de consolidado de grupos de atención
 * @author Feisar Moreno
 * @date 02/12/2019
 */
public class Grupo {
    private int idGrupo;
    private String grupo;
    private int cantidad;
    
    public Grupo(ResultSet rs) {
        try {
            idGrupo = rs.getInt("ID_GRUPO");
        } catch (SQLException e) {
            idGrupo = 0;
        }
        try {
            grupo = rs.getString("GRUPO");
        } catch (SQLException e) {
            grupo = "";
        }
        try {
            cantidad = rs.getInt("CANTIDAD");
        } catch (SQLException e) {
            cantidad = 0;
        }
    }
    
    public int getIdGrupo() {
        return idGrupo;
    }
    
    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }
    
    public String getGrupo() {
        return grupo;
    }
    
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}
