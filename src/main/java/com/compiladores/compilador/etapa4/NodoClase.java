/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import java.util.HashMap;

/**
 *
 * @author gx23
 */
public class NodoClase {
    private HashMap<String, NodoMetodo> metodos;
    private String nombre;
    
    public NodoClase(String nombre){
        this.nombre = nombre;
    }

    public HashMap<String, NodoMetodo> getMetodos() {
        return metodos;
    }

    public String getNombre() {
        return nombre;
    }

    public void putMetodos(String nombre, NodoMetodo metodo) {
        this.metodos.put(nombre, metodo);
    }
    
    
    
}
