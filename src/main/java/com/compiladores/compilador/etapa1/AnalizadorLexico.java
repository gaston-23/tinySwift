package com.compiladores.compilador.etapa1;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase AnalizadorLexico
 * Se encarga de obtener una lista de tokens a partir de un archivo y los almacena en una cola
 * @author Gaston Cavallo
 * @author Mariel Volman
 */
public class AnalizadorLexico {
	private Queue<Token> tokens = new LinkedList<>();
	
	/**
	 * Constructor para AnalizadorLexico, recibe una ruta como parametro, la cual tiene como destino un archivo, lo abre y almacena todos los tokens encontrados en dicho documento
	 * @param ruta Ruta del archivo .swift a analizar
	 */
	public AnalizadorLexico(String ruta) {
            int fila= 0;
            LectorArchivo lector = new LectorArchivo(ruta);
            String linea;
            
            int comentado = 0;
            boolean exito = true;
            while((linea = lector.getLine())!=null) {
                fila++;
                if(comentado == 1 && !linea.contains("*/")) {
                    continue;
                }
                if(linea.equals(" ") || linea.length()==0) {
                    continue;
                }
                //TODO: poner cuando es correcto el analisis
                try {
                    comentado = this.extractToken(linea.toCharArray(), fila, comentado);
                }catch (ExcepcionLexica e) {
                    System.err.println(e.mensaje);
                    exito = false;
                    break;
                }
            }
                if (exito) System.out.println("CORRECTO: ANALISIS LEXICO");
			
	}
	/**
	 * añade un token a la cola de tokens
	 * @param token Token a añadir a la cola tokens
	 */
	public void addToken(Token token) {
		this.tokens.add(token);
	}
	
	/**
	 * obtiene el primer token de la cola tokens y lo elimina de ella
	 * @return el primer token de la cola tokens
	 */
	public Token nextToken() {
		return this.tokens.remove();
	}
	
	/**
	 * metodo que consulta si quedan tokens en la cola tokens
	 * @return True si hay por lo menos un token
	 */
	public boolean hasNextToken() {
		return !this.tokens.isEmpty();
	}
	
