package com.osps.db;

import com.osps.entidad.EAdministradoras;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla eadministradoras
 * @author Feisar Moreno
 * @date 21/02/2012
 */
public class DbEAdministradoras extends DbConexion {
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 21/02/2012
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<EAdministradoras> getListaEntidades(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<EAdministradoras> listaEntidades = new ArrayList<EAdministradoras>();
            while (rst.next()) {
                EAdministradoras entidadAux = new EAdministradoras(rst);
                listaEntidades.add(entidadAux);
            }
            rst.close();
            
            return listaEntidades;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna las entidades administradoras.
     * @author Feisar Moreno
     * @date 21/02/2012
     * @return <code>ArrayList</code> con las entidades administradoras.
     */
    public ArrayList<EAdministradoras> getListaEntidades() {
        try {
            String sql =
                "SELECT * FROM eadministradoras ORDER BY nombre";
            
            return getListaEntidades(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<EAdministradoras>();
        }
    }
    
    /**
     * Método que retorna las entidades administradoras que han sido cargadas
     * indicando si han sido consolidadas.
     * @author Feisar Moreno
     * @date 16/07/2012
     * @return <code>ArrayList</code> con las entidades administradoras.
     */
    public ArrayList<EAdministradoras> getListaEntidadesConsolidado() {
        try {
            String sql =
                "SELECT DISTINCT EA.codentadm, EA.nombre, " +
                "CASE WHEN HI.codentadm IS NOT NULL THEN 'SI' ELSE 'NO' END AS indconsolidado " +
                "FROM carga_arch CA " +
                "INNER JOIN eadministradoras EA ON CA.codentadm=EA.codentadm " +
                "LEFT JOIN (" +
                "SELECT DISTINCT codentadm " +
                "FROM historial" +
                ") HI ON EA.codentadm=HI.codentadm " +
                "ORDER BY EA.nombre";
            
            return getListaEntidades(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<EAdministradoras>();
        }
    }
}
