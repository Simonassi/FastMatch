package FastMatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CriaHash{

    static int primoC = 1; // chave unica das classes
    static int primoA = 1; // chave unica dos atributos
    static ArrayList<StringDupla> relacaoA = new ArrayList <StringDupla>();
    static ArrayList<StringDupla> relacaoC = new ArrayList <StringDupla>();
    static ConcurrentHashMap<String, Lista> hashH = new ConcurrentHashMap<String, Lista>();

    public static ConcurrentHashMap<String, Atributo> criaHashAtributos(String xmlPathname) throws Exception{
        ConcurrentHashMap<String, Atributo> Hash = new ConcurrentHashMap<String, Atributo>(); // Hash com os atributos criadas
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse( xmlPathname );  
            Element elem = doc.getDocumentElement();
            
            String nome;
            String relacao;
            
            String busca_linha_atributo = "xs:complexType";
            String busca_nome_atributo = "name";
            String busca_linha_relacao = "xs:element";
            String busca_nome_relacao = "type";
            
            NodeList nl = elem.getElementsByTagName(busca_linha_atributo);  
            for( int i=0; i<nl.getLength(); i++ ) {  // percorre todos os atributos encontrados
                Element tagAtributo = (Element) nl.item( i ); 
                nome = tagAtributo.getAttribute(busca_nome_atributo); // pega o nome do atributo
                Hash.put(nome, new Atributo(nome)); // cria o atributo e adiciona na hash de atributos
                
                NodeList nl2 =  tagAtributo.getElementsByTagName(busca_linha_relacao); // pega todas as equivalencias do atributo
                for( int j=0; j<nl2.getLength(); j++ ) { // percorre todas as esquivalencias do atributo
                    Element tagRelacao = (Element)nl2.item(j);
                    relacao = tagRelacao.getAttribute(busca_nome_relacao);
                    CriaHash.relacaoA.add(new StringDupla(nome,relacao)); 
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao criar a lista de atributos: "+e);
        }
        return Hash;
    }
        
    public static void insereHashH(String pai, String filho){
        hashH.put(pai, Lista.insereInicio(hashH.get(pai), new Lista(filho)));
    } 

    public static ConcurrentHashMap<String, Objeto> criaHashClasses(ConcurrentHashMap<String, Atributo> HashA, String xmlPathname) throws Exception{

        ConcurrentHashMap<String, Objeto> Hash = new ConcurrentHashMap<String, Objeto>(); // Hash com as classes criadas	
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            Document doc = db.parse( xmlPathname );  
            Element elem = doc.getDocumentElement();  

            Objeto temp;
            String nome;
            String relacao;
            String heranca;
            String atributo;

            String busca_linha_classe = "xs:complexType";
            String busca_linha_heranca = "xs:extension";
            String busca_linha_simples = "xs:element";
            String busca_linha_atributo = "xs:string";

            String busca_nome_classe = "name";
            String busca_nome_heranca = "base";
            String busca_nome_atributo = "name";
            String busca_nome_simples = "type";
            
            NodeList nl = elem.getElementsByTagName(busca_linha_classe); // pega todos os elementos complexos do XML  
            for( int i=0; i<nl.getLength(); i++ ) {  // percorre todas as classes encontradas
                Element tagClasse = (Element) nl.item( i );  
                nome = tagClasse.getAttribute(busca_nome_classe); // pega o nome da classe
                Hash.put(nome, new Objeto(nome)); // cria o objeto e adiciona na hash de classes
                NodeList nl2 =  tagClasse.getElementsByTagName(busca_linha_heranca); // pega todas as herancas da classe
                for( int j=0; j<nl2.getLength(); j++ ) { // percorre todas a herancas da classe
                    Element tagHeranca = (Element)nl2.item(j);
                    heranca = tagHeranca.getAttribute(busca_nome_heranca);
                    insereHashH(heranca,nome);
                }

                NodeList nl3 = tagClasse.getElementsByTagName(busca_linha_simples);
                for( int k=0; k<nl3.getLength(); k++ ) { // percorre todos os elementos simples da classe (atributos e equivalencias)
                    Element tagSimples = (Element)nl3.item(k);
                    String tipo = tagSimples.getAttribute(busca_nome_simples);
                    if(tipo.equals(busca_linha_atributo)){ // o elemento eh um atributo
                        atributo = tagSimples.getAttribute(busca_nome_atributo);
                        Hash.get(nome).setAtributo(HashA.get(atributo));
                    }else{ // o elemento eh uma equivalencia
                        relacao = tipo;
                        CriaHash.relacaoC.add(new StringDupla(nome,relacao)); 
                        Hash.get(nome).setEquivalencia(relacao);

                        /* NOVO CODIGO */
                        temp = Hash.get(relacao);
                        if(temp != null)
                            temp.setEquivalencia(nome);
                        /* FIM NOVO CODIGO*/
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao criar a lista de classes: "+e);
        }
        return Hash;
    }

    public static void primeiraEtapa(ConcurrentHashMap<String, Objeto> hashC,Lista frequencias){
        // ## Primeia Etapa - Atribuir um numero primo para as classes com heranca, levando em conta quem tem mais herdeiros
        // ## falta verificar as herancas indiretas, por enquanto esta olhando apenas para as diretas ##

        Lista aux = frequencias;
        System.out.println();
        while(aux != null){
            String str = aux.retornaStr();
            int primo = Primos.geraPrimosC();
            hashC.get(str).setChave(primo);
            aux = aux.retornaProx();
        }
    }
    
    public static void segundaEtapa(ConcurrentHashMap<String, Objeto> hashC){
        // ## Segunda Etapa - Atribuir mesmo numero primo as classes equivalentes
        System.out.println();
        System.out.println("Segunda Etapa");
        int chave1, chave2, primo;
        StringDupla elemento;
        String key1, key2;
        System.out.println(relacaoC.size());
        for(int i=0; i < relacaoC.size(); i++){ // percorre a lista de relacoes
            elemento = relacaoC.get(i);
            key1 = elemento.getStr2();
            key2 = elemento.getStr1();
            chave1 = hashC.get(key1).getChave(); // encontra a chave da primeira classe
            chave2 = hashC.get(key2).getChave(); // encontra a chave da segunda classe

            System.out.println(key1+" -- "+chave1+" / "+key2+" -- "+chave2);
            if(chave1 == 0){ // se a chave do primeiro for 0
                if(chave2 == 0){ // se as duas chaves são 0 
                    primo = Primos.geraPrimosC(); // gera um novo primo
                    hashC.get(key1).setChave(primo); // insere o primo aos dois elementos
                    hashC.get(key2).setChave(primo); // insere o primo aos dois elementos
                }else{ // se apenas a chave do primeiro é 0
                    hashC.get(key1).setChave(chave2); // insere a chave do segundo no primeiro
                }
            }else{
                if(chave2 == 0){ // se apenas a chave do segundo é 0
                    hashC.get(key2).setChave(chave1); // insere a chave do primeiro no segundo
                }else{ // nenhuma das chaves é 0
                    if(chave1 < chave2){ // se a chave do primeiro é menor que a chave do segundo 
                        hashC.get(key2).setChave(chave1); // insere a chave do primeiro no segundo
                    }else{ // se a chave do segundo é menor ou igual que a chave do primeiro
                        hashC.get(key1).setChave(chave2); // insere a chave do segundo no primeiro
                    }
                }
            }
        }
        System.out.println("Fim Segunda Etapa");
        System.out.println();
    }
    
    public static void terceiraEtapa(ConcurrentHashMap<String, Objeto> hashC){
        // ## Terceira Etapa - Atribuir numeros primos para as classes restantes
        int chave;
        String key;
        Enumeration<String> en=hashC.keys();
        while(en.hasMoreElements()){ // percorre a lista de objetos
            key=en.nextElement();
            chave = hashC.get(key).getChave();
            if(chave == 0){ // verifica se a chave eh 0
                hashC.get(key).setChave(Primos.geraPrimosC()); // se for, cria um novo primo
            }
            System.out.println("Clsse: "+key+" -- Chave: "+hashC.get(key).getChave());
        }
    }
    
    public static void quartaEtapa(ConcurrentHashMap<String, Objeto> hashC){
        // ## Quarta Etapa - Multiplicar o primo de cada classe pelo primo do seu pai
        int chave;
        String key;
        Enumeration<String> en=hashH.keys();
        while(en.hasMoreElements()){ // percorre a lista de herancas
            key=en.nextElement();
            chave = hashC.get(key).getChave(); // pega a chave do pai
            Lista lista = hashH.get(key); // pega a lista de herancas do pai
            while(lista != null){ // percorre a lista de filhos
                CriaHash.propagaPrimo(lista.retornaStr(), chave, hashC, hashH); // propaga o primo do pai para seus filhos
                lista = lista.retornaProx();
            }
        }
    }
    
    public static void inserePrimosC(ConcurrentHashMap<String, Objeto> hashC, Lista frequencias){
            
        primeiraEtapa(hashC,frequencias); // ## Atribui um numero primo para as classes com heranca
        segundaEtapa(hashC);// ## Atribui mesmo numero primo as classes equivalentes
        terceiraEtapa(hashC); // ## Atribui numeros primos para as classes restantes
        quartaEtapa(hashC); // ## Multiplica o primo de cada classe pelo primo do seu pai
    }
    
    public static void inserePrimosA(ConcurrentHashMap<String, Atributo> hashA, ArrayList<StringDupla> relacaoA){
        int chave1, chave2, primo;
        StringDupla elemento;
        String key1, key2;
        for(int i=0; i < relacaoA.size(); i++){ // percorre a lista de relacoes
            elemento = relacaoA.get(i);
            key1 = elemento.getStr1();
            key2 = elemento.getStr2();
            chave1 = hashA.get(key1).getChave(); // encontra a chave do primeiro atributo
            chave2 = hashA.get(key2).getChave(); // encontra a chave do segundo atributo
            if(chave1 == 0){ // se a chave do primeiro seja 0
                if(chave2 == 0){ // se as duas chaves são 0 
                        primo = Primos.geraPrimosA(); // gera um novo primo
                        hashA.get(key1).setChave(primo); // insere o primo aos dois elementos
                        hashA.get(key2).setChave(primo); // insere o primo aos dois elementos
                }else{ // se apenas a chave do primeiro é 0
                        hashA.get(key1).setChave(chave2); // insere a chave do segundo no primeiro
                }
            }else{
                if(chave2 == 0){ // se apenas a chave do segundo é 0
                    hashA.get(key2).setChave(chave1); // insere a chave do primeiro no segundo
                }else{ // nenhuma das chaves é 0
                    if(chave1 < chave2){ // se a chave do primeiro é menor que a chave do segundo 
                            hashA.get(key2).setChave(chave1); // insere a chave do primeiro no segundo
                    }else{ // se a chave do segundo é menor ou igual que a chave do primeiro
                        hashA.get(key1).setChave(chave2); // insere a chave do segundo no primeiro
                    }
                }
           }
        }
    }
	
    static public void mergeHerancaEquivalencia(ConcurrentHashMap<String, Lista> hashH, ArrayList<StringDupla> equivalencias){
        Lista lista1;
        Lista lista2;
        Lista aux;
        for(int i = 0 ; i < equivalencias.size(); i++){ // percorre toda a lista de equivalencias
            StringDupla elemento = equivalencias.get(i);
            lista1 = hashH.get(elemento.getStr1()); // pega a lista de herancas do primeiro elemento
            lista2 = hashH.get(elemento.getStr2()); // pega a lista de herancas do segundo elemento
            if(lista1 != null || lista2 != null){
                if(lista1 == null){ // se a primeira lista for nula, usar apenas a segunda que nao eh nula
                    hashH.put(elemento.getStr1(),lista2); // insere a lista apenas para 1 dos elementos, caso contrario teremos redundancia ao atribuir os primos para cada filho
                    hashH.remove(elemento.getStr2()); // remove a lista do segundo
                }else{
                   if(lista2 == null){ // se a segunda linha for nula, usar apenas a primeira que nao eh nula
                       hashH.put(elemento.getStr2(),lista1);
                       hashH.remove(elemento.getStr1());
                   }else{
                       aux = lista1; // guarda a referencia para o inicio da lista
                       while(lista1.retornaProx() != null){ // percorre a lista1 ate encontra o ultimo elemento
                            lista1 = lista1.retornaProx();
                       }
                       lista1.insereProx(lista2); // insere a lista2 no fim da lista1
                       aux.insereN(aux.retornaN()+lista2.retornaN()); // atualiza o tamanho da lista
                       hashH.put(elemento.getStr1(),aux); // insere a lista apenas para 1 dos elementos, caso contrario teremos redundancia ao atribuir os primos para cada filho
                       hashH.remove(elemento.getStr2());  // remove a lista do segundo
                    }
                }
            }
         }
    }
        
    static public Lista contaHerancas (ConcurrentHashMap<String, Lista> hashH ){
        Enumeration<String> en=hashH.keys();
        Lista lista = null;
        Lista aux;
        while(en.hasMoreElements()){
            String key=en.nextElement();
            aux = hashH.get(key);
            lista = Lista.insere(lista, new Lista(key,aux.retornaN()));
        }
        return lista;
    }
	
    static public ArrayList<StringDupla> retiraIguais(ArrayList<StringDupla> lista){
        if(!lista.isEmpty()){
            boolean achou = false;
            ArrayList<StringDupla> nova_lista = new ArrayList<StringDupla>();
            nova_lista.add(lista.get(0)); // copia o primeiro elemento para garantir que nunca sera nula
            System.out.println("Lista size: "+lista.size());
            for(int i = 1 ; i < lista.size(); i++){
                StringDupla elemento = lista.get(i);
                String classe1 = elemento.getStr1();
                String classe2 = elemento.getStr2();
                System.out.println("Lista Original: "+classe1+" -- "+classe2);
                for(int j = 0; j < nova_lista.size(); j++){
                    StringDupla elemento2 = nova_lista.get(j);
                    String classe3 = elemento2.getStr1();
                    String classe4 = elemento2.getStr2();
                    System.out.println("Nova Lista: "+classe3+" -- "+classe4);
                    if(classe3.equals(classe2) && classe4.equals(classe1)){
                        achou = true;
                        break;
                    }
                }
                System.out.println(achou);
                if(!achou){
                    nova_lista.add(elemento);
                }
                /* NOVO CODIGO */
                achou = false;
                /* FIM NOVO CODIGO */
            }
            return nova_lista;
        }else{
            return lista;
        }
    }
        
    public static void propagaPrimo(String key, int chave, ConcurrentHashMap<String, Objeto> HashC, ConcurrentHashMap<String, Lista> hashH){
        Objeto objeto = HashC.get(key); // procura pelo objeto
        objeto.multiplicaChave(chave); // atualiza a chava
        Lista herancas = hashH.get(key);
        while(herancas != null){ // enquanto possuir heranca
            propagaPrimo(herancas.retornaStr(),chave, HashC, hashH); // repete o processo para os filhos do filho
            herancas = herancas.retornaProx();
        }
    }
        
    public static void verificaHerancasIndiretas(ConcurrentHashMap<String, Lista> hashH, ConcurrentHashMap<String, Objeto> HashC ){
        Enumeration<String> en=hashH.keys();
        while(en.hasMoreElements()){ // percorrea lista de herancas
            String key=en.nextElement();
            Lista lista = hashH.get(key);
            Lista aux = lista;
            System.out.println(key);
            if(lista != null){
                while(lista != null){ // percorre a lista de filhos
                    Objeto objeto = HashC.get(lista.retornaStr());
                    System.out.println("1)"+objeto.getClasse());
                    if(objeto.possuiEquivalencia()){ // verifica se o filho possui equivalencias
                        ConcurrentHashMap<String, String > equivalencias = objeto.getEquivalencias();
                        Enumeration<String> en2=equivalencias.keys();
                        while(en2.hasMoreElements()){ // percorrea lista de equivalencias
                            String key2=en2.nextElement();
                            System.out.println("2)"+key2);
                            aux = Lista.insereInicio(aux, new Lista(key2));
                        }
                    }
                    lista = lista.retornaProx();
                }
                hashH.put(key, aux);
            }
        }
    }

    public static void main (String args[]) throws Exception{
        String xmlPathnameA = "atributos.xsd";
        String xmlPathnameC = "classes.xsd";
        // Inicio Funcoes Atributos
        ConcurrentHashMap<String, Atributo> HashA = CriaHash.criaHashAtributos(xmlPathnameA);
        CriaHash.inserePrimosA(HashA, CriaHash.relacaoA);
        // Fim Funcoes Atributos
        
        // Inicio Funcoes Classes
        ConcurrentHashMap<String, Objeto> HashC = CriaHash.criaHashClasses(HashA,xmlPathnameC);
        System.out.println("1)"+relacaoC.size());
        relacaoC = retiraIguais(relacaoC);
        System.out.println("2)"+relacaoC.size());
        mergeHerancaEquivalencia(hashH,relacaoC);
        verificaHerancasIndiretas(hashH,HashC);
        Lista frequencias = contaHerancas(hashH);
        inserePrimosC( HashC, frequencias);
        // Fim Funcoes Classes

        //imprimir.imprimeRelacaoA(CriaHash.relacaoA, HashA);

        //imprimir.imprimeRelacaoC(CriaHash.relacaoC, HashC);
        //imprimir.imprimeFrequencias(frequencias);
        
        
        Imprimir.imprimeHashA(HashA);

        Imprimir.imprimeHashH(hashH);
        Imprimir.imprimeHashC(HashC);
        
        
        //Menu.criaMenu(HashC, HashA);
        Serializa.serializa(HashC, "hashC.ser");
        Serializa.serializa(HashA, "hashA.ser");
    }
}
