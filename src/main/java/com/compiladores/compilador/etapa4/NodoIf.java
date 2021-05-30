/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import java.util.LinkedList;

/**
 *
 * @author gx23
 */
public class NodoIf extends NodoSentencia{
    private NodoExpresion declaracion;
    private LinkedList<NodoSentencia> sentenciasThen;
    private LinkedList<NodoSentencia> sentenciasElse;
    
    public NodoIf(Token tok){
        super(tok);
        
    }
    
    @Override
    public boolean verifica() throws ExcepcionSemantica{
        if(!this.declaracion.checkIsBoolean()){
            throw new ExcepcionSemantica(this.declaracion.getToken(),"No es una declaracion de tipo booleana",this.declaracion.getToken().getValor(),false);
        }
        this.sentenciasThen.forEach((elem) -> {
            try{
                elem.verifica();
            }catch(ExcepcionSemantica eS){}
        });
        this.sentenciasElse.forEach((elem) -> {
            try{
                elem.verifica();
            }catch(ExcepcionSemantica eS){}
        });
        return true;
    }
    
    
}
