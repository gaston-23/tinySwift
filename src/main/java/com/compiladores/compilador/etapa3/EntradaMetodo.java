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
public class EntradaMetodo{
    
    private String tipoRetorno;
    private String nombre;
    private LinkedList<EntradaParametro> parametros;
    private HashMap<String,EntradaVar>  variablesMet;    
    private HashMap<String,EntradaConstante>  constanteMet;
    private int fila, columna;
    private boolean isStatic;
    private int posAtri=0;
    private int posicion;
    
    public EntradaMetodo(String retorno,int fila, int col){
        this.tipoRetorno = retorno;
        this.parametros = new LinkedList<>();
        this.variablesMet = new HashMap<>();
        this.constanteMet = new HashMap<>();
        this.columna = col;
        this.fila = fila;
    }
    public void insertaParametro(String tipo, String nombrePar,int fila,int col){
        this.parametros.add(new EntradaParametro(tipo,nombrePar,this.parametros.size(),fila,col));
    }
    
    public void insertaVariable(String nombre, EntradaVar varble) throws ExcepcionSemantica{
        if(this.variablesMet.get(nombre)!=null){
            throw new ExcepcionSemantica(varble.getFila(),varble.getColumna(),"Ya se ha declarado una variable con el mismo nombre",nombre,true);
        }
        this.variablesMet.put(nombre, varble);
        this.variablesMet.get(nombre).setPosicion(this.posAtri);
        this.posAtri++;
    }
    
    /**
     * @param posicion the posicion to set
     */
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    
    /**
     * @return the posicion
     */
    public int getPosicion() {
        return posicion;
    }
    
    public void insertaConstante(String nombre, EntradaConstante k) throws ExcepcionSemantica{
        if(this.constanteMet.get(nombre)!=null){
            throw new ExcepcionSemantica(k.getFila(),k.getColumna(),"Ya se ha declarado una constante con el mismo nombre",nombre,true);
        }
        this.constanteMet.put(nombre, k);
        this.constanteMet.get(nombre).setPosicion(this.posAtri);
        this.posAtri++;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public LinkedList<EntradaParametro> getParametros() {
        return parametros;
    }

    public HashMap<String, EntradaConstante> getConstanteMet() {
        return constanteMet;
    }

    public HashMap<String, EntradaVar> getVariablesMet() {
        return variablesMet;
    }
    public int getTamañoParam(){
        int tam = 0;
        for (int i = 0; i < parametros.size(); i++) {
            EntradaParametro par = parametros.get(i);
            if(par.getTipo().equals("String")){
                tam += 32;
            }else{
                tam += 4;
            }
        }
        return tam;
    }
    public int getTamañoAtribs(){
        int tam = 0;
        for(Map.Entry<String, EntradaVar> varble : variablesMet.entrySet()){
            EntradaVar eVar = varble.getValue();
            if(eVar.getTipo().equals("String")){
                tam += 32;
            }else{
                tam += 4;
            }
        }
        for(Map.Entry<String, EntradaConstante> cte : constanteMet.entrySet()){
            EntradaConstante eCte = cte.getValue();
            if(eCte.getTipo().equals("String")){
                tam += 32;
            }else{
                tam += 4;
            }
        }
        return tam;
    }
    
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }
    public boolean existPar(String p){
        for (int i = 0; i < parametros.size(); i++) {
            EntradaParametro par = parametros.get(i);
            if(par.getNombre().equals(p)){
                return true;
            }
        }
        return false;
    }
    public EntradaParametro getParByName(String p){
        for (int i = 0; i < parametros.size(); i++) {
            EntradaParametro par = parametros.get(i);
            if(par.getNombre().equals(p)){
                return par;
            }
        }
        return null;
    }
    
    public String imprimeMet(){
        String json = "\"Retorno\": \""+this.tipoRetorno+"\",\n\"Parametros\":[\n";
        if(!this.parametros.isEmpty()){
            for (int i = 0; i < parametros.size(); i++) {
                EntradaParametro par = parametros.get(i);
                json +="{\""+ par.getNombre()+ "\": {\n"+ par.imprimePar()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
        }
        json +="]";
        if(!constanteMet.isEmpty()){
            json +=",\n\"Constantes\": [";
            for(Map.Entry<String, EntradaConstante> entry : constanteMet.entrySet()) {
                String key = entry.getKey();
                EntradaConstante value = entry.getValue();
                json += "{\""+ key + "\": {\n"+value.imprimeConst()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        if(!variablesMet.isEmpty()){
            json +=",\n\"Variables\": [";
            for(Map.Entry<String, EntradaVar> entry : variablesMet.entrySet()) {
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