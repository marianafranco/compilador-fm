package compilador.lexico;

import java.io.FileReader;
import java.io.Reader;

import compilador.estruturas.AFD;
import compilador.estruturas.FluxoTokens;
import compilador.exceptions.ArquivoNaoEcontradoException;


public class Lexico {
	
	private Reader arquivoFonte;
	private AFD automato;
	private MontaAFD montador;
	private PercorreAFD simulador;
	
	
	
	public Lexico(String nomeArquivo) throws ArquivoNaoEcontradoException{
		
		try{
			this.arquivoFonte = new FileReader(nomeArquivo);
		}catch (Exception e){
			throw new ArquivoNaoEcontradoException("O arquivo " + nomeArquivo + " nao foi encontrado.");
		}
		
		this.automato = new AFD();
		this.montador = new MontaAFD();
		this.simulador = new PercorreAFD();
	}
	
	
	public boolean executa(FluxoTokens fluxoTokens){
		
		// Monta o Automato Finito Deterministico
		boolean montadorOK = montador.executa(this.automato);
		
		if(montadorOK){
			// Simula Automato Finito Deterministico com o aquivo fonte de entrada
			boolean simuladorOK = simulador.executa(this.automato, this.arquivoFonte, fluxoTokens);
			fechaArquivoFonte();
			
			return simuladorOK;
			
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
