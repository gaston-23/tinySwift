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
    private int fila, columna,posicion;
    
    public EntradaConstante(String tipo,int fila,int col){
        this.tipo = tipo;
        this.fila = fila;
        this.columna = col;
    }
    public String imprimeConst(){
        return "\"Tipo\": \""+this.tipo+"\",\n\"Posicion\": \""+this.posicion+"\"";
    }

    public String generaCodigo(){
        if (this.tipo.equals("String") || this.tipo.equals("Char")){
            return ".asciiz \"\"";
        }else{
            if(this.tipo.equals("Int") || this.tipo.equals("Bool")) {
                return ".word 0";
            }else{
                return ".space ";
            }
        }
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

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() {
        return posicion;
    }
    
}