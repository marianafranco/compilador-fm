package compilador.lexico.estruturas;

public class FluxoTokens {

	private String nome[];
	private int valorAtributo[];
	
	public FluxoTokens () {
		
	}
	
	public void adicionaToken (String nome, int valor) {
		
		System.out.println("TOKEN: " + nome);
		
		int ultimaPosicao = this.valorAtributo.length;
		
		this.nome[ultimaPosicao] = nome;
		this.valorAtributo[ultimaPosicao] = valor;
	}
	
}
