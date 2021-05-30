/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;

/**
 *
 * @author gx23
 */
public class NodoSentencia {
    private Token token;

    public NodoSentencia(Token token){
        this.token = token;
    }
    
    
    public Token getToken() {
        return token;
    }
    
    public boolean verifica() throws ExcepcionSemantica{
        return true;
    }
}
