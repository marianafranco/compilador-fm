package compilador.sintatico;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;

import compilador.estruturas.PalavrasReservadas;
import compilador.estruturas.APE;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.TiposLexico;
import compilador.estruturas.TiposSimbolos;
import compilador.estruturas.Token;
import compilador.estruturas.PilhaEstadoSubmaquina;
import compilador.estruturas.AFD;
import compilador.estruturas.TabelaSimbolos;

public class PercorreAPE {
	
	/**
	 * string que recebe o codigo em MVN
	 */
	private String output;
	
	/**
	 * nome para o arquivo de saida 
	 */
	private String arquivoMVN;
	
	/**
	 * pilha usada para as chamadas de submáquina
	 */
	private Stack<PilhaEstadoSubmaquina> pilhaSubmaquinas;
	
	/**
	 * vetor que guarda as tabela de simbolos
	 */
	private Vector<TabelaSimbolos> vetorEscopos;
	
	/**
	 * pilha de escopo
	 */
	private Stack<TabelaSimbolos> pilhaEscopos;
	
	/**
	 * pilha de tokens para a geracao de codigo
	 */
	private Stack<Token> pilhaTokens;
	
	/**
	 * pilha de instrucoes de 
	 */
	private Vector<String> pilhaDeclaracoes;
	
	/**
	 * pilha de tokens para a geracao de codigo
	 */
	private Stack<String> pilhaInstrucoes;
	
	
	/**
	 * contador de variáveis declaradas
	 */
	private int contador;
	
	
	public PercorreAPE(String arquivoMVN) {
		this.output = "";
		this.arquivoMVN = arquivoMVN;
		
		pilhaSubmaquinas = new Stack<PilhaEstadoSubmaquina>();
		vetorEscopos = new Vector<TabelaSimbolos>();
		pilhaEscopos = new Stack<TabelaSimbolos>();
		pilhaTokens = new Stack<Token>();
		pilhaDeclaracoes = new Vector<String>();
		pilhaInstrucoes = new Stack<String>();
		
		contador = 0;
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
		
		// inicializa a tabela de simbolos
		TabelaSimbolos tabela = new TabelaSimbolos();
		tabela.setEscopo(vetorEscopos.size());
		vetorEscopos.add(tabela);
		pilhaEscopos.push(tabela);
		
		// Empilha codigo de inicializacao
		pilhaDeclaracoes.add("@ /0");
		pilhaDeclaracoes.add("JP INICIO");
		pilhaInstrucoes.push("@ /256");
		
		// Pega primeiro token
		token = tokensTokens.recuperaToken();
		
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
			
			// seta novamente o estado ativo da submaquina
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			// Caso haja apenas uma transicao possivel, realiza a mesma
			if (possiveisTransicoes == 1) {
				// Percorre submaquina
				submaquina.percorre(aPercorrer);
				// Salva estado da submaquina
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				
				// Gera código
				geraCodigo(token, tabela, submaquina);
				
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
		
		criaArquivo();
		
		return true;
	}
	
	
	public void geraCodigo(Token token, TabelaSimbolos tabela, AFD submaquina){
		
		//System.out.println("[DEBUG] Token: " + token.getValor() + " submaquina: " + submaquina.getNome());
		
		// Se submaquina <programa>
		if(submaquina.getNome().equals("programa")){
			
			// se achamos um tipo
			if (token.getValor().equals("inteiro") || token.getValor().equals("booleano") || token.getValor().equals("caracteres")) {
				pilhaTokens.push(token);
				
			// se achamos um nome
			}else if(token.getTipo() == TiposLexico.NOME){
				
				// se ja esta na tabela de simbolos
				if(tabela.estaNaTabela(token.getValor())){
					
					// verificamos se teve um token <retorno>
					Token retorno = pilhaTokens.pop();
					if(retorno.getValor().equals("retorno")){
						// TODO Verifica se é o mesmo tipo da função
						// TODO Faz colocar o valor no topo da pilha de execução
					}else{
						// TODO Fez retornar um ERRO
					}
					
				// se nova declaracao
				}else{
					
					// verificamos o tipo
					Token tipo = pilhaTokens.pop();
					if(tipo.getValor().equals("inteiro")){
						tabela.adicionaEntrada(token.getValor(), TiposSimbolos.INTEIRO, token.getLinha(), token.getColuna());
					}
				}
				
				
			}
		
			
			
			
		// Se submaquina <comando>
		}else if(submaquina.getNome().equals("comando")){
			// Se acharmos uma declaracao
			if (token.getValor().equals("inteiro") || token.getValor().equals("booleano") || tabela.estaNaTabela(token.getValor())) {
				pilhaTokens.push(token);
			// Se acharmos o fim de um comando
			}else if (token.getValor().equals(";")) {
				Token ultimo = pilhaTokens.pop();
				Token penultimo = pilhaTokens.pop();
				// Foi declarado um inteiro ou um booleano
				if ((penultimo.getValor().equals("inteiro") || penultimo.getValor().equals("booleano")) && tabela.estaNaTabela(ultimo.getValor())) {
					// Caso ele nao tenha sido declarado
					if (tabela.recuperaEntrada(ultimo.getValor()).jaDeclarado() == false) {
						// Define proximo endereco
						tabela.recuperaEntrada(ultimo.getValor()).setPosicao(ultimo.getValor());
						pilhaDeclaracoes.add(ultimo.getValor() + " K /0");
					}
					else {
						System.out.println("[ERRO] Variavel " + ultimo.getValor() + " ja declarada!");
					}
				// Foi declarado um booleano
				}else {
					System.out.println("[ERRO] Erro na geracao de codigo!");
				}
				
			}
		
			
			
			
		// Se submaquina <expressao>
		}else if(submaquina.getNome().equals("expressao")){
			
		
		}
	}
	
	
	
	public void criaArquivo(){
		try{
			FileWriter writer = new FileWriter(this.arquivoMVN);
			PrintWriter saida = new PrintWriter(writer);
			
			for(int i=0; i < pilhaDeclaracoes.size(); i++){
				this.output = this.output + (String)pilhaDeclaracoes.get(i) + "\n";
			}
			
			System.out.println("ARQUIVO DE SAIDA\n"+ this.output);
			saida.print(this.output);
			
			saida.close();
			writer.close(); 
		}catch(Exception e){
			
		}
	}
	
}