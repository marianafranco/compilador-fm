package compilador.lexico.estruturas;

import compilador.lexico.estruturas.Transicao;

public class Estado {
	
	private int id;
	private boolean aceitacao;
	private Transicao transicoes[];
	
	public Estado(int id, boolean aceitacao) {
		this.id = id;
		this.aceitacao = aceitacao;
	}
	
	public void adicionaTransicao (Transicao nova) {
		this.transicoes[this.transicoes.length] = nova;
	}
	
	public int getId () {
		return this.id;
	}
	
	public boolean getAceitacao () {
		return this.aceitacao;
	}
	
	public int proximoEstado (char entrada) {
		int proximo = -1;
		
		for (int i = 0; this.transicoes.length > i; i++) {
			proximo = this.transicoes[i].proximoEstado(entrada);
			if (proximo != -1) {
				return proximo;
			}
		}
		
		return proximo;
	}
}
