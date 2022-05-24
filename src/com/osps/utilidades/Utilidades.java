package com.osps.utilidades;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase con métodos varios estático
 * @author Feisar Moreno
 * @date 27/02/2012
 */
public final class Utilidades {
    
    /**
     * Método que retorna los caracteres a la izquierda de una cadena
     * @author Feisar Moreno
     * @date 27/02/2012
     * @param cadena Cadena de la que se quiere extraer la parte izquierda
     * @param longitud Número de caracteres a extraer
     * @return <code>String</code> con los caracteres a la izquierda de la cadena original
     */
    public static String izquierda(String cadena, int longitud) {
        String retorno = cadena;
        if (cadena.length() > longitud) {
            retorno = cadena.substring(0, longitud);
        }
        
        return retorno;
    }
    
    /**
     * Método que retorna los caracteres a la derecha de una cadena
     * @author Feisar Moreno
     * @date 27/02/2012
     * @param cadena Cadena de la que se quiere extraer la parte derecha
     * @param longitud Número de caracteres a extraer
     * @return <code>String</code> con los caracteres a la derecha de la cadena original
     */
    public static String derecha(String cadena, int longitud) {
        String retorno = cadena;
        int longCad = cadena.length();
        if (longCad > longitud) {
            retorno = cadena.substring(longCad - longitud);
        }
        
        return retorno;
    }
    
    /**
     * Método que retorna un <code>ArrayList</code> de <code>String</code> tomado de una cadena de texto al aplicar un listado de separadores
     * @param cadena Cadena de texto
     * @param arrSeparadores Lista de separadores
     * @return <code>ArrayList</code> con los <code>String</code> resultantes de la aplicación de los separadores.
     */
    public static ArrayList<String> dividirCadena(String cadena, String[] arrSeparadores) {
        ArrayList<String> listaRespuesta = new ArrayList<>();
        
        ArrayList<String> listaAux = new ArrayList<>();
        listaAux.add(cadena);
        for (String sepAux : arrSeparadores) {
            for (String cadenaAux : listaAux) {
                String[] arrAux = cadenaAux.split(sepAux);
                listaRespuesta.addAll(Arrays.asList(arrAux));
            }
            
        }
        
        return listaRespuesta;
    }
    
}
