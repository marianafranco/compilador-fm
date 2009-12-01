package compilador.estruturas;

import java.util.Vector;

public class Funcao {

	private String nome;
	
	private int tipo;
	
	private String end;
	
	private int numParam;
	
	private Vector<Simbolo> vetorParams;
	
	
	public Funcao(String nome, int tipo, String end){
		this.nome = nome;
		this.tipo = tipo;
		this.end = end;
		this.numParam = 0;
		vetorParams = new Vector<Simbolo>();
		
	}

	
	public void insereParam(String nome, int tipo, String end){
		Simbolo simbolo = new Simbolo(this.numParam, nome, tipo, 0, 0, end, 0);
		this.vetorParams.add(simbolo);
	}
	
	
	
	
	
	// Gets e Sets

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public int getTipo() {
		return tipo;
	}


	public void setTipo(int tipo) {
		this.tipo = tipo;
	}


	public int getNumParam() {
		return numParam;
	}


	public void setNumParam(int numParam) {
		this.numParam = numParam;
	}


	public String getEnd() {
		return end;
	}


	public void setEnd(String end) {
		this.end = end;
	}
	
	
	
}
