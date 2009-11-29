package compilador.semantico;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;

import compilador.estruturas.PalavrasReservadas;
import compilador.estruturas.PilhaSeSenao;
import compilador.estruturas.TiposComando;
import compilador.estruturas.TiposLexico;
import compilador.estruturas.TiposSimbolos;
import compilador.estruturas.Token;
import compilador.estruturas.AFD;
import compilador.estruturas.TabelaSimbolos;

public class Semantico {
	
	/**
	 * string que recebe o codigo em MVN
	 */
	private String output;
	
	/**
	 * nome para o arquivo de saida 
	 */
	private String arquivoMVN;
	
	/**
	 * pilha que guarda a tabela de simbolos atual
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
	 * pilha de declaracao 
	 */
	private Vector<String> pilhaDeclaracoes;
	
	/**
	 * pilha de instrucoes
	 */
	private Vector<String> pilhaInstrucoes;
	
	/**
	 * contador de variáveis declaradas do tipo inteiro
	 */
	private int contaInt;
	
	
	/**
	 * contador de variáveis declaradas do tipo boolean
	 */
	private int contaBool;
	
	
	/**
	 * contador de variáveis declaradas do tipo string
	 */
	private int contaString;
	
	
	/**
	 * label da variavel que ira receber o resultado de uma atribuicao
	 */
	private String nomeResultAtrib;
	
	
	private int tipoResultAtrib;
	
	
	//private int tipoComando;
	
	private Stack<Integer> pilhaComando;
	
	
	private TabelaSimbolos tabela;
	
	private Stack<String> pilhaOperadores;
	
	private Stack<String> pilhaOperandos;
	
	/**
	 * guarda o ponteiro para os temporarios
	 */
	private int temp;
	
	
	private int maxTemp;
	
	
	private String tipoComparacao;
	
	private String operadorA;
	
	private String operadorB;
	
	
	private Stack<PilhaSeSenao> pilhaIF;
	
	
	private int contaIF;
	
	
	private Stack<Integer> pilhaWhile;
	
