package compilador.estruturas;

public class Simbolo {

	private int id;
	private String nome;
	private int tipo;
	private int linha;
	private int coluna;
	public Simbolo proximo;
	
	public Simbolo (int id, String nome, int tipo, int coluna, int linha) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.coluna = coluna;
		this.linha = linha;
	}
	
	public int getId () {
		return this.id;
	}
	
	public int getTipo () {
		return this.tipo;
	}
	
	public String getNome () {
		return this.nome;
	}
	
	public Simbolo getproximo () {
		return this.proximo;
	}
}
