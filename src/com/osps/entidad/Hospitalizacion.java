package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla hospitalizacion
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Hospitalizacion extends EntidadRIPS {
    private long idHospitalizacion;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipId;
    private String numId;
    private String viaIng;
    private String fechaIng;
    private String horaIng;
    private String numAut;
    private String cauExt;
    private String diaIng;
    private String diaEgr;
    private String diaRel1;
    private String diaRel2;
    private String diaRel3;
    private String diaCom;
    private String estSal;
    private String diaMue;
    private String fechaEgr;
    private String horaEgr;
    private int idTipoInter;
    private String numFacOri;
    private String observaciones;
    private int idGrupo;
    private String nomArch;
    private String nombreTipoInter;
    private String observacionesUS;
    private String nombreSede;

    public Hospitalizacion(long idHospitalizacion, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipId, String numId,
            String viaIng, String fechaIng, String horaIng, String numAut, String cauExt, String diaIng, String diaEgr, String diaRel1, String diaRel2, String diaRel3,
            String diaCom, String estSal, String diaMue, String fechaEgr, String horaEgr, String observaciones) {
        this.idHospitalizacion = idHospitalizacion;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.viaIng = viaIng;
        this.fechaIng = fechaIng;
        this.horaIng = horaIng;
        this.numAut = numAut;
        this.cauExt = cauExt;
        this.diaIng = diaIng;
        this.diaEgr = diaEgr;
        this.diaRel1 = diaRel1;
        this.diaRel2 = diaRel2;
        this.diaRel3 = diaRel3;
        this.diaCom = diaCom;
        this.estSal = estSal;
        this.diaMue = diaMue;
        this.fechaEgr = fechaEgr;
        this.horaEgr = horaEgr;
        this.observaciones = observaciones;
    }
    
    public Hospitalizacion(ResultSet rs) {
        try {
            idHospitalizacion = rs.getLong("ID_HOSPITALIZACION");
        } catch (SQLException e) {
            idHospitalizacion = 0L;
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
            viaIng = rs.getString("VIA_ING");
        } catch (SQLException e) {
            viaIng = "";
        }
        try {
            fechaIng = rs.getString("FECHA_ING_T");
        } catch (SQLException e) {
            fechaIng = "";
        }
        try {
            horaIng = rs.getString("HORA_ING_T");
        } catch (SQLException e) {
            horaIng = "";
        }
        try {
            numAut = rs.getString("NUM_AUT");
        } catch (SQLException e) {
            numAut = "";
        }
        try {
            cauExt = rs.getString("CAU_EXT");
        } catch (SQLException e) {
            cauExt = "";
        }
        try {
            diaIng = rs.getString("DIA_ING");
        } catch (SQLException e) {
            diaIng = "";
        }
        try {
            diaEgr = rs.getString("DIA_EGR");
        } catch (SQLException e) {
            diaEgr = "";
        }
        try {
            diaRel1 = rs.getString("DIA_REL1");
        } catch (SQLException e) {
            diaRel1 = "";
        }
        try {
            diaRel2 = rs.getString("DIA_REL2");
        } catch (SQLException e) {
            diaRel2 = "";
        }
        try {
            diaRel3 = rs.getString("DIA_REL3");
        } catch (SQLException e) {
            diaRel3 = "";
        }
        try {
            diaCom = rs.getString("DIA_COM");
        } catch (SQLException e) {
            diaCom = "";
        }
        try {
            estSal = rs.getString("EST_SAL");
        } catch (SQLException e) {
            estSal = "";
        }
        try {
            diaMue = rs.getString("DIA_MUE");
        } catch (SQLException e) {
            diaMue = "";
        }
        try {
            fechaEgr = rs.getString("FECHA_EGR_T");
        } catch (SQLException e) {
            fechaEgr = "";
        }
        try {
            horaEgr = rs.getString("HORA_EGR_T");
        } catch (SQLException e) {
            horaEgr = "";
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
    
    public long getIdHospitalizacion() {
        return idHospitalizacion;
    }
    
    public void setIdHospitalizacion(long idHospitalizacion) {
        this.idHospitalizacion = idHospitalizacion;
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

    public String getViaIng() {
        return viaIng;
    }

    public void setViaIng(String viaIng) {
        this.viaIng = viaIng;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getHoraIng() {
        return horaIng;
    }

    public void setHoraIng(String horaIng) {
        this.horaIng = horaIng;
    }

    public String getNumAut() {
        return numAut;
    }

    public void setNumAut(String numAut) {
        this.numAut = numAut;
    }

    public String getCauExt() {
        return cauExt;
    }

    public void setCauExt(String cauExt) {
        this.cauExt = cauExt;
    }

    public String getDiaIng() {
        return diaIng;
    }

    public void setDiaIng(String diaIng) {
        this.diaIng = diaIng;
    }

    public String getDiaEgr() {
        return diaEgr;
    }

    public void setDiaEgr(String diaEgr) {
        this.diaEgr = diaEgr;
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

    public String getDiaCom() {
        return diaCom;
    }

    public void setDiaCom(String diaCom) {
        this.diaCom = diaCom;
    }

    public String getEstSal() {
        return estSal;
    }

    public void setEstSal(String estSal) {
        this.estSal = estSal;
    }

    public String getDiaMue() {
        return diaMue;
    }

    public void setDiaMue(String diaMue) {
        this.diaMue = diaMue;
    }

    public String getFechaEgr() {
        return fechaEgr;
    }

    public void setFechaEgr(String fechaEgr) {
        this.fechaEgr = fechaEgr;
    }

    public String getHoraEgr() {
        return horaEgr;
    }

    public void setHoraEgr(String horaEgr) {
        this.horaEgr = horaEgr;
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
