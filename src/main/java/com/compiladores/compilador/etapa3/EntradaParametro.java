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
public class EntradaParametro{
    private String tipo;
    private String nombre;
    private int posicion;
    private int fila, columna;
    
    public EntradaParametro(String tipo, String nombre,int posicion, int fila, int col){
        this.tipo = tipo;
        this.posicion = posicion;
        this.nombre = nombre;
        this.columna = col;
        this.fila = fila;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public String imprimePar() {
        String json = "\n\"Tipo\": \""+this.tipo+"\",\n\"Posicion\": "+this.posicion+"\n";
        return json;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }
    
    
}
