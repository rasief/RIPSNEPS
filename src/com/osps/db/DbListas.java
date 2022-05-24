package com.osps.db;

import com.osps.entidad.ListasDetalle;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla lista_detalle
 * @author Feisar Moreno
 * @date 12/12/2012
 */
public class DbListas extends DbConexion {
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 12/12/2012
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<ListasDetalle> getListaListaDetalle(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<ListasDetalle> listaListaDetalle = new ArrayList<>();
            while (rst.next()) {
                ListasDetalle listaDetalleAux = new ListasDetalle(rst);
                listaListaDetalle.add(listaDetalleAux);
            }
            rst.close();
            
            return listaListaDetalle;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna el detalle de una lista dada
     * @author Feisar Moreno
     * @date 12/12/2012
     * @param idLista Identificador de la lista
     * @return <code>ArrayList</code> con los datos de detalle de la lista.
     */
    public ArrayList<ListasDetalle> getListaListaDetalle(int idLista) {
        try {
            String sql =
                "SELECT * " +
                "FROM listas_detalle " +
                "WHERE idlista=" + idLista + " " +
                "ORDER BY orden";
            
            return this.getListaListaDetalle(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
}
