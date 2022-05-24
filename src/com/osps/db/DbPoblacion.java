package com.osps.db;

import com.osps.entidad.Poblacion;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de las consultas de poblacion
 * @author Feisar Moreno
 * @date 21/02/2013
 */
public class DbPoblacion extends DbConexion {
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 15/02/2013
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Poblacion> getListaPoblacion(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Poblacion> listaPoblacion = new ArrayList<>();
            while (rst.next()) {
                Poblacion poblacionAux = new Poblacion(rst);
                listaPoblacion.add(poblacionAux);
            }
            rst.close();
            
            return listaPoblacion;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los datos de población por sexo y grupo de edad (trazadores)
     * @author Feisar Moreno
     * @date 21/02/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de población.
     */
    public ArrayList<Poblacion> getListaPoblacionSexoGEdad(String codDep, int ano) {
        try {
            String sql =
                "SELECT sexo, gedad, SUM(habitantes) AS habitantes " +
                "FROM vi_poblacion_trazadores " +
                "WHERE cod_dep='" + codDep + "' " +
                "AND ano=" + ano + " " +
                "GROUP BY sexo, gedad " +
                "ORDER BY sexo, gedad";
            
            return this.getListaPoblacion(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los datos de población por municipio y grupo de edad (trazadores)
     * @author Feisar Moreno
     * @date 21/02/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de población.
     */
    public ArrayList<Poblacion> getListaPoblacionMunGEdad(String codDep, int ano) {
        try {
            String sql =
                "SELECT cod_mun, cod_mun_dane, gedad, SUM(habitantes) AS habitantes " +
                "FROM vi_poblacion_trazadores " +
                "WHERE cod_dep='" + codDep + "' " +
                "AND ano=" + ano + " " +
                "GROUP BY cod_mun, cod_mun_dane, gedad " +
                "ORDER BY cod_mun, gedad";
            
            return this.getListaPoblacion(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los datos de población por grupo de edad (trazadores)
     * @author Feisar Moreno
     * @date 21/02/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de población.
     */
    public ArrayList<Poblacion> getListaPoblacionGEdad(String codDep, int ano) {
        try {
            String sql =
                "SELECT gedad, SUM(habitantes) AS habitantes " +
                "FROM vi_poblacion_trazadores " +
                "WHERE cod_dep='" + codDep + "' " +
                "AND ano=" + ano + " " +
                "GROUP BY gedad " +
                "ORDER BY gedad";
            
            return this.getListaPoblacion(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los datos de población por municipio
     * @author Feisar Moreno
     * @date 21/02/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de población.
     */
    public ArrayList<Poblacion> getListaPoblacionMun(String codDep, int ano) {
        try {
            String sql =
                "SELECT cod_mun, cod_mun_dane, SUM(habitantes) AS habitantes " +
                "FROM vi_poblacion_trazadores " +
                "WHERE cod_dep='" + codDep + "' " +
                "AND ano=" + ano + " " +
                "GROUP BY cod_mun, cod_mun_dane " +
                "ORDER BY cod_mun";
            
            return this.getListaPoblacion(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
