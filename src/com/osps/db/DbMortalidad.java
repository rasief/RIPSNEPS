package com.osps.db;

import com.osps.entidad.Mortalidad;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de las consultas de mortalidad
 * @author Feisar Moreno
 * @date 15/02/2013
 */
public class DbMortalidad extends DbConexion {
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 15/02/2013
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Mortalidad> getListaMortalidad(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Mortalidad> listaMortalidad = new ArrayList<>();
            while (rst.next()) {
                Mortalidad mortalidadAux = new Mortalidad(rst);
                listaMortalidad.add(mortalidadAux);
            }
            rst.close();
            
            return listaMortalidad;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los datos de mortalidad para indicadores básicos
     * @author Feisar Moreno
     * @date 15/02/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de mortalidad.
     */
    public ArrayList<Mortalidad> getListaMortalidadBasico(String codDep, int ano) {
        try {
            String sql =
                "SELECT D.tipo_defun, D.gru_ed2, D.sexo, C6.codpadre AS cod667, " +
                "CX.cod770, D.codmunre AS codmun, COUNT(*) AS cantidadt " +
                "FROM defunciones08 D " +
                "LEFT JOIN ciex CX ON D.c_bas1=CX.codciex " +
                "LEFT JOIN causas_667 C6 ON CX.cod667=C6.codcausa " +
                "WHERE D.codptore='" + codDep + "' " +
                "AND D.def_anio='" + ano + "' " +
                "GROUP BY D.tipo_defun, D.gru_ed2, D.sexo, C6.codpadre, CX.cod770, D.codptore, D.codmunre " +
                "ORDER BY D.tipo_defun, D.gru_ed2, D.sexo, C6.codpadre, CX.cod770, D.codmunre";
            
            return this.getListaMortalidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna las cantidades de sexo y grupo de edad incorrectos
     * @author Feisar Moreno
     * @date 21/02/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de mortalidad.
     */
    public ArrayList<Mortalidad> getListaMortalidadBasicoIncorrectos(String codDep, int ano) {
        try {
            String sql =
                "SELECT sexo, null AS gru_ed2, COUNT(*) AS cantidadt " +
                "FROM defunciones08 " +
                "WHERE codptore='" + codDep + "' " +
                "AND def_anio='" + ano + "' " +
                "AND sexo='3' " +
                "GROUP BY sexo " +
                "UNION ALL " +
                "SELECT null, gru_ed2, COUNT(*) " +
                "FROM defunciones08 " +
                "WHERE codptore='" + codDep + "' " +
                "AND def_anio='" + ano + "' " +
                "AND gru_ed2='07' " +
                "GROUP BY gru_ed2 " +
                "ORDER BY sexo";
            
            return this.getListaMortalidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna las cantidades de causas 667 de registros
     * sin dato de sexo
     * @author Feisar Moreno
     * @date 05/03/2013
     * @param codDep Código del departamento
     * @param ano Año de consulta
     * @return <code>ArrayList</code> con los datos de mortalidad.
     */
    public ArrayList<Mortalidad> getListaMortalidad667SinSexo(String codDep, int ano) {
        try {
            String sql =
                "SELECT CP.codcausa AS cod667, COUNT(*) AS cantidadt " +
                "FROM defunciones08 D " +
                "LEFT JOIN ciex CX ON D.c_bas1=CX.codciex " +
                "LEFT JOIN causas_667 C6 ON CX.cod667=C6.codcausa " +
                "LEFT JOIN causas_667 CP ON C6.codpadre=CP.codcausa " +
                "WHERE D.codptore='" + codDep + "' " +
                "AND D.def_anio='" + ano + "' " +
                "AND D.sexo NOT IN ('1', '2') " +
                "GROUP BY CP.codcausa, CP.codpadre " +
                "ORDER BY CP.codpadre DESC, CP.codcausa";
            
            return this.getListaMortalidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
