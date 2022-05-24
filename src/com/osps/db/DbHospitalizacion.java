package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Hospitalizacion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla hospitalizacion
 * @author Feisar Moreno
 * @date 27/02/2012
 */
public class DbHospitalizacion extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de hospitalizaciones
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaHospitalizaciones Listado de hospitalizaciones
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Hospitalizacion> listaHospitalizaciones, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO hospitalizacion " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id, num_id, via_ing, fecha_ing, hora_ing, num_aut, " +
                    "cau_ext, dia_ing, dia_egr, dia_rel1, dia_rel2, dia_rel3, dia_com, est_sal, dia_mue, fecha_egr, hora_egr, ind_borrado) VALUES ";
            String sql = "";
            for (Hospitalizacion hospitalizacionAux : listaHospitalizaciones) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AH", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                String fechaIngAux;
                String horaIngAux;
                if (hospitalizacionAux.getFechaIng() == null || hospitalizacionAux.getFechaIng().equals("")) {
                    fechaIngAux = "NULL";
                    horaIngAux = "NULL";
                } else {
                    fechaIngAux = "STR_TO_DATE('" + hospitalizacionAux.getFechaIng() + "', '%d/%m/%Y')";
                    horaIngAux = "STR_TO_DATE('" + hospitalizacionAux.getFechaIng() + " " + hospitalizacionAux.getHoraIng() + "', '%d/%m/%Y %H:%i')";
                }
                String fechaEgrAux;
                String horaEgrAux;
                if (hospitalizacionAux.getFechaEgr() == null || hospitalizacionAux.getFechaEgr().equals("")) {
                    fechaEgrAux = "NULL";
                    horaEgrAux = "NULL";
                } else {
                    fechaEgrAux = "STR_TO_DATE('" + hospitalizacionAux.getFechaEgr() + "', '%d/%m/%Y')";
                    horaEgrAux = "STR_TO_DATE('" + hospitalizacionAux.getFechaEgr() + " " + hospitalizacionAux.getHoraEgr() + "', '%d/%m/%Y %H:%i')";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(hospitalizacionAux.getNumFac()) + ", " +
                        obtenerValorSQL(hospitalizacionAux.getCodEntPre()) + ", '" + hospitalizacionAux.getTipId() + "', '" + hospitalizacionAux.getNumId() + "', " +
                        obtenerValorSQL(hospitalizacionAux.getViaIng()) + ", " + fechaIngAux + ", " + horaIngAux + ", " +
                        obtenerValorSQL(hospitalizacionAux.getNumAut()) + ", " + obtenerValorSQL(hospitalizacionAux.getCauExt()) + ", " +
                        obtenerValorSQL(hospitalizacionAux.getDiaIng()) + ", " + obtenerValorSQL(hospitalizacionAux.getDiaEgr()) + ", " +
                        obtenerValorSQL(hospitalizacionAux.getDiaRel1()) + ", " + obtenerValorSQL(hospitalizacionAux.getDiaRel2()) + ", " +
                        obtenerValorSQL(hospitalizacionAux.getDiaRel3()) + ", " + obtenerValorSQL(hospitalizacionAux.getDiaCom()) + ", " +
                        obtenerValorSQL(hospitalizacionAux.getEstSal()) + ", " + obtenerValorSQL(hospitalizacionAux.getDiaMue()) + ", " +
                        fechaEgrAux + ", " + horaEgrAux + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AH", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AH", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que unifica registros de hospitalización de un mismo usuario cuando la diferencia en horas entre ingreso y egreso es inferior a 3 horas
     * @author Feisar Moreno
     * @date 09/10/2019
     * @param idCarga Identificador del registro de carga
     * @return Valor positivo si se realizó la unificación, -1 en caso de error
     */
    public int unificarHospitalizaciones(long idCarga) {
        try {
            crearConexion();
            
            String procAlmacenado = "{call pa_unificar_hospitalizaciones(?,?)}";
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
     * Método que realiza la validación de los registros de hospitalización
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
                String procAlmacenado = "{? = call fu_validar_hospitalizaciones(?,?,?)}";
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
    
    private ArrayList<Hospitalizacion> getListaHospitalizacion(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Hospitalizacion> listaHospitalizacion = new ArrayList<>();
            while (rst.next()) {
                Hospitalizacion hospitalizacionAux = new Hospitalizacion(rst);
                listaHospitalizacion.add(hospitalizacionAux);
            }
            rst.close();
            
            return listaHospitalizacion;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de procedimientos con un código específico para un usuario dado.
     * @author Feisar Moreno
     * @date 07/10/2019
     * @param idCarga Identificador de la carga
     * @param tipId Tipo de identificación del usuario
     * @param numId Número de identificación del usuario
     * @param ano Año de la carga
     * @param mes Mes de la carga
     * @return <code>ArrayList</code> con los procedimientos que cumplen las condiciones recibidas.
     */
    public ArrayList<Hospitalizacion> getListaHospitalizacion(long idCarga, String tipId, String numId, int ano, int mes) {
        try {
            String periodo = ano + "/" + (mes < 10 ? "0" : "") + mes;
            String sql =
                    "SELECT H.*, DATE_FORMAT(H.fecha_ing, '%d/%m/%Y') AS fecha_ing_t, DATE_FORMAT(H.hora_ing, '%H:%i') AS hora_ing_t, " +
                    "DATE_FORMAT(H.fecha_egr, '%d/%m/%Y') AS fecha_egr_t, DATE_FORMAT(H.hora_egr, '%H:%i') AS hora_egr_t " +
                    "FROM hospitalizacion H " +
                    "INNER JOIN carga_arch_deta CD ON H.id_carga_deta=CD.id_carga_deta " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND H.tip_id='" + tipId + "' " +
                    "AND H.num_id='" + numId + "' " +
                    "AND DATE_FORMAT(H.fecha_ing, '%Y/%m')<='" + periodo + "' " +
                    "AND DATE_FORMAT(H.fecha_egr, '%Y/%m')>='" + periodo + "' " +
                    "AND H.ind_borrado=0 " +
                    "ORDER BY DATEDIFF(H.fecha_egr, H.fecha_ing) DESC, H.id_hospitalizacion";
            
            return this.getListaHospitalizacion(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los registros de hospitalización asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con las hospitalizaciones.
     */
    public ArrayList<Hospitalizacion> getListaHospitalizacion(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, DATE_FORMAT(T.fecha_ing, '%d/%m/%Y') AS fecha_ing_t, DATE_FORMAT(T.hora_ing, '%H:%i') AS hora_ing_t, " +
                    "DATE_FORMAT(T.fecha_egr, '%d/%m/%Y') AS fecha_egr_t, DATE_FORMAT(T.hora_egr, '%H:%i') AS hora_egr_t, " +
                    "CD.nom_arch, TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM hospitalizacion T " +
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
                        "DATE_FORMAT(T.fecha_egr, '%d/%m/%Y') AS fecha_egr_t, DATE_FORMAT(T.hora_egr, '%H:%i') AS hora_egr_t, " +
                        "CD.nom_arch, TI.nombre_tipo_inter, U.tip_usu, U.nombre_sede, U.observaciones " +
                        "FROM hospitalizacion T " +
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
            
            return this.getListaHospitalizacion(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
