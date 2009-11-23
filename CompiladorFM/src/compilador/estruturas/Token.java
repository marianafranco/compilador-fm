package compilador.estruturas;

public class Token {
	
	private String valor;
	private int tipo;
	private int linha;
	private int coluna;
	
	public Token (String valor, int tipo) {
		this.valor = valor;
		this.tipo = tipo;
	}
	
	public Token (String valor, int tipo, int linha, int coluna) {
		this.valor = valor;
		this.tipo = tipo;
		this.linha = linha;
		this.coluna = coluna;
	}

	// Gets e Sets
	
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public int getLinha(){
		return this.linha;
	}
	
}
