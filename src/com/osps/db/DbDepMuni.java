package com.osps.db;

import com.osps.entidad.DepMuni;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de las tablas de departamentos y municipios
 * @author Feisar Moreno
 * @date 15/02/2013
 */
public class DbDepMuni extends DbConexion {
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 15/02/2013
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<DepMuni> getListaDepMuni(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<DepMuni> listaDepMuni = new ArrayList<>();
            while (rst.next()) {
                DepMuni depMuniAux = new DepMuni(rst);
                listaDepMuni.add(depMuniAux);
            }
            rst.close();
            
            return listaDepMuni;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los municipios de un departamento
     * @author Feisar Moreno
     * @date 15/02/2013
     * @param codDep Código DANE del departamento
     * @return <code>ArrayList</code> con los municipios.
     */
    public ArrayList<DepMuni> getListaMunicipios(String codDep) {
        try {
            String sql =
                "SELECT coddep, codmun, nombre AS nommun, codprov, codndp, coddane " +
                "FROM municipios " +
                "WHERE coddep='" + codDep + "' " +
                "ORDER BY coddane";
            
            return this.getListaDepMuni(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
