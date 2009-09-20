package compilador.lexico.estruturas;

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
	
	public int proximoEstado (char entrada) {
		
		int proximo = -1;
		
		for (int i = 0; this.entrada.length > i; i++) {
			if (this.entrada[i] == entrada) {
				proximo = this.proximo;
			}
		}
		return proximo;
	}
	
	
}
