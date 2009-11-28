package compilador.estruturas;

public class Simbolo {

	private int id;
	private String nome;
	private int tipo;
	private int linha;
	private int coluna;
	public Simbolo proximo;
	
	private String endereco;
	private int tamanho;
	
	
	public Simbolo (int id, String nome, int tipo, int coluna, int linha, String end, int tamanho) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.coluna = coluna;
		this.linha = linha;
		this.endereco = end;
		this.tamanho = tamanho;
	}
	
	
	public boolean jaDeclarado () {
		if (this.endereco == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	public int getId () {
		return this.id;
	}
	
	public int getTipo () {
		return this.tipo;
	}
	
	public void setTipo(int tipo){
		this.tipo = tipo;
	}
	
	public String getNome () {
		return this.nome;
	}
	
	public Simbolo getProximo () {
		return this.proximo;
	}
	
	public String getEndereco () {
		return this.endereco;
	}
	
	public void setEndereco (String posicao) {
		this.endereco = posicao;
	}


	public int getTamanho() {
		return tamanho;
	}


	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	
	
}
