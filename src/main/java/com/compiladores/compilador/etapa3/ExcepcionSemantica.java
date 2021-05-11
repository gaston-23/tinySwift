/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import com.compiladores.compilador.etapa1.Token;

/**
 *
 * @author gx23
 */
public class ExcepcionSemantica  extends Exception {
    
    public String mensaje;

    /**
     * Constructor de la clase ExcepcionLexica
     * @param fila Numero de fila en la que se encuentra el error
     * @param col Numero de columna en la que se encuentra el error
     * @param expectativa Descripcion del error, por lo general, que se esperaba encontrar
     * @param valor Valor actual del token
     */
     public ExcepcionSemantica (int fila, int col, String expectativa, String valor) {
            this.mensaje = "ERROR: SEMANTICO | LINEA: "+fila+" | COLUMNA: "+ col +" | DESCRIPCION: "+expectativa+" , se encontro: "+valor +" |";
     }

     public ExcepcionSemantica (Token token, String expectativa, String valor) {
            this.mensaje = "ERROR: SEMANTICO | LINEA: "+token.getFila()+" | COLUMNA: "+ token.getColumna() +" | DESCRIPCION: "+expectativa+" , se encontro: "+valor +" |";
     }

    @Override
    public String toString() {
        return this.mensaje; //To change body of generated methods, choose Tools | Templates.
    }
        
        

}