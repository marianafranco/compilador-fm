package compilador.estruturas;

import compilador.estruturas.Simbolo;

/**
 * TabelaSimbolos: Implementa a estrutura utilizada para a tabela de simbolos.
 * 
 * @author Felipe Yoshida, Mariana R. Franco
 *
 */
public class TabelaSimbolos {
	
	
	private Simbolo simbolo;
	private int entradas;
	
	
	/**
	 * id do escopo ao qual pertence a tabela de simbolos 
	 */
	private int escopo;
	
	/**
	 * indica o id da tabela se simbolos do escopo anterior
	 */
	private int escopoAnterior;
	
	
	/**
	 * M�todo construtor
	 */
	public TabelaSimbolos () {
		this.entradas = 0;
	}
	
	
	public void adicionaEntrada (int posicao, String nome, int tipo, int linha, int coluna) {
		
		//System.out.println("Posicao: " + posicao + ", nome: " + nome + ", tipo: " + tipo + ", linha: " + linha + ", coluna: "+ coluna);
		Simbolo novo = new Simbolo(posicao, nome, tipo, linha, coluna);
		
		if (this.entradas == 0) {
			this.simbolo = novo;
		
		}else {
			Simbolo temp = this.simbolo;
						
			for(int i = 1; i < this.entradas; i++) {
				temp = temp.proximo;
			}	
			temp.proximo = novo;
		}
		this.entradas++;
	}
	
	
	public void adicionaEntrada (String nome, int tipo, int linha, int coluna) {
		
		//System.out.println("Posicao: " + posicao + ", nome: " + nome + ", tipo: " + tipo + ", linha: " + linha + ", coluna: "+ coluna);
		Simbolo novo = new Simbolo(this.entradas, nome, tipo, linha, coluna);
		
		if (this.entradas == 0) {
			this.simbolo = novo;
		
		}else {
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
		
		}else {
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
	
	public Simbolo recuperaEntrada (String nome) {
		
		if (this.entradas == 0) {
			return null;
		
		}else {
			Simbolo temp = this.simbolo;
			
			if(temp.getNome().equals(nome)) {
				return temp;
			}
			
			for (int i = 2; i <= this.entradas; i++) {
				temp = temp.getproximo();
				if(temp.getNome().equals(nome)) {
					return temp;
				}
			}			
			return null;
		}
	}
	
	public boolean estaNaTabela (String nome) {
		
		if (this.entradas == 0) {
			return false;
		
		}else {
			Simbolo temp = this.simbolo;
			if(temp.getNome().equals(nome)) {
				return true;
			}
			
			for (int i = 2; i <= this.entradas; i++) {
				temp = temp.getproximo();
				if(temp.getNome().equals(nome)) {
					return true;
				}
			}
			return false;
		}
	}
	
	
	// Gets e Sets
	
	public int getEntradas() {
		return this.entradas;
	}
	

	public int getEscopo() {
		return escopo;
	}


	public void setEscopo(int escopo) {
		this.escopo = escopo;
	}


	public int getEscopoAnterior() {
		return escopoAnterior;
	}


	public void setEscopoAnterior(int escopoAnterior) {
		this.escopoAnterior = escopoAnterior;
	}

	
}
