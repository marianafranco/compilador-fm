package compilador.lexico.estruturas;

public class PalavrasReservadas {
	
	private String[] palavras = {"se", "entao", "enquanto", "int", "flut"};
	
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
