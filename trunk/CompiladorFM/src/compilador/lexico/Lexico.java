package compilador.lexico;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;

import compilador.exceptions.ArquivoNaoEcontradoException;
import compilador.lexico.estruturas.AFD;
import compilador.lexico.estruturas.FluxoTokens;
import compilador.lexico.estruturas.TabelaSimbolos;


public class Lexico {
	
	private Reader arquivoFonte;
	
	private AFD automato;
	
	private MontaAFD montador;
	private PercorreAFD simulador;
	
	private TabelaSimbolos tabelaSimbolos;
	private FluxoTokens fluxoTokens;
	
	
	
	public Lexico(String nomeArquivo) throws ArquivoNaoEcontradoException{
		
		try{
			this.arquivoFonte = new FileReader(nomeArquivo);
		}catch (Exception e){
			throw new ArquivoNaoEcontradoException("O arquivo " + nomeArquivo + " nao foi encontrado.");
		}
		
		this.automato = new AFD();
		this.montador = new MontaAFD();
		this.simulador = new PercorreAFD();
		this.tabelaSimbolos = new TabelaSimbolos();
		this.fluxoTokens = new FluxoTokens();
	}
	
	
	public boolean executa(){
		
		// Monta o Automato Finito Deterministico
		boolean montadorOK = montador.executa(this.automato);
		
		if(montadorOK){
			// Simula Automato Finito Deterministico com o aquivo fonte de entrada
			boolean simuladorOK = simulador.executa(this.automato, this.arquivoFonte, this.fluxoTokens, this.tabelaSimbolos);
			fechaArquivoFonte();
			
			if(simuladorOK){
				return true;
			}else{
				return false;
			}
			
		}else{
			fechaArquivoFonte();
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
