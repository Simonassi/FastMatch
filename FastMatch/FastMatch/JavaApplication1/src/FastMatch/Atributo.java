package FastMatch;


public class Atributo implements java.io.Serializable {
    String valor;
    String nome; // atributo usado como indice na tabela hash
    int chave;
    String id;

    public void imprimeAtributo(String campo){
        if("valor".equals(campo)) System.out.print(" - "+this.nome+": "+this.valor);
        else if ("chave".equals(campo)) System.out.print(" - "+this.nome+": "+this.chave);
        else if ("id".equals(campo)) System.out.print(" - "+this.nome+": "+this.id);

    }

    public Atributo(String nome, String valor, int chave){
        this.nome = nome;
        this.valor = valor;
        this.chave = chave;
    }

    public Atributo(String nome, int chave){
        this.nome = nome;
        this.chave = chave;
    }

    public Atributo(String nome){
        this.nome = nome;
        this.chave = 0;
    }
    
    public int getChave(){return this.chave;}
    public void setChave(int chave){this.chave = chave;}

    public void setValor(String valor){this.valor = valor;}
    public String getValor(){return this.valor;}

    public String getNome(){return this.nome;}
    public void setNome(String nome){this.nome = nome;}

    public String getId(){return this.id;}
}
