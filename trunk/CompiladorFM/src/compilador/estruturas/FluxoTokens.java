package compilador.estruturas;

import java.util.LinkedList;

import compilador.estruturas.Token;

public class FluxoTokens {
	
	private int tamanho;
	private LinkedList<Token> tokens;
	
	public FluxoTokens () {
		this.tamanho = 0;
		this.tokens = new LinkedList<Token>();
	}
	
	public void adicionaToken (String nome, int tipo) {
		Token novoToken = new Token(nome, tipo);
		this.tokens.add(novoToken);
		this.tamanho++;
	}
	
	public void adicionaToken (String nome, int tipo, int linha, int coluna) {
		//System.out.println("TOKEN = " + nome +" | TIPO = " + tipo);
		Token novoToken = new Token(nome, tipo, linha, coluna);
		this.tokens.add(novoToken);
		this.tamanho++;
	}
	
	public Token recuperaToken(){
		if (this.tamanho <= 0) {
			return null;
		}
		else {
			Token token = this.tokens.removeFirst();
			this.tamanho--;
			return token;
		}
	}
	
	
	// Gets e Sets
	
	public int getTamanho(){
		return this.tamanho;
	}
}
