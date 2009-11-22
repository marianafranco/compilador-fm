package compilador;

import java.io.FileNotFoundException;
import java.io.IOException;

import compilador.estruturas.FluxoTokens;
import compilador.estruturas.TabelaSimbolos;
import compilador.lexico.Lexico;
import compilador.metacompilador.MetaCompilador;
import compilador.estruturas.APE;
import compilador.semantico.PercorreAPE;

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
				TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();
				FluxoTokens fluxoTokens = new FluxoTokens();
				
				APE newAutomato = new APE();
				MetaCompilador meta = new MetaCompilador();
				meta.executa(newAutomato);
				
				Lexico lex = new Lexico(codFonte);
				lex.executa(tabelaSimbolos, fluxoTokens);
				
				PercorreAPE gerador = new PercorreAPE();
				gerador.geraCodigo(newAutomato, fluxoTokens, tabelaSimbolos);
				
			}catch(Exception e){
				//e.printStackTrace();
			}
			
		}

	}
	
	
	/**
	 * Método para mostrar as mensagens de informação. Usa o atributo debug para
	 * determinar se essas informações serão mostradas ou não durante a execução
	 * do compilador. 
	 * 
	 * @param mensagem mensagem à ser mostrada
	 */
	public static void debugMensagem(String mensagem){
		if(debug == 1){
			System.out.println("[INFO] " + mensagem);
		}
	}

}
