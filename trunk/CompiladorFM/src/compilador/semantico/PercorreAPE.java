package compilador.semantico;

import java.util.Stack;
import compilador.estruturas.PilhaEstados;
import compilador.estruturas.APE;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.TiposLexico;
import compilador.estruturas.TiposMeta;
import compilador.estruturas.Token;
import compilador.estruturas.PilhaEstadoSubmaquina;
import compilador.estruturas.AFD;
import compilador.estruturas.TabelaSimbolos;
import compilador.estruturas.TiposLexico;

public class PercorreAPE {
	
	private Stack<PilhaEstados> pilhaGeraAPE;
	private int cs;								// estado corrente
	private int ns;								// proximo estado
	
	public PercorreAPE() {
		this.pilhaGeraAPE = new Stack<PilhaEstados>();
		this.cs = 0;
		this.ns = 1;
	}
	
	// Verifica se ha em alguma transicao em um dos nao terminais 
	private boolean temTransicao (String token, int tokenTipo, AFD submaquina, APE automato) {
		
		submaquina.setEstadoAtivo(0);
		AFD progr = automato.getSubmaquina("programa");
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
		if (submaquina.temTransicao("identificador") && tokenTipo > 0) {
			return true;
		}
		if (submaquina.temTransicao("numero") && tokenTipo == -2) {
			return true;
		}
		
		return false;
	}
	
	
	public boolean geraCodigo(APE automato, FluxoTokens tokensTokens, TabelaSimbolos tabelaDeSimbolos) {
		
		Token token;
		Stack pilha = new Stack<PilhaEstadoSubmaquina>();
		
		if(tokensTokens.getTamanho() < 2){
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
		pilha.push(new PilhaEstadoSubmaquina(0, "programa"));
		
		boolean fimTokensEFinal = true;
		
		// Enquanto não chegamos ao final dos tokens
		while(fimTokensEFinal) {
			
			// Desempilha m‡quina
			conteudoPilha = (PilhaEstadoSubmaquina) pilha.pop();
			
			// Identifica qual a m‡quina que estava empilhada
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
			
			// Seta o estado ativo da m‡quina e zera o estado das outras maquinas
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			
			// Vari‡veis de identificacao
			boolean getNewToken = false;
			String aPercorrer = "";
			int possiveisTransicoes = 0;
			
			// Verifica quais as possiveis transicoes a partir do estado atual
			if (submaquina.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = '"' + token.getValor() + '"';
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			}
			if (submaquina.temTransicao("comando") && temTransicao (token.getValor(), token.getTipo(), comando, automato)) {
				possiveisTransicoes++;
				aPercorrer = "comando";
			}
			if (submaquina.temTransicao("expressao") && temTransicao (token.getValor(), token.getTipo(), expr, automato)) {
				possiveisTransicoes++;
				aPercorrer = "expressao";
			}
			if (submaquina.temTransicao("tipo") && temTransicao (token.getValor(), token.getTipo(), tipo, automato)) {
				possiveisTransicoes++;
				aPercorrer = "tipo";
			}
			if (submaquina.temTransicao("booleano") && temTransicao (token.getValor(), token.getTipo(), booleano, automato)) {
				possiveisTransicoes++;
				aPercorrer = "booleano";
			}
			if (submaquina.temTransicao("identificador") && tabelaDeSimbolos.estaNaTabela(token.getValor())) {
				possiveisTransicoes++;
				aPercorrer = "identificador";
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			}
			if (submaquina.temTransicao("numero") && token.getTipo() == -2) {
				possiveisTransicoes++;
				aPercorrer = "numero";
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			}
			
			// Caso haja apenas uma transicao possivel, realiza a mesma
			if (possiveisTransicoes == 1) {
				// Percorre submaquina
				submaquina.percorre(aPercorrer);
				// Salva estado da submaquina
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				// Caso seja necessario, pega um novo token
				if (getNewToken == true) {
					token = tokensTokens.recuperaToken();
				}
				// Empilha submaquina, caso ela nao tenha terminado
				if (!submaquina.estadoAtivoFinal() || submaquina.temTransicao('"' + token.getValor() + '"')) {
					pilha.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
					// Empilha nova maquina, caso a transicao Ž de um nao-terminal
					if (aPercorrer == "programa" || aPercorrer == "comando" || aPercorrer == "expressao" || 
							aPercorrer == "tipo" || aPercorrer == "booleano") {
						pilha.push(new PilhaEstadoSubmaquina(0, aPercorrer));
					}
				}
				
			}
			
			// Caso haja mais de uma possivel transicao ou nenhuma, ha um erro
			else {
				System.out.println("[ERRO] N‹o h‡ transicoes possiveis ou existe mais de uma transicao");
				return false;
			}
			
			// Verifica se a pilha est‡ vazia e o estado Ž final
			if (tokensTokens.getTamanho() == 0 && submaquina.estadoAtivoFinal()) {
				fimTokensEFinal = false;
			}
			
		}
		
		System.out.println("[INFO] Codigo aceito!");
		return true;
	}
	
}