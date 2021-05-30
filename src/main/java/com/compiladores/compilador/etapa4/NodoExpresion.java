/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;

/**
 *
 * @author gx23
 */
public class NodoExpresion extends NodoSentencia{
    
    private Object value;
    private String tipo;
    
    public NodoExpresion(Token tok){
        super(tok);
    }    
    
    public NodoExpresion(Token tok,Object value, String tipo){
        super(tok);
    }    
    
    public boolean checkIsBoolean(){
        if(!this.tipo.equals("Bool") && !this.tipo.equals("NodoExpresionBinaria")){
            return false;
        }else{
            if(this.tipo.equals("Bool")){
                //TODO analiza expresion
                return true;
            }else{
                //TODO analiza expresion
                return true;
            }
        }
    }

    public String getTipo() {
        return tipo;
    }

    public Object getValue() {
        return value;
    }
    
    
}
