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
 * @author Gaston Cavallo
 * @author Mariel Volman
 */
public class AnalizadorSintactico {
    
    private Token tokenActual ;
    private AnalizadorLexico al;
    
    public AnalizadorSintactico (AnalizadorLexico aL) {
        this.al = aL;
        this.tokenActual = al.nextToken();
        boolean exito = true;
        try{
            this.program();

        }catch(ExcepcionSintactica eS){
            System.out.println(eS.mensaje);
            exito = false;
        }
        if(exito) System.out.println("CORRECTO: ANALISIS SINTACTICO");
    }

    private boolean macheo(String w) throws ExcepcionSintactica {
        System.out.println("mach: v: "+tokenActual.getValor()+" p: "+tokenActual.getpReservada());
        if(tokenActual.getValor().equals(w) || tokenActual.getpReservada().equals(w)){
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
        System.out.println("ver: v: "+tokenActual.getValor()+" p: "+tokenActual.getpReservada());
        return (tokenActual.getValor().equals(w) || tokenActual.getpReservada().equals(w));            
    }
    private boolean verifico(String[] w) throws ExcepcionSintactica {
        System.out.println("ver: v: "+tokenActual.getValor()+" p: "+tokenActual.getpReservada());
        for (String string : w) {
            if(tokenActual.getValor().equals(string) || tokenActual.getpReservada().equals(string)){
                return true; 
            }
        }
        return false;            
    }

    private boolean program() throws ExcepcionSintactica {
        if(macheo("class")){
           c(); 
        }else{
            if(verifico("EOF")){
                return true;  
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o 'EOF'", tokenActual.getValor());
            }
        }
        return true;
    }

    private void c() throws ExcepcionSintactica {
        if( verifico("id_clase") ){
            clase();
            program();
        }else{
            if(verifico("Main")){
                claseMain();
            }else{
                System.out.println("c");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o EOF", tokenActual.getValor());
            }
        }
    }
    
    private void claseMain() throws ExcepcionSintactica {
        System.out.println("33333333333333333333333333333333333333333");
        if( macheo("Main")){
            h();
            if(macheo("{")){
                m();
                if(macheo("static") && macheo("func") && macheo("void") && macheo("main") && macheo("(") && macheo(")")){
                    bloque();
                    m();
                    if(!macheo("}")){
                        System.out.println("claseMain");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
                    }
                }else{
                    System.out.println("claseMain");            throw new ExcepcionSintactica(tokenActual,"se esperaba una declaracion de tipo 'static func void main ()'", tokenActual.getValor());
                }
            }else{
                System.out.println("claseMain");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }else{
            System.out.println("claseMain");
            throw new ExcepcionSintactica(tokenActual,"se esperaba una declaracion de tipo 'class Main'", tokenActual.getValor());
        }
    }

    private void h() throws ExcepcionSintactica {
        if(verifico(":")){
            herencia();
        }else{
            if(!verifico("{")){
                System.out.println("h");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' o '{'", tokenActual.getValor());
            }
        }
    }

    private void m() throws ExcepcionSintactica {
        String[] args = {"private",  "static" , "init" , "let", "var", "func"};
        if(verifico(args)){
            miembro();
            m();
        }else{
            String[] args2 = {"static func void main ()", "}"};
            if(!verifico(args2)){
                System.out.println("m");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let', 'var', 'func', '{' o una declaracion de tipo 'static func void main ()'", tokenActual.getValor());
            }
        }
    }

    private void clase() throws ExcepcionSintactica {
        if( macheo("id_clase")){
            h(); 
            if( macheo("{") ){
               m();
               if( !macheo("}") ){ 
                   System.out.println("clase");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
               }
            }else {
                System.out.println("clase");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
            }

        }else {
            System.out.println("clase");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token  'id_clase'", tokenActual.getValor());
        }
        
    }

    private void herencia() throws ExcepcionSintactica {
        if(macheo(":")){
            tipo();
        }else{
            System.out.println("herencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
        
    }

    private void miembro() throws ExcepcionSintactica {
        if(verifico("private") || verifico("var")){
            atributo();
        }else{
            if(verifico("static") || verifico ("func")){
                metodo();
            }else{
                if(verifico("init")){
                    constructor();
                }else{
                    if(verifico("let")){
                        constante();
                    }else{
                        System.out.println("miembro");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let'", tokenActual.getValor());
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
            System.out.println("constructor");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'init'", tokenActual.getValor());
        }
    }

    private void atributo() throws ExcepcionSintactica {
        v();
        if(macheo("var")){
            tipo();
            if(macheo(":")){
                listaDeclaracionVariables();
                if(!macheo(";")){
                    System.out.println("atributo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                System.out.println("atributo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
            }
        }else{
            System.out.println("atributo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'var'", tokenActual.getValor());
        }
    }
    
    private void v() throws ExcepcionSintactica {
        if(verifico("private")){
            visibilidad();
        }else{
            if(!verifico("var")){
                System.out.println("v");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'var'", tokenActual.getValor());
            }
        }
    }
    
    private void metodo() throws ExcepcionSintactica {
        f();
        if (macheo("func")){
            tipoMetodo();
            if(macheo("id_objeto")){
                argumentosFormales();
                bloque();
            }else{
                System.out.println("metodo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }
        }else{
            System.out.println("metodo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'func'", tokenActual.getValor());
        }
    }

    private void f() throws ExcepcionSintactica {
        if(verifico("static")){
            formaMetodo();
        }else{
            if(!verifico("func")){
                System.out.println("f");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'static' o 'func'", tokenActual.getValor());
            }
        }
    }

    private void visibilidad() throws ExcepcionSintactica {
        if(!macheo("private")){
            System.out.println("visibilidad");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private'", tokenActual.getValor());
        }
    }

    private void formaMetodo() throws ExcepcionSintactica {
        if(!macheo("static")){
            System.out.println("formaMetodo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'static'", tokenActual.getValor());
        }
    }

    private void constante() throws ExcepcionSintactica {
        if(macheo("let")){
            tipoPrimitivo();
            if(macheo(":") && macheo("id_objeto") && macheo("=")){
                expresion();
                if(!macheo(";")){
                    System.out.println("constante");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                System.out.println("constante");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' , 'id_objeto' o '='", tokenActual.getValor());
            }
        }else{
            System.out.println("constante");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'let' ", tokenActual.getValor());
        }
    }

    private void argumentosFormales() throws ExcepcionSintactica {
        if(macheo("(")){
            l();
            if(!macheo(")")){
                System.out.println("argumentosFormales");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' ", tokenActual.getValor());
            }
        }else{
            System.out.println("argumentosFormales");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());
        }
        
    }

    private void l() throws ExcepcionSintactica {
        String[] args = { "String" , "Bool" , "Int" , "Char" };
        if(verifico(args)){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                System.out.println("l");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
            }
        }
    }

    private void listaArgumentosFormales() throws ExcepcionSintactica {
        String[] args = { "String" , "Bool" , "Int" , "Char" };
        if(verifico(args)){
            argumentoFormal();
            listaArgumentosFormalesPrima();
        }else{
            System.out.println("listaArgumentosFormales");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
        }
    }

    private void listaArgumentosFormalesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                System.out.println("listaArgumentosFormalesPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ')'", tokenActual.getValor());
            }
        }
    }

    private void argumentoFormal() throws ExcepcionSintactica {
        tipo();
        if(!macheo(":") || !macheo("id_objeto")){
            System.out.println("argumentoFormal");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' o 'id_objeto'", tokenActual.getValor());
        }
    }

    private void tipoMetodo() throws ExcepcionSintactica {
        String[] args = { "String","Bool","Int","Char","id_clase" };
        if(verifico(args)){
            tipo();
        }else{
            if(!macheo("void")){
                System.out.println("tipoMetodo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char' o 'id_clase", tokenActual.getValor());
            }
        }
    }

    private void tipo() throws ExcepcionSintactica {
        String[] args = { "String","Bool","Int","Char"};
        if(verifico(args)){
            tipoPrimitivo(); 
        }else{
            if(verifico("id_clase")){
                tipoReferencia(); 
            }else{
                System.out.println("tipo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char' o 'id_clase", tokenActual.getValor());
            }
        }
        
    }

    private void tipoPrimitivo() throws ExcepcionSintactica {
        if(!macheo("String")){
            if(!macheo("Bool")){
                if(!macheo("Int")){
                    if(!macheo("Char")){
                        System.out.println("tipoPrimitivo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
                    }
                }
            }
        }
    }

    private void tipoReferencia() throws ExcepcionSintactica {
        if(!macheo("id_clase")){
            System.out.println("tipoReferencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
        }
    }

    private void bloque() throws ExcepcionSintactica {
        if(macheo("{")){
           s(); 
            if(!macheo("}")){
                System.out.println("bloque");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }
    }

    private void s() throws ExcepcionSintactica {
        String[] args = { ";" , "var" , "if", "let" , "while" , "return" , "id_objeto" , "self" , "(" , "{"};
        if(verifico(args)){
            sentencia();
            s();
        }else{
            if(!verifico("}")){
                System.out.println("s");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';' , 'var' , 'if', 'let' , 'while' , 'return' , 'id_objeto' , 'self' , '(' , '{' , '}'", tokenActual.getValor());
            }
        }
    }
    
    private void sentencia() throws ExcepcionSintactica {
        if(!macheo(";")){
            String[] args = { "id_objeto" , "self" };
            if(verifico(args)){
                asignacion();
                if(!macheo(";")){
                    System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                if(verifico("(")){
                    sentenciaSimple();
                    if(!macheo(";")){
                        System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                    }
                }else{
                    if(macheo("var")){
                        tipo();
                        if(macheo(":")){
                            listaDeclaracionVariables();
                            if(!macheo(";")){
                                System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                            }
                        }else{
                            System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
                        }
                    }else{
                        if(macheo("if") && macheo("(")){ //TODO TEST
                            expresion();
                            if(macheo(")")){
                                sentencia();
                                sentenciaPrima();
                            }else{
                                System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'if' , '(' o ')'", tokenActual.getValor());
                            }
                        }else{
                            if(macheo("let")){
                                tipoPrimitivo();
                                if(macheo(":") && macheo("id_objeto") && macheo("=")){
                                    expresion();
                                    if(!macheo(";")){
                                        System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                                    }
                                }else{
                                    System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':', 'id_objeto', '='", tokenActual.getValor());
                                }
                            }else{
                                if(macheo("while") && macheo("(")){
                                    expresion();
                                    if(macheo(")")){
                                        sentencia();
                                    }else{
                                        System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
                                    }
                                }else{
                                    if(macheo("return")){ 
                                        x();
                                        if(!macheo(";")){
                                            System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                                        }
                                    }else{
                                        if(verifico("{")){
                                            bloque();
                                        }else{
                                            System.out.println("sentencia");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{', 'return', 'while', '(', 'let', 'if', 'var', 'self' ", tokenActual.getValor());
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
            if(!verifico("}")){ //TODO TEST
                System.out.println("sentenciaPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}' o 'else'", tokenActual.getValor());
            }
        }
    }
    
    private void x() throws ExcepcionSintactica {
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new"};
        if(verifico(args)){
            expresion();
        }else{
            String[] args2 = {")" , ";"};
            if(!verifico(args2)){
                System.out.println("x");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new', ')' , ';'", tokenActual.getValor());
            }
        }
    }
    
    private void listaDeclaracionVariables() throws ExcepcionSintactica {
        if(macheo("id_objeto")){
            listaDeclaracionVariablesPrima();
        }else{
            System.out.println("listaDeclaracionVariables");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void listaDeclaracionVariablesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaDeclaracionVariables();
        }else{
            if(!verifico(";")){
                System.out.println("listaDeclaracionVariablesPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ';'", tokenActual.getValor());
            }
        }
    }
    
    private void asignacion() throws ExcepcionSintactica {
        if(verifico("id_objeto")){ 
            accesoVarSimple();
            
        }else{ 
            if(verifico("self")){
                accesoSelfSimple();
            }else{
                System.out.println("asignacion");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto' o 'self'", tokenActual.getValor());
            }
        }
        if(macheo("=")){
            expresion();
        }else{
            System.out.println("asignacion");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
        }
    }
    
    private void accesoVarSimple() throws ExcepcionSintactica {
        if(macheo("id_objeto")){
            enc();
        }else{
            System.out.println("accesoVarSimple");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void accesoSelfSimple() throws ExcepcionSintactica {
        if(macheo("self")){
            enc();
        }else{
            System.out.println("accesoSelfSimple");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self'", tokenActual.getValor());
        }
    }
    
    private void enc() throws ExcepcionSintactica {
        if(verifico(".")){ 
            encadenadoSimple();
            enc();
        }else{
            if(!verifico("=")){
                System.out.println("enc");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
            }
        }
    }
    
    private void encadenadoSimple() throws ExcepcionSintactica {
        if(!macheo(".") || !macheo("id_objeto")){
            System.out.println("encadenadoSimple");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto' o '.'", tokenActual.getValor());
        }
    }
    
    private void sentenciaSimple() throws ExcepcionSintactica {
        if(macheo("(")){
            expresion();
            if(!macheo(")")){
                System.out.println("sentenciaSimple");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            System.out.println("sentenciaSimple");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
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
                System.out.println("expOrPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '||' , ')' o ';'", tokenActual.getValor());
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
                System.out.println("expAndPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&', '||' , ')' , ';' ", tokenActual.getValor());
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
                System.out.println("expIgualPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&','==' , '!=', '||' , ')' , ';' ", tokenActual.getValor());
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
            if(!verifico(args2)){
                System.out.println("expCompuestaPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&','==' , '<' , '>' , '<=' , '>=', '!=', '||' , ')' , ';' ", tokenActual.getValor());
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
            String[] args2 = { "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" ,"<=" ,">=" ,">" ,"<" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args2)){
                System.out.println("expAddPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&', '+' , '-', '==' , '<' , '>' , '<=' , '>=', '!=', '||' , ')' , ';' ", tokenActual.getValor());

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
            String[] args2 = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" ,"<=" ,">=" ,">" ,"<" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args2)){
                System.out.println("expMulPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&', '*' , '/' , '%', '+' , '-', '==' , '<' , '>' , '<=' , '>=', '!=', '||' , ')' , ';' ", tokenActual.getValor());
            }
        }
    }
    
    private void expUn() throws ExcepcionSintactica {
        String[] args = {"+" , "-" , "!"};
        if(verifico(args)){ 
            opUnario();
            expUn();
        }else{ 
            String[] args2 = {"nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "id_clase" , "new"};
            if(verifico(args2)){
                operando();
            }else{
                System.out.println("expUn");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-' , '!', 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto', 'id_clase' , 'new'", tokenActual.getValor());
            }  
        }
    }
    
    private void opIgual() throws ExcepcionSintactica {
        if(!macheo("==")){
            if(!macheo("!=")){
                System.out.println("opIgual");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '!=' , '=='", tokenActual.getValor());
            }
        }
    }
    
    private void opCompuesto() throws ExcepcionSintactica {
        if(!macheo("<") && !macheo(">") && !macheo("<=") && !macheo(">=")){
            System.out.println("opCompuesto");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '<' , '>' , '<=', '>='", tokenActual.getValor());

        }
    }
    
    private void opAdd() throws ExcepcionSintactica {
        if(!macheo("+") && !macheo("-") ){
                System.out.println("opAdd");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-'", tokenActual.getValor());
        }
    }
    
    private void opUnario() throws ExcepcionSintactica {
        if(!macheo("+") && !macheo("-") && !macheo("!")){
            System.out.println("opUnario");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-', '!'", tokenActual.getValor());
        }
    }
    
    private void opMul() throws ExcepcionSintactica {
        if(!macheo("*") && !macheo("/") && !macheo("%")){
            System.out.println("opMul");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '*' , '/', '%'", tokenActual.getValor());
        }
    }
    
    private void operando() throws ExcepcionSintactica {
        String[] args = {"nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car"};
        if(verifico(args)){ 
            literal();
        }else{
            String[] args2 = {"(" , "self" , "id_objeto", "id_clase" , "new"};
            if(verifico(args2)){ 
                primario();
                encmul();
            }else{
                System.out.println("operando");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car', '(' , 'self' , 'id_clase', 'id_objeto' , 'new'", tokenActual.getValor());
            }
        }
    }
    
    private void literal() throws ExcepcionSintactica {
        if(!macheo("nil") && !macheo("true") && !macheo("false") && !macheo("lit_ent") && !macheo("lit_cad") && !macheo("lit_car")){
            System.out.println("literal");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car'", tokenActual.getValor());
        }
    }
    
    private void primario() throws ExcepcionSintactica {
        if(verifico("(")){ 
            expresionParentizada();
        }else{
            if(verifico("self")){ 
                accesoSelf();
            }else{
                if(verifico("new")){ 
                    llamadaConstructor();
                }else{
                    if(verifico("id_clase")){
                        llamadaMetodoEstatico();
                    }else{
                        if(macheo("id_objeto")){
                            primarioPrima();
                        }else{
                            System.out.println("primario");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' , 'self' , 'id_clase', 'id_objeto' , 'new'", tokenActual.getValor());
                        }
                    }
                }
            }
        }
    }
    private void primarioPrima() throws ExcepcionSintactica {
        if(verifico("(")){ 
            llamadaMetodo();
        }else{//TODO TEST
            String[] args = {".", "*" , "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" ,"==" , "!=" , "&&" , "||" , ")" , ";"};
            if(verifico(args)){
                encmul();
            }else{
                System.out.println("primarioPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' o '(' ", tokenActual.getValor());
            }
        }
    }
        
    
    private void expresionParentizada() throws ExcepcionSintactica {
        if(macheo("(")){
            expresion();
            if(macheo(")")){
                encmul();
            }else{
                System.out.println("expresionParentizada");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' ", tokenActual.getValor());            
            }
        }else{
            System.out.println("expresionParentizada");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());        
        }
    }
    
    ///////////////////////////
    private void encmul() throws ExcepcionSintactica {
        if(verifico(".")){ 
            encadenado();
        }else{
            String[] args = {"*" , "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args)){
                System.out.println("encmul");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.', '*' , '/' , '%' , '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new' , '==' , '!=' , '&&' , '||' , ')' , ';' ", tokenActual.getValor());            }
        }
    }
    
    private void accesoSelf() throws ExcepcionSintactica {
        if(macheo("self")){
            encmul();
        }else{
            System.out.println("accesoSelf");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self' ", tokenActual.getValor());
        }
    }
    
    private void llamadaMetodo() throws ExcepcionSintactica {
        if(verifico("(")){
            argumentosActuales();
            encmul();
        }else{
            System.out.println("llamadaMetodo");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());  
        }
        
    }
    
    private void llamadaMetodoEstatico() throws ExcepcionSintactica {
        if(macheo("id_clase") && macheo(".") && macheo("id_objeto")){
            llamadaMetodo();
            encmul();
        }else{
            System.out.println("llamadaMetodoEstatico");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase' o '.' ", tokenActual.getValor()); 
        }
        
    }
    
    private void llamadaConstructor() throws ExcepcionSintactica {
        if(macheo("new") && macheo("id_clase")){
            argumentosActuales();
            encmul();
        }else{
            System.out.println("llamadaConstructor");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'new' seguido por 'id_clase'", tokenActual.getValor());
        }
        
    }
    
    private void argumentosActuales() throws ExcepcionSintactica {
        if(macheo("(")){
            listExp();
            if(!macheo(")")){
                System.out.println("argumentosActuales");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            System.out.println("argumentosActuales");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        }
        
    }
    
    private void listExp() throws ExcepcionSintactica {
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" };
        if(verifico(args)){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                System.out.println("listExp");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token \"+\" , \"-\" , \"!\" , \"nil\" , \"true\" , \"false\" , \"lit_ent\" , \"lit_cad\" , \"lit_car\" , \"(\" , \"self\" , \"id_objeto\" , \"new\"", tokenActual.getValor());
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
                System.out.println("listaExpresionesPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' o ','", tokenActual.getValor());
            }
        }
    }
    
    /////////////////////////////////
    private void encadenado() throws ExcepcionSintactica {
        if(macheo(".") && macheo("id_objeto")){
            encadenadoPrima();
        }else{
            System.out.println("encadenado");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' seguido por 'id_objeto'", tokenActual.getValor());
        }        
    }
    
    private void encadenadoPrima() throws ExcepcionSintactica {
        if(verifico("(")){ 
                llamadaMetodoEncadenado();
            }else{ 
                if(verifico(".")){ 
                    encmul();
                }else{
                    System.out.println("encadenadoPrima");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' o '.'", tokenActual.getValor());
                }
            }
    }
    
    private void llamadaMetodoEncadenado() throws ExcepcionSintactica {
        if(verifico("(")){
            argumentosActuales();
            encmul();
        }else{
            System.out.println("llamadaMetodoEncadenado");            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        } 
        
    }
}
