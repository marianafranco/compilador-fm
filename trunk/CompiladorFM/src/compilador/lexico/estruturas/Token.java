package compilador.lexico.estruturas;

public class Token {
	
	private String valor;
	private int indice;
	public Token proximo;
	
	public Token (String valor, int indice) {
		this.valor = valor;
		this.indice = indice;
	}
}
