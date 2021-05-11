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
 * @author gx23
 */
public class EntradaClase{
    private Hashtable<String,EntradaMetodo> metodos;
    private String nombre;
    private Hashtable<String, EntradaVar> variablesInst;
    private String herencia;
    private Hashtable<String,EntradaConstante> constantes;
    private EntradaMetodo constructor=null;
    
    public EntradaClase(String tipo){
        this.nombre = tipo;
//        if(!tipo.equals("Int") && !tipo.equals("Char") && !tipo.equals("Bool")){
            this.metodos = new Hashtable<>();
            this.variablesInst = new Hashtable<>();
            this.constantes = new Hashtable<>();
//        }
    }
    public EntradaClase(String tipo,String herencia){
        this.nombre = tipo;
        this.herencia = herencia;
//        if(!tipo.equals("Int") && !tipo.equals("Char") && !tipo.equals("Bool")){
            this.metodos = new Hashtable<>();
//        }
    }
    
    public void insertaMetodo(String nombre, String retorno){
        this.metodos.put(nombre, new EntradaMetodo(retorno));
    }
    
    public void insertaMetodo(String nombre, EntradaMetodo metodo){
        if(nombre.equals("consructor")){
            this.constructor = metodo;
        }else{
            this.metodos.put(nombre, metodo);

        }
    }
    
    public void insertaVariable(String nombre, String tipo){
        this.variablesInst.put(nombre, new EntradaVar(tipo));
    }
    
    public void insertaVariable(String nombre, String tipo, boolean priv){
        this.variablesInst.put(nombre, new EntradaVar(tipo, priv));
    }
    
    public void insertaConstante(String nombre, String tipo){
        this.constantes.put(nombre, new EntradaConstante(tipo));
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

    public void setHerencia(String herencia) {
        this.herencia = herencia;
    }
    
    public String imprimirEC(){
        String json = "";
        json += "\"Herencia\": \""+this.herencia+"\",\n\"Constructor\":";
        json += this.constructor != (null)? "{"+this.constructor.imprimeMet()+"}," : "\"null\"";
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
