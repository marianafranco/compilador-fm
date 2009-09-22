package compilador.lexico.estruturas;

import compilador.lexico.estruturas.Estado;

public class AFD {

	private int estadoAtivo;
	private int numEstados;
	private Estado estados[];
	
	public AFD () {
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
	
	public void setNumEstados(int numEstados){
		this.numEstados = numEstados;
		this.estados = new Estado [this.numEstados];
	}
	
	public int procuraEstado (int id) {
		
		for (int i = 0; this.estados.length > i; i++) {
			if (this.estados[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean temTransicao (char atual) {
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Verifica se existe transicao
		int proximoEstado = this.estados[estadoAtual].proximoEstado(atual);
		
		// Se nao existe
		if (proximoEstado == -1) {
			// Verifica se o estado atual é de aceitacao
			if (this.estados[estadoAtual].getAceitacao() == true) {
				return false;
			}
			else {
				System.out.println("Transicao incorreta, imprimir linha e coluna e dar erro");
				return false;
			}
		}
		// Se existe transicao, retorna true
		else {
			return true;
		}
	}
	
	public void percorre(char atual){
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Pega o próximo estado
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
			// Verifica se o estado é de aceitacao
			if (this.estados[proximoEstado].getAceitacao() == true) {
				return true;
			}else{
				return false;
			}
		}
	}
	
	public void setEstadoAtivo(int estadoID){
		this.estadoAtivo = estadoID;
	}
	
	public int recebeEntrada (char atual, char proximo) {
		
		int tipo;
		
		System.out.println(atual  + " " + proximo);
		
		// Pega o indice do estado atual
		int estadoAtual = procuraEstado(this.estadoAtivo);
		
		// Pega o estado atual
		int proximoEstado = this.estados[estadoAtual].proximoEstado(atual);
		
		if(proximoEstado == -1){
			this.estadoAtivo = 0;
			tipo = this.estados[estadoAtual].getTipo();
			return tipo;
			//return true;
		}else{
			this.estadoAtivo = proximoEstado;
			
			// Pega o tipo do estado atual
			estadoAtual = procuraEstado(this.estadoAtivo);
			tipo = this.estados[estadoAtual].getTipo();
			
			proximoEstado = this.estados[estadoAtual].proximoEstado(proximo);
			
			if(proximoEstado == -1 || proximoEstado != estadoAtivo){
				return tipo;
				//return true;
			}
			else {
				return -1;
				//return false;
			}
			
		}
	}
}
