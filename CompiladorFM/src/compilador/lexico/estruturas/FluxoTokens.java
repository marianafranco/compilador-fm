package compilador.lexico.estruturas;

import compilador.lexico.estruturas.Token;

public class FluxoTokens {

	private Token token;
	private int tamanho;
	
	public FluxoTokens () {
		this.tamanho = 0;
	}
	
	public void adicionaToken (String nome, int valor) {
		
		//System.out.println("Token: " + nome + ", valor: " + valor);
		
		Token novo = new Token(nome, valor);
		
		if (this.tamanho == 0) {
			this.token = novo;
		}
		else {
			Token temp = this.token;
						
			for(int i = 1; i < this.tamanho; i++) {
				temp = temp.proximo;
			}

			temp.proximo = novo;
			
		}
		
		this.tamanho++;
	}
	
	public void adicionaToken (String nome, int valor, int linha, int coluna) {
		
		//System.out.println("Token: " + nome + ", valor: " + valor);
		
		Token novo = new Token(nome, valor, linha, coluna);
		
		if (this.tamanho == 0) {
			this.token = novo;
		}
		else {
			Token temp = this.token;
						
			for(int i = 1; i < this.tamanho; i++) {
				temp = temp.proximo;
			}

			temp.proximo = novo;
			
		}
		
		this.tamanho++;
	}
	
	public Token recuperaToken () {
		
		if (this.tamanho == 0) {
			return null;
		}
		else {
			Token temp = this.token;
			this.token = this.token.proximo;
			this.tamanho--;
			return temp;
		}
	}
	
	public int getTamanho(){
		return this.tamanho;
	}
	
}
