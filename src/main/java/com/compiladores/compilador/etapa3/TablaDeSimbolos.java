/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import com.compiladores.compilador.etapa2.ExcepcionSintactica;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Gaston Cavallo
 */
public class TablaDeSimbolos {
    private Hashtable<String,EntradaClase> clases;
    private EntradaClase claseActual;
    private EntradaMetodo metodoActual;
    private String nombre; 
    private int lastLabel=0;
    private int labelString = 0;
    private Hashtable<String,String> labels;

    
    public TablaDeSimbolos (String nombre){
        this.clases = new Hashtable<>();
        this.clases.put("Int", new EntradaClase("Int","Object",0,0));
        this.clases.put("Bool", new EntradaClase("Bool","Object",0,0));        
        this.clases.put("Char", new EntradaClase("Char","Object",0,0));
        this.clases.put("String", stringClass());
        this.clases.put("IO", ioClass());
        this.clases.put("Object", new EntradaClase("Object",null,0,0));
        this.nombre = nombre;
        this.labels = new Hashtable<>();
    }
    
    public EntradaClase stringClass(){
        EntradaClase claseString = new EntradaClase("String","Object",0,0);
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
     * @return la clase IO para insertar en la hashTable de la tds
     * 
     */
    public EntradaClase ioClass(){
        EntradaClase claseIO = new EntradaClase("IO","Object",0,0);
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

    public Hashtable<String, EntradaClase> getClases() {
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

    public Hashtable<String, String> getLabels() {
        return labels;
    }
    
    

    public String imprimeTS(){
        String json = "{\n \"nombre\":\""+this.nombre+"\",\n";
        json += "\"Clases\":[\n";
        for(Map.Entry<String, EntradaClase> entry : clases.entrySet()) {
            String key = entry.getKey();
            EntradaClase value = entry.getValue();
            json +="{\""+ value.getNombre() + "\": {\n"+ value.imprimirEC()+"\n}\n},";
        }
        json = json.substring(0,json.length()-1);
        json += "]\n}";
        return json;
    }
}