package compilador.metacompilador.estruturas;

import java.util.LinkedList;

import compilador.metacompilador.estruturas.Token;

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
		Token token = this.tokens.getFirst();
		this.tokens.remove();
		this.tamanho--;
		return token;
	}
}
