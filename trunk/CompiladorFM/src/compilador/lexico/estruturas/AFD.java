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
	
	public void recebeEntrada (char atual, char proximo, int tipo, boolean fechou) {
		
		// Pega o indice do estado atual
		int indice = procuraEstado(this.estadoAtivo);
		
		// Muda o estado atual
		this.estadoAtivo = this.estados[indice].proximoEstado(atual);
		
		// Pega o tipo do estado atual
		indice = procuraEstado(this.estadoAtivo);
		tipo = this.estados[indice].getTipo();
		
		if (this.estados[indice].proximoEstado(proximo) != estadoAtivo) {
			fechou = true;
		}
		else {
			fechou = false;
		}
	}
}
