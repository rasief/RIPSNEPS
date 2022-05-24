package com.osps.db;

import com.osps.entidad.NDP;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla NDP
 * @author Feisar Moreno
 * @date 18/12/2012
 */
public class DbNDP extends DbConexion {
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 18/12/2012
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<NDP> getListaNDP(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<NDP> listaNDP = new ArrayList<NDP>();
            while (rst.next()) {
                NDP ndpAux = new NDP(rst);
                listaNDP.add(ndpAux);
            }
            rst.close();
            
            return listaNDP;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los núcleos de desarrollo provincial.
     * @author Feisar Moreno
     * @date 18/12/2012
     * @return <code>ArrayList</code> con los núcleos de desarrollo provincial.
     */
    public ArrayList<NDP> getListaNDP() {
        try {
            String sql =
                "SELECT * FROM ndp ORDER BY nomndp";
            
            return getListaNDP(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<NDP>();
        }
    }
}
