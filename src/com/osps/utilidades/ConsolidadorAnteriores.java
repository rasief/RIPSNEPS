package com.osps.utilidades;

import com.osps.db.DbHistorial;

/**
 * Clase que realiza la consolidación de los años anteriores
 * @author Feisar Moreno
 * @date 20/11/2012
 */
public class ConsolidadorAnteriores {
    public static void main(String args[]) {
        //Se carga la lista de entidades
        String[] listaEntidades = {
            "EMP012", "EMP017", "EMP023", "EMP028", "EPS001", "EPS005", "EPS006", 
            "EPS010", "EPS013", "EPS016", "EPS017", "EPS018", "EPS020", "EPS026", "EPS033", "EPS037", 
            "ESS002", "ESS024", "ESS062", "ESS133", "RES002", "RES004"};
        
        String periodo = "0900";
        DbHistorial dbHistorial = new DbHistorial();
        for (String codEntAdm : listaEntidades) {
            int resultado = dbHistorial.consolidarEntidad(codEntAdm, periodo);
            System.out.println(codEntAdm + " - " + resultado);
        }
    }
}
