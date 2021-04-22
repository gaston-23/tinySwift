/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa2;

import com.compiladores.compilador.etapa1.AnalizadorLexico;
import com.compiladores.compilador.etapa1.Token;


/**
 *
 * @author gx23
 */
public class AnalizadorSintactico {
    
    private Token tokenActual ;
    private AnalizadorLexico al;
    
    public AnalizadorSintactico (AnalizadorLexico aL) {
        this.al = aL;
        this.tokenActual = al.nextToken();
        
        //comenzar con program()
    }

    private boolean macheo(String w) throws ExcepcionSintactica {
        if(tokenActual.getValor().equals(w)){
            this.tokenActual = this.al.nextToken();
            return true;
        }else return false;
    }

    private boolean program() throws ExcepcionSintactica {
        c();
        claseMain();
        c();
        return true;        
    }

    private void c() throws ExcepcionSintactica {
        
        clase();
        
        c();
        //TODO lambda
    }

    private void claseMain() throws ExcepcionSintactica {
        if( macheo("class") && macheo("Main")){
            h();
            if(macheo("{")){
                m();
                if(macheo("static") && macheo("func") && macheo("void") && macheo("main") && macheo("(") && macheo(")")){
                    bloque();
                    m();
                    if(!macheo("}")){
                        //TODO error sintactico no machea
                    }else{
                        //TODO error sintactico no machea
                    }
                }else{
                    //TODO error sintactico no machea
                }
            }else{
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
    }

    private void h() throws ExcepcionSintactica {
        herencia();
        //TODO lambda
    }

    private void m() throws ExcepcionSintactica {
        miembro();
        m();
        //TODO lambda
    }

    private void clase() throws ExcepcionSintactica {
        if( macheo("class") && macheo("id")){
            h(); 
            if( macheo("{") ){
               m();
               if( !macheo("}") ){ 
                   //TODO error sintactico no machea
               }
            }else {
                //TODO error sintactico no machea
            }

        }else {
            //TODO error sintactico no machea
        }
        
    }

    private void herencia() throws ExcepcionSintactica {
        if(macheo(":")){
            tipo();
        }else{
            //TODO error sintactico no machea
        }
        
    }

    private void miembro() throws ExcepcionSintactica {
        atributo();
        metodo();
        constante();
        constructor();
        //TODO primeros
    }

    private void constructor() throws ExcepcionSintactica {
        if(macheo("init")){
            argumentosFormales();
            bloque();
        }else{
            //TODO error sintactico no machea
        }
    }

    private void atributo() throws ExcepcionSintactica {
        v();
        if(macheo("var")){
            tipo();
            if(macheo(":")){
                listaDeclaracionVariables();
                if(!macheo(";")){
                    //TODO error sintactico no machea
                }
            }else{
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
    }

    
    private void v() throws ExcepcionSintactica {
        visibilidad();
        //TODO  lambda
    }
    ///////////////////////
    private void metodo() throws ExcepcionSintactica {
        f();
        if (macheo("func")){
            tipoMetodo();
            if(macheo("id")){
                argumentosFormales();
                bloque();
            }else{
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
    }

    private void f() throws ExcepcionSintactica {
        formaMetodo();
        //TODO  lambda
    }

    private void visibilidad() throws ExcepcionSintactica {
        if(!macheo("private")){
            //TODO error sintactico no machea
        }
    }

    private void formaMetodo() throws ExcepcionSintactica {
        if(!macheo("static")){
            //TODO error sintactico no machea
        }
    }

    private void constante() throws ExcepcionSintactica {
        if(macheo("let")){
            tipoPrimitivo();
            if(macheo(":") && macheo("id") && macheo("=")){
                expresion();
                if(!macheo(";")){
                    //TODO error sintactico no machea
                }
            }else{
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
    }

    private void argumentosFormales() throws ExcepcionSintactica {
        if(macheo("(")){
            l();
            if(!macheo(")")){
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
        
    }

    private void l() throws ExcepcionSintactica {
        listaArgumentosFormales();
        //TODO  lambda
    }

    private void listaArgumentosFormales() throws ExcepcionSintactica {
        argumentoFormal();
        listaArgumentosFormalesPrima();
    }

    private void listaArgumentosFormalesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaArgumentosFormales();
        }
        //TODO  lambda
    }

    private void argumentoFormal() throws ExcepcionSintactica {
        tipo();
        if(!macheo(":") || !macheo("id")){
            //TODO error sintactico no machea
        }
    }

    private void tipoMetodo() throws ExcepcionSintactica {
        if(false){
            tipo();
        } //TODO primeros de tipo
        else{
            if(!macheo("void")){
                //TODO error sintactico no machea
            }
        }
    }

    private void tipo() throws ExcepcionSintactica {
        tipoPrimitivo(); //TODO primeros de tipoPrimitivo
        tipoReferencia(); //TODO primeros de tipoReferencia
    }

    private void tipoPrimitivo() throws ExcepcionSintactica {
        if(!macheo("String")){
            if(!macheo("Bool")){
                if(!macheo("Int")){
                    if(!macheo("Char")){
                         //TODO error sintactico no machea
                    }
                }
            }
        }
    }

    private void tipoReferencia() throws ExcepcionSintactica {
        if(!macheo("id")){
             //TODO error sintactico no machea
        }
    }

    private void bloque() throws ExcepcionSintactica {
        if(macheo("{")){
           s(); 
            if(!macheo("}")){
                 //TODO error sintactico no machea
            }
        }
    }

    private void s() throws ExcepcionSintactica {
        sentencia();
        s();
        //TODO  lambda
    }
    
    private void sentencia() throws ExcepcionSintactica {
        if(!macheo(";")){
            if(true){ //TODO primeros de asignacion
                asignacion();
                if(!macheo(";")){
                     //TODO error sintactico no machea
                }
            }else{
                if(true){ //TODO primeros de sentenciaSimple
                    sentenciaSimple();
                    if(!macheo(";")){
                        //TODO error sintactico no machea
                    }
                }else{
                    if(macheo("var")){
                        tipo();
                        if(macheo(":")){
                            listaDeclaracionVariables();
                            if(!macheo(";")){
                                //TODO error sintactico no machea
                            }
                        }else{
                            //TODO error sintactico no machea
                        }
                    }else{
                        if(macheo("if") && macheo("(")){
                            expresion();
                            if(macheo(")")){
                                sentencia();
                                sentenciaPrima();
                            }else{
                                //TODO error sintactico no machea
                            }
                        }else{
                            if(macheo("let")){
                                tipoPrimitivo();
                                if(macheo(":") && macheo("id") && macheo("=")){
                                    expresion();
                                    if(!macheo(";")){
                                        //TODO error sintactico no machea
                                    }
                                }else{
                                    //TODO error sintactico no machea
                                }
                            }else{
                                if(macheo("while") && macheo("(")){
                                    expresion();
                                    if(macheo(")")){
                                        sentencia();
                                    }else{
                                        //TODO error sintactico no machea
                                    }
                                }else{
                                    if(macheo("return")){ //TODO primeros de sentenciaSimple
                                        x();
                                        if(!macheo(";")){
                                            //TODO error sintactico no machea
                                        }
                                    }else{
                                        if(true){//TODO primeros de bloque
                                            bloque();
                                        }else{
                                            //TODO error sintactico no machea
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void sentenciaPrima() throws ExcepcionSintactica {
        if(macheo("else")){
            sentencia();
        }else{
            //TODO lambda
        }
        
    }
    
    private void x() throws ExcepcionSintactica {
        expresion();
        //TODO lambda
    }
    
    private void listaDeclaracionVariables() throws ExcepcionSintactica {
        if(macheo("id")){
            listaDeclaracionVariablesPrima();
        }
        
    }
    
    private void listaDeclaracionVariablesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaDeclaracionVariables();
        }else{
            //TODO lambda
        }
        
    }
    
    private void asignacion() throws ExcepcionSintactica {
        if(true){ //TODO primeros accesoVarSimple
            accesoVarSimple();
            
        }else{ //TODO primeros accesoSelfSimple
            accesoSelfSimple();
        }
        if(macheo("=")){
            expresion();
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void accesoVarSimple() throws ExcepcionSintactica {
        if(macheo("id")){
            enc();
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void accesoSelfSimple() throws ExcepcionSintactica {
        if(macheo("self")){
            enc();
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void enc() throws ExcepcionSintactica {
        if(true){ //TODO primeros encadenadoSimple
            encadenadoSimple();
            enc();
        }else{
            //TODO lambda
        }
        
    }
    
    private void encadenadoSimple() throws ExcepcionSintactica {
        if(!macheo(".") || !macheo("id")){
            //TODO error sintactico no machea
        }
    }
    
    private void sentenciaSimple() throws ExcepcionSintactica {
        if(macheo("(")){
            expresion();
            if(!macheo(")")){
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void expresion() throws ExcepcionSintactica {
        expOr();
    }
    
    private void expOr() throws ExcepcionSintactica {
        expAnd();
        expOrPrima();
    }
    
    private void expOrPrima() throws ExcepcionSintactica {
        if(macheo("||")){
            expAnd();
            expOrPrima();
        }else{
            //TODO lambda
        }
        
        
    }
    
    private void expAnd() throws ExcepcionSintactica {
        expIgual();
        expAndPrima();
    }
    
    private void expAndPrima() throws ExcepcionSintactica {
        if(macheo("&&")){
            expIgual();
            expAndPrima();
        }else{
            //TODO lambda
        }
    }
    
    private void expIgual() throws ExcepcionSintactica {
        expCompuesta();
        expIgualPrima();
    }
    
    private void expIgualPrima() throws ExcepcionSintactica {
        opIgual();
        expCompuesta();
        expIgualPrima();
        //TODO lambda
    }
    
    private void expCompuesta() throws ExcepcionSintactica {
        expAdd();
        expCompuestaPrima();
    }
    
    private void expCompuestaPrima() throws ExcepcionSintactica {
        opCompuesto();
        expAdd();
        //TODO lambda
    }
    
    private void expAdd() throws ExcepcionSintactica {
        expMul();
        expAddPrima();
    }
    
    private void expAddPrima() throws ExcepcionSintactica {
        opAdd();
        expMul();
        expAddPrima();
        //TODO lambda
    }
    
    private void expMul() throws ExcepcionSintactica {
        expUn();
        expMulPrima();
    }
    
    private void expMulPrima() throws ExcepcionSintactica {
        opMul();
        expUn();
        expMulPrima();
        //TODO lambda
    }
    
    private void expUn() throws ExcepcionSintactica {
        if(true){ //TODO primeros opUnario
            opUnario();
            expUn();
        }else{ //TODO primeros operando
            operando();
        }
        
        
        
    }
    
    private void opIgual() throws ExcepcionSintactica {
        if(!macheo("==")){
            if(!macheo("!=")){
                //TODO error sintactico no machea
            }
        }
        
    }
    
    private void opCompuesto() throws ExcepcionSintactica {
        if(!macheo("<") || !macheo(">") || !macheo("<=") || !macheo(">=")){
            //TODO error sintactico no machea
        }
    }
    
    private void opAdd() throws ExcepcionSintactica {
        if(!macheo("+") || !macheo("-") ){
            //TODO error sintactico no machea
        }
    }
    
    private void opUnario() throws ExcepcionSintactica {
        if(!macheo("+") || !macheo("") || !macheo("!")){
            //TODO error sintactico no machea
        }
    }
    
    private void opMul() throws ExcepcionSintactica {
        if(!macheo("*") || !macheo("/") || !macheo("%")){
            //TODO error sintactico no machea
        }
    }
    
    private void operando() throws ExcepcionSintactica {
        if(true){ //TODO primeros de literal
            literal();
        }else{
            if(true){ //TODO primeros de primario
                primario();
                encmul();
            }else{
                //TODO error sintactico no machea
            }
        }
        
        
    }
    
    private void literal() throws ExcepcionSintactica {
        if(!macheo("nil") || !macheo("true") || !macheo("false") || !macheo("intLiteral") || !macheo("stringLiteral") || !macheo("charLiteral")){
            //TODO error sintactico no machea
        }
    }
    ///////////////////////////////////////////
    private void primario() throws ExcepcionSintactica {
        if(true){ //TODO primeros de expresionParentizada
            expresionParentizada();
        }else{
            if(true){ //TODO primeros de expresionParentizada
                accesoSelf();
            }else{
                if(true){ //TODO primeros de expresionParentizada
                    accesoVar();
                }else{
                    if(true){ //TODO primeros de expresionParentizada
                        llamadaMetodo();
                    }else{
                        if(true){ //TODO primeros de expresionParentizada
                            llamadaMetodoEstatico();
                        }else{
                            if(true){ //TODO primeros de expresionParentizada
                                llamadaConstructor();
                            }else{
                                //TODO error sintactico no machea
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void expresionParentizada() throws ExcepcionSintactica {
        if(macheo("(")){
            expresion();
            if(macheo(")")){
                encmul();
            }else{
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void encmul() throws ExcepcionSintactica {
        if(true){ //TODO primeros de encadenado
            encadenado();
        }
        //TODO lambda
    }
    
    private void accesoSelf() throws ExcepcionSintactica {
        if(macheo("self")){
            encmul();
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void accesoVar() throws ExcepcionSintactica {
        if(macheo("id")){
            encmul();
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void llamadaMetodo() throws ExcepcionSintactica {
        if(macheo("id")){
            argumentosActuales();
            encmul();
        }else{
            //TODO error sintactico no machea
        }
        
    }
    
    private void llamadaMetodoEstatico() throws ExcepcionSintactica {
        if(macheo("id") && macheo(".")){
            llamadaMetodo();
            encmul();
        }else{
            //TODO error sintactico no machea
        }
        
    }
    
    private void llamadaConstructor() throws ExcepcionSintactica {
        if(macheo("new") && macheo("id")){
            argumentosActuales();
            encmul();
        }else{
            //TODO error sintactico no machea
        }
        
    }
    
    private void argumentosActuales() throws ExcepcionSintactica {
        if(macheo("(")){
            listExp();
            if(!macheo(")")){
                //TODO error sintactico no machea
            }
        }else{
            //TODO error sintactico no machea
        }
        
    }
    
    private void listExp() throws ExcepcionSintactica {
        if(true){ //TODO primeros de listaExpresiones
            listaExpresiones();
        }
        //TODO lambda
    }
    
    private void listaExpresiones() throws ExcepcionSintactica {
        expresion();
        listaExpresionesPrima();
    }
    
    private void listaExpresionesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaExpresiones();
        }else{
            //TODO lambda
        }
    }
    
    private void encadenado() throws ExcepcionSintactica {
        if(macheo(".")){
            if(true){ //TODO primeros de llamadaMetodoEncadenado
                llamadaMetodoEncadenado();
            }else{
                if(true){ //TODO primeros de accesoVariableEncadenado
                    accesoVariableEncadenado();
                }else{
                    //TODO error sintactico no machea
                }
            }
        }else{
            //TODO error sintactico no machea
        }        
    }
    
    private void llamadaMetodoEncadenado() throws ExcepcionSintactica {
        if(macheo("id")){
            argumentosActuales();
            encmul();
        }else{
            //TODO error sintactico no machea
        } 
        
    }
    
    private void accesoVariableEncadenado() throws ExcepcionSintactica {
        if(macheo("id")){
            encmul();
        }else{
            //TODO error sintactico no machea
        } 
    }
}
