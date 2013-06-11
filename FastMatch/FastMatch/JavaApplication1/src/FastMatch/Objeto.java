package FastMatch;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class Objeto implements java.io.Serializable{

    ConcurrentHashMap<String, Atributo> attributes;
    ConcurrentHashMap<String, String > equivalencias;
    String classe;
    int chave;// = 1;
    String id;

    public ConcurrentHashMap<String, Atributo> getHash(){
        return this.attributes;
    }
    
    public Objeto(String classe) {
        this.classe = classe;
        attributes=new ConcurrentHashMap<String,  Atributo>();
        equivalencias=new ConcurrentHashMap<String,  String>();
    }
    
    public Objeto(String classe, int chave) {
        this.classe = classe;
        this.chave = chave;
        attributes=new ConcurrentHashMap<String,  Atributo>();
        equivalencias=new ConcurrentHashMap<String,  String>();
    }

    public String getClasse(){return this.classe;}
    public void setClasse(String classe){ this.classe = classe;}

    public int getChave(){return this.chave;}
    public void setChave(int chave){ this.chave = chave;}
    public int multiplicaChave(int chave){ 
        this.chave = this.chave*chave;
        return this.chave;
    }

    public void setAtributo(Atributo a){attributes.put(a.getNome(), a);}
    public Atributo getAtributo(Atributo a) {return attributes.get(a.getNome());}
    public Atributo getAtributo(String nome) {return attributes.get(nome);}
    public void imprimeAtributos(String campo){
        if(!this.attributes.isEmpty()){
            Enumeration<String> en=attributes.keys();
            Atributo atributo;
            while(en.hasMoreElements()){
                String key=en.nextElement();
                atributo = attributes.get(key);
                atributo.imprimeAtributo(campo);
                System.out.println();
            }
        }
    }

    public void setEquivalencia(String e){equivalencias.put(e, e);}
    public String getEquivalencia(String nome) {return equivalencias.get(nome);}
    public ConcurrentHashMap<String, String > getEquivalencias() {return equivalencias;}
    public boolean possuiEquivalencia(){return !this.equivalencias.isEmpty();}
    public void imprimeEquivalencia(){
        System.out.print("Equivalencias: ");
        if(!this.equivalencias.isEmpty()){
            Enumeration<String> en=equivalencias.keys();
            while(en.hasMoreElements()){
                String key = en.nextElement();
                System.out.print(" -- "+key);
            }
            System.out.println();
        }else{
            System.out.println("-- Classe sem equivalencias");
        }
    }
        
    public String toSelector(ConcurrentHashMap<String, Atributo> Hash){
        Enumeration<String> en=attributes.keys();
        String selector=new String();
        Enumeration<String> i;
        Atributo aux;
        Atributo atributo;
        boolean first=true;
        boolean first_match;
        while(en.hasMoreElements()){
            first_match = true;
            String key=en.nextElement();
            
            if(!first)selector=selector+ " AND ";
            
            atributo = attributes.get(key);
            chave = atributo.getChave(); 
            i = Hash.keys();
            
            while(i.hasMoreElements()){
            	String key2 = i.nextElement();
            	aux = Hash.get(key2); 
            	
            	if(aux.getChave()%chave == 0 && atributo != aux){
                    if(first_match) {
                        selector = selector + " ( ";
                        first_match = false;
                        selector=selector+ key + " LIKE "+ "'"+atributo.getValor()+ "'";
                    }
                    selector = selector + " OR " + aux.getNome() + " LIKE " + "'" + atributo.getValor() + "'";  
            	}
            }
            
            if(!first_match){ // encontrou algum atributo equivalente
            	selector = selector + " ) ";
            }else{
            	selector=selector+ key + " LIKE "+ "'"+atributo.getValor()+ "'";
            }
            first=false;
        }
        return selector;
    }
}
