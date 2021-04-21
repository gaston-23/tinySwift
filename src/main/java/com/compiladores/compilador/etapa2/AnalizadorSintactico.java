/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa2;

/**
 *
 * @author gx23
 */
public class AnalizadorSintactico {
    
    Program(){
        C();
        ClaseMain();
        C();
    }
    
    C(){
        Clase();
        C();
    }
    
    ClaseMain(){
        Macheo("class");
        Macheo("Main");
        H();
        Macheo("{");
        M();
        Macheo("static");
        Macheo("func");
        Macheo("void");
        Macheo("main");
        Macheo("(");
        Macheo(")");
        Bloque();
        M();
        Macheo("}");
    }
    
    H(){
        Herencia();
    }
    
    M(){
        Miembro();
        M();
    }
    
    Clase(){
        Macheo("class");
        Macheo("id");
        H();
        Macheo("{");
        M();
        Macheo("}");
    }
    
    Herencia(){
        Macheo(":");
        Tipo();
    }
    
    Miembro(){
        Atributo();
        Metodo();
        Constante();
        Constructor();
    }
    
    Constructor(){
        Macheo("init");
        Argumentos-Formales();
        Bloque();
    }
    
    Atributo(){
        V();
        Macheo("var");
        Tipo();
        Macheo(":");
        Lista-Declaracion-Variables();
        Macheo(";");
    }
    
    V(){
        Visibilidad();
    }
    
    Metodo(){
        F();
        Macheo("func");
        Tipo-Metodo();
        Macheo("id");
        Argumentos-Formales();
        Bloque();
    }
    
    F(){
        Forma-Metodo();
    }
    
    Visibilidad(){
        Macheo("private");
    }
    
    Forma-Metodo(){
        Macheo("static");
    }
    
    Constante(){
        Macheo("let");
        Tipo-Primitivo();
        Macheo(":");
        Macheo("id");
        Macheo("=");
        Expresion();
        Macheo(";");
    }
    
    Argumentos-Formales(){
        Macheo("(");
        L();
        Macheo(")");
    }
    
    L(){
        Lista-Argumentos-Formales();
    }
    
    Lista-Argumentos-Formales(){
        Argumento-Formal();
        Lista-Argumentos-Formales-Prima();
    }
    
    Lista-Argumentos-Formales-Prima(){
        Macheo(",");
        Lista-Argumentos-Formales();
    }
    
    Argumento-Formal(){
        Tipo();
        Macheo(":");
        Macheo("id");
    }
    
    Tipo-Metodo(){
        Tipo();
        Macheo("void");
    }
    
    Tipo(){
        Tipo-Primitivo();
        Tipo-Referencia();
    }
    
    Tipo-Primitivo(){
        Macheo("String");
        Macheo("Bool");
        Macheo("Int");
        Macheo("Char");
    }
    
    Tipo-Referencia(){
        Macheo("id");
    }
    
    Bloque(){
        Macheo("{");
        S();
        Macheo("}");
    }
    
    S(){
        Sentencia();
        S();
    }
    
    Sentencia(){
        
    }
    
    Sentencia-Prima(){
        Macheo("else");
        Sentencia();
    }
    
    X(){
        Expresion();
    }
    
    Lista-Declaracion-Variables(){
        Macheo("id");
        Lista-Declaracion-Variables-Prima();
    }
    
    Lista-Declaracion-Variables-Primas(){
        Macheo(",");
        Lista-Declaracion-Variables();
    }
    
    Asignacion(){
        AccesoVar-Simple();
        Macheo("=");
        Expresion();
        
        AccesoSelf-Simple();
        Macheo("=");
        Expresion();
    }
    
    AccesoVar-Simple(){
        Macheo("id");
        Enc();
    }
    
    AccesoSelf-Simple(){
        Macheo("self");
        Enc();
    }
    
    Enc(){
        Encadenado-Simple();
        Enc();
    }
    
    Encadenado-Simple(){
        Macheo(".");
        Macheo("id");
    }
    
    Sentencia-Simple(){
        Macheo("(");
        Expresion();
        Macheo(")");
    }
    
    Expresion(){
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
        Macheo("-");
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
        enc-mul();
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
        llamada-Metodo();
        llamada-MetodoEstatico();
        llamada-Constructor();
    }
    
    expresionParentizada(){
        Macheo("(");
        expresion();
        Macheo(")");
        enc-mul();
    }
    
    enc-mul(){
        encadenado();
    }
    
    accesoSelf(){
        macheo("self");
        enc-mul();
    }
    
    accesoVar(){
        macheo("id");
        enc-mul();
    }
    
    llamada-Metodo(){
        macheo("id");
        argumentos-actuales();
        enc-mul();
    }
    
    llamada-Metodo-Estatico(){
        macheo("id");
        macheo(".");
        llamada-metodo();
        enc-mul();
    }
    
    llamada-Constructor(){
        macheo("new");
        macheo("id");
        argumentos-actuales();
        enc-mul();
    }
    
    argumentos-Actuales(){
        macheo("(");
        list-Exp();
        macheo(")");
    }
    
    list-Exp(){
        lista-Expresiones();
    }
    
    lista-Expresiones(){
        expresion();
        lista-Expresiones-Prima();
    }
    
    lista-Expresiones-Prima(){
        macheo(",");
        lista-Expresiones();
    }
    
    encadenado(){
        macheo(".");
        llamada-Metodo-Encadenado();
        
        macheo(".");
        acceso-Variable-Encadenado();
    }
    
    llamada-Metodo-Encadenado(){
        macheo("id");
        argumentos-Actuales();
        enc-mul();
    }
    
    acceso-Variable-Encadenado(){
        macheo("id");
        enc-mul();
    }
}
