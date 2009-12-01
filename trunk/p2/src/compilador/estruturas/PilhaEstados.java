package compilador.estruturas;

public class PilhaEstados {
	
	int L;	// estado de entrada
	int R;	// estado de saida
	
	public PilhaEstados(int L, int R){
		this.L = L;
		this.R = R;
	}

	
	// Gets e Sets
	
	public int getL() {
		return L;
	}

	public void setL(int l) {
		L = l;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

}
