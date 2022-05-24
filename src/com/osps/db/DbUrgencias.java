package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Urgencia;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla urgencias
 * @author Feisar Moreno
 * @date 28/02/2012
 */
public class DbUrgencias extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de urgecias
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaUrgencias Listado de consultas
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Urgencia> listaUrgencias, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO urgencias " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id, num_id, fecha_ing, hora_ing, num_aut, " +
                    "cau_ext, dia_sal, dia_rel1, dia_rel2, dia_rel3, des_usu, est_sal, cau_mue, fecha_sal, hora_sal, ind_borrado) VALUES ";
            String sql = "";
            for (Urgencia urgenciaAux : listaUrgencias) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AU", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                String fechaIngAux;
                String horaIngAux;
                if (urgenciaAux.getFechaIng() == null || urgenciaAux.getFechaIng().equals("")) {
                    fechaIngAux = "NULL";
                    horaIngAux = "NULL";
                } else {
                    fechaIngAux = "STR_TO_DATE('" + urgenciaAux.getFechaIng() + "', '%d/%m/%Y')";
                    horaIngAux = "STR_TO_DATE('" + urgenciaAux.getFechaIng() + " " + urgenciaAux.getHoraIng() + "', '%d/%m/%Y %H:%i')";
                }
                String fechaSalAux;
                String horaSalAux;
                if (urgenciaAux.getFechaSal() == null || urgenciaAux.getFechaSal().equals("")) {
                    fechaSalAux = "NULL";
                    horaSalAux = "NULL";
                } else {
                    fechaSalAux = "STR_TO_DATE('" + urgenciaAux.getFechaSal() + "', '%d/%m/%Y')";
                    horaSalAux = "STR_TO_DATE('" + urgenciaAux.getFechaSal() + " " + urgenciaAux.getHoraSal() + "', '%d/%m/%Y %H:%i')";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(urgenciaAux.getNumFac()) + ", " +
                        obtenerValorSQL(urgenciaAux.getCodEntPre()) + ", '" + urgenciaAux.getTipId() + "', '" + urgenciaAux.getNumId() + "', " +
                        fechaIngAux + ", " + horaIngAux + ", " + obtenerValorSQL(urgenciaAux.getNumAut()) + ", " +
                        obtenerValorSQL(urgenciaAux.getCauExt()) + ", " + obtenerValorSQL(urgenciaAux.getDiaSal()) + ", " +
                        obtenerValorSQL(urgenciaAux.getDiaRel1()) + ", " + obtenerValorSQL(urgenciaAux.getDiaRel2()) + ", " +
                        obtenerValorSQL(urgenciaAux.getDiaRel3()) + ", " + obtenerValorSQL(urgenciaAux.getDesUsu()) + ", " +
                        obtenerValorSQL(urgenciaAux.getEstSal()) + ", " + obtenerValorSQL(urgenciaAux.getCauMue()) + ", " +
                        fechaSalAux + ", " + horaSalAux + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AU", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AU", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de urgencias
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
                String procAlmacenado = "{? = call fu_validar_urgencias(?,?,?)}";
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
    
    private ArrayList<Urgencia> getListaUrgencias(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Urgencia> listaUrgencias = new ArrayList<>();
            while (rst.next()) {
                Urgencia urgenciaAux = new Urgencia(rst);
                listaUrgencias.add(urgenciaAux);
            }
            rst.close();
            
            return listaUrgencias;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de urgencias asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con las urgencias.
     */
    public ArrayList<Urgencia> getListaUrgencias(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, DATE_FORMAT(T.fecha_ing, '%d/%m/%Y') AS fecha_ing_t, DATE_FORMAT(T.hora_ing, '%H:%i') AS hora_ing_t, " +
                    "DATE_FORMAT(T.fecha_sal, '%d/%m/%Y') AS fecha_sal_t, DATE_FORMAT(T.hora_sal, '%H:%i') AS hora_sal_t, " +
                    "CD.nom_arch, TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM urgencias T " +
                    "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                    "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                    "LEFT JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND T.id_grupo=" + idGrupo + " " +
                    "AND T.ind_borrado=0 ";
            if (indSinGrupo) {
                sql +=
                        "UNION ALL " +
                        "SELECT T.*, DATE_FORMAT(T.fecha_ing, '%d/%m/%Y') AS fecha_ing_t, DATE_FORMAT(T.hora_ing, '%H:%i') AS hora_ing_t, " +
                        "DATE_FORMAT(T.fecha_sal, '%d/%m/%Y') AS fecha_sal_t, DATE_FORMAT(T.hora_sal, '%H:%i') AS hora_sal_t, " +
                        "CD.nom_arch, TI.nombre_tipo_inter, U.tip_usu, U.nombre_sede, U.observaciones " +
                        "FROM urgencias T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                        "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.id_grupo IS NULL " +
                        "AND T.ind_borrado=0 " +
                        "ORDER BY nom_arch, id_grupo DESC, tip_usu DESC, num_id, tip_id, fecha_ing";
            } else {
                sql += "ORDER BY CD.nom_arch, T.num_id, T.tip_id, T.fecha_ing";
            }
            
            return this.getListaUrgencias(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
