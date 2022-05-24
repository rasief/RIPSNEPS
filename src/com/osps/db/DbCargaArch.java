package com.osps.db;

import com.osps.entidad.CargaArch;
import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Grupo;
import com.osps.entidad.IntervaloFecha;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Clase para el manejo de las tablas carga_arch y carga_arch_deta
 * @author Feisar Moreno
 * @date 23/02/2012
 */
public class DbCargaArch extends DbConexion {
    
    /**
     * Método que inserta un registro en la tabla carga_arch, si el registro ya
     * existe retorna el id
     * @author Feisar Moreno
     * @date 09/09/2019
     * @param rutaCarga Ruta de la carpeta de carga
     * @param codEntAdm Código de la entidad administradora
     * @param ano Año a cargar
     * @param mes Mes a cargar
     * @return Identificador del archivo de carga
     */
    public long insertarRegistro(String rutaCarga, String codEntAdm, int ano, int mes) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_crear_carga_arch(?,?,?,?,?)}";
            long resultado;
            try (CallableStatement cstmt = conn.prepareCall(procAlmacenado)) {
                cstmt.setString(1, rutaCarga);
                cstmt.setString(2, codEntAdm);
                cstmt.setInt(3, ano);
                cstmt.setInt(4, mes);
                cstmt.registerOutParameter(5, Types.BIGINT);
                cstmt.execute();
                resultado = cstmt.getLong(5);
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
     * Método que actualiza el número de registros cargados de un archivo de detalle de carga
     * @author Feisar Moreno
     * @date 12/09/2019
     * @param idCargaDeta Identificador del registro de detalle de carga
     * @param idCarga Identificador del registro de carga
     * @param nomArch Nombre del archivo
     * @param codTabla Código de la tabla
     * @param numRegistros Cantidad de registros cargados
     * @param idEstado Identificador del estado de carga
     * @return valor positivo si se realizó la actualización, -1 en caso de error
     */
    public long crearActualizarRegistroDeta(long idCargaDeta, long idCarga, String nomArch, String codTabla, int numRegistros, int idEstado) {
        return this.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, codTabla, numRegistros, idEstado, true);
    }
    
    /**
     * Método que actualiza el número de registros cargados de un archivo de detalle de carga
     * @author Feisar Moreno
     * @date 12/09/2019
     * @param idCargaDeta Identificador del registro de detalle de carga
     * @param idCarga Identificador del registro de carga
     * @param nomArch Nombre del archivo
     * @param codTabla Código de la tabla
     * @param numRegistros Cantidad de registros cargados
     * @param idEstado Identificador del estado de carga
     * @param indCommit Indicador de commit al procedimiento almacenado
     * @return valor positivo si se realizó la actualización, -1 en caso de error
     */
    public long crearActualizarRegistroDeta(long idCargaDeta, long idCarga, String nomArch, String codTabla, int numRegistros, int idEstado, boolean indCommit) {
        try {
            if (indCommit) {
                crearConexion();
            }
            String procAlmacenado = "{call pa_crear_actualizar_carga_arch_deta(?,?,?,?,?,?,?,?)}";
            long resultado;
            try (CallableStatement cstmt = conn.prepareCall(procAlmacenado)) {
                if (idCargaDeta > 0) {
                    cstmt.setLong(1, idCargaDeta);
                } else {
                    cstmt.setNull(1, Types.BIGINT);
                }
                cstmt.setLong(2, idCarga);
                cstmt.setString(3, nomArch);
                cstmt.setString(4, codTabla);
                if (numRegistros > 0) {
                    cstmt.setInt(5, numRegistros);
                } else {
                    cstmt.setNull(5, Types.INTEGER);
                }
                if (idEstado > 0) {
                    cstmt.setInt(6, idEstado);
                } else {
                    cstmt.setNull(6, Types.INTEGER);
                }
                cstmt.setInt(7, indCommit ? 1 : 0);
                cstmt.registerOutParameter(8, Types.BIGINT);
                cstmt.execute();
                resultado = cstmt.getLong(8);
            }
            
            return resultado;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return -1;
        } finally {
            if (indCommit) {
                cerrarConexion();
            }
        }
    }
    
    /**
     * Método que busca y marca los registros de hospitalización y servicios de estancia faltantes
     * @author Feisar Moreno
     * @date 15/10/2019
     * @param idCarga Identificador del registro de carga
     * @return valor positivo si se realizó la actualización, -1 en caso de error
     */
    public int marcarFaltantesAHAT(long idCarga) {
        try {
            crearConexion();
            
            String procAlmacenado = "{call pa_marcar_fantantes_ah_at(?,?)}";
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
     * Método que actualiza los códigos antiguos de diferentes tablas de RIPS
     * @author Feisar Moreno
     * @date 08/10/2019
     * @param idCarga Identificador del registro de carga
     * @return valor positivo si se realizó la actualización, -1 en caso de error
     */
    public int actualizarCodigosAntiguos(long idCarga) {
        try {
            crearConexion();
            
            String procAlmacenado = "{call pa_actualizar_codigos_antiguos(?,?)}";
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
     * Método que actualiza el tipo de internación de los archivos del RIPS de una carga
     * @author Feisar Moreno
     * @date 08/10/2019
     * @param idCarga Identificador del registro de carga
     * @return valor positivo si se realizó la actualización, -1 en caso de error
     */
    public int actualizarTipoInternacion(long idCarga) {
        try {
            crearConexion();
            
            String procAlmacenado = "{call pa_actualizar_tipo_internacion(?,?)}";
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
     * Método que borra un registro de carga
     * @author Feisar Moreno
     * @date 11/10/2019
     * @param idCarga Identificador de la carga
     * @return idCarga
     */
    public int borrarRegistro(long idCarga) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_borrar_carga_arch(?,?)}";
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
            return -2;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que borra un registro de detalle de carga
     * @author Feisar Moreno
     * @date 11/10/2019
     * @param idCargaDeta Identificador del detalle de carga
     * @return Código de error
     */
    public int borrarRegistroDeta(long idCargaDeta) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_borrar_carga_arch_deta(?,?)}";
            int resultado;
            try (CallableStatement cstmt = conn.prepareCall(procAlmacenado)) {
                cstmt.setLong(1, idCargaDeta);
                cstmt.registerOutParameter(2, Types.INTEGER);
                cstmt.execute();
                resultado = cstmt.getInt(2);
            }
            
            return resultado;
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            return -2;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que carga los afiliados a Nueva EPS
     * @author Feisar Moreno
     * @date 23/10/2019
     * @param idCarga Identificador de la carga
     * @return Valor positivo si se realizó la carga, -1 en caso de error
     */
    public int cargarAfiliadosNEPS(long idCarga) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_cargar_afiliados_neps(?,?)}";
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
     * Método que asocia los diferentes servicios con sus respectivos usuarios y grupos
     * @author Feisar Moreno
     * @date 23/10/2019
     * @param idCarga Identificador de la carga
     * @return Valor positivo si se realizó la asociación, -1 en caso de error
     */
    public int asociarUsuariosServicios(long idCarga) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_asociar_usuarios_servicios(?,?)}";
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
     * Método que identifica la agrupación a la que pertenece cada atención
     * @author Feisar Moreno
     * @date 02/12/2019
     * @param idCarga Identificador de la carga
     * @return Valor positivo si se realizó la asociación, -1 en caso de error
     */
    public int identificarAgrupaciones(long idCarga) {
        try {
            crearConexion();
            String procAlmacenado = "{call pa_identificar_agrupaciones(?,?)}";
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
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 12/09/2019
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<CargaArch> getListaCargaArch(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<CargaArch> listaCargaArch = new ArrayList<>();
            while (rst.next()) {
                CargaArch cargaAux = new CargaArch(rst);
                listaCargaArch.add(cargaAux);
            }
            rst.close();
            
            return listaCargaArch;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 24/02/2012
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<CargaArchDeta> getListaCargaArchDeta(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<CargaArchDeta> listaCargaArchDeta = new ArrayList<>();
            while (rst.next()) {
                CargaArchDeta cargaAux = new CargaArchDeta(rst);
                listaCargaArchDeta.add(cargaAux);
            }
            rst.close();
            
            return listaCargaArchDeta;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 02/12/2019
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Grupo> getListaGrupos(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Grupo> listaGrupos = new ArrayList<>();
            while (rst.next()) {
                Grupo grupoAux = new Grupo(rst);
                listaGrupos.add(grupoAux);
            }
            rst.close();
            
            return listaGrupos;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método privado que realiza una consulta sql y retorna un registro.
     * @author Feisar Moreno
     * @date 02/12/2019
     * @param sql Consulta SQL a ejecutar
     * @return objeto del tipo solicitado que cumple con la consulta SQL.
     * @throws SQLException
     */
    private IntervaloFecha getIntervaloFecha(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            IntervaloFecha intervaloFechaAux;
            if (rst.next()) {
                intervaloFechaAux = new IntervaloFecha(rst);
            } else {
                intervaloFechaAux = null;
            }
            rst.close();
            
            return intervaloFechaAux;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna un registro de carga.
     * @author Feisar Moreno
     * @date 12/09/2019
     * @param idCarga Identificador de la carga
     * @return Objeto <code>CargaArch</code> con la información de la carga,
     * <code>null</code> en caso de no encontrar registros.
     */
    public CargaArch getCargaArch(long idCarga) {
        try {
            String sql =
                "SELECT * FROM carga_arch " +
                "WHERE id_carga=" + idCarga;
            
            ArrayList<CargaArch> listaCargaArch = this.getListaCargaArch(sql);
            
            if (listaCargaArch.size() > 0) {
                return listaCargaArch.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    /**
     * Método que retorna un registro de carga.
     * @author Feisar Moreno
     * @date 12/09/2019
     * @param codEntAdm Código de la entidad administradora
     * @param anio Año de carga
     * @param mes Mes de carga
     * @return Objeto <code>CargaArch</code> con la información de la carga,
     * <code>null</code> en caso de no encontrar registros.
     */
    public CargaArch getCargaArch(String codEntAdm, int anio, int mes) {
        try {
            String sql =
                    "SELECT * FROM carga_arch " +
                    "WHERE cod_ent_adm='" + codEntAdm + "' " +
                    "AND anio=" + anio + " " +
                    "AND mes=" + mes;
            
            ArrayList<CargaArch> listaCargaArch = this.getListaCargaArch(sql);
            
            if (listaCargaArch.size() > 0) {
                return listaCargaArch.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    /**
     * Método que retorna un registro de detalle de carga.
     * @author Feisar Moreno
     * @date 12/09/2019
     * @param idCargaDeta Identificador del detalle de la carga
     * @return Objeto <code>CargaArchDeta</code> con la información del detalle de carga,
     * <code>null</code> en caso de no encontrar registros.
     */
    public CargaArchDeta getCargaArchDeta(long idCargaDeta) {
        try {
            String sql =
                "SELECT * FROM carga_arch_deta " +
                "WHERE id_carga_deta=" + idCargaDeta;
            
            ArrayList<CargaArchDeta> listaCargaArchDeta = this.getListaCargaArchDeta(sql);
            
            if (listaCargaArchDeta.size() > 0) {
                return listaCargaArchDeta.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    /**
     * Método que retorna un registro de detalle de carga.
     * @author Feisar Moreno
     * @date 24/02/2012
     * @param idCarga Identificador de la carga
     * @param nomArch Nombre del archivo cargado
     * @param codTabla Código de la tabla
     * @return Objeto <code>CargaArchDeta</code> con la información del detalle de carga,
     * <code>null</code> en caso de no encontrar registros.
     */
    public CargaArchDeta getCargaArchDeta(long idCarga, String nomArch, String codTabla) {
        try {
            nomArch = nomArch.replace("\\", "/");
            String sql =
                    "SELECT * FROM carga_arch_deta " +
                    "WHERE id_carga=" + idCarga + " " +
                    "AND nom_arch='" + nomArch + "' " +
                    "AND cod_tabla='" + codTabla + "'";
            
            ArrayList<CargaArchDeta> listaCargaArchDeta = this.getListaCargaArchDeta(sql);
            
            if (listaCargaArchDeta.size() > 0) {
                return listaCargaArchDeta.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    /**
     * Método que retorna los registros de detalle de una carga dada.
     * @author Feisar Moreno
     * @date 29/02/2012
     * @param idCarga Identificador de la carga
     * @return <code>ArrayList</code> con la información del detalle de una carga.
     */
    public ArrayList<CargaArchDeta> getListaCargaArchDeta(long idCarga) {
        try {
            String sql =
                "SELECT CD.*, EC.nom_estado " +
                "FROM carga_arch_deta CD " +
                "INNER JOIN estados_carga EC ON CD.id_estado=EC.id_estado " +
                "WHERE CD.id_carga=" + idCarga + " " +
                "ORDER BY CASE WHEN CD.id_estado BETWEEN 5 AND 8 THEN 1 ELSE 2 END, CD.nom_arch, CD.cod_tabla";
            
            return this.getListaCargaArchDeta(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna el consolidado de grupos asociado a una carga dada.
     * @author Feisar Moreno
     * @date 02/12/2019
     * @param idCarga Identificador de la carga
     * @param fechaIni Fecha inicial del periodo
     * @param fechaFin Fecha final del periodo
     * @return <code>ArrayList</code> con la información del detalle de una carga.
     */
    public ArrayList<Grupo> getListaGrupos(long idCarga, String fechaIni, String fechaFin) {
        try {
            String sql =
                    "SELECT IFNULL(id_grupo, 99) AS id_grupo, grupo, SUM(cantidad) AS cantidad " +
                    "FROM (" +
                        "SELECT T.id_grupo, MP.grupo, COUNT(*) AS cantidad " +
                        "FROM consultas T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN maestro_procedimientos MP ON T.cod_con=MP.cod_procedimiento " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.ind_ppal_agrup=1 " +
                        "AND T.fecha_con BETWEEN '" + fechaIni + "' AND '" + fechaFin + "' " +
                        "AND MP.grupo IS NOT NULL " +
                        "GROUP BY T.id_grupo, MP.grupo " +
                        "UNION ALL " +
                        "SELECT T.id_grupo, MP.grupo, COUNT(*) AS cantidad " +
                        "FROM procedimientos T " +
                        "INNER JOIN carga_arch_deta CD ON T.id_carga_deta=CD.id_carga_deta " +
                        "INNER JOIN maestro_procedimientos MP ON T.cod_pro=MP.cod_procedimiento " +
                        "WHERE CD.id_carga=" + idCarga + " " +
                        "AND T.ind_ppal_agrup=1 " +
                        "AND T.fecha_pro BETWEEN '" + fechaIni + "' AND '" + fechaFin + "' " +
                        "AND MP.grupo IS NOT NULL " +
                        "GROUP BY T.id_grupo, MP.grupo" +
                    ") T " +
                    "GROUP BY id_grupo, grupo " +
                    "ORDER BY 1, grupo";
            
            return this.getListaGrupos(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<>();
        }
    }
    
    /**
     * Método que retorna la fecha inicial y final correspondientes a un periodo.
     * @author Feisar Moreno
     * @date 02/12/2019
     * @param ano Año de carga
     * @param mes Mes de carga
     * @return <code>ArrayList</code> con la información de las fechas.
     */
    public IntervaloFecha getIntervaloFecha(int ano, int mes) {
        try {
            String sql =
                    "SELECT fecha_ini, fecha_fin, DATE_FORMAT(fecha_ini, '%d/%m/%Y') AS fecha_ini_t, DATE_FORMAT(fecha_fin, '%d/%m/%Y') AS fecha_fin_t " +
                    "FROM (" +
                        "SELECT STR_TO_DATE('01/" + mes + "/" + ano + "', '%d/%m/%Y') AS fecha_ini, " +
                        "DATE_SUB(DATE_ADD(STR_TO_DATE('01/" + mes + "/" + ano + "', '%d/%m/%Y'), INTERVAL 1 MONTH), INTERVAL 1 DAY) AS fecha_fin" +
                    ") T";
            
            return this.getIntervaloFecha(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
}
