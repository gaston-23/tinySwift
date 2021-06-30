/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoIf extends NodoSentencia{
    private NodoExpresion declaracion;
    private LinkedList<NodoSentencia> sentenciasThen;
    private LinkedList<NodoSentencia> sentenciasElse;
    private boolean isElse = false;
    private boolean scoped = false;
    
    public NodoIf(int filaTok,int colTok){
        super(filaTok,colTok);
        this.sentenciasElse = new LinkedList<>();
        this.sentenciasThen = new LinkedList<>();
        this.isElse = false;
        this.scoped = false;
    }
    
    public void addSentenciaThen(NodoSentencia e){
        this.sentenciasThen.add(e);
    }
    
    public void addSentenciaElse(NodoSentencia e){
        this.sentenciasElse.add(e);
    }
    
    public void addSentencia(NodoSentencia e){
        if(this.isElse){
            this.sentenciasElse.add(e);
        }else{
            this.sentenciasThen.add(e);
        }
        
    }

    public void setDeclaracion(NodoExpresion declaracion) {
        this.declaracion = declaracion;
    }

    public NodoExpresion getDeclaracion() {
        return declaracion;
    }

    public void setIsElse(boolean isElse) {
        this.isElse = isElse;
    }

    public LinkedList<NodoSentencia> getSentenciasThen() {
        return sentenciasThen;
    }

    public LinkedList<NodoSentencia> getSentenciasElse() {
        return sentenciasElse;
    }

    public boolean isIsElse() {
        return isElse;
    }

    public void setScoped(boolean scoped) {
        this.scoped = scoped;
    }

    public boolean isScoped() {
        return scoped;
    }
    
    
    
    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica{
        if(!this.declaracion.checkIsBoolean(ts)){
            throw new ExcepcionSemantica(this.declaracion.getFila(),this.declaracion.getCol(),"No es una declaracion de tipo booleana",this.declaracion.getTipo(ts),false);
        } 
        this.sentenciasThen.forEach((elem) -> {
            try{
                
                if(elem != null) elem.verifica(ts);
            }catch(ExcepcionSemantica eS){}
        });
        this.sentenciasElse.forEach((elem) -> {
            try{
                if(elem != null) elem.verifica(ts);
            }catch(ExcepcionSemantica eS){}
        });
        return true;
    }
    
    @Override
    public String imprimeSentencia() {
        String json= "\"nodo\": \"NodoIf\",\n"
                + "\"declaracion\":{\n"+this.declaracion.imprimeSentencia()+"\n},\n"
                + "\"sentenciasThen\":[ ";
        if(!this.sentenciasThen.isEmpty()){
            for (int i = 0; i < sentenciasThen.size(); i++) {
                if(this.sentenciasThen.get(i)!=null){
                    json += "\n{"+ this.sentenciasThen.get(i).imprimeSentencia() +"},";
                }
                
            }
            json = json.substring(0,json.length()-1);
        }
        json += "]";
        if(!this.sentenciasElse.isEmpty()){
            json +=",\n \"sentenciasElse\":[";
            for (int i = 0; i < sentenciasElse.size(); i++) {
                json += "\n{"+ this.sentenciasElse.get(i).imprimeSentencia() +"},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        
        
        return json;
    }

    @Override
    public String getCodigo(TablaDeSimbolos ts) {
        String asm = "";
        String labelElse = "resto"+ts.getLastLabel();
        asm += this.declaracion.getCodigo(ts);
        asm += "\tbne $t1, 1, "+labelElse+"\n";
        for (int i = 0; i < sentenciasThen.size(); i++) {
            NodoSentencia sT = sentenciasThen.get(i);
            if(sT != null) asm += sT.getCodigo(ts);
        }
        asm +="\tj finifelse"+labelElse+"\n";
        asm += labelElse+":\n";
        for (int j = 0; j < sentenciasElse.size(); j++) {
            NodoSentencia sE = sentenciasElse.get(j);
            if(sE != null) asm += sE.getCodigo(ts);
        }
        asm +="finifelse"+labelElse+":\n";
        
        return asm; 
    }
    
    
}
