package compilador.lexico.exceptions;

public class ArquivoNaoEcontradoException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public ArquivoNaoEcontradoException() {
	}
	
	public ArquivoNaoEcontradoException(String message) {
		//super(message);
		System.out.println("[ERRO] " + message);
	}

}