	/**
	 * Metodo encargado de extraer los tokens de un String
	 * @param linea Array de caracteres correspondiente a la linea que se esta leyendo
	 * @param fila Linea sobre la cual se esta leyendo el String
	 * @param comentario Indica si la presente linea pertenece o no a un comentario multilinea
	 * @return 1 si la linea es un comentario multilinea, 0 si se continua con la ejecucion normalmente
	 * @throws ExcepcionLexica cuando detecta un error lexico
	 */
	private int extractToken(char[] linea, int fila, int comentario) throws ExcepcionLexica {
		
		String curToken = "";
		String state = "";
		for(int i = 0; i< linea.length; i++) {
			if(comentario == 1 ) {
				if( ( i+1 < linea.length && (""+linea[i]+linea[i+1]).equals("*/"))) {
					comentario = 0; //finaliza el comentario multilinea
				}
				continue;
			}
			if(state.equals("lit_cad")) {
				curToken += linea[i];
				if(linea[i] == '"') {
					Token token = new Token(fila, i - curToken.length() +1, state, curToken);
					this.addToken(token);
					curToken = "";
					state = "";
				}
				continue;
			}
			if(!curToken.equals("") && linea[i] == ' ') {
				Token token = new Token(fila, i - curToken.length() +1, state, curToken);
				this.addToken(token);
				curToken = "";
				state = "";
				continue;
			}
			//saltea los espacios
			if(linea[i] != ' ') {
				
				if(linea[i] == '/') {
					//si habia algun token se guarda
					if(!curToken.equals("")) {
						// guarda lo que tenia
						Token token = new Token(fila, i - curToken.length() +1, state, curToken);
						this.addToken(token);
						curToken = "";
						state = "";
					}
					//no es el ultimo caracter
					//comentario multilinea
					if(i+1 < linea.length && linea[i+1] == '*') {
						return 1;
						//comentario de linea
					}else if(i+1 < linea.length && linea[i+1] == '/') {
						return 0;
					}
					//solo es simbolo "/"
					Token token = new Token(fila, i , "op_suma", ""+linea[i]);
					this.addToken(token);
					continue;
					
				}else {
					//operadores suma (op matematicos)
					//TODO: separar tokens
					if(linea[i] == '+' || linea[i] == '-' || linea[i] == '*') {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_suma", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador dos puntos
					if(linea[i] == ':' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_dos_puntos", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador punto
					if(linea[i] == '.' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_punto", ""+linea[i]);
						this.addToken(token);
						//es el ultimo caracter
						continue;
					}
					//operador llave abierta
					if(linea[i] == '{' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_llave_abierta", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador llave cerrada
					if(linea[i] == '}' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_llave_cerrada", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador corchete abierto
					if(linea[i] == '[' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_corchete_abierto", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador corchete cerrado
					if(linea[i] == ']' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_corchete_cerrado", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador parentesis abierto
					if(linea[i] == '(' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_parentesis_abierto", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador parentesis cerrado
					if(linea[i] == ')' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_parentesis_cerrado", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador punto coma
					if(linea[i] == ';' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_punto_coma", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					//operador coma
					if(linea[i] == ',' ) {
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						Token token = new Token(fila, i , "op_coma", ""+linea[i]);
						this.addToken(token);
						continue;
					}
					
					//operador igual
					//TODO: separar tokens
					if(linea[i] == '!' || linea[i] == '=' ) {
						//si habia algun token se guarda
						if(!curToken.equals("") && !state.equals("op_unit") && !state.equals("op_comparador")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						if(state.equals("op_unit")) {
							if(linea[i] == '!')	{
								//throw error se esperaba un simbolo =
								throw new ExcepcionLexica(fila, i, "se esperaba un simbolo =", ""+linea[i]);
							//op igual
							}else {
								Token token = new Token(fila, i-1 , "op_igual", curToken+linea[i]);
								this.addToken(token);
								curToken = "";
								state = "";
								continue;
							}
							
						}
						if(state.equals("op_comparador")) {
							if(linea[i] != '=')	{
								//throw error se esperaba un simbolo = < >
								throw new ExcepcionLexica(fila, i, "se esperaba un simbolo =", ""+linea[i]);
							//op comparador
							}else {
								Token token = new Token(fila, i-1 , "op_comparador", curToken+linea[i]);
								this.addToken(token);
								curToken = "";
								state = "";
								continue;
							}
							
						}
						//operador unitario
						if(curToken.equals("")) {
							state = "op_unit";
							curToken += linea[i];
						}
						continue;
					}
					//operador comparacion
					if(linea[i] == '<' || linea[i] == '>' ) {
						if(!curToken.equals("") && state.equals("op_comparador")) {
							throw new ExcepcionLexica(fila, i, "se esperaba un simbolo =", ""+linea[i]);
						}
						//si habia algun token se guarda
						if(!curToken.equals("")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						//operador comparacion
						if(curToken.equals("")) {
							state = "op_comparador";
							curToken += linea[i];
						}

						continue;
					}
					//operador and
					if(linea[i] == '&' ) {
                                            
						if(i+1 >= linea.length) {
							throw new ExcepcionLexica(fila, i, "se esperaba un simbolo &", "fin de linea");
						}
						if((""+linea[i]+linea[i+1]).equals("&&")) {
							//si habia algun token se guarda
							if(!curToken.equals("")) {
								// guarda lo que tenia
								Token token = new Token(fila, i - curToken.length() +1, state, curToken);
								this.addToken(token);
								curToken = "";
								state = "";
							}
							//operador and
							Token token = new Token(fila, i - curToken.length() +1, "op_and" , "&&");
							this.addToken(token);
							i++;
						}else {
							//throw error se esperaba un simbolo &
							throw new ExcepcionLexica(fila, i, "se esperaba un simbolo &", ""+linea[i+1]);
						}

						continue;
					}
					//operador or
					if(linea[i] == '|' ) {
						if(i+1 >= linea.length) {
							throw new ExcepcionLexica(fila, i, "se esperaba un simbolo |", "fin de linea");
						}
						if((""+linea[i]+linea[i+1]).equals("||")) {
							//si habia algun token se guarda
							if(!curToken.equals("")) {
								// guarda lo que tenia
								Token token = new Token(fila, i - curToken.length() +1, state, curToken);
								this.addToken(token);
								curToken = "";
								state = "";
							}
							//operador or
							Token token = new Token(fila, i - curToken.length() +1, "op_or" , "||");
							this.addToken(token);
							i++;
						}else {
							//throw error se esperaba un simbolo |
							throw new ExcepcionLexica(fila, i, "se esperaba un simbolo |", ""+linea[i+1]);
						}
						continue;
					}
					
					//literal entero
					if((int)linea[i] > 47 && (int)linea[i] < 58 ) {
						
						//si habia algun token se guarda
						if(!curToken.equals("") && (!state.equals("lit_cad") || !state.equals("lit_car") || !state.equals("id_clase") || !state.equals("id_objeto"))) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}//literal entero
						curToken += linea[i];
						state = "lit_ent";
						continue;
					}
					//lit cadena
					if(linea[i] == '"') {
						//si habia algun token se guarda
						if(!curToken.equals("") && !state.equals("lit_cad")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						//lit cadena
						curToken += linea[i];
						state = "lit_cad";
						continue;
					}
					//lit caracter
					if(linea[i] == '\'') {
						//si habia algun token se guarda
						if(!curToken.equals("") && !state.equals("lit_cad")) {
							// guarda lo que tenia
							Token token = new Token(fila, i - curToken.length() +1, state, curToken);
							this.addToken(token);
							curToken = "";
							state = "";
						}
						if( linea.length-i < 3) {
							throw new ExcepcionLexica(fila, i, "declaracion de caracter", "fin de linea");
						}
						
						//lit caracter
						// TODO: Invalid escape sequence (valid ones are  \b  \t  \n  \f  \r  \"  \'  \\ )
						if((""+linea[i]+linea[i+1]).equals("'\\") && linea[i+3]=='\'') {
							Token token = new Token(fila, i , "int_car" , ""+ linea[i]+ linea[i+1]+ linea[i+2]+ linea[i+3]);
							this.addToken(token);
							i += 3;
							
						}else {
							if( linea[i+2] == '\'') {
								Token token = new Token(fila, i , "int_car" , ""+ linea[i]+ linea[i+1]+ linea[i+2] );
								this.addToken(token);
								i += 2;
							}else {
								//throw error se esperaba un simbolo '
								throw new ExcepcionLexica(fila, i, "se esperaba un simbolo '", ""+linea[i]);
							}
							
						}
						continue;
					}
					//id_clase
					if(linea[i] < 91 && linea[i] > 64 && curToken.equals("")) {
						curToken += linea[i];
						state = "id_clase";
						continue;
					}
					//id objeto
					if(linea[i] < 123 && linea[i] > 96 && curToken.equals("")) {
						curToken += linea[i];
						state = "id_objeto";
						continue;
					}
					if(((linea[i] < 91 && linea[i] > 64) || (linea[i] < 123 && linea[i] > 96) || linea[i] == '_') && !curToken.equals("")) {
						curToken += linea[i];
						if( state.equals("lit_ent")){
							//throw error empieza con numero
							throw new ExcepcionLexica(fila, i, "el identificador comienza con digito", curToken);
						}
						if((curToken.equals("class") 	|| 
							curToken.equals("else") 	||
							curToken.equals("false")	||
							curToken.equals("if") 		||
							curToken.equals("let") 		||
							curToken.equals("while")	||
							curToken.equals("not") 		||
							curToken.equals("true") 	||
							curToken.equals("new") 		||
							curToken.equals("nil")	) 	&& 
							(i+1 < linea.length && !((linea[i+1] < 91 && linea[i+1] > 64) || (linea[i+1] < 123 && linea[i+1] > 96) || linea[i+1] == '_'))){
								Token token = new Token(fila, i - curToken.length() +1, "p_"+curToken, curToken);
								this.addToken(token);
								curToken = "";
								state = "";
							}
							
					}
					
					
				}
			}
		}
		//TODO: verificar comillas dobles
		return 0;
	}
	
	/**
	 * Clase que define al Token lexico
	 * @author Gaston Cavallo
	 * @author Mariel Volman
	 **
	 */
	class Token {
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
	
	/**
	 * Clase encargada de definir el error en el Analizador Sintactico
	 * @author Gaston Cavallo
	 * @author Mariel Volman
	 *
	 */
	class ExcepcionLexica extends Exception {

		private static final long serialVersionUID = 839292488121117913L;
		public String mensaje;
		
		/**
		 * Constructor de la clase ExcepcionLexica
		 * @param fila Numero de fila en la que se encuentra el error
		 * @param col Numero de columna en la que se encuentra el error
		 * @param expectativa Descripcion del error, por lo general, que se esperaba encontrar
		 * @param valor Valor actual del token
		 */
		public ExcepcionLexica (int fila, int col, String expectativa, String valor) {
			this.mensaje = "ERROR: LEXICO | LINEA: "+fila+" | COLUMNA: "+ col +" | DESCRIPCION: "+expectativa+" , se encontro: "+valor +" |";
		}
		
	}
        
        class LectorArchivo {
            File archivo ; 
            FileReader fr = null;
            BufferedReader br;
            
            public LectorArchivo(String ruta){
                this.archivo = new File (ruta);
                try {
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
            }
            
            public String getLine(){
                try{
                    return this.br.readLine();
                }catch(Exception e) {
			e.printStackTrace();
                        return null;
		}
            }
        }
}
