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
public class EntradaMetodo{
    
    private String tipoRetorno;
    private String nombre;
    private Hashtable<String,EntradaParametro> parametros;
    private Hashtable<String,EntradaVar>  variablesMet;    
    private Hashtable<String,EntradaConstante>  constanteMet;
    private int fila, columna;
    private boolean isStatic;
    
    public EntradaMetodo(String retorno,int fila, int col){
        this.tipoRetorno = retorno;
        this.parametros = new Hashtable<>();
        this.variablesMet = new Hashtable<>();
        this.constanteMet = new Hashtable<>();
        this.columna = col;
        this.fila = fila;
    }
    public void insertaParametro(String tipo, String nombrePar,int fila,int col){
        this.parametros.put(nombrePar, new EntradaParametro(tipo,nombrePar,this.parametros.size(),fila,col));
    }
    
    public void insertaVariable(String nombre, EntradaVar var) throws ExcepcionSemantica{
        if(this.variablesMet.get(nombre)!=null){
            throw new ExcepcionSemantica(var.getFila(),var.getColumna(),"Ya se ha declarado una variable con el mismo nombre",nombre,true);
        }
        this.variablesMet.put(nombre, var);
    }
    
    
    public void insertaConstante(String nombre, EntradaConstante k) throws ExcepcionSemantica{
        if(this.constanteMet.get(nombre)!=null){
            throw new ExcepcionSemantica(k.getFila(),k.getColumna(),"Ya se ha declarado una constante con el mismo nombre",nombre,true);
        }
        this.constanteMet.put(nombre, k);
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

    public Hashtable<String, EntradaParametro> getParametros() {
        return parametros;
    }

    public Hashtable<String, EntradaConstante> getConstanteMet() {
        return constanteMet;
    }

    public Hashtable<String, EntradaVar> getVariablesMet() {
        return variablesMet;
    }
    
    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
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