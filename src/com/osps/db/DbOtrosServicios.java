package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.OtroServicio;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla otros_servicios
 * @author Feisar Moreno
 * @date 18/03/2019
 */
public class DbOtrosServicios extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de otros servicios
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaOtrosServicios Listado de otros servicios
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<OtroServicio> listaOtrosServicios, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO otros_servicios " +
                    "(id_carga_deta, num_registro, num_fac, cod_ent_pre, tip_id, num_id, num_aut, " +
                    "tip_serv, cod_serv, nom_serv, cantidad, val_uni_serv, val_tot_serv, ind_borrado) VALUES ";
            String sql = "";
            for (OtroServicio otroServicioAux : listaOtrosServicios) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AT", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", " + obtenerValorSQL(otroServicioAux.getNumFac()) + ", " +
                        obtenerValorSQL(otroServicioAux.getCodEntPre()) + ", '" + otroServicioAux.getTipId() + "', '" + otroServicioAux.getNumId() + "', " +
                        obtenerValorSQL(otroServicioAux.getNumAut()) + ", " + obtenerValorSQL(otroServicioAux.getTipServ()) + ", '" +
                        otroServicioAux.getCodServ()+ "', " + obtenerValorSQL(otroServicioAux.getNomServ()) + ", " +
                        obtenerValorSQL(otroServicioAux.getCantidad()) + ", " + obtenerValorSQL(otroServicioAux.getValUniServ()) + ", " +
                        obtenerValorSQL(otroServicioAux.getValTotServ()) + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AT", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AT", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de otros servicios
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
                String procAlmacenado = "{? = call fu_validar_otros_servicios(?,?,?)}";
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
     * @date 03/10/2019
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<OtroServicio> getListaOtrosServicios(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<OtroServicio> listaOtrosServicios = new ArrayList<>();
            while (rst.next()) {
                OtroServicio otroServicioAux = new OtroServicio(rst);
                listaOtrosServicios.add(otroServicioAux);
            }
            rst.close();
            
            return listaOtrosServicios;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los registros de otros servicios de un tipo dado para un grupo de carga específico.
     * @author Feisar Moreno
     * @date 03/10/2019
     * @param idCarga Identificador de la carga
     * @param codGrupo Código del grupo de servicios
     * @return <code>ArrayList</code> con los servicios que cumplen las condiciones recibidas.
     */
    public ArrayList<OtroServicio> getListaOtrosServicios(long idCarga, String codGrupo) {
        try {
            String sql =
                    "SELECT OS.* " +
                    "FROM otros_servicios OS " +
                    "INNER JOIN carga_arch_deta CD ON OS.id_carga_deta=CD.id_carga_deta " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND OS.cod_serv LIKE '" + codGrupo + "%' " +
                    "AND OS.ind_borrado=0 " +
                    "ORDER BY OS.id_carga_deta, OS.num_registro";
            
            return this.getListaOtrosServicios(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los registros de otros servicios con código incluido en un array de entrada.
     * @author Feisar Moreno
     * @date 08/10/2019
     * @param idCarga Identificador de la carga
     * @param arrProcedimientos Array con los códigos de servicios a incluir
     * @return <code>ArrayList</code> con los servicios que cumplen las condiciones recibidas.
     */
    public ArrayList<OtroServicio> getListaOtrosServicios(long idCarga, String[] arrProcedimientos) {
        try {
            String cadenaProcedimientos = "";
            for (String codProcedimiento : arrProcedimientos) {
                if (!cadenaProcedimientos.equals("")) {
                    cadenaProcedimientos += ", ";
                }
                cadenaProcedimientos += "'" + codProcedimiento + "'";
            }
            
            String sql =
                    "SELECT OS.* " +
                    "FROM otros_servicios OS " +
                    "INNER JOIN carga_arch_deta CD ON OS.id_carga_deta=CD.id_carga_deta " +
                    "WHERE CD.id_carga=" + idCarga + " " +
                    "AND OS.cod_serv IN (" + cadenaProcedimientos + ") " +
                    "AND OS.ind_borrado=0 " +
                    "ORDER BY OS.id_carga_deta, OS.num_registro";
            
            return this.getListaOtrosServicios(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna los registros de otros servicios asociados a una carga y un grupo.
     * @author Feisar Moreno
     * @date 24/10/2019
     * @param idCarga Identificador de la carga
     * @param idGrupo Identificador del grupo
     * @param indSinGrupo Indicador de inclusión de registros sin grupo
     * @return <code>ArrayList</code> con los otros servicios.
     */
    public ArrayList<OtroServicio> getListaOtrosServicios(long idCarga, int idGrupo, boolean indSinGrupo) {
        try {
            String sql =
                    "SELECT T.*, CD.nom_arch, TI.nombre_tipo_inter, NULL AS tip_usu, U.nombre_sede, NULL AS observaciones_us " +
                    "FROM otros_servicios T " +
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
                        "FROM otros_servicios T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN usuarios U ON T.id_usuario=U.id_usuario " +
                        "LEFT JOIN tipos_internacion TI ON T.id_tipo_inter=TI.id_tipo_inter " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.id_grupo IS NULL " +
                        "AND T.ind_borrado=0 " +
                        "ORDER BY nom_arch, id_grupo DESC, tip_usu DESC, num_id, tip_id, cod_serv";
            } else {
                sql += "ORDER BY CD.nom_arch, T.num_id, T.tip_id, T.cod_serv";
            }
            
            return this.getListaOtrosServicios(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
}
