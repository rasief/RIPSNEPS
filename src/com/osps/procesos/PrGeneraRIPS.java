package com.osps.procesos;

import com.osps.db.DbCargaArch;
import com.osps.db.DbConsultas;
import com.osps.db.DbHospitalizacion;
import com.osps.db.DbMedicamentos;
import com.osps.db.DbNacidos;
import com.osps.db.DbOtrosServicios;
import com.osps.db.DbProcedimientos;
import com.osps.db.DbUrgencias;
import com.osps.db.DbUsuarios;
import com.osps.entidad.CargaArch;
import com.osps.entidad.Consulta;
import com.osps.entidad.Grupo;
import com.osps.entidad.IntervaloFecha;
import com.osps.entidad.Hospitalizacion;
import com.osps.entidad.Medicamento;
import com.osps.entidad.Nacido;
import com.osps.entidad.OtroServicio;
import com.osps.entidad.Procedimiento;
import com.osps.entidad.Urgencia;
import com.osps.entidad.Usuario;
import com.osps.utilidades.Utilidades;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Clase para la generación del archivo xlsx de morbilidad por causas
 * @author Feisar Moreno
 * @date 12/12/2012
 */
public class PrGeneraRIPS {
    private final CargaArch cargaArch;
    private String nombreBase;
    
    public PrGeneraRIPS(CargaArch cargaArch) {
        this.cargaArch = cargaArch;
    }
    
    /**
     * Método que genera el archivo de Excel de Morbilidad por Causas
     * @param nombreCarpeta Nombre de la carpeta en la que se generarán los archivos
     * @throws IOException
     */
    public void generarRIPS(String nombreCarpeta) throws IOException {
        int anio = cargaArch.getAnio();
        int mes = cargaArch.getMes();
        
        if (!nombreCarpeta.substring(nombreCarpeta.length() - 1, nombreCarpeta.length()).equals(File.separator) ) {
            nombreCarpeta += File.separator;
        }
        
        for (int idGrupo = 1; idGrupo <= 2; idGrupo++) {
            //Se construye el nombre del archivo de salida
            nombreBase = nombreCarpeta + "RIPS_" + anio + "_" + Utilidades.derecha("00" + mes, 2);
            if (idGrupo == 1) {
                nombreBase += "_pgp_integral_";
            } else {
                nombreBase += "_pgp_municipios_";
            }
            
            //Se crean los diferentes archivos
            boolean indSinGrupo = idGrupo == 1;
            this.generarUsuarios(idGrupo, indSinGrupo);
            this.generarConsultas(idGrupo, indSinGrupo);
            this.generarProcedimientos(idGrupo, indSinGrupo);
            this.generarMedicamentos(idGrupo, indSinGrupo);
            this.generarOtrosServicios(idGrupo, indSinGrupo);
            this.generarNacidos(idGrupo, indSinGrupo);
            this.generarHospitalizacion(idGrupo, indSinGrupo);
            this.generarUrgencias(idGrupo, indSinGrupo);
        }
    }
    
