/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa2.ExcepcionSintactica;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;

/**
 *
 * @author  Gaston Cavallo
 */
public class NodoAsignacion extends NodoExpresion{
    
    private NodoExpresion izq;    
    private NodoExpresion der;

    private String tipoAsig;
    
    public NodoAsignacion(int filaTok,int colTok,NodoExpresion izqui, NodoExpresion dere,String tipo){
        super(filaTok,colTok);
        this.der= dere;
        this.izq = izqui;
        this.tipoAsig = tipo;
    }
    
    public NodoAsignacion(int filaTok,int colTok,NodoExpresion izqui,String tipo){
        super(filaTok,colTok);
        this.izq = izqui;
        this.tipoAsig = tipo;
    }

    public void setDer(NodoExpresion der) {
        this.der = der;
    }

    public void setIzq(NodoExpresion izq) {
        this.izq = izq;
    }

    @Override
    public String getTipo(TablaDeSimbolos ts) throws ExcepcionSemantica {
        return this.izq.getTipo(ts);
    }

    public String getTipoAsig() {
        return tipoAsig;
    }

    public NodoExpresion getIzq() {
        return izq;
    }

    public NodoExpresion getDer() {
        return der;
    }
    
    

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica,ExcepcionSintactica {
        
        if (der.getTipo(ts) == null || der.getTipo(ts).equals("void")) {
            String msg = der.getTipo(ts) == null ? "Error en la declaracion de la expresion " : "Error en la declaracion de la expresion no se puede asignar un tipo void ";
            throw new ExcepcionSemantica(der.getFila(),der.getCol(),msg,der.getNombre(),true);
        }
        if(izq.getTipo(ts).equals(der.getTipo(ts)) || der.getTipo(ts).equals("nil")){
            return der.verifica(ts);
        }else{
            
            if(ts.getClases().get(der.getTipo(ts)).hereda(izq.getTipo(ts),ts)){
                izq.setTipo(der.getTipo(ts));
                return der.verifica(ts);
            }
            String comp = this.izq.getTipo(ts)+ " y "+this.der.getTipo(ts);
            throw new ExcepcionSemantica(this.getFila(),this.getCol(),"La asignacion contiene tipos incompatibles",comp,false);
        }
    }
    
    
    @Override
    public String imprimeSentencia() {
        return "\"nodo\": \"NodoAsignacion\",\n"
                + "\"ladoIzq\":{\n"+this.izq.imprimeSentencia()+"\n},\n"
                + "\"tipoAsignacion\":\""+this.tipoAsig+"\",\n"
                + "\"ladoDer\":{"+this.der.imprimeSentencia()+"\n}";
    }

    
    
    @Override
    public String getCodigo(TablaDeSimbolos ts){
        // la $t0, value
        // li $t1, der
        // sw $t1, 0($t0)
//        String asm = "\tsw $ra, ($s7)\n";
        String asm = "";
        
        asm += this.izq.getCodigo(ts);
        asm += "\tla $t7, ($t1)\n";
        asm += this.der.getCodigo(ts);
        asm += "\tsw $t1, ($t7)\n";
        
        return asm;
    }
}
