/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FastMatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author Rafael
 */
public class FastMatch {
    
    public static ArrayList<String> encontraEspecializacoes(ConcurrentHashMap<String, Objeto> hashC, String str){
        int chave = 0;
        String key = "";
        Enumeration<String> en=hashC.keys();
        boolean achou = false;
        ArrayList<String> especializacoes = new ArrayList <String>();
        
        while(en.hasMoreElements()){ // percorre a lista de herancas
            key=en.nextElement(); // nome da classe
            if(key.compareTo(str) == 0){ // se achou a classe procurada
                chave = hashC.get(key).getChave(); // pega a chave (numero primo) da classe
                achou = true;
                break;
            }
        } 
        
        if(achou){
           //System.out.println()
           en=hashC.keys();
           while(en.hasMoreElements()){ // percorre a lista de herancas
                String keyD=en.nextElement(); // nome da classe
                int chaveD = hashC.get(keyD).getChave(); // pega a chave (numero primo) da classe
                if(chaveD%chave == 0){ // achou um descendente
                    especializacoes.add(keyD); // adiciona a lista
                }
            }
        }else{
            especializacoes.add(str); // se nao achou a classe, retorna uma lista com um unico elemento, a propria classe
        }
        
        return especializacoes;
    }
    
    public static ArrayList<String> encontraEquivalencias(ConcurrentHashMap<String, Atributo> hashA, String str){
        int chave = 0;
        String key = "";
        Enumeration<String> en=hashA.keys();
        boolean achou = false;
        ArrayList<String> especializacoes = new ArrayList <String>();
        
        while(en.hasMoreElements()){ // percorre a lista de herancas
            key=en.nextElement(); // nome da classe
            if(key.compareTo(str) == 0){ // se achou a classe procurada
                chave = hashA.get(key).getChave(); // pega a chave (numero primo) da classe
                achou = true;
                break;
            }
        } 
        
        if(achou){
           //System.out.println()
           en=hashA.keys();
           while(en.hasMoreElements()){ // percorre a lista de herancas
                String keyD=en.nextElement(); // nome do
                int chaveD = hashA.get(keyD).getChave(); // pega a chave (numero primo) da classe
                if(chaveD%chave == 0){ // achou um descendente
                    especializacoes.add(keyD); // adiciona a lista
                }
            }
        }else{
            especializacoes.add(str); // se nao achou a classe, retorna uma lista com um unico elemento, a propria classe
        }
        
        return especializacoes;
    }
    
    public static void main (String args[]) throws NumberFormatException, IOException{
        ConcurrentHashMap<String, Atributo> HashA = (ConcurrentHashMap<String, Atributo>)Serializa.deserializa("hashA.ser"); 
        ConcurrentHashMap<String, Objeto> HashC = (ConcurrentHashMap<String, Objeto>)Serializa.deserializa("hashC.ser"); 
        Imprimir.imprimeHashA(HashA);
        Imprimir.imprimeHashC(HashC);
        //ArrayList<String> especializacoes = FastMatch.encontraEspecializacoes(HashC, "Estudante");
        //Imprimir.imprimeLista(especializacoes);
        
        ArrayList<String> equivalencias = FastMatch.encontraEquivalencias(HashA, "valor");
        Imprimir.imprimeLista(equivalencias);
    }
    
}
