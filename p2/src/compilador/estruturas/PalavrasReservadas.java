package compilador.estruturas;

public final class PalavrasReservadas {
	
	private static String[] palavras = {"estrut", "principal", "retorno", "inteiro", "caracteres", "booleano",
			"entrada", "saida", "se", "senao", "enquanto", "verdadeiro", "falso"};
	
	
	public static boolean reservada (String palavra) {
		palavra.toLowerCase();
		for (int i = 0; i < palavras.length; i++) {
			if (palavras[i].compareTo(palavra) == 0) {
				return true;
			}
		}
		return false;
	}
	
}
