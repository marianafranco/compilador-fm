package compilador.estruturas;

public class Transicao {

	private int proximo;
	private char[] entrada;
	
	public Transicao(int proximo, String entrada) {
		this.proximo = proximo;
		this.entrada = new char[entrada.length()];
		for (int i = 0; i < entrada.length(); i++) {
			this.entrada[i] = entrada.charAt(i); 
		}
	}
	
	public int proximoEstado(char entrada) {
		
		int proximo = -1;
		
		for (int i = 0; this.entrada.length > i; i++) {
			if (this.entrada[i] == entrada) {
				proximo = this.proximo;
			}
		}
		return proximo;
	}
	
	
	public int proximoEstado(String entrada){
		
		int proximo = -1;
		String transicao =  new String(this.entrada);
		
		if(transicao.equals(entrada)){
			proximo = this.proximo;
		}
		
		return proximo;
	}
	
	
	// Gets e Sets
	
	public void setProximo(int proximo) {
		this.proximo = proximo;
	}

	public void setEntrada(char[] entrada) {
		this.entrada = entrada;
	}

	public int getProximo(){
		return this.proximo;
	}
	
	public String getEntrada(){
		return new String(this.entrada);
	}
	
}
