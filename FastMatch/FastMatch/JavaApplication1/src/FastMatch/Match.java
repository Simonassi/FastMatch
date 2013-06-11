package FastMatch;


import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Match {
	
    static int verificaObjeto(String classe, ConcurrentHashMap<String, Objeto> Hash){ // Verifica se a classe exite na hash e retorna sua chave
        Objeto aux = Hash.get(classe);
        if(aux != null){ // caso a classe ja exista
             return aux.getChave();
        }else{
            return 0;
        }
    }

    static int verificaAtributo(String atributo, ConcurrentHashMap<String, Atributo> Hash){ // Verifica se a classe exite na hash e retorna sua chave
        Atributo aux = Hash.get(atributo);
        if(aux != null){ // caso a classe ja exista
            return aux.getChave();
        }else{
            return 0;
        }
    }

    static void verificaMatch( List<Objeto> Lista, Objeto evento, String tipo){
        Objeto aux;
        int chave = evento.getChave();
        int tam = Lista.size();
        for(int i = 0; i < tam; i++){
            aux = Lista.get(i);
            if(chave%aux.getChave() == 0){ // Mesma categoria ou subcategoria
                verificaAtributos(aux,evento,tipo);
            }
        }
    }	

    static void verificaAtributos(Objeto ev1, Objeto ev2, String tipo){

        ConcurrentHashMap<String, Atributo> h1 = ev1.getHash(); 
        ConcurrentHashMap<String, Atributo> h2 = ev2.getHash();

        Enumeration<String> en1 = h1.keys();
        Enumeration<String> en2 = h2.keys();

        Atributo a1;
        Atributo a2;

        boolean achou = true;
        int n_atributos = h2.size(); 

        if("S".equals(tipo)){ // ev1 = publicacao; ev2 = subscricao
            while(en2.hasMoreElements() & achou){
                a2 = h2.get(en2.nextElement());
                achou = false;
                while(en1.hasMoreElements()){
                    a1 = h1.get(en1.nextElement());
                    if(a2.getChave()%a1.getChave() == 0 & a1.getValor().equals(a2.getValor())){
                        achou = true;
                        n_atributos--;
                        break;
                    }
                }	
            }
            if(n_atributos == 0){
                System.out.println("");
                System.out.print(ev1.getClasse());
                ev1.imprimeAtributos("valor");
            }
        }

        else if("P".equals(tipo)){ // ev1 = subscricao; ev2 = publicacao
             while(en1.hasMoreElements() & achou){
                a1 = h1.get(en1.nextElement());
                achou = false;
                while(en2.hasMoreElements()){
                    a2 = h2.get(en2.nextElement());
                    if(a1.getChave()%a2.getChave() == 0 & a2.getValor().equals(a1.getValor())){
                        achou = true;
                        n_atributos--;
                        break;
                    }
                }	
            }
            if(n_atributos == 0){
                System.out.println("");
                System.out.print(ev2.getClasse());
                ev2.imprimeAtributos("valor");
            }
        }
    }
}
