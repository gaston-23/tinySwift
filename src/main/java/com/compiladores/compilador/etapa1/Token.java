/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa1;

    /**
    * Clase que define al Token lexico
    * @author Gaston Cavallo
    * @author Mariel Volman
    **
    */
    public class Token {
        private final int fila;
        private final int columna;
        private final String pReservada;
        private final String valor;

        /**
         * Constructor de la clase Token
         * @param fila Fila en la que se encuentra con respecto del archivo .swift
         * @param columna Columna en la que comienza el token
         * @param pReservada Nombre del token
         * @param valor Lexema correspondiente
         */
        public Token(int fila, int columna, String pReservada, String valor) {
                this.columna = columna;
                this.fila = fila;
                this.pReservada = pReservada;
                this.valor = valor;
        }

        /**
         * Metodo para obtener la fila del token
         * @return numero de fila
         */
        public int getFila() {
                return fila;
        }

        /**
         * Metodo para obtener la columna del token
         * @return numero de columna
         */
        public int getColumna() {
                return columna;
        }

        /**
         * Metodo para obtener el nombre del token
         * @return nombre del token
         */
        public String getpReservada() {
                return pReservada;
        }

        /**
         * Metodo para obtener el lexema del token
         * @return lexema del token
         */
        public String getValor() {
                return valor;
        }

   }
	
