package com.osps.entidad;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Clase que representa una entidad abstracta para las tablas de RIPS
 * @author Feisar Moreno
 * @date 26/09/2019
 */
public abstract class EntidadRIPS {
    
    /**
     * Método que convierte una fecha EPOCH a un formato específico.
     * @param valorEPOCH Número EPOCH que representa la fecha
     * @param formato Texto que representa el formato al que se desea convertir
     * @return Texto de fecha con el formato requerido
     */
    protected static String getFechaEPOCHString(long valorEPOCH, String formato) {
        LocalDate ld = LocalDate.ofEpochDay(valorEPOCH);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formato, Locale.ENGLISH);
        String rta = dtf.format(ld);
        
        return rta;
    }
    
    /**
     * Método que convierte una fecha de Microsoft Excel a un formato específico.
     * @param valorExcel Número en Excel que representa la fecha
     * @param formato Texto que representa el formato al que se desea convertir
     * @return Texto de fecha con el formato requerido
     */
    protected static String getFechaExcelString(long valorExcel, String formato) {
        return getFechaEPOCHString(valorExcel - 25569, formato);
    }
}
