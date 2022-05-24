package com.osps.db;

import com.osps.entidad.Morbilidad;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para el manejo de las consultas de morbilidad
 * @author Feisar Moreno
 * @date 12/12/2012
 */
public class DbMorbilidad extends DbConexion {
    /**
     * Método privado que realiza una consulta sql y retorna una lista de registros.
     * @author Feisar Moreno
     * @date 12/12/2012
     * @param sql Consulta SQL a ejecutar
     * @return <code>ArrayList</code> con los registros que cumplen con la consulta SQL.
     * @throws SQLException
     */
    private ArrayList<Morbilidad> getListaMorbilidad(String sql) throws SQLException {
        try {
            crearConexion();
            pstm = conn.prepareStatement(sql);
            rst = pstm.executeQuery();
            
            ArrayList<Morbilidad> listaMorbilidad = new ArrayList<Morbilidad>();
            while (rst.next()) {
                Morbilidad morbilidadAux = new Morbilidad(rst);
                listaMorbilidad.add(morbilidadAux);
            }
            rst.close();
            
            return listaMorbilidad;
        } finally {
            cerrarConexion();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por causas
     * @author Feisar Moreno
     * @date 12/12/2012
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadCausas(String codDep) {
        try {
            String sql =
                "SELECT T.indzeta, T.gedad, T.tipocons, T.codagrcie, T.nombreagrcie, " +
                "SUM(CASE T.sexo WHEN 'M' THEN T.cantidad ELSE 0 END) AS cantidadm, " +
                "SUM(CASE T.sexo WHEN 'F' THEN T.cantidad ELSE 0 END) AS cantidadf, " +
                "SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.indzeta, CC.gedad, CC.coddep, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, " +
                    "CX.codagrcie, AC.nombreagrcie, CC.sexo, CC.tipusu, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "INNER JOIN agrupaciones_ciex AC ON CX.codagrcie=AC.codagrcie " +
                    "GROUP BY CC.indzeta, CC.gedad, CC.coddep, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, CX.codagrcie, AC.nombreagrcie, CC.sexo, CC.tipusu " +
                    "UNION ALL " +
                    "SELECT UC.indzeta, UC.gedad, UC.coddep, 3 AS tipocons, " +
                    "CX.codagrcie, AC.nombreagrcie, UC.sexo, UC.tipusu, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "INNER JOIN agrupaciones_ciex AC ON CX.codagrcie=AC.codagrcie " +
                    "GROUP BY UC.indzeta, UC.gedad, UC.coddep, CX.codagrcie, " +
                    "AC.nombreagrcie, UC.sexo, UC.tipusu " +
                    "UNION ALL " +
                    "SELECT HC.indzeta, HC.gedad, HC.coddep, 4 AS tipocons, " +
                    "CX.codagrcie, AC.nombreagrcie, HC.sexo, HC.tipusu, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "INNER JOIN agrupaciones_ciex AC ON CX.codagrcie=AC.codagrcie " +
                    "GROUP BY HC.indzeta, HC.gedad, HC.coddep, CX.codagrcie, " +
                    "AC.nombreagrcie, HC.sexo, HC.tipusu" +
                ") T " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad BETWEEN 0 AND 6 " +
                "AND T.tipusu IN ('1', '2', '3', '4', '5', '6', '7', '8') " +
                "GROUP BY T.indzeta, T.gedad, T.tipocons, T.codagrcie, T.nombreagrcie " +
                "ORDER BY T.indzeta, T.gedad, T.tipocons, cantidadt DESC, T.nombreagrcie";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por variables sociodemograficas
     * @author Feisar Moreno
     * @date 12/12/2012
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadDemogDist(String codDep) {
        try {
            String sql =
                "SELECT T.gedad, T.tipocons, T.sexo, M.codndp, " +
                "CASE WHEN T.zona IN ('U', 'R') THEN T.zona ELSE 'E' END AS zona, " +
                "CASE " +
                    "WHEN T.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                    "WHEN T.tipusu IN ('2', '7') THEN 2 " +
                    "WHEN T.tipusu IN ('3', '8') THEN 3 " +
                    "ELSE 0 " +
                "END AS tipousu, SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.gedad, CC.coddep, CC.codmun, CC.zona, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, CC.sexo, CC.tipusu, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "GROUP BY CC.gedad, CC.coddep, CC.codmun, CC.zona, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, CC.sexo, CC.tipusu " +
                    "UNION ALL " +
                    "SELECT UC.gedad, UC.coddep, UC.codmun, UC.zona, " +
                    "3 AS tipocons, UC.sexo, UC.tipusu, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "GROUP BY UC.gedad, UC.coddep, " +
                    "UC.codmun, UC.zona, UC.sexo, UC.tipusu " +
                    "UNION ALL " +
                    "SELECT HC.gedad, HC.coddep, HC.codmun, HC.zona, " +
                    "4 AS tipocons, HC.sexo, HC.tipusu, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "GROUP BY HC.gedad, HC.coddep, " +
                    "HC.codmun, HC.zona, HC.sexo, HC.tipusu" +
                ") T " +
                "LEFT JOIN municipios M ON T.coddep=M.coddep AND T.codmun=M.codmun " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad BETWEEN 0 AND 6 " +
                "AND T.tipusu IN ('1', '2', '3', '4', '5', '6', '7', '8') " +
                "GROUP BY T.gedad, T.tipocons, T.sexo, M.codndp, " +
                "CASE WHEN T.zona IN ('U', 'R') THEN T.zona ELSE 'E' END, " +
                "CASE " +
                    "WHEN T.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                    "WHEN T.tipusu IN ('2', '7') THEN 2 " +
                    "WHEN T.tipusu IN ('3', '8') THEN 3 " +
                    "ELSE 0 " +
                "END " +
                "ORDER BY T.gedad, T.tipocons, T.sexo, M.codndp, zona DESC";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por variables sociodemograficas
     * para los procedimientos
     * @author Feisar Moreno
     * @date 19/12/2012
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadDemogProc(String codDep) {
        try {
            String sql =
                "/*SELECT 'Error' AS tipousu, 0 AS cantidadt */SELECT CASE " +
                    "WHEN P.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                    "WHEN P.tipusu IN ('2', '7') THEN 2 " +
                    "WHEN P.tipusu IN ('3', '8') THEN 3 " +
                    "ELSE 0 " +
                "END AS tipousu, SUM(P.cantidad) AS cantidadt " +
                "FROM procedimientos_con P " +
                "INNER JOIN (SELECT codcups FROM cups GROUP BY codcups) C ON P.cups=C.codcups " +
                "WHERE P.coddep='" + codDep + "' " +
                "AND P.tipusu IN ('1', '2', '3', '4', '5', '6', '7', '8') " +
                "GROUP BY CASE " +
                    "WHEN P.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                    "WHEN P.tipusu IN ('2', '7') THEN 2 " +
                    "WHEN P.tipusu IN ('3', '8') THEN 3 " +
                    "ELSE 0 " +
                "END " +
                "ORDER BY 1";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los totales por servicio de morbilidad
     * por variables sociodemograficas sin dato de edad
     * @author Feisar Moreno
     * @date 12/12/2012
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadDemogSinEdad(String codDep) {
        try {
            String sql =
                "SELECT T.tipocons, SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.gedad, CC.coddep, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, " +
                    "CC.sexo, CC.tipusu, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "GROUP BY CC.gedad, CC.coddep, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, CC.sexo, CC.tipusu " +
                    "UNION ALL " +
                    "SELECT UC.gedad, UC.coddep, 3 AS tipocons, " +
                    "UC.sexo, UC.tipusu, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "GROUP BY UC.gedad, UC.coddep, UC.sexo, UC.tipusu " +
                    "UNION ALL " +
                    "SELECT HC.gedad, HC.coddep, 4 AS tipocons, " +
                    "HC.sexo, HC.tipusu, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "GROUP BY HC.gedad, HC.coddep, HC.sexo, HC.tipusu" +
                ") T " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad=7 " +
                "AND T.tipusu IN ('1', '2', '3', '4', '5', '6', '7', '8') " +
                "GROUP BY T.tipocons " +
                "ORDER BY T.tipocons";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por especialidad y agrcie
     * @author Feisar Moreno
     * @date 27/12/2012
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadEspAgrCieSexo(String codDep) {
        try {
            String sql =
                "SELECT T.indzeta, T.gedad, T.tipocons, T.tipousu, T.sexo, " +
                "EC.nombreesp, AC.nombreagrcie, SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, " +
                    "CASE " +
                        "WHEN CC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN CC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN CC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, CC.sexo, CX.codesp, CX.codagrcie, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "GROUP BY CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, " +
                    "CASE " +
                        "WHEN CC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN CC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN CC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, CC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT UC.coddep, UC.indzeta, UC.gedad, 3 AS tipocons, " +
                    "CASE " +
                        "WHEN UC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN UC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN UC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, UC.sexo, CX.codesp, CX.codagrcie, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "GROUP BY UC.coddep, UC.indzeta, UC.gedad, " +
                    "CASE " +
                        "WHEN UC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN UC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN UC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, UC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT HC.coddep, HC.indzeta, HC.gedad, 4 AS tipocons, " +
                    "CASE " +
                        "WHEN HC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN HC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN HC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, HC.sexo, CX.codesp, CX.codagrcie, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "GROUP BY HC.coddep, HC.indzeta, HC.gedad, " +
                    "CASE " +
                        "WHEN HC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN HC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN HC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, HC.sexo, CX.codesp, CX.codagrcie" +
                ") T " +
                "LEFT JOIN especialidades_ciex EC ON T.codesp=EC.codesp " +
                "LEFT JOIN agrupaciones_ciex AC ON T.codagrcie=AC.codagrcie " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad BETWEEN 0 AND 6 " +
                "AND T.tipousu BETWEEN 1 AND 3 " +
                "GROUP BY T.indzeta, T.gedad, T.tipocons, T.tipousu, T.sexo, EC.nombreesp, AC.nombreagrcie " +
                "ORDER BY T.indzeta, T.gedad, T.tipocons, T.tipousu, T.sexo, cantidadt DESC, AC.nombreagrcie";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por especialidad y agrcie
     * @author Feisar Moreno
     * @date 27/12/2012
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadEspAgrCie(String codDep) {
        try {
            String sql =
                "SELECT T.indzeta, T.gedad, T.tipocons, T.tipousu, " +
                "EC.nombreesp, AC.nombreagrcie, " +
                "SUM(CASE T.sexo WHEN 'F' THEN T.cantidad ELSE 0 END) AS cantidadf, " +
                "SUM(CASE T.sexo WHEN 'M' THEN T.cantidad ELSE 0 END) AS cantidadm, " +
                "SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, " +
                    "CASE " +
                        "WHEN CC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN CC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN CC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, CC.sexo, CX.codesp, CX.codagrcie, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "GROUP BY CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, " +
                    "CASE " +
                        "WHEN CC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN CC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN CC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, CC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT UC.coddep, UC.indzeta, UC.gedad, 3 AS tipocons, " +
                    "CASE " +
                        "WHEN UC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN UC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN UC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, UC.sexo, CX.codesp, CX.codagrcie, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "GROUP BY UC.coddep, UC.indzeta, UC.gedad, " +
                    "CASE " +
                        "WHEN UC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN UC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN UC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, UC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT HC.coddep, HC.indzeta, HC.gedad, 4 AS tipocons, " +
                    "CASE " +
                        "WHEN HC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN HC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN HC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, HC.sexo, CX.codesp, CX.codagrcie, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "GROUP BY HC.coddep, HC.indzeta, HC.gedad, " +
                    "CASE " +
                        "WHEN HC.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN HC.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN HC.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, HC.sexo, CX.codesp, CX.codagrcie" +
                ") T " +
                "LEFT JOIN especialidades_ciex EC ON T.codesp=EC.codesp " +
                "LEFT JOIN agrupaciones_ciex AC ON T.codagrcie=AC.codagrcie " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad BETWEEN 0 AND 6 " +
                "AND T.tipousu BETWEEN 1 AND 3 " +
                "GROUP BY T.indzeta, T.gedad, T.tipocons, T.tipousu, EC.nombreesp, AC.nombreagrcie " +
                "ORDER BY T.indzeta, T.gedad, T.tipocons, T.tipousu, cantidadt DESC, AC.nombreagrcie";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por ámbito, finalidad
     * y sección para los procedimientos
     * @author Feisar Moreno
     * @date 01/02/2013
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadProcAmbFinSec(String codDep) {
        try {
            String sql =
                "SELECT P.tipousu, A.coddetalle AS ambreapro, A.nomdetalle AS ambito, " +
                "F.coddetalle AS finpro, F.nomdetalle AS finalidad, G.codsec, SC.dessec, " +
                "G.codgru, G.desgru, S.codsubgru, S.dessubgru, SUM(P.cantidadt) AS cantidadt " +
                "FROM (" +
                    "SELECT CASE " +
                        "WHEN P.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN P.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN P.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END AS tipousu, P.ambreapro, P.finpro, C.codgru, C.codsubgru, " +
                    "SUM(P.cantidad) AS cantidadt " +
                    "FROM procedimientos_con P " +
                    "INNER JOIN (" +
                        "SELECT DISTINCT codcups, SUBSTRING(codcups FROM 1 FOR 2) AS codgru, " +
                        "SUBSTRING(codcups FROM 1 FOR 3) AS codsubgru " +
                        "FROM cups" +
                    ") C ON P.cups=C.codcups " +
                    "WHERE P.coddep = '" + codDep + "' " +
                    "AND P.tipusu in ('1', '2', '3', '4', '5', '6', '7', '8') " +
                    "GROUP BY CASE " +
                        "WHEN P.tipusu IN ('1', '4', '5', '6') THEN 1 " +
                        "WHEN P.tipusu IN ('2', '7') THEN 2 " +
                        "WHEN P.tipusu IN ('3', '8') THEN 3 " +
                        "ELSE 0 " +
                    "END, P.ambreapro, P.finpro, C.codgru, C.codsubgru" +
                ") P " +
                "LEFT JOIN listas_detalle A ON P.ambreapro=A.coddetalle AND A.idlista=4 " +
                "LEFT JOIN listas_detalle F ON P.finpro=F.coddetalle AND F.idlista=5 " +
                "LEFT JOIN cups_grupo G ON P.codgru=G.codgru " +
                "LEFT JOIN cups_secciones SC ON G.codsec=SC.codsec " +
                "LEFT JOIN cups_subgrupo S ON P.codsubgru=S.codsubgru " +
                "GROUP BY P.tipousu, A.coddetalle, A.nomdetalle, F.coddetalle, F.nomdetalle, G.codsec, " +
                "SC.dessec, G.codgru, G.desgru, S.codsubgru, S.dessubgru, A.orden, F.orden " +
                "ORDER BY P.tipousu, A.orden, F.orden, cantidadt DESC";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por especialidad y agrcie
     * para el perfil de morbilidad
     * @author Feisar Moreno
     * @date 11/02/2013
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadPerfilSexo(String codDep) {
        try {
            String sql =
                "SELECT T.indzeta, T.gedad, T.tipocons, T.sexo, EC.nombreesp, " +
                "AC.nombreagrcie, SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, CC.tipusu, CC.sexo, CX.codesp, CX.codagrcie, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "GROUP BY CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, CC.tipusu, CC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT UC.coddep, UC.indzeta, UC.gedad, 3 AS tipocons, UC.tipusu, " +
                    "UC.sexo, CX.codesp, CX.codagrcie, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "GROUP BY UC.coddep, UC.indzeta, UC.gedad, UC.tipusu, UC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT HC.coddep, HC.indzeta, HC.gedad, 4 AS tipocons, HC.tipusu, " +
                    "HC.sexo, CX.codesp, CX.codagrcie, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "GROUP BY HC.coddep, HC.indzeta, HC.gedad, HC.tipusu, HC.sexo, CX.codesp, CX.codagrcie" +
                ") T " +
                "LEFT JOIN especialidades_ciex EC ON T.codesp=EC.codesp " +
                "LEFT JOIN agrupaciones_ciex AC ON T.codagrcie=AC.codagrcie " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad BETWEEN 0 AND 6 " +
                "AND T.tipusu IN ('1', '2', '3', '4', '5', '6', '7', '8') " +
                "GROUP BY T.indzeta, T.gedad, T.tipocons, T.sexo, EC.nombreesp, AC.nombreagrcie " +
                "ORDER BY T.indzeta, T.gedad, T.tipocons, T.sexo, cantidadt DESC, AC.nombreagrcie";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
    
    /**
     * Método que retorna los datos de morbilidad por especialidad y agrcie
     * para el perfil de morbilidad
     * @author Feisar Moreno
     * @date 11/02/2013
     * @param codDep Código del departamento
     * @return <code>ArrayList</code> con los datos de morbilidad.
     */
    public ArrayList<Morbilidad> getListaMorbilidadPerfil(String codDep) {
        try {
            String sql =
                "SELECT T.indzeta, T.gedad, T.tipocons, EC.nombreesp, AC.nombreagrcie, " +
                "SUM(CASE T.sexo WHEN 'F' THEN T.cantidad ELSE 0 END) AS cantidadf, " +
                "SUM(CASE T.sexo WHEN 'M' THEN T.cantidad ELSE 0 END) AS cantidadm, " +
                "SUM(T.cantidad) AS cantidadt " +
                "FROM (" +
                    "SELECT CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END AS tipocons, CC.tipusu, CC.sexo, CX.codesp, CX.codagrcie, SUM(CC.cantidad) AS cantidad " +
                    "FROM consultas_con CC " +
                    "INNER JOIN ciex CX ON CC.coddia=CX.codciex " +
                    "GROUP BY CC.coddep, CC.indzeta, CC.gedad, " +
                    "CASE CC.tipocons " +
                        "WHEN 'Consulta Externa' THEN 1 " +
                        "WHEN 'Consulta de Urgencias' THEN 2 " +
                        "ELSE 0 " +
                    "END, CC.tipusu, CC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT UC.coddep, UC.indzeta, UC.gedad, 3 AS tipocons, UC.tipusu, " +
                    "UC.sexo, CX.codesp, CX.codagrcie, SUM(UC.cantidad) AS cantidad " +
                    "FROM urgencias_con UC " +
                    "INNER JOIN ciex CX ON UC.diasal=CX.codciex " +
                    "GROUP BY UC.coddep, UC.indzeta, UC.gedad, UC.tipusu, UC.sexo, CX.codesp, CX.codagrcie " +
                    "UNION ALL " +
                    "SELECT HC.coddep, HC.indzeta, HC.gedad, 4 AS tipocons, HC.tipusu, " +
                    "HC.sexo, CX.codesp, CX.codagrcie, SUM(HC.cantidad) AS cantidad " +
                    "FROM hospitalizacion_con HC " +
                    "INNER JOIN ciex CX ON HC.diaegr=CX.codciex " +
                    "GROUP BY HC.coddep, HC.indzeta, HC.gedad, HC.tipusu, HC.sexo, CX.codesp, CX.codagrcie" +
                ") T " +
                "LEFT JOIN especialidades_ciex EC ON T.codesp=EC.codesp " +
                "LEFT JOIN agrupaciones_ciex AC ON T.codagrcie=AC.codagrcie " +
                "WHERE T.coddep='" + codDep + "' " +
                "AND T.tipocons>0 " +
                "AND T.sexo IN ('M', 'F') " +
                "AND T.gedad BETWEEN 0 AND 6 " +
                "AND T.tipusu IN ('1', '2', '3', '4', '5', '6', '7', '8') " +
                "GROUP BY T.indzeta, T.gedad, T.tipocons, EC.nombreesp, AC.nombreagrcie " +
                "ORDER BY T.indzeta, T.gedad, T.tipocons, cantidadt DESC, AC.nombreagrcie";
            
            return this.getListaMorbilidad(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            return new ArrayList<Morbilidad>();
        }
    }
}
