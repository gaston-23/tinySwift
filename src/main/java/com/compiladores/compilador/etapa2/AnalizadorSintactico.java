/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa2;

import com.compiladores.compilador.etapa1.AnalizadorLexico;
import com.compiladores.compilador.etapa1.Token;
import com.compiladores.compilador.etapa3.EntradaClase;
import com.compiladores.compilador.etapa3.EntradaConstante;
import com.compiladores.compilador.etapa3.EntradaMetodo;
import com.compiladores.compilador.etapa3.EntradaVar;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;


/**
 * Clase AnalizadorSintactico
 * Se encarga de corroborar, a partir de una lista de tokens, si el codigo fuente coincide con la gramatica
 * @author Gaston Cavallo
 * @author Mariel Volman
 */
public class AnalizadorSintactico {
    
    private Token tokenActual ;
    private AnalizadorLexico al;
    private boolean mainClass,mainMethod;
    private int psfvm = 0;
    private TablaDeSimbolos ts;
    
    /**
    * Constructor AnalizadorSintactico
    * inicializa el objeto AnalizadorLexico y comienza verificando la gramatica a partir del nodo incial "program", si llega al final de la gramatica
    * y encuentra el token 'EOF' finaliza su ejecucion con exito
    * @author Gaston Cavallo
    * @author Mariel Volman
    */
   public AnalizadorSintactico (AnalizadorLexico aL) {
        this.al = aL;
        this.tokenActual = al.nextToken();
        boolean exito = true;
        this.ts = new TablaDeSimbolos();
        try{
            this.program();
        }catch(ExcepcionSintactica | ExcepcionSemantica  eS){
            System.out.println(eS.toString());
            exito = false;
        }catch(Exception e){
            e.printStackTrace();
            exito = false;
        }
        if(exito && this.mainMethod){
            System.out.println("CORRECTO: ANALISIS SINTACTICO");
        }
    }
    
    /**
    * funcion macheo
    * se encarga de machear el token con la cadena w, si coincide pide un nextToken al AnalizadorLexico y retorna true
    * @param w
    * @return
    * @throws ExcepcionSintactica 
    */
    private boolean macheo(String w){
        
        if(tokenActual.getValor().equals(w) || tokenActual.getpReservada().equals(w)){
            System.out.println(this.tokenActual.getValor());
            this.tokenActual = this.al.nextToken();
            return true;
        }else return false;
    }
    /**
     * metodo encargado de mirar el proximo no terminal recibiendo un String (Lookahead = 1) y corrobora si coinciden sin machear
     * @param w
     * @return
     * @throws ExcepcionSintactica 
     */
    private boolean verifico(String w){
        return (tokenActual.getValor().equals(w) || tokenActual.getpReservada().equals(w));            
    }
    /**
     * metodo encargado de mirar el proximo no terminal si coincide con algun elemento del array de Strings
     * @param w
     * @return 
     */
    private boolean verifico(String[] w){
        for (String string : w) {
            if(tokenActual.getValor().equals(string) || tokenActual.getpReservada().equals(string)){
                return true; 
            }
        }
        return false;            
    }

    /**
     * Primer no terminal que da comienzo a la gramatica
     * @throws ExcepcionSintactica 
     */
    private void program() throws Exception {
        clase();
    }
    
    private void programPrima() throws Exception {
        System.out.println("programPrima :: "+this.tokenActual.getValor());
        if(verifico("class")){
           clase(); 
        }else{
            if(!verifico("EOF")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o 'EOF'", tokenActual.getValor());
            }
        }
    }
    
    private void clase() throws Exception {
        System.out.println("clase :: "+this.tokenActual.getValor());
        if(macheo("class")){
            if(verifico("id_clase")&& verifico("Main")){
                this.mainClass = true;
            }
            String lexActual = tokenActual.getValor();
            int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
            if(macheo("id_clase")){
                this.ts.setClaseActual(new EntradaClase(lexActual,"Object",fil,col));
                h(); 
                if( macheo("{") ){
                    m();
                    if( macheo("}") ){ 
                        this.ts.insertaClase(this.ts.getClaseActual());
                        this.ts.setClaseActual(null);
                        programPrima();
                    }else{
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());

                    }
                }else {
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
                }

            }else {
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class'", tokenActual.getValor());
        }
    }

