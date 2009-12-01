package compilador.estruturas;

public class PilhaEstadoSubmaquina {

	int estado;
	String submaquina;
	
	public PilhaEstadoSubmaquina (int estado, String submaquina){
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
