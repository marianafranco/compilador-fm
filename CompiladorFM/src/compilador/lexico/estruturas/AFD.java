package compilador.lexico.estruturas;

import compilador.lexico.estruturas.Estado;

public class AFD {

	private int estadoAtivo;
	private Estado estados[];
	
	public AFD () {
		this.estadoAtivo = -1;
	}
	
	public void adicionaEstado (Estado adicionado, boolean inicial) {
		if (inicial == true) {
			this.estadoAtivo = adicionado.getId();
		}
		this.estados[this.estados.length] = adicionado;
	}
	
	public void recebeEntrada (char atual, char proximo) {
		
	}

}
