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
public class NodoExpresionBinaria extends NodoExpresion {
    
    private NodoExpresion izq;    
    private NodoExpresion der;
    private String oper;

    
    public NodoExpresionBinaria(Token tok){
        super(tok);
    } 
    
    public NodoExpresionBinaria(Token tok, NodoExpresion izq, String op, NodoExpresion der){
        super(tok);
        this.der = der;
        this.izq = izq;
    }

    @Override
    public boolean verifica() throws ExcepcionSemantica {
        return isValid(this.der.getTipo(),this.izq.getTipo(),this.oper);
    }
    
    private boolean isValid(String der,String izq,String op) throws ExcepcionSemantica {
        if(op.equals("||") || op.equals("&&")){
            if(der.equals(izq) && der.equals("Bool")){
                return true;
            }else{
                throw new ExcepcionSemantica(this.getToken(),"La expresion contiene tipos incompatibles",this.getToken().getValor(),false);
            }
        }else{
            if(op.equals("*") && op.equals("/") && op.equals("%") && op.equals("-") && op.equals("<") && op.equals(">") && op.equals("<=") && op.equals(">=") && op.equals("==") ){
                if(der.equals(izq) && der.equals("Int")){
                    return true;
                }else{
                    throw new ExcepcionSemantica(this.getToken(),"La expresion contiene tipos incompatibles",this.getToken().getValor(),false);

                }
            }else{
                if(op.equals("+")){
                    if(der.equals(izq) && (der.equals("Int") || der.equals("String") || der.equals("Char"))){
                        return true;
                    }else{
                        throw new ExcepcionSemantica(this.getToken(),"La expresion contiene tipos incompatibles",this.getToken().getValor(),false);
                    } 
                }
            }
        }
        //TODO verificar herencias y compatibilidades
        System.out.println("tipo no manejado:: "+op);
        return false;
    }
    
    
}
