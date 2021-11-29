/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa2.ExcepcionSintactica;
import com.compiladores.compilador.etapa3.EntradaMetodo;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoMetodo extends NodoAST {
    private LinkedList<NodoSentencia> bloque;
    private LinkedList<NodoExpresion> args;
    private String nombre;
    private String tipo;
    private String padre;
    private NodoExpresion retorno;
    
    
    public NodoMetodo(int filaTok,int colTok,String tipo,String padre){
        super(filaTok,colTok);
        this.tipo = tipo;
        this.args = new LinkedList<>();
        this.bloque = new LinkedList<>();
        this.padre = padre;
    }

    public LinkedList<NodoSentencia> getBloque() {
        return bloque;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRetorno(NodoExpresion retorno) {
        this.retorno = retorno;
    }

    public void putBloque(NodoSentencia sentencia) {
        this.bloque.add(sentencia);
    }

    public LinkedList<NodoExpresion> getArgs() {
        return args;
    }
    
    public void putArg(NodoExpresion nE){
        this.args.add(nE);
    }
    
    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica,ExcepcionSintactica {
        EntradaMetodo eC = ts.getClases().get(this.padre).getMetodo(this.nombre);
            
        if(this.retorno!=null && !this.retorno.getTipo(ts).equals(eC.getTipoRetorno())){
            throw new ExcepcionSemantica(this.getFila(),this.getCol(),"El tipo de retorno no coincide con el declarado",this.nombre,false);
        }
        if(!this.bloque.isEmpty()){
            for (int i = 0; i < this.bloque.size(); i++) {
                this.bloque.get(i).verifica(ts);
            }
        }
        return true;
    }

    public String getPadre() {
        return padre;
    }

    public NodoExpresion getRetorno() {
        return retorno;
    }

    public String getTipo() {
        return tipo;
    }
    
    
    
    public String imprimeMet(){
        String json = "";
        if(this.retorno!=null){
            json += "\"Retorno\": {"+this.retorno.imprimeSentencia()+"},\n";
        }
        json += "\"Bloque\":[\n";
        if(!this.bloque.isEmpty()){
            for (int i = 0; i < this.bloque.size(); i++) {
                
                json +="{"+ this.bloque.get(i).imprimeSentencia()+"},";
            }
            json = json.substring(0,json.length()-1);
        }
        json +="]\n";
        return json;
    }
    
    @Override
    public String getCodigo(TablaDeSimbolos ts){
        String asm = "";
        if(!this.bloque.isEmpty()){
            for (int i = 0; i < this.bloque.size(); i++) {
                
                asm += this.bloque.get(i).getCodigo(ts);
            }
        }
        
        return asm;
    }
}
