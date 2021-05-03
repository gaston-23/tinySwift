/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa2;

import com.compiladores.compilador.etapa1.AnalizadorLexico;
import com.compiladores.compilador.etapa1.Token;


/**
 * Clase AnalizadorSintactico
 * Se encarga de corroborar, a partir de una lista de tokens, si el codigo fuente coincide con la gramatica
 * @author Gaston Cavallo
 * @author Mariel Volman
 */
public class AnalizadorSintactico {
    
    private Token tokenActual ;
    private AnalizadorLexico al;
    
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
        try{
            this.program();
        }catch(ExcepcionSintactica eS){
            System.out.println(eS.mensaje);
            exito = false;
        }
        if(exito) System.out.println("CORRECTO: ANALISIS SINTACTICO");
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
    private void program() throws ExcepcionSintactica {
        if(macheo("class")){
           c(); 
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o 'EOF'", tokenActual.getValor());
        }
    }
    
    private void programPrima() throws ExcepcionSintactica {
        if(macheo("class")){
           c(); 
        }else{
            if(!verifico("EOF")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o 'EOF'", tokenActual.getValor());
            }
        }
    }

    private void c() throws ExcepcionSintactica {
        if( verifico("id_clase") ){
            clase();
            programPrima();
        }else{
            if(verifico("Main")){
                claseMain();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'class' o EOF", tokenActual.getValor());
            }
        }
    }
    
