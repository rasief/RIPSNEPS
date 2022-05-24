package com.osps.db;

import com.osps.entidad.ValidacionArchDeta;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de las tablas validacion_arch y validacion_arch_deta
 * @author Feisar Moreno
 * @date 09/03/2012
 */
public class DbValidacionArch extends DbConexion {
    
    /**
     * Método que inserta un registro en la tabla validacion_arch,
     * si el registro ya existe retorna el id
     * @author Feisar Moreno
     * @date 09/03/2012
     * @param codEntAdm Código de la entidad administradora
     * @param trimAnio Período a cargar
     * @return Identificador del archivo de carga
     */
    public long insertarRegistro(String codEntAdm, String trimAnio) {
        try {
            crearConexion();
            String procAlmacenado = "{? = call fu_insertar_validacion_arch(?,?)}";
            CallableStatement cstmt = conn.prepareCall(procAlmacenado);
            cstmt.registerOutParameter(1, Types.BIGINT);
            cstmt.setString(2, codEntAdm);
            cstmt.setString(3, trimAnio);
            cstmt.execute();
            long resultado = cstmt.getLong(1);
            cstmt.close();
            
            return resultado;
            
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 09/03/2012
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<ValidacionArchDeta> getListaValidacionArchDeta(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<ValidacionArchDeta> listaValidacionArchDeta = new ArrayList<ValidacionArchDeta>();
            while (rst.next()) {
                ValidacionArchDeta validaAux = new ValidacionArchDeta(rst);
                listaValidacionArchDeta.add(validaAux);
            }
            rst.close();
            
            return listaValidacionArchDeta;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de detalle de una validación dada.
     * @author Feisar Moreno
     * @date 09/03/2012
     * @param idValida Identificador de la validación
     * @return <code>ArrayList</code> con la información del detalle de una validación.
     */
    public ArrayList<ValidacionArchDeta> getListaValidacionArchDeta(long idValida) {
        try {
            String sql =
                "SELECT * " +
                "FROM validacion_arch_deta " +
                "WHERE idvalida=" + idValida + " " +
                "ORDER BY codarch";
            
            return this.getListaValidacionArchDeta(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<ValidacionArchDeta>();
        }
    }
}
