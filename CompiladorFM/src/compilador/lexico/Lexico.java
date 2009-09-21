package compilador.lexico;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;

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
	
	
	
	public Lexico(String nomeArquivo) throws FileNotFoundException{
		
		this.arquivoFonte = new FileReader(nomeArquivo);

		this.automato = new AFD();
		this.montador = new MontaAFD();
		this.simulador = new PercorreAFD();
		this.tabelaSimbolos = new TabelaSimbolos();
		this.fluxoTokens = new FluxoTokens();
		//this.arquivoFonte.close();
	}
	
	public boolean executa(){
		
		// Monta o Automato Finito Deterministico
		montador.executa(this.automato);
		
		
		
		// Simula Automato Finito Deterministico com o aquivo fonte de entrada
		simulador.executa(this.automato, this.arquivoFonte, this.fluxoTokens, this.tabelaSimbolos);
		
		try{
			this.arquivoFonte.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	

}
