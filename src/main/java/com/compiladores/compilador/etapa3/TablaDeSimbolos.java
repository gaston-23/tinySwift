/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import java.util.HashMap;
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
    
    public TablaDeSimbolos (){
        this.clases = new Hashtable<>();
        this.clases.put("Int", new EntradaClase("Int"));
        this.clases.put("Bool", new EntradaClase("Bool"));        
        this.clases.put("Char", new EntradaClase("Char"));
        this.clases.put("String", stringClass());
        this.clases.put("IO", ioClass());
        this.clases.put("Object", new EntradaClase("Object"));
    }
    
    public EntradaClase stringClass(){
        EntradaClase claseString = new EntradaClase("String");
        claseString.insertaMetodo("length", "Int");
        claseString.insertaMetodo("concat","String");
        claseString.getMetodo("concat").insertaParametro("String","s");
        claseString.insertaMetodo("substr", "String");
        claseString.getMetodo("substr").insertaParametro("String","i");
        claseString.getMetodo("substr").insertaParametro("String","l");
        return claseString;
    }
    
    /**
     * ioClass
     * retorna la clase IO en formato EntradaClase con todos sus metodos
     * @return la clase IO para insertar en la hashTable de la tds
     * 
     */
    public EntradaClase ioClass(){
        EntradaClase claseIO = new EntradaClase("IO");
        claseIO.insertaMetodo("out_string", "void");
        claseIO.getMetodo("out_string").insertaParametro("String","s");
        claseIO.insertaMetodo("out_int","void");
        claseIO.getMetodo("out_int").insertaParametro("Int","i");
        claseIO.insertaMetodo("out_bool", "void");
        claseIO.getMetodo("out_bool").insertaParametro("Bool","b");
        claseIO.insertaMetodo("out_char", "void");
        claseIO.getMetodo("out_char").insertaParametro("Char","c");
        claseIO.insertaMetodo("in_string", "String");
        claseIO.insertaMetodo("in_int", "Int");
        claseIO.insertaMetodo("in_bool", "Bool");
        claseIO.insertaMetodo("in_char", "Char");
        return claseIO;
    }
    
    public void insertaClase(String nombre, String herencia){
        this.clases.put(nombre, new EntradaClase(nombre,herencia));
    }
    
    public void insertaClase(EntradaClase claseNueva){
        this.clases.put(claseNueva.getNombre(), claseNueva);
    }

    public void setClaseActual(EntradaClase claseActual) {
        this.claseActual = claseActual;
    }

    public void setMetodoActual(EntradaMetodo metodoActual) {
        this.metodoActual = metodoActual;
    }

    public EntradaMetodo getMetodoActual() {
        return metodoActual;
    }

    public EntradaClase getClaseActual() {
        return claseActual;
    }

    public String imprimeTS(){
        String json = "{\n";
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