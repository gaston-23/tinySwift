/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;
import java.util.Hashtable;

/**
 *
 * @author gx23
 */
public class NodoLLamadaMetodo extends NodoSentencia{
    
    private NodoExpresion padre;
    private String nombre;
    private Hashtable<String,NodoExpresion> args;
    
    public NodoLLamadaMetodo(Token tok,String nombre,NodoExpresion padre){
        super(tok);
        this.nombre = nombre;
        this.padre = padre;
    }
    
    
}
