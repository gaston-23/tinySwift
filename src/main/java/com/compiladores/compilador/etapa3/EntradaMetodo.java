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
public class EntradaMetodo{
    
    private String tipoRetorno;
    private String nombre;
    private Hashtable<String,EntradaParametro> parametros;
    private Hashtable<String,EntradaVar>  variablesMet;    
    private Hashtable<String,EntradaConstante>  constanteMet;

    private boolean isStatic;
    
    public EntradaMetodo(String retorno){
        this.tipoRetorno = retorno;
        this.parametros = new Hashtable<>();
        this.variablesMet = new Hashtable<>();
        this.constanteMet = new Hashtable<>();
    }
    public void insertaParametro(String tipo, String nombrePar, Object valor){
        this.parametros.put(nombrePar, new EntradaParametro(tipo,nombrePar,valor,this.parametros.size()));
    }
    public void insertaParametro(String tipo, String nombrePar){
        this.parametros.put(nombrePar, new EntradaParametro(tipo,nombrePar,this.parametros.size()));
    }
    
    public void insertaVariable(String nombre, String tipo, boolean priv){
        this.variablesMet.put(nombre, new EntradaVar(tipo, priv));
    }
    
    public void insertaConstante(String nombre, String tipo){
        this.constanteMet.put(nombre, new EntradaConstante(tipo));
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
    
    public String imprimeMet(){
        String json = "\"Retorno\": \""+this.tipoRetorno+"\",\n\"Parametros\":[\n";
        if(!this.parametros.isEmpty()){
            for(Map.Entry<String, EntradaParametro> entry : parametros.entrySet()) {
                String key = entry.getKey();
                EntradaParametro value = entry.getValue();
                json +="{\""+ key+ "\": {\n"+ value.imprimePar()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
        }
        json +="]\n";
        return json;
    }

    
}