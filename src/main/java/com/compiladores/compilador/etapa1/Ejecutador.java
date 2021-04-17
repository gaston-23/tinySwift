package com.compiladores.compilador.etapa1;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.compiladores.compilador.etapa1.AnalizadorLexico.Token;

public class Ejecutador {

	public static void main(String[] args) {
		AnalizadorLexico aL = new AnalizadorLexico(args[0]);
		boolean imprimir = args.length == 2;
		FileWriter destino = null;
		PrintWriter impresora = null;
		String linea, cabecera= "| TOKEN | LEXEMA | NUMERO DE LINEA |\n";
		
		
		
		
		if(imprimir) {
			try {
				destino = new FileWriter(args[1]);
				impresora = new PrintWriter(destino);
				impresora.write(cabecera);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}else {
			System.out.print(cabecera);
		}
		try {
			while(aL.hasNextToken()) {
				Token actualToken = aL.nextToken();
				linea = "| "+ actualToken.getpReservada() + 
						" | "+ actualToken.getValor()+
						" | "+ actualToken.getFila() + " |\n";
				if(!imprimir) {
					System.out.print(linea);
						
				}else {
					impresora.write(linea);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(imprimir) {
				try {
					impresora.close();
				}catch(Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
	}
	

}
