package compilador.semantico;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;

import compilador.estruturas.PalavrasReservadas;
import compilador.estruturas.PilhaSeSenao;
import compilador.estruturas.TabelaFuncao;
import compilador.estruturas.TiposComando;
import compilador.estruturas.TiposLexico;
import compilador.estruturas.TiposSimbolos;
import compilador.estruturas.Token;
import compilador.estruturas.AFD;
import compilador.estruturas.TabelaSimbolos;
import compilador.estruturas.TiposPrograma;
import compilador.exceptions.SemanticoException;

public class Semantico {
	
	private String arquivo;
	private String output;
	
	public Semantico(String arquivoMVN) {
		this.arquivo = arquivoMVN;
		this.output = "";
	}
	
	public void geraCodigo(Token token, Token nextToken, String aPercorrer, AFD submaquina) throws SemanticoException{
		
		System.out.println("[DEBUG] Token: " + token.getValor() + " submaquina: " + submaquina.getNome());
		//System.out.println("[DEBUG] nextToken: " + nextToken.getValor());
		
		
		
	}
	
	public void criaArquivo(){
		try{
			FileWriter writer = new FileWriter(this.arquivo);
			PrintWriter saida = new PrintWriter(writer);
				
			// imprime na tela
			System.out.println("ARQUIVO DE SAIDA\n"+ this.output);
			saida.print(this.output);
			
			// fecha o arquivo
			saida.close();
			writer.close(); 
		}catch(Exception e){
			System.out.println("[ERRO] Foi impossivel criar o arquivo de saida.");
			e.printStackTrace();
		}
	}
}