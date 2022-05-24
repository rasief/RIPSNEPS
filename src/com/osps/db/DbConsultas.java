package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Consulta;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla consultas
 * @author Feisar Moreno
 * @date 27/02/2012
 */
public class DbConsultas extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de consultas
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaConsultas Listado de consultas
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Consulta> listaConsultas, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO consultas " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id, num_id, fecha_con, num_aut, cod_con, " +
                    "fin_con, cau_ext, cod_dia, dia_rel1, dia_rel2, dia_rel3, tip_dia, val_con, val_cuo, val_net, ind_borrado) VALUES ";
            String sql = "";
            for (Consulta consultaAux : listaConsultas) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AC", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                //Se ajusta el tipo de diagnóstico en caso de venir vacío
                String tipDiaAux;
                if (consultaAux.getTipDia() != null && !consultaAux.getTipDia().equals("")) {
                    tipDiaAux = consultaAux.getTipDia();
                } else if (consultaAux.getCodCon() != null) {
                    switch ((consultaAux.getCodCon() + "    ").substring(0, 4)) {
                        case "8901":
                        case "8902":
                        case "8904":
                        case "8905":
                        case "8907":
                            tipDiaAux = "2";
                            break;
                        case "8903":
                            tipDiaAux = "3";
                            break;
                        default:
                            tipDiaAux = "";
                            break;
                    }
                } else {
                    tipDiaAux = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(consultaAux.getNumFac()) + ", " +
                        obtenerValorSQL(consultaAux.getCodEntPre()) + ", '" + consultaAux.getTipId() + "', '" + consultaAux.getNumId() +
                        "', STR_TO_DATE('" + consultaAux.getFechaCon() + "', '%d/%m/%Y'), " +
                        obtenerValorSQL(consultaAux.getNumAut()) + ", '" + consultaAux.getCodCon() + "', " +
                        obtenerValorSQL(consultaAux.getFinCon()) + ", " + obtenerValorSQL(consultaAux.getCauExt()) + ", " +
                        obtenerValorSQL(consultaAux.getCodDia()) + ", " + obtenerValorSQL(consultaAux.getDiaRel1()) + ", " +
                        obtenerValorSQL(consultaAux.getDiaRel2()) + ", " + obtenerValorSQL(consultaAux.getDiaRel3()) + ", " +
                        obtenerValorSQL(tipDiaAux) + ", " + obtenerValorSQL(consultaAux.getValCon()) + ", " +
                        obtenerValorSQL(consultaAux.getValCuo()) + ", " + obtenerValorSQL(consultaAux.getValNet()) + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AC", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException | StringIndexOutOfBoundsException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AC", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de consultas
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
                String procAlmacenado = "{? = call fu_validar_consultas(?,?,?)}";
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
    
    private ArrayList<Consulta> getListaConsultas(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Consulta> listaConsultas = new ArrayList<>();
            while (rst.next()) {
                Consulta consultaAux = new Consulta(rst);
                listaConsultas.add(consultaAux);
            }
            rst.close();
            
            return listaConsultas;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de consultas asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con las consultas.
     */
    public ArrayList<Consulta> getListaConsultas(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, DATE_FORMAT(T.fecha_con, '%d/%m/%Y') AS fecha_con_t, CD.nom_arch, " +
                    "TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM consultas T " +
                    "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                    "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                    "LEFT JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND T.id_grupo=" + idGrupo + " " +
                    "AND T.ind_borrado=0 ";
            if (indSinGrupo) {
                sql +=
                        "UNION ALL " +
                        "SELECT T.*, DATE_FORMAT(T.fecha_con, '%d/%m/%Y') AS fecha_con_t, CD.nom_arch, " +
                        "TI.nombre_tipo_inter, U.tip_usu, U.nombre_sede, U.observaciones " +
                        "FROM consultas T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                        "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.id_grupo IS NULL " +
                        "AND T.ind_borrado=0 " +
                        "ORDER BY nom_arch, id_grupo DESC, tip_usu DESC, num_id, tip_id, fecha_con, cod_con";
            } else {
                sql += "ORDER BY CD.nom_arch, T.num_id, T.tip_id, T.fecha_con, T.cod_con";
            }
            
            return this.getListaConsultas(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
