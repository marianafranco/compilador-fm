package compilador.estruturas;

import java.util.Vector;

import compilador.estruturas.Transicao;

public class Estado {
	
	private int id;
	private boolean aceitacao;
	private int tipo;
	private Vector<Transicao> transicoes;
	
	public Estado(int id, boolean aceitacao) {
		this.id = id;
		this.aceitacao = aceitacao;
		this.transicoes = new Vector<Transicao>();
	}
	
	public Estado(int id, boolean aceitacao, int tipo) {
		this.id = id;
		this.aceitacao = aceitacao;
		this.tipo = tipo;
		this.transicoes = new Vector<Transicao>();
	}
	
	
	public void adicionaTransicao (Transicao nova) {
		this.transicoes.add(nova);
	}
	
	
	public void removeTransicao(int i){
		this.transicoes.remove(i);	
	}
	
	
	public void removeTransicao(Transicao trans){
		this.transicoes.remove(trans);
	}
	
	
	public int proximoEstado (char entrada) {
		int proximo = -1;
		
		for (int i = 0; this.transicoes.size() > i; i++) {
			proximo = this.transicoes.get(i).proximoEstado(entrada);
			if (proximo != -1) {
				return proximo;
			}
		}
		
		return proximo;
	}
	
	
	public int proximoEstado (String entrada) {
		int proximo = -1;
		
		for (int i = 0; this.transicoes.size() > i; i++) {
			proximo = this.transicoes.get(i).proximoEstado(entrada);
			if (proximo != -1) {
				return proximo;
			}
		}
		
		return proximo;
	}
	
	public int getTamanho(){
		return this.transicoes.size();
	}
	
	public Transicao getTransicao(int indice){
		return this.transicoes.get(indice);
	}
	
	// Gets e Sets
	
	public int getId () {
		return this.id;
	}
	
	public boolean getAceitacao () {
		return this.aceitacao;
	}
	
	public void setAceitacao(boolean aceita){
		this.aceitacao = aceita;
	}
	
	public int getTipo () {
		return this.tipo;
	}
}
