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
	
	public void adicionaTransicao (Vector<Transicao> transicoes) {
		boolean jaExiste = false;
		for(int i=0; i < transicoes.size(); i++){
			for(int j=0; j < this.transicoes.size(); j++){
				if(transicoes.get(i).getEntrada().equals(this.transicoes.get(j).getEntrada())){
					if(transicoes.get(i).getProximo() == this.transicoes.get(j).getProximo()){
						jaExiste = true;
					}
				}
			}
			
			if(!jaExiste){
				this.transicoes.add(new Transicao(transicoes.get(i).getProximo(), transicoes.get(i).getEntrada()));
			}
		}
	}
	
	public void removeTransicao(int i){
		this.transicoes.remove(i);	
	}
	
	
	public void removeTransicao(Transicao trans){
		this.transicoes.remove(trans);
	}
	
	public void removeTransicao(String entrada){
		for (int i = 0; this.transicoes.size() > i; i++) {
			if (this.transicoes.get(i).proximoEstado(entrada) != -1) {
				this.transicoes.remove(i);
				return;
			}
		}
	}
	
	public boolean naoDeterminismo(){
		boolean naoDeterminismo = false;
		for (int i = 0; this.transicoes.size() > i; i++) {
			for(int j= i+1; this.transicoes.size() > j; j++){
				if (this.transicoes.get(i).getEntrada().equals(this.transicoes.get(j).getEntrada())){
					naoDeterminismo = true;
				}
			}
		}
		return naoDeterminismo;
	}
	
	public Vector<Transicao> getNaoDeterminismo(){
		Vector<Transicao> naoDeterminismo = new Vector<Transicao>();
		for (int i = 0; this.transicoes.size() > i; i++) {
			for(int j= i+1; this.transicoes.size() > j; j++){
				if (this.transicoes.get(i).getEntrada().equals(this.transicoes.get(j).getEntrada())){
					naoDeterminismo.add(this.transicoes.get(i));
					naoDeterminismo.add(this.transicoes.get(j));
				}
			}
		}
		return naoDeterminismo;
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

	public Vector<Transicao> getTransicoes() {
		return transicoes;
	}
	
}
