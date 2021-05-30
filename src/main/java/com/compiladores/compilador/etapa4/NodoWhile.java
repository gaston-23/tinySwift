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
public class NodoWhile extends NodoSentencia{
    
    private NodoExpresion declaracion;
    private LinkedList<NodoSentencia> loop;
    
    public NodoWhile(Token tok){
        super(tok);
        
    }

    public void setDeclaracion(NodoExpresion declaracion) {
        this.declaracion = declaracion;
    }

    public void setLoop(LinkedList<NodoSentencia> loop) {
        this.loop = loop;
    }
    
    //como es la verificacion del while? como saber cuando cortar? deberia verificarlo yo? o solo verificar que sea todo correcto?
    // verificar condicion y loop si es correcto semanticamente.. es suficiente?

    @Override
    public boolean verifica() throws ExcepcionSemantica {
        if(!this.declaracion.checkIsBoolean()){
            throw new ExcepcionSemantica(this.declaracion.getToken(),"No es una declaracion de tipo booleana",this.declaracion.getToken().getValor(),false);
        }
        this.loop.forEach((elem)  -> {
            try{
                elem.verifica();
            }catch(ExcepcionSemantica eS){}
        });
        return true;
    }
    
}
