package compilador.exceptions;

public class CaractereInvalidoException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public CaractereInvalidoException() {
	}
	
	public CaractereInvalidoException(String message) {
		//super(message);
		System.out.println("[ERRO] " + message);
	}

}
