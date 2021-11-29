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
    private int fila, columna,posicion;
    private int tamaño;
    
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

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() {
        return posicion;
    }
    
    /**
     * @param tamaño the tamaño to set
     */
    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    /**
     * @return the tamaño
     */
    public int getTamaño() {
        return tamaño;
    }
    
    public String imprimeVar(){
        return "\"Tipo\": \""+this.tipo+"\",\n\"isPrivate\": "+ this.isPrivate+",\n\"Posicion\": \""+this.posicion+"\"";
    }
    
    public String generaCodigo(){
        if (this.tipo.equals("String") || this.tipo.equals("Char")){
            return ".asciiz \"\"";
        }else{
            if(this.tipo.equals("Int") || this.tipo.equals("Bool")) {
                return ".word 4";
            }else{
                return ".space "+this.tamaño;
            }
        }
    }
}
