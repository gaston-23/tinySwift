/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa5;

import com.compiladores.compilador.etapa3.EntradaClase;
import com.compiladores.compilador.etapa3.EntradaConstante;
import com.compiladores.compilador.etapa3.EntradaMetodo;
import com.compiladores.compilador.etapa3.EntradaVar;
import com.compiladores.compilador.etapa3.ExcepcionSemantica;
import com.compiladores.compilador.etapa3.TablaDeSimbolos;
import com.compiladores.compilador.etapa4.ArbolSintacticoAbstracto;
import com.compiladores.compilador.etapa4.NodoAsignacion;
import com.compiladores.compilador.etapa4.NodoClase;
import com.compiladores.compilador.etapa4.NodoMetodo;
import com.compiladores.compilador.etapa4.NodoSentencia;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gx23
 */
public class GeneradorCodigo {
    private TablaDeSimbolos ts;
    private ArbolSintacticoAbstracto ast;
    private String codigo;
    private HashMap<String,String> data;
    private HashMap<String,String> text;
    
    
    public GeneradorCodigo(TablaDeSimbolos ts, ArbolSintacticoAbstracto ast){
        this.ts = ts;
        this.ast= ast;
        this.text = new HashMap<>();
        this.data = new HashMap<>();
        try{
            this.generarData();
            this.generaMetodosBasicos();
            this.generarText();
            
            this.codigo = this.generar();
        }catch(ExcepcionSemantica es){
            System.out.println(es.mensaje);
        }
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    private void generarData(){
        
        
        for(Map.Entry<String, EntradaClase> clases : ts.getClases().entrySet()) {
            EntradaClase eC = clases.getValue();
            int cantEsp =0;
            
            String vt = "\n";
            // for(Map.Entry<String, EntradaMetodo> mets : eC.getMetodos().entrySet()) {
            //     EntradaMetodo met = mets.getValue();
            //     String metK = mets.getKey();
            //     vt += "\t.word "+eC.getNombre()+"_"+metK+"\n";
                
            // }  
            for (String met : eC.getVtable()) {
                vt += met+"\n";
            }
//            if(!vt.equals("\n")){
                this.data.put(eC.getNombre()+"_vtable:",vt);
                String cir ="\n\t.word "+eC.getNombre()+"_vtable" ;
                this.data.put("cir_"+eC.getNombre()+":", cir);
//            }
            
            for(Map.Entry<String, EntradaConstante> ctes : eC.getConstantes().entrySet()) {
                EntradaConstante cte = ctes.getValue();
                String key = "cte_"+ctes.getKey()+"_"+eC.getNombre()+": ";
                String dat = cte.generaCodigo();
                cantEsp += cte.getTipo().equals("String") ? 32 : 4;
                this.data.put(key, dat);
            }
            for(Map.Entry<String, EntradaVar> vars : eC.getVariablesInst().entrySet()) {
                EntradaVar varble = vars.getValue();
                String key = "var_"+vars.getKey()+"_"+eC.getNombre()+": ";
                String dat = varble.generaCodigo();
                cantEsp += varble.getTipo().equals("String") ? 32 : 4;
                this.data.put(key, dat);
            }
            this.data.put(eC.getNombre()+"_temp:",".word "+cantEsp);
        }
        
    }

    private void generarText() throws ExcepcionSemantica{
        
        for(Map.Entry<String, NodoClase> entry : this.ast.getClases().entrySet()) {
            NodoClase value = entry.getValue();
            String asm = "";
            this.ts.setClaseActual(value.getNombre());
            if(!value.getConstantes().isEmpty()){
                
                for(Map.Entry<String, NodoSentencia> cte : value.getConstantes().entrySet()) {
                    NodoAsignacion cteV = (NodoAsignacion) cte.getValue();
                    String key = "cte_"+cte.getKey()+"_"+value.getNombre();
                    String tipoDer = cteV.getDer().getTipo(ts);
                    if(cteV.getDer().getNombre().equals("literal")){
                        asm += "\tli $t1, "+cteV.getDer().getValor()+"\n";
                    }
                    
                    asm += "\tsw $t1, "+key+"\n";
                }
                
            }
            
            asm +="""
                    \tmove $fp, $sp #mueve el contenido de $sp a $fp
                    \tsw $ra, 0($sp) #copia el contenido de $ra a $sp (direccion de retorno)
                    \taddiu $sp, $sp, -4 #mueve el $sp 1 pos arriba
                    """
                    +"\tla $t0, "+this.ts.getClaseActual().getNombre()+"_vtable\n\tsw $t0, 0($a0)\n";
            if(value.getConstructor() != null){
                this.ts.setMetodoActual("constructor");
                
                asm += value.getConstructor().getCodigo(this.ts) ;
                int pars = this.ts.getMetodoActual().getTamañoParam()+this.ts.getMetodoActual().getTamañoAtribs()+8;
                        
                asm += """
                        \tlw $ra, 4($sp) #ponemos el tope de la pila en $ra
                        """;
                asm += "\taddiu $sp, $sp, "+pars+"\n";
                asm += """
                        \tlw $fp 0($sp) #movemos el puntero 1 palabra hacia abajo
                        \tjr $ra #retornamos a la funcion que lo llamo
                        """;
            }
            asm += "\tjr $ra #retornamos a la funcion que lo llamo";
            this.text.put(entry.getKey()+"_constructor", asm);
            
            if(!value.getMetodos().isEmpty()){
                for(Map.Entry<String, NodoMetodo> meto : value.getMetodos().entrySet()){
                    this.ts.setMetodoActual(meto.getKey());
                    NodoMetodo metV = meto.getValue();
                    String cod = """
                                \tmove $fp, $sp #mueve el contenido de $sp a $fp
                                \tsw $ra, 0($sp) #copia el contenido de $ra a $sp (direccion de retorno)
                                \taddiu $sp, $sp, -4 #mueve el $sp 1 pos arriba
                                """+metV.getCodigo(this.ts);
                    if(value.getNombre().equals("Main")&& metV.getNombre().equals("main")){
                        cod += """
                               \tli $v0, 10 #cargamos el codigo de finalizacion
                               \tsyscall #llamada a sistema
                               """;
                    }else{
                        int pars = this.ts.getMetodoActual().getTamañoParam()+this.ts.getMetodoActual().getTamañoAtribs()+8;
                        cod += "\tlw $ra, 4($sp) #ponemos el tope de la pila en $ra\n";
                        cod += "\taddiu $sp, $sp, "+pars+"\n";
                        cod += """
                               \tlw $fp 0($sp) #movemos el puntero 1 palabra hacia abajo
                               \tjr $ra #retornamos a la funcion que lo llamo
                               """;
                    }
                    
                    this.text.put(entry.getKey()+"_"+meto.getKey(),cod );
                }
            }
            
            
        }
    }
    
    private void generaMetodosBasicos(){
        // IO
        String finMetIn = "\tlw $ra, 4($sp) #ponemos el tope de la pila en $ra\n"
                      + "\tadd $sp, $sp 8\n"
                      + "\tlw  $fp, 0($sp)\n"
                      + "\tjr $ra\n";
        String finMetOut = "\tlw $ra, 4($sp) #ponemos el tope de la pila en $ra\n"
                      + "\tadd $sp, $sp 12\n"
                      + "\tlw  $fp, 0($sp)\n"
                      + "\tjr $ra\n";
        String iniMet = """
                        \tmove $fp, $sp #mueve el contenido de $sp a $fp
                        \tsw $ra, 0($sp) #copia el contenido de $ra a $sp (direccion de retorno)
                        \taddiu $sp, $sp, -4 #mueve el $sp 1 pos arriba
                        """;
        /**
         * IO_out_string
         * imprime la cadena ingresado en la direccion de foo_cadena y retorna a la direccion definida en $ra
         * //TODO poner en la llamada el valor t o f y llamar a print string
         */
        this.text.put("IO_out_string", 
                iniMet+ """
                        \tlw $a0, 8($sp)
                        \tli $v0, 4 
                        \tsyscall 
                        """+finMetOut); //TODO cargar parametro en el tope de la pila
        /**
         * IO_out_int
         * imprime el numero ingresado en la direccion de foo_cadena y retorna a la direccion definida en $ra
         * //TODO poner en la llamada el valor t o f y llamar a print string
         */
        this.text.put("IO_out_int", 
                iniMet+ """
                        \tlw $a0, 8($sp)
                        \tli $v0, 1 
                        \tsyscall 
                        """+finMetOut); //TODO cargar parametro en el tope de la pila
        /**
         * IO_out_bool
         * imprime el caracter ingresado en la direccion de foo_cadena y retorna a la direccion definida en $ra
         * //TODO poner en la llamada el valor t o f y llamar a print string
         */
        this.text.put("IO_out_bool", 
                iniMet+ """
                        \tlw $a0, 8($sp)
                        \tli $v0, 1 
                        \tsyscall 
                        """+finMetOut); //TODO cargar parametro en el tope de la pila
        /**
         * IO_out_char
         * imprime el caracter ingresado en la direccion de foo_cadena y retorna a la direccion definida en $ra
         */
        this.text.put("IO_out_char",
                iniMet+ """
                        \tlw $a0, 8($sp)
                        \tli $v0, 11
                        \tsyscall 
                        """+finMetOut); //TODO cargar parametro en el tope de la pila
        /**
         * IO_in_string
         * devuelve el valor ingresado en la direccion del registro guardado en $a0 y retorna a la direccion definida en $ra
         * TODO ver si carga el a0 para guardar el valor
         */
        this.text.put("IO_in_string", 
                iniMet+ """
                        \tli $a1, 32
                        \tli $v0, 8
                        \tsyscall 
                        """+finMetIn); //TODO cargar parametro en el tope de la pila
        /**
         * IO_in_int
         * devuelve el valor ingresado en $v0 y retorna a la direccion definida en $ra
         */
        this.text.put("IO_in_int", 
                iniMet+ """
                        \tli $v0, 5
                        \tsyscall 
                        \tmove $t1, $v0
                        """+finMetIn); //TODO cargar parametro en el tope de la pila
        /**
         * IO_in_bool
         * devuelve el valor ingresado en $v0 y retorna a la direccion definida en $ra
         * TODO: manejar de transformar t o f en 1 o 0
         */
        this.text.put("IO_in_bool", 
                iniMet+ """
                        \tli $v0, 5
                        \tsyscall 
                        \tmove $t1, $v0
                        """+finMetIn); //TODO cargar parametro en el tope de la pila
        
        /**
         * IO_in_char
         * devuelve el valor ingresado en $v0 y retorna a la direccion definida en $ra
         */
        this.text.put("IO_in_char", 
                iniMet+ """
                        \tli $v0, 12
                        \tsyscall 
                        \tmove $t1, $v0
                        """+finMetIn); //TODO cargar parametro en el tope de la pila
        //String
        /**
         * String_length
         * devuelve el valor de la longitud de la cadena almacenada en $t0 y lo guarda en $a0 y retorna a la direccion definida en $ra
         */
        this.text.put("String_length", 
                iniMet+ """
                        \tli $t0, 0
                        loop_String_length:
                        \tlb $t1, 0($a0) # load the next character into t1
                        \tbeqz $t1, exit_String_length # check for the null character
                        \taddi $a0, $a0, 1 # increment the string pointer
                        \taddi $t0, $t0, 1 # increment the count
                        \tj loop_String_length # return to the top of the loop
                        exit_String_length:
                        \tla $a0, ($t0)
                        """+finMetIn); //TODO cargar parametro en el tope de la pila
        /**
         * String_concat
         * @input: tomara el valor de la cadena almacenada en $a1
         * TODO: leer la direccion primer parametro
         * devuelve el valor de la longitud de la cadena almacenada en $t0 y lo guarda en $a0 y retorna a la direccion definida en $ra
         */
        this.text.put("String_concat", 
                iniMet+ """
                        \tli $t0, 0 # initialize the count to zero
                        \tli $t2, 0 # initialize the count to zero
                        \tjal String_concat_gotoend
                        \tjal String_concat_loop
                        String_concat_loop:
                        \tlb $t3, 0($a1) # load the next character into t1
                        \tbeqz $t3, String_concat_exit # check for the null character
                        \tsb $t3, 0($a0) # increment the string pointer
                        \taddi $a0, $a0, 1 # increment the string pointer
                        \taddi $a1, $a1, 1 # increment the string pointer
                        \taddi $t2, $t2, 1 # increment the count
                        \tj String_concat_loop # return to the top of the loop
                        String_concat_gotoend:
                        \tlb $t1, 0($a0) # load the next character into t1
                        \tbeqz $t1, exit_String_length # check for the null character
                        \taddi $a0, $a0, 1 # increment the string pointer
                        \taddi $t0, $t0, 1 # increment the count
                        \tj String_concat_gotoend # return to the top of the loop
                        String_concat_exit:
                        """+finMetOut); //TODO cargar parametro en el tope de la pila
        /**
         * String_concat
         * @input: tomara el valor de la cadena almacenada en $a1
         * TODO: leer la direccion primer parametro y la del 2do, ponerlos en 3 y 5 respectivamente
         * devuelve el valor de la longitud de la cadena almacenada en $t0 y lo guarda en $a0 y retorna a la direccion definida en $ra
         */
        this.text.put("String_substr",  
                iniMet+ """
                        \tla $t0, 8($sp)
                        \tla $t5, 12($sp)
                        \tjal String_substr_loop
                        String_substr_loop:
                        \tlb $t1, 0($t0) # load the next character into t1
                        \tbeq $t0,$t5, String_substr_exit # check for the null character
                        \tsb $t1, 0($a0) # increment the string pointer
                        \taddi $a0, $a0, 1 # increment the string pointer
                        \taddi $t0, $t0, 1 # increment the count
                        \tj String_substr_loop # return to the top of the loop
                        String_substr_exit:
                        \tlw $ra, 8($sp) #ponemos el tope de la pila en $ra
                        \tadd $sp, $sp 8
                        \tjr $ra                        
                        """); //TODO cargar parametro en el tope de la pila
    }
    
    private String generar(){
        String asm = "\n.data\n";
        for(Map.Entry<String, String> datAsm : data.entrySet()) {
            asm+=datAsm.getKey() + " "+ datAsm.getValue()+"\n";
        }
        for(Map.Entry<String, String> datLab : ts.getLabels().entrySet()) {
            asm+=datLab.getKey() + " "+ datLab.getValue()+"\n";
        }
        
        asm += "\n.text\n";
        asm+="Main_main: \n"+text.remove("Main_main")+"\n";
        for(Map.Entry<String, String> texAsm : text.entrySet()) {
            asm+=texAsm.getKey() + ": \n"+ texAsm.getValue()+"\n";
        }
        return asm;
    }
    
    
    
}
