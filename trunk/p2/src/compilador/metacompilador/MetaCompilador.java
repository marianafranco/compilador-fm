package compilador.metacompilador;

import java.io.Reader;

import compilador.estruturas.APE;
import compilador.metacompilador.lexico.Lexico;

/**
 * MetaCompilador: Responsável por criar o autômato de pilha estruturado da linguagem FM
 * a partir da sua descrição em notação de Wirth.
 * 
 * @author Felipe Yoshida, Mariana R. Franco
 *
 */
public class MetaCompilador {
	
	/**
	 * arquivo texto que contém a descrição em notação de Wirth para a linguagem FM.
	 */
	private static String arquivoFonte = "conf/gramatica.txt";
	
	/**
	 * reconhecedor para a notação de Wirth.
	 */
	private APE automato;
	
	/**
	 * monta o reconhecedor para a notação de Wirth.
	 */
	private MontaMetaAPE montador;
	
	/**
	 * percorre o reconhecedor para criar um APE da linguagem FM. 
	 */
	private PercorreMetaAPE simulador;
	
	
	/**
	 * Método construtor.
	 */
	public MetaCompilador(){
		this.automato = new APE();
		this.montador = new MontaMetaAPE();
		this.simulador = new PercorreMetaAPE();
	}
		
	/**
	 * Cria o APE da linguagem FM a partir da sua descrição na notação de Wirth. 
	 * @param newAutomato	objeto APE que irá descrever a linguagem FM.
	 * @return true/false dependendo do sucesso na criação do APE para a linguagem FM.
	 */
	public boolean executa(APE newAutomato) {
		
		try{
			// Monta o Automato de Pilha Estruturado
			boolean montadorOK = montador.executa(this.automato);
			
			if(montadorOK){
				
				// Lexico
				Lexico lex = new Lexico(arquivoFonte);
				boolean lexOK = lex.executa();
				
				if(lexOK){
					// Simula o Automato de Pilha Estruturado
					boolean simuladorOK = simulador.executa(this.automato, lex.getFluxoTokens(), newAutomato);
					
					newAutomato.minimiza();
					//newAutomato.imprime();
					newAutomato.imprime_desenho();
					
					if(simuladorOK){
						return true;
					}else{
						return false;
					}
					
				}else{
					return false;
				}
			
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}

}
