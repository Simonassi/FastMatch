/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FastMatch;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
/**
 *
 * @author Rafael
 */
public class Menu {
    public static void criaMenu(ConcurrentHashMap<String, Objeto> HashC, ConcurrentHashMap<String, Atributo> HashA) throws NumberFormatException, IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        Imprimir imprimir = new Imprimir();
        List<Objeto> Publicacoes = new ArrayList<Objeto>();
        List<Objeto> Subscricoes = new ArrayList<Objeto>();
        int opcao = 0;
        int primoC;
        int primoA;
        int tam;
        int i;
        @SuppressWarnings("unused")
        String lixo;
        do{
            LimpaTela();
            System.out.println("Escolha a opcao desejada:");
            System.out.println("");
            System.out.println("1 - Publicar");
            System.out.println("2 - Subscrever");
            System.out.println("3 - Matching");
            System.out.println("4 - Exibir Classes");
            System.out.println("5 - Exibir Atributos");
            System.out.println("6 - Exibir Publicações");
            System.out.println("7 - Exibir Subscrições");
            System.out.println("8 - Sair\n");
            opcao = Integer.parseInt(reader.readLine()); 
            Objeto objeto;
            String valor;
            String atributo;
            String classe;
            String resposta;
            String aux;
            switch (opcao){
            case 1:
                resposta = "s";
                System.out.println("Escolha uma categoria existente ou crie uma nova categoria:\n");
                System.out.println("");
                classe = reader.readLine();
                primoC = Match.verificaObjeto(classe,HashC);
                if(primoC == 0){
                    aux = levenshtein.buscaDistanciaLevenshteinC(classe,HashC);
                    if(!aux.equals("null")){
                        System.out.println(aux);
                        primoC = HashC.get(aux).getChave();
                        objeto = new Objeto(aux, primoC);
                    }else{
                        primoC = Primos.geraPrimosC();
                        HashC.put(classe, new Objeto(classe,primoC));
                        objeto = new Objeto(classe, primoC);
                    }
                }else{
                    objeto = new Objeto(classe, primoC);
                }
                do{
                    System.out.println("Escolha um atributo existente ou crie um novo atributo:");
                    System.out.println("");
                    atributo = reader.readLine();
                    System.out.println("Digite um valor:");
                    valor = reader.readLine();
                    primoA = Match.verificaAtributo(atributo,HashA);
                    if(primoA == 0){
                        aux = levenshtein.buscaDistanciaLevenshteinA(atributo,HashA);
                        if( !aux.equals("null")){
                            System.out.println(aux);
                            primoA = HashA.get(aux).getChave();
                            objeto.setAtributo(new Atributo(aux, valor, primoA));
                        }else{ 
                            primoA = Primos.geraPrimosA();
                            HashA.put(atributo, new Atributo(atributo,primoA));
                            objeto.setAtributo(new Atributo(atributo, valor, primoA));
                        }
                    }else{
                        objeto.setAtributo(new Atributo(atributo, valor, primoA));
                    }
                    System.out.println("Deseja inserir um novo atributo? s/n ");
                    resposta = reader.readLine();
                }while(resposta.equals("s"));
                    Publicacoes.add(objeto);
                break;
            case 2:
                resposta = "s";
                System.out.println("Escolha uma categoria existente ou crie uma nova categoria:\n");
                System.out.println("");
                classe = reader.readLine();
                primoC = Match.verificaObjeto(classe,HashC);
                if(primoC == 0){
                    aux = levenshtein.buscaDistanciaLevenshteinC(classe,HashC);
                    if(!aux.equals("null")){
                        System.out.println(aux);
                        primoC = HashC.get(aux).getChave();
                        objeto = new Objeto(aux, primoC);
                    }else{
                        primoC = Primos.geraPrimosC();
                        HashC.put(classe, new Objeto(classe,primoC));
                        objeto = new Objeto(classe, primoC);
                    }
                }else{
                    objeto = new Objeto(classe, primoC);
                }
                do{
                    System.out.println("Escolha um atributo existente ou crie um novo atributo:");
                    System.out.println("");
                    atributo = reader.readLine();
                    System.out.println("Digite um valor:");
                    valor = reader.readLine();
                    primoA = Match.verificaAtributo(atributo,HashA);
                    if(primoA == 0){
                        aux = levenshtein.buscaDistanciaLevenshteinA(atributo,HashA);
                        if( !aux.equals("null")){
                            System.out.println(aux);
                            primoA = HashA.get(aux).getChave();
                            objeto.setAtributo(new Atributo(aux, valor, primoA));
                        }else{ 
                            primoA = Primos.geraPrimosA();
                            HashA.put(atributo, new Atributo(atributo,primoA));
                            objeto.setAtributo(new Atributo(atributo, valor, primoA));
                        }
                    }else{
                        objeto.setAtributo(new Atributo(atributo, valor, primoA));
                    }
                    System.out.println("Deseja inserir um novo atributo? s/n ");
                    resposta = reader.readLine();
                }while(resposta.equals("s"));
                    Subscricoes.add(objeto);
                break;
            case 3:
                tam = Subscricoes.size();
                for( i = 0; i< tam; i++){
                    System.out.println("");
                    System.out.print("## ");
                    Objeto obj = Subscricoes.get(i);
                    System.out.print(obj.getClasse());
                    obj.imprimeAtributos("valor");
                    System.out.print("##");
                    Match.verificaMatch(Publicacoes,Subscricoes.get(i), "S");
                    System.out.println("\n");
                }
                lixo = reader.readLine();
                break;
            case 4:
                LimpaTela();
                imprimir.imprimeHashC(HashC);
                lixo = reader.readLine();
                break;
            case 5:
                LimpaTela();
                imprimir.imprimeHashA(HashA);
                lixo = reader.readLine();
                break;
            case 6:
                imprimir.imprimePS(Publicacoes);
                lixo = reader.readLine();
                break;
            case 7:
                imprimir.imprimePS(Subscricoes);
                lixo = reader.readLine();
                break;
            }
        }
        while(opcao != 8);
}

    static public void LimpaTela(){
        for( int i = 0 ; i < 10; i++)
        System.out.println("");
    }
    
}
