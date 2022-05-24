package com.osps.utilidades;

/**
 * Clase que representa una tupla (para usar especialmente en combos de datos)
 * @author Feisar Moreno
 * @date 22/02/2012
 */
public class Tupla<T, U> {
    private T id;
    private U valor;
    
    public Tupla(T id, U valor) {
        this.id = id;
        this.valor = valor;
    }
    
    public T getId() {
        return id;
    }
    
    public U getValor() {
        return valor;
    }
    
    @Override
    public String toString() {
        return this.getValor().toString();
    }
}
