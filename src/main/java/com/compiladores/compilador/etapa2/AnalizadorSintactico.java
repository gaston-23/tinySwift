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
import com.compiladores.compilador.etapa4.ArbolSintacticoAbstracto;
import com.compiladores.compilador.etapa4.NodoAST;
import com.compiladores.compilador.etapa4.NodoAsignacion;
import com.compiladores.compilador.etapa4.NodoClase;
import com.compiladores.compilador.etapa4.NodoExpresion;
import com.compiladores.compilador.etapa4.NodoExpresionBinaria;
import com.compiladores.compilador.etapa4.NodoExpresionUnaria;
import com.compiladores.compilador.etapa4.NodoIf;
import com.compiladores.compilador.etapa4.NodoLLamadaMetodo;
import com.compiladores.compilador.etapa4.NodoMetodo;
import com.compiladores.compilador.etapa4.NodoSentencia;
import com.compiladores.compilador.etapa4.NodoWhile;


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
    private ArbolSintacticoAbstracto ast;
    
    /**
    * Constructor AnalizadorSintactico
    * inicializa el objeto AnalizadorLexico y comienza verificando la gramatica a partir del nodo incial "program", si llega al final de la gramatica
    * y encuentra el token 'EOF' finaliza su ejecucion con exito
    * @author Gaston Cavallo
    * @author Mariel Volman
    */
   public AnalizadorSintactico (AnalizadorLexico aL, String archivo)throws ExcepcionSintactica,ExcepcionSemantica {
        this.al = aL;
        this.tokenActual = al.nextToken();
        
        this.ts = new TablaDeSimbolos(archivo);
        this.ast = new ArbolSintacticoAbstracto(archivo);
        this.program();

        if(this.mainMethod){
            System.out.println("CORRECTO: ANALISIS SINTACTICO");
        }else{
            throw new ExcepcionSintactica(tokenActual,"El codigo no tiene una clase Main", "EOF");
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
            //System.out.println(this.tokenActual.getValor());
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
    private void program() throws ExcepcionSintactica,ExcepcionSemantica {
        clase();
    }
    
    private void programPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("programPrima :: "+this.tokenActual.getValor());
        if(verifico("class")){
           clase(); 
        }else{
            if(!verifico("EOF")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o 'EOF'", tokenActual.getValor());
            }
        }
    }
    
    private void clase() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("clase :: "+this.tokenActual.getValor());
        if(macheo("class")){
            if(verifico("id_clase")&& verifico("Main")){
                this.mainClass = true;
            }
            String lexActual = tokenActual.getValor();
            int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
            if(macheo("id_clase")){
                this.ts.setClaseActual(new EntradaClase(lexActual,"Object",fil,col));
                NodoClase nC = new NodoClase(fil,col,lexActual);
                this.ast.putClase(lexActual, nC);
                this.ast.pushScope(nC);
                h(); 
                if( macheo("{") ){
                    m();
                    if( macheo("}") ){ 
                        this.ts.insertaClase(this.ts.getClaseActual());
                        this.ts.limpiaClaseActual();
                        this.ast.popScope();
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

    private void h() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("h :: "+this.tokenActual.getValor());
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
    
    private void herencia() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("herencia :: "+this.tokenActual.getValor());
        if(macheo(":")){
            String lexActual = tokenActual.getValor();
            tipo();
            this.ts.getClaseActual().setHerencia(lexActual);
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
        
    }

    private void m() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("m :: "+this.tokenActual.getValor());
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
    
    private void miembro() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("miembro :: "+this.tokenActual.getValor());
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
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let' o 'var'", tokenActual.getValor());
                    }
                }
            }
        }
    }

    private void constructor() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("constructor :: "+this.tokenActual.getValor());
        int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(macheo("init")){
            this.ts.setMetodoActual(new EntradaMetodo(this.ts.getClaseActual().getNombre(),fil,col));
            this.ts.getMetodoActual().setNombre("constructor");
            this.ast.pushScope(new NodoMetodo(fil,col,"constructor",this.ts.getClaseActual().getNombre()));
            ((NodoMetodo) this.ast.peekScope()).setNombre("constructor");
            argumentosFormales();
            bloqueMetodo();
            NodoMetodo nM = (NodoMetodo) this.ast.popScope();
            ((NodoClase) this.ast.peekScope()).setConstructor(nM);
            this.ts.getClaseActual().insertaMetodo("constructor", this.ts.getMetodoActual());
            this.ts.limpiaMetodoActual();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'init'", tokenActual.getValor());
        }
    }
    
    private void bloqueMetodo() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("bloqueMetodo :: "+this.tokenActual.getValor());
        if(macheo("{")){
            declaConstLocales();
            declaVarLocales();
            s(); 
            if(!macheo("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }
    }
    
    private void declaVarLocales() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("declaVarLocales :: "+this.tokenActual.getValor());
        if(verifico("var")){
            declaVarLoc();
            declaVarLocales();
        }
    }
    
    private void declaVarLoc() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("declaVarLoc :: "+this.tokenActual.getValor());
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
    
    private void declaConstLocales() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("declaConstLocales :: "+this.tokenActual.getValor());
        if(verifico("let")){
            declaConstLoc();
            declaConstLocales();
        }
    }

    private void declaConstLoc() throws ExcepcionSintactica,ExcepcionSemantica {
//        //System.out.println("declaConstLoc :: "+this.tokenActual.getValor());
        if(macheo("let")){
            String auxTipo = this.tokenActual.getValor();
            tipoPrimitivo();
            if(macheo(":")){
                String auxNombre = this.tokenActual.getValor();
                int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
                if(macheo("id_objeto")){
                    if(macheo("=")){
                        if(this.ts.getMetodoActual() != null){
                            this.ts.getMetodoActual().insertaConstante(auxNombre, new EntradaConstante(auxTipo,fil,col));
                        }else{
                            this.ts.getClaseActual().insertaConstante(auxNombre,  new EntradaConstante(auxTipo,fil,col));
                        }
                        NodoAsignacion nA = new NodoAsignacion(fil,col,new NodoExpresion(fil,col,auxNombre,auxTipo),"constante");
                        this.ast.pushScope(nA);
                        NodoExpresion nEAux = expresion();
                        NodoAsignacion nAux = (NodoAsignacion) this.ast.popScope();
                        nAux.setDer(nEAux);
                        ((NodoMetodo) this.ast.peekScope()).putBloque( nAux);
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
    
    private void atributo() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("atributo :: "+this.tokenActual.getValor());
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
    
    private void variable(boolean auxVisib) throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("variable :: "+this.tokenActual.getValor());
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
    
    private void restoMetodoVoid() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("restoMetodoVoid :: "+this.tokenActual.getValor());
        if(verifico("id_objeto") && verifico("main") && this.psfvm == 3  && this.mainClass){
            this.psfvm++;
        }else{
            this.psfvm = 0;
        }
        String auxId = tokenActual.getValor();
        if(macheo("id_objeto")){
            this.ts.getMetodoActual().setNombre(auxId);
            ((NodoMetodo)this.ast.peekScope()).setNombre(auxId);
            argumentosFormales();
            bloqueMetodo();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void restoMetodoEstatico() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("restoMetodoEstatico :: "+this.tokenActual.getValor());
        String auxTipo = this.tokenActual.getValor();
        int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if( !macheo("void")     && 
            !macheo("String")   && 
            !macheo("Bool")     && 
            !macheo("Int")      && 
            !macheo("Char")     && 
            !macheo("id_clase")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'void' o  'String' , 'Bool' , 'Int' , 'Char', 'id_clase'", tokenActual.getValor());

        }else{
            if(this.psfvm == 2 && this.mainClass){
                this.psfvm++;
            }else{
                this.psfvm = 0;
            }
        }
        this.ts.setMetodoActual(new EntradaMetodo(auxTipo,fil,col));
        
        NodoMetodo nM = new NodoMetodo(fil,col,auxTipo,this.ts.getClaseActual().getNombre());
        this.ast.pushScope(nM);
        restoMetodoVoid();
        NodoMetodo nMAux = (NodoMetodo) this.ast.popScope();
        NodoClase aux = (NodoClase) this.ast.peekScope();
        aux.putMetodos(nMAux.getNombre(),nMAux);
    }
    
    private void metodo() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("metodo :: "+this.tokenActual.getValor());
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
            this.ts.limpiaMetodoActual();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'static' o 'func'", tokenActual.getValor());
        }
    }

    private void constante() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("constante :: "+this.tokenActual.getValor());
        if(macheo("let")){
            String auxTipo = this.tokenActual.getValor();
            tipoPrimitivo();
            if(macheo(":")){
                String auxNombre = this.tokenActual.getValor();
                int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
                if(macheo("id_objeto")){
                    if(macheo("=")){
                        if(this.ts.getMetodoActual() != null){
                            this.ts.getMetodoActual().insertaConstante(auxNombre, new EntradaConstante(auxTipo,fil,col));
                        }else{
                            this.ts.getClaseActual().insertaConstante(auxNombre,  new EntradaConstante(auxTipo,fil,col));
                        }
                        NodoAsignacion nA = new NodoAsignacion(fil,col,new NodoExpresion(fil,col,auxNombre,auxTipo),"constante");
                        this.ast.pushScope(nA);
                        NodoExpresion nEAux = expresion();
                        NodoAsignacion nAux = (NodoAsignacion) this.ast.popScope();
                        nAux.setDer(nEAux);
                        ((NodoClase) this.ast.peekScope()).putConstantes(auxNombre, nAux);
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

    private void argumentosFormales() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("argFor :: "+this.tokenActual.getValor());
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

    private void l() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("l :: "+this.tokenActual.getValor());
        String[] args = { "String" , "Bool" , "Int" , "Char", "id_clase" };
        if(verifico(args)){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char', 'id_clase' o ')'", tokenActual.getValor());
            }
        }
    }

    private void listaArgumentosFormales() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listaArgumentosFormales :: "+this.tokenActual.getValor());
        String[] args = { "String" , "Bool" , "Int" , "Char", "id_clase" };
        if(verifico(args)){
            argumentoFormal();
            listaArgumentosFormalesPrima();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char', 'id_clase'", tokenActual.getValor());
        }
    }

    private void listaArgumentosFormalesPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listaArgumentosFormalesPrima :: "+this.tokenActual.getValor());
        if(macheo(",")){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ')'", tokenActual.getValor());
            }
        }
    }

    private void argumentoFormal() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("argumentoFormal :: "+this.tokenActual.getValor());
        String auxTipo = this.tokenActual.getValor();
        tipo();
        if(macheo(":")){
            String auxNombre = this.tokenActual.getValor();
            int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
            if(!macheo("id_objeto")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }else{
                this.ts.getMetodoActual().insertaParametro(auxTipo, auxNombre,fil,col);
                ((NodoMetodo)this.ast.peekScope()).putArg(new NodoExpresion(fil,col,auxNombre,auxTipo));
            }
        }else{   
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
        }
    }


    private void tipo() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("tipo :: "+this.tokenActual.getValor());
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

    private void tipoPrimitivo() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("tipoPrimitivo :: "+this.tokenActual.getValor());
        if( !macheo("String")&&
            !macheo("Bool")  &&
            !macheo("Int")   &&
            !macheo("Char")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
        }
    }

    private void tipoReferencia() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("tipoReferencia :: "+this.tokenActual.getValor());
        if(!macheo("id_clase")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
        }
    }

    private void bloque() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("bloque :: "+this.tokenActual.getValor());
        if(macheo("{")){
            s(); 
            if(!macheo("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }
    }

    private void s() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("s :: "+this.tokenActual.getValor());
        String[] args = { ";" , "if", "while" , "return" , "id_objeto" , "self" , "(" , "{"};
        if(verifico(args)){
            
            NodoSentencia nS = sentencia();
            NodoAST nAux = this.ast.peekScope();
//            //System.out.println("s:: "+nAux.getClass().toString());
//            //System.out.println("ns:: "+nS.imprimeSentencia());
            if(nAux.getClass()== NodoMetodo.class){
                //System.out.println("CLASE");
                ((NodoMetodo) this.ast.peekScope()).putBloque(nS);
//                //System.out.println(((NodoMetodo) this.ast.peekScope()).imprimeSentencia());
            }else{
                if(nAux.getClass()== NodoIf.class ){
                    //System.out.println("IF");
                    ((NodoIf) this.ast.peekScope()).addSentencia(nS);
//                    //System.out.println(((NodoIf) this.ast.peekScope()).imprimeSentencia());
                }else{
                    if(nAux.getClass()== NodoWhile.class){
                        //System.out.println("WHILE");
                        ((NodoWhile) this.ast.peekScope()).addSentencia(nS);
                        
//                        //System.out.println(((NodoWhile) this.ast.peekScope()).imprimeSentencia());
                    }else{
                        //System.out.println("ERROR DE IMPLEMENTACION, SE ENCONTRO : "+nAux.getClass()+" linea "+nAux.getFila());
                    }
                }
            }
            s();
        }else{
            if(!verifico("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';' , 'if', 'while' , 'return' , 'id_objeto' , 'self' , '(' , '{' , '}'", tokenActual.getValor());
            }
        }
    }
    
    private NodoSentencia sentencia() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("sentencia :: "+this.tokenActual.getValor());
        int fil = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        NodoSentencia ret = null;
        if(!macheo(";")){
            String[] args = { "id_objeto" , "self" };
            if(verifico(args)){
                ret = asignacion();
                if(!macheo(";")){
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                if(verifico("(")){
                    ret = sentenciaSimple();
                    if(!macheo(";")){
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                    }
                }else{
                    if(macheo("if") && macheo("(")){ //TODO TEST
                        NodoIf aux = new NodoIf(fil,col);
                        aux.setDeclaracion(expresion());
                        this.ast.pushScope(aux);
                        if(macheo(")")){
                            if(!verifico("{")){
                                ((NodoIf) this.ast.peekScope()).addSentencia(sentencia());
                            }else{
                                sentencia();
                            }
                            sentenciaPrima();
                            ret = (NodoSentencia) this.ast.popScope();
                        }else{
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
                        }
                    }else{
                        if(macheo("while")){
                            if(macheo("(")){
                                NodoWhile nW = new NodoWhile(fil,col);
                                nW.setDeclaracion(expresion());
                                this.ast.pushScope(nW);
                                if(macheo(")")){
                                    sentencia();
                                    ret = (NodoSentencia) this.ast.popScope();
                                }else{
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
                                } 
                            }else{
                                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
                            }
                        }else{
                            if(macheo("return")){ 
                                ret = x();
//                                this.ast.pushScope(ret);
                                for (int i = this.ast.getScope().size()-1 ; i >= 0 ; i--) {
                                    if(this.ast.getScope().get(i).getClass().equals(NodoMetodo.class)){
                                        NodoMetodo nm = (NodoMetodo) this.ast.getScope().get(i);
                                        nm.setRetorno((NodoExpresion)ret);
                                    }
                                }
                                ret = (NodoSentencia) ret;
                                if(!macheo(";")){
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                                }
                            }else{
                                if(verifico("{")){
                                    bloque();
                                }else{
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{', 'return', 'while', '(', 'if',  'self' ", tokenActual.getValor());
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    
    private void sentenciaPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("sentenciaPrima :: "+this.tokenActual.getValor());
        if(macheo("else")){
            ((NodoIf) this.ast.peekScope()).setIsElse(true);
            if(!verifico("{")){
                ((NodoIf) this.ast.peekScope()).addSentencia(sentencia());
            }else{
                sentencia();
            }
        }
    }
    
    private NodoSentencia x() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("x :: "+this.tokenActual.getValor());
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new"};
        if(verifico(args)){
            return expresion();
        }else{
            String[] args2 = {")" , ";"};
            if(!verifico(args2)){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new', ')' , ';'", tokenActual.getValor());
            }
            return null;
        }
    }
    
    private void listaDeclaracionVariables(String tipo, boolean priv) throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listaDeclaracionVariables :: "+this.tokenActual.getValor());
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
    
    private void listaDeclaracionVariablesPrima(String tipo, boolean priv) throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listaDeclaracionVariablesPrima :: "+this.tokenActual.getValor());
        if(macheo(",")){
            listaDeclaracionVariables(tipo , priv);
        }else{
            if(!verifico(";")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ';'", tokenActual.getValor());
            }
        }
    }
    
    private NodoSentencia asignacion() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("asignacion :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        String auxNom = this.tokenActual.getValor();
        if(verifico("id_objeto")){ 
            //TODO puede fallar sino esta en el metodo
            EntradaVar varAux = this.ts.getMetodoActual().getVariablesMet().get(auxNom);
            
            if(varAux == null){
                varAux = this.ts.getClaseActual().getVariablesInst().get(auxNom);
                if(varAux == null){
                    if(this.ts.getMetodoActual().getConstanteMet().containsKey(auxNom) || this.ts.getClaseActual().getConstantes().containsKey(auxNom)){
                        throw new ExcepcionSemantica(tokenActual,"no se puede redefinir una constante", tokenActual.getValor(),true);
                    }else{
                        throw new ExcepcionSemantica(tokenActual,"la variable no fue declarada correctamente", tokenActual.getValor(),true);
                    }
                }
            }
            this.ast.pushScope(new NodoExpresion(fila,col,auxNom,varAux.getTipo()));
            accesoVarSimple();
        }else{ 
            if(verifico("self")){
                //TODO puede fallar sino esta en el metodo
                this.ast.pushScope(new NodoExpresion(fila,col,"self"));
                ((NodoExpresion) this.ast.peekScope()).setValor(this.ts.getClaseActual().getNombre());
                accesoSelfSimple();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto' o 'self'", tokenActual.getValor());
            }
        }
        if(macheo("=")){
            NodoExpresion nEI = (NodoExpresion) this.ast.popScope();
            NodoExpresion nED = expresion();
            return new NodoAsignacion(fila,col,nEI,nED,"var");
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
        }
    }
    
    private void accesoVarSimple() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("accesoVarSimple :: "+this.tokenActual.getValor());
        if(macheo("id_objeto")){
            enc();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void accesoSelfSimple() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("accesoSelfSimple :: "+this.tokenActual.getValor());
        if(macheo("self")){
            enc();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self'", tokenActual.getValor());
        }
    }
    
    private void enc() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("enc :: "+this.tokenActual.getValor());
        if(verifico(".")){ 
            encadenadoSimple();
            enc();
        }else{
            if(!verifico("=")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '=' o '.'", tokenActual.getValor());
            }
        }
    }
    
    private void encadenadoSimple() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("encadenadoSimple :: "+this.tokenActual.getValor());
        if(macheo(".")){
            String auxNom = this.tokenActual.getValor();
            if(!macheo("id_objeto")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }
            int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
            String tipo = null;
            NodoExpresion aux = (NodoExpresion) this.ast.popScope();
            if(aux.getNombre().equals("self")){
                tipo = (this.ts.getClaseActual().getVariablesInst().get(auxNom).getTipo());
                aux.setTipo(tipo);
            }
            this.ast.pushScope(new NodoLLamadaMetodo(fila,col,auxNom,aux));
            ((NodoLLamadaMetodo)this.ast.peekScope()).setTipo(tipo);
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.'", tokenActual.getValor());
        }
    }
    
    private NodoSentencia sentenciaSimple() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("sentSimp :: "+this.tokenActual.getValor());
        if(macheo("(")){
            NodoSentencia nS = expresion(); 
            
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
            return nS;
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        }
    }
    
    private NodoExpresion expresion() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expresion :: "+this.tokenActual.getValor());
        return expOr();
    }
    
    private NodoExpresion expOr() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expOr :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        NodoExpresion izq = expAnd();
        NodoExpresion der = expOrPrima();
        if(der == null){
            return izq;
        }
        return new NodoExpresionBinaria(fila,col,izq,"||",der);
    }
    
    private NodoExpresion expOrPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expOrPrima :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(macheo("||")){
            NodoExpresion izq = expAnd();
            NodoExpresion der = expOrPrima();
            if(der == null){
                return izq;
            }
            return new NodoExpresionBinaria(fila,col,izq,"||",der);
        }
        return null;
    }
    
    private NodoExpresion expAnd() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expAnd :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        NodoExpresion izq = expIgual();
        NodoExpresion der = expAndPrima();
        if(der == null){
            return izq;
        }
        return new NodoExpresionBinaria(fila,col,izq,"&&",der);
    }
    
    private NodoExpresion expAndPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expAndPrima :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(macheo("&&")){
            NodoExpresion izq = expIgual();
            NodoExpresion der = expAndPrima();
            if(der == null){
                return izq;
            }
            return new NodoExpresionBinaria(fila,col,izq,"&&",der);
        }
        return null;
    }
    
    private NodoExpresion expIgual() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expIgual :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        this.ast.pushScope(new NodoExpresionBinaria(fila,col));
        NodoExpresion izq = expCompuesta();
        ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
        NodoExpresion der = expIgualPrima();
        if(der == null){
            this.ast.popScope();
            return izq;
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
        return (NodoExpresionBinaria) this.ast.popScope();
    }
    
    private NodoExpresion expIgualPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expIgualPrima :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        String[] args = {"==" , "!="};
        if(verifico(args)){
            opIgual();
            this.ast.pushScope(new NodoExpresionBinaria(fila,col));
            NodoExpresion izq = expCompuesta();
            ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
            NodoExpresion der = expIgualPrima();
            if(der == null){
                this.ast.popScope();
                return izq;
            }
            ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
            return (NodoExpresionBinaria) this.ast.popScope();
        }
        return null;
    }
    
    private NodoExpresion expCompuesta() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expCompuesta :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        this.ast.pushScope(new NodoExpresionBinaria(fila,col));
        NodoExpresion izq = expAdd();
        ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
        NodoExpresion der = expCompuestaPrima();
        if(der == null){
            this.ast.popScope();
            return izq;
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
        return (NodoExpresionBinaria) this.ast.popScope();
    }
    
    private NodoExpresion expCompuestaPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expCompuestaPrima :: "+this.tokenActual.getValor());
        String[] args = {"<" , ">" , "<=" , ">="};
        if(verifico(args)){
            opCompuesto();
            return  expAdd();
        }
        return null;
    }
    
    private NodoExpresion expAdd() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expAdd :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        this.ast.pushScope(new NodoExpresionBinaria(fila,col));
        NodoExpresion izq = expMul();
        ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
        NodoExpresion der = expAddPrima();
        if(der == null){
            this.ast.popScope();
            return izq;
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
        return (NodoExpresionBinaria) this.ast.popScope();
    }
    
    private NodoExpresion expAddPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expAddPrima :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        String[] args = { "+" , "-"};
        if(verifico(args)){
            opAdd();
            this.ast.pushScope(new NodoExpresionBinaria(fila,col));
            NodoExpresion izq = expMul();
            ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
            NodoExpresion der = expAddPrima();
            if(der == null){
                this.ast.popScope();
                return izq;
            }
            ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
            return (NodoExpresionBinaria) this.ast.popScope();
        }
        return null;
    }
    
    private NodoExpresion expMul() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expMul :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        this.ast.pushScope(new NodoExpresionBinaria(fila,col));
        NodoExpresion izq = expUn();
        ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
        NodoExpresion der = expMulPrima();
        if(der == null){
            this.ast.popScope();
            return izq;
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
        return (NodoExpresionBinaria) this.ast.popScope();
    }
    
    private NodoExpresion expMulPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expMulPrima :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        String[] args = {"*" , "/" , "%"};
        if(verifico(args)){
            opMul();
            this.ast.pushScope(new NodoExpresionBinaria(fila,col));
            NodoExpresion izq = expUn();
            ((NodoExpresionBinaria) this.ast.peekScope()).setIzq(izq);
            NodoExpresion der = expMulPrima();  
            if(der == null){
                this.ast.popScope();
                return izq;
            }
            ((NodoExpresionBinaria) this.ast.peekScope()).setDer(der);
            return (NodoExpresionBinaria) this.ast.popScope();
        }
        return null;
    }
    
    private NodoExpresion expUn() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expUn :: "+this.tokenActual.getValor());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        String[] args = {"+" , "-" , "!"};
        if(verifico(args)){ 
            this.ast.pushScope(new NodoExpresionUnaria(fila,col));
            opUnario();
            ((NodoExpresionUnaria) this.ast.peekScope()).setDer(expUn());
            return (NodoExpresionUnaria) this.ast.popScope();
        }else{ 
            String[] args2 = {"nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "id_clase" , "new"};
            if(verifico(args2)){
                return operando();
            }else{
                throw new ExcepcionSintactica(tokenActual,"mal finalizacion de expresion", tokenActual.getValor());
            }  
        }
    }
    
    private void opIgual() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("opIgual :: "+this.tokenActual.getValor());
        String oper = this.tokenActual.getValor();
        if( !macheo("==") &&
            !macheo("!=")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '!=' , '=='", tokenActual.getValor());
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setOper(oper);
    }
    
    private void opCompuesto() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("opCompuesto :: "+this.tokenActual.getValor());
        String oper = this.tokenActual.getValor();
        if(!macheo("<") && !macheo(">") && !macheo("<=") && !macheo(">=")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '<' , '>' , '<=', '>='", tokenActual.getValor());
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setOper(oper);
    }
    
    private void opAdd() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("opAdd :: "+this.tokenActual.getValor());
        String oper = this.tokenActual.getValor();
        if(!macheo("+") && !macheo("-") ){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-'", tokenActual.getValor());
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setOper(oper);
    }
    
    private void opUnario() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("opUnario :: "+this.tokenActual.getValor());
        String oper = this.tokenActual.getValor();
        if(!macheo("+") && !macheo("-") && !macheo("!")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-', '!'", tokenActual.getValor());
        }
        ((NodoExpresionUnaria) this.ast.peekScope()).setOper(oper);
    }
    
    private void opMul() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("opMul :: "+this.tokenActual.getValor());
        String oper = this.tokenActual.getValor();
        if(!macheo("*") && !macheo("/") && !macheo("%")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '*' , '/', '%'", tokenActual.getValor());
        }
        ((NodoExpresionBinaria) this.ast.peekScope()).setOper(oper);
    }
    
    private NodoExpresion operando() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("operando :: "+this.tokenActual.getValor());
        String[] args = {"nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car"};
        if(verifico(args)){ 
            return literal();
        }else{
            String[] args2 = {"(" , "self" , "id_objeto", "id_clase" , "new"};
            if(verifico(args2)){ 
                return primario();
            }else{
                throw new ExcepcionSintactica(tokenActual,"mal finalizacion de operando", tokenActual.getValor());
            }
        }
    }
    
    private NodoExpresion literal() throws ExcepcionSintactica,ExcepcionSemantica {
        String aux = this.tokenActual.getpReservada();
        String auxVal = this.tokenActual.getValor();
//        //System.out.println("literal :: "+this.tokenActual.getValor()+" && "+aux);
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(!macheo("nil") && !macheo("true") && !macheo("false") && !macheo("lit_ent") && !macheo("lit_cad") && !macheo("lit_car")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car'", tokenActual.getValor());
        }
        String auxTipo;
        if(aux.equals("p_nil")){
            auxTipo = "nil";
        }else{
            if(aux.equals("p_true") || aux.equals("p_false")){
                auxTipo = "Bool";
            }else{
                if(aux.equals("lit_ent")){
                    auxTipo = "Int";
                }else{
                    if(aux.equals("lit_cad")){
                        auxTipo = "String";
                    }else{
                        auxTipo = "Char";
                    }
                }
            }
        }
        NodoExpresion nE = new NodoExpresion(fila,col,"literal",auxTipo);
        if(auxTipo.equals("Bool")){
            auxVal = auxVal.equals("true")? ""+1 : ""+0;
        }
        nE.setValor(auxVal);
        return nE;
    }
    
    private NodoExpresion primario() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("primario :: "+this.tokenActual.getValor()+ " tok "+this.tokenActual.getpReservada());
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        this.ast.pushScope(new NodoExpresion(fila,col));
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
                        String auxNom = this.tokenActual.getValor();
                        if(macheo("id_objeto")){
                            this.ast.popScope();
                            this.ast.pushScope(new NodoExpresion(fila,col,auxNom));
                            EntradaVar auxType = this.ts.getMetodoActual() != null? this.ts.getMetodoActual().getVariablesMet().get(auxNom) : null ;
                            String auxTipo = null ;
                            if(auxType != null ){
                               auxTipo =  auxType.getTipo();
                            }else{
                                if(this.ts.getMetodoActual() != null && this.ts.getMetodoActual().getConstanteMet().containsKey(auxNom)){
                                    auxTipo = this.ts.getMetodoActual().getConstanteMet().get(auxNom).getTipo();
                                }else{
                                    if(this.ts.getMetodoActual() != null && this.ts.getMetodoActual().existPar(auxNom)){
                                        auxTipo = this.ts.getMetodoActual().getParByName(auxNom).getTipo();
                                    }else{
                                        if(this.ts.getClaseActual().getVariablesInst().containsKey(auxNom)){
                                            auxTipo = this.ts.getClaseActual().getVariablesInst().get(auxNom).getTipo();
                                        }else{
                                            if(this.ts.getClaseActual().getConstantes().containsKey(auxNom)){
                                                auxTipo = this.ts.getClaseActual().getConstantes().get(auxNom).getTipo();
                                            }else{
                                                //no esta declarado todavia
//                                                System.out.println("ERROR DE IMPLEMENTACION NO SE ENCUENTRA EL TIPO DE LA CLASE EN LA FILA "+fila);
//                                                throw new ExcepcionSemantica(tokenActual.getFila(), tokenActual.getColumna(),"El identificador de objeto no fue declarado o no fue inicializado todavia", tokenActual.getValor(),true);
                                            }
                                        }
                                    }
                                }
                            }
                            ((NodoExpresion) this.ast.peekScope()).setTipo(auxTipo);
                            primarioPrima();
                        }else{
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' , 'self' , 'id_clase', 'id_objeto' , 'new'", tokenActual.getValor());
                        }
                    }
                }
            }
        }
        return (NodoExpresion) this.ast.popScope();
    }
    private void primarioPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("primPrim :: "+this.tokenActual.getValor());
        if(verifico("(")){ 
            llamadaMetodo();
        }else{
            String[] args = {".", ",", "*" , "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" ,"==" , "!=" , "&&" , "||" , ")" , ";"};
            if(verifico(args)){
                encmul();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' o '(' ", tokenActual.getValor());
            }
        }
    }
        
    
    private void expresionParentizada() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("expPar :: "+this.tokenActual.getValor());
        if(macheo("(")){
            NodoExpresion der = expresion();
            this.ast.popScope();
            this.ast.pushScope(der);
            if(macheo(")")){
                encmul();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' ", tokenActual.getValor());            
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());        
        }
    }
    
    private void encmul() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("encMul :: "+this.tokenActual.getValor());
        if(verifico(".")){ 
            encadenado();
        }else{
            String[] args = {"*", ",", "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args)){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.', '*' , '/' , '%' , '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new' , '==' , '!=' , '&&' , '||' , ')' , ';' ", tokenActual.getValor());            
            }
        }
    }
    
    private void accesoSelf() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("accesoSelf :: "+this.tokenActual.getValor());
        if(macheo("self")){
            ((NodoExpresion) this.ast.peekScope()).setNombre("self");
            ((NodoExpresion) this.ast.peekScope()).setValor(this.ts.getClaseActual().getNombre());
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self' ", tokenActual.getValor());
        }
    }
    
    private void llamadaMetodo() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("llamadaMetodo :: "+this.tokenActual.getValor());
        if(this.ast.peekScope().getClass() != NodoLLamadaMetodo.class){
            NodoExpresion aux = (NodoExpresion) this.ast.popScope();
            this.ast.pushScope(new NodoLLamadaMetodo(aux.getFila(),aux.getCol(),aux.getNombre(),this.ts.getClaseActual().getNombre()));
            
        }
        argumentosActuales();
        encmul();        
    }
    
    private void llamadaMetodoEstatico() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("llamadaMetodoEstatico :: "+this.tokenActual.getValor());
        String auxClas = this.tokenActual.getValor();
        int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
        if(macheo("id_clase")){
            if(macheo(".")){
                String auxNom = this.tokenActual.getValor();
                if(macheo("id_objeto")){
                    this.ast.popScope().imprimeSentencia();
                    
                    this.ast.pushScope(new NodoLLamadaMetodo(fila,col,auxNom,auxClas));
                    llamadaMetodo();
                    encmul();
                }else{
                    if(this.tokenActual.getValor().equals("init")){
                        throw new ExcepcionSintactica(tokenActual,"no se puede acceder al constructor de una clase mediante una llamada a metodo", tokenActual.getValor());
                    }
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
                }
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor()); 
        }
        
    }
    
    private void llamadaConstructor() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("llamadaConstructor :: "+this.tokenActual.getValor());
        if(macheo("new")){
            int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
            String auxNom = this.tokenActual.getValor();
            if(macheo("id_clase")){
                this.ast.popScope();
                this.ast.pushScope(new NodoLLamadaMetodo(fila,col,"constructor",auxNom));
                argumentosActuales();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'new'", tokenActual.getValor());
        }
    }
    
    private void argumentosActuales() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("argAct :: "+this.tokenActual.getValor());
        if(macheo("(")){
            ((NodoLLamadaMetodo)this.ast.peekScope()).setIsMetodo(true);
            listExp();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        }
    }
    
    private void listExp() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listExp :: "+this.tokenActual.getValor());
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" };
        if(verifico(args)){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"mal declaracion de expresion", tokenActual.getValor());
            }
        }
    }
    
    private void listaExpresiones() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listaExpresiones :: "+this.tokenActual.getValor());
        ((NodoLLamadaMetodo)this.ast.peekScope()).putArgs(expresion());
        listaExpresionesPrima();
    }
    
    private void listaExpresionesPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("listaExpresionesPrima :: "+this.tokenActual.getValor());
        if(macheo(",")){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' o ','", tokenActual.getValor());
            }
        }
    }
    
    private void encadenado() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("encad :: "+this.tokenActual.getValor());
        if(macheo(".")){
            String auxNom = this.tokenActual.getValor();
            if(macheo("id_objeto")){
                int fila = this.tokenActual.getFila(), col = this.tokenActual.getColumna();
                String tipo = null;
                NodoExpresion aux = (NodoExpresion) this.ast.popScope();
                if(aux.getNombre().equals("self")){
                    EntradaClase auxClase = this.ts.getClaseActual();
                    if(auxClase.getVariablesInst().containsKey(auxNom)){
                        tipo = (auxClase.getVariablesInst().get(auxNom).getTipo());
                    }else{
                        if(auxClase.getConstantes().containsKey(auxNom)){
                            tipo = (auxClase.getConstantes().get(auxNom).getTipo());
                        }else{
                            throw new ExcepcionSemantica(tokenActual,"la variable no fue declarada correctamente", tokenActual.getValor(),true);
                        }
                    }
                    aux.setTipo(tipo);
                }
                
                this.ast.pushScope(new NodoLLamadaMetodo(fila,col,auxNom,aux));
                ((NodoLLamadaMetodo)this.ast.peekScope()).setTipo(tipo);
                encadenadoPrima();
            }else{
              throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }        
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' ", tokenActual.getValor());
        }        
    }
    
    private void encadenadoPrima() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("encadenadoPrima :: "+this.tokenActual.getValor());
        if(verifico("(")){ 
            llamadaMetodoEncadenado();
        }else{ 
//            if(verifico(".")){ 
                encmul();
//            }else{
//                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' o '.'", tokenActual.getValor());
//            }
        }
    }
    
    private void llamadaMetodoEncadenado() throws ExcepcionSintactica,ExcepcionSemantica {
        //System.out.println("llamadaMetodoEncadenado :: "+this.tokenActual.getValor());
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

    public ArbolSintacticoAbstracto getAst() {
        return ast;
    }
    
    
}
