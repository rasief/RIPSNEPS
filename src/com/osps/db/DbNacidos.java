package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Nacido;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla nacidos
 * @author Feisar Moreno
 * @date 28/02/2012
 */
public class DbNacidos extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de recien nacidos
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaNacidos Listado de recien nacidos
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Nacido> listaNacidos, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO nacidos " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id_mad, num_id_mad, fecha_nac, " +
                    "hora_nac, edad_ges, con_pre, sexo, peso, dia_nac, cau_mue, fecha_mue, hora_mue, ind_borrado) VALUES ";
            String sql = "";
            for (Nacido nacidoAux : listaNacidos) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AN", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                String fechaNacAux;
                String horaNacAux;
                if (nacidoAux.getFechaNac() == null || nacidoAux.getFechaNac().equals("")) {
                    fechaNacAux = "NULL";
                    horaNacAux = "NULL";
                } else {
                    fechaNacAux = "STR_TO_DATE('" + nacidoAux.getFechaNac() + "', '%d/%m/%Y')";
                    horaNacAux = "STR_TO_DATE('" + nacidoAux.getFechaNac() + " " + nacidoAux.getHoraNac() + "', '%d/%m/%Y %H:%i')";
                }
                String fechaMueAux;
                String horaMueAux;
                if (nacidoAux.getFechaMue() == null || nacidoAux.getFechaMue().equals("")) {
                    fechaMueAux = "NULL";
                    horaMueAux = "NULL";
                } else {
                    fechaMueAux = "STR_TO_DATE('" + nacidoAux.getFechaMue() + "', '%d/%m/%Y')";
                    horaMueAux = "STR_TO_DATE('" + nacidoAux.getFechaMue() + " " + nacidoAux.getHoraMue() + "', '%d/%m/%Y %H:%i')";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(nacidoAux.getNumFac()) + ", " +
                        obtenerValorSQL(nacidoAux.getCodEntPre()) + ", '" + nacidoAux.getTipIdMad() + "', '" + nacidoAux.getNumIdMad() + "', " +
                        fechaNacAux + ", " + horaNacAux + ", " + obtenerValorSQL(nacidoAux.getEdadGes()) + ", " +
                        obtenerValorSQL(nacidoAux.getConPre()) + ", " + obtenerValorSQL(nacidoAux.getSexo()) + ", " +
                        obtenerValorSQL(nacidoAux.getPeso()) + ", " + obtenerValorSQL(nacidoAux.getDiaNac()) + ", " +
                        obtenerValorSQL(nacidoAux.getCauMue()) + ", " + fechaMueAux + ", " + horaMueAux + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AN", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AN", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de recién nacidos
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
                String procAlmacenado = "{? = call fu_validar_nacidos(?,?,?)}";
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
    
    private ArrayList<Nacido> getListaNacidos(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Nacido> listaNacidos = new ArrayList<>();
            while (rst.next()) {
                Nacido nacidoAux = new Nacido(rst);
                listaNacidos.add(nacidoAux);
            }
            rst.close();
            
            return listaNacidos;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de recién nacidos asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con los recién nacidos.
     */
    public ArrayList<Nacido> getListaNacidos(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, DATE_FORMAT(T.fecha_nac, '%d/%m/%Y') AS fecha_nac_t, DATE_FORMAT(T.hora_nac, '%H:%i') AS hora_nac_t, " +
                    "DATE_FORMAT(T.fecha_mue, '%d/%m/%Y') AS fecha_mue_t, DATE_FORMAT(T.hora_mue, '%H:%i') AS hora_mue_t, " +
                    "CD.nom_arch, TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM nacidos T " +
                    "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                    "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                    "LEFT JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND T.id_grupo=" + idGrupo + " " +
                    "AND T.ind_borrado=0 ";
            if (indSinGrupo) {
                sql +=
                        "UNION ALL " +
                        "SELECT T.*, DATE_FORMAT(T.fecha_nac, '%d/%m/%Y') AS fecha_nac_t, DATE_FORMAT(T.hora_nac, '%H:%i') AS hora_nac_t, " +
                        "DATE_FORMAT(T.fecha_mue, '%d/%m/%Y') AS fecha_mue_t, DATE_FORMAT(T.hora_mue, '%H:%i') AS hora_mue_t, " +
                        "CD.nom_arch, TI.nombre_tipo_inter, U.tip_usu, U.nombre_sede, U.observaciones " +
                        "FROM nacidos T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                        "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.id_grupo IS NULL " +
                        "AND T.ind_borrado=0 " +
                        "ORDER BY nom_arch, id_grupo DESC, tip_usu DESC, num_id_mad, tip_id_mad, fecha_nac";
            } else {
                sql += "ORDER BY CD.nom_arch, T.num_id_mad, T.tip_id_mad, T.fecha_nac";
            }
            
            return this.getListaNacidos(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
