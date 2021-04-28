/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa2;

import com.compiladores.compilador.etapa1.AnalizadorLexico;

/**
 *
 * @author gx23
 */
public class EjecutadorSintactico {
    
    public static void main(String[] args) {
        
        AnalizadorLexico aL = new AnalizadorLexico(args[0]);
        AnalizadorSintactico aS = new AnalizadorSintactico(aL);
        
    }
    
}
