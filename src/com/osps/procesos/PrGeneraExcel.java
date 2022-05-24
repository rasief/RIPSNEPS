package com.osps.procesos;

import com.osps.entidad.CargaArch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase para la generación de archivos xlsx
 * @author Feisar Moreno
 * @date 12/12/2012
 */
public abstract class PrGeneraExcel {
    protected final CargaArch cargaArch;
    protected Workbook wb;
    protected CellStyle styleGeneral;
    protected CellStyle styleInteger;
    protected CellStyle styleFloat;
    protected CellStyle stylePercentage;
    protected CellStyle styleGeneralBold;
    protected CellStyle styleIntegerBold;
    protected CellStyle stylePercentageBold;
    protected CellStyle styleHeaderBorder;
    protected CellStyle styleGeneralBorder;
    protected CellStyle styleIntegerBorder;
    protected CellStyle styleFloatBorder;
    protected CellStyle stylePercentageBorder;
    protected CellStyle styleGeneralBoldBorder;
    protected CellStyle styleIntegerBoldBorder;
    protected CellStyle styleFloatBoldBorder;
    protected CellStyle stylePercentageBoldBorder;
    
    /**
     * Constructor de la clase
     * @param cargaArch Objeto que representa una carga de archivos
     */
    public PrGeneraExcel(CargaArch cargaArch) {
        this.cargaArch = cargaArch;
    }
    
