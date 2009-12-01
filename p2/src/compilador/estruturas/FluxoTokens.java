package compilador.estruturas;

import java.util.LinkedList;

import compilador.estruturas.Token;

/**
 * FluxoTokens: Implementa a estrutura de fluxo de tokens.
 * 
 * @author Felipe Yoshida, Mariana R. Franco
 *
 */
public class FluxoTokens {
	
	/**
	 * numero de tokens no fluxo.
	 */
	private int tamanho;
	
	/**
	 * vetor de tokens.
	 */
	private LinkedList<Token> tokens;
	
	/**
	 * Método contrutor.
	 */
	public FluxoTokens () {
		this.tamanho = 0;
		this.tokens = new LinkedList<Token>();
	}
	
	/**
	 * Adiciona um novo token ao fluxo.
	 * @param nome	valor do token.
	 * @param tipo	tipo do token.
	 */
	public void adicionaToken (String nome, int tipo) {
		Token novoToken = new Token(nome, tipo);
		this.tokens.add(novoToken);
		this.tamanho++;
	}
	
	/**
	 * Adiciona um novo token ao fluxo.
	 * @param nome
	 * @param tipo
	 * @param linha
	 * @param coluna
	 */
	public void adicionaToken (String nome, int tipo, int linha, int coluna) {
		//System.out.println("TOKEN = " + nome +" | TIPO = " + tipo);
		Token novoToken = new Token(nome, tipo, linha, coluna);
		this.tokens.add(novoToken);
		this.tamanho++;
	}
	
	/**
	 * Devolve o primeiro token do fluxo.
	 * @return
	 */
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
		
	//--- Gets e Sets ---
	
	/**
	 * Recupera o tamanho do fluxo de tokens.
	 */
	public int getTamanho(){
		return this.tamanho;
	}
	
}
