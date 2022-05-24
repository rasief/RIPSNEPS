package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de la tabla transaccion
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Factura extends EntidadRIPS {
    private long idFactura;
    private long idCargaDeta;
    private int numRegistro;
    private String codEntPre;
    private String nomEntPre;
    private String tipId;
    private String numId;
    private String numFac;
    private String fechaExp;
    private String fechaIni;
    private String fechaFin;
    private String codEntAdm;
    private String nomEntAdm;
    private String numCon;
    private String plaBen;
    private String numPol;
    private double valCop;
    private double valCom;
    private double valDes;
    private double valNet;
    private String observaciones;

    public Factura(long idFactura, long idCargaDeta, int numRegistro, String codEntPre, String nomEntPre, String tipId, String numId, String numFac,
            String fechaExp, String fechaIni, String fechaFin, String codEntAdm, String nomEntAdm, String numCon, String plaBen, String numPol,
            double valCop, double valCom, double valDes, double valNet, String observaciones) {
        this.idFactura = idFactura;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.codEntPre = codEntPre;
        this.nomEntPre = nomEntPre;
        this.tipId = tipId;
        this.numId = numId;
        this.numFac = numFac;
        this.fechaExp = fechaExp;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.codEntAdm = codEntAdm;
        this.nomEntAdm = nomEntAdm;
        this.numCon = numCon;
        this.plaBen = plaBen;
        this.numPol = numPol;
        this.valCop = valCop;
        this.valCom = valCom;
        this.valDes = valDes;
        this.valNet = valNet;
        this.observaciones = observaciones;
    }
    
    public Factura(ResultSet rs) {
        try {
            idFactura = rs.getLong("ID_FACTURA");
        } catch (SQLException e) {
            idFactura = 0L;
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
            codEntPre = rs.getString("COD_ENT_PRE");
        } catch (SQLException e) {
            codEntPre = "";
        }
        try {
            nomEntPre = rs.getString("NOM_ENT_PRE");
        } catch (SQLException e) {
            nomEntPre = "";
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
            numFac = rs.getString("NUM_FAC");
        } catch (SQLException e) {
            numFac = "";
        }
        try {
            fechaExp = rs.getString("FECHA_EXP_T");
        } catch (SQLException e) {
            fechaExp = "";
        }
        try {
            fechaIni = rs.getString("FECHA_INI_T");
        } catch (SQLException e) {
            fechaIni = "";
        }
        try {
            fechaFin = rs.getString("FECHA_FIN_T");
        } catch (SQLException e) {
            fechaFin = "";
        }
        try {
            codEntAdm = rs.getString("COD_ENT_ADM");
        } catch (SQLException e) {
            codEntAdm = "";
        }
        try {
            nomEntAdm = rs.getString("NOM_ENT_ADM");
        } catch (SQLException e) {
            nomEntAdm = "";
        }
        try {
            numCon = rs.getString("NUM_CON");
        } catch (SQLException e) {
            numCon = "";
        }
        try {
            plaBen = rs.getString("PLA_BEN");
        } catch (SQLException e) {
            plaBen = "";
        }
        try {
            numPol = rs.getString("NUM_POL");
        } catch (SQLException e) {
            numPol = "";
        }
        try {
            valCop = rs.getDouble("VAL_COP");
        } catch (SQLException e) {
            valCop = 0.0;
        }
        try {
            valCom = rs.getDouble("VAL_COM");
        } catch (SQLException e) {
            valCom = 0.0;
        }
        try {
            valDes = rs.getDouble("VAL_DES");
        } catch (SQLException e) {
            valDes = 0.0;
        }
        try {
            valNet = rs.getDouble("VAL_NET");
        } catch (SQLException e) {
            valNet = 0.0;
        }
        try {
            observaciones = rs.getString("OBSERVACIONES");
        } catch (SQLException e) {
            observaciones = "";
        }
    }
    
    public long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(long idFactura) {
        this.idFactura = idFactura;
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

    public String getCodEntPre() {
        return codEntPre;
    }

    public void setCodEntPre(String codEntPre) {
        this.codEntPre = codEntPre;
    }

    public String getNomEntPre() {
        return nomEntPre;
    }

    public void setNomEntPre(String nomEntPre) {
        this.nomEntPre = nomEntPre;
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

    public String getNumFac() {
        return numFac;
    }

    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }

    public String getFechaExp() {
        return fechaExp;
    }

    public void setFechaExp(String fechaExp) {
        this.fechaExp = fechaExp;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCodEntAdm() {
        return codEntAdm;
    }

    public void setCodEntAdm(String codEntAdm) {
        this.codEntAdm = codEntAdm;
    }

    public String getNomEntAdm() {
        return nomEntAdm;
    }

    public void setNomEntAdm(String nomEntAdm) {
        this.nomEntAdm = nomEntAdm;
    }

    public String getNumCon() {
        return numCon;
    }

    public void setNumCon(String numCon) {
        this.numCon = numCon;
    }

    public String getPlaBen() {
        return plaBen;
    }

    public void setPlaBen(String plaBen) {
        this.plaBen = plaBen;
    }

    public String getNumPol() {
        return numPol;
    }

    public void setNumPol(String numPol) {
        this.numPol = numPol;
    }

    public double getValCop() {
        return valCop;
    }

    public void setValCop(double valCop) {
        this.valCop = valCop;
    }

    public double getValCom() {
        return valCom;
    }

    public void setValCom(double valCom) {
        this.valCom = valCom;
    }

    public double getValDes() {
        return valDes;
    }

    public void setValDes(double valDes) {
        this.valDes = valDes;
    }

    public double getValNet() {
        return valNet;
    }

    public void setValNet(double valNet) {
        this.valNet = valNet;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
}
