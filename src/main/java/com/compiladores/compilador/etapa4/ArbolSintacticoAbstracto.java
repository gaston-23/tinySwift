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
public class ArbolSintacticoAbstracto {
    private HashMap<String,NodoClase> clases;
    private String nombre;
    
    public ArbolSintacticoAbstracto(String archivo){
        this.nombre = archivo;
    }

    public HashMap<String, NodoClase> getClases() {
        return clases;
    }

    public String getNombre() {
        return nombre;
    }

    public void putClase(String nombre, NodoClase clase) {
        this.clases.put(nombre, clase);
    }
    
    
    
    
}
