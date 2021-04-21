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
        macheo("func");
        tipoMetodo();
        macheo("id");
        argumentosFormales();
        Bloque();
    }

    private void f() throws ExcepcionSintactica {
        formaMetodo();
    }

    private void visibilidad() throws ExcepcionSintactica {
        macheo("private");
    }

    private void formaMetodo() throws ExcepcionSintactica {
        macheo("static");
    }

    private void constante() throws ExcepcionSintactica {
        macheo("let");
        tipoPrimitivo();
        macheo(":");
        macheo("id");
        macheo("=");
        expresion();
        macheo(";");
    }

    private void argumentosFormales() throws ExcepcionSintactica {
        macheo("(");
        l();
        macheo(")");
    }

    private void L() throws ExcepcionSintactica {
        listaArgumentosFormales();
    }

    private void listaArgumentosFormales() throws ExcepcionSintactica {
        ArgumentoFormal();
        listaArgumentosFormalesPrima();
    }

    private void listaArgumentosFormalesPrima() throws ExcepcionSintactica {
        macheo(",");
        listaArgumentosFormales();
    }

    private void argumentoFormal() throws ExcepcionSintactica {
        tipo();
        macheo(":");
        macheo("id");
    }

    private void tipoMetodo() throws ExcepcionSintactica {
        tipo();
        macheo("void");
    }

    private void tipo() throws ExcepcionSintactica {
        tipoPrimitivo();
        tipoReferencia();
    }

    private void tipoPrimitivo() throws ExcepcionSintactica {
        macheo("String");
        macheo("Bool");
        macheo("Int");
        macheo("Char");
    }

    private void tipoReferencia() throws ExcepcionSintactica {
        macheo("id");
    }

    private void bloque() throws ExcepcionSintactica {
        macheo("{");
        s();
        macheo("}");
    }

    private void s() throws ExcepcionSintactica {
        sentencia();
        s();
    }
    
    sentencia(){
        
    }
    
    sentenciaPrima(){
        Macheo("else");
        sentencia();
    }
    
    X(){
        expresion();
    }
    
    listaDeclaracionVariables(){
        Macheo("id");
        listaDeclaracionVariablesPrima();
    }
    
    listaDeclaracionVariablesPrimas(){
        Macheo(",");
        listaDeclaracionVariables();
    }
    
    Asignacion(){
        AccesoVarSimple();
        Macheo("=");
        expresion();
        
        AccesoSelfSimple();
        Macheo("=");
        expresion();
    }
    
    AccesoVarSimple(){
        Macheo("id");
        Enc();
    }
    
    AccesoSelfSimple(){
        Macheo("self");
        Enc();
    }
    
    Enc(){
        encadenadoSimple();
        Enc();
    }
    
    encadenadoSimple(){
        Macheo(".");
        Macheo("id");
    }
    
    sentenciaSimple(){
        Macheo("(");
        expresion();
        Macheo(")");
    }
    
    expresion(){
        ExpOr();
    }
    
    ExpOr(){
        ExpAnd();
        ExpOrPrima();
    }
    
    ExpOrPrima(){
        Macheo("||");
        ExpAnd();
        ExpOrPrima();
    }
    
    ExpAnd(){
        ExpIgual();
        ExpAndPrima();
    }
    
    ExpAndPrima(){
        Macheo("&&");
        ExpIgual();
        ExpAndPrima();
    }
    
    expIgual(){
        expCompuesta();
        expIgualPrima();
    }
    
    expIgualPrima(){
        opIgual();
        expCompuesta();
        expIgualPrima();
    }
    
    expCompuesta(){
        opCompuesto();
        expAdd();
    }
    
    expAdd(){
        expMul();
        expAddPrima();
    }
    
    expAddPrima(){
        opAdd();
        expMul();
        expAddPrima();
    }
    
    expMul(){
        expUn();
        expMulPrima();
    }
    
    expMulPrima(){
        opMul();
        expUn();
        expMulPrima();
    }
    
    expUn(){
        opUnario();
        expUn();
        
        operando();
    }
    
    opIgual(){
        Macheo("==");
        Macheo("!=");
    }
    
    opCompuesto(){
        Macheo("<");
        Macheo(">");
        Macheo("<=");
        Macheo(">=");
    }
    
    opAdd(){
        Macheo("+");
        Macheo("-");
    }
    
    opUnario(){
        Macheo("+");
        Macheo("");
        Macheo("!");
    }
    
    opMul(){
        Macheo("*");
        Macheo("/");
        Macheo("%");
    }
    
    operando(){
        literal();
        
        primario();
        encmul();
    }
    
    literal(){
        Macheo("nil");
        Macheo("true");
        Macheo("false");
        Macheo("intLiteral");
        Macheo("stringLiteral");
        Macheo("charLiteral");
    }
    
    primario(){
        expresionParentizada();
        accesoSelf();
        accesoVar();
        llamadaMetodo();
        llamadaMetodoEstatico();
        llamadaConstructor();
    }
    
    expresionParentizada(){
        Macheo("(");
        expresion();
        Macheo(")");
        encmul();
    }
    
    encmul(){
        encadenado();
    }
    
    accesoSelf(){
        macheo("self");
        encmul();
    }
    
    accesoVar(){
        macheo("id");
        encmul();
    }
    
    llamadaMetodo(){
        macheo("id");
        argumentosactuales();
        encmul();
    }
    
    llamadaMetodoEstatico(){
        macheo("id");
        macheo(".");
        llamadametodo();
        encmul();
    }
    
    llamadaConstructor(){
        macheo("new");
        macheo("id");
        argumentosactuales();
        encmul();
    }
    
    argumentosActuales(){
        macheo("(");
        listExp();
        macheo(")");
    }
    
    listExp(){
        listaexpresiones();
    }
    
    listaexpresiones(){
        expresion();
        listaexpresionesPrima();
    }
    
    listaexpresionesPrima(){
        macheo(",");
        listaexpresiones();
    }
    
    encadenado(){
        macheo(".");
        llamadaMetodoencadenado();
        
        macheo(".");
        accesoVariableencadenado();
    }
    
    llamadaMetodoencadenado(){
        macheo("id");
        argumentosActuales();
        encmul();
    }
    
    accesoVariableencadenado(){
        macheo("id");
        encmul();
    }
}
