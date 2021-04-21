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
}
