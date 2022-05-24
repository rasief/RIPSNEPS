package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * Clase que representa un registro de la tabla otros servicios
 * @author Feisar Moreno
 * @date 18/03/2019
 */
public class OtroServicio extends EntidadRIPS {
    private long idOtroServ;
    private long idCargaDeta;
    private int numRegistro;
    private long idUsuario;
    private String numFac;
    private String codEntPre;
    private String tipId;
    private String numId;
    private String numAut;
    private String tipServ;
    private String codServ;
    private String nomServ;
    private int cantidad;
    private double valUniServ;
    private double valTotServ;
    private int idTipoInter;
    private String numFacOri;
    private String observaciones;
    private int idGrupo;
    private String nomArch;
    private String nombreTipoInter;
    private String observacionesUS;
    private String nombreSede;
    private boolean indValores;
    
    public OtroServicio(long idOtroServ, long idCargaDeta, int numRegistro, long idUsuario, String numFac, String codEntPre, String tipId, String numId,
            String numAut, String tipServ, String codServ, String nomServ, int cantidad, double valUniServ, double valTotServ, String observaciones) {
        this.idOtroServ = idOtroServ;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.idUsuario = idUsuario;
        this.numFac = numFac;
        this.codEntPre = codEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.numAut = numAut;
        this.tipServ = tipServ;
        this.codServ = codServ;
        this.nomServ = nomServ;
        this.cantidad = cantidad;
        this.valUniServ = valUniServ;
        this.valTotServ = valTotServ;
        this.observaciones = observaciones;
        this.indValores = true;
    }
    
    public OtroServicio(ResultSet rs) {
        try {
            idOtroServ = rs.getLong("ID_OTRO_SERV");
        } catch (SQLException e) {
            idOtroServ = 0L;
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
            tipServ = rs.getString("TIP_SERV");
        } catch (SQLException e) {
            tipServ = "";
        }
        try {
            codServ = rs.getString("COD_SERV");
        } catch (SQLException e) {
            codServ = "";
        }
        try {
            nomServ = rs.getString("NOM_SERV");
        } catch (SQLException e) {
            nomServ = "";
        }
        try {
            cantidad = rs.getInt("CANTIDAD");
        } catch (SQLException e) {
            cantidad = 0;
        }
        try {
            valUniServ = rs.getDouble("VAL_UNI_SERV");
        } catch (SQLException e) {
            valUniServ = 0.0;
        }
        try {
            valTotServ = rs.getDouble("VAL_TOT_SERV");
        } catch (SQLException e) {
            valTotServ = 0.0;
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
        indValores = true;
    }
    
    public OtroServicio(Iterator<Cell> celdaIt, HashMap<Integer, String> mapaCols) {
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
                    if (valAux == Math.round(valAux)) {
                        valCampo = ((long)valAux) + "";
                    } else {
                        valCampo = valAux + "";
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
                    case "num_aut":
                        numAut = valCampo;
                        break;
                    case "tip_serv":
                        tipServ = valCampo;
                        break;
                    case "cod_serv":
                        codServ = valCampo;
                        break;
                    case "nom_serv":
                        nomServ = valCampo;
                        break;
                    case "cantidad":
                        try {
                            cantidad = Integer.parseInt(valCampo);
                        } catch (NumberFormatException ex) {
                            cantidad = Integer.MIN_VALUE;
                        }
                        break;
                    case "val_uni_serv":
                        try {
                            valUniServ = Double.parseDouble(valCampo);
                        } catch (NumberFormatException ex) {
                            valUniServ = Double.NaN;
                        }
                        break;
                    case "val_tot_serv":
                        try {
                            valTotServ = Double.parseDouble(valCampo);
                        } catch (NumberFormatException ex) {
                            valTotServ = Double.NaN;
                        }
                        break;
                }
            }
        }
    }
    
    public long getIdOtroServ() {
        return idOtroServ;
    }

    public void setIdOtroServ(long idOtroServ) {
        this.idOtroServ = idOtroServ;
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

    public String getTipServ() {
        return tipServ;
    }

    public void setTipServ(String tipServ) {
        this.tipServ = tipServ;
    }

    public String getCodServ() {
        return codServ;
    }

    public void setCodServ(String codServ) {
        this.codServ = codServ;
    }

    public String getNomServ() {
        return nomServ;
    }

    public void setNomServ(String nomServ) {
        this.nomServ = nomServ;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValUniServ() {
        return valUniServ;
    }

    public void setValUniServ(double valUniServ) {
        this.valUniServ = valUniServ;
    }

    public double getValTotServ() {
        return valTotServ;
    }

    public void setValTotServ(double valTotServ) {
        this.valTotServ = valTotServ;
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
