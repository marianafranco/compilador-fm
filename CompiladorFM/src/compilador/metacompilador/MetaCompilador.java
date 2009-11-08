package compilador.metacompilador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;

import compilador.metacompilador.estruturas.APE;
import compilador.metacompilador.lexico.Lexico;
import compilador.exceptions.ArquivoNaoEcontradoException;


public class MetaCompilador {
	
	private Reader arquivoFonte;
	
	private APE automato;
	
	private MontaMetaAFD montador;
	//private PercorreMetaAFD simulador;
	
	
	//public MetaCompilador(String nomeArquivo) throws ArquivoNaoEcontradoException{
	public MetaCompilador(){
		
		//try{
		//	this.arquivoFonte = new FileReader(nomeArquivo);
		//}catch (Exception e){
		//	throw new ArquivoNaoEcontradoException("O arquivo " + nomeArquivo + " nao foi encontrado.");
		//}
		
		this.automato = new APE();
		this.montador = new MontaMetaAFD();
		//this.simulador = new PercorreMetaAFD();
	}
	
	
	public boolean executa() {
		
		try{
			// Monta o Automato Finito Deterministico
			boolean montadorOK = montador.executa(this.automato);
			
			if(montadorOK){
				
				// Lexico
				Lexico lex = new Lexico("gramatica.txt");
				boolean lexOK = lex.executa();
				
				// Simula Automato Finito Deterministico com o aquivo fonte de entrada
				//boolean simuladorOK = simulador.executa(this.automato, this.arquivoFonte, this.fluxoTokens, this.tabelaSimbolos);
				boolean simuladorOK = true;
				//fechaArquivoFonte();
				
				if(simuladorOK){
					return true;
				}else{
					return false;
				}
				
			}else{
				//fechaArquivoFonte();
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
