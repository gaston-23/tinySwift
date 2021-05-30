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
public class NodoAsignacion extends NodoExpresion{
    
    private NodoExpresion izq;    
    private NodoExpresion der;

    private String tipoAsig;
    
    public NodoAsignacion(Token tok,NodoExpresion izqui, NodoExpresion dere,String tipo){
        super(tok);
        this.der= dere;
        this.izq = izqui;
        this.tipoAsig = tipo;
    }

    public void setDer(NodoExpresion der) {
        this.der = der;
    }

    public void setIzq(NodoExpresion izq) {
        this.izq = izq;
    }

    @Override
    public String getTipo() {
        return this.izq.getTipo();
    }

    public String getTipoAsig() {
        return tipoAsig;
    }
    

    @Override
    public boolean verifica() throws ExcepcionSemantica {
        //TODO verificar herencias y compatibilidades
        //TODO if tipoAsig es != primitivo throw err 
        if(izq.getTipo().equals(der.getTipo())){
            return true;
        }else{
            throw new ExcepcionSemantica(this.getToken(),"La asignacion contiene tipos incompatibles",this.getToken().getValor(),false);
        }
    }
    
    
}
