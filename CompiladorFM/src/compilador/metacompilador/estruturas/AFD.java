package compilador.metacompilador.estruturas;

import compilador.metacompilador.estruturas.Estado;
import compilador.exceptions.CaractereInvalidoException;

public class AFD {

	private String nome;
	private int estadoAtivo;
	private int numEstados;
	private Estado estados[];
	
	
	public AFD () {
		this.estadoAtivo = -1;
	}
	
	public AFD (String nome) {
		this.nome = nome;
		this.estadoAtivo = -1;
	}
	
	public void adicionaEstado (Estado adicionado, boolean inicial, int indice) {
		if (inicial == true) {
			this.estadoAtivo = adicionado.getId();
		}
		this.estados[indice] = adicionado;
	}
	
	
	public void adicionaEstado (Estado adicionado, int indice) {
		if (adicionado.getId() == 0) {
			this.estadoAtivo = adicionado.getId();
		}
		this.estados[indice] = adicionado;
	}
	
	
	public int procuraEstado (int id) {	
		for (int i = 0; this.estados.length > i; i++) {
			if (this.estados[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public int getTipo(){
		int estadoAtual = procuraEstado(this.estadoAtivo);
		return this.estados[estadoAtual].getTipo();
	}
	
	public boolean estadoAtivoFinal(){
		int estadoAtual = procuraEstado(this.estadoAtivo);
		return this.estados[estadoAtual].getAceitacao();
	}
	
	public boolean temTransicao (char atual) {
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Verifica se existe transicao
		int proximoEstado = this.estados[estadoAtual].proximoEstado(atual);
		
		// Se nao existe
		if (proximoEstado == -1) {
			return false;
		
		// Se existe transicao, retorna true
		}else {
			return true;
		}
	}
	
	public boolean temTransicao (String atual) {
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Verifica se existe transicao
		int proximoEstado = this.estados[estadoAtual].proximoEstado(atual);
		
		// Se nao existe
		if (proximoEstado == -1) {
			return false;
		
		// Se existe transicao, retorna true
		}else {
			return true;
		}
	}
	
	
	public void percorre(char atual){
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Pega o pr�ximo estado
		int proximoEstado = this.estados[estadoAtual].proximoEstado(atual);
		
		this.estadoAtivo = proximoEstado;
	}
	
	
	public void percorre(String atual){
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Pega o pr�ximo estado
		int proximoEstado = this.estados[estadoAtual].proximoEstado(atual);
		
		this.estadoAtivo = proximoEstado;
	}
	
	
	public boolean transicaoFinal(char proximo){
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Verifica se existe transicao
		int proximoEstado = this.estados[estadoAtual].proximoEstado(proximo);
		
		// Se nao existe
		if (proximoEstado == -1) {
			System.out.println("Erro em transicaoFinal");
			return false;
		}
		// Se existe transicao
		else {
			// Verifica se o estado � de aceitacao
			if (this.estados[proximoEstado].getAceitacao() == true) {
				return true;
			}else{
				return false;
			}
		}
	}
	
	
	// Gets e Sets
	
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	public void setNumEstados(int numEstados){
		this.numEstados = numEstados;
		this.estados = new Estado [this.numEstados];
	}
	
	
	public void setEstadoAtivo(int estado){
		this.estadoAtivo = estado;
	}
	
	public int getEstadoAtivo(){
		return this.estadoAtivo;
	}
	
}
