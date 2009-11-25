package compilador;

import java.io.FileNotFoundException;

import compilador.estruturas.FluxoTokens;
import compilador.estruturas.TabelaSimbolos;
import compilador.lexico.Lexico;
import compilador.metacompilador.MetaCompilador;
import compilador.estruturas.APE;
import compilador.exceptions.ArquivoNaoEcontradoException;
import compilador.semantico.PercorreAPE;

/**
 * Main: Controla a execu��o do compilador.
 * 
 * @author Felipe Yoshida, Mariana R. Franco
 *
 */
public class Main {
	
	/**
	 * flag que indica se as mensagens de debug ser�o monstradas ou n�o.
	 */
	private static int debug = 1;	
	
	
	/**
	 * Ponto de entrada do compilador.
	 * 
	 * @param args argumentos de linha de comando
	 */
	public static void main(String[] args) {
		
		// Caso o numero de argumentos passados seja diferente de 2
		if(args.length != 1){ // TODO Depois mudar para 2
			System.out.println("[ERRO] Forma de uso: compilador <fonte> <objeto>");
		
		}else{
			debugMensagem("arquivo fonte: " + args[0]);
			String codFonte = args[0];
			
			try{
				// Cria a tabela de simbolos e o fluxo de tokens
				TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();
				FluxoTokens fluxoTokens = new FluxoTokens();
				
				// Cria o aut�mato de pilha estruturado da linguagem
				APE newAutomato = new APE();
				MetaCompilador meta = new MetaCompilador();
				meta.executa(newAutomato);
				
				// Executa o l�xico, preenchendo a tabela de simbolos e o
				// fluxo de tokens.
				Lexico lex = new Lexico(codFonte);
				lex.executa(fluxoTokens);
				
				// Percorre o aut�mato gerando o c�digo em MVN
				PercorreAPE gerador = new PercorreAPE();
				gerador.executa(newAutomato, fluxoTokens);
				
			}catch(ArquivoNaoEcontradoException e){
				System.out.println("[INFO] Compilador finalizador com ERROS.");
			}catch(Exception e){
				e.printStackTrace();
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
