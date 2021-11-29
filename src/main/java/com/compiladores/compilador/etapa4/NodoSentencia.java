/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa4;

import com.compiladores.compilador.etapa2.ExcepcionSintactica;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;

/**
 *
 * @author Gaston Cavallo
 */
public class NodoSentencia extends NodoAST {
    
    public NodoSentencia(int filaTok,int colTok){
        super(filaTok,colTok);
    }
    
    @Override
    public boolean verifica(TablaDeSimbolos ts) throws ExcepcionSemantica,ExcepcionSintactica{
        return true;
    }
    
    @Override
    public String imprimeSentencia(){
        return "\"nodo\": \"Sentencia\"";
    }
    
}
