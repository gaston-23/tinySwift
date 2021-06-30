/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;

/**
 *
 * @author  Gaston Cavallo
 */
public class NodoAST {
    
    private int fila,col;
    
    public NodoAST(int filaTok,int colTok){
        this.fila = filaTok;
        this.col = colTok;
    }

    public int getCol() {
        return col;
    }

    public int getFila() {
        return fila;
    }
    
    public String imprimeSentencia(){
        return "\"nodo\":\"NodoAST\"";
    }
    
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica {
        return true;
    }
    public String getCodigo(TablaDeSimbolos ts){
        return "";
    }
}