    /**
     * Método protegido que crea nuevamente el WorkBook interno
     */
    protected final void crearWorkBook() {
        this.wb = new XSSFWorkbook();
        
        Font fontBold = this.wb.createFont();
        fontBold.setBold(true);
        
        //Se crean los estilos para las celdas
        this.styleGeneral = this.wb.createCellStyle();
        this.styleGeneral.setVerticalAlignment(VerticalAlignment.CENTER);
        
        this.styleInteger = this.wb.createCellStyle();
        this.styleInteger.setAlignment(HorizontalAlignment.RIGHT);
        this.styleInteger.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleInteger.setDataFormat((short)3);
        
        this.styleFloat = this.wb.createCellStyle();
        this.styleFloat.setAlignment(HorizontalAlignment.RIGHT);
        this.styleFloat.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleFloat.setDataFormat((short)4);
        
        this.stylePercentage = this.wb.createCellStyle();
        this.stylePercentage.setAlignment(HorizontalAlignment.RIGHT);
        this.stylePercentage.setVerticalAlignment(VerticalAlignment.CENTER);
        this.stylePercentage.setDataFormat((short)10);
        
        this.styleGeneralBold = this.wb.createCellStyle();
        this.styleGeneralBold.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleGeneralBold.setFont(fontBold);
        
        this.styleIntegerBold = this.wb.createCellStyle();
        this.styleIntegerBold.setAlignment(HorizontalAlignment.RIGHT);
        this.styleIntegerBold.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleIntegerBold.setDataFormat((short)3);
        this.styleIntegerBold.setFont(fontBold);
        
        this.stylePercentageBold = this.wb.createCellStyle();
        this.stylePercentageBold.setAlignment(HorizontalAlignment.RIGHT);
        this.stylePercentageBold.setVerticalAlignment(VerticalAlignment.CENTER);
        this.stylePercentageBold.setDataFormat((short)10);
        this.stylePercentageBold.setFont(fontBold);
        
        this.styleHeaderBorder = this.wb.createCellStyle();
        this.styleHeaderBorder.setBorderTop(BorderStyle.THIN);
        this.styleHeaderBorder.setBorderLeft(BorderStyle.THIN);
        this.styleHeaderBorder.setBorderRight(BorderStyle.THIN);
        this.styleHeaderBorder.setBorderBottom(BorderStyle.THIN);
        this.styleHeaderBorder.setAlignment(HorizontalAlignment.CENTER);
        this.styleHeaderBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleHeaderBorder.setWrapText(true);
        this.styleHeaderBorder.setFont(fontBold);
        
        this.styleGeneralBorder = this.wb.createCellStyle();
        this.styleGeneralBorder.setBorderTop(BorderStyle.THIN);
        this.styleGeneralBorder.setBorderLeft(BorderStyle.THIN);
        this.styleGeneralBorder.setBorderRight(BorderStyle.THIN);
        this.styleGeneralBorder.setBorderBottom(BorderStyle.THIN);
        this.styleGeneralBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleGeneralBorder.setWrapText(true);
        
        this.styleIntegerBorder = this.wb.createCellStyle();
        this.styleIntegerBorder.setBorderTop(BorderStyle.THIN);
        this.styleIntegerBorder.setBorderLeft(BorderStyle.THIN);
        this.styleIntegerBorder.setBorderRight(BorderStyle.THIN);
        this.styleIntegerBorder.setBorderBottom(BorderStyle.THIN);
        this.styleIntegerBorder.setAlignment(HorizontalAlignment.RIGHT);
        this.styleIntegerBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleIntegerBorder.setDataFormat((short)3);
        
        this.styleFloatBorder = this.wb.createCellStyle();
        this.styleFloatBorder.setBorderTop(BorderStyle.THIN);
        this.styleFloatBorder.setBorderLeft(BorderStyle.THIN);
        this.styleFloatBorder.setBorderRight(BorderStyle.THIN);
        this.styleFloatBorder.setBorderBottom(BorderStyle.THIN);
        this.styleFloatBorder.setAlignment(HorizontalAlignment.RIGHT);
        this.styleFloatBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleFloatBorder.setDataFormat((short)4);
        
        this.stylePercentageBorder = this.wb.createCellStyle();
        this.stylePercentageBorder.setBorderTop(BorderStyle.THIN);
        this.stylePercentageBorder.setBorderLeft(BorderStyle.THIN);
        this.stylePercentageBorder.setBorderRight(BorderStyle.THIN);
        this.stylePercentageBorder.setBorderBottom(BorderStyle.THIN);
        this.stylePercentageBorder.setAlignment(HorizontalAlignment.RIGHT);
        this.stylePercentageBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.stylePercentageBorder.setDataFormat((short)10);
        
        this.styleGeneralBoldBorder = this.wb.createCellStyle();
        this.styleGeneralBoldBorder.setBorderTop(BorderStyle.THIN);
        this.styleGeneralBoldBorder.setBorderLeft(BorderStyle.THIN);
        this.styleGeneralBoldBorder.setBorderRight(BorderStyle.THIN);
        this.styleGeneralBoldBorder.setBorderBottom(BorderStyle.THIN);
        this.styleGeneralBoldBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleGeneralBoldBorder.setWrapText(true);
        this.styleGeneralBoldBorder.setFont(fontBold);
        
        this.styleIntegerBoldBorder = this.wb.createCellStyle();
        this.styleIntegerBoldBorder.setBorderTop(BorderStyle.THIN);
        this.styleIntegerBoldBorder.setBorderLeft(BorderStyle.THIN);
        this.styleIntegerBoldBorder.setBorderRight(BorderStyle.THIN);
        this.styleIntegerBoldBorder.setBorderBottom(BorderStyle.THIN);
        this.styleIntegerBoldBorder.setAlignment(HorizontalAlignment.RIGHT);
        this.styleIntegerBoldBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleIntegerBoldBorder.setDataFormat((short)3);
        this.styleIntegerBoldBorder.setFont(fontBold);
        
        this.styleFloatBoldBorder = this.wb.createCellStyle();
        this.styleFloatBoldBorder.setBorderTop(BorderStyle.THIN);
        this.styleFloatBoldBorder.setBorderLeft(BorderStyle.THIN);
        this.styleFloatBoldBorder.setBorderRight(BorderStyle.THIN);
        this.styleFloatBoldBorder.setBorderBottom(BorderStyle.THIN);
        this.styleFloatBoldBorder.setAlignment(HorizontalAlignment.RIGHT);
        this.styleFloatBoldBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.styleFloatBoldBorder.setDataFormat((short)4);
        this.styleFloatBoldBorder.setFont(fontBold);
        
        this.stylePercentageBoldBorder = this.wb.createCellStyle();
        this.stylePercentageBoldBorder.setBorderTop(BorderStyle.THIN);
        this.stylePercentageBoldBorder.setBorderLeft(BorderStyle.THIN);
        this.stylePercentageBoldBorder.setBorderRight(BorderStyle.THIN);
        this.stylePercentageBoldBorder.setBorderBottom(BorderStyle.THIN);
        this.stylePercentageBoldBorder.setAlignment(HorizontalAlignment.RIGHT);
        this.stylePercentageBoldBorder.setVerticalAlignment(VerticalAlignment.CENTER);
        this.stylePercentageBoldBorder.setDataFormat((short)10);
        this.stylePercentageBoldBorder.setFont(fontBold);
    }
    
    /**
     * Método protegido que recibe un mapa con valores y retorna un ArrayList
     * ordenado por cantidad de mayor a menor
     * @param <T>
     * @param <K>
     * @param mapaTotales HashMap con los datos y sus cantidades
     * @return ArrayList con los datos ordenados
     */
    protected <T extends Comparable, K extends Number> ArrayList<T> ordenarMapaValores(HashMap<T, K> mapaTotales) {
        ArrayList<T> listaTotales = new ArrayList<T>();
        for (Map.Entry<T, K> totalAux : mapaTotales.entrySet()) {
            int indexAux = 0;
            boolean hallado = false;
            for (int i = 0; i < listaTotales.size(); i++) {
                T llaveAux = listaTotales.get(i);
                if (totalAux.getValue().doubleValue() > mapaTotales.get(llaveAux).doubleValue() ||
                        (totalAux.getValue() == mapaTotales.get(llaveAux) &&
                        totalAux.getKey().compareTo(llaveAux) < 0)) {
                    indexAux = i;
                    hallado = true;
                    break;
                }
            }
            if (!hallado) {
                indexAux = listaTotales.size();
            }
            listaTotales.add(indexAux, totalAux.getKey());
        }
        
        return listaTotales;
    }
}
