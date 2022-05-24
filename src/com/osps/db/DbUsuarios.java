package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Usuario;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla usuarios
 * @author Feisar Moreno
 * @date 23/02/2012
 */
public class DbUsuarios extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de usuarios
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaUsuarios Listado de usuarios
     * @param cargaArchDeta Objeto de detalle de carga
     * @return identificador del detalle de carga si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Usuario> listaUsuarios, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO usuarios " +
                    "(id_carga_deta, num_registro, tip_id, num_id, cod_ent_adm, tip_usu, ape_1, ape_2, " +
                    "nom_1, nom_2, edad, uni_edad, sexo, cod_dep, cod_mun, zona, ind_err, ind_borrado) VALUES ";
            String sql = "";
            for (Usuario usuarioAux : listaUsuarios) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "US", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", '" + usuarioAux.getTipId() + "', '" + usuarioAux.getNumId() + "', '" +
                        usuarioAux.getCodEntAdm() + "', '" + usuarioAux.getTipUsu() + "', '" + usuarioAux.getApe1() + "', " +
                        obtenerValorSQL(usuarioAux.getApe2()) + ", '" + usuarioAux.getNom1() + "', " + obtenerValorSQL(usuarioAux.getNom2()) + ", " +
                        obtenerValorSQL(usuarioAux.getEdad()) + ", '" + usuarioAux.getUniEdad() + "', '" + usuarioAux.getSexo() + "', '" +
                        usuarioAux.getCodDep() + "', '" + usuarioAux.getCodMun() + "', '" + usuarioAux.getZona() + "', 0, 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "US", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "US", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que carga los datos de los usuarios asociados a una carga desde la base de afiliados a Nueva EPS
     * @author Feisar Moreno
     * @date 22/10/2019
     * @param idCarga Identificador de la carga
     * @return Valor positivo si se realizó la carga, -1 en caso de error
     */
    public int cargarUsuarios(long idCarga) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_cargar_usuarios(?,?)}";
            int resultado;
            try (CallableStatement cstmt = conn.prepareCall(procAlmacenado)) {
                cstmt.setLong(1, idCarga);
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.execute();
                resultado = cstmt.getInt(2);
            }
            
            return resultado;
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            return -1;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que realiza la validación de los registros de usuarios
     * @param idValida Identificaior de la validación
     * @param codEntAdm Código de la entidad administradora
     * @param trimAnio Periodo a validar
     * @return <code>true</code> si se pudo realizar la validación,
     * de lo contrario <code>false</code>.
     */
    public boolean validarRegistros(long idValida, String codEntAdm, String trimAnio) {
        int cantidad = 0;
        boolean validado = true;
        
        do {
            try {
                crearConexion();
                String procAlmacenado = "{? = call fu_validar_usuarios(?,?,?)}";
                try (CallableStatement cstmt = conn.prepareCall(procAlmacenado)) {
                    cstmt.registerOutParameter(1, Types.INTEGER);
                    cstmt.setLong(2, idValida);
                    cstmt.setString(3, codEntAdm);
                    cstmt.setString(4, trimAnio);
                    cstmt.execute();
                    cantidad = cstmt.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
                validado = false;
            } finally {
                cerrarConexion();
            }
        } while (cantidad > 0 && validado);
        
        return validado;
    }
    
    private ArrayList<Usuario> getListaUsuarios(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Usuario> listaUsuarios = new ArrayList<>();
            while (rst.next()) {
                Usuario usuarioAux = new Usuario(rst);
                listaUsuarios.add(usuarioAux);
            }
            rst.close();
            
            return listaUsuarios;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de usuarios asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con los usuario.
     */
    public ArrayList<Usuario> getListaUsuarios(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT U.* " +
                    "FROM usuarios U " +
                    "WHERE U.id_carga=" + idCarga + " " +
                    "AND U.id_grupo=" + idGrupo + " " +
                    "AND U.ind_borrado=0 ";
            if (indSinGrupo) {
                sql +=
                        "UNION ALL " +
                        "SELECT U.* " +
                        "FROM usuarios U " +
                        "WHERE U.id_carga=" + idCarga + " " +
                        "AND U.id_grupo IS NULL " +
                        "AND U.ind_borrado=0 " +
                        "ORDER BY id_grupo DESC, tip_usu DESC, num_id, tip_id";
            } else {
                sql += "ORDER BY U.num_id, U.tip_id";
            }
            
            return this.getListaUsuarios(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
