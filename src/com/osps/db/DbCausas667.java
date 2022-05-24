package com.osps.db;

import com.osps.entidad.Causa667;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla causas_667
 * @author Feisar Moreno
 * @date 27/02/2013
 */
public class DbCausas667 extends DbConexion {
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 27/02/2013
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Causa667> getListaCausas667(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Causa667> listaCausas667 = new ArrayList<>();
            while (rst.next()) {
                Causa667 causa667Aux = new Causa667(rst);
                listaCausas667.add(causa667Aux);
            }
            rst.close();
            
            return listaCausas667;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna el listado de causas padre 667
     * @author Feisar Moreno
     * @date 27/02/2013
     * @return <code>ArrayList</code> con las causas 667.
     */
    public ArrayList<Causa667> getListaCausas667Padre() {
        try {
            String sql =
                "SELECT * FROM causas_667 " +
                "WHERE codcausa IN (" +
                    "SELECT codpadre FROM causas_667 " +
                    "WHERE codpadre IS NOT NULL" +
                ") " +
                "ORDER BY codpadre DESC, codcausa";
            
            return this.getListaCausas667(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
