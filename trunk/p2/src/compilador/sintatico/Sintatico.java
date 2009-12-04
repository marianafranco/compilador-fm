package compilador.sintatico;

import java.util.Stack;


import compilador.estruturas.APE;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.Token;
import compilador.estruturas.PilhaEstadoSubmaquina;
import compilador.estruturas.AFD;
import compilador.exceptions.SemanticoException;
import compilador.semantico.Semantico;

public class Sintatico {
	
	/**
	 * pilha usada para as chamadas de subm�quina
	 */
	private Stack<PilhaEstadoSubmaquina> pilhaSubmaquinas;
	
	
	/**
	 * Classe usada para executar as a��es semanticas
	 */
	private Semantico semantico;
	
	
	public Sintatico(String arquivo) {
		pilhaSubmaquinas = new Stack<PilhaEstadoSubmaquina>();
		semantico = new Semantico(arquivo);
	}
	
	
	// Verifica se ha em alguma transicao em um dos nao terminais 
	private boolean temTransicao (String token, AFD submaquina) {
		
		submaquina.setEstadoAtivo(0);
		
		if (submaquina.temTransicao('"' + token + '"')) {
			return true;
		}
		if (submaquina.temTransicao("Expr")) {
			if (temTransicao(token, submaquina))
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
		AFD progr = automato.getSubmaquina("Program");
		AFD expr = automato.getSubmaquina("Expr");
		
		// Submaquina e pilha
		AFD submaquina;
		PilhaEstadoSubmaquina conteudoPilha;
		
		// Inicializa pilha
		pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(0, "Program"));
		
		// Pega primeiro token
		token = tokensTokens.recuperaToken();
		nextToken = token;
		
		boolean fimTokens = false;
		boolean fimTokensEFinal = true;
				
		// Enquanto n�o chegamos ao final dos tokens
		while(fimTokensEFinal) {
			
			if(pilhaSubmaquinas.empty()){
				// ERRO
				System.out.println("[ERRO] Caractere inesperado '" + token.getValor() + "' na linha " + token.getLinha() + ".");
				return false;
			}
			
			// Desempilha maquina
			conteudoPilha = (PilhaEstadoSubmaquina) pilhaSubmaquinas.pop();
			//System.out.println("desempilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
			//System.out.println("token: " + token.getValor());
			
			// Identifica qual a maquina que estava empilhada
			if(conteudoPilha.getSubmaquina().equals("Program")){
				submaquina = progr;
			}
			else if(conteudoPilha.getSubmaquina().equals("Expr")){
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
					
				}else{
					fimTokens = true;
				}
				
			// Se chamada para subm�quina "Expr"
			}else if (submaquina.temTransicao("Expr") && temTransicao(token.getValor(), expr)) {
				possiveisTransicoes++;
				aPercorrer = "Expr";
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
					// Gera c�digo
					semantico.geraCodigo(token, aPercorrer);
				}catch(SemanticoException e){
					System.out.println("[INFO] Compilador finalizado com ERRO.");
					return false;
				}
				
				
				// Caso seja necessario, pega um novo token
				if (getNewToken == true) {
					token = nextToken;
				}
				
				// Empilha submaquina, caso ela nao tenha terminado
				if (!submaquina.estadoAtivoFinal() || submaquina.temTransicao('"' + token.getValor() + '"') || aPercorrer == "Expr") {
					pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
					
					//System.out.println("empilha: (" + pilhaSubmaquinas.peek().getEstado() + ", " 
					//		+ pilhaSubmaquinas.peek().getSubmaquina() + ")");
					
					// Empilha nova maquina, caso transicao de um nao-terminal
					if (aPercorrer == "Program" || aPercorrer == "Expr" ) {
						pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(0, aPercorrer));
						
						//System.out.println("empilha: (" + pilhaSubmaquinas.peek().getEstado() + ", " 
						//		+ pilhaSubmaquinas.peek().getSubmaquina() + ")");
					}
				}
				
			// Caso haja mais de uma possivel transicao ou nenhuma, ha um erro
			}else {
				System.out.println("[ERRO] Caractere inesperado '" + token.getValor() + "' na linha " + token.getLinha() + ".");
				return false;
			}
			
			// Verifica se a pilha esta vazia e o estado � final
			if (tokensTokens.getTamanho() == 0 && submaquina.estadoAtivoFinal() && pilhaSubmaquinas.empty()) {
				fimTokensEFinal = false;
			
			}else if(fimTokens && pilhaSubmaquinas.peek().getSubmaquina().equals("Program")){
				fimTokensEFinal = false;
			}
			
		}
		
		System.out.println("[INFO] Codigo aceito!");
		semantico.criaArquivo();
		
		return true;
	}
	
}