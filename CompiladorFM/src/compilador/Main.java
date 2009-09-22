package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

import compilador.lexico.Lexico;

public class Main {

	private static int debug = 1;
	
	
	/**
	 * Ponto de entrada do compilador.
	 * 
	 * @param args argumentos de linha de comando
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) {
		
		// Caso o numero de argumentos passados seja diferente de 2
		if(args.length != 1){ // TODO Depois mudar para 2
			System.out.println("[ERRO] Forma de uso: compilador <fonte> <objeto>");
		
		}else{
			debugMensagem("arquivo fonte: " + args[0]);
			String codFonte = args[0];
			
			try{
				Lexico lex = new Lexico(codFonte);
				lex.executa();
			}catch(Exception e){
				//e.printStackTrace();
			}
			
		}

	}
	
	
	/**
	 * M�todo para mostrar as mensagens de informa��o. Usa o atributo debug para
	 * determinar se essas informa��es ser�o mostradas ou n�o durante a execu��o
	 * do compilador. 
	 * 
	 * @param mensagem mensagem � ser mostrada
	 */
	public static void debugMensagem(String mensagem){
		if(debug == 1){
			System.out.println("[INFO] " + mensagem);
		}
	}

}
