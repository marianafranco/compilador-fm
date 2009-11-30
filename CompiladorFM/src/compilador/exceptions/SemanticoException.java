package compilador.exceptions;

public class SemanticoException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public SemanticoException() {
	}
	
	public SemanticoException(String messagem, int linha) {
		//super(message);
		System.out.println("[ERRO] " + messagem + "(linha " + linha + ")");
	}
}
