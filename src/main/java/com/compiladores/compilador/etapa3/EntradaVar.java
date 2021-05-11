/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

/**
 *
 * @author gx23
 */
public class EntradaVar{
    private String tipo;
    private boolean isPrivate = false;
    
    public EntradaVar(String tipo){
        this.tipo = tipo;
    }
    
    public EntradaVar(String tipo, boolean priv){
        this.tipo = tipo;
        this.isPrivate = priv;
    }
    
    public String imprimeVar(){
        return "\"Tipo\": \""+this.tipo+"\",\n\"isPrivate\": "+ this.isPrivate;
    }
}
