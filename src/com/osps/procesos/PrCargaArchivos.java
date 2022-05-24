package com.osps.procesos;

import com.osps.db.DbCargaArch;
import com.osps.db.DbConsultas;
import com.osps.db.DbErrores;
import com.osps.db.DbFacturas;
import com.osps.db.DbHospitalizacion;
import com.osps.db.DbMedicamentos;
import com.osps.db.DbNacidos;
import com.osps.db.DbOtrosServicios;
import com.osps.db.DbProcedimientos;
import com.osps.db.DbUrgencias;
import com.osps.db.DbUsuarios;
import com.osps.entidad.CargaArch;
import com.osps.entidad.CargaArchDeta;
import com.osps.entidad.Consulta;
import com.osps.entidad.Factura;
import com.osps.entidad.Hospitalizacion;
import com.osps.entidad.Medicamento;
import com.osps.entidad.Nacido;
import com.osps.entidad.OtroServicio;
import com.osps.entidad.Procedimiento;
import com.osps.entidad.Urgencia;
import com.osps.entidad.Usuario;
import com.osps.utilidades.Utilidades;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase para la carga de archivos de RIPS
 *
 * @author Feisar Moreno
 * @date 22/02/2012
 */
public class PrCargaArchivos {
    private static final String COD_ENT_ADM = "EPS037";
    private final long idCarga; //Identificador de la carga
    private final int ano; //Año a cargar
    private final int mes; //Mes a cargar (0 para todos los meses)
    private final String nomCarpeta; //Nombre de la carpeta en la que se encuentran los archivos
    private final boolean selUsuarios; //Indicador de archivo de usuarios seleccionado
    private final boolean selConsultas; //Indicador de archivo de consultas seleccionado
    private final boolean selProcedimientos; //Indicador de archivo de procedimientos seleccionado
    private final boolean selHospitalizacion; //Indicador de archivo de hospitalización seleccionado
    private final boolean selUrgencias; //Indicador de archivo de urgencias seleccionado
    private final boolean selMedicamentos; //Indicador de archivo de medicamentos seleccionado
    private final boolean selNacidos; //Indicador de archivo de recién nacidos seleccionado
    private final boolean selTransacciones; //Indicador de archivo de transacciones seleccionado
    private final boolean selOtrosServicios; //Indicador de archivo de otros servicios seleccionado
    private final DbErrores dbErrores = new DbErrores();
    private final String charSet = "ISO-8859-1";
    
    private PrCargaArchivos(long idCarga, int ano, int mes, String nomCarpeta) {
        this.idCarga = idCarga;
        this.ano = ano;
        this.mes = mes;
        this.nomCarpeta = nomCarpeta;
        this.selUsuarios = true;
        this.selConsultas = true;
        this.selProcedimientos = true;
        this.selHospitalizacion = true;
        this.selUrgencias = true;
        this.selMedicamentos = true;
        this.selNacidos = true;
        this.selTransacciones = true;
        this.selOtrosServicios = true;
    }
    
    /**
     * Método estático que inicializa una carga
     * @author Feisar Moreno
     * @date 10/10/2019
     * @param ano Año a cargar
     * @param mes Mes a cargar (0 para todos los meses)
     * @param nomCarpeta Nombre de la carpeta en la que se encuentran los archivos
     * @return Objeto <code>PrCargaArchivos</code>.
     */
    public static PrCargaArchivos iniciarCarga(int ano, int mes, String nomCarpeta) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        
        //Se busca el registro de carga
        CargaArch cargaArch = dbCargaArch.getCargaArch(COD_ENT_ADM, ano, mes);
        
        long idCarga;
        if (cargaArch != null) {
            idCarga = cargaArch.getIdCarga();
            if (!nomCarpeta.equalsIgnoreCase(cargaArch.getRutaCarga())) {
                int opcionAux = JOptionPane.showConfirmDialog(null, "Ya existe un registro de carga para el período seleccionado con una carpeta diferente\n(" +
                        cargaArch.getRutaCarga() + ")\n¿Desea reanudar la carga con la carpeta inicial?", "Carga de archivos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (opcionAux == JOptionPane.YES_OPTION) {
                    nomCarpeta = cargaArch.getRutaCarga();
                } else {
                    cargaArch = null;
                }
            }
        } else {
            //Se inserta el registro de control de carga
            idCarga = dbCargaArch.insertarRegistro(nomCarpeta, COD_ENT_ADM, ano, mes);
            cargaArch = dbCargaArch.getCargaArch(idCarga);
        }
        
        PrCargaArchivos prCargaArchivos;
        if (cargaArch != null) {
            prCargaArchivos = new PrCargaArchivos(idCarga, ano, mes, nomCarpeta);
        } else {
            prCargaArchivos = null;
        }
        