    private void h() throws Exception {
        System.out.println("h :: "+this.tokenActual.getValor());
        if(verifico(":")){
            herencia();
        }else{
            if(!verifico("{")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' o '{'", tokenActual.getValor());
            }else{
                this.ts.getClaseActual().setHerencia("Object");
            }
        }
    }
    
    private void herencia() throws Exception {
        System.out.println("herencia :: "+this.tokenActual.getValor());
        if(macheo(":")){
            String lexActual = tokenActual.getValor();
            tipo();
            this.ts.getClaseActual().setHerencia(lexActual);
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
        
    }

    private void m() throws Exception {
        System.out.println("m :: "+this.tokenActual.getValor());
        if(!verifico("}")){
            String[] args = {"private",  "static" , "init" , "let", "var", "func"};
            if(verifico(args)){
                miembro();
                m();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let', 'var', 'func', o '{'", tokenActual.getValor());

            }
        }
    }
    
    private void miembro() throws Exception {
        System.out.println("miembro :: "+this.tokenActual.getValor());
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
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let'", tokenActual.getValor());
                    }
                }
            }
        }
    }

    private void constructor() throws Exception {
        System.out.println("constructor :: "+this.tokenActual.getValor());
        int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(macheo("init")){
            this.ts.setMetodoActual(new EntradaMetodo(this.ts.getClaseActual().getNombre(),fil,col));
            this.ts.getMetodoActual().setNombre("constructor");
            argumentosFormales();
            bloqueMetodo();
            this.ts.getClaseActual().insertaMetodo("constructor", this.ts.getMetodoActual());
            this.ts.setMetodoActual(null);
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'init'", tokenActual.getValor());
        }
    }
    
    private void bloqueMetodo() throws Exception {
        System.out.println("bloqueMetodo :: "+this.tokenActual.getValor());
        if(macheo("{")){
            declaConstLocales();
            declaVarLocales();
            s(); 
            if(!macheo("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }
    }
    
    private void declaVarLocales() throws Exception {
        System.out.println("declaVarLocales :: "+this.tokenActual.getValor());
        if(verifico("var")){
            declaVarLoc();
            declaVarLocales();
        }
    }
    
    private void declaVarLoc() throws Exception {
        System.out.println("declaVarLoc :: "+this.tokenActual.getValor());
        if(!macheo("var")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'var'", tokenActual.getValor());
        }
        String lexTipo = this.tokenActual.getValor();
        tipo();   
        if(macheo(":")){
            listaDeclaracionVariables(lexTipo , false);
            if(!macheo(";")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
    }
    
    private void declaConstLocales() throws Exception {
        System.out.println("declaConstLocales :: "+this.tokenActual.getValor());
        if(verifico("let")){
            declaConstLoc();
            declaConstLocales();
        }
    }

    private void declaConstLoc() throws Exception {
        System.out.println("declaConstLoc :: "+this.tokenActual.getValor());
        if(macheo("let")){
            String auxTipo = this.tokenActual.getValor();
            tipoPrimitivo();
            if(macheo(":")){
                String auxNombre = this.tokenActual.getValor();
                int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
                if(macheo("id_objeto")){
                    if(macheo("=")){
                        if(!this.ts.getMetodoActual().equals(null)){
                            this.ts.getMetodoActual().insertaConstante(auxNombre, new EntradaConstante(auxTipo,fil,col));
                        }else{
                            this.ts.getClaseActual().insertaConstante(auxNombre,  new EntradaConstante(auxTipo,fil,col));
                        }
                        expresion();
                        if(!macheo(";")){
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                        }
                    }else{
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
                    }
                }else{
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
                }
                
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'let' ", tokenActual.getValor());
        }
    }
    
    private void atributo() throws Exception {
        System.out.println("atributo :: "+this.tokenActual.getValor());
        boolean auxVisib = false;
        if(!macheo("private")){
            if(!verifico("var")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private' o 'var'", tokenActual.getValor());
            }
        }else{
            auxVisib = true;
        }
        variable(auxVisib);
    }
    
    private void variable(boolean auxVisib) throws Exception {
        System.out.println("variable :: "+this.tokenActual.getValor());
        if(!macheo("var")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'var'", tokenActual.getValor());
        }
        String lexTipo = this.tokenActual.getValor();
        tipo();   
        if(macheo(":")){
            listaDeclaracionVariables(lexTipo , auxVisib);

            if(!macheo(";")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
    }
    
    private void restoMetodoVoid() throws Exception {
        System.out.println("restoMetodoVoid :: "+this.tokenActual.getValor());
        if(verifico("id_objeto") && verifico("main") && this.psfvm == 3  && this.mainClass){
            this.psfvm++;
        }else{
            this.psfvm = 0;
        }
        String auxId = tokenActual.getValor();
        if(macheo("id_objeto")){
            this.ts.getMetodoActual().setNombre(auxId);
            argumentosFormales();
            bloqueMetodo();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void restoMetodoEstatico() throws Exception {
        System.out.println("restoMetodoEstatico :: "+this.tokenActual.getValor());
        String auxTipo = this.tokenActual.getValor();
        int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(!macheo("void")){
            if(!macheo("String")){
                if(!macheo("Bool")){
                    if(!macheo("Int")){
                        if(!macheo("Char")){
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'void' o  'String' , 'Bool' , 'Int' , 'Char', 'id_clase'", tokenActual.getValor());
                        }
                    }
                }
            }
        }else{
            if(this.psfvm == 2 && this.mainClass){
                this.psfvm++;
            }else{
                this.psfvm = 0;
            }
        }
        this.ts.setMetodoActual(new EntradaMetodo(auxTipo,fil,col));
        restoMetodoVoid();
    }
    
    private void metodo() throws Exception {
        System.out.println("metodo :: "+this.tokenActual.getValor());
        boolean auxStatic = false;
        if(macheo("static")){
            auxStatic = true;
            if(this.mainClass){
                this.psfvm = 1;
            }
            if(!verifico("func")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'func'", tokenActual.getValor());
            }
        }
        if (macheo("func")){
            if(this.psfvm == 1 && this.mainClass){
                this.psfvm++;
            }else{
                this.psfvm = 0;
            }
            restoMetodoEstatico();
            this.ts.getMetodoActual().setIsStatic(auxStatic);
            this.ts.getClaseActual().insertaMetodo(this.ts.getMetodoActual().getNombre(), this.ts.getMetodoActual());
            this.ts.setMetodoActual(null);
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'static' o 'func'", tokenActual.getValor());
        }
    }

    private void constante() throws Exception {
        System.out.println("constante :: "+this.tokenActual.getValor());
        if(macheo("let")){
            String auxTipo = this.tokenActual.getValor();
            tipoPrimitivo();
            if(macheo(":")){
                String auxNombre = this.tokenActual.getValor();
                int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
                if(macheo("id_objeto")){
                    if(macheo("=")){
                        if(!this.ts.getMetodoActual().equals(null)){
                            this.ts.getMetodoActual().insertaConstante(auxNombre, new EntradaConstante(auxTipo,fil,col));
                        }else{
                            this.ts.getClaseActual().insertaConstante(auxNombre,  new EntradaConstante(auxTipo,fil,col));
                        }
                        expresion();
                        if(!macheo(";")){
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                        }
                    }else{
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
                    }
                }else{
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
                }
                
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'let' ", tokenActual.getValor());
        }
    }

    private void argumentosFormales() throws Exception {
        System.out.println("argFor :: "+this.tokenActual.getValor());
        if(macheo("(")){
            if(verifico(")") && this.psfvm == 4  && this.mainClass){
                this.mainMethod = true;
            }else{
                this.psfvm = 0;
            }
            l();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' ", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());
        }
        
    }

    private void l() throws Exception {
        System.out.println("l :: "+this.tokenActual.getValor());
        String[] args = { "String" , "Bool" , "Int" , "Char", "id_clase" };
        if(verifico(args)){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char', 'id_clase' o ')'", tokenActual.getValor());
            }
        }
    }

    private void listaArgumentosFormales() throws Exception {
        System.out.println("listaArgumentosFormales :: "+this.tokenActual.getValor());
        String[] args = { "String" , "Bool" , "Int" , "Char", "id_clase" };
        if(verifico(args)){
            argumentoFormal();
            listaArgumentosFormalesPrima();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char', 'id_clase'", tokenActual.getValor());
        }
    }

    private void listaArgumentosFormalesPrima() throws Exception {
        System.out.println("listaArgumentosFormalesPrima :: "+this.tokenActual.getValor());
        if(macheo(",")){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ')'", tokenActual.getValor());
            }
        }
    }

    private void argumentoFormal() throws Exception {
        System.out.println("argumentoFormal :: "+this.tokenActual.getValor());
        String auxTipo = this.tokenActual.getValor();
        tipo();
        if(macheo(":")){
            String auxNombre = this.tokenActual.getValor();
            int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
            if(!macheo("id_objeto")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }else{
                this.ts.getMetodoActual().insertaParametro(auxTipo, auxNombre,fil,col);
            }
        }else{   
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
    }


    private void tipo() throws Exception {
        System.out.println("tipo :: "+this.tokenActual.getValor());
        String[] args = { "String","Bool","Int","Char"};
        if(verifico(args)){
            tipoPrimitivo(); 
        }else{
            if(verifico("id_clase")){
                tipoReferencia(); 
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char' o 'id_clase", tokenActual.getValor());
            }
        }
        
    }

    private void tipoPrimitivo() throws Exception {
        System.out.println("tipoPrimitivo :: "+this.tokenActual.getValor());
        if(!macheo("String")){
            if(!macheo("Bool")){
                if(!macheo("Int")){
                    if(!macheo("Char")){
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
                    }
                }
            }
        }
    }

    private void tipoReferencia() throws Exception {
        System.out.println("tipoReferencia :: "+this.tokenActual.getValor());
        if(!macheo("id_clase")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
        }
    }

    private void bloque() throws Exception {
        System.out.println("bloque :: "+this.tokenActual.getValor());
        if(macheo("{")){
            s(); 
            if(!macheo("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }
    }

    private void s() throws Exception {
        System.out.println("s :: "+this.tokenActual.getValor());
        String[] args = { ";" , "var" , "if", "let" , "while" , "return" , "id_objeto" , "self" , "(" , "{"};
        if(verifico(args)){
            sentencia();
            s();
        }else{
            if(!verifico("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';' , 'var' , 'if', 'let' , 'while' , 'return' , 'id_objeto' , 'self' , '(' , '{' , '}'", tokenActual.getValor());
            }
        }
    }
    
    private void sentencia() throws Exception {
        System.out.println("sentencia :: "+this.tokenActual.getValor());
        if(!macheo(";")){
            String[] args = { "id_objeto" , "self" };
            if(verifico(args)){
                asignacion();
                if(!macheo(";")){
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                if(verifico("(")){
                    sentenciaSimple();
                    if(!macheo(";")){
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                    }
                }else{
                    if(macheo("if") && macheo("(")){ //TODO TEST
                        expresion();
                        if(macheo(")")){
                            sentencia();
                            sentenciaPrima();
                        }else{
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
                        }
                    }else{
                        if(macheo("while")){
                            if(macheo("(")){
                                expresion();
                                if(macheo(")")){
                                    sentencia();
                                }else{
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
                                } 
                            }else{
                                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
                            }
                        }else{
                            if(macheo("return")){ 
                                x();
                                if(!macheo(";")){
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                                }
                            }else{
                                if(verifico("{")){
                                    bloque();
                                }else{
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{', 'return', 'while', '(', 'let', 'if', 'var', 'self' ", tokenActual.getValor());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void sentenciaPrima() throws Exception {
        System.out.println("sentenciaPrima :: "+this.tokenActual.getValor());
        if(macheo("else")){
            sentencia();
        }
//        else{
//            if(!verifico("}")){ 
//                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}' o 'else'", tokenActual.getValor());
//            }
//        }
    }
    
    private void x() throws Exception {
        System.out.println("x :: "+this.tokenActual.getValor());
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new"};
        if(verifico(args)){
            expresion();
        }else{
            String[] args2 = {")" , ";"};
            if(!verifico(args2)){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new', ')' , ';'", tokenActual.getValor());
            }
        }
    }
    
    private void listaDeclaracionVariables(String tipo, boolean priv) throws Exception {
        System.out.println("listaDeclaracionVariables :: "+this.tokenActual.getValor());
        String nombre = this.tokenActual.getValor();
        int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(macheo("id_objeto")){
            if(this.ts.getMetodoActual() == null){
                this.ts.getClaseActual().insertaVariable(nombre, new EntradaVar(tipo,priv,fil,col));
            }else{
                this.ts.getMetodoActual().insertaVariable(nombre, new EntradaVar(tipo,priv,fil,col));
            }
            listaDeclaracionVariablesPrima(tipo , priv);
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void listaDeclaracionVariablesPrima(String tipo, boolean priv) throws Exception {
        System.out.println("listaDeclaracionVariablesPrima :: "+this.tokenActual.getValor());
        if(macheo(",")){
            listaDeclaracionVariables(tipo , priv);
        }else{
            if(!verifico(";")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ';'", tokenActual.getValor());
            }
        }
    }
    
    private void asignacion() throws Exception {
        System.out.println("asignacion :: "+this.tokenActual.getValor());
        if(verifico("id_objeto")){ 
            accesoVarSimple();
        }else{ 
            if(verifico("self")){
                accesoSelfSimple();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto' o 'self'", tokenActual.getValor());
            }
        }
        if(macheo("=")){
            expresion();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
        }
    }
    
    private void accesoVarSimple() throws Exception {
        System.out.println("accesoVarSimple :: "+this.tokenActual.getValor());
        if(macheo("id_objeto")){
            enc();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void accesoSelfSimple() throws Exception {
        System.out.println("accesoSelfSimple :: "+this.tokenActual.getValor());
        if(macheo("self")){
            enc();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self'", tokenActual.getValor());
        }
    }
    
    private void enc() throws Exception {
        System.out.println("enc :: "+this.tokenActual.getValor());
        if(verifico(".")){ 
            encadenadoSimple();
            enc();
        }else{
            if(!verifico("=")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '=' o '.'", tokenActual.getValor());
            }
        }
    }
    
    private void encadenadoSimple() throws Exception {
        System.out.println("encadenadoSimple :: "+this.tokenActual.getValor());
        if(macheo(".")){
            if(!macheo("id_objeto")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.'", tokenActual.getValor());
        }
    }
    
    private void sentenciaSimple() throws Exception {
        System.out.println("sentSimp :: "+this.tokenActual.getValor());
        if(macheo("(")){
            expresion();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        }
    }
    
    private void expresion() throws Exception {
        System.out.println("expresion :: "+this.tokenActual.getValor());
        expOr();
    }
    
    private void expOr() throws Exception {
        System.out.println("expOr :: "+this.tokenActual.getValor());
        expAnd();
        expOrPrima();
    }
    
    private void expOrPrima() throws Exception {
        System.out.println("expOrPrima :: "+this.tokenActual.getValor());
        if(macheo("||")){
            expAnd();
            expOrPrima();
        }
    }
    
    private void expAnd() throws Exception {
        System.out.println("expAnd :: "+this.tokenActual.getValor());
        expIgual();
        expAndPrima();
    }
    
    private void expAndPrima() throws Exception {
        System.out.println("expAndPrima :: "+this.tokenActual.getValor());
        if(macheo("&&")){
            expIgual();
            expAndPrima();
        }
    }
    
    private void expIgual() throws Exception {
        System.out.println("expIgual :: "+this.tokenActual.getValor());
        expCompuesta();
        expIgualPrima();
    }
    
    private void expIgualPrima() throws Exception {
        System.out.println("expIgualPrima :: "+this.tokenActual.getValor());
        String[] args = {"==" , "!="};
        if(verifico(args)){
            opIgual();
            expCompuesta();
            expIgualPrima();
        }
    }
    
    private void expCompuesta() throws Exception {
        System.out.println("expCompuesta :: "+this.tokenActual.getValor());
        expAdd();
        expCompuestaPrima();
    }
    
    private void expCompuestaPrima() throws Exception {
        System.out.println("expCompuestaPrima :: "+this.tokenActual.getValor());
        String[] args = {"<" , ">" , "<=" , ">="};
        if(verifico(args)){
            opCompuesto();
            expAdd();
        }
    }
    
    private void expAdd() throws Exception {
        System.out.println("expAdd :: "+this.tokenActual.getValor());
        expMul();
        expAddPrima();
    }
    
    private void expAddPrima() throws Exception {
        System.out.println("expAddPrima :: "+this.tokenActual.getValor());
        String[] args = { "+" , "-"};
        if(verifico(args)){
            opAdd();
            expMul();
            expAddPrima();
        }
    }
    
    private void expMul() throws Exception {
        System.out.println("expMul :: "+this.tokenActual.getValor());
        expUn();
        expMulPrima();
    }
    
    private void expMulPrima() throws Exception {
        System.out.println("expMulPrima :: "+this.tokenActual.getValor());
        String[] args = {"*" , "/" , "%"};
        if(verifico(args)){
            opMul();
            expUn();
            expMulPrima();  
        }
    }
    
    private void expUn() throws Exception {
        System.out.println("expUn :: "+this.tokenActual.getValor());
        String[] args = {"+" , "-" , "!"};
        if(verifico(args)){ 
            opUnario();
            expUn();
        }else{ 
            String[] args2 = {"nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "id_clase" , "new"};
            if(verifico(args2)){
                operando();
            }else{
                throw new ExcepcionSintactica(tokenActual,"mal finalizacion de expresion", tokenActual.getValor());
            }  
        }
    }
    
    private void opIgual() throws Exception {
        System.out.println("opIgual :: "+this.tokenActual.getValor());
        if(!macheo("==")){
            if(!macheo("!=")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '!=' , '=='", tokenActual.getValor());
            }
        }
    }
    
    private void opCompuesto() throws Exception {
        System.out.println("opCompuesto :: "+this.tokenActual.getValor());
        if(!macheo("<") && !macheo(">") && !macheo("<=") && !macheo(">=")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '<' , '>' , '<=', '>='", tokenActual.getValor());
        }
    }
    
    private void opAdd() throws Exception {
        System.out.println("opAdd :: "+this.tokenActual.getValor());
        if(!macheo("+") && !macheo("-") ){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-'", tokenActual.getValor());
        }
    }
    
    private void opUnario() throws Exception {
        System.out.println("opUnario :: "+this.tokenActual.getValor());
        if(!macheo("+") && !macheo("-") && !macheo("!")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-', '!'", tokenActual.getValor());
        }
    }
    
    private void opMul() throws Exception {
        System.out.println("opMul :: "+this.tokenActual.getValor());
        if(!macheo("*") && !macheo("/") && !macheo("%")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '*' , '/', '%'", tokenActual.getValor());
        }
    }
    
    private void operando() throws Exception {
        System.out.println("operando :: "+this.tokenActual.getValor());
        String[] args = {"nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car"};
        if(verifico(args)){ 
            literal();
        }else{
            String[] args2 = {"(" , "self" , "id_objeto", "id_clase" , "new"};
            if(verifico(args2)){ 
                primario();
            }else{
                throw new ExcepcionSintactica(tokenActual,"mal finalizacion de operando", tokenActual.getValor());
            }
        }
    }
    
    private void literal() throws Exception {
        System.out.println("literal :: "+this.tokenActual.getValor());
        if(!macheo("nil") && !macheo("true") && !macheo("false") && !macheo("lit_ent") && !macheo("lit_cad") && !macheo("lit_car")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car'", tokenActual.getValor());
        }
    }
    
    private void primario() throws Exception {
        System.out.println("primario :: "+this.tokenActual.getValor()+ " tok "+this.tokenActual.getpReservada());
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
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' , 'self' , 'id_clase', 'id_objeto' , 'new'", tokenActual.getValor());
                        }
                    }
                }
            }
        }
    }
    private void primarioPrima() throws Exception {
        System.out.println("primPrim :: "+this.tokenActual.getValor());
        if(verifico("(")){ 
            llamadaMetodo();
        }else{
            String[] args = {".", "*" , "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" ,"==" , "!=" , "&&" , "||" , ")" , ";"};
            if(verifico(args)){
                encmul();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' o '(' ", tokenActual.getValor());
            }
        }
    }
        
    
    private void expresionParentizada() throws Exception {
        System.out.println("expPar :: "+this.tokenActual.getValor());
        if(macheo("(")){
            expresion();
            if(macheo(")")){
                encmul();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' ", tokenActual.getValor());            
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());        
        }
    }
    
    private void encmul() throws Exception {
        System.out.println("encMul :: "+this.tokenActual.getValor());
        if(verifico(".")){ 
            encadenado();
        }else{
            String[] args = {"*", ",", "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args)){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.', '*' , '/' , '%' , '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new' , '==' , '!=' , '&&' , '||' , ')' , ';' ", tokenActual.getValor());            
            }
        }
    }
    
    private void accesoSelf() throws Exception {
        System.out.println("accesoSelf :: "+this.tokenActual.getValor());
        if(macheo("self")){
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self' ", tokenActual.getValor());
        }
    }
    
    private void llamadaMetodo() throws Exception {
        System.out.println("llamadaMetodo :: "+this.tokenActual.getValor());
        argumentosActuales();
        encmul();        
    }
    
    private void llamadaMetodoEstatico() throws Exception {
        System.out.println("llamadaMetodoEstatico :: "+this.tokenActual.getValor());
        if(macheo("id_clase")){
            if(macheo(".")){
                if(macheo("id_objeto")){
                    llamadaMetodo();
                    encmul();
                }else{
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
                }
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor()); 
        }
        
    }
    
    private void llamadaConstructor() throws Exception {
        System.out.println("llamadaConstructor :: "+this.tokenActual.getValor());
        if(macheo("new")){
            if(macheo("id_clase")){
                argumentosActuales();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'new'", tokenActual.getValor());
        }
    }
    
    private void argumentosActuales() throws Exception {
        System.out.println("argAct :: "+this.tokenActual.getValor());
        if(macheo("(")){
            listExp();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        }
    }
    
    private void listExp() throws Exception {
        System.out.println("listExp :: "+this.tokenActual.getValor());
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" };
        if(verifico(args)){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"mal declaracion de expresion", tokenActual.getValor());
            }
        }
    }
    
    private void listaExpresiones() throws Exception {
        System.out.println("listaExpresiones :: "+this.tokenActual.getValor());
        expresion();
        listaExpresionesPrima();
    }
    
    private void listaExpresionesPrima() throws Exception {
        System.out.println("listaExpresionesPrima :: "+this.tokenActual.getValor());
        if(macheo(",")){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' o ','", tokenActual.getValor());
            }
        }
    }
    
    private void encadenado() throws Exception {
        System.out.println("encad :: "+this.tokenActual.getValor());
        if(macheo(".")){
            if(macheo("id_objeto")){
                encadenadoPrima();
            }else{
              throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }        
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' ", tokenActual.getValor());
        }        
    }
    
    private void encadenadoPrima() throws Exception {
        System.out.println("encadenadoPrima :: "+this.tokenActual.getValor());
        if(verifico("(")){ 
            llamadaMetodoEncadenado();
        }else{ 
            if(verifico(".")){ 
                encmul();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' o '.'", tokenActual.getValor());
            }
        }
    }
    
    private void llamadaMetodoEncadenado() throws Exception {
        System.out.println("llamadaMetodoEncadenado :: "+this.tokenActual.getValor());
        if(verifico("(")){
            argumentosActuales();
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        } 
        
    }

    public TablaDeSimbolos getTs() {
        return ts;
    }
}
