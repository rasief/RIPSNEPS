package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Procedimiento;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla procedimientos
 * @author Feisar Moreno
 * @date 27/02/2012
 */
public class DbProcedimientos extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de procedimientos
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaProcedimientos Listado de procedimientos
     * @param cargaArchDeta Objeto de detalle de carga
     * @param indBorrarAT Indicador de borrado de los registros asociados de otros servicios
     * @param observacionBase Texto base de la observación
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Procedimiento> listaProcedimientos, CargaArchDeta cargaArchDeta, boolean indBorrarAT, String observacionBase) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO procedimientos " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id, num_id, fecha_pro, num_aut, cod_pro, amb_rea_pro, " +
                    "fin_pro, per_ati, dia_pri, dia_rel, dia_com, for_rea, val_pro, id_otro_serv, ind_borrado, observaciones) VALUES ";
            String sqlBaseAT = "UPDATE otros_servicios " +
                    "SET ind_borrado=1 " +
                    "WHERE id_otro_serv IN (";
            String sql = "";
            String cadena = "";
            for (Procedimiento procedimientoAux : listaProcedimientos) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AP", contTotal, 0, false);
                    
                    if (indBorrarAT && !cadena.equals("")) {
                        cadena = sqlBaseAT + cadena + ")";
                        
                        try (PreparedStatement pstmt = conn.prepareStatement(cadena)) {
                            pstmt.execute();
                        }
                    }
                    
                    this.commit();
                    
                    sql = "";
                    cadena = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                    if (indBorrarAT && procedimientoAux.getIdOtroServ() > 0) {
                        cadena += ", ";
                    }
                }
                String fechaAux;
                if (procedimientoAux.getFechaPro() == null ||procedimientoAux.getFechaPro().equals("")) {
                    fechaAux = "NULL";
                } else {
                    fechaAux = "STR_TO_DATE('" + procedimientoAux.getFechaPro() + "', '%d/%m/%Y')";
                }
                String observacionAux;
                if (!observacionBase.equals("") && procedimientoAux.getIdOtroServ() > 0) {
                    observacionAux = "'" + observacionBase + " - " + procedimientoAux.getNumRegistro() + "'";
                } else {
                    observacionAux = "NULL";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(procedimientoAux.getNumFac()) + ", " +
                        obtenerValorSQL(procedimientoAux.getCodEntPre()) + ", '" + procedimientoAux.getTipId() + "', '" + procedimientoAux.getNumId() + "', " +
                        fechaAux + ", " + obtenerValorSQL(procedimientoAux.getNumAut()) + ", '" + procedimientoAux.getCodPro() + "', " +
                        obtenerValorSQL(procedimientoAux.getAmbReaPro()) + ", " + obtenerValorSQL(procedimientoAux.getFinPro()) + ", " +
                        obtenerValorSQL(procedimientoAux.getPerAti()) + ", " + obtenerValorSQL(procedimientoAux.getDiaPri()) + ", " +
                        obtenerValorSQL(procedimientoAux.getDiaRel()) + ", " + obtenerValorSQL(procedimientoAux.getDiaCom()) + ", " +
                        obtenerValorSQL(procedimientoAux.getForRea()) + ", " + obtenerValorSQL(procedimientoAux.getValPro()) + ", " +
                        obtenerValorSQL(procedimientoAux.getIdOtroServ()) + ", 0, " + observacionAux + ")";
                
                if (indBorrarAT && procedimientoAux.getIdOtroServ() > 0) {
                    cadena += "" + procedimientoAux.getIdOtroServ();
                }
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AP", contTotal, 0, false);
                
                if (indBorrarAT && !cadena.equals("")) {
                    cadena = sqlBaseAT + cadena + ")";
                    
                    try (PreparedStatement pstmt = conn.prepareStatement(cadena)) {
                        pstmt.execute();
                    }
                }
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AP", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de procedimientos
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
                String procAlmacenado = "{? = call fu_validar_procedimientos(?,?,?)}";
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
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 07/10/2019
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Procedimiento> getListaProcedimientos(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Procedimiento> listaProcedimientos = new ArrayList<>();
            while (rst.next()) {
                Procedimiento procedimientoAux = new Procedimiento(rst);
                listaProcedimientos.add(procedimientoAux);
            }
            rst.close();
            
            return listaProcedimientos;
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
     * @param codPro Código del procedimiento
     * @return <code>ArrayList</code> con los procedimientos que cumplen las condiciones recibidas.
     */
    public ArrayList<Procedimiento> getListaProcedimientos(long idCarga, String tipId, String numId, String codPro) {
        try {
            String sql =
                    "SELECT P.*, DATE_FORMAT(P.fecha_pro, '%d/%m/%Y') AS fecha_pro_t " +
                    "FROM procedimientos P " +
                    "INNER JOIN carga_arch_deta CD ON P.id_carga_deta=CD.id_carga_deta " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND P.tip_id='" + tipId + "' " +
                    "AND P.num_id='" + numId + "' " +
                    "AND P.cod_pro='" + codPro + "' " +
                    "AND P.ind_borrado=0 " +
                    "ORDER BY P.fecha_pro, P.id_procedimiento";
            
            return this.getListaProcedimientos(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los registros de procedimientos asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con los procedimientos.
     */
    public ArrayList<Procedimiento> getListaProcedimientos(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, DATE_FORMAT(T.fecha_pro, '%d/%m/%Y') AS fecha_pro_t, CD.nom_arch, " +
                    "TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM procedimientos T " +
                    "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                    "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                    "LEFT JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND T.id_grupo=" + idGrupo + " " +
                    "AND T.ind_borrado=0 ";
            if (indSinGrupo) {
                sql +=
                        "UNION ALL " +
                        "SELECT T.*, DATE_FORMAT(T.fecha_pro, '%d/%m/%Y') AS fecha_pro_t, CD.nom_arch, " +
                        "TI.nombre_tipo_inter, U.tip_usu, U.nombre_sede, U.observaciones " +
                        "FROM procedimientos T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                        "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.id_grupo IS NULL " +
                        "AND T.ind_borrado=0 " +
                        "ORDER BY nom_arch, id_grupo DESC, tip_usu DESC, num_id, tip_id, fecha_pro, cod_pro";
            } else {
                sql += "ORDER BY CD.nom_arch, T.num_id, T.tip_id, T.fecha_pro, T.cod_pro";
            }
            
            return this.getListaProcedimientos(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
