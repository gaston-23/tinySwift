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
public class EntradaParametro{
    private String tipo;
    private String nombre;
    private int posicion;
    private Object valor;
    
    public EntradaParametro(String tipo, String nombre,int posicion){
        this.tipo = tipo;
        this.posicion = posicion;
        this.nombre = nombre;
    }
    
    public EntradaParametro(String tipo, String nombre,Object valor,int posicion){
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.nombre = nombre;
    }
    
    public String imprimePar() {
        String json = "\n\"Tipo\": \""+this.tipo+"\",\n\"Posicion\": "+this.posicion+"\n";
        
        
        return json;
    }
}
