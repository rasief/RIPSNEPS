package com.osps.db;

import com.osps.entidad.Causa770;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla causas_770
 * @author Feisar Moreno
 * @date 15/02/2013
 */
public class DbCausas770 extends DbConexion {
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 15/02/2013
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Causa770> getListaCausas770(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Causa770> listaCausas770 = new ArrayList<>();
            while (rst.next()) {
                Causa770 causa770Aux = new Causa770(rst);
                listaCausas770.add(causa770Aux);
            }
            rst.close();
            
            return listaCausas770;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna el listado de causas 770
     * @author Feisar Moreno
     * @date 15/02/2013
     * @return <code>ArrayList</code> con las causas 770.
     */
    public ArrayList<Causa770> getListaCausas770() {
        try {
            String sql =
                "SELECT * FROM causas_770 " +
                "ORDER BY codcausa";
            
            return this.getListaCausas770(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
