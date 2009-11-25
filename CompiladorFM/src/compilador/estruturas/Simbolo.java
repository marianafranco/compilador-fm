package compilador.estruturas;

public class Simbolo {

	private int id;
	private String nome;
	private int tipo;
	private int linha;
	private int coluna;
	public Simbolo proximo;
	
	//atributos utilizados para delimitar escopo
	private int escopoProximo;
	private int escopoAnterior;
	
	
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

	public int getEscopoProximo() {
		return escopoProximo;
	}

	public void setEscopoProximo(int escopoProximo) {
		this.escopoProximo = escopoProximo;
	}

	public int getEscopoAnterior() {
		return escopoAnterior;
	}

	public void setEscopoAnterior(int escopoAnterior) {
		this.escopoAnterior = escopoAnterior;
	}
	
	
}
