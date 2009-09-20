package compilador.lexico.estruturas;

public class TabelaSimbolos {

	private String nome[];
	private int tipo[];
	private int linha[];
	private int coluna[];
	
	public TabelaSimbolos () {
		
	}
	
	public void adicionaEntrada (int posicao, String nome, int tipo, int linha, int coluna) {
		
		this.nome[posicao] = nome;
		this.tipo[posicao] = tipo;
		this.linha [posicao] = linha;
		this.coluna [posicao] = coluna;
	}
	
	public int ultimaPosicao () {
		return this.nome.length;
	}
	
	
	
}
