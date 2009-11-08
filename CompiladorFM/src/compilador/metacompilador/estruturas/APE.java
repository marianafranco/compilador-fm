package compilador.metacompilador.estruturas;

public class APE {
	
	int numSubmaquinas;
	private AFD submaquinas[];
	
	
	public void adicionaSubmaquina (AFD nova, int indice) {
		this.submaquinas[indice] = nova;
	}
	
	public AFD getSubmaquina(String nome){
		for (int i=this.numSubmaquinas - 1; i > 0; i--){
			if(this.submaquinas[i].getNome().equals(nome)){
				return this.submaquinas[i];
			}
		}
		return null;
	}
	
	
	// Gets e Sets
	
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
