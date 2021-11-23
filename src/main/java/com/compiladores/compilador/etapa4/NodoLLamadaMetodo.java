/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa3.EntradaMetodo;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import java.util.LinkedList;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoLLamadaMetodo extends NodoExpresion{
    
    private NodoExpresion padre;
    private String nombre;
    private String clasePadre;
    private String tipo;
    private LinkedList<NodoExpresion> args;
    private boolean metodo;
    
    public NodoLLamadaMetodo(int filaTok,int colTok,String nombre,NodoExpresion padre){
        super(filaTok,colTok);
        this.nombre = nombre;
        this.padre = padre;
        this.metodo = false;
        this.args = new LinkedList<>();
    }
    
    public NodoLLamadaMetodo(int filaTok,int colTok,String nombre,String padre){
        super(filaTok,colTok);
        this.nombre = nombre;
        this.clasePadre = padre;
        this.metodo = false;
        this.args = new LinkedList<>();
    }

    public void putArgs( NodoExpresion arg) {
        this.args.add(arg);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPadre(NodoExpresion padre) {
        this.padre = padre;
    }

    public void setIsMetodo(boolean isStatic) {
        this.metodo = isStatic;
    }

    public boolean isMetodo() {
        return metodo;
    }

    public String getClasePadre() {
        return clasePadre;
    }

    public NodoExpresion getPadre() {
        return padre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMaxPadre(){
        if(this.padre!= null && this.padre.getClass().equals(NodoExpresion.class)){
            return this.padre.getTipoImpreso()!=null? this.padre.getTipoImpreso() : this.padre.getNombre();
        }else{
            if(this.padre == null){
                if(this.getNombre().equals("constructor")){
                    return this.clasePadre;
                }else{
                    System.out.println("Verificar relacion");
                    return this.clasePadre;
                }
            }else{
                return ((NodoLLamadaMetodo)this.padre).getMaxPadre();
            }
        }
    }
    @Override
    public String getNombre() {
        return this.nombre; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica {
        if(this.tipo == null) this.getTipo(ts);
        if(this.metodo){
           String parAux = null;
            if(this.clasePadre!=null){
                parAux = this.clasePadre;
            }else{
                parAux = this.getMaxPadre();
            }
             
            EntradaMetodo eC = this.nombre.equals("constructor")? ts.getClases().get(this.clasePadre).getConstructor() : ts.getClases().get(parAux).getMetodo(this.nombre);
            for (int i = 0; i < args.size(); i++) {
                try{
                    NodoExpresion argAux = args.get(i);
                    if(!argAux.getTipo(ts).equals(eC.getParametros().get(i).getTipo())){
                        throw new ExcepcionSemantica(argAux.getFila(),argAux.getCol(),"El tipo del argumento formal no coincide con el esperado, se esperaba tipo: "+eC.getParametros().get(i).getTipo(),argAux.getTipo(ts),false);
                    }
                }catch(IndexOutOfBoundsException e){
                    throw new ExcepcionSemantica(this.getFila(),this.getCol(),"El numero de argumentos formales excede lo esperado","por lo menos "+String.valueOf(i+1)+" argumentos",false);
                }
            }
        }else{
            String parAux = null;
            if(this.clasePadre!=null){
                parAux = this.clasePadre;
            }else{
                if(!this.padre.getNombre().equals("self")){
                    parAux = this.padre.getTipo(ts);
                }
            }
            if(parAux != null && !ts.getClases().get(parAux).getConstantes().containsKey(this.nombre) && !ts.getClases().get(parAux).getVariablesInst().contains(this.nombre)){
                throw new ExcepcionSemantica(this.getFila(),this.getCol(),"El identificador no fue declarado o no es accesible por la clase",this.nombre,false);
            }
        }
        return true;
    }

    @Override
    public String getTipo(TablaDeSimbolos ts) throws ExcepcionSemantica{ //retorno ver ts?
        if(this.tipo == null){
            if(this.metodo){
                if(this.nombre.equals("constructor")){
                    this.tipo = this.clasePadre;
                    return this.tipo;
                }
                String auxPar = this.clasePadre!=null ? clasePadre : getMaxPadre();
                this.tipo = ts.getClases().get(auxPar).getMetodo(nombre).getTipoRetorno();
                return this.tipo;
                
            }else{
                String parAux = null;
                if(this.clasePadre!=null){
                    parAux = this.clasePadre;
                }else{
                    if(this.padre.getNombre().equals("self")){
                        parAux = this.padre.getValor().toString();
                    }else{
                        parAux = this.padre.getTipo(ts);
                    }
                }
                if(ts.getClases().get(parAux).getConstantes().containsKey(this.nombre)){
                    this.tipo = ts.getClases().get(parAux).getConstantes().get(nombre).getTipo();
                    return this.tipo;
                }else{
                    if(ts.getClases().get(parAux).getVariablesInst().containsKey(this.nombre)){
                        this.tipo = ts.getClases().get(parAux).getVariablesInst().get(nombre).getTipo();
                        return this.tipo;
                    }else{
                        throw new ExcepcionSemantica(this.getFila(),this.getCol(),"El identificador no fue declarado o no es accesible por la clase",this.nombre,false);
                    }
                }
            }            
        }
        return this.tipo;
    }
    
    
    
    
    @Override
    public String imprimeSentencia() {
        String padreClase = "",json="",padreSentencia ="",metodo = "";
        if(this.clasePadre!=null){
            padreClase = ",\n\"padre\":\""+this.clasePadre+"\"\n";
        }
        if(this.padre != null){
            padreSentencia = ",\n\"padre\":{"+this.padre.imprimeSentencia()+"\n}";
        }
        if(this.metodo ){
            metodo = ",\n\"isMetodo\":\""+this.isMetodo()+"\"\n";
        }
        json= "\"nodo\": \"NodoLlamadaMetodo\",\n"
                + "\"nombre\":\n\""+this.nombre+"\""
                + ",\n\"tipo\":\n\""+this.tipo+"\""
                + padreClase
                + padreSentencia 
                + metodo + "\n";
        if(!this.args.isEmpty()){
            json +=",\n \"args\":[";
            for (int i = 0; i < args.size(); i++) {
                json += "\n{"+ this.args.get(i).imprimeSentencia() +"},";
            }
            json = json.substring(0,json.length()-1);
            json += "]";
        }
        
        return json;
    }

    @Override
    public String getCodigo(TablaDeSimbolos ts) {
        String asm = "\tsw $fp, 0($sp)\n\taddiu $sp, $sp, -4\n";
        if(this.isMetodo()){
            for (int i = 0; i < args.size(); i++) {
                NodoExpresion argAux = args.get(i);
                //int size = argAux.getTipoImpreso().equals("String") ? 32 : 4;
                //int pos = (i + 1 )* size;
                asm += argAux.getCodigo(ts);
                if(!argAux.getNombre().equals("literal")){
                    asm +="\tlw $t1, 0($t1)\n";
                }
//                asm +="\tsw $t1, 0($sp)\n\taddiu $sp, $sp, -"+size+"\n";
                asm +="\tsw $t1, 0($sp)\n\taddiu $sp, $sp, -4"+"\n";
            }
        }
        if(this.clasePadre != null){
//            asm +="\tla $a0, ($t1)\n\tjal "+this.clasePadre+"_"+nombre+"\n"; //funciona con mas de un param??
            if(this.nombre.equals("constructor")){
                asm +="\tla $a0, "+this.clasePadre+"_temp\n";
            }
            asm +="\tjal "+this.clasePadre+"_"+nombre+"\n"; //funciona con mas de un param??
        }else{
            if(this.padre.getNombre().equals("self")){
                if(!this.isMetodo()){
                   return "\tla $t1, var_"+this.getNombre()+"_"+ts.getClaseActual().getNombre()+"\n";
                }
            }else{
//                asm +="\tla $a0, ($t1)\n\tjal "+this.padre.getTipoImpreso()+"_"+nombre+"\n";
                asm +="\tjal "+this.padre.getTipoImpreso()+"_"+nombre+"\n";
            }
        }
        return asm; 
    }
    
    
}
