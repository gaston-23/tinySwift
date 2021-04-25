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
    /**
     * clase encargada de mirar el proximo no terminal Lookahead = 1
     * @param w
     * @return
     * @throws ExcepcionSintactica 
     */
    private boolean verifico(String w) throws ExcepcionSintactica {
        return (tokenActual.getValor().equals(w));            
    }
    private boolean verifico(String[] w) throws ExcepcionSintactica {
        for (String string : w) {
            if((tokenActual.getValor().equals(string))){
                return true; 
            }
        }
        return false;            
    }

    private boolean program() throws ExcepcionSintactica {
        c();
        claseMain();
        c();
        return true;        
    }

    private void c() throws ExcepcionSintactica {
        if( verifico("class") ){
            clase();
            c();
        }else{
            //TODO error sintactico no machea
        }
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
        if(verifico(":")){
            herencia();
        }else{
            if(!verifico("{")){
                //TODO error sintactico no machea
            }
        }
    }

    private void m() throws ExcepcionSintactica {
        String[] args = {"private",  "static" , "init" , "let"};
        if(verifico(args)){
            miembro();
            m();
        }else{
            String[] args2 = {"static func void main ()", "}"};
            if(!verifico(args2)){
                //TODO error sintactico no machea
            }
        }
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
        if(verifico("private")){
            atributo();
        }else{
            if(verifico("static")){
                metodo();
            }else{
                if(verifico("init")){
                    constructor();
                }else{
                    if(verifico("let")){
                        constante();
                    }else{
                        String[] args2 = {"static func void main ()", "}"};
                        if(!verifico(args2)){
                            //TODO error sintactico no machea
                        }
                    }
                }
            }
        }
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
        if(verifico("private")){
            visibilidad();
        }else{
            if(!verifico("var")){
                //TODO error sintactico no machea
            }
        }
    }
    
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
        if(verifico("static")){
            formaMetodo();
        }else{
            if(!verifico("func")){
                //TODO error sintactico no machea
            }
        }
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
        String[] args = { "String" , "Bool" , "Int" , "Char" };
        if(verifico(args)){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                //TODO error sintactico no machea
            }
        }
    }

    private void listaArgumentosFormales() throws ExcepcionSintactica {
        String[] args = { "String" , "Bool" , "Int" , "Char" };
        if(verifico(args)){
            argumentoFormal();
            listaArgumentosFormalesPrima();
        }else{
            //TODO error sintactico no machea
        }
    }

    private void listaArgumentosFormalesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                //TODO error sintactico no machea
            }
        }
    }

    private void argumentoFormal() throws ExcepcionSintactica {
        tipo();
        if(!macheo(":") || !macheo("id")){
            //TODO error sintactico no machea
        }
    }

    private void tipoMetodo() throws ExcepcionSintactica {
        String[] args = { "String","Bool","Int","Char","id" };
        if(verifico(args)){
            tipo();
        } 
        else{
            if(!macheo("void")){
                //TODO error sintactico no machea
            }
        }
    }

    private void tipo() throws ExcepcionSintactica {
        String[] args = { "String","Bool","Int","Char"};
        if(verifico(args)){
            tipoPrimitivo(); //TODO primeros de tipoPrimitivo
        }else{
            if(verifico("id")){
                tipoReferencia(); //TODO primeros de tipoReferencia
            }else{
                //TODO error sintactico no machea
            }
        }
        
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
        String[] args = { ";" , "var" , "if", "let" , "while" , "return" , "id" , "self" , "(" , "{"};
        if(verifico(args)){
            sentencia();
            s();
        }else{
            if(!verifico("}")){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void sentencia() throws ExcepcionSintactica {
        if(!macheo(";")){
            String[] args = { "id" , "self" };
            if(verifico(args)){ //TODO primeros de asignacion
                asignacion();
                if(!macheo(";")){
                     //TODO error sintactico no machea
                }
            }else{
                if(verifico("(")){ //TODO primeros de sentenciaSimple
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
                        if(macheo("if") && macheo("(")){ //TODO TEST
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
                                        if(verifico("{")){//TODO primeros de bloque
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
            if(verifico("}")){ //TODO TEST
                //TODO error sintactico no machea
            }
        }
    }
    
    private void x() throws ExcepcionSintactica {
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "intLiteral" , "stringLiteral" , "charLiteral" , "(" , "self" , "id" , "new"};
        if(verifico(args)){
            expresion();
        }else{
            String[] args2 = {")" , ";"};
            if(!verifico(args2)){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void listaDeclaracionVariables() throws ExcepcionSintactica {
        if(macheo("id")){
            listaDeclaracionVariablesPrima();
        }else{
            //TODO error sintactico no machea
        }
    }
    
    private void listaDeclaracionVariablesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaDeclaracionVariables();
        }else{
            if(!verifico(";")){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void asignacion() throws ExcepcionSintactica {
        if(verifico("id")){ //TODO primeros accesoVarSimple
            accesoVarSimple();
            
        }else{ //TODO primeros accesoSelfSimple
            if(verifico("self")){
                accesoSelfSimple();
            }else{
                //TODO error sintactico no machea
            }
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
        if(verifico(".")){ //TODO primeros encadenadoSimple
            encadenadoSimple();
            enc();
        }else{
            if(verifico("=")){
                //TODO error sintactico no machea
            }
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
            String[] args= {")" , ";"};
            if(!verifico(args)){
                //TODO error sintactico no machea
            }
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
            String[] args= {"||" , ")" , ";"};
            if(!verifico(args)){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void expIgual() throws ExcepcionSintactica {
        expCompuesta();
        expIgualPrima();
    }
    
    private void expIgualPrima() throws ExcepcionSintactica {
        String[] args = {"==" , "!="};
        if(verifico(args)){
            opIgual();
            expCompuesta();
            expIgualPrima();
        }else{
           String[] args2 = {"&&" , "||" , ")" , ";"};
            if(!verifico(args2)){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void expCompuesta() throws ExcepcionSintactica {
        expAdd();
        expCompuestaPrima();
    }
    
    private void expCompuestaPrima() throws ExcepcionSintactica {
        String[] args = {"<" , ">" , "<=" , ">="};
        if(verifico(args)){
            opCompuesto();
            expAdd();
        }else{
            String[] args2 = {"==" , "!=" , "&&" , "||" , ")" , ";"};
            if(verifico(args)){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void expAdd() throws ExcepcionSintactica {
        expMul();
        expAddPrima();
    }
    
    private void expAddPrima() throws ExcepcionSintactica {
        String[] args = { "+" , "-"};
        if(verifico(args)){
            opAdd();
            expMul();
            expAddPrima();
        }else{
            String[] args2 = { "!" , "nil" , "true" , "false" , "intLiteral" , "stringLiteral" , "charLiteral" , "(" , "self" , "id" , "new" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(verifico(args2)){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void expMul() throws ExcepcionSintactica {
        expUn();
        expMulPrima();
    }
    
    private void expMulPrima() throws ExcepcionSintactica {
        String[] args = {"*" , "/" , "%"};
        if(verifico(args)){
            opMul();
            expUn();
            expMulPrima();  
        }else{
            String[] args2 = {"+" , "-" , "!" , "nil" , "true" , "false" , "intLiteral" , "stringLiteral" , "charLiteral" , "(" , "self" , "id" , "new" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args2)){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void expUn() throws ExcepcionSintactica {
        String[] args = {"+" , "-" , "!"};
        if(verifico(args)){ 
            opUnario();
            expUn();
        }else{ 
            String[] args2 = {"nil" , "true" , "false" , "intLiteral" , "stringLiteral" , "charLiteral" , "(" , "self" , "id" , "new"};
            if(verifico(args2)){
                operando();
            }else{
                //TODO error sintactico no machea
            }  
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
        String[] args = {"nil" , "true" , "false" , "intLiteral" , "stringLiteral" , "charLiteral"};
        if(verifico(args)){ 
            literal();
        }else{
            String[] args2 = {"(" , "self" , "id" , "new"};
            if(verifico(args2)){ 
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
        if(verifico("(")){ //TODO primeros de expresionParentizada
            expresionParentizada();
        }else{
            if(verifico("self")){ //TODO primeros de accesoSelf
                accesoSelf();
            }else{
                if(verifico("new")){ //TODO primeros de accesoVar
                    llamadaConstructor();
                }else{
                    if(verifico("id")){ //TODO primeros de llamadaMetodo
                        if(verifico(".")){
                            llamadaMetodo();
                        }else{
                            
                        }
                        
                        if(verifico("(")){ //TODO primeros de llamadaMetodoEstatico
                            llamadaMetodoEstatico();
                        }else{
                            if(verifico("(")){ //TODO primeros de llamadaConstructor
                                accesoVar();
                            }else{
                            }
                        }
                    }else{
                            //TODO error sintactico no machea
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
    
    ///////////////////////////
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
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "intLiteral" , "stringLiteral" , "charLiteral" , "(" , "self" , "id" , "new" };
        if(verifico(args)){ //TODO primeros de listaExpresiones
            listaExpresiones();
        }else{
            if(!verifico(")")){
                //TODO error sintactico no machea
            }
        }
    }
    
    private void listaExpresiones() throws ExcepcionSintactica {
        expresion();
        listaExpresionesPrima();
    }
    
    private void listaExpresionesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                //TODO error sintactico no machea
            }
        }
    }
    
    /////////////////////////////////
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
