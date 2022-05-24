package com.osps.db;

import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Factura;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de la tabla transaccion
 * @author Feisar Moreno
 * @date 28/02/2012
 */
public class DbFacturas extends DbCargaArch {
    
    /**
     * Método que inserta por lotes un listado de facturas
     * @author Feisar Moreno
     * @date 18/06/2018
     * @param listaFacturas Listado de facturas
     * @param cargaArchDeta Objeto de detalle de carga
     * @return 1 si se realizó la creación de registros, de lo contrario -1
     */
    public int insertarRegistros(ArrayList<Factura> listaFacturas, CargaArchDeta cargaArchDeta) {
        int realizado = 1;
        
        //Se obtienen los datos de la carga actual
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long idCarga = cargaArchDeta.getIdCarga();
        String nomArch = cargaArchDeta.getNomArch();
        int contTotal = cargaArchDeta.getNumRegistros();
        int contAct = 0;
        
        try {
            crearConexion(false);
            
            String sqlBase = "INSERT INTO facturas " +
                    "(id_carga_deta, num_registro, cod_ent_pre, nom_ent_pre, tip_id, num_id, num_fac, fecha_exp, fecha_ini, " +
                    "fecha_fin, cod_ent_adm, nom_ent_adm, num_con, pla_ben, num_pol, val_cop, val_com, val_des, val_net, ind_borrado) VALUES ";
            String sql = "";
            for (Factura facturaAux : listaFacturas) {
                if (contAct > 0 && contAct % 200 == 0) {
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                        pstmt.execute();
                    }
                    
                    //Se actualiza el registro de detalle de carga
                    idCargaDeta = this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AF", contTotal, 0, false);
                    
                    this.commit();
                    
                    sql = "";
                }
                
                if (!sql.equals("")) {
                    sql += ", ";
                }
                sql += "(" + idCargaDeta + ", " + (contTotal + 1) + ", '" + facturaAux.getCodEntPre() + "', '" + facturaAux.getNomEntPre() + "', '" +
                        facturaAux.getTipId() + "', '" + facturaAux.getNumId() + "', '" + facturaAux.getNumFac() + "', " +
                        "STR_TO_DATE('" + facturaAux.getFechaExp() + "', '%d/%m/%Y'), " + "STR_TO_DATE('" + facturaAux.getFechaIni() + "', '%d/%m/%Y'), " +
                        "STR_TO_DATE('" + facturaAux.getFechaFin() + "', '%d/%m/%Y'), '" + facturaAux.getCodEntAdm() + "', '" +
                        facturaAux.getNomEntAdm() + "', " + obtenerValorSQL(facturaAux.getNumCon()) + ", " + obtenerValorSQL(facturaAux.getPlaBen()) + ", " +
                        obtenerValorSQL(facturaAux.getNumPol()) + ", " + obtenerValorSQL(facturaAux.getValCop()) + ", " +
                        obtenerValorSQL(facturaAux.getValCom()) + ", " + obtenerValorSQL(facturaAux.getValDes()) + ", " +
                        obtenerValorSQL(facturaAux.getValNet()) + ", 0)";
                
                contTotal++;
                contAct++;
            }
            
            if (!sql.equals("")) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlBase + sql)) {
                    pstmt.execute();
                }
                
                //Se actualiza el registro de detalle de carga
                this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AF", contTotal, 0, false);
                
                this.commit();
            }
        } catch (SQLException e) {
            this.rollback();
            DbErrores dbErrores = new DbErrores();
            dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, contAct, "AF", 0, e.getMessage());
            return -1;
        } finally {
            cerrarConexion();
        }
        
        return realizado;
    }
    
    /**
     * Método que realiza la validación de los registros de transacciones
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
                String procAlmacenado = "{? = call fu_validar_transacciones(?,?,?)}";
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
}