        return prCargaArchivos;
    }
    
    public long getIdCarga() {
        return this.idCarga;
    }
    
    /**
     * Método que abre y carga los archivos uno a uno
     */
    public void cargarArchivos() {
        //Se abre la carpeta seleccionada
        File carpetaRIPS = new File(this.nomCarpeta);
        this.cargarArchivos(carpetaRIPS);
    }

    private void cargarArchivos(File carpetaRIPS) {
        if (!carpetaRIPS.isDirectory()) {
            throw new IllegalArgumentException("El parámetro seleccionado no es una carpeta válida");
        } else {
            DbCargaArch dbCargaArch = new DbCargaArch();
            File[] listaArchivos = carpetaRIPS.listFiles();
            for (File archAux : listaArchivos) {
                //si se trata de una carpeta, se debe abrir
                if (archAux.isDirectory()) {
                    this.cargarArchivos(archAux);
                } else {
                    String extension;
                    System.out.println(archAux.getAbsolutePath());
                    int posAux = archAux.getName().lastIndexOf(".");
                    if (posAux >= 0) {
                        extension = archAux.getName().substring(posAux + 1);
                    } else {
                        extension = "";
                    }
                    
                    if (extension.equalsIgnoreCase("txt") || extension.equalsIgnoreCase("csv")) {
                        //Archivos de texto plano
                        String prefijo = archAux.getName().substring(0, 2).toUpperCase();
                        
                        //Se carga el archivo respectivo
                        switch (prefijo) {
                            case "US":
                                this.cargarUsuarios(archAux, this.idCarga);
                                break;
                            case "AC":
                                //this.cargarConsultas(archAux, this.idCarga);
                                break;
                            case "AP":
                                this.cargarProcedimientos(archAux, this.idCarga);
                                break;
                            case "AM":
                                //this.cargarMedicamentos(archAux, this.idCarga);
                                break;
                            case "AT":
                                this.cargarOtrosServicios(archAux, this.idCarga);
                                break;
                            case "AH":
                                //this.cargarHospitalizacion(archAux, this.idCarga);
                                break;
                            case "AU":
                                //this.cargarUrgencias(archAux, this.idCarga);
                                break;
                            case "AN":
                                //this.cargarNacidos(archAux, this.idCarga);
                                break;
                            case "AF":
                                //this.cargarFacturas(archAux, this.idCarga);
                                break;
                            case "AD":
                            case "CT":
                                break;
                            default:
                                //Nombre incorrecto
                                String nomArch = obtenerNombreArchivo(archAux);
                                dbCargaArch.crearActualizarRegistroDeta(0, this.idCarga, nomArch, "NN", 0, 8);
                                break;
                        }
                    } else if (extension.equalsIgnoreCase("xlsx")) {
                        //Archivos de Excel
                        this.cargarXLSX(archAux, this.idCarga);
                    } else {
                        //Archivos no cargados
                        String nomArch = obtenerNombreArchivo(archAux);
                        dbCargaArch.crearActualizarRegistroDeta(0, this.idCarga, nomArch, "NN", 0, 6);
                    }
                }
            }
        }
    }
    
    /**
     * Método que realiza los ajustes a los datos previamente cargados
     * @return Mensaje de error en caso de presentarse, de lo contrario cadena de longitud cero (0).
     */
    public String ajustarDatos() {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbProcedimientos dbProcedimientos = new DbProcedimientos();
        DbOtrosServicios dbOtrosServicios = new DbOtrosServicios();
        DbHospitalizacion dbHospitalizacion = new DbHospitalizacion();
        DbUsuarios dbUsuarios= new DbUsuarios();
        
        //Se validan los códigos CUPS almacenados en los nombres de los servicios del archivo AT
        /*Pattern pattern56 = Pattern.compile("^\\d{5,6}");
        Pattern pattern6 = Pattern.compile("^\\d{6}");
        Pattern pattern13 = Pattern.compile("^\\d{1,3}");
        Pattern pattern4m = Pattern.compile("^\\d{4,}");
        
        //Lista para agregar los procedimientos a incluir a partir de otros servicios
        ArrayList<Procedimiento> listaProcedimientos = new ArrayList<>();
        
        //Se cargan los registros de honorarios y manejo a analizar
        ArrayList<OtroServicio> listaOtrosServicios = dbOtrosServicios.getListaOtrosServicios(idCarga, "S4");
        for (OtroServicio otroServicioAux : listaOtrosServicios) {
            ArrayList<String> listaPartes = obtenerPartes(otroServicioAux.getNomServ(), "\\s+|\\.|\\+|\\(|\\)|-|[xX]");
            
            //Se analizan los componentes del nombre buscando códigos CUPS
            for (int i = 0; i < listaPartes.size(); i++) {
                String parteAux = listaPartes.get(i);
                
                Matcher matcher56 = pattern56.matcher(parteAux);
                if (matcher56.lookingAt()) {
                    //Se trata de un código CUPS, se extrae el código
                    String codCUPSAux;
                    Matcher matcher6 = pattern6.matcher(parteAux);
                    if (matcher6.lookingAt()) {
                        codCUPSAux = parteAux.substring(0, 6);
                    } else {
                        codCUPSAux = "0" + parteAux.substring(0, 5);
                    }
                    
                    //Se buscan los procedimientos
                    int cantidad = 1;
                    if (!matcher56.matches()) {
                        //Multiplicador en el mismo texto
                        String textoAux = parteAux.substring(codCUPSAux.length()).replaceFirst("^\\D", "");
                        if (!textoAux.contains("%")) {
                            String textoVal = "";
                            for (int j = 0; j < textoAux.length(); j++) {
                                String caracterAux = textoAux.substring(j, j + 1);
                                if (caracterAux.matches("\\d")) {
                                    textoVal += caracterAux;
                                } else {
                                    break;
                                }
                            }
                            
                            if (textoVal.length() > 0 && (codCUPSAux.substring(0, 2).equals("23") || codCUPSAux.substring(0, 2).equals("24") || codCUPSAux.substring(0, 3).equals("997"))) {
                                cantidad = Integer.parseInt(textoVal);
                            }
                        }
                    } else if (i < listaPartes.size() - 1) {
                        //Multiplicador en la siguiente parte
                        String textoAux = listaPartes.get(i + 1).replaceFirst("^\\D", "");
                        
                        if (!textoAux.contains("%")) {
                            Matcher matcher13 = pattern13.matcher(textoAux);
                            Matcher matcher4m = pattern4m.matcher(textoAux);
                            if (matcher13.lookingAt() && !matcher4m.lookingAt()) {
                                String textoVal = "";
                                for (int j = 0; j < textoAux.length(); j++) {
                                    String caracterAux = textoAux.substring(j, j + 1);
                                    if (caracterAux.matches("\\d")) {
                                        textoVal += caracterAux;
                                    } else {
                                        break;
                                    }
                                }
                                
                                if (textoVal.length() > 0 && (codCUPSAux.substring(0, 2).equals("23") || codCUPSAux.substring(0, 2).equals("24") || codCUPSAux.substring(0, 3).equals("997"))) {
                                    cantidad = Integer.parseInt(textoVal);
                                }
                            }
                        }
                    }
                    
                    //Se agrega el procedimiento al archivo AP buscando además el registro relacionado de hospitalización
                    ArrayList<Procedimiento> listaProcedimientosAux = convertirATaAP(otroServicioAux, codCUPSAux, cantidad, true);
                    
                    listaProcedimientos.addAll(listaProcedimientosAux);
                    //System.out.println(codCUPSAux + " X " + cantidad + " - " + otroServicioAux.getNomServ());
                }
            }
        }
        
        if (listaProcedimientos.size() > 0) {
            //Se separan los procedimientos por archivo de carga en un mapa
            HashMap<Long, ArrayList<Procedimiento>> mapaProcedimientos = new HashMap<>();
            listaProcedimientos.forEach((procedimientoAux) -> {
                ArrayList<Procedimiento> listaAux;
                if (mapaProcedimientos.containsKey(procedimientoAux.getIdCargaDeta())) {
                    listaAux = mapaProcedimientos.get(procedimientoAux.getIdCargaDeta());
                } else {
                    listaAux = new ArrayList<>();
                }
                listaAux.add(procedimientoAux);
                mapaProcedimientos.put(procedimientoAux.getIdCargaDeta(), listaAux);
            });
            
            for (Map.Entry<Long, ArrayList<Procedimiento>> entradaAux : mapaProcedimientos.entrySet()) {
                CargaArchDeta cargaArchDetaAux = dbCargaArch.getCargaArchDeta(entradaAux.getKey());
                ArrayList<Procedimiento> listaAux = entradaAux.getValue();
                
                int rtaAux = dbProcedimientos.insertarRegistros(listaAux, cargaArchDetaAux, false, "Creado a partir de códigos incluidos en registro AT");
                
                if (rtaAux < 0) {
                    return "Error en la creación de procedimientos a partir de otros servicios con código en el nombre (" + cargaArchDetaAux.getNomArch() + ").";
                }
            }
        }
        
        //Se cargan los registros de otros servicios a convertir directamente en procedimientos
        listaProcedimientos = new ArrayList<>();
        String[] arrProcedimientos = {"869500", "895100", "897011", "897012"};
        listaOtrosServicios = dbOtrosServicios.getListaOtrosServicios(idCarga, arrProcedimientos);
        for (OtroServicio otroServicioAux : listaOtrosServicios) {
            //Se agrega el procedimiento al archivo AP buscando además el registro relacionado de hospitalización
            ArrayList<Procedimiento> listaProcedimientosAux = convertirATaAP(otroServicioAux, otroServicioAux.getCodServ(), 1, false);
            listaProcedimientos.addAll(listaProcedimientosAux);
        }
        
        if (listaProcedimientos.size() > 0) {
            //Se separan los procedimientos por archivo de carga en un mapa
            HashMap<Long, ArrayList<Procedimiento>> mapaProcedimientos = new HashMap<>();
            listaProcedimientos.forEach((procedimientoAux) -> {
                ArrayList<Procedimiento> listaAux;
                if (mapaProcedimientos.containsKey(procedimientoAux.getIdCargaDeta())) {
                    listaAux = mapaProcedimientos.get(procedimientoAux.getIdCargaDeta());
                } else {
                    listaAux = new ArrayList<>();
                }
                listaAux.add(procedimientoAux);
                mapaProcedimientos.put(procedimientoAux.getIdCargaDeta(), listaAux);
            });
            
            for (Map.Entry<Long, ArrayList<Procedimiento>> entradaAux : mapaProcedimientos.entrySet()) {
                CargaArchDeta cargaArchDetaAux = dbCargaArch.getCargaArchDeta(entradaAux.getKey());
                ArrayList<Procedimiento> listaAux = entradaAux.getValue();
                
                int rtaAux = dbProcedimientos.insertarRegistros(listaAux, cargaArchDetaAux, true, "Creado a partir de procedimientos asignados incorrectamente en registro AT");
                
                if (rtaAux < 0) {
                    return "Error en la creación de procedimientos a partir de otros servicios (" + cargaArchDetaAux.getNomArch() + ").";
                }
            }
        }
        
        //Se actualizan los registros con códigos antiguos
        int rtaAux = dbCargaArch.actualizarCodigosAntiguos(idCarga);
        if (rtaAux < 0) {
            return "Error en la actualización de códigos antiguos.";
        }
        
        //Se unifican los registros de hospitalización cercanos en tiempo
        rtaAux = dbHospitalizacion.unificarHospitalizaciones(idCarga);
        if (rtaAux < 0) {
            return "Error en la unificación de registros de hospitalización.";
        }*/
        
        //Se marcan los faltantes de días de hospitalización y servicios de estancia
        int rtaAux = dbCargaArch.marcarFaltantesAHAT(idCarga);
        if (rtaAux < 0) {
            return "Error en la revisión de hospitalizaciones y estancias.";
        }
        
        //Se realizan las marcas por tipo de internación
        rtaAux = dbCargaArch.actualizarTipoInternacion(idCarga);
        if (rtaAux < 0) {
            return "Error en la actualización del tipo de internación.";
        }
        
        //Se cargan los registros de afiliados de la Nueva EPS
        rtaAux = dbCargaArch.cargarAfiliadosNEPS(idCarga);
        if (rtaAux < 0) {
            return "Error en la carga de afiliados de la Nueva EPS.";
        }
        
        //Se cargan los registros de usuarios de las bases de Nueva EPS
        rtaAux = dbUsuarios.cargarUsuarios(idCarga);
        if (rtaAux < 0) {
            return "Error en la carga de usuarios desde la base de afiliados de la Nueva EPS.";
        }
        
        //Se realiza la asociación entre usuarios y servicios
        rtaAux = dbCargaArch.asociarUsuariosServicios(idCarga);
        if (rtaAux < 0) {
            return "Error en la asociación entre usuarios y servicios.";
        }
        
        return "";
    }
    
    private ArrayList<String> obtenerPartes(String textoBase, String expresionRegular) {
        ArrayList<String> listaPartes = new ArrayList<>();
        
        String[] arrPartes = textoBase.split(expresionRegular);
        for (String parteAux : arrPartes) {
            if (!parteAux.equals("")) {
                listaPartes.add(parteAux);
            }
        }
        
        return listaPartes;
    }
    
    private ArrayList<Procedimiento> convertirATaAP(OtroServicio otroServicio, String codPro, int cantidad, boolean indValidarExistencia) {
        ArrayList<Procedimiento> listaProcedimientosRta = new ArrayList<>();
        
        DbProcedimientos dbProcedimientos = new DbProcedimientos();
        DbHospitalizacion dbHospitalizacion = new DbHospitalizacion();
        
        //Se obtiene el listado de procedimientos existentes del tipo dado
        int cantidadActual;
        if (indValidarExistencia) {
            ArrayList<Procedimiento> listaProcedimientos = dbProcedimientos.getListaProcedimientos(idCarga, otroServicio.getTipId(), otroServicio.getNumId(), codPro);
            cantidadActual = listaProcedimientos.size();
        } else {
            cantidadActual = 0;
        }
        
        if (cantidadActual < cantidad) {
            //Faltan procedimientos, se busca el registro de hospitalización para aproximar una fecha de prestación de los procedimientos
            ArrayList<Hospitalizacion> listaHospitalizacion = dbHospitalizacion.getListaHospitalizacion(idCarga, otroServicio.getTipId(), otroServicio.getNumId(), ano, mes);
            String fechaPro;
            if (listaHospitalizacion.size() > 0) {
                //Se toma el primer registro para la fecha de los procedimientos
                fechaPro = calcularFechaInicioHospitalizacionMes(listaHospitalizacion.get(0).getFechaIng());
            } else {
                fechaPro = "";
            }
            
            for (int i = cantidadActual; i < cantidad; i++) {
                Procedimiento procedimientoAux = new Procedimiento(0, otroServicio.getIdCargaDeta(), otroServicio.getNumRegistro(), otroServicio.getIdUsuario(), otroServicio.getNumFac(),
                        otroServicio.getCodEntPre(), otroServicio.getTipId(), otroServicio.getNumId(), fechaPro, otroServicio.getNumAut(), codPro, (fechaPro.equals("") ? "1" : "2"),
                        "1", "", "", "", "", "", otroServicio.getValUniServ(), "", otroServicio.getIdOtroServ(), "");
                
                listaProcedimientosRta.add(procedimientoAux);
            }
        }
        
        return listaProcedimientosRta;
    }
    
    private String calcularFechaInicioHospitalizacionMes(String fechaIng) {
        String fechaRta = "01/" + (mes < 10 ? "0" : "") + mes + "/" + ano;
        try {
            String[] arrPartes = fechaIng.split("/");
            int mesAux = Integer.parseInt(arrPartes[1], 10);
            int anoAux = Integer.parseInt(arrPartes[2], 10);
            
            if (anoAux == ano && mesAux == mes) {
                fechaRta = fechaIng;
            }
        } catch (Exception ex) {}
        return fechaRta;
    }
    
    /**
     * Método que valida si un archivo específico debe ser cargado
     * @param nombArchivo Nombre del archivo
     * @return <code>true</code> si se debe cargar el archivo, de lo contrario
     * <code>false</code>.
     */
    private boolean validarCargar(String nombArchivo) {
        boolean cargar = false;
        String extension = "";
        int posAux = nombArchivo.lastIndexOf(".");
        if (posAux != -1) {
            extension = nombArchivo.substring(posAux + 1).toLowerCase();
        }

        //Solo se acepta si el archivo es txt
        if (extension.equals("txt")) {
            String prefijo = nombArchivo.substring(0, 2).toUpperCase();
            switch (prefijo) {
                case "US":
                    if (this.selUsuarios) {
                        cargar = true;
                    }   break;
                case "AC":
                    if (this.selConsultas) {
                        cargar = true;
                    }   break;
                case "AP":
                    if (this.selProcedimientos) {
                        cargar = true;
                    }   break;
                case "AH":
                    if (this.selHospitalizacion) {
                        cargar = true;
                    }   break;
                case "AU":
                    if (this.selUrgencias) {
                        cargar = true;
                    }   break;
                case "AM":
                    if (this.selMedicamentos) {
                        cargar = true;
                    }   break;
                case "AN":
                    if (this.selNacidos) {
                        cargar = true;
                    }   break;
                case "AF":
                    if (this.selTransacciones) {
                        cargar = true;
                    }   break;
                case "AT":
                    if (this.selOtrosServicios) {
                        cargar = true;
                    }   break;
                default:
                    break;
            }
        }

        return cargar;
    }
    
    private CargaArchDeta obtenerDetalleCarga(long idCarga, String nomArch, String codTabla) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        CargaArchDeta cargaArchDeta = dbCargaArch.getCargaArchDeta(idCarga, nomArch, codTabla);
        
        if (cargaArchDeta == null) {
            long idCargaDeta = dbCargaArch.crearActualizarRegistroDeta(0, idCarga, nomArch.replace("\\", "/"), codTabla, 0, 1);
            cargaArchDeta = dbCargaArch.getCargaArchDeta(idCargaDeta);
        }
        
        return cargaArchDeta;
    }
    
    private String obtenerNombreArchivo(File archivo) {
        String nombreArchivo = archivo.getAbsolutePath().replace(this.nomCarpeta, "").replace("\\", "/");
        
        return nombreArchivo;
    }
    
    /**
     * Método que carga un archivo de usuarios
     * @param archUsuarios Archivo de usuarios
     * @param idCarga Identificador del registro de carga
     */
    private void cargarUsuarios(File archUsuarios, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbUsuarios dbUsuarios = new DbUsuarios();
        String nomArch = obtenerNombreArchivo(archUsuarios);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "US");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;

        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Usuario> listaUsuarios;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archUsuarios), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaUsuarios = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 15) {
                            String tipId = Utilidades.izquierda(arrCampos[0], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[1], 20);
                            String codEntAdm = Utilidades.izquierda(arrCampos[2], 6);
                            String tipUsu = Utilidades.izquierda(arrCampos[3], 1);
                            String ape1 = Utilidades.izquierda(arrCampos[4], 30);
                            String ape2 = Utilidades.izquierda(arrCampos[5], 30);
                            String nom1 = Utilidades.izquierda(arrCampos[6], 20);
                            String nom2 = Utilidades.izquierda(arrCampos[7], 20);
                            int edad;
                            try {
                                edad = Integer.parseInt(Utilidades.izquierda(arrCampos[8], 3));
                            } catch (NumberFormatException e) {
                                edad = Integer.MIN_VALUE;
                            }
                            String uniEdad = Utilidades.izquierda(arrCampos[9], 1);
                            String sexo = Utilidades.izquierda(arrCampos[10], 1).toUpperCase();
                            String codDep = Utilidades.izquierda(arrCampos[11], 2).trim();
                            try {
                                int codDepAux = Integer.parseInt(codDep, 10);
                                codDep = Utilidades.derecha("00" + codDepAux, 2);
                            } catch (NumberFormatException e) {}
                            String codMun = Utilidades.izquierda(arrCampos[12], 3).trim();
                            try {
                                int codMunAux = Integer.parseInt(codMun, 10);
                                codMun = Utilidades.derecha("000" + codMunAux, 3);
                            } catch (NumberFormatException e) {}
                            String zona = Utilidades.izquierda(arrCampos[13], 1);
                            
                            Usuario usuarioAux = new Usuario(0, idCargaDeta, contAux, tipId, numId, codEntAdm, tipUsu, ape1, ape2,
                                    nom1, nom2, edad, uniEdad, sexo, codDep, codMun, zona, "");
                            
                            listaUsuarios.add(usuarioAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "US", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "US", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }

            //Se insertan los registros sin errores de estructura
            if (!listaUsuarios.isEmpty()) {
                dbUsuarios.insertarRegistros(listaUsuarios, cargaArchDeta);
            }

            if (!interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "US", 0, 2);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "US", 0, null);
        }
    }

    /**
     * Método que carga un archivo de consultas
     * @param archConsultas Archivo de consultas
     * @param idCarga Identificador del registro de carga
     */
    private void cargarConsultas(File archConsultas, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbConsultas dbConsultas = new DbConsultas();
        String nomArch = obtenerNombreArchivo(archConsultas);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AC");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;

        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Consulta> listaConsultas;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archConsultas), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaConsultas = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 18) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String fechaCon = Utilidades.izquierda(arrCampos[4], 10);
                            String numAut = Utilidades.izquierda(arrCampos[5], 15);
                            String codCon = Utilidades.izquierda(arrCampos[6], 8).toUpperCase();
                            String finCon = Utilidades.izquierda(arrCampos[7], 2);
                            String cauExt = Utilidades.izquierda(arrCampos[8], 2);
                            String codDia = Utilidades.izquierda(arrCampos[9], 4).toUpperCase();
                            String diaRel1 = Utilidades.izquierda(arrCampos[10], 4).toUpperCase();
                            String diaRel2 = Utilidades.izquierda(arrCampos[11], 4).toUpperCase();
                            String diaRel3 = Utilidades.izquierda(arrCampos[12], 4).toUpperCase();
                            String tipDia = Utilidades.izquierda(arrCampos[13], 1);
                            double valCon;
                            try {
                                valCon = Double.parseDouble(Utilidades.izquierda(arrCampos[14], 15));
                            } catch (NumberFormatException ex) {
                                valCon = Double.NaN;
                            }
                            double valCuo;
                            try {
                                valCuo = Double.parseDouble(Utilidades.izquierda(arrCampos[15], 15));
                            } catch (NumberFormatException ex) {
                                valCuo = Double.NaN;
                            }
                            double valNet;
                            try {
                                valNet = Double.parseDouble(Utilidades.izquierda(arrCampos[16], 15));
                            } catch (NumberFormatException ex) {
                                valNet = Double.NaN;
                            }
                            
                            Consulta consultaAux = new Consulta(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipId, numId, fechaCon,
                                    numAut, codCon, finCon, cauExt, codDia, diaRel1, diaRel2, diaRel3, tipDia, valCon, valCuo, valNet, "", "");
                            
                            listaConsultas.add(consultaAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AC", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AC", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaConsultas.isEmpty()) {
                resultadoAux = dbConsultas.insertarRegistros(listaConsultas, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AC", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AC", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AC", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de procedimientos
     * @param archProcedimientos Archivo de procedimientos
     * @param idCarga Identificador del registro de carga
     */
    private void cargarProcedimientos(File archProcedimientos, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbProcedimientos dbProcedimientos = new DbProcedimientos();
        String nomArch = obtenerNombreArchivo(archProcedimientos);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AP");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;

        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Procedimiento> listaProcedimientos;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archProcedimientos), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaProcedimientos = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 16) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String fechaPro = Utilidades.izquierda(arrCampos[4], 10);
                            String numAut = Utilidades.izquierda(arrCampos[5], 15);
                            String codPro = Utilidades.izquierda(arrCampos[6], 8).toUpperCase();
                            String ambReaPro = Utilidades.izquierda(arrCampos[7], 1);
                            String finPro = Utilidades.izquierda(arrCampos[8], 1);
                            String perAti = Utilidades.izquierda(arrCampos[9], 1);
                            String diaPri = Utilidades.izquierda(arrCampos[10], 4).toUpperCase();
                            String diaRel = Utilidades.izquierda(arrCampos[11], 4).toUpperCase();
                            String diaCom = Utilidades.izquierda(arrCampos[12], 4).toUpperCase();
                            String forRea = Utilidades.izquierda(arrCampos[13], 1);
                            double valPro;
                            try {
                                valPro = Double.parseDouble(Utilidades.izquierda(arrCampos[14], 15));
                            } catch (NumberFormatException ex) {
                                valPro = Double.NaN;
                            }
                            
                            Procedimiento procedimientoAux = new Procedimiento(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipId, numId, fechaPro,
                                    numAut, codPro, ambReaPro, finPro, perAti, diaPri, diaRel, diaCom, forRea, valPro, "", Long.MIN_VALUE, "");
                            
                            listaProcedimientos.add(procedimientoAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AP", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AP", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaProcedimientos.isEmpty()) {
                resultadoAux = dbProcedimientos.insertarRegistros(listaProcedimientos, cargaArchDeta, false, "");
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AP", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AP", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AP", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de medicamentos
     * @param archMedicamentos Archivo de medicamentos
     * @param idCarga Identificador del registro de carga
     */
    private void cargarMedicamentos(File archMedicamentos, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbMedicamentos dbMedicamentos = new DbMedicamentos();
        String nomArch = obtenerNombreArchivo(archMedicamentos);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AM");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;

        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Medicamento> listaMedicamentos;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archMedicamentos), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaMedicamentos = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 15) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String numAut = Utilidades.izquierda(arrCampos[4], 15);
                            String codMed = Utilidades.izquierda(arrCampos[5], 20).toUpperCase();
                            String tipMed = Utilidades.izquierda(arrCampos[6], 1);
                            String nomMed = Utilidades.izquierda(arrCampos[7], 30);
                            String forFar = Utilidades.izquierda(arrCampos[8], 20);
                            String conMed = Utilidades.izquierda(arrCampos[9], 20);
                            String uniMed = Utilidades.izquierda(arrCampos[10], 20);
                            int numUni;
                            try {
                                numUni = Integer.parseInt(Utilidades.izquierda(arrCampos[11], 5));
                            } catch (NumberFormatException ex) {
                                numUni = Integer.MIN_VALUE;
                            }
                            double valUniMed;
                            try {
                                valUniMed = Double.parseDouble(Utilidades.izquierda(arrCampos[12], 15));
                            } catch (NumberFormatException ex) {
                                valUniMed = Double.NaN;
                            }
                            double valTotMed;
                            try {
                                valTotMed = Double.parseDouble(Utilidades.izquierda(arrCampos[13], 15));
                            } catch (NumberFormatException ex) {
                                valTotMed = Double.NaN;
                            }
                            
                            Medicamento medicamentoAux = new Medicamento(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipId, numId,
                                    numAut, codMed, tipMed, nomMed, forFar, conMed, uniMed, numUni, valUniMed, valTotMed, "");
                            
                            listaMedicamentos.add(medicamentoAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AM", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AM", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaMedicamentos.isEmpty()) {
                resultadoAux = dbMedicamentos.insertarRegistros(listaMedicamentos, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AM", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AM", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AM", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de otros servicios
     * @param archOtrosServicios Archivo de otros servicios
     * @param idCarga Identificador del registro de carga
     */
    private void cargarOtrosServicios(File archOtrosServicios, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbOtrosServicios dbOtrosServicios = new DbOtrosServicios();
        String nomArch = obtenerNombreArchivo(archOtrosServicios);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AT");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;
        
        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<OtroServicio> listaOtroServicios;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archOtrosServicios), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaOtroServicios = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 12) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String numAut = Utilidades.izquierda(arrCampos[4], 15);
                            String tipServ = Utilidades.izquierda(arrCampos[5], 1);
                            String codServ = Utilidades.izquierda(arrCampos[6], 20);
                            String nomServ = Utilidades.izquierda(arrCampos[7], 60).toUpperCase();
                            int cantidad;
                            try {
                                cantidad = Integer.parseInt(Utilidades.izquierda(arrCampos[8], 5));
                            } catch (NumberFormatException ex) {
                                cantidad = Integer.MIN_VALUE;
                            }
                            double valUniServ;
                            try {
                                valUniServ = Double.parseDouble(Utilidades.izquierda(arrCampos[9], 15));
                            } catch (NumberFormatException ex) {
                                valUniServ = Double.NaN;
                            }
                            double valTotServ;
                            try {
                                valTotServ = Double.parseDouble(Utilidades.izquierda(arrCampos[10], 15));
                            } catch (NumberFormatException ex) {
                                valTotServ = Double.NaN;
                            }
                            
                            OtroServicio otroServicioAux = new OtroServicio(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipId, numId,
                                    numAut, tipServ, codServ, nomServ, cantidad, valUniServ, valTotServ, "");
                            
                            listaOtroServicios.add(otroServicioAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AT", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AT", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaOtroServicios.isEmpty()) {
                resultadoAux = dbOtrosServicios.insertarRegistros(listaOtroServicios, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AT", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AT", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AT", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de hospitalización
     * @param archHospitalizacion Archivo de hospitalización
     * @param idCarga Identificador del registro de carga
     */
    private void cargarHospitalizacion(File archHospitalizacion, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbHospitalizacion dbHospitalizacion = new DbHospitalizacion();
        String nomArch = obtenerNombreArchivo(archHospitalizacion);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AH");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;
	
        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Hospitalizacion> listaHospitalizaciones;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archHospitalizacion), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaHospitalizaciones = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 20) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String viaIng = Utilidades.izquierda(arrCampos[4], 1);
                            String fechaIng = Utilidades.izquierda(arrCampos[5], 10);
                            String horaIng = Utilidades.izquierda(arrCampos[6], 5);
                            String numAut = Utilidades.izquierda(arrCampos[7], 15);
                            String cauExt = Utilidades.izquierda(arrCampos[8], 2);
                            String diaIng = Utilidades.izquierda(arrCampos[9], 4).toUpperCase();
                            String diaEgr = Utilidades.izquierda(arrCampos[10], 4).toUpperCase();
                            String diaRel1 = Utilidades.izquierda(arrCampos[11], 4).toUpperCase();
                            String diaRel2 = Utilidades.izquierda(arrCampos[12], 4).toUpperCase();
                            String diaRel3 = Utilidades.izquierda(arrCampos[13], 4).toUpperCase();
                            String diaCom = Utilidades.izquierda(arrCampos[14], 4).toUpperCase();
                            String estSal = Utilidades.izquierda(arrCampos[15], 1);
                            String diaMue = Utilidades.izquierda(arrCampos[16], 4).toUpperCase();
                            String fechaEgr = Utilidades.izquierda(arrCampos[17], 10);
                            String horaEgr = Utilidades.izquierda(arrCampos[18], 5);
                            
                            Hospitalizacion hospitalizacionAux = new Hospitalizacion(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipId, numId, viaIng,
                                    fechaIng, horaIng, numAut, cauExt, diaIng, diaEgr, diaRel1, diaRel2, diaRel3, diaCom, estSal, diaMue, fechaEgr, horaEgr, "");
                            
                            listaHospitalizaciones.add(hospitalizacionAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AH", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AH", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaHospitalizaciones.isEmpty()) {
                resultadoAux = dbHospitalizacion.insertarRegistros(listaHospitalizaciones, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AH", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AH", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AH", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de urgencias
     * @param archUrgencias Archivo de urgencias
     * @param idCarga Identificador del registro de carga
     */
    private void cargarUrgencias(File archUrgencias, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbUrgencias dbUrgencias = new DbUrgencias();
        String nomArch = obtenerNombreArchivo(archUrgencias);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AU");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;
	
        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Urgencia> listaUrgencias;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archUrgencias), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaUrgencias = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 18) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String fechaIng = Utilidades.izquierda(arrCampos[4], 10);
                            String horaIng = Utilidades.izquierda(arrCampos[5], 5);
                            String numAut = Utilidades.izquierda(arrCampos[6], 15);
                            String cauExt = Utilidades.izquierda(arrCampos[7], 2);
                            String diaSal = Utilidades.izquierda(arrCampos[8], 4).toUpperCase();
                            String diaRel1 = Utilidades.izquierda(arrCampos[9], 4).toUpperCase();
                            String diaRel2 = Utilidades.izquierda(arrCampos[10], 4).toUpperCase();
                            String diaRel3 = Utilidades.izquierda(arrCampos[11], 4).toUpperCase();
                            String desUsu = Utilidades.izquierda(arrCampos[12], 1);
                            String estSal = Utilidades.izquierda(arrCampos[13], 1);
                            String cauMue = Utilidades.izquierda(arrCampos[14], 4).toUpperCase();
                            String fechaSal = Utilidades.izquierda(arrCampos[15], 10);
                            String horaSal = Utilidades.izquierda(arrCampos[16], 5);
                            
                            Urgencia urgenciaAux = new Urgencia(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipId, numId, fechaIng, horaIng,
                                    numAut, cauExt, diaSal, diaRel1, diaRel2, diaRel3, desUsu, estSal, cauMue, fechaSal, horaSal, "");
                            
                            listaUrgencias.add(urgenciaAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AU", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AU", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaUrgencias.isEmpty()) {
                resultadoAux = dbUrgencias.insertarRegistros(listaUrgencias, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AU", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AU", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AU", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de nacidos
     * @param archNacidos Archivo de nacidos
     * @param idCarga Identificador del registro de carga
     */
    private void cargarNacidos(File archNacidos, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbNacidos dbNacidos = new DbNacidos();
        String nomArch = obtenerNombreArchivo(archNacidos);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AN");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;
		
        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Nacido> listaNacidos;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archNacidos), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaNacidos = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 15) {
                            String numFac = Utilidades.izquierda(arrCampos[0], 20);
                            String codEntPre = Utilidades.izquierda(arrCampos[1], 12);
                            String tipIdMad = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numIdMad = Utilidades.izquierda(arrCampos[3], 20);
                            String fechaNac = Utilidades.izquierda(arrCampos[4], 10);
                            String horaNac = Utilidades.izquierda(arrCampos[5], 5);
                            int edadGes;
                            try {
                                edadGes = Integer.parseInt(Utilidades.izquierda(arrCampos[6], 5));
                            } catch (NumberFormatException ex) {
                                edadGes = Integer.MIN_VALUE;
                            }
                            String conPre = Utilidades.izquierda(arrCampos[7], 1);
                            String sexo = Utilidades.izquierda(arrCampos[8], 1).toUpperCase();
                            int peso;
                            try {
                                peso = Integer.parseInt(Utilidades.izquierda(arrCampos[9], 5));
                            } catch (NumberFormatException ex) {
                                peso = Integer.MIN_VALUE;
                            }
                            String diagNac = Utilidades.izquierda(arrCampos[10], 4).toUpperCase();
                            String cauMue = Utilidades.izquierda(arrCampos[11], 4).toUpperCase();
                            String fechaMue = Utilidades.izquierda(arrCampos[12], 10);
                            String horaMue = Utilidades.izquierda(arrCampos[13], 5);
                            
                            Nacido nacidoAux = new Nacido(0, idCargaDeta, contAux, 0, numFac, codEntPre, tipIdMad, numIdMad,
                                    fechaNac, horaNac, edadGes, conPre, sexo, peso, diagNac, cauMue, fechaMue, horaMue, "");
                            
                            listaNacidos.add(nacidoAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AN", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AN", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaNacidos.isEmpty()) {
                resultadoAux = dbNacidos.insertarRegistros(listaNacidos, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AN", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AN", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AN", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo de facturas
     * @param archFacturas Archivo de facturas
     * @param idCarga Identificador del registro de carga
     */
    private void cargarFacturas(File archFacturas, long idCarga) {
        DbCargaArch dbCargaArch = new DbCargaArch();
        DbFacturas dbFacturas = new DbFacturas();
        String nomArch = obtenerNombreArchivo(archFacturas);
        
        //Se verifica si se debe cargar el archivo y en que registro iniciar
        CargaArchDeta cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, "AF");
        long idCargaDeta = cargaArchDeta.getIdCargaDeta();
        long numRegIni = cargaArchDeta.getNumRegistros() + 1;
		
        if (cargaArchDeta.getIdEstado() != 1 && cargaArchDeta.getIdEstado() != 5) {
            //El archivo no está pendiente de carga, no se debe continuar
            return;
        }
        
        try {
            boolean interrumpido;
            ArrayList<Factura> listaFacturas;
            
            //Se abre el archivo
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archFacturas), this.charSet))) {
                String linea;
                interrumpido = false;
                int contAux = 1;
                int contErrEst = 0;
                listaFacturas = new ArrayList<>();
                while ((linea = br.readLine()) != null && !interrumpido) {
                    if (contAux >= numRegIni) {
                        String[] arrCampos = (linea + ",0").split(",");
                        if (arrCampos.length == 18) {
                            String codEntPre = Utilidades.izquierda(arrCampos[0], 12);
                            String nomEntPre = Utilidades.izquierda(arrCampos[1], 60);
                            String tipId = Utilidades.izquierda(arrCampos[2], 2).toUpperCase();
                            String numId = Utilidades.izquierda(arrCampos[3], 20);
                            String numFac = Utilidades.izquierda(arrCampos[4], 20);
                            String fechaExp = Utilidades.izquierda(arrCampos[5], 10);
                            String fechaIni = Utilidades.izquierda(arrCampos[6], 10);
                            String fechaFin = Utilidades.izquierda(arrCampos[7], 10);
                            String codEntAdm = Utilidades.izquierda(arrCampos[8], 6);
                            String nomEntAdm = Utilidades.izquierda(arrCampos[9], 30);
                            String numCon = Utilidades.izquierda(arrCampos[10], 15);
                            String plaBen = Utilidades.izquierda(arrCampos[11], 30);
                            String numPol = Utilidades.izquierda(arrCampos[12], 10);
                            double valCop;
                            try {
                                valCop = Double.parseDouble(Utilidades.izquierda(arrCampos[13], 15));
                            } catch (NumberFormatException ex) {
                                valCop = Double.NaN;
                            }
                            double valCom;
                            try {
                                valCom = Double.parseDouble(Utilidades.izquierda(arrCampos[14], 15));
                            } catch (NumberFormatException ex) {
                                valCom = Double.NaN;
                            }
                            double valDes;
                            try {
                                valDes = Double.parseDouble(Utilidades.izquierda(arrCampos[15], 15));
                            } catch (NumberFormatException ex) {
                                valDes = Double.NaN;
                            }
                            double valNet;
                            try {
                                valNet = Double.parseDouble(Utilidades.izquierda(arrCampos[16], 15));
                            } catch (NumberFormatException ex) {
                                valNet = Double.NaN;
                            }
                            
                            Factura facturaAux = new Factura(0, idCargaDeta, contAux, codEntPre, nomEntPre, tipId, numId, numFac, fechaExp,
                                    fechaIni, fechaFin, codEntAdm, nomEntAdm, numCon, plaBen, numPol, valCop, valCom, valDes, valNet, "");
                            
                            listaFacturas.add(facturaAux);
                        } else {
                            contErrEst++;
                            
                            //Se inserta error de estructura incorrecta
                            this.dbErrores.insertarRegistro("E01", idCarga, idCargaDeta, contAux, "AF", 0, null);
                            
                            if (contErrEst >= 3) {
                                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AF", 0, 5);
                                interrumpido = true;
                                break;
                            }
                        }
                    }
                    
                    contAux++;
                }
            }
            
            //Se insertan los registros sin errores de estructura
            int resultadoAux = 1;
            if (!listaFacturas.isEmpty()) {
                resultadoAux = dbFacturas.insertarRegistros(listaFacturas, cargaArchDeta);
            }
            
            if (resultadoAux > 0 && !interrumpido) {
                //Se marca el archivo como cargado
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AF", 0, 2);
            } else {
                //Se marca el archivo con errores de estructura
                dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, "AF", 0, 5);
            }
        } catch (IOException e) {
            //Se inserta error en el archivo plano
            this.dbErrores.insertarRegistro("E02", idCarga, idCargaDeta, 0, "AF", 0, null);
        }
    }
    
    /**
     * Método que carga un archivo del Microsoft Excel
     * @param archXLSX Archivo a cargar
     * @param idCarga Identificador del registro de carga
     */
    private void cargarXLSX(File archXLSX, long idCarga) {
        String nomArch = obtenerNombreArchivo(archXLSX);
        DbCargaArch dbCargaArch = new DbCargaArch();
        try (FileInputStream excelFile = new FileInputStream(archXLSX)) {
            Workbook workbook = new XSSFWorkbook(excelFile);
            
            Iterator<Sheet> hojaIt = workbook.sheetIterator();
            
            //Indicador de archivo identificado para carga
            boolean indIdentificado = false;
            
            while (hojaIt.hasNext()) {
                Sheet hoja = hojaIt.next();
                
                CargaArchDeta cargaArchDeta = null;
                long idCargaDeta = -1;
                long numRegIni = -1;
                int contAux = 1;
                
                String codTabla = "";
                boolean indColsIdent = false;
                HashMap<Integer, String> mapaCols = null;
                
                String nombreAux = hoja.getSheetName().toLowerCase();
                if (nombreAux.contains("consulta")) {
                    codTabla = "AC";
                } else if (nombreAux.contains("procedimiento")) {
                    codTabla = "AP";
                } else if (nombreAux.contains("otro") && nombreAux.contains("servicio")) {
                    codTabla = "AT";
                } else if (nombreAux.contains("usuario")) {
                    codTabla = "US";
                }
                
                ArrayList<Consulta> listaConsultas = new ArrayList<>();
                ArrayList<Procedimiento> listaProcedimientos = new ArrayList<>();
                ArrayList<OtroServicio> listaOtrosServicios = new ArrayList<>();
                ArrayList<Usuario> listaUsuarios = new ArrayList<>();
                
                Iterator<Row> filaIt = hoja.iterator();
                
                boolean indContinuarFilas = true;
                while (filaIt.hasNext() && indContinuarFilas) {
                    Row fila = filaIt.next();
                    Iterator<Cell> celdaIt = fila.iterator();
                    
                    if (codTabla.equals("")) {
                        //Se trata de determinar el tipo de archivo a cargar
                        while (celdaIt.hasNext()) {
                            Cell celda = celdaIt.next();
                            if (celda.getCellTypeEnum() == CellType.STRING) {
                                nombreAux = celda.getStringCellValue().toLowerCase();
                                if (nombreAux.contains("consulta")) {
                                    codTabla = "AC";
                                    break;
                                } else if (nombreAux.contains("procedimiento")) {
                                    codTabla = "AP";
                                    break;
                                } else if (nombreAux.contains("servicio")) {
                                    codTabla = "AT";
                                    break;
                                } else if (nombreAux.contains("apellido") || nombreAux.contains("sexo")) {
                                    codTabla = "US";
                                    break;
                                }
                            }
                        }
                        
                        if (!codTabla.equals("")) {
                            //Se asignan las columnas a los campos
                            mapaCols = this.asignarCamposXLSX(fila, codTabla);
                            if (mapaCols.size() >= 8) {
                                indColsIdent = true;
                                cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, codTabla);
                                idCargaDeta = cargaArchDeta.getIdCargaDeta();
                                numRegIni = cargaArchDeta.getNumRegistros() + 1;
                            }
                        }
                    } else if (!indColsIdent) {
                        //Se asignan las columnas a los campos
                        mapaCols = this.asignarCamposXLSX(fila, codTabla);
                        if (mapaCols.size() >= 8) {
                            indColsIdent = true;
                            cargaArchDeta = this.obtenerDetalleCarga(idCarga, nomArch, codTabla);
                            idCargaDeta = cargaArchDeta.getIdCargaDeta();
                            numRegIni = cargaArchDeta.getNumRegistros() + 1;
                        }
                    } else {
                        if (contAux >= numRegIni) {
                            switch (codTabla) {
                                case "AC":
                                    Consulta consultaObj = new Consulta(celdaIt, mapaCols);
                                    if (consultaObj.isIndValores()) {
                                        listaConsultas.add(consultaObj);
                                    } else {
                                        indContinuarFilas = false;
                                    }
                                    break;
                                    
                                case "AP":
                                    Procedimiento procedimentoObj = new Procedimiento(celdaIt, mapaCols);
                                    if (procedimentoObj.isIndValores()) {
                                        listaProcedimientos.add(procedimentoObj);
                                    } else {
                                        indContinuarFilas = false;
                                    }
                                    break;
                                    
                                case "AT":
                                    OtroServicio otroServicioObj = new OtroServicio(celdaIt, mapaCols);
                                    if (otroServicioObj.isIndValores()) {
                                        listaOtrosServicios.add(otroServicioObj);
                                    } else {
                                        indContinuarFilas = false;
                                    }
                                    break;
                                    
                                case "US":
                                    Usuario usuarioObj = new Usuario(celdaIt, mapaCols);
                                    if (usuarioObj.isIndValores()) {
                                        listaUsuarios.add(usuarioObj);
                                    } else {
                                        indContinuarFilas = false;
                                    }
                                    break;
                            }
                        }
                        contAux++;
                    }
                }
                
                if (cargaArchDeta != null) {
                    indIdentificado = true;
                    
                    //Se agregan los campos obtenidos a la base de datos
                    boolean indCargado = false;
                    switch (codTabla) {
                        case "AC":
                            //Se insertan los registros
                            if (!listaConsultas.isEmpty()) {
                                DbConsultas dbConsultas = new DbConsultas();
                                int resultadoAux = dbConsultas.insertarRegistros(listaConsultas, cargaArchDeta);
                                indCargado = (resultadoAux >= 0);
                            } else {
                                indCargado = true;
                            }
                            break;
                            
                        case "AP":
                            //Se insertan los registros
                            if (!listaProcedimientos.isEmpty()) {
                                DbProcedimientos dbProcedimientos = new DbProcedimientos();
                                int resultadoAux = dbProcedimientos.insertarRegistros(listaProcedimientos, cargaArchDeta, false, "");
                                indCargado = (resultadoAux >= 0);
                            } else {
                                indCargado = true;
                            }
                            break;
                            
                        case "AT":
                            //Se insertan los registros
                            if (!listaOtrosServicios.isEmpty()) {
                                DbOtrosServicios dbOtrosServicios = new DbOtrosServicios();
                                int resultadoAux = dbOtrosServicios.insertarRegistros(listaOtrosServicios, cargaArchDeta);
                                indCargado = (resultadoAux >= 0);
                            } else {
                                indCargado = true;
                            }
                            break;
                            
                        case "US":
                            //Se insertan los registros
                            if (!listaUsuarios.isEmpty()) {
                                DbUsuarios dbUsuarios = new DbUsuarios();
                                int resultadoAux = dbUsuarios.insertarRegistros(listaUsuarios, cargaArchDeta);
                                indCargado = (resultadoAux >= 0);
                            } else {
                                indCargado = true;
                            }
                            break;
                    }
                    
                    if (indCargado) {
                        //Se marca el archivo como cargado
                        dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, codTabla, 0, 2);
                    } else {
                        //Se marca el archivo con errores de estructura
                        dbCargaArch.crearActualizarRegistroDeta(idCargaDeta, idCarga, nomArch, codTabla, 0, 5);
                    }
                }
            }
            
            if (!indIdentificado) {
                //No se identificó el archivo
                String nomXLSX = obtenerNombreArchivo(archXLSX);
                dbCargaArch.crearActualizarRegistroDeta(0, this.idCarga, nomXLSX, "NN", 0, 7);
            }
        } catch (NotOfficeXmlFileException ex) {
            //Archivo de Excel en formato xls con extensión xlsx
            String nomXLSX = obtenerNombreArchivo(archXLSX);
            dbCargaArch.crearActualizarRegistroDeta(0, this.idCarga, nomXLSX, "NN", 0, 6);
        } catch (IOException ex) {
            this.dbErrores.insertarRegistro("E02", idCarga, 0, 0, "XL", 0, null);
        }
    }
    
    private HashMap<Integer, String> asignarCamposXLSX(Row fila, String codTabla) {
        HashMap<Integer, String> mapaCampos = new HashMap<>();
        Iterator<Cell> celdaIt = fila.iterator();
        while (celdaIt.hasNext()) {
            Cell celdaAux = celdaIt.next();
            
            if (celdaAux.getCellTypeEnum() == CellType.STRING) {
                String nombreAux = celdaAux.getStringCellValue().toLowerCase();
                String campoAux = "";
                switch (codTabla) {
                    case "AC":
                        if (nombreAux.contains("factura")) {
                            campoAux = "num_fac";
                        } else if (nombreAux.contains("prestad")) {
                            campoAux = "cod_ent_pre";
                        } else if (nombreAux.contains("tipo") && nombreAux.contains("identif")) {
                            campoAux = "tip_id";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("identif")) {
                            campoAux = "num_id";
                        } else if (nombreAux.contains("fecha")) {
                            campoAux = "fecha_con";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("autori")) {
                            campoAux = "num_aut";
                        } else if (nombreAux.contains("digo") && nombreAux.contains("consul")) {
                            campoAux = "cod_con";
                        } else if (nombreAux.contains("final") && nombreAux.contains("consul")) {
                            campoAux = "fin_con";
                        } else if (nombreAux.contains("causa") && nombreAux.contains("ext")) {
                            campoAux = "cau_ext";
                        } else if (nombreAux.contains("diag") || nombreAux.contains("dx")) {
                            if (nombreAux.contains("princ") || nombreAux.contains("ppal")) {
                                if (nombreAux.contains("tipo")) {
                                    campoAux = "tip_dia";
                                } else {
                                    campoAux = "cod_dia";
                                }
                            } else if (nombreAux.contains("rel")){
                                if (nombreAux.contains("1")) {
                                    campoAux = "dia_rel1";
                                } else if (nombreAux.contains("2")) {
                                    campoAux = "dia_rel2";
                                } else if (nombreAux.contains("3")) {
                                    campoAux = "dia_rel3";
                                }
                            }
                        } else if (nombreAux.contains("valor")) {
                            if (nombreAux.contains("consul")) {
                                campoAux = "val_con";
                            } else if (nombreAux.contains("cuota")) {
                                campoAux = "val_cuo";
                            } else if (nombreAux.contains("neto")) {
                                campoAux = "val_net";
                            }
                        }
                        
                        if (!campoAux.equals("")) {
                            mapaCampos.put(celdaAux.getColumnIndex(), campoAux);
                        }
                        break;
                        
                    case "AP":
                        if (nombreAux.contains("factura")) {
                            campoAux = "num_fac";
                        } else if (nombreAux.contains("prestad")) {
                            campoAux = "cod_ent_pre";
                        } else if (nombreAux.contains("tipo") && nombreAux.contains("identif")) {
                            campoAux = "tip_id";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("identif")) {
                            campoAux = "num_id";
                        } else if (nombreAux.contains("fecha")) {
                            campoAux = "fecha_pro";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("autori")) {
                            campoAux = "num_aut";
                        } else if (nombreAux.contains("digo") && nombreAux.contains("proced")) {
                            campoAux = "cod_pro";
                        } else if (nombreAux.contains("mbito") && nombreAux.contains("reali")) {
                            campoAux = "amb_rea_pro";
                        } else if (nombreAux.contains("final") && nombreAux.contains("proced")) {
                            campoAux = "fin_pro";
                        } else if (nombreAux.contains("person") && nombreAux.contains("atie")) {
                            campoAux = "per_ati";
                        } else if (nombreAux.contains("diag") || nombreAux.contains("dx")) {
                            if (nombreAux.contains("princ") || nombreAux.contains("ppal")) {
                                campoAux = "dia_pri";
                            } else if (nombreAux.contains("rel")){
                                campoAux = "dia_rel";
                            }
                        } else if (nombreAux.contains("complic")){
                            campoAux = "dia_com";
                        } else if (nombreAux.contains("forma") && nombreAux.contains("reali")) {
                            campoAux = "for_rea";
                        } else if (nombreAux.contains("valor") && nombreAux.contains("proced")) {
                            campoAux = "val_pro";
                        }
                        
                        if (!campoAux.equals("")) {
                            mapaCampos.put(celdaAux.getColumnIndex(), campoAux);
                        }
                        break;
                        
                    case "AT":
                        if (nombreAux.contains("factura")) {
                            campoAux = "num_fac";
                        } else if (nombreAux.contains("prestad")) {
                            campoAux = "cod_ent_pre";
                        } else if (nombreAux.contains("tipo") && nombreAux.contains("identif")) {
                            campoAux = "tip_id";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("identif")) {
                            campoAux = "num_id";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("autori")) {
                            campoAux = "num_aut";
                        } else if (nombreAux.contains("tipo") && nombreAux.contains("serv")) {
                            campoAux = "tip_serv";
                        } else if (nombreAux.contains("digo") && nombreAux.contains("serv")) {
                            campoAux = "cod_serv";
                        } else if (nombreAux.contains("nomb") && nombreAux.contains("serv")) {
                            campoAux = "nom_serv";
                        } else if (nombreAux.contains("cantidad")){
                            campoAux = "cantidad";
                        } else if (nombreAux.contains("valor") && nombreAux.contains("uni")) {
                            campoAux = "val_uni_serv";
                        } else if (nombreAux.contains("valor") && nombreAux.contains("total")) {
                            campoAux = "val_tot_serv";
                        }
                        
                        if (!campoAux.equals("")) {
                            mapaCampos.put(celdaAux.getColumnIndex(), campoAux);
                        }
                        break;
                        
                    case "US":
                        if (nombreAux.contains("tipo") && nombreAux.contains("identif")) {
                            campoAux = "tip_id";
                        } else if (nombreAux.contains("mero") && nombreAux.contains("identif")) {
                            campoAux = "num_id";
                        } else if (nombreAux.contains("entid") && nombreAux.contains("admin")) {
                            campoAux = "cod_ent_adm";
                        } else if (nombreAux.contains("tipo") && nombreAux.contains("usua")) {
                            campoAux = "tip_usu";
                        } else if (nombreAux.contains("apellido") && (nombreAux.contains("primer") || nombreAux.contains("1"))) {
                            campoAux = "ape_1";
                        } else if (nombreAux.contains("apellido") && (nombreAux.contains("segun") || nombreAux.contains("2"))) {
                            campoAux = "ape_2";
                        } else if (nombreAux.contains("nombre") && (nombreAux.contains("primer") || nombreAux.contains("1"))) {
                            campoAux = "nom_1";
                        } else if (nombreAux.contains("nombre") && (nombreAux.contains("segun") || nombreAux.contains("2"))) {
                            campoAux = "nom_2";
                        } else if (nombreAux.contains("edad") && !nombreAux.contains("unidad")) {
                            campoAux = "edad";
                        } else if (nombreAux.contains("edad") && nombreAux.contains("unidad")) {
                            campoAux = "uni_edad";
                        } else if (nombreAux.contains("sexo")){
                            campoAux = "sexo";
                        } else if (nombreAux.contains("digo") && nombreAux.contains("depart")) {
                            campoAux = "cod_dep";
                        } else if (nombreAux.contains("digo") && nombreAux.contains("munic")) {
                            campoAux = "cod_mun";
                        } else if (nombreAux.contains("zona")) {
                            campoAux = "zona";
                        }
                        
                        if (!campoAux.equals("")) {
                            mapaCampos.put(celdaAux.getColumnIndex(), campoAux);
                        }
                        break;
                }
            }
        }
        
        return mapaCampos;
    }
    
}
