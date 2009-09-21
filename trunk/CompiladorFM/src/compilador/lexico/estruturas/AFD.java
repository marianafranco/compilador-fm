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
	
	public int recebeEntrada (char atual, char proximo, int tipo) {
		
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
