package compilador.lexico.estruturas;

import compilador.lexico.estruturas.Simbolo;

public class TabelaSimbolos {

	private Simbolo simbolo;
	private int entradas;
	
	public TabelaSimbolos () {
		this.entradas = 0;
	}
	
	public void adicionaEntrada (int posicao, String nome, int tipo, int linha, int coluna) {
		
		System.out.println("TOKEN: " + nome);
		
		Simbolo novo = new Simbolo(posicao, nome, tipo, linha, coluna);
		
		if (this.entradas == 0) {
			this.simbolo = novo;
		}
		else {
			Simbolo temp = this.simbolo;
						
			for(int i = 0; i < this.entradas; i++) {
				temp.proximo = temp;
			}
			
			this.simbolo = novo;
		}
		
		this.entradas++;
	}
	
	public int getEntradas() {
		return this.entradas;
	}
}
