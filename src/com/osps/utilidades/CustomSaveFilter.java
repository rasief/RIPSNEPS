package com.osps.utilidades;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Clase que define un filtro para guardar archivos xlsx
 * @author Feisar Moreno
 * @date 14/12/2012
 */
public class CustomSaveFilter extends FileFilter {
    private String extension;
    
    public CustomSaveFilter(String extension) {
        this.extension = extension.toLowerCase();
    }
    
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return false;
        }
        String name = f.getName();
        
        return name.toLowerCase().endsWith("." + this.extension);
    }

    @Override
    public String getDescription() {
        return "*." + this.extension;
    }
}
