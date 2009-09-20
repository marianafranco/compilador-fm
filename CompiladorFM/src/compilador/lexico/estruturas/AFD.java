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
	
	
	public void recebeEntrada (char atual, char proximo, int tipo, boolean fechou) {
		
	}

}
