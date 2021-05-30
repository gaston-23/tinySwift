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
public class NodoReturn extends NodoSentencia{
    
    private NodoExpresion retorno;
    
    public NodoReturn(Token tok, NodoExpresion ret){
        super(tok);
        this.retorno = ret;
    }

    @Override
    public boolean verifica() throws ExcepcionSemantica{
        return retorno.verifica();
    }
    
    
}
