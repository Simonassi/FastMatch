package FastMatch;


public class Lista {
	String str;
	int n;
	Lista prox;
	
	Lista(String str, int n){
		this.str = str;
		this.n = n;
		prox = null;
	}
	
        Lista(String str){
		this.str = str;
		this.n = 1;
		prox = null;
	}
        
	public int retornaN(){ return this.n;}
	public Lista retornaProx(){ return this.prox;}
	public String retornaStr(){ return this.str;}
	
	public void insereProx(Lista prox){ this.prox = prox;}
        public void insereN(int n){ this.n = n;}
        public void incrementaN(){ this.n = this.n + 1;}
	
	public static boolean compara (Lista no1, Lista no2){ // verifica se o no1 � menor que o no2
		if(no1.retornaN() < no2.retornaN()){
			return true;
		}
		return false;
	}
	
        public static Lista insereInicio(Lista lista, Lista no){
            no.insereProx(lista);
            if(lista != null)
                no.n += lista.n;
            return no;
        }
        
	public static Lista insere(Lista lista, Lista no){
		if(lista == null){ // primeiro elemento a ser inserido
			return no;
		}
		if(!Lista.compara(no, lista)){ // n� � maior ou igual que a lista 
			no.insereProx(lista);
			return no;
		}
		if(lista.retornaProx() == null){ // n� n�o � maior que a lista e a lista possui 1 elemento 
			lista.insereProx(no);
			return lista;
		}
		
		Lista aux = lista.retornaProx();
		if(aux.retornaProx() == null){ // lista possui apenas 2 elemetos
			if(Lista.compara(aux, no)){ // se o aux < n�, aux passa a ser o ultimo elemento
				no.insereProx(aux);
				lista.insereProx(no);
				return lista;
			}
			aux.insereProx(no);
			return lista;
		}
		for(aux = lista.retornaProx(); aux.retornaProx() != null; aux = aux.retornaProx()){
			if(Lista.compara(aux.retornaProx(), no)){
				no.insereProx(aux.retornaProx());
				aux.insereProx(no);
				return lista;
			}
		}
		aux.insereProx(no);
		return lista;
	}
}
