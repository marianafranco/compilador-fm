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
	
	/**
	 * string que recebe o codigo em MVN
	 */
	private String output;
	
	/**
	 * nome para o arquivo de saida 
	 */
	private String arquivo;
	
	/**
	 * pilha que guarda as tabelas de simbolos das fun��es e do programa principal
	 */
	private Vector<TabelaSimbolos> vetorEscopos;
	
	/**
	 * tabela de simbolos do escopo atual
	 */
	private TabelaSimbolos tabela;
	
	/**
	 * pilha de tokens para a geracao de codigo
	 */
	private Stack<Token> pilhaTokens;
	
	/**
	 * pilha de declaracao de variaveis
	 */
	private Vector<String> pilhaDeclaracoes;
	
	/**
	 * pilha de instrucoes
	 */
	private Vector<String> pilhaInstrucoes;
	
	/**
	 * contador de vari�veis declaradas do tipo inteiro
	 */
	private int contaInt;
	
	/**
	 * contador de vari�veis declaradas do tipo boolean
	 */
	private int contaBool;
	
	/**
	 * contador de vari�veis declaradas do tipo string
	 */
	private int contaString;
	
	/**
	 * label da variavel que ira receber o resultado de uma atribuicao
	 */
	private String nomeResultAtrib;
	
	/**
	 * tipo de resultado esperado
	 */
	private int tipoResultAtrib;
	
	/**
	 * pilha de tipo de comando
	 */
	private Stack<Integer> pilhaComando;
	
	/**
	 * pilha de operadores
	 */
	private Stack<String> pilhaOperadores;
	
	/**
	 * pilha de operandos
	 */
	private Stack<String> pilhaOperandos;
	
	/**
	 * ponteiro para os temporarios
	 */
	private int temp;
	
	/**
	 * numero maximo de temporarios
	 */
	private int maxTemp;
	
	/**
	 * tipo de comparacao
	 */
	private String tipoComparacao;
	
	/**
	 * primeiro elemento de uma comparacao
	 */
	private String operadorA;
	
	/**
	 * segundo elemento de uma comparacao
	 */
	private String operadorB;
	
	/**
	 * pilha de IFs
	 */
	private Stack<PilhaSeSenao> pilhaIF;
	
	/**
	 * contador de IFs no programa
	 */
	private int contaIF;
	
	/**
	 * pilha de whiles
	 */
	private Stack<Integer> pilhaWhile;
	
	/**
	 * contador de whiles no programa
	 */
	private int contaWhile;
	
	/**
	 * contador de subrotinas no programa
	 */
	private int contaSubrotinas;
	
	/**
	 * pilha de tipo de programa
	 */
	private Stack<Integer> pilhaPrograma;
	
	
	private TabelaFuncao tabelaFuncao;
	
	
	private int tipoFuncao;
	
	
	/**
	 * Metodo Construtor
	 * 
	 * @param arquivoMVN
	 */
	public Semantico(String arquivoMVN) {
		this.output = "";
		this.arquivo = arquivoMVN;
		
		pilhaTokens = new Stack<Token>();
		pilhaDeclaracoes = new Vector<String>();
		pilhaInstrucoes = new Vector<String>();
		
		contaInt = 0;
		contaBool = 0;
		contaString = 0;
		
		nomeResultAtrib = "";
		pilhaComando = new Stack<Integer>();
		
		pilhaOperadores = new Stack<String>();
		pilhaOperandos = new Stack<String>();
		
		temp = 0;
		maxTemp = 20;
		
		tipoComparacao = "";
		operadorA = "";
		operadorB = "";
		pilhaIF = new Stack<PilhaSeSenao>();
		contaIF = 0;
		
		pilhaWhile = new Stack<Integer>();
		contaWhile = 0;
		
		
		// Empilha codigo de inicializacao
		pilhaDeclaracoes.add("@\t/0");
		pilhaDeclaracoes.add("\t\tJP\tINICIO");
		
		
		pilhaPrograma = new Stack<Integer>();
		pilhaPrograma.push(TiposPrograma.DECLARAFUNCAO);
		
		contaSubrotinas = 0;
		tabelaFuncao = new TabelaFuncao();
		vetorEscopos = new Vector<TabelaSimbolos>();
		
		// inicializa a tabela de simbolos
		tabela = new TabelaSimbolos();
		tabela.setEscopo(vetorEscopos.size());
		tabela.setEscopoAnterior(0);
		vetorEscopos.add(tabela);
	}
	
	
	/**
	 * Gera o codigo em MVN
	 * 
	 * @param token
	 * @param nextToken
	 * @param aPercorrer
	 * @param submaquina
	 * @throws SemanticoException 
	 */
	public void geraCodigo(Token token, Token nextToken, String aPercorrer, AFD submaquina) throws SemanticoException{
		
		//System.out.println("[DEBUG] Token: " + token.getValor() + " submaquina: " + submaquina.getNome());
		//System.out.println("[DEBUG] nextToken: " + nextToken.getValor());
		
		
		//*********************************************************************
		// Submaquina <PROGRAMA>
		//*********************************************************************
		if(submaquina.getNome().equals("programa")){
			
			
			if(aPercorrer.equals("comando") || aPercorrer.equals("expressao")){
				// N�o faz nada
				
			// TIPO
			}else if (token.getValor().equals("inteiro") || token.getValor().equals("booleano") || token.getValor().equals("caracteres")) {
				this.pilhaTokens.push(token);
				if(token.getValor().equals("inteiro")){
					tipoFuncao = TiposSimbolos.INTEIRO;
				}else if(token.getValor().equals("booleano")){
					tipoFuncao = TiposSimbolos.BOOLEANO;
				}else if(token.getValor().equals("caracteres")){
					tipoFuncao = TiposSimbolos.BOOLEANO;
				}
				
			// IDENTIFICADOR - Pode ser o nome da subrotina ou um de seus parametros
			}else if(token.getTipo() == TiposLexico.NOME && !PalavrasReservadas.reservada(token.getValor())){
				
				// Caso seja uma subrotina
				if (pilhaPrograma.peek() == TiposPrograma.DECLARAFUNCAO) {
					pilhaInstrucoes.add("FUNC_" + contaSubrotinas + "\t\tJP\t/00");
					tabelaFuncao.insereFuncao(token.getValor(), tipoFuncao, "FUNC_" + contaSubrotinas);
				
				
				}else if(pilhaPrograma.peek() == TiposPrograma.RETORNO){
					if(tabela.estaNaTabela(token.getValor())){
						if(tabela.recuperaEntrada(token.getValor()).isInicializado()){
							pilhaInstrucoes.add("\t\tLD\t"+tabela.recuperaEntrada(token.getValor()).getEndereco() 
									+ "\t; RETURN " + tabela.recuperaEntrada(token.getValor()).getEndereco());
							pilhaInstrucoes.add("\t\tRS\t" + "FUNC_" + contaSubrotinas);
						}else{
							throw new SemanticoException("O parametro de retorno '" + token.getValor() + "' n�o foi inicializado.", token.getLinha());
						}
						pilhaPrograma.pop();
					}else{
						throw new SemanticoException(" O parametro de retorno '" + token.getValor() + "' n�o foi anteriormente declarado.", token.getLinha());
					}
				}
				
				// Caso seja uma variavel
				else {
					//pilhaDeclaracoes.add();
				}
				
			// RETORNO
			}else if(token.getValor().equals("retorno")){
				pilhaPrograma.push(TiposPrograma.RETORNO);
				// TODO verificar se o tipo do retorno � o mesmo da fun��o
			
			// PRINCIPAL
			}else if(token.getValor().equals("principal")){
				tabela = new TabelaSimbolos();
				pilhaInstrucoes.add("INICIO\t\tLD\t/00");
				pilhaTokens.clear();
				pilhaPrograma.push(TiposPrograma.PRINCIPAL);
			
			// (
			}else if (token.getValor().equals("(")) {
				pilhaPrograma.push(TiposPrograma.ARGUMENTO);
				
			// )
			}else if (token.getValor().equals(")")) {
				pilhaPrograma.pop();
				
			// TOKENS DE ESCOPO
			}else if (token.getValor().equals("{")) { 
				//pilhaInstrucoes.add("FUNC_" + contaSubrotinas + "\t\tLD\t/00");
				// cria um novo escopo
				tabela = new TabelaSimbolos();
				tabela.setEscopo(vetorEscopos.size());
				vetorEscopos.add(tabela);
				
			}else if (token.getValor().equals("}")) { 
				if(pilhaPrograma.peek() == TiposPrograma.DECLARAFUNCAO){
					pilhaPrograma.pop();
					contaSubrotinas++;
				}
				
			}else {
				// {, }, aPercorrer == comando, aPercorrer == expressao 
			}
		
			
		//*********************************************************************
		// Submaquina <COMANDO>
		//*********************************************************************
		}else if(submaquina.getNome().equals("comando")){
			
			// STRING
			if(aPercorrer.equals("string")){
				
				
			// EXPRESSAO
			}else if(aPercorrer.equals("expressao")){
				
				// comando SE ou ENQUANTO
				if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
					nomeResultAtrib = "TEMP_" + temp;
					tipoResultAtrib = TiposSimbolos.INTEIRO;
					temp++;
					if(operadorA.equals("")){
						if(pilhaComando.peek() == TiposComando.ENQUANTO){
							pilhaInstrucoes.add("LOOP_" + pilhaWhile.peek() + "\t\tLD\t/00");
						}else if(pilhaComando.peek() == TiposComando.SE){
							pilhaInstrucoes.add("IF_" + pilhaIF.peek().getIdIF() + "\t\tLD\t/00");
						}
						operadorA = operadorA + nomeResultAtrib;
					}else{
						operadorB = operadorB + nomeResultAtrib;
					}
				
				// comando SAIDA
				}else if(pilhaComando.peek() == TiposComando.SAIDA){
					nomeResultAtrib = "TEMP_" + temp;
					tipoResultAtrib = TiposSimbolos.INTEIRO;
					temp++;
					
				// comando ENTRADA
				}else if(pilhaComando.peek() == TiposComando.ENTRADA){
					// TODO Implementar
					
				// comando ATRIBUICAO
				}else if(pilhaComando.peek() == TiposComando.ATRIBUICAO){
					// Nao faz nada
				}
				
			
			// TIPO
			}else if (token.getValor().equals("inteiro") || token.getValor().equals("booleano") || token.getValor().equals("caracteres")) {
				if(!pilhaComando.empty()){
					if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
						pilhaComando.push(TiposComando.DESCONHECIDO);
					}else{
						if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
							pilhaComando.pop();
							
							pilhaTokens.push(token);
							pilhaComando.push(TiposComando.DECLARACAO);
						}
					}
				}else{
					pilhaTokens.push(token);
					pilhaComando.push(TiposComando.DECLARACAO);
				}
				
				
			// IDENTIFICADOR
			}else if (token.getTipo() == TiposLexico.NOME && !PalavrasReservadas.reservada(token.getValor())){
				
				// FUNCAO
				if(tabelaFuncao.estaNaTabela(token.getValor())){
					// chamada de funcao
					pilhaInstrucoes.add("\t\tSC\t" + tabelaFuncao.recuperaFuncao(token.getValor()).getEnd());
					pilhaComando.push(TiposComando.FUNCAO);
				
				// se ja esta na tabela de simbolos
				}else if(tabela.estaNaTabela(token.getValor())){
					
					if(!pilhaComando.empty()){
						
						// ENTRADA
						if(pilhaComando.peek() == TiposComando.ENTRADA){
							// TODO Gerar a entrada
							
						// ERRO - declaracao de variavel ja declarada
						}else if(pilhaComando.peek() == TiposComando.DECLARACAO){
							throw new SemanticoException(" Variavel " + token.getValor() + " ja declarada!", token.getLinha());
						
						// ATRIBUICAO
						}else{
							if(!pilhaComando.empty()){
								if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
									pilhaComando.push(TiposComando.DESCONHECIDO);
								}else{
									if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
										pilhaComando.pop();
										
										// Gera inicio para a chamada de atribuicao
										nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
										tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
										pilhaComando.push(TiposComando.ATRIBUICAO);
									}
								}
							}else{
								nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
								tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
								pilhaComando.push(TiposComando.ATRIBUICAO);
							}
						}
					
					// ATRIBUICAO
					}else{
						if(!pilhaComando.empty()){
							if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
								pilhaComando.push(TiposComando.DESCONHECIDO);
							}else{
								if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
									pilhaComando.pop();
									
									nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
									tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
									pilhaComando.push(TiposComando.ATRIBUICAO);
								}
							}
						}else{
							nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
							tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
							pilhaComando.push(TiposComando.ATRIBUICAO);
						}
					}
					
					
				// senao nova DECLARACAO
				}else{
					Token tipo = pilhaTokens.pop();
					if(tipo.getValor().equals("inteiro")){
						tabela.adicionaEntrada(token.getValor(), TiposSimbolos.INTEIRO, "VAR_"+contaInt, 1, token.getLinha(), token.getColuna());
						contaInt++;
					}else if(tipo.getValor().equals("booleano")){
						tabela.adicionaEntrada(token.getValor(), TiposSimbolos.BOOLEANO, "BOOL_"+contaBool, 1, token.getLinha(), token.getColuna());
						contaBool++;
					}else if(tipo.getValor().equals("caracteres")){
						tabela.adicionaEntrada(token.getValor(), TiposSimbolos.STRING, "CHAR_"+contaString, 0, token.getLinha(), token.getColuna());
						contaString++;
					}else{
						throw new SemanticoException(" Esperava-se um tipo no lugar de " + tipo.getValor(), token.getLinha());
					}
					// empilha para poder depois usar em caso de vetor
					pilhaTokens.push(token);
				}
			
				
				
			// NUMERO
			}else if(token.getTipo() == TiposLexico.NUMERO){
				if(!pilhaTokens.empty()){
					Token anterior = pilhaTokens.pop();
					
					if(anterior.getTipo() == TiposLexico.NOME && tabela.estaNaTabela(anterior.getValor())){
						tabela.recuperaEntrada(anterior.getValor()).setTamanho(Integer.parseInt(token.getValor()));
					}else{
						// ATRIBUICAO
					}
				}else{
					// ATRIBUICAO
				}
				
				
			// VERDADEIRO
			}else if(token.getValor().equals("verdadeiro")){
				if(pilhaComando.peek() == TiposComando.ATRIBUICAO){
					if(tipoResultAtrib == TiposSimbolos.BOOLEANO){
						pilhaInstrucoes.add("\t\tLD\t/01\t; " + nomeResultAtrib + " = TRUE");
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
					}else{
						throw new SemanticoException("Impossivel atribuir o valor 'verdadeiro' para variavel nao booleana.", token.getLinha());
					}
				}
				
			// FALSO
			}else if(token.getValor().equals("falso")){
				if(pilhaComando.peek() == TiposComando.ATRIBUICAO){
					if(tipoResultAtrib == TiposSimbolos.BOOLEANO){
						pilhaInstrucoes.add("\t\tLD\t/00\t; " + nomeResultAtrib + " = FALSE");
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
					}else{
						throw new SemanticoException("Impossivel atribuir o valor 'falso' para variavel nao booleana.", token.getLinha());
					}
				}
				
			// ENQUANTO
			}else if(token.getValor().equals("enquanto")){
				if(!pilhaComando.empty()){
					if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
						pilhaComando.push(TiposComando.DESCONHECIDO);
					}else{
						if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
							pilhaComando.pop();
							pilhaComando.push(TiposComando.ENQUANTO);
							pilhaWhile.push(contaWhile);
							contaWhile ++;
						}
					}
				}else{
					pilhaComando.push(TiposComando.ENQUANTO);
					pilhaWhile.push(contaWhile);
					contaWhile ++;
				}
				
			// SE
			}else if(token.getValor().equals("se")){
				if(!pilhaComando.empty()){
					if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
						pilhaComando.push(TiposComando.DESCONHECIDO);
					}else{
						if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
							pilhaComando.pop();
							pilhaComando.push(TiposComando.SE);
							pilhaIF.push(new PilhaSeSenao(contaIF, false));
							contaIF ++;
						}
					}
				}else{
					pilhaComando.push(TiposComando.SE);
					pilhaIF.push(new PilhaSeSenao(contaIF, false));
					contaIF ++;
				}
				

			// SENAO
			}else if(token.getValor().equals("senao")){
				pilhaInstrucoes.add("\t\tJP\tENDIF_" + pilhaIF.peek().getIdIF());
				pilhaInstrucoes.add("ELSE_" + pilhaIF.peek().getIdIF() + "\t\tLD\t/00\t; ELSE");
				pilhaIF.peek().setTemELSE(true);
			
			// ENTRADA
			}else if(token.getValor().equals("entrada")){
			
			
			// SAIDA
			}else if(token.getValor().equals("saida")){
				
				if(!pilhaComando.empty()){
					if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
						pilhaComando.push(TiposComando.DESCONHECIDO);
					}else{
						if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
							pilhaComando.pop();
							pilhaComando.push(TiposComando.SAIDA);
						}
					}
				}else{
					pilhaComando.push(TiposComando.SAIDA);
				}
			
			// COMPARACAO
			}else if(token.getValor().equals(">") || token.getValor().equals("<") || token.getValor().equals(">=") 
					|| token.getValor().equals("<=") || token.getValor().equals("==") || token.getValor().equals("!=")){
				tipoComparacao = token.getValor();
				geraExpressao();
				String result = pilhaOperandos.pop();
				pilhaInstrucoes.add("\t\tLD\t"+ result + "\t; " + nomeResultAtrib + " = " + result);
				pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
				
			// ESPECIAL ')'
			}else if(token.getValor().equals(")")){
				
				if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
					geraExpressao();
					String result = pilhaOperandos.pop();
					pilhaInstrucoes.add("\t\tLD\t"+ result + "\t; " + nomeResultAtrib + " = " + result);
					pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
					temp = 0;
					if(pilhaComando.peek() == TiposComando.SE){
						geraComparacao();
					}else{
						geraLoop();
					}
					
				}else if(pilhaComando.peek() == TiposComando.FUNCAO){
					// desempilha ao receber ;
				}
			
			// NOVO ESCOPO	
			}else if(token.getValor().equals("{")){
				
				
			// FIM DE COMANDO IF OU WHILE
			}else if(token.getValor().equals("}")){
				// fim SE
				if(pilhaComando.peek() == TiposComando.SE){
					
					if(!nextToken.getValor().equals("senao")){
						if(!pilhaIF.peek().isTemELSE()){
							pilhaInstrucoes.add("ELSE_" + pilhaIF.peek().getIdIF() + "\t\tLD\t/00\t; ELSE");
							pilhaInstrucoes.add("ENDIF_" + pilhaIF.peek().getIdIF() + "\t\tLD\t/00\t; END IF");
						}else{
							pilhaInstrucoes.add("ENDIF_" + pilhaIF.peek().getIdIF() + "\t\tLD\t/00\t; END IF");
						}
						pilhaComando.pop();
						pilhaIF.pop();
					}
					
				// fim ENQUANTO	
				}else if(pilhaComando.peek() == TiposComando.ENQUANTO){
					pilhaInstrucoes.add("\t\tJP\tLOOP_" + pilhaWhile.peek());
					pilhaInstrucoes.add("ENDLOOP_" + pilhaWhile.peek() + "\t\tLD\t/00\t; END WHILE");
					pilhaComando.pop();
					pilhaWhile.pop();
				}
				
			
			// FIM DE COMANDO
			}else if (token.getValor().equals(";")) {
				
				// DECLARACAO
				if(pilhaComando.peek() == TiposComando.DECLARACAO){
					// desempilha o identificador
					pilhaTokens.pop();
				
				// ATRIBUICAO
				}else if(pilhaComando.peek() == TiposComando.ATRIBUICAO){
					if(tipoResultAtrib == TiposSimbolos.INTEIRO){
						geraExpressao();
						String result = pilhaOperandos.pop();
						pilhaInstrucoes.add("\t\tLD\t"+ result + "\t; " + nomeResultAtrib + " = " + result);
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
						temp = 0;
						
						tabela.recuperaEntradaPorEnd(nomeResultAtrib).setInicializado(true);
					}
				
				// SAIDA	
				} else if(pilhaComando.peek() == TiposComando.SAIDA){
					if(tipoResultAtrib == TiposSimbolos.INTEIRO){
						geraExpressao();
						String result = pilhaOperandos.pop();
						pilhaInstrucoes.add("\t\tLD\t"+ result + "\t; " + nomeResultAtrib + " = " + result);
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
						pilhaInstrucoes.add("\t\tLD\t" + nomeResultAtrib);
						pilhaInstrucoes.add("\t\tMM\tARG_INT");
						pilhaInstrucoes.add("\t\tSC\tPRINT_INT");
						pilhaInstrucoes.add("\t\tLD\tRESULT_ASC");
						pilhaInstrucoes.add("\t\tPD\t/100\t; SAIDA = " + nomeResultAtrib);
						pilhaInstrucoes.add("\t\tLD\tNEW_LINE");
						pilhaInstrucoes.add("\t\tPD\t/100");
						temp = 0;
					}
					
				}
				//System.out.println("Retira da pilha " + pilhaComando.peek());
				pilhaComando.pop();
			}
		
			
		//*********************************************************************
		// Submaquina <EXPRESSAO>
		//*********************************************************************
		}else if(submaquina.getNome().equals("expressao")){
			
			// BOOL OU STRING
			if(tipoResultAtrib != TiposSimbolos.INTEIRO){
				throw new SemanticoException("Impossivel atribuir o valor " + token.getValor() + " para variavel de tipo booleano ou caracteres.", token.getLinha());
			
			// chama EXPRESSAO
			}else if(aPercorrer.equals("expressao")){
				// N�o faz nada
			
			// NUMERO
			}else if(token.getTipo() == TiposLexico.NUMERO){
				if(!encontraConstante(token.getValor())){
					tabela.adicionaEntrada(token.getValor(), TiposSimbolos.CONSTANTE, "NUM_"+token.getValor(), 1, token.getLinha(), token.getColuna());
				}
				
				pilhaOperandos.push("NUM_" + token.getValor());
				
				if(!pilhaOperadores.empty()){
					if(pilhaOperadores.peek().equals("*")){
						geraOperacao("*");
					}else if(pilhaOperadores.peek().equals("/")){
						geraOperacao("/");
						pilhaOperadores.pop();
					}
				}
			
			// IDENTIFICADOR	
			}else if(token.getTipo() == TiposLexico.NOME && !PalavrasReservadas.reservada(token.getValor())){
				
				if(tabela.estaNaTabela(token.getValor())){
					
					if(tabela.recuperaEntrada(token.getValor()).getTipo() == TiposSimbolos.INTEIRO){
						
						// verifica se foi inicializada a variavel
						if(tabela.recuperaEntrada(token.getValor()).isInicializado()){
							
							// empilha
							pilhaOperandos.push(tabela.recuperaEntrada(token.getValor()).getEndereco());
							
							if(!pilhaOperadores.empty()){
								
								if(pilhaOperadores.peek().equals("*")){
									geraOperacao("*");
								}else if(pilhaOperadores.peek().equals("/")){
									geraOperacao("/");
								}
							}
						}else{
							throw new SemanticoException("Variavel " + token.getValor() + " nao foi inicializada.", token.getLinha());
						}
						
					}else{
						throw new SemanticoException("Variavel " + token.getValor() + " nao inteira, impossivel de calcular a expressao.", token.getLinha());
					}
					
				}else{
					throw new SemanticoException("Variavel " + token.getValor() + " nao foi declarada.", token.getLinha());
				}
			
			// OPERADOR
			}else if(token.getValor().equals("*") || token.getValor().equals("/") || token.getValor().equals("+")
					|| token.getValor().equals("-") || token.getValor().equals("(")){
				pilhaOperadores.push(token.getValor());
				
			// )
			}else if(token.getValor().equals(")")){
				pilhaOperadores.push(")");
				geraExpressao();
				
			// ERRO
			}else{
				throw new SemanticoException("Impossivel calcular a expressao.", token.getLinha());
			}
		}
	}

	
	
	/**
	 * Gera as expressoes conforme o valor na pilha de operadores e operandos
	 */
	private void geraExpressao(){
		if(!pilhaOperadores.empty()){
			String op = pilhaOperadores.peek();
			if(op.equals("*") || op.equals("/") || op.equals("+") || op.equals("-")){
				geraOperacao(op);
				geraExpressao();
			}else if(op.equals("(")){
				// N�o faz nada, s� desempilha
				pilhaOperadores.pop();
			}else if(op.equals(")")){
				pilhaOperadores.pop();
				geraExpressao();
			}else{
				System.out.println("[ERRO] Impossivel realizar uma operacao com menos de dois operandos.");
			}
		}
	}
	
	
	/**
	 * Gera o codigo MVN para a operacao passada em parametro
	 * 
	 * @param op
	 */
	private void geraOperacao(String op){
		if(pilhaOperadores.peek().equals(op)){
			if(pilhaOperandos.size() >= 2){
				String b = pilhaOperandos.pop();
				String a = pilhaOperandos.pop();
				pilhaInstrucoes.add("\t\tLD\t" + a + "\t; TEMP_" + temp + " = " + a + " " + op +" " + b);
				pilhaInstrucoes.add("\t\t" + op + "\t" + b);
				pilhaInstrucoes.add("\t\tMM\tTEMP_" + temp);
				pilhaOperandos.push("TEMP_"+temp);
				temp++;
			}else{
				System.out.println("[ERRO] Impossivel realizar a operacao '"+ op +"' com menos de dois operandos.");
			}
			
		}else{
			System.out.println("[ERRO] Impossivel realizar a operacao '" + op + "'." );
		}
		pilhaOperadores.pop();
	}
	

	/**
	 * Gera o codigo MVN inicial do comando SE
	 */
	private void geraComparacao(){
		if(tipoComparacao.equals(">")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; IF ( " + operadorA + " > " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJN\tELSE_" + pilhaIF.peek().getIdIF());
			pilhaInstrucoes.add("\t\tJZ\tELSE_" + pilhaIF.peek().getIdIF());
			
		}else if(tipoComparacao.equals("<")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorB + "\t; IF ( " + operadorA + " < " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorA);
			pilhaInstrucoes.add("\t\tJN\tELSE_" + pilhaIF.peek().getIdIF());
			pilhaInstrucoes.add("\t\tJZ\tELSE_" + pilhaIF.peek().getIdIF());
			
		}else if(tipoComparacao.equals(">=")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; IF ( " + operadorA + " >= " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJN\tELSE_" + pilhaIF.peek().getIdIF());
			
		}else if(tipoComparacao.equals("<=")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorB + "\t; IF ( " + operadorA + " <= " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorA);
			pilhaInstrucoes.add("\t\tJN\tELSE_" + pilhaIF.peek().getIdIF());
			
		}else if(tipoComparacao.equals("==")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; IF ( " + operadorA + " == " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJZ\tIF_EQ_" + pilhaIF.peek().getIdIF());
			pilhaInstrucoes.add("\t\tJP\tELSE_" + pilhaIF.peek().getIdIF());
			pilhaInstrucoes.add("IF_EQ_" + pilhaIF.peek().getIdIF() + "\t\tLD\t/00");
			
		}else if(tipoComparacao.equals("!=")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; IF ( " + operadorA + " != " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJZ\tELSE_" + pilhaIF.peek().getIdIF());
		}
		
		operadorA = "";
		operadorB = "";
	}
	
	/**
	 * Gera o codigo MVN inicial para o comando ENQUANTO 
	 */
	private void geraLoop(){
		if(tipoComparacao.equals(">")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; WHILE ( " + operadorA + " > " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJN\tENDLOOP_" + pilhaWhile.peek());
			pilhaInstrucoes.add("\t\tJZ\tENDLOOP_" + pilhaWhile.peek());
			
		}else if(tipoComparacao.equals("<")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorB + "\t; WHILE ( " + operadorA + " < " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorA);
			pilhaInstrucoes.add("\t\tJN\tENDLOOP_" + pilhaWhile.peek());
			pilhaInstrucoes.add("\t\tJZ\tENDLOOP_" + pilhaWhile.peek());
			
		}else if(tipoComparacao.equals(">=")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; WHILE ( " + operadorA + " >= " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJN\tENDLOOP_" + pilhaWhile.peek());
			
		}else if(tipoComparacao.equals("<=")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorB + "\t; WHILE ( " + operadorA + " <= " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorA);
			pilhaInstrucoes.add("\t\tJN\tENDLOOP_" + pilhaWhile.peek());
			
		}else if(tipoComparacao.equals("==")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; WHILE ( " + operadorA + " == " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJZ\tLOOP_EQ_" + pilhaWhile.peek());
			pilhaInstrucoes.add("\t\tJP\tENDLOOP_" + pilhaWhile.peek());
			pilhaInstrucoes.add("LOOP_EQ_" + pilhaWhile.peek() + "\t\tLD\t/00");
			
		}else if(tipoComparacao.equals("!=")){
			pilhaInstrucoes.add("\t\tOS\t=0" + "\t; WHILE ( " + operadorA + " != " + operadorB + " )");
			pilhaInstrucoes.add("\t\tLD\t" + operadorA);
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJZ\tENDLOOP_" + pilhaWhile.peek());
		}
		
		operadorA = "";
		operadorB = "";
	}
	
	/**
	 * Verifica se a constante ja foi declarada em algum escopo
	 * @param cte
	 * @return true/false
	 */
	private boolean encontraConstante(String cte){
		for(int i=0; i < vetorEscopos.size(); i++){
			TabelaSimbolos tab = vetorEscopos.get(i);
			//System.out.println(tab.getEntradas());
			for(int j=0; j< tab.getEntradas(); j++){
				if(tab.recuperaEntrada(j).getNome().equals(cte)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Subrotina para impressao
	 */
	private void print(){
		String intToAsc = "PRINT_INT\t\tJP\t/00\n"
				 		+ "\t\tLD\tARG_INT\t; CONVERT INT TO ASC\n" 
						+ "\t\t+\tOFFSET\n"
						+ "\t\tMM\tRESULT_ASC\n"
						+ "\t\tLD\tSTRING_1\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_2\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_3\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_4\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_5\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_6\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_7\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tLD\tSTRING_6\n"
						+ "\t\tPD\t/100\t;\n"
						+ "\t\tRS\tPRINT_INT\n"
						+ "ARG_INT\t\tK\t/00\t; ARG INT\n"
						+ "RESULT_ASC\t\tK\t/00\t; RESULT ASC\n"
						+ "OFFSET\t\tK\t/30\t; OFFSET\n"
						+ "STRING_1\t\tK\t/53\t; S\n"
						+ "STRING_2\t\tK\t/41\t; A\n"
						+ "STRING_3\t\tK\t/49\t; I\n"
						+ "STRING_4\t\tK\t/44\t; D\n"
						+ "STRING_5\t\tK\t/41\t; A\n"
						+ "STRING_6\t\tK\t/20\t; \n"
						+ "STRING_7\t\tK\t/3d\t; =\n"
						+ "NEW_LINE\t\tK\t/0a\t; NEW LINE\n"
						;
		this.output = this.output + intToAsc;
	}
	
	
	/**
	 * Cria o arquivo de saida
	 */
	public void criaArquivo(){
		try{
			FileWriter writer = new FileWriter(this.arquivo);
			PrintWriter saida = new PrintWriter(writer);
			
			// constantes e declaracoes na pilha de declaracoes
			for(int i=0; i < vetorEscopos.size(); i++){
				TabelaSimbolos tab = vetorEscopos.get(i);
				for(int j=0; j< tab.getEntradas(); j++){
					if(tab.recuperaEntrada(j).getTipo() == TiposSimbolos.CONSTANTE){
						pilhaDeclaracoes.add(tab.recuperaEntrada(j).getEndereco() + "\t\tK\t/" 
								+ Integer.toHexString(Integer.parseInt(tab.recuperaEntrada(j).getNome())) + "\t;" 
								+ " " + tab.recuperaEntrada(j).getNome());
					}
				}
				for(int j=0; j< tab.getEntradas(); j++){
					if(tab.recuperaEntrada(j).getTipo() != TiposSimbolos.CONSTANTE){
						pilhaDeclaracoes.add(tab.recuperaEntrada(j).getEndereco() + "\t\tK\t/00"
								+ "\t; " + tab.recuperaEntrada(j).getNome());
					}
				}
			}
			
			// poe a pilha de declaracoes na saida
			for(int i=0; i < pilhaDeclaracoes.size(); i++){
				this.output = this.output + (String)pilhaDeclaracoes.get(i) + "\n";
			}
			
			// poe as instrucoes na saida
			for(int i=0; i < pilhaInstrucoes.size(); i++){
				this.output = this.output + (String)pilhaInstrucoes.get(i) + "\n";
			}
			
			// gera o final de programa
			this.output = this.output + "FIM\t\tHM\t/00" + "\n";
			
			// imprime os temporarios
			for(int i=0; i <= maxTemp; i++){
				this.output = this.output + "TEMP_" + i + "\t\tK\t/00\n"; 
			}
			
			// acrescenta a rotina de impressao
			print();
			
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