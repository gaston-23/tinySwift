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
public class NodoMetodo {
    private HashMap<String, NodoSentencia> bloque;
    private String nombre;
    
    
    public NodoMetodo(String nombre){
        this.nombre = nombre;
    }

    public HashMap<String, NodoSentencia> getBloque() {
        return bloque;
    }

    public String getNombre() {
        return nombre;
    }

    public void putBloque(String nombre, NodoSentencia sentencia) {
        this.bloque.put(nombre, sentencia);
    }
    
}
