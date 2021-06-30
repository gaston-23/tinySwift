/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoReturn extends NodoSentencia{
    
    private NodoExpresion retorno;
    
    public NodoReturn(int filaTok,int colTok, NodoExpresion ret){
        super(filaTok,colTok);
        this.retorno = ret;
    }

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica{
        return retorno.verifica(ts);
    }
    
    
    @Override
    public String imprimeSentencia() {
        return "\"nodo\":\"NodoReturn\",\n\"tipo\":\""+this.retorno.getTipoImpreso()+"\",\n\"valor\":{"+this.retorno.imprimeSentencia()+"}";
    }

    
}
