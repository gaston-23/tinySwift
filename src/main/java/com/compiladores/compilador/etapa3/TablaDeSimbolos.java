/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gaston Cavallo
 */
public class TablaDeSimbolos {
    private HashMap<String,EntradaClase> clases;// lista de clases del programa
    private EntradaClase claseActual;           // clase en el scope
    private EntradaMetodo metodoActual;         // metodo en el scope
    private String nombre;                      // nombre del programa
    private int lastLabel=0;                    // para enumerar bloques en gci
    private int labelString = 0;                // para enumerar Strings en gci
    private HashMap<String,String> labels;      // lista de labels para gci

    
    public TablaDeSimbolos (String nombre){
        this.clases = new HashMap<>();
        this.clases.put("Int", new EntradaClase("Int","Object",0,0,4));
        this.clases.put("Bool", new EntradaClase("Bool","Object",0,0,4));        
        this.clases.put("Char", new EntradaClase("Char","Object",0,0,4));
        this.clases.put("String", stringClass());
        this.clases.put("IO", ioClass());
        this.clases.put("Object", new EntradaClase("Object",null,0,0,4));
        this.nombre = nombre;
        this.labels = new HashMap<>();
    }
    
    public EntradaClase stringClass(){
        EntradaClase claseString = new EntradaClase("String","Object",0,0,4);
        claseString.insertaMetodo("length", "Int");
        claseString.insertaMetodo("concat","String");
        claseString.getMetodo("concat").insertaParametro("String","s",0,0);
        claseString.insertaMetodo("substr", "String");
        claseString.getMetodo("substr").insertaParametro("String","i",0,0);
        claseString.getMetodo("substr").insertaParametro("String","l",0,0);
        return claseString;
    }
    
    /**
     * ioClass
     * retorna la clase IO en formato EntradaClase con todos sus metodos
     * @return la clase IO para insertar en la HashMap de la tds
     * 
     */
    public EntradaClase ioClass(){
        EntradaClase claseIO = new EntradaClase("IO","Object",0,0,4);
        claseIO.insertaMetodo("out_string", "void");
        claseIO.getMetodo("out_string").insertaParametro("String","s",0,0);
        claseIO.insertaMetodo("out_int","void");
        claseIO.getMetodo("out_int").insertaParametro("Int","i",0,0);
        claseIO.insertaMetodo("out_bool", "void");
        claseIO.getMetodo("out_bool").insertaParametro("Bool","b",0,0);
        claseIO.insertaMetodo("out_char", "void");
        claseIO.getMetodo("out_char").insertaParametro("Char","c",0,0);
        claseIO.insertaMetodo("in_string", "String");
        claseIO.insertaMetodo("in_int", "Int");
        claseIO.insertaMetodo("in_bool", "Bool");
        claseIO.insertaMetodo("in_char", "Char");
        return claseIO;
    }
    
    public void insertaClase(EntradaClase claseNueva) throws ExcepcionSemantica{
        if(!this.clases.containsKey(claseNueva.getNombre())){
            this.clases.put(claseNueva.getNombre(), claseNueva);
        }else{
            throw new ExcepcionSemantica(claseNueva.getFila(),claseNueva.getColumna(),"Clase declarada anteriormente",claseNueva.getHerencia(),true);
        }
    }

    public void setClaseActual(EntradaClase claseActual) {
        this.claseActual = claseActual;
    }

    public void setMetodoActual(EntradaMetodo metodoActual) {
        this.metodoActual = metodoActual;
    }

    public void setClaseActual(String claseActual) {
        this.claseActual = this.getClases().get(claseActual);
    }

    public void setMetodoActual(String metodoActual) {
        if(metodoActual.equals("constructor")){
            this.metodoActual = this.getClaseActual().getConstructor();
        }else{
            this.metodoActual = this.getClaseActual().getMetodo(metodoActual);
        }
    }
    
    public void limpiaMetodoActual(){
        this.metodoActual = null;
    }
    
    public void limpiaClaseActual(){
        this.claseActual = null;
    }

    public EntradaMetodo getMetodoActual() {
        return metodoActual;
    }

    public EntradaClase getClaseActual() {
        return claseActual;
    }

    public HashMap<String, EntradaClase> getClases() {
        return clases;
    }

    public int getLastLabel() {
        return lastLabel++;
    }

    public int getLastString() {
        return labelString++;
    }
    
    public void putLabel(String key, String data){
        this.labels.put(key, data);
    }

    public HashMap<String, String> getLabels() {
        return labels;
    }
    
    
    /**
     * imprimeTS : devuelve una serializacion de la TS
     * @author Gaston Cavallo
     * @return un String con los datos de la TS
     */
    public String imprimeTS(){
        String json = "{\n \"nombre\":\""+this.nombre+"\",\n";
        json += "\"Clases\":[\n";
        for(Map.Entry<String, EntradaClase> entry : clases.entrySet()) {
            
            EntradaClase value = entry.getValue();
            json +="{\""+ value.getNombre() + "\": {\n"+ value.imprimirEC()+"\n}\n},";
        }
        json = json.substring(0,json.length()-1);
        json += "]\n}";
        return json;
    }
}