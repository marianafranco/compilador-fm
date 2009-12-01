package compilador.estruturas;

import java.util.Vector;

import compilador.estruturas.Estado;
import compilador.exceptions.CaractereInvalidoException;

public class AFD {

	private String nome;
	private int estadoAtivo;
	private Vector<Estado> estados;
	
	
	public AFD () {
		this.estadoAtivo = -1;
		this.estados = new Vector<Estado>();
	}
	
	public AFD (String nome) {
		this.nome = nome;
		this.estadoAtivo = -1;
		this.estados = new Vector<Estado>();
	}
	
	public void adicionaEstado (Estado adicionado, boolean inicial) {
		if (inicial == true) {
			this.estadoAtivo = adicionado.getId();
		}
		this.estados.add(adicionado);
	}
	
	
	public void adicionaEstado (Estado adicionado) {
		if (adicionado.getId() == 0) {
			this.estadoAtivo = adicionado.getId();
		}
		this.estados.add(adicionado);
	}
	
	
	public int procuraEstado (int id) {	
		for (int i = 0; i < this.estados.size(); i++) {
			if (this.estados.get(i).getId() == id) {
				return i;
			}
		}
		//System.out.println("[ERRO] Não encontrou o estado:" + id);
		return -1;
	}
	
	public Estado getEstado(int id){
		for (int i = 0; this.estados.size() > i; i++) {
			if (this.estados.get(i).getId() == id) {
				return this.estados.get(i);
			}
		}
		return null;
	}
	
	public Estado getEstadoIndice(int indice){
		return this.estados.get(indice);
	}
	
	public int getTipo(){
		int estadoAtual = procuraEstado(this.estadoAtivo);
		return this.estados.get(estadoAtual).getTipo();
	}
	
	public boolean estadoAtivoFinal(){
		int estadoAtual = procuraEstado(this.estadoAtivo);
		return this.estados.get(estadoAtual).getAceitacao();
	}
	
	public boolean temTransicao (char atual) {
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Verifica se existe transicao
		int proximoEstado = this.estados.get(estadoAtual).proximoEstado(atual);
		
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
		int proximoEstado = this.estados.get(estadoAtual).proximoEstado(atual);
		
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
		
		// Pega o próximo estado
		int proximoEstado = this.estados.get(estadoAtual).proximoEstado(atual);
		
		this.estadoAtivo = proximoEstado;
	}
	
	
	public void percorre(String atual){
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Pega o próximo estado
		int proximoEstado = this.estados.get(estadoAtual).proximoEstado(atual);
		
		this.estadoAtivo = proximoEstado;
	}
	
	
	public boolean transicaoFinal(char proximo){
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Verifica se existe transicao
		int proximoEstado = this.estados.get(estadoAtual).proximoEstado(proximo);
		
		// Se nao existe
		if (proximoEstado == -1) {
			System.out.println("Erro em transicaoFinal");
			return false;
		}
		// Se existe transicao
		else {
			// Verifica se o estado é de aceitacao
			if (this.estados.get(proximoEstado).getAceitacao() == true) {
				return true;
			}else{
				return false;
			}
		}
	}
	
	
	public int getNextEstadoID(){
		int nextID = 0;
		for(int i = 0; i < this.estados.size(); i++){
			if(this.estados.get(i).getId() > nextID){
				nextID = this.estados.get(i).getId();
			}
		}
		return nextID + 1;
	}
	
	public void removeEstado (int id) {
		for(int i = 0; i < this.estados.size(); i++){
			if(this.estados.get(i).getId() == id){
				estados.remove(i);
			}
		}
	}
	
	public int getTamanho(){
		return this.estados.size();
	}
	
	// Gets e Sets
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setEstadoAtivo(int estado){
		this.estadoAtivo = estado;
	}
	
	public int getEstadoAtivo(){
		return this.estadoAtivo;
	}
	
}
