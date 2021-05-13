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
public class EntradaVar{
    private String tipo;
    private boolean isPrivate = false;
    private int fila, columna;
    
    public EntradaVar(String tipo,int fila,int col){
        this.tipo = tipo;
        this.columna = col;
        this.fila = fila;
    }
    
    public EntradaVar(String tipo, boolean priv,int fila,int col){
        this.tipo = tipo;
        this.isPrivate = priv;
        this.columna = col;
        this.fila = fila;
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
    
    public String imprimeVar(){
        return "\"Tipo\": \""+this.tipo+"\",\n\"isPrivate\": "+ this.isPrivate;
    }
}
