/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Gaston Cavallo
 */
public class ArbolSintacticoAbstracto {
    private HashMap<String,NodoClase> clases;
    private String nombre;
    private Stack<NodoAST> scope;
    
    public ArbolSintacticoAbstracto(String archivo){
        this.nombre = archivo;
        this.clases = new HashMap<>();
        this.scope = new Stack<>();
    }

    public HashMap<String, NodoClase> getClases() {
        return clases;
    }

    public String getNombre() {
        return nombre;
    }

    public void putClase(String nombre, NodoClase clase) {
        this.clases.put(nombre, clase);
    }

    public NodoAST popScope() {
        return this.scope.pop();
    }

    public void pushScope(NodoAST scope) {
        this.scope.push(scope);
    }
    
    public NodoAST peekScope(){
        return this.scope.peek();
    }
    
    public Stack<NodoAST> getScope() {
        return scope;
    }
    
    
    public String imprimeAST(){
        String json = "{\n \"nombre\":\""+this.nombre+"\",\n";
        
        json += "\"Clases\":[\n";
        for(Map.Entry<String, NodoClase> entry : clases.entrySet()) {
            String key = entry.getKey();
            NodoClase value = entry.getValue();
            json +="{\""+ value.getNombre() + "\": {\n"+ value.imprimirNC()+"\n}\n},";
        }
        json = json.substring(0,json.length()-1);
        json += "]\n}";
        return json;
    }
    
    public boolean verifica(TablaDeSimbolos ts)throws ExcepcionSemantica{
        for (int i = 0; i < scope.size(); i++) {
            NodoClase nC = (NodoClase) scope.get(i);
            nC.verifica(ts);
        }
        return true;
    }
    
}
