package com.osps.entidad;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * Clase que representa un registro de la tabla usuarios
 * @author Feisar Moreno
 * @date 18/06/2018
 */
public class Usuario extends EntidadRIPS {
    private long idUsuario;
    private long idCargaDeta;
    private int numRegistro;
    private String tipId;
    private String numId;
    private String codEntAdm;
    private String tipUsu;
    private String ape1;
    private String ape2;
    private String nom1;
    private String nom2;
    private int edad;
    private String uniEdad;
    private String sexo;
    private String codDep;
    private String codMun;
    private String zona;
    private String observaciones;
    private long idAfiliado;
    private String nombreSede;
    private boolean indValores;
    
    public Usuario(long idUsuario, long idCargaDeta, int numRegistro, String tipId, String numId, String codEntAdm, String tipUsu, String ape1, String ape2,
            String nom1, String nom2, int edad, String uniEdad, String sexo, String codDep, String codMun, String zona, String observaciones) {
        this.idUsuario = idUsuario;
        this.idCargaDeta = idCargaDeta;
        this.numRegistro = numRegistro;
        this.tipId = tipId;
        this.numId = numId;
        this.codEntAdm = codEntAdm;
        this.tipUsu = tipUsu;
        this.ape1 = ape1;
        this.ape2 = ape2;
        this.nom1 = nom1;
        this.nom2 = nom2;
        this.edad = edad;
        this.uniEdad = uniEdad;
        this.sexo = sexo;
        this.codDep = codDep;
        this.codMun = codMun;
        this.zona = zona;
        this.observaciones = observaciones;
        this.indValores = true;
    }
    
    public Usuario(Iterator<Cell> celdaIt, HashMap<Integer, String> mapaCols) {
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
                    case "tip_id":
                        tipId = valCampo;
                        break;
                    case "num_id":
                        numId = valCampo;
                        break;
                    case "cod_ent_adm":
                        codEntAdm = valCampo;
                        break;
                    case "tip_usu":
                        tipUsu = valCampo;
                        break;
                    case "ape_1":
                        ape1 = valCampo;
                        break;
                    case "ape_2":
                        ape2 = valCampo;
                        break;
                    case "nom_1":
                        nom1 = valCampo;
                        break;
                    case "nom_2":
                        nom2 = valCampo;
                        break;
                    case "edad":
                        try {
                            edad = Integer.parseInt(valCampo);
                        } catch (NumberFormatException ex) {
                            edad = Integer.MIN_VALUE;
                        }
                        break;
                    case "uni_edad":
                        uniEdad = valCampo;
                        break;
                    case "sexo":
                        sexo = valCampo;
                        break;
                    case "cod_dep":
                        valCampo = "00" + valCampo;
                        codDep = valCampo.substring(valCampo.length() - 2);
                        break;
                    case "cod_mun":
                        valCampo = "000" + valCampo;
                        codMun = valCampo.substring(valCampo.length() - 3);
                        break;
                    case "zona":
                        zona = valCampo;
                        break;
                }
            }
        }
    }
    
    public Usuario(ResultSet rs) {
        try {
            idUsuario = rs.getLong("ID_USUARIO");
        } catch (SQLException e) {
            idUsuario = 0L;
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
            codEntAdm = rs.getString("COD_ENT_ADM");
        } catch (SQLException e) {
            codEntAdm = "";
        }
        try {
            tipUsu = rs.getString("TIP_USU");
        } catch (SQLException e) {
            tipUsu = "";
        }
        try {
            ape1 = rs.getString("APE_1");
        } catch (SQLException e) {
            ape1 = "";
        }
        try {
            ape2 = rs.getString("APE_2");
        } catch (SQLException e) {
            ape2 = "";
        }
        try {
            nom1 = rs.getString("NOM_1");
        } catch (SQLException e) {
            nom1 = "";
        }
        try {
            nom2 = rs.getString("NOM_2");
        } catch (SQLException e) {
            nom2 = "";
        }
        try {
            edad = rs.getInt("EDAD");
        } catch (SQLException e) {
            edad = 0;
        }
        try {
            uniEdad = rs.getString("UNI_EDAD");
        } catch (SQLException e) {
            uniEdad = "";
        }
        try {
            sexo = rs.getString("SEXO");
        } catch (SQLException e) {
            sexo = "";
        }
        try {
            codDep = rs.getString("COD_DEP");
        } catch (SQLException e) {
            codDep = "";
        }
        try {
            codMun = rs.getString("COD_MUN");
        } catch (SQLException e) {
            codMun = "";
        }
        try {
            zona = rs.getString("ZONA");
        } catch (SQLException e) {
            zona = "";
        }
        try {
            observaciones = rs.getString("OBSERVACIONES");
        } catch (SQLException e) {
            observaciones = "";
        }
        try {
            idAfiliado = rs.getLong("ID_AFILIADO");
        } catch (SQLException e) {
            idAfiliado = 0L;
        }
        try {
            nombreSede = rs.getString("NOMBRE_SEDE");
        } catch (SQLException e) {
            nombreSede = "";
        }
        this.indValores = true;
    }
    
    public long getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
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
    
    public String getCodEntAdm() {
        return codEntAdm;
    }
    
    public void setCodEntAdm(String codEntAdm) {
        this.codEntAdm = codEntAdm;
    }
    
    public String getTipUsu() {
        return tipUsu;
    }
    
    public void setTipUsu(String tipUsu) {
        this.tipUsu = tipUsu;
    }
    
    public String getApe1() {
        return ape1;
    }
    
    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }
    
    public String getApe2() {
        return ape2;
    }
    
    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }
    
    public String getNom1() {
        return nom1;
    }
    
    public void setNom1(String nom1) {
        this.nom1 = nom1;
    }
    
    public String getNom2() {
        return nom2;
    }
    
    public void setNom2(String nom2) {
        this.nom2 = nom2;
    }
    
    public int getEdad() {
        return edad;
    }
    
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public String getUniEdad() {
        return uniEdad;
    }
    
    public void setUniEdad(String uniEdad) {
        this.uniEdad = uniEdad;
    }
    
    public String getSexo() {
        return sexo;
    }
    
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
    public String getCodDep() {
        return codDep;
    }
    
    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }
    
    public String getCodMun() {
        return codMun;
    }
    
    public void setCodMun(String codMun) {
        this.codMun = codMun;
    }
    
    public String getZona() {
        return zona;
    }
    
    public void setZona(String zona) {
        this.zona = zona;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public long getIdAfiliado() {
        return idAfiliado;
    }
    
    public void setIdAfiliado(long idAfiliado) {
        this.idAfiliado = idAfiliado;
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