	private int contaWhile;
	
	
	public Semantico(String arquivoMVN) {
		this.output = "";
		this.arquivoMVN = arquivoMVN;
		
		vetorEscopos = new Vector<TabelaSimbolos>();
		pilhaEscopos = new Stack<TabelaSimbolos>();
		pilhaTokens = new Stack<Token>();
		pilhaDeclaracoes = new Vector<String>();
		pilhaInstrucoes = new Vector<String>();
		
		contaInt = 0;
		contaBool = 0;
		contaString = 0;
		
		nomeResultAtrib = "";
		//tipoComando = 0;
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
		
		// inicializa a tabela de simbolos
		tabela = new TabelaSimbolos();
		tabela.setEscopo(vetorEscopos.size());
		vetorEscopos.add(tabela);
		pilhaEscopos.push(tabela);
		
		// Empilha codigo de inicializacao
		pilhaDeclaracoes.add("@\t/00");
		pilhaDeclaracoes.add("\t\tJP\tINICIO");
		pilhaInstrucoes.add("@\t/256");
	}
	
	
public void geraCodigo(Token token, Token nextToken, String aPercorrer, AFD submaquina){
		
		//System.out.println("[DEBUG] Token: " + token.getValor() + " submaquina: " + submaquina.getNome());
		//System.out.println("[DEBUG] nextToken: " + nextToken.getValor());
		
		//*********************************************************************
		// Submaquina <PROGRAMA>
		//*********************************************************************
		if(submaquina.getNome().equals("programa")){
			
			// chama COMANDO
			if(aPercorrer.equals("comando")){
				
				
			// chama EXPRESSAO
			}else if(aPercorrer.equals("expressao")){
			
				
			
			// TIPO
			}else if (token.getValor().equals("inteiro") || token.getValor().equals("booleano") || token.getValor().equals("caracteres")) {
				pilhaTokens.push(token);
				
			// IDENTIFICADOR
			}else if(token.getTipo() == TiposLexico.NOME && !PalavrasReservadas.reservada(token.getValor())){
				
				// se ja esta na tabela de simbolos
				if(tabela.estaNaTabela(token.getValor())){
					
					// verificamos se teve um token RETORNO
					Token retorno = pilhaTokens.pop();
					if(retorno.getValor().equals("retorno")){
						// TODO Verifica se é o mesmo tipo da função
						// TODO Faz colocar o valor no topo da pilha de execução
					}else{
						// TODO Faz retornar um ERRO
					}
					
				// se nova declaracao
				}else{
					pilhaTokens.push(token);
				}
				
			// RETORNO
			}else if(token.getValor().equals("retorno")){
				pilhaTokens.push(token);
			
			// PRINCIPAL
			}else if(token.getValor().equals("principal")){
				pilhaInstrucoes.add("INICIO\t\tK\t/00");
				pilhaTokens.clear();
			
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
							pilhaInstrucoes.add("LOOP_" + pilhaWhile.peek() + "\t\tOS\t=0");
						}else if(pilhaComando.peek() == TiposComando.SE){
							pilhaInstrucoes.add("IF_" + pilhaIF.peek().getIdIF() + "\t\tOS\t=0");
						}
						
						operadorA = operadorA + nomeResultAtrib;
					}else{
						operadorB = operadorB + nomeResultAtrib;
					}
				
				// comando SAIDA
				}else if(pilhaComando.peek() == TiposComando.SAIDA){
					// TODO Implementar
					
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
							//System.out.println("Poe na pilha: " + pilhaComando.peek());
						}
					}
				}else{
					pilhaTokens.push(token);
					pilhaComando.push(TiposComando.DECLARACAO);
					//System.out.println("Poe na pilha: " + pilhaComando.peek());
				}
				
				
				
			// IDENTIFICADOR
			}else if (token.getTipo() == TiposLexico.NOME && !PalavrasReservadas.reservada(token.getValor())){
				
				//System.out.println("identificador = " + token.getValor());
				
				// se ja esta na tabela de simbolos
				if(tabela.estaNaTabela(token.getValor())){
					//System.out.println("token = " + token.getValor());
					
					if(!pilhaComando.empty()){
						// ENTRADA
						if(pilhaComando.peek() == TiposComando.ENTRADA){
							// TODO Gerar a saida
							
						
						// ERRO - declaracao de variavel ja declarada
						}else if(pilhaComando.peek() == TiposComando.DECLARACAO){
							// TODO Erro, abortar o programa
							System.out.println("[ERRO] Variavel " + token.getValor() + " ja declarada!");
						
						// ATRIBUICAO
						}else{
							if(!pilhaComando.empty()){
								if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
									pilhaComando.push(TiposComando.DESCONHECIDO);
								}else{
									if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
										pilhaComando.pop();
										
										// TODO Gerar inicio para a chamada de atribuicao
										nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
										tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
										pilhaComando.push(TiposComando.ATRIBUICAO);
										//System.out.println("Poe na pilha: " + pilhaComando.peek());
									}
								}
							}else{
								// TODO Gerar inicio para a chamada de atribuicao
								nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
								tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
								pilhaComando.push(TiposComando.ATRIBUICAO);
								//System.out.println("Poe na pilha: " + pilhaComando.peek());
							}
						}
					
					// Atribuicao
					}else{
						if(!pilhaComando.empty()){
							if(pilhaComando.peek() == TiposComando.SE || pilhaComando.peek() == TiposComando.ENQUANTO){
								pilhaComando.push(TiposComando.DESCONHECIDO);
							}else{
								if(pilhaComando.peek() == TiposComando.DESCONHECIDO){
									pilhaComando.pop();
									
									// TODO Gerar inicio para a chamada de atribuicao
									nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
									tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
									pilhaComando.push(TiposComando.ATRIBUICAO);
									//System.out.println("Poe na pilha: " + pilhaComando.peek());
								}
							}
						}else{
							// TODO Gerar inicio para a chamada de atribuicao
							nomeResultAtrib = tabela.recuperaEntrada(token.getValor()).getEndereco();
							tipoResultAtrib = tabela.recuperaEntrada(token.getValor()).getTipo();
							pilhaComando.push(TiposComando.ATRIBUICAO);
							//System.out.println("Poe na pilha: " + pilhaComando.peek());
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
						// TODO Erro, abortar o programa
						System.out.println("[ERRO] Esperava-se um tipo no lugar de " + tipo.getValor());
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
						// TODO ATRIBUICAO
					}
				}else{
					// TODO ATRIBUICAO
				}
				
				
			// VERDADEIRO
			}else if(token.getValor().equals("verdadeiro")){
				if(pilhaComando.peek() == TiposComando.ATRIBUICAO){
					if(tipoResultAtrib == TiposSimbolos.BOOLEANO){
						pilhaInstrucoes.add("\t\tLD\t/01\t; " + nomeResultAtrib + " = TRUE");
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
					}else{
						// TODO Erro, aborta programa
						System.out.println("[ERRO] Impossivel atribuir o valor 'verdadeiro' para variavel nao booleana.");
					}
				}
				
			// FALSO
			}else if(token.getValor().equals("falso")){
				if(pilhaComando.peek() == TiposComando.ATRIBUICAO){
					if(tipoResultAtrib == TiposSimbolos.BOOLEANO){
						pilhaInstrucoes.add("\t\tLD\t/00\t; " + nomeResultAtrib + " = FALSE");
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
					}else{
						// TODO Erro, aborta programa
						System.out.println("[ERRO] Impossivel atribuir o valor 'verdadeiro' para variavel nao booleana.");
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
							//System.out.println("Poe na pilha: " + pilhaComando.peek());
							pilhaWhile.push(contaWhile);
							contaWhile ++;
						}
					}
				}else{
					pilhaComando.push(TiposComando.ENQUANTO);
					//System.out.println("Poe na pilha: " + pilhaComando.peek());
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
							//System.out.println("Poe na pilha: " + pilhaComando.peek());
							pilhaIF.push(new PilhaSeSenao(contaIF, false));
							contaIF ++;
						}
					}
				}else{
					pilhaComando.push(TiposComando.SE);
					//System.out.println("Poe na pilha: " + pilhaComando.peek());
					pilhaIF.push(new PilhaSeSenao(contaIF, false));
					contaIF ++;
				}
				

			// SENAO
			}else if(token.getValor().equals("senao")){
				pilhaInstrucoes.add("\t\tJP\tENDIF_" + pilhaIF.peek().getIdIF());
				pilhaInstrucoes.add("ELSE_" + pilhaIF.peek().getIdIF() + "\t\tOS\t=0\t; ELSE");
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
				//temp = 0;
				
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
					
				}
			
			// FIM DE COMANDO IF OU WHILE
			}else if(token.getValor().equals("}")){
				// fim SE
				if(pilhaComando.peek() == TiposComando.SE){
					
					if(!nextToken.getValor().equals("senao")){
						if(!pilhaIF.peek().isTemELSE()){
							pilhaInstrucoes.add("ELSE_" + pilhaIF.peek().getIdIF() + "\t\tOS\t=0\t; ELSE");
							pilhaInstrucoes.add("ENDIF_" + pilhaIF.peek().getIdIF() + "\t\tOS\t=0\t; END IF");
						}else{
							pilhaInstrucoes.add("ENDIF_" + pilhaIF.peek().getIdIF() + "\t\tOS\t=0\t; END IF");
						}
						pilhaComando.pop();
						pilhaIF.pop();
					}
					
				// fim ENQUANTO	
				}else if(pilhaComando.peek() == TiposComando.ENQUANTO){
					pilhaInstrucoes.add("\t\tJP\tLOOP_" + pilhaWhile.peek());
					pilhaInstrucoes.add("ENDLOOP_" + pilhaWhile.peek() + "\t\tOS\t=0\t; END WHILE");
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
						pilhaInstrucoes.add("\t\tGD\t=0\t; SAIDA = " + nomeResultAtrib);
						pilhaInstrucoes.add("\t\tMM\t" + nomeResultAtrib);
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
				// TODO Erro, abortar o programa
				System.out.println("[ERRO] Impossivel atribuir o valor " + token.getValor() + " para variavel de tipo booleano ou caracteres.");
			
			
			// chama EXPRESSAO
			}else if(aPercorrer.equals("expressao")){
				
			
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
								}else{
									// Nao faz nada
								}
							}
						}else{
							// TODO Erro, abortar o programa
							System.out.println("[ERRO] Variavel " + token.getValor() + " nao foi inicializada." );
						}
						
					}else{
						// TODO Erro, abortar o programa
						System.out.println("[ERRO] Variavel " + token.getValor() + " nao inteira, impossivel de calcular a expressao." );
					}
					
				}else{
					// TODO Erro, abortar o programa
					System.out.println("[ERRO] Variavel " + token.getValor() + " nao foi declarada." );
				}
			
			// OPERADOR *
			}else if(token.getValor().equals("*")){
				pilhaOperadores.push("*");
				
			// OPERADOR /
			}else if(token.getValor().equals("/")){
				pilhaOperadores.push("/");
			
			// OPERADOR +
			}else if(token.getValor().equals("+")){
				pilhaOperadores.push("+");
			
			// OPERADOR -
			}else if(token.getValor().equals("-")){
				pilhaOperadores.push("-");
			
			// (
			}else if(token.getValor().equals("(")){
				pilhaOperadores.push("(");
			
			// )
			}else if(token.getValor().equals(")")){
				pilhaOperadores.push(")");
				geraExpressao();
				
			// ERRO
			}else{
				// TODO Erro, abortar o programa
				System.out.println("[ERRO] Impossivel calcular a expressao.");
			}
		}
	}


	private void geraExpressao(){
		if(!pilhaOperadores.empty()){
			String op = pilhaOperadores.peek();
			if(op.equals("*") || op.equals("/") || op.equals("+") || op.equals("-")){
				geraOperacao(op);
				geraExpressao();
			}else if(op.equals("(")){
				// Não faz nada, só desempilha
				pilhaOperadores.pop();
			}else if(op.equals(")")){
				pilhaOperadores.pop();
				geraExpressao();
			}else{
				System.out.println("[ERRO] Impossivel realizar uma operacao com menos de dois operandos.");
			}
		}
	}
	
	
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
			pilhaInstrucoes.add("IF_EQ_" + pilhaIF.peek().getIdIF() + "\t\tOS\t=0");
			
		}else if(tipoComparacao.equals("!=")){
			pilhaInstrucoes.add("\t\tLD\t" + operadorA + "\t; IF ( " + operadorA + " != " + operadorB + " )");
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJZ\tELSE_" + pilhaIF.peek().getIdIF());
		}
		
		operadorA = "";
		operadorB = "";
	}
	
	
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
			pilhaInstrucoes.add("LOOP_EQ_" + pilhaWhile.peek() + "\t\tOS\t=0");
			
		}else if(tipoComparacao.equals("!=")){
			pilhaInstrucoes.add("\t\tOS\t=0" + "\t; WHILE ( " + operadorA + " != " + operadorB + " )");
			pilhaInstrucoes.add("\t\tLD\t" + operadorA);
			pilhaInstrucoes.add("\t\t-\t" + operadorB);
			pilhaInstrucoes.add("\t\tJZ\tENDLOOP_" + pilhaWhile.peek());
		}
		
		operadorA = "";
		operadorB = "";
	}
	
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

	
	public void criaArquivo(){
		try{
			FileWriter writer = new FileWriter(this.arquivoMVN);
			PrintWriter saida = new PrintWriter(writer);
			
			
			for(int i=0; i < vetorEscopos.size(); i++){
				TabelaSimbolos tab = vetorEscopos.get(i);
				//System.out.println(tab.getEntradas());
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
			
			
			for(int i=0; i < pilhaDeclaracoes.size(); i++){
				this.output = this.output + (String)pilhaDeclaracoes.get(i) + "\n";
			}
			
			for(int i=0; i < pilhaInstrucoes.size(); i++){
				this.output = this.output + (String)pilhaInstrucoes.get(i) + "\n";
			}
			
			this.output = this.output + "FIM\t\tHM\t/00" + "\n";
			
			
			for(int i=0; i <= maxTemp; i++){
				this.output = this.output + "TEMP_" + i + "\t\tK\t/00\n"; 
			}
			
			
			System.out.println("ARQUIVO DE SAIDA\n"+ this.output);
			saida.print(this.output);
			
			saida.close();
			writer.close(); 
		}catch(Exception e){
			System.out.println("[ERRO] Foi impossivel criar o arquivo de saida.");
			e.printStackTrace();
		}
	}
	
}