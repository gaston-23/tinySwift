/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Gaston Cavallo
 */
public class EntradaClase{
    private HashMap<String,EntradaMetodo> metodos;
    private String nombre;
    private HashMap<String, EntradaVar> variablesInst;
    private String herencia;
    private HashMap<String,EntradaConstante> constantes;
    private EntradaMetodo constructor=null;
    private boolean consolidado=false;
    private int fila, columna, posAtri=0;
    private int tamaño;
    private LinkedList<String> vtable;
    
    public EntradaClase(String tipo,String herencia,int fila,int col, int tam){
        this.nombre = tipo;
        this.herencia = herencia;
        this.metodos = new HashMap<>();
        this.constantes = new HashMap<>();
        this.variablesInst = new HashMap<>();
        this.columna = col;
        this.fila = fila;
        this.tamaño = tam;
        this.vtable = new LinkedList<>();
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
            this.vtable.add("\t.word "+this.nombre+"_"+nombre);
        }
    }

    public void insertaMetodo(String nombre, EntradaMetodo metodo, String heredado, int pos) throws ExcepcionSemantica{
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
            this.vtable.add(pos, "\t.word "+heredado+"_"+nombre);
        }
    }

    public int getPosicion(String nombre){
        for (String met : this.vtable) {
            if ( met.contains(nombre)){
                return this.vtable.indexOf(met);
            }
        }
        return -1;
    }

    public void moveVtable(String method,int pos){
        String aux = "";
        for (String met : this.vtable) {
            if ( met.contains(method)){
                aux = met;
                this.vtable.remove(met);
                this.vtable.add(pos, aux);
                return;
            }
        }
    }

    public void enumeraMetodos(){
        if (this.constructor != null) {
            this.constructor.setPosicion(0);
        }
        for(Map.Entry<String, EntradaMetodo> mets : this.metodos.entrySet()) {
            EntradaMetodo met = mets.getValue();
            met.setPosicion(getPosicion(mets.getKey()));
        }
    }
    
    /**
     * @return the vtable
     */
    public LinkedList<String> getVtable() {
        return vtable;
    }

    public void insertaVariable(String nombre, EntradaVar varble) throws ExcepcionSemantica{
        if(this.variablesInst.get(nombre)!=null){
            throw new ExcepcionSemantica(varble.getFila(),varble.getColumna(),"Ya se ha declarado una variable con el mismo nombre",nombre,true);
        }
        this.variablesInst.put(nombre, varble);
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

    public HashMap<String, EntradaConstante> getConstantes() {
        return constantes;
    }

    public HashMap<String, EntradaMetodo> getMetodos() {
        return metodos;
    }

    public HashMap<String, EntradaVar> getVariablesInst() {
        return variablesInst;
    }
    
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }
    
    /**
     * @param tamaño the tamaño to set
     */
    public void incTamaño(int tamaño) {
        this.tamaño += tamaño;
    }

    /**
     * @return the tamaño
     */
    public int getTamaño() {
        return tamaño;
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
