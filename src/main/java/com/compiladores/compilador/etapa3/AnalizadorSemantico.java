/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compiladores.compilador.etapa3;

import com.compiladores.compilador.etapa2.AnalizadorSintactico;
import com.compiladores.compilador.etapa2.ExcepcionSintactica;
import com.compiladores.compilador.etapa4.ArbolSintacticoAbstracto;
import com.compiladores.compilador.etapa4.NodoAsignacion;
import com.compiladores.compilador.etapa4.NodoClase;
import com.compiladores.compilador.etapa4.NodoMetodo;
import com.compiladores.compilador.etapa4.NodoSentencia;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Gaston Cavallo
 */
public class AnalizadorSemantico {
    
    private TablaDeSimbolos ts;
    private ArbolSintacticoAbstracto ast;
    
    public AnalizadorSemantico(AnalizadorSintactico aS) {
        this.ts = aS.getTs();
        this.ast = aS.getAst();
        
        boolean exito = true;
        try{
            this.verificaHerenciaCircular();
            this.consolidarTS();
            this.consolidarAST();
        }catch(ExcepcionSintactica | ExcepcionSemantica eS){
            System.out.println(eS.toString());
            exito = false;
        }catch(Exception e){
            System.out.println("Excepcion no capturada ");
            e.printStackTrace();
        }
        if(exito){ 
            System.out.println("CORRECTO: ANALISIS SEMANTICO - DECLARACIONES");
            System.out.println("CORRECTO: ANALISIS SEMANTICO - SENTENCIAS");
        }
    }
    
    public void verificaHerenciaCircular() throws ExcepcionSemantica{
        Queue<String> verif = new LinkedList<>();        
        HashMap<String, String> finalizados = new HashMap<>();

        verif.add("Object");
        while(finalizados.size()<this.ts.getClases().size()){
            String h = verif.remove();
            for(Map.Entry<String, EntradaClase> entry : this.ts.getClases().entrySet()) {
                String key = entry.getKey();
                EntradaClase c = entry.getValue();
                if(!key.equals(h) && c.getHerencia()!=null && c.getHerencia().equals(h)){
                    verif.add(key);
                    if(finalizados.containsKey(key)){
                        throw new ExcepcionSemantica(c.getFila(),c.getColumna(),"La clase contiene herencia circular",c.getNombre(),true);
                    }
                }
            }
            finalizados.put(h, h);
            if(verif.isEmpty() && finalizados.size()<this.ts.getClases().size()){
                for(Map.Entry<String, EntradaClase> entry : this.ts.getClases().entrySet()) {
                    String key = entry.getKey();
                    
                    if(!finalizados.containsKey(key)){
                        verif.add(key);
                    }
                }
            }
        }
        
    }
    
    public boolean existeTipo(String tipo){
        return this.ts.getClases().get(tipo)!=null;
    }
    
    public void consolidarTS() throws ExcepcionSemantica{
        
        for(Map.Entry<String, EntradaClase> entry : this.ts.getClases().entrySet()) {
            String key = entry.getKey();
            EntradaClase value = entry.getValue();
            if(key.equals("Object")){
                value.setConsolidado(true);
            }
            if(!value.isConsolidado() && !key.equals("Object")){
                this.consolidarClase(key);
            }
        }
    }
    
