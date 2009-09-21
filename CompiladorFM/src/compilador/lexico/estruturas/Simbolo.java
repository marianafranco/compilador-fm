package compilador.lexico.estruturas;

public class Simbolo {

	private int id;
	private String nome;
	private int tipo;
	private int linha;
	private int coluna;
	public Simbolo proximo;
	
	public Simbolo (int id, String nome, int tipo, int coluna, int linha) {
		this.nome = nome;
		this.tipo = tipo;
		this.coluna = coluna;
		this.linha = linha;
	}
}
