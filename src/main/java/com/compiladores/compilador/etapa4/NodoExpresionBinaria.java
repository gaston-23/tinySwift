/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa1.Token;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoExpresionBinaria extends NodoExpresion {
    
    private NodoExpresion izq;    
    private NodoExpresion der;
    private String oper;

    
    public NodoExpresionBinaria(int filaTok,int colTok){
        super(filaTok,colTok);
    } 
    
    public NodoExpresionBinaria(int filaTok,int colTok, NodoExpresion izq, String op, NodoExpresion der){
        super(filaTok,colTok);
        this.der = der;
        this.izq = izq;
    }

    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica {
        
        if(this.oper.equals("||") || this.oper.equals("&&")){
            if(der.getTipo(ts).equals(izq.getTipo(ts)) && der.getTipo(ts).equals("Bool")){
                return true;
            }else{
                String comp = izq+ " y "+der;
                throw new ExcepcionSemantica(this.getFila(),this.getCol(),"La expresion contiene tipos incompatibles",comp,false);
            }
        }else{
            if(this.oper.equals("*") || this.oper.equals("+") || this.oper.equals("/") || this.oper.equals("%") || this.oper.equals("-") || this.oper.equals("<") || this.oper.equals(">") || this.oper.equals("<=") || this.oper.equals(">=") ){
                if(der.getTipo(ts).equals(izq.getTipo(ts)) && der.getTipo(ts).equals("Int")){
                    return true;
                }else{
                    String comp = izq+ " y "+der;
                    throw new ExcepcionSemantica(this.getFila(),this.getCol(),"La expresion contiene tipos incompatibles",comp,false);

                }
            }else{
                if(this.oper.equals("==")){
                    if(der.getTipo(ts).equals(izq.getTipo(ts))){
                        return true;
                    }else{
                        String comp = izq+ " y "+der;
                        throw new ExcepcionSemantica(this.getFila(),this.getCol(),"La expresion contiene tipos incompatibles",comp,false);
                    } 
                }
            }
        }
        System.out.println("tipo no manejado:: "+this.oper);
        return false;
    }

    public String getOper() {
        return oper;
    }

    public void setDer(NodoExpresion der) {
        this.der = der;
    }

    public void setIzq(NodoExpresion izq) {
        this.izq = izq;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public NodoExpresion getIzq() {
        return izq;
    }

    @Override
    public String getTipo(TablaDeSimbolos ts) throws ExcepcionSemantica {
        if(this.oper.equals("<") || this.oper.equals(">") || this.oper.equals("<=") || this.oper.equals(">=") || this.oper.equals("==") || this.oper.equals("!=")){
            if(der.getTipo(ts).equals(izq.getTipo(ts)) && der.getTipo(ts).equals("Int")){
                return "Bool";
            }
        }
        return der.getTipo(ts);
    }
    
    @Override
    public boolean checkIsBoolean(TablaDeSimbolos ts) throws ExcepcionSemantica  {
        return (this.getTipo(ts).equals("Bool"));
    }
    
    

    @Override
    public String imprimeSentencia() {
        return "\"nodo\": \"NodoExpresionBinaria\",\n"
                + "\"ladoIzq\":{\n"+this.izq.imprimeSentencia()+"\n},\n"
                + "\"operador\":\""+this.oper+"\",\n"
                + "\"ladoDer\":{"+this.der.imprimeSentencia()+"\n}";
    }

    @Override
    public String getCodigo(TablaDeSimbolos ts) {
        String  asm = "";
        asm += der.getCodigo(ts)+"\tmove $t8, $t1\n";
        asm += izq.getCodigo(ts);
        String op = "";
        
        if (this.oper.equals("||")) op = "or"; 
        else if (this.oper.equals("&&")) op = "and"; 
        else if (this.oper.equals("==")) op = "seq"; 
        else if (this.oper.equals("<")) op = "slt"; 
        else if (this.oper.equals(">")) op = "sgt"; 
        else if (this.oper.equals("<=")) op = "sle"; 
        else if (this.oper.equals(">=")) op = "sge"; 
        else if (this.oper.equals("!=")) op = "sne"; 
        else if (this.oper.equals("+")) op = "add"; 
        else if (this.oper.equals("-")) op = "sub"; 
        else if (this.oper.equals("*")) op = "mul"; 
        else if (this.oper.equals("/")) op = "div"; 
        else if (this.oper.equals("%")) op = "div";  
        
        if(der.getClass().equals(NodoExpresion.class) && izq.getClass().equals(NodoExpresion.class) && der.getNombre().equals("literal") && izq.getNombre().equals("literal")){
            asm += "\t"+op+" $t1,$t8,$t1\n";
        }else{
            if(der.getClass().equals(NodoExpresion.class) && der.getNombre().equals("literal")){
                asm += "\tlw $t1, ($t1)\n";
                asm += "\t"+op+" $t1,$t1,$t8\n";
            }else{
                if(izq.getClass().equals(NodoExpresion.class) && izq.getNombre().equals("literal")){
                    asm += "\tlw $t8, ($t8)\n";
                    asm += "\t"+op+" $t1,$t1,$t8\n";
                }else{
                    if(der.getClass().equals(NodoExpresionBinaria.class)){
                        asm += "\tlw $t1, ($t1)\n";
                        asm += "\t"+op+" $t1,$t1,$t8\n";
                    }else{
                        asm += "\tlw $t8, ($t8)\n";
                        asm += "\tlw $t1, ($t1)\n";
                        asm += "\t"+op+" $t1,$t1,$t8\n";
                    }
                    
                }
            }
        }
        if(this.oper.equals("%")) asm += "\tmfhi $t1";
        
        return asm; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