    private void claseMain() throws ExcepcionSintactica {
        if( macheo("Main")){
            h();
            if(macheo("{")){
                m();
                bloque();
                m();
                if(!macheo("}")){
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
                }
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba una declaracion de tipo 'class Main'", tokenActual.getValor());
        }
    }

    private void h() throws ExcepcionSintactica {
        if(verifico(":")){
            herencia();
        }else{
            if(!verifico("{")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' o '{'", tokenActual.getValor());
            }
        }
    }
    
    private void m() throws ExcepcionSintactica {
        if(!main() && !verifico("}")){
            String[] args = {"private",  "static" , "init" , "let", "var", "func"};
            if(verifico(args)){
                miembro();
                m();
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let', 'var', 'func', '{' o una declaracion de tipo 'static func void main ()'", tokenActual.getValor());

            }
        }
    }
    
    private boolean main() throws ExcepcionSintactica {
        return (macheo("static") && macheo("func") && macheo("void")  && macheo("main") && macheo("(") && macheo(")")) ;
    }

    private void clase() throws ExcepcionSintactica {
        if( macheo("id_clase")){
            h(); 
            if( macheo("{") ){
               m();
               if( !macheo("}") ){ 
                   throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
               }
            }else {
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}'", tokenActual.getValor());
            }

        }else {
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token  'id_clase'", tokenActual.getValor());
        }
        
    }

    private void herencia() throws ExcepcionSintactica {
        if(macheo(":")){
            tipo();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
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
                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private',  'static' , 'init' , 'let'", tokenActual.getValor());
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
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'init'", tokenActual.getValor());
        }
    }

    private void atributo() throws ExcepcionSintactica {
        v();
        if(macheo("var")){
            tipo();
            if(macheo(":")){
                listaDeclaracionVariables();
                if(!macheo(";")){
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'var'", tokenActual.getValor());
        }
    }
    
    private void v() throws ExcepcionSintactica {
        if(verifico("private")){
            visibilidad();
        }else{
            if(!verifico("var")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'var'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'func'", tokenActual.getValor());
        }
    }

    private void f() throws ExcepcionSintactica {
        if(verifico("static")){
            formaMetodo();
        }else{
            if(!verifico("func")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'static' o 'func'", tokenActual.getValor());
            }
        }
    }

    private void visibilidad() throws ExcepcionSintactica {
        if(!macheo("private")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'private'", tokenActual.getValor());
        }
    }

    private void formaMetodo() throws ExcepcionSintactica {
        if(!macheo("static")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'static'", tokenActual.getValor());
        }
    }

    private void constante() throws ExcepcionSintactica {
        if(macheo("let")){
            tipoPrimitivo();
            if(macheo(":") && macheo("id_objeto") && macheo("=")){
                expresion();
                if(!macheo(";")){
                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                }
            }else{
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' , 'id_objeto' o '='", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'let' ", tokenActual.getValor());
        }
    }

    private void argumentosFormales() throws ExcepcionSintactica {
        if(macheo("(")){
            l();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' ", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());
        }
        
    }

    private void l() throws ExcepcionSintactica {
        String[] args = { "String" , "Bool" , "Int" , "Char" };
        if(verifico(args)){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
            }
        }
    }

    private void listaArgumentosFormales() throws ExcepcionSintactica {
        String[] args = { "String" , "Bool" , "Int" , "Char" };
        if(verifico(args)){
            argumentoFormal();
            listaArgumentosFormalesPrima();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char'", tokenActual.getValor());
        }
    }

    private void listaArgumentosFormalesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaArgumentosFormales();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ')'", tokenActual.getValor());
            }
        }
    }

    private void argumentoFormal() throws ExcepcionSintactica {
        tipo();
        if(!macheo(":") || !macheo("id_objeto")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':' o 'id_objeto'", tokenActual.getValor());
        }
    }

    private void tipoMetodo() throws ExcepcionSintactica {
        String[] args = { "String","Bool","Int","Char","id_clase" };
        if(verifico(args)){
            tipo();
        }else{
            if(!macheo("void")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char' o 'id_clase", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'String' , 'Bool' , 'Int' , 'Char' o 'id_clase", tokenActual.getValor());
            }
        }
        
    }

    private void tipoPrimitivo() throws ExcepcionSintactica {
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

    private void tipoReferencia() throws ExcepcionSintactica {
        if(!macheo("id_clase")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase'", tokenActual.getValor());
        }
    }

    private void bloque() throws ExcepcionSintactica {
        if(macheo("{")){
           s(); 
            if(!macheo("}")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '{'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';' , 'var' , 'if', 'let' , 'while' , 'return' , 'id_objeto' , 'self' , '(' , '{' , '}'", tokenActual.getValor());
            }
        }
    }
    
    private void sentencia() throws ExcepcionSintactica {
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
                    if(macheo("var")){
                        tipo();
                        if(macheo(":")){
                            listaDeclaracionVariables();
                            if(!macheo(";")){
                                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                            }
                        }else{
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':'", tokenActual.getValor());
                        }
                    }else{
                        if(macheo("if") && macheo("(")){ //TODO TEST
                            expresion();
                            if(macheo(")")){
                                sentencia();
                                sentenciaPrima();
                            }else{
                                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'if' , '(' o ')'", tokenActual.getValor());
                            }
                        }else{
                            if(macheo("let")){
                                tipoPrimitivo();
                                if(macheo(":") && macheo("id_objeto") && macheo("=")){
                                    expresion();
                                    if(!macheo(";")){
                                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token ';'", tokenActual.getValor());
                                    }
                                }else{
                                    throw new ExcepcionSintactica(tokenActual,"se esperaba un token ':', 'id_objeto', '='", tokenActual.getValor());
                                }
                            }else{
                                if(macheo("while") && macheo("(")){
                                    expresion();
                                    if(macheo(")")){
                                        sentencia();
                                    }else{
                                        throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
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
        }
    }
    
    private void sentenciaPrima() throws ExcepcionSintactica {
        if(macheo("else")){
            sentencia();
        }else{
            if(!verifico("}")){ //TODO TEST
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '}' o 'else'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new', ')' , ';'", tokenActual.getValor());
            }
        }
    }
    
    private void listaDeclaracionVariables() throws ExcepcionSintactica {
        if(macheo("id_objeto")){
            listaDeclaracionVariablesPrima();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void listaDeclaracionVariablesPrima() throws ExcepcionSintactica {
        if(macheo(",")){
            listaDeclaracionVariables();
        }else{
            if(!verifico(";")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ',' o ';'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto' o 'self'", tokenActual.getValor());
            }
        }
        if(macheo("=")){
            expresion();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
        }
    }
    
    private void accesoVarSimple() throws ExcepcionSintactica {
        if(macheo("id_objeto")){
            enc();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto'", tokenActual.getValor());
        }
    }
    
    private void accesoSelfSimple() throws ExcepcionSintactica {
        if(macheo("self")){
            enc();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self'", tokenActual.getValor());
        }
    }
    
    private void enc() throws ExcepcionSintactica {
        if(verifico(".")){ 
            encadenadoSimple();
            enc();
        }else{
            if(!verifico("=")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '='", tokenActual.getValor());
            }
        }
    }
    
    private void encadenadoSimple() throws ExcepcionSintactica {
        if(!macheo(".") || !macheo("id_objeto")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_objeto' o '.'", tokenActual.getValor());
        }
    }
    
    private void sentenciaSimple() throws ExcepcionSintactica {
        if(macheo("(")){
            expresion();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '||' , ')' o ';'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&', '||' , ')' , ';' ", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&','==' , '!=', '||' , ')' , ';' ", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&','==' , '<' , '>' , '<=' , '>=', '!=', '||' , ')' , ';' ", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&', '+' , '-', '==' , '<' , '>' , '<=' , '>=', '!=', '||' , ')' , ';' ", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '&&', '*' , '/' , '%', '+' , '-', '==' , '<' , '>' , '<=' , '>=', '!=', '||' , ')' , ';' ", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-' , '!', 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto', 'id_clase' , 'new'", tokenActual.getValor());
            }  
        }
    }
    
    private void opIgual() throws ExcepcionSintactica {
        if(!macheo("==")){
            if(!macheo("!=")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '!=' , '=='", tokenActual.getValor());
            }
        }
    }
    
    private void opCompuesto() throws ExcepcionSintactica {
        if(!macheo("<") && !macheo(">") && !macheo("<=") && !macheo(">=")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '<' , '>' , '<=', '>='", tokenActual.getValor());
        }
    }
    
    private void opAdd() throws ExcepcionSintactica {
        if(!macheo("+") && !macheo("-") ){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-'", tokenActual.getValor());
        }
    }
    
    private void opUnario() throws ExcepcionSintactica {
        if(!macheo("+") && !macheo("-") && !macheo("!")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '+' , '-', '!'", tokenActual.getValor());
        }
    }
    
    private void opMul() throws ExcepcionSintactica {
        if(!macheo("*") && !macheo("/") && !macheo("%")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '*' , '/', '%'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car', '(' , 'self' , 'id_clase', 'id_objeto' , 'new'", tokenActual.getValor());
            }
        }
    }
    
    private void literal() throws ExcepcionSintactica {
        if(!macheo("nil") && !macheo("true") && !macheo("false") && !macheo("lit_ent") && !macheo("lit_cad") && !macheo("lit_car")){
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car'", tokenActual.getValor());
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
                            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' , 'self' , 'id_clase', 'id_objeto' , 'new'", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' o '(' ", tokenActual.getValor());
            }
        }
    }
        
    
    private void expresionParentizada() throws ExcepcionSintactica {
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
    
    ///////////////////////////
    private void encmul() throws ExcepcionSintactica {
        if(verifico(".")){ 
            encadenado();
        }else{
            String[] args = {"*" , "/" , "%" , "+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" , "<=" ,">=" ,">" ,"<" , "==" , "!=" , "&&" , "||" , ")" , ";"};
            if(!verifico(args)){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.', '*' , '/' , '%' , '+' , '-' , '!' , 'nil' , 'true' , 'false' , 'lit_ent' , 'lit_cad' , 'lit_car' , '(' , 'self' , 'id_objeto' , 'new' , '==' , '!=' , '&&' , '||' , ')' , ';' ", tokenActual.getValor());            
            }
        }
    }
    
    private void accesoSelf() throws ExcepcionSintactica {
        if(macheo("self")){
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'self' ", tokenActual.getValor());
        }
    }
    
    private void llamadaMetodo() throws ExcepcionSintactica {
        if(verifico("(")){
            argumentosActuales();
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '(' ", tokenActual.getValor());  
        }
        
    }
    
    private void llamadaMetodoEstatico() throws ExcepcionSintactica {
        if(macheo("id_clase") && macheo(".") && macheo("id_objeto")){
            llamadaMetodo();
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'id_clase' o '.' ", tokenActual.getValor()); 
        }
        
    }
    
    private void llamadaConstructor() throws ExcepcionSintactica {
        if(macheo("new") && macheo("id_clase")){
            argumentosActuales();
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token 'new' seguido por 'id_clase'", tokenActual.getValor());
        }
        
    }
    
    private void argumentosActuales() throws ExcepcionSintactica {
        if(macheo("(")){
            listExp();
            if(!macheo(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')'", tokenActual.getValor());
            }
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        }
        
    }
    
    private void listExp() throws ExcepcionSintactica {
        String[] args = {"+" , "-" , "!" , "nil" , "true" , "false" , "lit_ent" , "lit_cad" , "lit_car" , "(" , "self" , "id_objeto" , "new" };
        if(verifico(args)){
            listaExpresiones();
        }else{
            if(!verifico(")")){
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token \"+\" , \"-\" , \"!\" , \"nil\" , \"true\" , \"false\" , \"lit_ent\" , \"lit_cad\" , \"lit_car\" , \"(\" , \"self\" , \"id_objeto\" , \"new\"", tokenActual.getValor());
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
                throw new ExcepcionSintactica(tokenActual,"se esperaba un token ')' o ','", tokenActual.getValor());
            }
        }
    }
    
    private void encadenado() throws ExcepcionSintactica {
        if(macheo(".") && macheo("id_objeto")){
            encadenadoPrima();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '.' seguido por 'id_objeto'", tokenActual.getValor());
        }        
    }
    
    private void encadenadoPrima() throws ExcepcionSintactica {
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
    
    private void llamadaMetodoEncadenado() throws ExcepcionSintactica {
        if(verifico("(")){
            argumentosActuales();
            encmul();
        }else{
            throw new ExcepcionSintactica(tokenActual,"se esperaba un token '('", tokenActual.getValor());
        } 
        
    }
}
