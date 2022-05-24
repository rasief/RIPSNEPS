package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla medicamentos
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Medicamento extends EntidadRIPS {
    private long idMedicamento;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipId;
    private String numId;
    private String numAut;
    private String codMed;
    private String tipMed;
    private String nomMed;
    private String forFar;
    private String conMed;
    private String uniMed;
    private int numUni;
    private double valUniMed;
    private double valTotMed;
    private int idTipoInter;
    private String numFacOri;
    private String observaciones;
    private int idGrupo;
    private String nomArch;
    private String nombreTipoInter;
    private String observacionesUS;
    private String nombreSede;
    
    public Medicamento(long idMedicamento, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipId, String numId,
            String numAut, String codMed, String tipMed, String nomMed, String forFar, String conMed, String uniMed, int numUni, double valUniMed,
            double valTotMed, String observaciones) {
        this.idMedicamento = idMedicamento;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.numAut = numAut;
        this.codMed = codMed;
        this.tipMed = tipMed;
        this.nomMed = nomMed;
        this.forFar = forFar;
        this.conMed = conMed;
        this.uniMed = uniMed;
        this.numUni = numUni;
        this.valUniMed = valUniMed;
        this.valTotMed = valTotMed;
        this.observaciones = observaciones;
    }
    
    public Medicamento(ResultSet rs) {
        try {
            idMedicamento = rs.getLong("ID_MEDICAMENTO");
        } catch (SQLException e) {
            idMedicamento = 0L;
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
            numAut = rs.getString("NUM_AUT");
        } catch (SQLException e) {
            numAut = "";
        }
        try {
            codMed = rs.getString("COD_MED");
        } catch (SQLException e) {
            codMed = "";
        }
        try {
            tipMed = rs.getString("TIP_MED");
        } catch (SQLException e) {
            tipMed = "";
        }
        try {
            nomMed = rs.getString("NOM_MED");
        } catch (SQLException e) {
            nomMed = "";
        }
        try {
            forFar = rs.getString("FOR_FAR");
        } catch (SQLException e) {
            forFar = "";
        }
        try {
            conMed = rs.getString("CON_MED");
        } catch (SQLException e) {
            conMed = "";
        }
        try {
            uniMed = rs.getString("UNI_MED");
        } catch (SQLException e) {
            uniMed = "";
        }
        try {
            numUni = rs.getInt("NUM_UNI");
        } catch (SQLException e) {
            numUni = 0;
        }
        try {
            valUniMed = rs.getDouble("VAL_UNI_MED");
        } catch (SQLException e) {
            valUniMed = 0.0;
        }
        try {
            valTotMed = rs.getDouble("VAL_TOT_MED");
        } catch (SQLException e) {
            valTotMed = 0.0;
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
    }
    
    public long getIdMedicamento() {
        return idMedicamento;
    }
    
    public void setIdMedicamento(long idMedicamento) {
        this.idMedicamento = idMedicamento;
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
    
    public String getNumAut() {
        return numAut;
    }
    
    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }
    
    public String getCodMed() {
        return codMed;
    }
    
    public void setCodMed(String codMed) {
        this.codMed = codMed;
    }
    
    public String getTipMed() {
        return tipMed;
    }
    
    public void setTipMed(String tipMed) {
        this.tipMed = tipMed;
    }
    
    public String getNomMed() {
        return nomMed;
    }
    
    public void setNomMed(String nomMed) {
        this.nomMed = nomMed;
    }
    
    public String getForFar() {
        return forFar;
    }
    
    public void setForFar(String forFar) {
        this.forFar = forFar;
    }
    
    public String getConMed() {
        return conMed;
    }
    
    public void setConMed(String conMed) {
        this.conMed = conMed;
    }
    
    public String getUniMed() {
        return uniMed;
    }
    
    public void setUniMed(String uniMed) {
        this.uniMed = uniMed;
    }
    
    public int getNumUni() {
        return numUni;
    }
    
    public void setNumUni(int numUni) {
        this.numUni = numUni;
    }
    
    public double getValUniMed() {
        return valUniMed;
    }
    
    public void setValUniMed(double valUniMed) {
        this.valUniMed = valUniMed;
    }
    
    public double getValTotMed() {
        return valTotMed;
    }
    
    public void setValTotMed(double valTotMed) {
        this.valTotMed = valTotMed;
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
    
}
