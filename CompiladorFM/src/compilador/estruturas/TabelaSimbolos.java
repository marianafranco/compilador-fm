package compilador.estruturas;

import compilador.estruturas.Simbolo;

public class TabelaSimbolos {

	private Simbolo simbolo;
	private int entradas;
	
	public TabelaSimbolos () {
		this.entradas = 0;
	}
	
	public void adicionaEntrada (int posicao, String nome, int tipo, int linha, int coluna) {
		
		//System.out.println("Posicao: " + posicao + ", nome: " + nome + ", tipo: " + tipo + ", linha: " + linha + ", coluna: "+ coluna);
		
		Simbolo novo = new Simbolo(posicao, nome, tipo, linha, coluna);
		
		if (this.entradas == 0) {
			this.simbolo = novo;
		}
		else {
			Simbolo temp = this.simbolo;
						
			for(int i = 1; i < this.entradas; i++) {
				temp = temp.proximo;
			}
			
			temp.proximo = novo;
		}
		
		this.entradas++;
		
	}
	
	public Simbolo recuperaEntrada (int posicao) {
		
		if (this.entradas == 0) {
			return null;
		}
		else {
			Simbolo temp = this.simbolo;
			if(temp.getId() == posicao) {
				return temp;
			}
			for (int i = 2; i <= this.entradas; i++) {
				temp = temp.getproximo();
				if(temp.getId() == posicao) {
					return temp;
				}
			}
			
			return null;
		}
		
	}
	
	public boolean estaNaTabela (String nome) {
		
		if (this.entradas == 0) {
			return false;
		}
		else {
			Simbolo temp = this.simbolo;
			if(temp.getNome() == nome) {
				return true;
			}
			for (int i = 2; i <= this.entradas; i++) {
				temp = temp.getproximo();
				if(temp.getNome() == nome) {
					return true;
				}
			}
			
			return false;
		}
		
	}
	
	public int getEntradas() {
		return this.entradas;
	}
}