    public void consolidarClase(String her) throws ExcepcionSemantica{
        EntradaClase eC = this.ts.getClases().get(her);
        if(!eC.isConsolidado() && !eC.getHerencia().equals("Object")){
            this.consolidarClase(eC.getHerencia());
        }
        EntradaClase padre = this.ts.getClases().get(eC.getHerencia()); //TODO verificar metodos y variables
        if(this.existeTipo(eC.getHerencia())){
            if(!eC.getHerencia().equals("Int") && !eC.getHerencia().equals("Bool") && !eC.getHerencia().equals("Char") && !eC.getHerencia().equals("String")){
                for(Map.Entry<String, EntradaConstante> ctes : eC.getConstantes().entrySet()) {
                    EntradaConstante cte = ctes.getValue();
                    if(!this.existeTipo(cte.getTipo())){
                        throw new ExcepcionSemantica(cte.getFila(),cte.getColumna(),"No existe el tipo de declaracion",cte.getTipo(),true);
                    }
                    eC.incTamaño(this.ts.getClases().get(cte.getTipo()).getTamaño());
                    cte.setTamaño(eC.getTamaño());
                }
                for(Map.Entry<String, EntradaVar> vars : eC.getVariablesInst().entrySet()) {
                    EntradaVar varble = vars.getValue();
                    if(!this.existeTipo(varble.getTipo())){
                        throw new ExcepcionSemantica(varble.getFila(),varble.getColumna(),"No existe el tipo de declaracion",varble.getTipo(),true);
                    }
                    eC.incTamaño(this.ts.getClases().get(varble.getTipo()).getTamaño());
                    varble.setTamaño(eC.getTamaño());
                }
                for(Map.Entry<String, EntradaMetodo> mets : eC.getMetodos().entrySet()) {
                    EntradaMetodo met = mets.getValue();
                    if(!this.existeTipo(met.getTipoRetorno()) && !met.getTipoRetorno().equals("void")){
                        throw new ExcepcionSemantica(met.getFila(),met.getColumna(),"No existe el tipo de declaracion",met.getTipoRetorno(),true);
                    }
                    for (int i = 0; i < met.getParametros().size(); i++) {
                        EntradaParametro par = met.getParametros().get(i);
                        if(!this.existeTipo(par.getTipo())){
                            throw new ExcepcionSemantica(par.getFila(),par.getColumna(),"No existe el tipo de declaracion",par.getTipo(),true);
                        }
                    }
                    for(Map.Entry<String, EntradaConstante> ctesMet : met.getConstanteMet().entrySet()) {
                        EntradaConstante cteM = ctesMet.getValue();
                        if(!this.existeTipo(cteM.getTipo())){
                            throw new ExcepcionSemantica(cteM.getFila(),cteM.getColumna(),"No existe el tipo de declaracion",cteM.getTipo(),true);
                        }
                    }
                    for(Map.Entry<String, EntradaVar> varsMet : met.getVariablesMet().entrySet()) {
                        EntradaVar varM = varsMet.getValue();
                        if(!this.existeTipo(varM.getTipo())){
                            throw new ExcepcionSemantica(varM.getFila(),varM.getColumna(),"No existe el tipo de declaracion",varM.getTipo(),true);
                        }
                    }
                }  
            }else{
                throw new ExcepcionSemantica(eC.getFila(),eC.getColumna(),"La clase no puede heredar de una clase base",eC.getHerencia(),true);
            }
        }else{
            throw new ExcepcionSemantica(eC.getFila(),eC.getColumna(),"Clase de Herencia mal declarada",eC.getHerencia(),true);
        }
        for(Map.Entry<String, EntradaMetodo> metH : padre.getMetodos().entrySet()) {
            EntradaMetodo meth = metH.getValue();
            EntradaMetodo auxM = eC.getMetodos().get(metH.getKey());
            if(auxM!=null){
                for (int i = 0; i < meth.getParametros().size(); i++) {
                    if(!auxM.existPar(meth.getParametros().get(i).getNombre())){
                        throw new ExcepcionSemantica(auxM.getFila(),auxM.getColumna(),"Los atributos de un metodo heredado deben tener el mismo nombre",auxM.getNombre(),true);
                    }
                }
                eC.moveVtable(metH.getKey(), padre.getPosicion(metH.getKey()));
            }else{
                eC.insertaMetodo(metH.getKey(), meth, padre.getNombre(),padre.getPosicion(metH.getKey()));
            }
        }
        
        for(Map.Entry<String, EntradaVar> varsH : padre.getVariablesInst().entrySet()) {
            EntradaVar varsh = varsH.getValue();
            EntradaVar auxV = eC.getVariablesInst().get(varsH.getKey());
            if(auxV!=null){
                if(!auxV.getTipo().equals(varsh.getTipo()) && !varsH.getValue().isIsPrivate()){
                    throw new ExcepcionSemantica(auxV.getFila(),auxV.getColumna(),"El tipo de la variable debe ser el mismo que el de su superclase",auxV.getTipo(),true);
                }
            }else{
                if(!varsH.getValue().isIsPrivate()) eC.insertaVariable(varsH.getKey(), varsh);
            }
        }
        for(Map.Entry<String, EntradaConstante> ctesPadre : padre.getConstantes().entrySet()) {
            EntradaConstante cte = ctesPadre.getValue();
            String key = ctesPadre.getKey();
            if(!eC.getConstantes().containsKey(key)){
                eC.insertaConstante(key, cte);
            }
        }
        eC.enumeraMetodos();
        eC.setConsolidado(true);
    }
    
    public void consolidarAST() throws ExcepcionSemantica,ExcepcionSintactica{
        for(Map.Entry<String, NodoClase> entry : this.ast.getClases().entrySet()) {
            NodoClase value = entry.getValue();
            if(!value.getMetodos().isEmpty()){
                for(Map.Entry<String, NodoMetodo> meto : value.getMetodos().entrySet()){
                    NodoMetodo metV = meto.getValue();
                    EntradaMetodo eC = ts.getClases().get(metV.getPadre()).getMetodo(metV.getNombre());

                    if(metV.getRetorno()!=null && !metV.getRetorno().getTipo(ts).equals(eC.getTipoRetorno()) && !metV.getRetorno().getTipo(ts).equals("nil")){
                        throw new ExcepcionSemantica(metV.getRetorno().getFila(),metV.getRetorno().getCol(),"El tipo de retorno no coincide con el declarado",metV.getRetorno().getTipo(ts),false);
                    }else{
                        if(metV.getRetorno()==null && !eC.getTipoRetorno().equals("void")){
                            throw new ExcepcionSemantica(metV.getFila(),metV.getCol(),"No se encontro expresion de retorno","null",true);
    
                        }
                    }
                    if(!metV.getBloque().isEmpty()){
                        for (int j = 0; j < metV.getBloque().size(); j++) {
                            metV.getBloque().get(j).verifica(ts);
                        }
                    }
                }
            }
            
            if(!value.getConstantes().isEmpty()){
                for(Map.Entry<String, NodoSentencia> cte : value.getConstantes().entrySet()) {
                    NodoAsignacion cteV = (NodoAsignacion) cte.getValue();
                    cteV.verifica(ts);
                }

            }
            
            if(value.getConstructor() != null){
                value.getConstructor().verifica(ts);
            }
        }
    }
    
}
