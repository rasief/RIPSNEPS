package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla nacidos
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Nacido extends EntidadRIPS {
    private long idNacido;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipIdMad;
    private String numIdMad;
    private String fechaNac;
    private String horaNac;
    private int edadGes;
    private String conPre;
    private String sexo;
    private int peso;
    private String diaNac;
    private String cauMue;
    private String fechaMue;
    private String horaMue;
    private int idTipoInter;
    private String numFacOri;
    private String observaciones;
    private int idGrupo;
    private String nomArch;
    private String nombreTipoInter;
    private String observacionesUS;
    private String nombreSede;
    
    public Nacido(long idNacido, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipIdMad, String numIdMad,
            String fechaNac, String horaNac, int edadGes, String conPre, String sexo, int peso, String diaNac, String cauMue, String fechaMue,
            String horaMue, String observaciones) {
        this.idNacido = idNacido;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipIdMad = tipIdMad;
        this.numIdMad = numIdMad;
        this.fechaNac = fechaNac;
        this.horaNac = horaNac;
        this.edadGes = edadGes;
        this.conPre = conPre;
        this.sexo = sexo;
        this.peso = peso;
        this.diaNac = diaNac;
        this.cauMue = cauMue;
        this.fechaMue = fechaMue;
        this.horaMue = horaMue;
        this.observaciones = observaciones;
    }
    
    public Nacido(ResultSet rs) {
        try {
            idNacido = rs.getLong("ID_NACIDO");
        } catch (SQLException e) {
            idNacido = 0L;
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
            tipIdMad = rs.getString("TIP_ID_MAD");
        } catch (SQLException e) {
            tipIdMad = "";
        }
        try {
            numIdMad = rs.getString("NUM_ID_MAD");
        } catch (SQLException e) {
            numIdMad = "";
        }
        try {
            fechaNac = rs.getString("FECHA_NAC_T");
        } catch (SQLException e) {
            fechaNac = "";
        }
        try {
            horaNac = rs.getString("HORA_NAC_T");
        } catch (SQLException e) {
            horaNac = "";
        }
        try {
            edadGes = rs.getInt("EDAD_GES");
        } catch (SQLException e) {
            edadGes = 0;
        }
        try {
            conPre = rs.getString("CON_PRE");
        } catch (SQLException e) {
            conPre = "";
        }
        try {
            sexo = rs.getString("SEXO");
        } catch (SQLException e) {
            sexo = "";
        }
        try {
            peso = rs.getInt("PESO");
        } catch (SQLException e) {
            peso = 0;
        }
        try {
            diaNac = rs.getString("DIA_NAC");
        } catch (SQLException e) {
            diaNac = "";
        }
        try {
            cauMue = rs.getString("CAU_MUE");
        } catch (SQLException e) {
            cauMue = "";
        }
        try {
            fechaMue = rs.getString("FECHA_MUE_T");
        } catch (SQLException e) {
            fechaMue = "";
        }
        try {
            horaMue = rs.getString("HORA_MUE_T");
        } catch (SQLException e) {
            horaMue = "";
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
    
    public long getIdNacido() {
        return idNacido;
    }

    public void setIdNacido(long idNacido) {
        this.idNacido = idNacido;
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

    public String getTipIdMad() {
        return tipIdMad;
    }

    public void setTipIdMad(String tipIdMad) {
        this.tipIdMad = tipIdMad;
    }

    public String getNumIdMad() {
        return numIdMad;
    }

    public void setNumIdMad(String numIdMad) {
        this.numIdMad = numIdMad;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getHoraNac() {
        return horaNac;
    }

    public void setHoraNac(String horaNac) {
        this.horaNac = horaNac;
    }

    public int getEdadGes() {
        return edadGes;
    }

    public void setEdadGes(int edadGes) {
        this.edadGes = edadGes;
    }

    public String getConPre() {
        return conPre;
    }

    public void setConPre(String conPre) {
        this.conPre = conPre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getDiaNac() {
        return diaNac;
    }

    public void setDiaNac(String diaNac) {
        this.diaNac = diaNac;
    }

    public String getCauMue() {
        return cauMue;
    }

    public void setCauMue(String cauMue) {
        this.cauMue = cauMue;
    }

    public String getFechaMue() {
        return fechaMue;
    }

    public void setFechaMue(String fechaMue) {
        this.fechaMue = fechaMue;
    }

    public String getHoraMue() {
        return horaMue;
    }
    
    public void setHoraMue(String horaMue) {
        this.horaMue = horaMue;
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
