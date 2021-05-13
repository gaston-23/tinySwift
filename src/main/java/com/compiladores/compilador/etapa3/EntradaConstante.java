/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

/**
 *
 * @author Gaston Cavallo
 */
public class EntradaConstante{
    private String tipo;
    private int fila, columna;
    
    public EntradaConstante(String tipo,int fila,int col){
        this.tipo = tipo;
        this.fila = fila;
        this.columna = col;
    }
    public String imprimeConst(){
        return "\"Tipo\": \""+this.tipo+"\"";
    }

    public String getTipo() {
        return tipo;
    }
    
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }
}