/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.main;

import com.compiladores.compilador.etapa1.*;
import com.compiladores.compilador.etapa2.*;
import java.io.FileWriter;
import java.io.PrintWriter;


/**
 *
 * @author Gaston Cavallo
 */
public class Main {
    
    public static void main(String[] args) {
        
        AnalizadorLexico aL = new AnalizadorLexico(args[0]);
        AnalizadorSintactico aS = new AnalizadorSintactico(aL);
        FileWriter destino = null;
        PrintWriter impresora = null;
        
        try {
                destino = new FileWriter(args[1]);
                impresora = new PrintWriter(destino);
        }catch(Exception e) {
                e.printStackTrace();
        }
        try {
                impresora.write(aS.ts.imprimeTS());
        }catch (Exception e) {
                e.printStackTrace();
        }finally {
            try {
                    impresora.close();
            }catch(Exception e2) {
                    e2.printStackTrace();
            }
        }
        
    }
}
