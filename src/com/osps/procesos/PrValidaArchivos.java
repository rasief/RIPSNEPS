package com.osps.procesos;

import com.osps.db.*;
import com.osps.entidad.EAdministradoras;

/**
 * Clase para la validación de archivos de RIPS
 * @author Feisar Moreno
 * @date 09/03/2012
 */
public class PrValidaArchivos {
    
    private long idValida; //Identificador de la validación
    private EAdministradoras entidad; //Objeto que representa la entidad administradora
    private int ano; //Año a cargar
    private int mes; //Mes a cargar (0 para todos los meses)
    private String trimAnio; //Periodo a cargar
    private boolean selUsuarios; //Indicador de archivo de usuarios seleccionado
    private boolean selConsultas; //Indicador de archivo de consultas seleccionado
    private boolean selProcedimientos; //Indicador de archivo de procedimientos seleccionado
    private boolean selHospitalizacion; //Indicador de archivo de hospitalización seleccionado
    private boolean selUrgencias; //Indicador de archivo de urgencias seleccionado
    private boolean selMedicamentos; //Indicador de archivo de medicamentos seleccionado
    private boolean selNacidos; //Indicador de archivo de recién nacidos seleccionado
    private boolean selTransacciones; //Indicador de archivo de transacciones seleccionado
    
    /**
     * Constructor de la clase
     * @param entidad Objeto que representa la entidad administradora
     * @param ano Año a cargar
     * @param mes Mes a cargar (0 para todos los meses)
     * @param selUsuarios Indicador de archivo de usuarios seleccionado
     * @param selConsultas Indicador de archivo de consultas seleccionado
     * @param selProcedimientos Indicador de archivo de procedimientos seleccionado
     * @param selHospitalizacion Indicador de archivo de hospitalización seleccionado
     * @param selUrgencias Indicador de archivo de urgencias seleccionado
     * @param selMedicamentos Indicador de archivo de medicamentos seleccionado
     * @param selNacidos Indicador de archivo de recién nacidos seleccionado
     * @param selTransacciones Indicador de archivo de transacciones seleccionado
     */
    public PrValidaArchivos(EAdministradoras entidad, int ano, int mes,
            boolean selUsuarios, boolean selConsultas, boolean selProcedimientos,
            boolean selHospitalizacion, boolean selUrgencias, boolean selMedicamentos,
            boolean selNacidos, boolean selTransacciones) {
        this.entidad = entidad;
        this.ano = ano;
        this.mes = mes;
        this.trimAnio = "00" + this.mes;
        this.trimAnio = (this.ano % 100) + trimAnio.substring(trimAnio.length() - 2);
        this.selUsuarios = selUsuarios;
        this.selConsultas = selConsultas;
        this.selProcedimientos = selProcedimientos;
        this.selHospitalizacion = selHospitalizacion;
        this.selUrgencias = selUrgencias;
        this.selMedicamentos = selMedicamentos;
        this.selNacidos = selNacidos;
        this.selTransacciones = selTransacciones;
        
        //Se inserta el registro de control de carga
        DbValidacionArch dbValidacionArch = new DbValidacionArch();
        this.idValida = dbValidacionArch.insertarRegistro(this.entidad.getCodEntAdm(), this.trimAnio);
    }
    
    public long getIdValida() {
        return this.idValida;
    }
    
    /**
     * Método que valida los archivos
     */
    public void validarArchivos() {
        if (this.selUsuarios) {
            //Validación de usuarios
            DbUsuarios dbUsuarios = new DbUsuarios();
            dbUsuarios.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selConsultas) {
            //Validación de consultas
            DbConsultas dbConsultas = new DbConsultas();
            dbConsultas.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selProcedimientos) {
            //Validación de procedimientos
            DbProcedimientos dbProcedimientos = new DbProcedimientos();
            dbProcedimientos.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selHospitalizacion) {
            //Validación de hospitalizaciones
            DbHospitalizacion dbHospitalizacion = new DbHospitalizacion();
            dbHospitalizacion.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selUrgencias) {
            //Validación de urgencias
            DbUrgencias dbUrgencias = new DbUrgencias();
            dbUrgencias.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selMedicamentos) {
            //Validación de medicamentos
            DbMedicamentos dbMedicamentos = new DbMedicamentos();
            dbMedicamentos.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selNacidos) {
            //Validación de recién nacidos
            DbNacidos dbNacidos = new DbNacidos();
            dbNacidos.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
        
        if (this.selTransacciones) {
            //Validación de transacciones
            DbFacturas dbTransacciones = new DbFacturas();
            dbTransacciones.validarRegistros(this.idValida, this.entidad.getCodEntAdm(), this.trimAnio);
        }
    }
}
