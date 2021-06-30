/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Gaston Cavallo
 */
public class EntradaClase{
    private Hashtable<String,EntradaMetodo> metodos;
    private String nombre;
    private Hashtable<String, EntradaVar> variablesInst;
    private String herencia;
    private Hashtable<String,EntradaConstante> constantes;
    private EntradaMetodo constructor=null;
    private boolean consolidado=false;
    private int fila, columna, posAtri=0;
    
    public EntradaClase(String tipo,String herencia,int fila,int col){
        this.nombre = tipo;
        this.herencia = herencia;
        this.metodos = new Hashtable<>();
        this.constantes = new Hashtable<>();
        this.variablesInst = new Hashtable<>();
        this.columna = col;
        this.fila = fila;
    }
    
    public void insertaMetodo(String nombre, String retorno){
        this.metodos.put(nombre, new EntradaMetodo(retorno,0,0));
    }
    
    public void insertaMetodo(String nombre, EntradaMetodo metodo) throws ExcepcionSemantica{
        if(nombre.equals("constructor")){
            if(this.constructor!= null){
                throw new ExcepcionSemantica(metodo.getFila(),metodo.getColumna(),"Ya se ha declarado un constructor para esta clase",nombre,true);
            }
            this.constructor = metodo;
        }else{
            if(this.metodos.get(nombre)!=null){
                throw new ExcepcionSemantica(metodo.getFila(),metodo.getColumna(),"Ya se ha declarado un metodo con el mismo nombre",nombre,true);

            }
            this.metodos.put(nombre, metodo);

        }
    }
    
    public void insertaVariable(String nombre, EntradaVar var) throws ExcepcionSemantica{
        if(this.variablesInst.get(nombre)!=null){
            throw new ExcepcionSemantica(var.getFila(),var.getColumna(),"Ya se ha declarado una variable con el mismo nombre",nombre,true);
        }
        this.variablesInst.put(nombre, var);
        this.variablesInst.get(nombre).setPosicion(this.posAtri);
        this.posAtri++;
    }
    
    public void insertaConstante(String nombre, EntradaConstante k) throws ExcepcionSemantica{
        if(this.constantes.get(nombre)!=null){
            throw new ExcepcionSemantica(k.getFila(),k.getColumna(),"Ya se ha declarado una constante con el mismo nombre",nombre,true);
        }
        this.constantes.put(nombre, k);
        this.constantes.get(nombre).setPosicion(this.posAtri);
        this.posAtri++;
    }
    
    public EntradaMetodo getMetodo(String nombre){
        return this.metodos.get(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    public String getHerencia() {
        return herencia;
    }

    public boolean hereda(String hijo, TablaDeSimbolos ts){
        if(herencia != null && !herencia.equals("Object")){
            if(herencia.equals(hijo)){
                return true;
            }else{
                return ts.getClases().get(herencia).hereda(hijo,ts);
            }
        }else{
            return false;
        }
    }
    
    public void setHerencia(String herencia) {
        this.herencia = herencia;
    }

    public boolean isConsolidado() {
        return consolidado;
    }

    public void setConsolidado(boolean consolidado) {
        this.consolidado = consolidado;
    }

    public EntradaMetodo getConstructor() {
        return constructor;
    }

    public Hashtable<String, EntradaConstante> getConstantes() {
        return constantes;
    }

    public Hashtable<String, EntradaMetodo> getMetodos() {
        return metodos;
    }

    public Hashtable<String, EntradaVar> getVariablesInst() {
        return variablesInst;
    }
    
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }
    
    public String imprimirEC(){
        String json = "";
        json += "\"Herencia\": \""+this.herencia+"\",\n\"Constructor\":";
        json += this.constructor != null? "{"+this.constructor.imprimeMet()+"}" : "\"null\"";
        if(!metodos.isEmpty()){
            json += ",\n \"Metodos\": [\n";
            for(Map.Entry<String, EntradaMetodo> entry : metodos.entrySet()) {
                String key = entry.getKey();
                EntradaMetodo value = entry.getValue();
                json += "{\""+ key + "\": {\n"+value.imprimeMet()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        if(!constantes.isEmpty()){
            json +=",\n\"Constantes\": [";
            for(Map.Entry<String, EntradaConstante> entry : constantes.entrySet()) {
                String key = entry.getKey();
                EntradaConstante value = entry.getValue();
                json += "{\""+ key + "\": {\n"+value.imprimeConst()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        if(!variablesInst.isEmpty()){
            json +=",\n\"Variables\": [";
            for(Map.Entry<String, EntradaVar> entry : variablesInst.entrySet()) {
                String key = entry.getKey();
                EntradaVar value = entry.getValue();
                json += "{\""+ key + "\": {\n"+value.imprimeVar()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        
        json +="\n";
        return json;
    }
    
}
