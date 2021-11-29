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
public class NodoExpresionUnaria extends NodoExpresion{
    
    private NodoExpresion der;
    private String oper;

    
    public NodoExpresionUnaria(int filaTok,int colTok){
        super(filaTok,colTok);
    } 
    
    public NodoExpresionUnaria(int filaTok,int colTok, String op, NodoExpresion der){
        super(filaTok,colTok);
        this.der = der;
    }

    @Override
    public boolean checkIsBoolean(TablaDeSimbolos ts) throws ExcepcionSemantica  {
        return (this.oper.equals("!") && der.getTipo(ts).equals("Bool"));
    }
    
    

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica {
        String derT = der.getTipo(ts);
        if(this.oper.equals("!") ){
            
            if(derT.equals("Bool")){
                return true;
            }else{
                throw new ExcepcionSemantica(super.getFila(),super.getCol(),"La expresion contiene tipos incompatibles","operador: "+this.oper+" y tipo: "+derT,false);
            }
        }else{
            if(oper.equals("-") && oper.equals("+") ){
                if(derT.equals("Int")){
                    return true;
                }else{
                    throw new ExcepcionSemantica(super.getFila(),super.getCol(),"La expresion contiene tipos incompatibles","operador: "+this.oper+" y tipo: "+derT,false);

                }
            }
        }
        return false;
    }

    public void setDer(NodoExpresion der) {
        this.der = der;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    @Override
    public String getTipo(TablaDeSimbolos ts) throws ExcepcionSemantica {
        return this.der.getTipo(ts);
    }
    
    @Override
    public String imprimeSentencia() {
        return "\"nodo\": \"NodoExpresionUnaria\",\n"
                + "\"operador\":\""+this.oper+"\",\n"
                + "\"ladoDer\":{"+this.der.imprimeSentencia()+"\n}";
    }

    @Override
    public String getCodigo(TablaDeSimbolos ts) {
        if(this.oper.equals("!") ){
            return der.getCodigo(ts)+"\tsubu $t0, $0, $t0\n";
            
        }else{
            if(oper.equals("-")){
                return der.getCodigo(ts)+"\tsubu $t0, $0, $t0\n";
            }else{
                return der.getCodigo(ts)+"\n";
            }
        }
    }
    
    
}
