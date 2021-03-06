/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa2;

import com.compiladores.compilador.etapa1.Token;

/**
* Clase encargada de definir el error en el Analizador Sintactico
* @author Gaston Cavallo
* @author Mariel Volman
*
*/
public class ExcepcionSintactica extends Exception {
    
    public String mensaje;

    /**
     * Constructor de la clase ExcepcionLexica
     * @param fila Numero de fila en la que se encuentra el error
     * @param col Numero de columna en la que se encuentra el error
     * @param expectativa Descripcion del error, por lo general, que se esperaba encontrar
     * @param valor Valor actual del token
     */
     public ExcepcionSintactica (int fila, int col, String expectativa, String valor) {
            this.mensaje = "ERROR: SINTACTICO | LINEA: "+fila+" | COLUMNA: "+ col +" | DESCRIPCION: "+expectativa+" , se encontro: "+valor +" |";
     }

     public ExcepcionSintactica (Token token, String expectativa, String valor) {
            this.mensaje = "ERROR: SINTACTICO | LINEA: "+token.getFila()+" | COLUMNA: "+ token.getColumna() +" | DESCRIPCION: "+expectativa+" , se encontro: "+valor +" |";
     }

    @Override
    public String toString() {
        return this.mensaje; //To change body of generated methods, choose Tools | Templates.
    }
        
        
}
