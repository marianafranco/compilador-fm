package compilador.lexico.estruturas;

public class Token {
	
	private String valor;
	private int indice;
	private int linha;
	private int coluna;
	public Token proximo;
	
	public Token (String valor, int indice) {
		this.valor = valor;
		this.indice = indice;
	}
	
	public Token (String valor, int tipo, int linha, int coluna) {
		this.valor = valor;
		this.indice = tipo;
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public String getValor() {
		return this.valor;
	}
	
	public int getIndice() {
		return this.indice;
	}
}
