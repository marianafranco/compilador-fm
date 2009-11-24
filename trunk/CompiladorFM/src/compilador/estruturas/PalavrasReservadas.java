package compilador.estruturas;

public class PalavrasReservadas {
	
	private String[] palavras = {"estrut", "principal", "retorno", "inteiro", "caracteres", "booleano",
			"entrada", "saida", "se", "senao", "enquanto", "verdadeiro", "falso"};
	
	public PalavrasReservadas () {
		
	}
	
	public boolean reservada (String palavra) {
		
		palavra.toLowerCase();
		
		for (int i = 0; i < this.palavras.length; i++) {
			if (this.palavras[i].compareTo(palavra) == 0) {
				return true;
			}
		}
		return false;
	}
	
}
