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
}
