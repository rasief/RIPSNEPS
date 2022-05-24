package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla urgencias
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Urgencia extends EntidadRIPS {
    private long idUrgencia;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipId;
    private String numId;
    private String fechaIng;
    private String horaIng;
    private String numAut;
    private String cauExt;
    private String diaSal;
    private String diaRel1;
    private String diaRel2;
    private String diaRel3;
    private String desUsu;
    private String estSal;
    private String cauMue;
    private String fechaSal;
    private String horaSal;
    private int idTipoInter;
    private String numFacOri;
    private String observaciones;
    private int idGrupo;
    private String nomArch;
    private String nombreTipoInter;
    private String observacionesUS;
    private String nombreSede;

    public Urgencia(long idUrgencia, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipId, String numId,
            String fechaIng, String horaIng, String numAut, String cauExt, String diaSal, String diaRel1, String diaRel2, String diaRel3, String desUsu,
            String estSal, String cauMue, String fechaSal, String horaSal, String observaciones) {
        this.idUrgencia = idUrgencia;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.fechaIng = fechaIng;
        this.horaIng = horaIng;
        this.numAut = numAut;
        this.cauExt = cauExt;
        this.diaSal = diaSal;
        this.diaRel1 = diaRel1;
        this.diaRel2 = diaRel2;
        this.diaRel3 = diaRel3;
        this.desUsu = desUsu;
        this.estSal = estSal;
        this.cauMue = cauMue;
        this.fechaSal = fechaSal;
        this.horaSal = horaSal;
        this.observaciones = observaciones;
    }
    
    public Urgencia(ResultSet rs) {
        try {
            idUrgencia = rs.getLong("ID_URGENCIA");
        } catch (SQLException e) {
            idUrgencia = 0L;
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
            diaSal = rs.getString("DIA_SAL");
        } catch (SQLException e) {
            diaSal = "";
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
            desUsu = rs.getString("DES_USU");
        } catch (SQLException e) {
            desUsu = "";
        }
        try {
            estSal = rs.getString("EST_SAL");
        } catch (SQLException e) {
            estSal = "";
        }
        try {
            cauMue = rs.getString("CAU_MUE");
        } catch (SQLException e) {
            cauMue = "";
        }
        try {
            fechaSal = rs.getString("FECHA_SAL_T");
        } catch (SQLException e) {
            fechaSal = "";
        }
        try {
            horaSal = rs.getString("HORA_SAL_T");
        } catch (SQLException e) {
            horaSal = "";
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
    
    public long getIdUrgencia() {
        return idUrgencia;
    }

    public void setIdUrgencia(long idUrgencia) {
        this.idUrgencia = idUrgencia;
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

    public String getDiaSal() {
        return diaSal;
    }

    public void setDiaSal(String diaSal) {
        this.diaSal = diaSal;
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

    public String getDesUsu() {
        return desUsu;
    }

    public void setDesUsu(String desUsu) {
        this.desUsu = desUsu;
    }

    public String getEstSal() {
        return estSal;
    }

    public void setEstSal(String estSal) {
        this.estSal = estSal;
    }

    public String getCauMue() {
        return cauMue;
    }

    public void setCauMue(String cauMue) {
        this.cauMue = cauMue;
    }

    public String getFechaSal() {
        return fechaSal;
    }

    public void setFechaSal(String fechaSal) {
        this.fechaSal = fechaSal;
    }

    public String getHoraSal() {
        return horaSal;
    }

    public void setHoraSal(String horaSal) {
        this.horaSal = horaSal;
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
