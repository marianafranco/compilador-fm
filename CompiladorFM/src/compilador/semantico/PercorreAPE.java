package compilador.semantico;

import java.util.Stack;
import java.util.Vector;

import compilador.estruturas.PalavrasReservadas;
import compilador.estruturas.PilhaEstados;
import compilador.estruturas.APE;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.TiposLexico;
import compilador.estruturas.TiposSimbolos;
import compilador.estruturas.Token;
import compilador.estruturas.PilhaEstadoSubmaquina;
import compilador.estruturas.AFD;
import compilador.estruturas.TabelaSimbolos;

public class PercorreAPE {
	
	private Stack<PilhaEstados> pilhaGeraAPE;
	private int cs;								// estado corrente
	private int ns;								// proximo estado
	
	/**
	 * pilha usada para as chamadas de submáquina
	 */
	private Stack<PilhaEstadoSubmaquina> pilhaSubmaquinas;
	
	/**
	 * pilha que guarda a tabela de simbolos atual
	 */
	private Vector<TabelaSimbolos> vetorEscopos;
	
	public PercorreAPE() {
		this.pilhaGeraAPE = new Stack<PilhaEstados>();
		this.cs = 0;
		this.ns = 1;
		
		pilhaSubmaquinas = new Stack<PilhaEstadoSubmaquina>();
		vetorEscopos = new Vector<TabelaSimbolos>();
	}
	
	// Verifica se ha em alguma transicao em um dos nao terminais 
	private boolean temTransicao (String token, int tokenTipo, AFD submaquina, APE automato) {
		
		submaquina.setEstadoAtivo(0);
		AFD comando = automato.getSubmaquina("comando");
		AFD expr = automato.getSubmaquina("expressao");
		AFD tipo = automato.getSubmaquina("tipo");
		AFD booleano = automato.getSubmaquina("booleano");
		
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
		if (submaquina.temTransicao("tipo")) {
			if (temTransicao (token, tokenTipo, tipo, automato))
				return true;
		}
		if (submaquina.temTransicao("booleano")) {
			if (temTransicao (token, tokenTipo, booleano, automato))
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
		
		// inicializa a tabela de simbolos
		TabelaSimbolos tabela = new TabelaSimbolos();
		vetorEscopos.add(tabela);
		
		if(tokensTokens.getTamanho() < 2){
			System.out.println("[ERRO] Arquivo fonte incorreto.");
			return false;
		}
		
		// Pega primeiro token
		token = tokensTokens.recuperaToken();
		
		// Submaquinas temporarias
		AFD progr = automato.getSubmaquina("programa");
		AFD comando = automato.getSubmaquina("comando");
		AFD expr = automato.getSubmaquina("expressao");
		AFD tipo = automato.getSubmaquina("tipo");
		AFD booleano = automato.getSubmaquina("booleano");
		
		// Submaquina e pilha
		AFD submaquina;
		PilhaEstadoSubmaquina conteudoPilha; 
		
		// Inicializa pilha
		pilhaSubmaquinas.push(new PilhaEstadoSubmaquina(0, "programa"));
		
		boolean fimTokensEFinal = true;
		
		// Enquanto não chegamos ao final dos tokens
		while(fimTokensEFinal) {
			
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
			else if(conteudoPilha.getSubmaquina().equals("tipo")){
				submaquina = tipo;
			}
			else if(conteudoPilha.getSubmaquina().equals("booleano")){
				submaquina = booleano;
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
			
			// Se chamada para submáquina "tipo"
			}else if (submaquina.temTransicao("tipo") && temTransicao (token.getValor(), token.getTipo(), tipo, automato)) {
				possiveisTransicoes++;
				aPercorrer = "tipo";
			
			// Se chamada para submáquina "booleano"
			}else if (submaquina.temTransicao("booleano") && temTransicao (token.getValor(), token.getTipo(), booleano, automato)) {
				possiveisTransicoes++;
				aPercorrer = "booleano";
			
			// Se identificador
			}else if (submaquina.temTransicao("identificador") && token.getTipo() == TiposLexico.NOME) {
				possiveisTransicoes++;
				aPercorrer = "identificador";
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
				
				// Adiciona na tabela de simbolos
				// Se não é palavra reservada
				if(!PalavrasReservadas.reservada(token.getValor())){
					// Verifica se o nome ja esta na tabela de simbolos
					if(!tabela.estaNaTabela(token.getValor())){
						System.out.println("SIMBOLO = " + token.getValor());
						tabela.adicionaEntrada(token.getValor(), TiposSimbolos.DESCONHECIDO, token.getLinha(), token.getColuna());
					}
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
			
			
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			// Caso haja apenas uma transicao possivel, realiza a mesma
			if (possiveisTransicoes == 1) {
				// Percorre submaquina
				submaquina.percorre(aPercorrer);
				// Salva estado da submaquina
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				
				// Gera código
				geraCodigo(token, aPercorrer, tabela, submaquina);
				
				// Caso seja necessario, pega um novo token
				if (getNewToken == true) {
					token = tokensTokens.recuperaToken();
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
		return true;
	}
	
	
	public void geraCodigo(Token token, String aPercorrer, TabelaSimbolos tabela, AFD submaquina){
		
		// Se submaquina <programa>
		if(submaquina.getNome().equals("programa")){
			
		
		// Se submaquina <comando>
		}else if(submaquina.getNome().equals("comando")){
			
		
		// Se submaquina <expressao>
		}else if(submaquina.getNome().equals("expressao")){
			
		
		// Se submaquina <tipo>
		}else if(submaquina.getNome().equals("tipo")){
			
			
		// Se submaquina <booleano>
		}else if(submaquina.getNome().equals("booleano")){
			
		}
	}
	
}