    private void generarUsuarios(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de usuarios
        DbUsuarios dbUsuarios = new DbUsuarios();
        ArrayList<Usuario> listaUsuarios = dbUsuarios.getListaUsuarios(idCarga, idGrupo, indSinGrupo);
        if (!listaUsuarios.isEmpty()) {
            String nombreArchivo = nombreBase + "US.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Tipo identificación;Número identificación;Código administradora;Tipo usuario;Primer apellido;Segundo apellido;" +
                        "Primer nombre;Segundo nombre;Edad;Unidad medida edad;Sexo;Departamento residencia;Municipio residencia;Zona residencia;Sede;Observaciones");

                listaUsuarios.stream().map((usuarioAux) -> {
                    String filaAux =
                            "\"" + usuarioAux.getTipId() + "\";" +
                            "=\"" + usuarioAux.getNumId() + "\";" +
                            "\"" + ajustarValor(usuarioAux.getCodEntAdm()) + "\";";
                    if (usuarioAux.getTipUsu() != null) {
                        filaAux +=
                                "\"" + ajustarValor(usuarioAux.getTipUsu()) + "\";" +
                                "\"" + ajustarValor(usuarioAux.getApe1()) + "\";" +
                                "\"" + ajustarValor(usuarioAux.getApe2()) + "\";" +
                                "\"" + ajustarValor(usuarioAux.getNom1()) + "\";" +
                                "\"" + ajustarValor(usuarioAux.getNom2()) + "\";" +
                                ajustarValor(usuarioAux.getEdad()) + ";" +
                                ajustarValor(usuarioAux.getUniEdad()) + ";" +
                                "\"" + ajustarValor(usuarioAux.getSexo()) + "\";" +
                                "=\"" + ajustarValor(usuarioAux.getCodDep()) + "\";" +
                                "=\"" + ajustarValor(usuarioAux.getCodMun()) + "\";" +
                                "\"" + ajustarValor(usuarioAux.getNombreSede()) + "\";" +
                                "\"" + ajustarValor(usuarioAux.getZona()) + "\";";
                    } else {
                        filaAux += ";;;;;;;;;;;;";
                    }
                    filaAux += "\"" + ajustarValor(usuarioAux.getObservaciones()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarConsultas(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de consultas
        DbConsultas dbConsultas = new DbConsultas();
        ArrayList<Consulta> listaConsultas = dbConsultas.getListaConsultas(idCarga, idGrupo, indSinGrupo);
        if (!listaConsultas.isEmpty()) {
            String nombreArchivo = nombreBase + "AC.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación;Número identificación;Fecha consulta;Número autorización;" +
                        "Código consulta;Finalidad consulta;Causa externa;Diag. principal;Diag. rel. 1;Diag. rel. 2;Diag. rel. 3;Tipo diag. principal;" +
                        "Valor consulta;Valor cuota moderadora;Valor neto;Tipo internación;Número factura original;Sede;Observaciones;Observaciones usuario");
                
                listaConsultas.stream().map((consultaAux) -> {
                    String filaAux =
                            "\"" + consultaAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(consultaAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(consultaAux.getCodEntPre()) + "\";" +
                            "\"" + consultaAux.getTipId() + "\";" +
                            "=\"" + consultaAux.getNumId() + "\";" +
                            "=\"" + ajustarValor(consultaAux.getFechaCon()) + "\";" +
                            "=\"" + ajustarValor(consultaAux.getNumAut()) + "\";" +
                            "=\"" + ajustarValor(consultaAux.getCodCon()) + "\";" +
                            "=\"" + ajustarValor(consultaAux.getFinCon()) + "\";" +
                            "=\"" + ajustarValor(consultaAux.getCauExt()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getCodDia()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getDiaRel1()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getDiaRel2()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getDiaRel3()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getTipDia()) + "\";" +
                            ajustarValor(consultaAux.getValCon()) + ";" +
                            ajustarValor(consultaAux.getValCuo()) + ";" +
                            ajustarValor(consultaAux.getValNet()) + ";" +
                            "\"" + ajustarValor(consultaAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(consultaAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(consultaAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarProcedimientos(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de procedimientos
        DbProcedimientos dbProcedimientos = new DbProcedimientos();
        ArrayList<Procedimiento> listaProcedimientos = dbProcedimientos.getListaProcedimientos(idCarga, idGrupo, indSinGrupo);
        if (!listaProcedimientos.isEmpty()) {
            String nombreArchivo = nombreBase + "AP.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación;Número identificación;Fecha procedimiento;Número autorización;" +
                        "Código procedimiento;Ámbito realización;Finalidad procedimiento;Personal atiende;Diag. principal;Diag. relacionado;Complicación;" +
                        "Forma realización;Valor procedimiento;Tipo internación;Número factura original;Sede;Observaciones;Observaciones usuario");
                
                listaProcedimientos.stream().map((procedimientoAux) -> {
                    String filaAux =
                            "\"" + procedimientoAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(procedimientoAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(procedimientoAux.getCodEntPre()) + "\";" +
                            "\"" + procedimientoAux.getTipId() + "\";" +
                            "=\"" + procedimientoAux.getNumId() + "\";" +
                            "=\"" + ajustarValor(procedimientoAux.getFechaPro()) + "\";" +
                            "=\"" + ajustarValor(procedimientoAux.getNumAut()) + "\";" +
                            "=\"" + ajustarValor(procedimientoAux.getCodPro()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getAmbReaPro()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getFinPro()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getPerAti()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getDiaPri()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getDiaRel()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getDiaCom()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getForRea()) + "\";" +
                            ajustarValor(procedimientoAux.getValPro()) + ";" +
                            "\"" + ajustarValor(procedimientoAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(procedimientoAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(procedimientoAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarMedicamentos(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de medicamentos
        DbMedicamentos dbMedicamentos = new DbMedicamentos();
        ArrayList<Medicamento> listaMedicamentos = dbMedicamentos.getListaMedicamentos(idCarga, idGrupo, indSinGrupo);
        if (!listaMedicamentos.isEmpty()) {
            String nombreArchivo = nombreBase + "AM.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación;Número identificación;Número autorización;Código medicamento;" +
                        "Tipo medicamento;Nombre genérico;Forma farmacéutica;Concentración;Unidad medida;Número unidades;Valor unitario;Valor total;" +
                        "Tipo internación;Número factura original;Sede;Observaciones;Observaciones usuario");
                
                listaMedicamentos.stream().map((medicamentoAux) -> {
                    String filaAux =
                            "\"" + medicamentoAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(medicamentoAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(medicamentoAux.getCodEntPre()) + "\";" +
                            "\"" + medicamentoAux.getTipId() + "\";" +
                            "=\"" + medicamentoAux.getNumId() + "\";" +
                            "=\"" + ajustarValor(medicamentoAux.getNumAut()) + "\";" +
                            "=\"" + ajustarValor(medicamentoAux.getCodMed()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getTipMed()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getNomMed()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getForFar()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getConMed()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getUniMed()) + "\";" +
                            ajustarValor(medicamentoAux.getNumUni()) + ";" +
                            ajustarValor(medicamentoAux.getValUniMed()) + ";" +
                            ajustarValor(medicamentoAux.getValTotMed()) + ";" +
                            "\"" + ajustarValor(medicamentoAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(medicamentoAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(medicamentoAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarOtrosServicios(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de otros servicios
        DbOtrosServicios dbOtrosServicios = new DbOtrosServicios();
        ArrayList<OtroServicio> listaOtrosServicios = dbOtrosServicios.getListaOtrosServicios(idCarga, idGrupo, indSinGrupo);
        if (!listaOtrosServicios.isEmpty()) {
            String nombreArchivo = nombreBase + "AT.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación;Número identificación;Número autorización;Tipo servicio;Código servicio;" +
                        "Nombre servicio;Cantidad;Valor unitario;Valor total;Tipo internación;Número factura original;Sede;Observaciones;Observaciones usuario");
                
                listaOtrosServicios.stream().map((otroServicioAux) -> {
                    String filaAux =
                            "\"" + otroServicioAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(otroServicioAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(otroServicioAux.getCodEntPre()) + "\";" +
                            "\"" + otroServicioAux.getTipId() + "\";" +
                            "=\"" + otroServicioAux.getNumId() + "\";" +
                            "=\"" + ajustarValor(otroServicioAux.getNumAut()) + "\";" +
                            "\"" + ajustarValor(otroServicioAux.getTipServ()) + "\";" +
                            "=\"" + ajustarValor(otroServicioAux.getCodServ()) + "\";" +
                            "\"" + ajustarValor(otroServicioAux.getNomServ()) + "\";" +
                            ajustarValor(otroServicioAux.getCantidad()) + ";" +
                            ajustarValor(otroServicioAux.getValUniServ()) + ";" +
                            ajustarValor(otroServicioAux.getValTotServ()) + ";" +
                            "\"" + ajustarValor(otroServicioAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(otroServicioAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(otroServicioAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(otroServicioAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(otroServicioAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarNacidos(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de recién nacidos
        DbNacidos dbNacidos = new DbNacidos();
        ArrayList<Nacido> listaNacidos = dbNacidos.getListaNacidos(idCarga, idGrupo, indSinGrupo);
        if (!listaNacidos.isEmpty()) {
            String nombreArchivo = nombreBase + "AN.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación madre;Número identificación madre;Fecha nacimiento;Hora nacimiento;" +
                        "Edad gestacional;Control prenatal;Sexo;Peso;Diag. recién nacido;Causa básica muerte;Fecha muerte;Hora muerte;Tipo internación;" +
                        "Número factura original;Sede;Observaciones;Observaciones usuario");
                
                listaNacidos.stream().map((nacidoAux) -> {
                    String filaAux =
                            "\"" + nacidoAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getCodEntPre()) + "\";" +
                            "\"" + nacidoAux.getTipIdMad() + "\";" +
                            "=\"" + nacidoAux.getNumIdMad() + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getFechaNac()) + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getHoraNac()) + "\";" +
                            ajustarValor(nacidoAux.getEdadGes()) + ";" +
                            "\"" + ajustarValor(nacidoAux.getConPre()) + "\";" +
                            "\"" + ajustarValor(nacidoAux.getSexo()) + "\";" +
                            ajustarValor(nacidoAux.getPeso()) + ";" +
                            "\"" + ajustarValor(nacidoAux.getDiaNac()) + "\";" +
                            "\"" + ajustarValor(nacidoAux.getCauMue()) + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getFechaMue()) + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getHoraMue()) + "\";" +
                            "\"" + ajustarValor(nacidoAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(nacidoAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(nacidoAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(nacidoAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(nacidoAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarHospitalizacion(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de hospitalizaciones
        DbHospitalizacion dbHospitalizacion = new DbHospitalizacion();
        ArrayList<Hospitalizacion> listaHospitalizacion = dbHospitalizacion.getListaHospitalizacion(idCarga, idGrupo, indSinGrupo);
        if (!listaHospitalizacion.isEmpty()) {
            String nombreArchivo = nombreBase + "AH.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación;Número identificación;Vía ingreso;Fecha ingreso;Hora ingreso;" +
                        "Número autorización;Causa externa;Diag. principal ingreso;Diag. principal egreso;Diag. rel. 1 egreso;Diag. rel. 2 egreso;" +
                        "Diag. rel. 3 egreso;Diag. complicación;Estado salida;Causa básica muerte;Fecha egreso;Hora egreso;Tipo internación;" +
                        "Número factura original;Sede;Observaciones;Observaciones usuario");
                
                listaHospitalizacion.stream().map((hospitalizacionAux) -> {
                    String filaAux =
                            "\"" + hospitalizacionAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getCodEntPre()) + "\";" +
                            "\"" + hospitalizacionAux.getTipId() + "\";" +
                            "=\"" + hospitalizacionAux.getNumId() + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getViaIng()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getFechaIng()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getHoraIng()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getNumAut()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getCauExt()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaIng()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaEgr()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaRel1()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaRel2()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaRel3()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaCom()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getEstSal()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getDiaMue()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getFechaEgr()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getHoraEgr()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(hospitalizacionAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(hospitalizacionAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    private void generarUrgencias(int idGrupo, boolean indSinGrupo) throws FileNotFoundException, UnsupportedEncodingException {
        long idCarga = cargaArch.getIdCarga();
        
        //Se obtiene el listado de urgencias
        DbUrgencias dbUrgencias = new DbUrgencias();
        ArrayList<Urgencia> listaUrgencias = dbUrgencias.getListaUrgencias(idCarga, idGrupo, indSinGrupo);
        if (!listaUrgencias.isEmpty()) {
            String nombreArchivo = nombreBase + "AU.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                pw.println("Archivo;Número factura;Código prestador;Tipo identificación;Número identificación;Fecha ingreso;Hora ingreso;" +
                        "Número autorización;Causa externa;Diag. salida;Diag. rel. 1 salida;Diag. rel. 2 salida;Diag. rel. 3 salida;" +
                        "Destino usuario;Estado salida;Causa básica muerte;Fecha salida;Hora salida;Tipo internación;Número factura original;" +
                        "Sede;Observaciones;Observaciones usuario");
                
                listaUrgencias.stream().map((urgenciaAux) -> {
                    String filaAux =
                            "\"" + urgenciaAux.getNomArch() + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getNumFac()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getCodEntPre()) + "\";" +
                            "\"" + urgenciaAux.getTipId() + "\";" +
                            "=\"" + urgenciaAux.getNumId() + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getFechaIng()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getHoraIng()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getNumAut()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getCauExt()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getDiaSal()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getDiaRel1()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getDiaRel2()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getDiaRel3()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getDesUsu()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getEstSal()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getCauMue()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getFechaSal()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getHoraSal()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getNombreTipoInter()) + "\";" +
                            "=\"" + ajustarValor(urgenciaAux.getNumFacOri()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getNombreSede()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getObservaciones()) + "\";" +
                            "\"" + ajustarValor(urgenciaAux.getObservacionesUS()) + "\"";
                    return filaAux;
                }).forEachOrdered((filaAux) -> {
                    pw.println(filaAux);
                });
            }
        }
    }
    
    /**
     * Método que genera el archivo de Excel de Morbilidad por Causas
     * @param nombreCarpeta Nombre de la carpeta en la que se generarán los archivos
     * @throws IOException
     */
    public void generarConsolidadoGrupos(String nombreCarpeta) throws IOException {
        int anio = cargaArch.getAnio();
        int mes = cargaArch.getMes();
        
        if (!nombreCarpeta.substring(nombreCarpeta.length() - 1, nombreCarpeta.length()).equals(File.separator) ) {
            nombreCarpeta += File.separator;
        }
        
        long idCarga = cargaArch.getIdCarga();
        
        DbCargaArch dbCargaArch = new DbCargaArch();
        
        //Se obtiene el rango de fechas del período
        IntervaloFecha intervaloFecha = dbCargaArch.getIntervaloFecha(anio, mes);
        
        //Se obtiene el listado consolidado de grupos
        ArrayList<Grupo> listaGrupos = dbCargaArch.getListaGrupos(idCarga, intervaloFecha.getFechaIni(), intervaloFecha.getFechaFin());
        if (!listaGrupos.isEmpty()) {
            //Se construye el nombre del archivo de salida
            String nombreArchivo = nombreCarpeta + "RIPS_" + anio + "_" + Utilidades.derecha("00" + mes, 2) + "_consolidado_grupos.csv";
            
            try (PrintWriter pw = new PrintWriter(nombreArchivo, "windows-1252")) {
                int idGrupoAnt = -1;
                for (Grupo grupoAux : listaGrupos) {
                    if (grupoAux.getIdGrupo() != idGrupoAnt) {
                        if (idGrupoAnt != -1) {
                            pw.println("");
                            pw.println("");
                        }
                        switch (grupoAux.getIdGrupo()) {
                            case 1:
                                pw.println("INTEGRAL");
                                break;
                            case 2:
                                pw.println("MUNICIPIOS");
                                break;
                            default:
                                pw.println("INACTIVOS/NO HALLADOS");
                                break;
                        }
                        pw.println("Grupo;Frecuencia");
                    }
                    String filaAux =
                            "\"" + grupoAux.getGrupo() + "\";" +
                            ajustarValor(grupoAux.getCantidad());
                    
                    pw.println(filaAux);
                    
                    idGrupoAnt = grupoAux.getIdGrupo();
                }
            }
        }
    }
    
    private String ajustarValor(String valor) {
        return valor == null ? "" : valor;
    }
    
    private String ajustarValor(int valor) {
        return valor == 0 ? "" : valor + "";
    }
    
    private String ajustarValor(double valor) {
        return (valor == 0 ? "" : valor + "").replace(".", ",");
    }
}
