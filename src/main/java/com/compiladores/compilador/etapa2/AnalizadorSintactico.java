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

    private void c() {
        
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
                    if(!macheo("}")) //TODO error sintactico no machea
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

    private void Miembro() throws ExcepcionSintactica {
        Atributo();
        Metodo();
        Constante();
        Constructor();
    }

    private void Constructor() throws ExcepcionSintactica {
        macheo("init");
        Argumentos - Formales();
        Bloque();
    }

    private void Atributo() throws ExcepcionSintactica {
        V();
        macheo("var");
        Tipo();
        macheo(":");
        Lista - Declaracion - Variables();
        macheo(";");
    }

    private void V() throws ExcepcionSintactica {
        Visibilidad();
    }

    private void Metodo() throws ExcepcionSintactica {
        F();
        macheo("func");
        Tipo - Metodo();
        macheo("id");
        Argumentos - Formales();
        Bloque();
    }

    private void F() throws ExcepcionSintactica {
        Forma - Metodo();
    }

    private void Visibilidad() throws ExcepcionSintactica {
        macheo("private");
    }

    private void Forma-Metodo() throws ExcepcionSintactica {
        macheo("static");
    }

    private void Constante() throws ExcepcionSintactica {
        macheo("let");
        Tipo - Primitivo();
        macheo(":");
        macheo("id");
        macheo("=");
        Expresion();
        macheo(";");
    }

    private void Argumentos-Formales() throws ExcepcionSintactica {
        macheo("(");
        L();
        macheo(")");
    }

    L() throws ExcepcionSintactica {
        Lista - Argumentos - Formales();
    }

    Lista-Argumentos-Formales() throws ExcepcionSintactica {
        Argumento - Formal();
        Lista - Argumentos - Formales - Prima();
    }

    Lista-Argumentos-Formales-Prima() throws ExcepcionSintactica {
        macheo(",");
        Lista - Argumentos - Formales();
    }

    Argumento-Formal() throws ExcepcionSintactica {
        Tipo();
        macheo(":");
        macheo("id");
    }

    Tipo-Metodo() throws ExcepcionSintactica {
        Tipo();
        macheo("void");
    }

    Tipo() throws ExcepcionSintactica {
        Tipo - Primitivo();
        Tipo - Referencia();
    }

    Tipo-Primitivo() throws ExcepcionSintactica {
        macheo("String");
        macheo("Bool");
        macheo("Int");
        macheo("Char");
    }

    Tipo-Referencia() throws ExcepcionSintactica {
        macheo("id");
    }

    Bloque() throws ExcepcionSintactica {
        macheo("{");
        S();
        macheo("}");
    }

    S() throws ExcepcionSintactica {
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
