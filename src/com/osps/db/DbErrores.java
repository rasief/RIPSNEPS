package com.osps.db;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Clase para el manejo de la tabla errores
 * @author Feisar Moreno
 * @date 24/02/2012
 */
public class DbErrores extends DbConexion {
    
    /**
     * Método que inserta un registro de error
     * @author Feisar Moreno
     * @date 24/02/2012
     * @param codErr Código del error
     * @param idCarga Identificador del registro de carga
     * @param idCargaDeta Identificador del registro de detalle de carga
     * @param numRegistro Número del registro dentro del archivo
     * @param codTabla Código de la tabla
     * @param idRegDuplicado Identificador del registro duplicado
     * @param observaciones Observaciones anexas al error
     * @return 1 si se actualizó el registro, de lo contrario 0.
     */
    public long insertarRegistro(String codErr, long idCarga, long idCargaDeta, int numRegistro, String codTabla, long idRegDuplicado, String observaciones) {
        
        try {
            crearConexion();
            String procAlmacenado = "{call pa_crear_error(?,?,?,?,?,?,?,?)}";
            long resultado;
            try (CallableStatement cstmt = conn.prepareCall(procAlmacenado)) {
                cstmt.setString(1, codErr);
                cstmt.setLong(2, idCarga);
                cstmt.setLong(3, idCargaDeta);
                cstmt.setLong(4, numRegistro);
                cstmt.setString(5, codTabla);
                if (idRegDuplicado > 0) {
                    cstmt.setLong(6, idRegDuplicado);
                } else {
                    cstmt.setNull(6, Types.BIGINT);
                }   if (observaciones != null && !observaciones.equals("")) {
                    cstmt.setString(7, observaciones);
                } else {
                    cstmt.setNull(7, Types.VARCHAR);
                }
                cstmt.registerOutParameter(8, Types.BIGINT);
                cstmt.execute();
                resultado = cstmt.getLong(8);
            }
            
            return resultado;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return -1;
        } finally {
            cerrarConexion();
        }
    }
}
