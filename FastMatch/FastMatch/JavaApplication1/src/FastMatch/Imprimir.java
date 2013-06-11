package FastMatch;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Imprimir {

	static public void imprimeHashC(ConcurrentHashMap<String, Objeto> Hash){
            Enumeration<String> en=Hash.keys();
            Objeto objeto;
            while(en.hasMoreElements()){
                String key=en.nextElement();
                objeto = Hash.get(key);
                System.out.println(objeto.getClasse() + " - " + objeto.getChave());
                objeto.imprimeAtributos("chave");
                objeto.imprimeEquivalencia();
                System.out.println();
            }
	}	
	static public void imprimeHashA(ConcurrentHashMap<String, Atributo> Hash){
            Enumeration<String> en=Hash.keys();
            Atributo atributo;
            while(en.hasMoreElements()){
                String key=en.nextElement();
                atributo = Hash.get(key);
                System.out.println(atributo.getNome() + " - " + atributo.getChave());
            }
	}
        
	static public void imprimePS(List<Objeto> Lista){
            Objeto objeto;
            int tam = Lista.size();
            for(int i = 0; i < tam; i++){
                objeto = Lista.get(i);
                System.out.println("");
                System.out.print(objeto.getClasse());
                objeto.imprimeAtributos("valor");
           }
	}
	
        static public void imprimeRelacaoA(ArrayList<StringDupla> matriz, ConcurrentHashMap<String, Atributo> hash){
            System.out.println();
            for(int i = 0 ; i < matriz.size(); i++){
                StringDupla elemento = matriz.get(i);
                String nome1 = hash.get(elemento.getStr1()).getNome();
                String nome2 = hash.get(elemento.getStr2()).getNome();
                System.out.println(nome1 + " - " + nome2);
            }
	}
        
        static public void imprimeRelacaoC(ArrayList<StringDupla> matriz, ConcurrentHashMap<String, Objeto> hash){
            System.out.println();
            for(int i = 0 ; i < matriz.size(); i++){
                StringDupla elemento = matriz.get(i);
                String nome1 = hash.get(elemento.getStr1()).getClasse();
                String nome2 = hash.get(elemento.getStr2()).getClasse();
                System.out.println(nome1 + " - " + nome2);
            }
	}
        
        static public void imprimeFrequencias(Lista frequencias){
            System.out.println();
            while(frequencias != null){
                System.out.println(frequencias.retornaStr()+" -- "+frequencias.retornaN());
                frequencias = frequencias.retornaProx();
            }
        }
        
        static public void imprimeHashH(ConcurrentHashMap<String, Lista> hashH){
            Enumeration<String> en=hashH.keys();
            Lista lista;
            System.out.println();
            while(en.hasMoreElements()){
                String key=en.nextElement();
                lista = hashH.get(key);
                System.out.println(key);
                System.out.print(lista.retornaN());
                while(lista != null){
                    System.out.print(" --> "+lista.retornaStr());
                    lista = lista.retornaProx();
                }
                System.out.println();
            }
            System.out.println("\n");
        }
        
        static public void imprimeLista(ArrayList<String> lista){
            int n = lista.size();
            for(int i = 0; i < n; i++){
                System.out.println(lista.get(i));
            }
        }
}
