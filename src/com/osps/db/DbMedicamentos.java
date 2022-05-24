package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Medicamento;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla medicamentos
 * @author Feisar Moreno
 * @date 28/02/2012
 */
public class DbMedicamentos extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de medicamentos
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaMedicamentos Listado de medicamentos
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Medicamento> listaMedicamentos, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO medicamentos " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id, num_id, num_aut, cod_med, " +
                    "tip_med, nom_med, for_far, con_med, uni_med, num_uni, val_uni_med, val_tot_med, ind_borrado) VALUES ";
            String sql = "";
            for (Medicamento medicamentoAux : listaMedicamentos) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AM", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(medicamentoAux.getNumFac()) + ", " +
                        obtenerValorSQL(medicamentoAux.getCodEntPre()) + ", '" + medicamentoAux.getTipId() + "', '" + medicamentoAux.getNumId() + "', " +
                        obtenerValorSQL(medicamentoAux.getNumAut()) + ", '" + medicamentoAux.getCodMed() + "', " +
                        obtenerValorSQL(medicamentoAux.getTipMed()) + ", " + obtenerValorSQL(medicamentoAux.getNomMed()) + ", " +
                        obtenerValorSQL(medicamentoAux.getForFar()) + ", " + obtenerValorSQL(medicamentoAux.getConMed()) + ", " +
                        obtenerValorSQL(medicamentoAux.getUniMed()) + ", " + obtenerValorSQL(medicamentoAux.getNumUni()) + ", " +
                        obtenerValorSQL(medicamentoAux.getValUniMed()) + ", " + obtenerValorSQL(medicamentoAux.getValTotMed()) + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AM", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AM", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de medicamentos
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
                String procAlmacenado = "{? = call fu_validar_medicamentos(?,?,?)}";
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
    
    private ArrayList<Medicamento> getListaMedicamentos(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Medicamento> listaMedicamentos = new ArrayList<>();
            while (rst.next()) {
                Medicamento medicamentoAux = new Medicamento(rst);
                listaMedicamentos.add(medicamentoAux);
            }
            rst.close();
            
            return listaMedicamentos;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de medicamentos asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con los medicamentos.
     */
    public ArrayList<Medicamento> getListaMedicamentos(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, CD.nom_arch, TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM medicamentos T " +
                    "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                    "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                    "LEFT JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND T.id_grupo=" + idGrupo + " " +
                    "AND T.ind_borrado=0 ";
            if (indSinGrupo) {
                sql +=
                        "UNION ALL " +
                        "SELECT T.*, CD.nom_arch, TI.nombre_tipo_inter, U.tip_usu, U.nombre_sede, U.observaciones " +
                        "FROM medicamentos T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                        "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.id_grupo IS NULL " +
                        "AND T.ind_borrado=0 " +
                        "ORDER BY nom_arch, id_grupo DESC, tip_usu DESC, num_id, tip_id, cod_med";
            } else {
                sql += "ORDER BY CD.nom_arch, T.num_id, T.tip_id, T.cod_med";
            }
            
            return this.getListaMedicamentos(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
