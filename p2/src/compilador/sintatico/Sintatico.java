package compilador.sintatico;

import java.util.Stack;


import compilador.estruturas.APE;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.TiposLexico;
import compilador.estruturas.Token;
import compilador.estruturas.PilhaEstadoSubmaquina;
import compilador.estruturas.AFD;
import compilador.exceptions.SemanticoException;
import compilador.semantico.Semantico;

public class Sintatico {
	
	/**
	 * pilha usada para as chamadas de submáquina
	 */
	private Stack<PilhaEstadoSubmaquina> pilhaSubmaquinas;
	
	
	/**
	 * Classe usada para executar as ações semanticas
	 */
	private Semantico semantico;
	
	
	public Sintatico(String arquivo) {
		pilhaSubmaquinas = new Stack<PilhaEstadoSubmaquina>();
		semantico = new Semantico(arquivo);
	}
	
	
	// Verifica se ha em alguma transicao em um dos nao terminais 
	private boolean temTransicao (String token, int tokenTipo, AFD submaquina, APE automato) {
		
		submaquina.setEstadoAtivo(0);
		AFD comando = automato.getSubmaquina("comando");
		AFD expr = automato.getSubmaquina("expressao");
		
		if (submaquina.temTransicao('"' + token + '"')) {
			return true;
		}
		if (submaquina.temTransicao("comando")) {
			if (temTransicao (token, tokenTipo, comando, automato))
				return true;
		}
		if (submaquina.temTransicao("expressao")) {
			if (temTransicao (token, tokenTipo, expr, automato))
				return true;
		}
		if (submaquina.temTransicao("identificador") && tokenTipo == TiposLexico.NOME) {
			return true;
		}
		if (submaquina.temTransicao("numero") && tokenTipo == TiposLexico.NUMERO) {
			return true;
		}
		if (submaquina.temTransicao("string") && tokenTipo == TiposLexico.STRING) {
			return true;
		}
		
		return false;
	}
	
	
	
	public boolean executa(APE automato, FluxoTokens tokensTokens) {
		
		Token token;
		Token nextToken;
		
		if(tokensTokens.getTamanho() < 2){
			System.out.println("[ERRO] Arquivo fonte incorreto.");
			return false;
		}
		
		
		// Submaquinas temporarias
		AFD progr = automato.getSubmaquina("programa");
		AFD comando = automato.getSubmaquina("comando");
		AFD expr = automato.getSubmaquina("expressao");
		
		// Submaquina e pilha
		AFD submaquina;
		PilhaEstadoSubmaquina conteudoPilha;
		
		// Inicializa pilha
		pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(0, "programa"));
		
		
		// Pega primeiro token
		token = tokensTokens.recuperaToken();
		nextToken = token;
		
		boolean fimTokensEFinal = true;
		
		
		// Enquanto não chegamos ao final dos tokens
		while(fimTokensEFinal) {
			
			if(pilhaSubmaquinas.empty()){
				// ERRO
				System.out.println("[ERRO] Caractere inesperado '" + token.getValor() + "' na linha " + token.getLinha() + ".");
				return false;
			}
			
			// Desempilha maquina
			conteudoPilha = (PilhaEstadoSubmaquina) pilhaSubmaquinas.pop();
			//System.out.println("pilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
			//System.out.println("token: " + token.getValor());
			
			// Identifica qual a maquina que estava empilhada
			if(conteudoPilha.getSubmaquina().equals("programa")){
				submaquina = progr;
			}
			else if(conteudoPilha.getSubmaquina().equals("comando")){
				submaquina = comando;
			}
			else if(conteudoPilha.getSubmaquina().equals("expressao")){
				submaquina = expr;
			}
			else{
				// TODO Acrescentar mensagem de erro 
				return false;
			}
			
			// Seta o estado ativo da maquina
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			
			// Variaveis de identificacao
			boolean getNewToken = false;
			String aPercorrer = "";
			int possiveisTransicoes = 0;
			
			// Verifica quais as possiveis transicoes a partir do estado atual
			// Se terminal
			if (submaquina.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = '"' + token.getValor() + '"';
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
				
			// Se chamada para submáquina "comando"
			}else if (submaquina.temTransicao("comando") && temTransicao(token.getValor(), token.getTipo(), comando, automato)) {
				possiveisTransicoes++;
				aPercorrer = "comando";
			
			// Se chamada para submáquina "expressao"
			}else if (submaquina.temTransicao("expressao") && temTransicao (token.getValor(), token.getTipo(), expr, automato)) {
				possiveisTransicoes++;
				aPercorrer = "expressao";

			}else if (submaquina.temTransicao("identificador") && token.getTipo() == TiposLexico.NOME) {
				possiveisTransicoes++;
				aPercorrer = "identificador";
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			
			// Se número
			}else if (submaquina.temTransicao("numero") && token.getTipo() == TiposLexico.NUMERO) {
				possiveisTransicoes++;
				aPercorrer = "numero";
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			
			// Se string
			}else if (submaquina.temTransicao("string") && token.getTipo() == TiposLexico.STRING) {
				possiveisTransicoes++;
				aPercorrer = "string";
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			}
			
			// seta novamente o estado ativo da submaquina
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			// Caso haja apenas uma transicao possivel, realiza a mesma
			if (possiveisTransicoes == 1) {
				// Percorre submaquina
				submaquina.percorre(aPercorrer);
				// Salva estado da submaquina
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				
				// Caso seja necessario, pega um novo token
				if (getNewToken == true) {
					nextToken = tokensTokens.recuperaToken();
				}
				
				try{
					// Gera código
					semantico.geraCodigo(token, nextToken, aPercorrer, submaquina);
				}catch(SemanticoException e){
					System.out.println("[INFO] Compilador finalizado com ERRO.");
					return false;
				}
				
				
				// Caso seja necessario, pega um novo token
				if (getNewToken == true) {
					token = nextToken;
				}
				
				// Empilha submaquina, caso ela nao tenha terminado
				if (!submaquina.estadoAtivoFinal() || submaquina.temTransicao('"' + token.getValor() + '"')) {
					pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
					
					// Empilha nova maquina, caso transicao de um nao-terminal
					if (aPercorrer == "programa" || aPercorrer == "comando" || aPercorrer == "expressao" || 
							aPercorrer == "tipo" || aPercorrer == "booleano") {
						pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(0, aPercorrer));
					}
				}
				
			// Caso haja mais de uma possivel transicao ou nenhuma, ha um erro
			}else {
				System.out.println("[ERRO] Caractere inesperado '" + token.getValor() + "' na linha " + token.getLinha() + ".");
				return false;
			}
			
			// Verifica se a pilha esta vazia e o estado é final
			if (tokensTokens.getTamanho() == 0 && submaquina.estadoAtivoFinal() && pilhaSubmaquinas.empty()) {
				fimTokensEFinal = false;
			}
			
		}
		
		System.out.println("[INFO] Codigo aceito!");
		
		semantico.criaArquivo();
		
		return true;
	}
	
}