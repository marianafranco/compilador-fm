package compilador.lexico.estruturas;

import compilador.lexico.estruturas.Token;

public class FluxoTokens {

	private Token token;
	private int tamanho;
	
	public FluxoTokens () {
		this.tamanho = 0;
	}
	
	public void adicionaToken (String nome, int valor) {
		
		System.out.println("Token: " + nome + ", valor: " + valor);
		
		Token novo = new Token(nome, valor);
		
		if (this.tamanho == 0) {
			this.token = novo;
		}
		else {
			Token temp = this.token;
						
			for(int i = 0; i < this.tamanho; i++) {
				temp.proximo = temp;
			}
			
			this.token = novo;
		}
		
		this.tamanho++;
	}
	
}
