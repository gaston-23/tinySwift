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
public class EntradaConstante{
    private String tipo;
    
    public EntradaConstante(String tipo){
        this.tipo = tipo;

    }
    public String imprimeConst(){
        return "\"Tipo\": \""+this.tipo+"\"";
    }
}