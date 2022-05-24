package com.osps.db;

import java.sql.CallableStatement;
import java.sql.Types;

/**
 * Clase para el manejo de las tablas de historial
 * @author Feisar Moreno
 * @date 17/07/2012
 */
public class DbHistorial extends DbConexion {
        
    /**
     * Método que realiza la consolidación de una entidad
     * @author Feisar Moreno
     * @date 24/02/2012
     * @param codEntAdm Código de la entidad administradora
     * @return 1 si se realizó la consolidación, de lo contrario -1
     */
    public int consolidarEntidad(String codEntAdm) {
        try {
            crearConexion();
            String procAlmacenado = "{? = call fu_consolidar_eadministradora(?,?)}";
            CallableStatement cstmt = conn.prepareCall(procAlmacenado);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, codEntAdm);
            cstmt.setString(3, "1000");
            cstmt.execute();
            int resultado = cstmt.getInt(1);
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
     * Método que realiza la consolidación de una entidad
     * @author Feisar Moreno
     * @date 20/11/2012
     * @param codEntAdm Código de la entidad administradora
     * @param periodo Periodo de carga
     * @return 1 si se realizó la consolidación, de lo contrario -1
     */
    public int consolidarEntidad(String codEntAdm, String periodo) {
        try {
            crearConexion();
            String procAlmacenado = "{? = call fu_consolidar_eadministradora(?,?)}";
            CallableStatement cstmt = conn.prepareCall(procAlmacenado);
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, codEntAdm);
            cstmt.setString(3, periodo);
            cstmt.execute();
            int resultado = cstmt.getInt(1);
            cstmt.close();
            
            return resultado;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        } finally {
            cerrarConexion();
        }
    }
}
