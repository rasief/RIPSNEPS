package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * Clase que representa un registro de la tabla consultas
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Consulta extends EntidadRIPS {
    private long idConsulta;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipId;
    private String numId;
    private String fechaCon;
    private String numAut;
    private String codCon;
    private String finCon;
    private String cauExt;
    private String codDia;
    private String diaRel1;
    private String diaRel2;
    private String diaRel3;
    private String tipDia;
    private double valCon;
    private double valCuo;
    private double valNet;
    private String codCUPS;
    private long idOtroServ;
    private int idTipoInter;
    private String numFacOri;
    private String observaciones;
    private int idGrupo;
    private String nomArch;
    private String nombreTipoInter;
    private String observacionesUS;
    private String nombreSede;
    private boolean indValores;
    
    public Consulta(long idConsulta, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipId, String numId,
            String fechaCon, String numAut, String codCon, String finCon, String cauExt, String codDia, String diaRel1, String diaRel2, String diaRel3,
                String tipDia, double valCon, double valCuo, double valNet, String codCUPS, String observaciones) {
        this.idConsulta = idConsulta;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.fechaCon = fechaCon;
        this.numAut = numAut;
        this.codCon = codCon;
        this.finCon = finCon;
        this.cauExt = cauExt;
        this.codDia = codDia;
        this.diaRel1 = diaRel1;
        this.diaRel2 = diaRel2;
        this.diaRel3 = diaRel3;
        this.tipDia = tipDia;
        this.valCon = valCon;
        this.valCuo = valCuo;
        this.valNet = valNet;
        this.codCUPS = codCUPS;
        this.observaciones = observaciones;
        this.indValores = true;
    }
    
    public Consulta(ResultSet rs) {
        try {
            idConsulta = rs.getLong("ID_CONSULTA");
        } catch (SQLException e) {
            idConsulta = 0L;
        }
        try {
            idCargaDeta = rs.getLong("ID_CARGA_DETA");
        } catch (SQLException e) {
            idCargaDeta = 0L;
        }
        try {
            numRegistro = rs.getInt("NUM_REGISTRO");
        } catch (SQLException e) {
            numRegistro = 0;
        }
        try {
            idUsuario = rs.getLong("ID_USUARIO");
        } catch (SQLException e) {
            idUsuario = 0L;
        }
        try {
            numFac = rs.getString("NUM_FAC");
        } catch (SQLException e) {
            numFac = "";
        }
        try {
            codEntPre = rs.getString("COD_ENT_PRE");
        } catch (SQLException e) {
            codEntPre = "";
        }
        try {
            tipId = rs.getString("TIP_ID");
        } catch (SQLException e) {
            tipId = "";
        }
        try {
            numId = rs.getString("NUM_ID");
        } catch (SQLException e) {
            numId = "";
        }
        try {
            fechaCon = rs.getString("FECHA_CON_T");
        } catch (SQLException e) {
            fechaCon = "";
        }
        try {
            numAut = rs.getString("NUM_AUT");
        } catch (SQLException e) {
            numAut = "";
        }
        try {
            codCon = rs.getString("COD_CON");
        } catch (SQLException e) {
            codCon = "";
        }
        try {
            finCon = rs.getString("FIN_CON");
        } catch (SQLException e) {
            finCon = "";
        }
        try {
            cauExt = rs.getString("CAU_EXT");
        } catch (SQLException e) {
            cauExt = "";
        }
        try {
            codDia = rs.getString("COD_DIA");
        } catch (SQLException e) {
            codDia = "";
        }
        try {
            diaRel1 = rs.getString("DIAL_REL_1");
        } catch (SQLException e) {
            diaRel1 = "";
        }
        try {
            diaRel2 = rs.getString("DIAL_REL_2");
        } catch (SQLException e) {
            diaRel2 = "";
        }
        try {
            diaRel3 = rs.getString("DIAL_REL_3");
        } catch (SQLException e) {
            diaRel3 = "";
        }
        try {
            tipDia = rs.getString("TIP_DIA");
        } catch (SQLException e) {
            tipDia = "";
        }
        try {
            valCon = rs.getDouble("VAL_CON");
        } catch (SQLException e) {
            valCon = 0.0;
        }
        try {
            valCuo = rs.getDouble("VAL_CUO");
        } catch (SQLException e) {
            valCuo = 0.0;
        }
        try {
            valNet = rs.getDouble("VAL_NET");
        } catch (SQLException e) {
            valNet = 0.0;
        }
        try {
            codCUPS = rs.getString("COD_CUPS");
        } catch (SQLException e) {
            codCUPS = "";
        }
        try {
            idOtroServ = rs.getLong("ID_OTRO_SERV");
        } catch (SQLException e) {
            idOtroServ = 0L;
        }
        try {
            idTipoInter = rs.getInt("ID_TIPO_INTER");
        } catch (SQLException e) {
            idTipoInter = 0;
        }
        try {
            numFacOri = rs.getString("NUM_FAC_ORI");
        } catch (SQLException e) {
            numFacOri = "";
        }
        try {
            observaciones = rs.getString("OBSERVACIONES");
        } catch (SQLException e) {
            observaciones = "";
        }
        try {
            idGrupo = rs.getInt("ID_GRUPO");
        } catch (SQLException e) {
            idGrupo = 0;
        }
        try {
            nomArch = rs.getString("NOM_ARCH");
        } catch (SQLException e) {
            nomArch = "";
        }
        try {
            nombreTipoInter = rs.getString("NOMBRE_TIPO_INTER");
        } catch (SQLException e) {
            nombreTipoInter = "";
        }
        try {
            observacionesUS = rs.getString("OBSERVACIONES_US");
        } catch (SQLException e) {
            observacionesUS = "";
        }
        try {
            nombreSede = rs.getString("NOMBRE_SEDE");
        } catch (SQLException e) {
            nombreSede = "";
        }
        this.indValores = true;
    }
    
    public Consulta(Iterator<Cell> celdaIt, HashMap<Integer, String> mapaCols) {
        indValores = false;
        while (celdaIt.hasNext()) {
            Cell celda = celdaIt.next();
            String nomCampo = mapaCols.get(celda.getColumnIndex());
            if (nomCampo != null) {
                String valCampo = "";
                if (celda.getCellTypeEnum() == CellType.STRING) {
                    valCampo = celda.getStringCellValue().trim();
                } else if (celda.getCellTypeEnum() == CellType.NUMERIC) {
                    double valAux = celda.getNumericCellValue();
                    if (nomCampo.equals("fecha_con")) {
                        valCampo = Consulta.getFechaExcelString((long)valAux, "dd/MM/yyyy");
                    } else {
                        if (valAux == Math.round(valAux)) {
                            valCampo = ((long)valAux) + "";
                        } else {
                            valCampo = valAux + "";
                        }
                    }
                }
                
                if (!valCampo.equals("")) {
                    indValores = true;
                }
                
                switch (nomCampo) {
                    case "num_fac":
                        numFac = valCampo;
                        break;
                    case "cod_ent_pre":
                        codEntPre = valCampo;
                        break;
                    case "tip_id":
                        tipId = valCampo;
                        break;
                    case "num_id":
                        numId = valCampo;
                        break;
                    case "fecha_con":
                        fechaCon = valCampo;
                        break;
                    case "num_aut":
                        numAut = valCampo;
                        break;
                    case "cod_con":
                        valCampo = "000000" + valCampo;
                        codCon = valCampo.substring(valCampo.length() - 6);
                        break;
                    case "fin_con":
                        valCampo = "00" + valCampo;
                        finCon = valCampo.substring(valCampo.length() - 2);
                        break;
                    case "cau_ext":
                        valCampo = "00" + valCampo;
                        cauExt = valCampo.substring(valCampo.length() - 2);
                        break;
                    case "cod_dia":
                        codDia = valCampo;
                        break;
                    case "dia_rel1":
                        diaRel1 = valCampo;
                        break;
                    case "dia_rel2":
                        diaRel2 = valCampo;
                        break;
                    case "dia_rel3":
                        diaRel3 = valCampo;
                        break;
                    case "tip_dia":
                        tipDia = valCampo;
                        break;
                    case "val_con":
                        try {
                            valCon = Double.parseDouble(valCampo);
                        } catch (NumberFormatException ex) {
                            valCon = Double.NaN;
                        }
                        break;
                    case "val_cuo":
                        try {
                            valCuo = Double.parseDouble(valCampo);
                        } catch (NumberFormatException ex) {
                            valCuo = Double.NaN;
                        }
                        break;
                    case "val_net":
                        try {
                            valNet = Double.parseDouble(valCampo);
                        } catch (NumberFormatException ex) {
                            valNet = Double.NaN;
                        }
                        break;
                }
            }
        }
    }
    
    public long getIdConsulta() {
        return idConsulta;
    }
    
    public void setIdConsulta(long idConsulta) {
        this.idConsulta = idConsulta;
    }
    
    public long getIdCargaDeta() {
        return idCargaDeta;
    }
    
    public void setIdCargaDeta(long idCargaDeta) {
        this.idCargaDeta = idCargaDeta;
    }
    
    public int getNumRegistro() {
        return numRegistro;
    }
    
    public void setNumRegistro(int numRegistro) {
        this.numRegistro = numRegistro;
    }
    
    public long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNumFac() {
        return numFac;
    }
    
    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }
    
    public String getCodEntPre() {
        return codEntPre;
    }
    
    public void setCodEntPre(String codEntPre) {
        this.codEntPre = codEntPre;
    }
    
    public String getTipId() {
        return tipId;
    }
    
    public void setTipId(String tipId) {
        this.tipId = tipId;
    }
    
    public String getNumId() {
        return numId;
    }
    
    public void setNumId(String numId) {
        this.numId = numId;
    }
    
    public String getFechaCon() {
        return fechaCon;
    }
    
    public void setFechaCon(String fechaCon) {
        this.fechaCon = fechaCon;
    }
    
    public String getNumAut() {
        return numAut;
    }
    
    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }
    
    public String getCodCon() {
        return codCon;
    }
    
    public void setCodCon(String codCon) {
        this.codCon = codCon;
    }
    
    public String getFinCon() {
        return finCon;
    }
    
    public void setFinCon(String finCon) {
        this.finCon = finCon;
    }
    
    public String getCauExt() {
        return cauExt;
    }
    
    public void setCauExt(String cauExt) {
        this.cauExt = cauExt;
    }
    
    public String getCodDia() {
        return codDia;
    }
    
    public void setCodDia(String codDia) {
        this.codDia = codDia;
    }
    
    public String getDiaRel1() {
        return diaRel1;
    }
    
    public void setDiaRel1(String diaRel1) {
        this.diaRel1 = diaRel1;
    }
    
    public String getDiaRel2() {
        return diaRel2;
    }
    
    public void setDiaRel2(String diaRel2) {
        this.diaRel2 = diaRel2;
    }
    
    public String getDiaRel3() {
        return diaRel3;
    }
    
    public void setDiaRel3(String diaRel3) {
        this.diaRel3 = diaRel3;
    }
    
    public String getTipDia() {
        return tipDia;
    }
    
    public void setTipDia(String tipDia) {
        this.tipDia = tipDia;
    }
    
    public double getValCon() {
        return valCon;
    }
    
    public void setValCon(double valCon) {
        this.valCon = valCon;
    }
    
    public double getValCuo() {
        return valCuo;
    }
    
    public void setValCuo(double valCuo) {
        this.valCuo = valCuo;
    }
    
    public double getValNet() {
        return valNet;
    }
    
    public void setValNet(double valNet) {
        this.valNet = valNet;
    }
    
    public String getCodCUPS() {
        return codCUPS;
    }
    
    public void setCodCUPS(String codCUPS) {
        this.codCUPS = codCUPS;
    }
    
    public long getIdOtroServ() {
        return idOtroServ;
    }
    
    public void setIdOtroServ(long idOtroServ) {
        this.idOtroServ = idOtroServ;
    }
    
    public int getIdTipoInter() {
        return idTipoInter;
    }
    
    public void setIdTipoInter(int idTipoInter) {
        this.idTipoInter = idTipoInter;
    }
    
    public String getNumFacOri() {
        return numFacOri;
    }
    
    public void setNumFacOri(String numFacOri) {
        this.numFacOri = numFacOri;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public int getIdGrupo() {
        return idGrupo;
    }
    
    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }
    
    public String getNomArch() {
        return nomArch;
    }
    
    public void setNomArch(String nomArch) {
        this.nomArch = nomArch;
    }
    
    public String getNombreTipoInter() {
        return nombreTipoInter;
    }
    
    public void setNombreTipoInter(String nombreTipoInter) {
        this.nombreTipoInter = nombreTipoInter;
    }
    
    public String getObservacionesUS() {
        return observacionesUS;
    }
    
    public void setObservacionesUS(String observacionesUS) {
        this.observacionesUS = observacionesUS;
    }
    
    public String getNombreSede() {
        return nombreSede;
    }
    
    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }
    
    public boolean isIndValores() {
        return indValores;
    }
    
    public void setIndValores(boolean indValores) {
        this.indValores = indValores;
    }
    
}
