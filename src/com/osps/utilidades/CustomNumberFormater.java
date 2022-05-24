package com.osps.utilidades;

import java.text.DecimalFormat;

/**
 * Clase que permite aplicar formatos de números predefinidos
 * @author Feisar Moreno
 * @date 27/02/2013
 */
public class CustomNumberFormater {
    private String integerFormat = "###,###,###,###,###";
    private String floatFormat = "###,###,###,###,###,00";
    
    private String applyFormat(double value, String format) {
        DecimalFormat myFormatter = new DecimalFormat(format);
        String result = myFormatter.format(value);
        //result = result.replaceAll("\\.", "\\*");
        //result = result.replaceAll(",", "\\.");
        //result = result.replaceAll("\\*", ",");
        
        return result;
    }
    
    public String applyIntegerFormat(double value) {
        return this.applyFormat(value, this.integerFormat);
    }
    
    public String applyIntegerFormat(float value) {
        return this.applyIntegerFormat((double)value);
    }
    
    public String applyIntegerFormat(long value) {
        return this.applyIntegerFormat((double)value);
    }
    
    public String applyIntegerFormat(int value) {
        return this.applyIntegerFormat((double)value);
    }
    
    public String applyIntegerFormat(short value) {
        return this.applyIntegerFormat((double)value);
    }
    
    public String applyFloatFormat(double value) {
        return this.applyFormat(value, this.floatFormat);
    }
    
    public String applyFloatFormat(float value) {
        return this.applyFloatFormat((double)value);
    }
    
    public String applyFloatFormat(long value) {
        return this.applyFloatFormat((double)value);
    }
    
    public String applyFloatFormat(int value) {
        return this.applyFloatFormat((double)value);
    }
    
    public String applyFloatFormat(short value) {
        return this.applyFloatFormat((double)value);
    }
}
