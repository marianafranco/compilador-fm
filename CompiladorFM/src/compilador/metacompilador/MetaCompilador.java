package compilador.metacompilador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;

import compilador.estruturas.APE;
import compilador.metacompilador.lexico.Lexico;
import compilador.exceptions.ArquivoNaoEcontradoException;


public class MetaCompilador {
	
	private Reader arquivoFonte;
	private APE automato;
	private MontaMetaAPE montador;
	private PercorreMetaAPE simulador;
	
	
	//public MetaCompilador(String nomeArquivo) throws ArquivoNaoEcontradoException{
	public MetaCompilador(){
		
		//try{
		//	this.arquivoFonte = new FileReader(nomeArquivo);
		//}catch (Exception e){
		//	throw new ArquivoNaoEcontradoException("O arquivo " + nomeArquivo + " nao foi encontrado.");
		//}
		
		this.automato = new APE();
		this.montador = new MontaMetaAPE();
		this.simulador = new PercorreMetaAPE();
	}
	
	
	public boolean executa(APE newAutomato) {
		
		try{
			// Monta o Automato Finito Deterministico
			boolean montadorOK = montador.executa(this.automato);
			
			if(montadorOK){
				
				// Lexico
				Lexico lex = new Lexico("gramatica.txt");
				boolean lexOK = lex.executa();
				
				if(lexOK){
					// Simula o Automato de Pilha Estruturado
					boolean simuladorOK = simulador.executa(this.automato, lex.getFluxoTokens(), newAutomato);
					
					newAutomato.minimiza();
					System.out.println("## Automato ##");
					newAutomato.imprime();
					
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
	
	
	public void fechaArquivoFonte(){
		try{
			this.arquivoFonte.close();
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("[ERRO] Impossivel fechar o arquivo fonte.");
		}
	}

}
