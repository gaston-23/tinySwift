/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoWhile extends NodoSentencia{
    
    private NodoExpresion declaracion;
    private LinkedList<NodoSentencia> loop;
    private boolean scoped = false;
    
    public NodoWhile(int filaTok,int colTok){
        super(filaTok,colTok);
        this.loop = new LinkedList<>();
        this.scoped = false;
    }

    public void setDeclaracion(NodoExpresion declaracion) {
        this.declaracion = declaracion;
    }

    
    public void addSentencia(NodoSentencia e){
        this.loop.add(e);
    }
    
    //como es la verificacion del while? como saber cuando cortar? deberia verificarlo yo? o solo verificar que sea todo correcto?
    // verificar condicion y loop si es correcto semanticamente.. es suficiente?

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica {
        if(!this.declaracion.checkIsBoolean(ts)){
            throw new ExcepcionSemantica(this.declaracion.getFila(),this.declaracion.getCol(),"No es una declaracion de tipo booleana",this.declaracion.getTipo(ts),false);
        }
        this.loop.forEach((elem)  -> {
            try{
                elem.verifica(ts);
            }catch(ExcepcionSemantica eS){}
        });
        return true;
    }
    
    public void setScoped(boolean scoped) {
        this.scoped = scoped;
    }

    public boolean isScoped() {
        return scoped;
    }

    public LinkedList<NodoSentencia> getLoop() {
        return loop;
    }
    
    
    @Override
    public String imprimeSentencia() {
        String json= "\"nodo\": \"NodoWhile\",\n"
                + "\"declaracion\":{\n"+this.declaracion.imprimeSentencia()+"\n},\n"
                + "\"Bloque\":[";
        for (int i = 0; i < loop.size(); i++) {
            json += "\n{"+ this.loop.get(i).imprimeSentencia() +"},";
        }
        json = json.substring(0,json.length()-1);
        json += "]";
        
        
        return json;
    }

    @Override
    public String getCodigo(TablaDeSimbolos ts) {
        String asm = "";
        String labelWhile = "while"+ts.getLastLabel();
        asm += labelWhile+":\n";
        asm += this.declaracion.getCodigo(ts);
        asm += "\tbne $t1, 1, fin"+labelWhile+"\n";
        for (int i = 0; i < loop.size(); i++) {
            NodoSentencia sT = loop.get(i);
            asm += sT.getCodigo(ts);
        }
        asm +="\tj "+labelWhile+"\n";
        asm +="fin"+labelWhile+":\n";
        
        return asm; 
    }
    
    
}
