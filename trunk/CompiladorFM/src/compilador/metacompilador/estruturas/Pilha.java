package compilador.metacompilador.estruturas;

public class Pilha {

	int estado;
	String submaquina;
	
	public Pilha (int estado, String submaquina){
		this.estado = estado;
		this.submaquina = submaquina;
	}
	
	// Gets e Sets
	
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getSubmaquina() {
		return submaquina;
	}
	public void setSubmaquina(String submaquina) {
		this.submaquina = submaquina;
	}
	
}
