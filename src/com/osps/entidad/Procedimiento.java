package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * Clase que representa un registro de la tabla procedimientos
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Procedimiento extends EntidadRIPS {
    private long idProcedimiento;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipId;
    private String numId;
    private String fechaPro;
    private String numAut;
    private String codPro;
    private String ambReaPro;
    private String finPro;
    private String perAti;
    private String diaPri;
    private String diaRel;
    private String diaCom;
    private String forRea;
    private double valPro;
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
    
    public Procedimiento(long idProcedimiento, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipId, String numId,
            String fechaPro, String numAut, String codPro, String ambReaPro, String finPro, String perAti, String diaPri, String diaRel, String diaCom,
            String forRea, double valPro, String codCUPS, long idOtroServ, String observaciones) {
        this.idProcedimiento = idProcedimiento;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.fechaPro = fechaPro;
        this.numAut = numAut;
        this.codPro = codPro;
        this.ambReaPro = ambReaPro;
        this.finPro = finPro;
        this.perAti = perAti;
        this.diaPri = diaPri;
        this.diaRel = diaRel;
        this.diaCom = diaCom;
        this.forRea = forRea;
        this.valPro = valPro;
        this.codCUPS = codCUPS;
        this.idOtroServ = idOtroServ;
        this.observaciones = observaciones;
        this.indValores = true;
    }
    
    public Procedimiento(ResultSet rs) {
        try {
            idProcedimiento = rs.getLong("ID_PROCEDIMIENTO");
        } catch (SQLException e) {
            idProcedimiento = 0L;
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
            fechaPro = rs.getString("FECHA_PRO_T");
        } catch (SQLException e) {
            fechaPro = "";
        }
        try {
            numAut = rs.getString("NUM_AUT");
        } catch (SQLException e) {
            numAut = "";
        }
        try {
            codPro = rs.getString("COD_PRO");
        } catch (SQLException e) {
            codPro = "";
        }
        try {
            ambReaPro = rs.getString("AMB_REA_PRO");
        } catch (SQLException e) {
            ambReaPro = "";
        }
        try {
            finPro = rs.getString("FIN_PRO");
        } catch (SQLException e) {
            finPro = "";
        }
        try {
            perAti = rs.getString("PER_ATI");
        } catch (SQLException e) {
            perAti = "";
        }
        try {
            diaPri = rs.getString("DIA_PRI");
        } catch (SQLException e) {
            diaPri = "";
        }
        try {
            diaRel = rs.getString("DIA_REL");
        } catch (SQLException e) {
            diaRel = "";
        }
        try {
            diaCom = rs.getString("DIA_COM");
        } catch (SQLException e) {
            diaCom = "";
        }
        try {
            forRea = rs.getString("FOR_REA");
        } catch (SQLException e) {
            forRea = "";
        }
        try {
            valPro = rs.getDouble("VAL_PRO");
        } catch (SQLException e) {
            valPro = 0.0;
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
    
    public Procedimiento(Iterator<Cell> celdaIt, HashMap<Integer, String> mapaCols) {
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
                    if (nomCampo.equals("fecha_pro")) {
                        valCampo = Procedimiento.getFechaExcelString((long)valAux, "dd/MM/yyyy");
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
                    case "fecha_pro":
                        fechaPro = valCampo;
                        break;
                    case "num_aut":
                        numAut = valCampo;
                        break;
                    case "cod_pro":
                        valCampo = "000000" + valCampo;
                        codPro = valCampo.substring(valCampo.length() - 6);
                        break;
                    case "amb_rea_pro":
                        ambReaPro = valCampo;
                        break;
                    case "fin_pro":
                        finPro = valCampo;
                        break;
                    case "per_ati":
                        perAti = valCampo;
                        break;
                    case "dia_pri":
                        diaPri = valCampo;
                        break;
                    case "dia_rel":
                        diaRel = valCampo;
                        break;
                    case "dia_com":
                        diaCom = valCampo;
                        break;
                    case "for_rea":
                        forRea = valCampo;
                        break;
                    case "val_pro":
                        try {
                            valPro = Double.parseDouble(valCampo);
                        } catch (NumberFormatException ex) {
                            valPro = Double.NaN;
                        }
                        break;
                }
            }
        }
    }
    
    public long getIdProcedimiento() {
        return idProcedimiento;
    }
    
    public void setIdProcedimiento(long idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
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
    
    public String getFechaPro() {
        return fechaPro;
    }
    
    public void setFechaPro(String fechaPro) {
        this.fechaPro = fechaPro;
    }
    
    public String getNumAut() {
        return numAut;
    }
    
    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }
    
    public String getCodPro() {
        return codPro;
    }
    
    public void setCodPro(String codPro) {
        this.codPro = codPro;
    }
    
    public String getAmbReaPro() {
        return ambReaPro;
    }
    
    public void setAmbReaPro(String ambReaPro) {
        this.ambReaPro = ambReaPro;
    }
    
    public String getFinPro() {
        return finPro;
    }
    
    public void setFinPro(String finPro) {
        this.finPro = finPro;
    }
    
    public String getPerAti() {
        return perAti;
    }
    
    public void setPerAti(String perAti) {
        this.perAti = perAti;
    }
    
    public String getDiaPri() {
        return diaPri;
    }
    
    public void setDiaPri(String diaPri) {
        this.diaPri = diaPri;
    }
    
    public String getDiaRel() {
        return diaRel;
    }
    
    public void setDiaRel(String diaRel) {
        this.diaRel = diaRel;
    }
    
    public String getDiaCom() {
        return diaCom;
    }
    
    public void setDiaCom(String diaCom) {
        this.diaCom = diaCom;
    }
    
    public String getForRea() {
        return forRea;
    }
    
    public void setForRea(String forRea) {
        this.forRea = forRea;
    }
    
    public double getValPro() {
        return valPro;
    }
    
    public void setValPro(double valPro) {
        this.valPro = valPro;
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
