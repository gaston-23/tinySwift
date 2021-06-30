/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;


/**
 *
 * @author Gaston Cavallo
 */
public class NodoExpresion extends NodoSentencia{
    
    private String nombre;
    private String tipo;
    private Object valor;
    
    public NodoExpresion(int filaTok,int colTok){
        super(filaTok,colTok);
    }    
    
    public NodoExpresion(int filaTok,int colTok,String nombre, String tipo){
        super(filaTok,colTok);
        this.nombre = nombre;
        this.tipo = tipo;
    }  
    
    public NodoExpresion(int filaTok,int colTok,String nombre){
        super(filaTok,colTok);
        this.nombre = nombre;
    }  
    
    public String getTipoImpreso(){
        return this.tipo;
    }
    
    public boolean checkIsBoolean(TablaDeSimbolos ts) throws ExcepcionSemantica{
        return this.tipo.equals("Bool");
    }

    public String getTipo(TablaDeSimbolos ts) throws ExcepcionSemantica {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica {
        return true;
    }

    @Override
    public String imprimeSentencia() {
        String valor = this.valor == null ? "null" : this.valor.toString().replace("\"", "\\\"");
        return "\"nodo\":\"NodoExpresion\",\n\"nombre\":\""+this.nombre+"\",\n\"tipo\":\""+this.tipo+"\",\n\"valor\":\""+valor+"\"";
    }

    @Override
    public String getCodigo(TablaDeSimbolos ts){
        if(nombre.equals("literal")){
            if(valor.equals("nil")){
                return "\tli $t1, 0\n";
            }else{
                if(this.tipo.equals("String")){
                    int pos = ts.getLastString();
                    ts.putLabel("string_"+pos+":", ".asciiz "+this.valor);
                    return "\tla $t1, string_"+pos+"\n";
                }
                return "\tli $t1, "+valor.toString().replace("\"", "'") +"\n";
            }
        }else{
            if(ts.getMetodoActual().getVariablesMet().containsKey(this.nombre)){
                //var de metodo
                int pos = (ts.getMetodoActual().getVariablesMet().get(this.nombre).getPosicion() + 1 )*4 + ts.getMetodoActual().getTamañoParam() ;
                return "\tla $t1, "+pos+"($fp)\n";
            }else{
                if(ts.getClaseActual().getVariablesInst().containsKey(this.nombre)){
                    //var de clase
                    return "\tla $t1, var_"+this.getNombre()+"_"+ts.getClaseActual().getNombre()+"\n";
                }else{
                    if(ts.getMetodoActual().getConstanteMet().containsKey(this.nombre)){
                        int pos = (ts.getMetodoActual().getConstanteMet().get(this.nombre).getPosicion() + 1 )*4 + ts.getMetodoActual().getTamañoParam() ;
                        return "\tla $t1, "+pos+"($fp)\n";
                    }else{
                        if(ts.getMetodoActual().getParByName(nombre) != null){
                            int pos = (ts.getMetodoActual().getParByName(nombre).getPosicion() + 1 )*4;
                            return "\tla $t1, "+pos+"($fp)\n";
                        }
                        System.out.println("Paso por aqui");//parametros
                    }
                }
            }
            return "A IMPLEMENTAR \n";
        }
    }
    
    
}
