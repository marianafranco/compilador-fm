package compilador.estruturas;

import java.util.LinkedList;

public class TabelaFuncao {
	
	LinkedList<Funcao> funcoes;
	
	int tamanho;
	
	
	public TabelaFuncao(){
		this.funcoes = new LinkedList<Funcao>();
		this.tamanho = 0;
	}
	
	
	public void insereFuncao(String nome, int tipo, String end){
		Funcao funcao = new Funcao(nome, tipo, end);
		this.funcoes.add(funcao);
		this.tamanho++;
	}
	

	public boolean estaNaTabela(String funcao){
		if(this.tamanho == 0){
			return false;
		}else{
			for(int i=0; i < this.funcoes.size(); i++){
				if(this.funcoes.get(i).getNome().equals(funcao)){
					return true;
				}
			}
			return false;
		}
	}
	
	public Funcao recuperaFuncao(String funcao){
		for(int i=0; i < this.funcoes.size(); i++){
			if(this.funcoes.get(i).getNome().equals(funcao)){
				return this.funcoes.get(i);
			}
		}
		return null;
	}
	
}
