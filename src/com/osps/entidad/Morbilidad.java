package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un registro de las consultas de morbilidad
 * @author Feisar Moreno
 * @date 12/12/2012
 */
public class Morbilidad {
    private byte indZeta;
    private byte gEdad;
    private int tipoCons;
    private int codEsp;
    private String nombreEsp;
    private int codAgrCie;
    private String nombreAgrCie;
    private String sexo;
    private String codNDP;
    private String zona;
    private int tipoUsu;
    private String ambReaPro;
    private String finPro;
    private String codSec;
    private String desSec;
    private String codGru;
    private String desGru;
    private String codSubGru;
    private String desSubGru;
    private long cantidadM;
    private long cantidadF;
    private long cantidadT;
    
    public Morbilidad() {
    }
    
    public Morbilidad(ResultSet rs) {
        try {
            this.indZeta = rs.getByte("INDZETA");
        } catch (SQLException e) {
            this.indZeta = 0;
        }
        
        try {
            this.gEdad = rs.getByte("GEDAD");
        } catch (SQLException e) {
            this.gEdad = 0;
        }
        
        try {
            this.tipoCons = rs.getInt("TIPOCONS");
        } catch (SQLException e) {
            this.tipoCons = 0;
        }
        
        try {
            this.codEsp = rs.getInt("CODESP");
        } catch (SQLException e) {
            this.codEsp = 0;
        }
        
        try {
            this.nombreEsp = rs.getString("NOMBREESP");
        } catch (SQLException e) {
            this.nombreEsp = "";
        }
        
        try {
            this.codAgrCie = rs.getInt("CODAGRCIE");
        } catch (SQLException e) {
            this.codAgrCie = 0;
        }
        
        try {
            this.nombreAgrCie = rs.getString("NOMBREAGRCIE");
        } catch (SQLException e) {
            this.nombreAgrCie = "";
        }
        
        try {
            this.sexo = rs.getString("SEXO");
        } catch (SQLException e) {
            this.sexo = "";
        }
        
        try {
            this.codNDP = rs.getString("CODNDP");
        } catch (SQLException e) {
            this.codNDP = "";
        }
        
        try {
            this.zona = rs.getString("ZONA");
        } catch (SQLException e) {
            this.zona = "";
        }
        
        try {
            this.tipoUsu = rs.getInt("TIPOUSU");
        } catch (SQLException e) {
            this.tipoUsu = 0;
        }
        
        try {
            this.ambReaPro = rs.getString("AMBREAPRO");
        } catch (SQLException e) {
            this.ambReaPro = "";
        }
        
        try {
            this.finPro = rs.getString("FINPRO");
        } catch (SQLException e) {
            this.finPro = "";
        }
        
        try {
            this.codSec = rs.getString("CODSEC");
        } catch (SQLException e) {
            this.codSec = "";
        }
        
        try {
            this.desSec = rs.getString("DESSEC");
        } catch (SQLException e) {
            this.desSec = "";
        }
        
        try {
            this.codGru = rs.getString("CODGRU");
        } catch (SQLException e) {
            this.codGru = "";
        }
        
        try {
            this.desGru = rs.getString("DESGRU");
        } catch (SQLException e) {
            this.desGru = "";
        }
        
        try {
            this.codSubGru = rs.getString("CODSUBGRU");
        } catch (SQLException e) {
            this.codSubGru = "";
        }
        
        try {
            this.desSubGru = rs.getString("DESSUBGRU");
        } catch (SQLException e) {
            this.desSubGru = "";
        }
        
        try {
            this.cantidadM = rs.getLong("CANTIDADM");
        } catch (SQLException e) {
            this.cantidadM = 0L;
        }
        
        try {
            this.cantidadF = rs.getLong("CANTIDADF");
        } catch (SQLException e) {
            this.cantidadF = 0L;
        }
        
        try {
            this.cantidadT = rs.getLong("CANTIDADT");
        } catch (SQLException e) {
            this.cantidadT = 0L;
        }
    }
    
    public byte getIndZeta() {
        return this.indZeta;
    }
    
    public byte getGEdad() {
        return this.gEdad;
    }
    
    public int getTipoCons() {
        return this.tipoCons;
    }
    
    public int getCodEsp() {
        return this.codEsp;
    }
    
    public String getNombreEsp() {
        return this.nombreEsp;
    }
    
    public int getCodAgrCie() {
        return this.codAgrCie;
    }
    
    public String getNombreAgrCie() {
        return this.nombreAgrCie;
    }
    
    public String getSexo() {
        return this.sexo;
    }
    
    public String getCodNDP() {
        return this.codNDP;
    }
    
    public String getZona() {
        return this.zona;
    }
    
    public int getTipoUsu() {
        return this.tipoUsu;
    }
    
    public String getAmbReaPro() {
        return this.ambReaPro;
    }
    
    public String getFinPro() {
        return this.finPro;
    }
    
    public String getCodSec() {
        return this.codSec;
    }
    
    public String getDesSec() {
        return this.desSec;
    }
    
    public String getCodGru() {
        return this.codGru;
    }
    
    public String getDesGru() {
        return this.desGru;
    }
    
    public String getCodSubGru() {
        return this.codSubGru;
    }
    
    public String getDesSubGru() {
        return this.desSubGru;
    }
    
    public long getCantidadM() {
        return this.cantidadM;
    }
    
    public long getCantidadF() {
        return this.cantidadF;
    }
    
    public long getCantidadT() {
        return this.cantidadT;
    }
}
