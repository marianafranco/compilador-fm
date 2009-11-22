package compilador.estruturas;

public class PalavrasReservadas {
	
	private String[] palavras = {"estrut", "principal", "retorno", "inteiro", "se", "entrada", "saida", "senao"};
	
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
