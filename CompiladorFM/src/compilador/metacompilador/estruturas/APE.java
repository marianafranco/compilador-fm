package compilador.metacompilador.estruturas;

public class APE {
	
	private String submaquinaPrincipal;
	int numSubmaquinas;
	private AFD submaquinas[];
	
	
	public void adicionaSubmaquina (AFD nova, int indice) {
		this.submaquinas[indice] = nova;
	}
	
	
	// Gets e Sets
	
	public String getSubmaquinaPrincipal() {
		return submaquinaPrincipal;
	}
	
	public void setSubmaquinaPrincipal(String submaquinaPrincipal) {
		this.submaquinaPrincipal = submaquinaPrincipal;
	}
	
	public int getNumSubmaquinas() {
		return numSubmaquinas;
	}
	
	public void setNumSubmaquinas(int numSubmaquinas) {
		this.numSubmaquinas = numSubmaquinas;
		this.submaquinas = new AFD [this.numSubmaquinas];
	}
	
	public AFD[] getSubmaquinas() {
		return submaquinas;
	}
	
	public void setSubmaquinas(AFD[] submaquinas) {
		this.submaquinas = submaquinas;
	}
	
}
