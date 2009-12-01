package compilador.estruturas;

public class PilhaSeSenao {

	int idIF;
	boolean temELSE;
	
	public PilhaSeSenao (int idIF, boolean temELSE){
		this.idIF = idIF;
		this.temELSE = temELSE;
	}

	
	// Gets e Sets
	
	public int getIdIF() {
		return idIF;
	}

	public void setIdIF(int idIF) {
		this.idIF = idIF;
	}

	public boolean isTemELSE() {
		return temELSE;
	}

	public void setTemELSE(boolean temELSE) {
		this.temELSE = temELSE;
	}
	
	
}
