/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.main;

import com.compiladores.compilador.etapa1.*;
import com.compiladores.compilador.etapa2.*;
import com.compiladores.compilador.etapa3.AnalizadorSemantico;
import com.compiladores.compilador.etapa5.GeneradorCodigo;
import java.io.FileWriter;
import java.io.PrintWriter;


/**
 *
 * @author Gaston Cavallo
 */
public class Main {
    
    public static void main(String[] args) {
        String ruta = args[0].split(".swift")[0];
        AnalizadorLexico aL = null;
        AnalizadorSintactico aS = null;
        AnalizadorSemantico aD = null;
        GeneradorCodigo cG = null;
        try{
            aL = new AnalizadorLexico(args[0]);
            aS = new AnalizadorSintactico(aL,ruta);
            aD = new AnalizadorSemantico(aS);
            cG = new GeneradorCodigo(aS.getTs(),aS.getAst());
            FileWriter destinoTs = null,destinoAst = null, destinoGc = null;
            PrintWriter impresoraTs = null,impresoraAst = null, impresoraGc = null;

    //        Ejecutador eAL = new Ejecutador();
    //        String[] argu2 = {args[0],args[0].split(".swift")[0]+".txt"} ;
    //        eAL.main(argu2);
            
            destinoTs = new FileWriter(ruta+".ts.json");
            destinoAst = new FileWriter(ruta+".ast.json");
            destinoGc = new FileWriter(ruta+".asm");
            impresoraTs = new PrintWriter(destinoTs);
            impresoraAst = new PrintWriter(destinoAst);
            impresoraGc = new PrintWriter(destinoGc);
            
            try {
                impresoraAst.write(aS.getAst().imprimeAST());
                impresoraTs.write(aS.getTs().imprimeTS());
                impresoraGc.write(cG.getCodigo());
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                    impresoraTs.close();
                    impresoraAst.close();
                    impresoraGc.close();
            }
        }catch(ExcepcionSintactica eS){
            System.out.println(eS.toString());
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
