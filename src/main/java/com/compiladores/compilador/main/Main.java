/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.main;

import com.compiladores.compilador.etapa1.*;
import com.compiladores.compilador.etapa2.*;
import com.compiladores.compilador.etapa3.AnalizadorSemantico;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;


/**
 *
 * @author Gaston Cavallo
 */
public class Main {
    
    public static void main(String[] args) {
        
        AnalizadorLexico aL = new AnalizadorLexico(args[0]);
        AnalizadorSintactico aS = new AnalizadorSintactico(aL);
        AnalizadorSemantico aD = new AnalizadorSemantico(aS);
        FileWriter destino = null;
        PrintWriter impresora = null;
        char barra = File.separatorChar;
        String ruta = "", rutaTok = "";
        
        File rr = new File(args[0]);
        ruta = rr.getParent() + barra + "./salida.json";      
        System.out.println(ruta);
        rutaTok = args[0].substring(0, args[0].lastIndexOf(barra))+barra+"tokens.txt";
        
        Ejecutador eAL = new Ejecutador();
        String[] argu2 = {args[0],rutaTok} ;
        eAL.main(argu2);
        try {
            destino = new FileWriter(ruta);
            impresora = new PrintWriter(destino);
        }catch(Exception e) {
            e.printStackTrace();
        }
        try {
            impresora.write(aS.getTs().imprimeTS());
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
