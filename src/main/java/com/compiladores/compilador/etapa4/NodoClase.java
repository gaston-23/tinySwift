/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa2.ExcepcionSintactica;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoClase extends NodoAST{
    private HashMap<String, NodoMetodo> metodos;
    private HashMap<String, NodoSentencia> constantes;
    private String nombre;
    private NodoMetodo constructor;
    
    public NodoClase(int filaTok,int colTok,String nombre){
        super(filaTok,colTok);
        this.nombre = nombre;
        this.metodos = new HashMap<>();
        this.constantes = new HashMap<>();
    }

    public HashMap<String, NodoMetodo> getMetodos() {
        return metodos;
    }

    public String getNombre() {
        return nombre;
    }

    public void putMetodos(String nombre, NodoMetodo metodo) {
        this.metodos.put(nombre, metodo);
    }
    
    public void putConstantes(String nombre, NodoSentencia cte) {
        this.constantes.put(nombre, cte);
    }

    public void setConstructor(NodoMetodo constructor) {
        this.constructor = constructor;
    }

    public NodoMetodo getConstructor() {
        return constructor;
    }

    public HashMap<String, NodoSentencia> getConstantes() {
        return constantes;
    }
    
    
    public String imprimirNC(){
        String json = "\"nombre\":\""+this.nombre+"\"";
        
        if(!constantes.isEmpty()){
            json +=",\n\"Constantes\": [";
            for(Map.Entry<String, NodoSentencia> entry : constantes.entrySet()) {
                String key = entry.getKey();
                NodoSentencia value = entry.getValue();
                json += "{\""+ key + "\": {\n"+value.imprimeSentencia()+"}\n},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        if(!metodos.isEmpty()){
            json += ",\n \"Metodos\": [\n";
            for(Map.Entry<String, NodoMetodo> entry : metodos.entrySet()) {
                String key = entry.getKey();
                NodoMetodo value = entry.getValue();
                json += "{\"nombre\":\""+ key + "\",\n"+value.imprimeMet()+"\n},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        if(this.constructor != null){
            json += ",\n\"Constructor\":";
            json += this.constructor != null? "{"+this.constructor.imprimeMet()+"\n}" : "\"null\"";
        }
        
        
        json +="\n";
        return json;
    }

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica,ExcepcionSintactica {
        if(!constantes.isEmpty()){
            for(Map.Entry<String, NodoSentencia> entry : constantes.entrySet()) {
                String key = entry.getKey();
                NodoAsignacion value = (NodoAsignacion) entry.getValue();
                value.verifica(ts);
                // ts.getClases().get(key).incTamaño(ts.getClases().get(value.getTipo(ts)).getTamaño());
            }
            
        }
        if(!metodos.isEmpty()){
            
            for(Map.Entry<String, NodoMetodo> entry : metodos.entrySet()) {
                String key = entry.getKey();
                NodoMetodo value = entry.getValue();
                value.verifica(ts);
            }
            
        }
        if(this.constructor != null){
            this.constructor.verifica(ts);
        }
        return true; 
    }
    
    
}
