package compilador.metacompilador;

import java.util.Stack;

import compilador.estruturas.AFD;
import compilador.estruturas.APE;
import compilador.estruturas.Estado;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.PilhaEstadoSubmaquina;
import compilador.estruturas.PilhaEstados;
import compilador.estruturas.TiposMeta;
import compilador.estruturas.Token;
import compilador.estruturas.Transicao;


public class PercorreMetaAPE {
	
	private Stack<PilhaEstados> pilhaGeraAPE;	// pilha para gerar os estados no novo APE
	private int cs;								// estado corrente
	private int ns;								// proximo estado
	private AFD submaquina;		
	private Estado estado;
	
	public PercorreMetaAPE(){
		this.pilhaGeraAPE = new Stack<PilhaEstados>();
		this.cs = 0;
		this.ns = 1;
	}
	
	public boolean executa(APE automato, FluxoTokens tokensTokens, APE newAutomato){
		
		Token token;
		Token proxToken;
		
		Stack pilha = new Stack<PilhaEstadoSubmaquina>();
		
		if(tokensTokens.getTamanho() < 2){
			return false;
		}
		
		token = tokensTokens.recuperaToken();
		
		AFD wirth = automato.getSubmaquina("WIRTH");
		AFD expr = automato.getSubmaquina("EXPR");
		
		AFD submaquina;
		String tokenValor = "";
		PilhaEstadoSubmaquina conteudoPilha; 
		AFD newSubmaquina = new AFD();	// usada para a cria��o do novo APE
		
		// Inicializa pilha
		pilha.push(new PilhaEstadoSubmaquina(0, "WIRTH"));
		
		// Enquanto n�o chegamos ao final dos tokens
		while(tokensTokens.getTamanho() >= 0 && !pilha.empty()){
			
			conteudoPilha = (PilhaEstadoSubmaquina) pilha.pop();
			//System.out.println("pilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
			//System.out.println("token " + token.getValor());
			
			// se aut�mato WIRTH
			if(conteudoPilha.getSubmaquina().equals("WIRTH")){
				submaquina = wirth;
				
			// se aut�mato EXPR
			}else if(conteudoPilha.getSubmaquina().equals("EXPR")){
				submaquina = expr;
				
			// se n�o � WIRTH nem EXPR -> ERRO
			}else{
				// TODO Acrescentar mensagem de erro 
				return false;
			}
			
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			
			switch(token.getTipo()){
			
			case TiposMeta.NTERM:
				tokenValor = "NTERM";
				break;
			case TiposMeta.TERM:
				tokenValor = "TERM";
				break;
			case TiposMeta.ESPECIAL:
				tokenValor = token.getValor();
				break;
			case TiposMeta.DESCONHECIDO:
				// TODO tratar erro
				System.out.println("[ERRO] N�o tem transi��o com " + token.getValor());
				return false;
			}
			
			if(submaquina.temTransicao(tokenValor)){
				
				// Gerar as transi��es no AFD
				geraAPE(newAutomato, conteudoPilha.getSubmaquina(), token);
				
				submaquina.percorre(tokenValor);
				
				// verifica se � final
				if(!submaquina.estadoAtivoFinal()){
					// sen�o atualiza topo com o novo estado
					conteudoPilha.setEstado(submaquina.getEstadoAtivo());
					pilha.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
					//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
					
					if(tokensTokens.getTamanho() > 0){
						token = tokensTokens.recuperaToken();
					}
					
				}else{
					
					if(tokensTokens.getTamanho() > 0){
						token = tokensTokens.recuperaToken();
						
						switch (token.getTipo()){
						case TiposMeta.NTERM:
							tokenValor =  "NTERM";
							break;
						case TiposMeta.TERM:
							tokenValor = "TERM";
							break;
						case TiposMeta.ESPECIAL:
							tokenValor = token.getValor();
							break;
						case TiposMeta.DESCONHECIDO:
							// TODO tratar erro
							System.out.println("[ERRO] N�o tem transi��o com " + token.getValor());
							return false;
						}
						
						// Se existir transi��o com o pr�ximo, reempilha o estado atual
						if(expr.temTransicao(tokenValor)){
							conteudoPilha.setEstado(expr.getEstadoAtivo());
							pilha.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
							//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
						
						// Sen�o, reincializa a pilha se pilha vazia  
						}else{
							if(pilha.empty()){
								pilha.push(new PilhaEstadoSubmaquina(0, "WIRTH"));
								//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
							}else{
								//System.out.println("entrou no final sem transi��o com o proximo");
							}
						}
					}
				}
				
			}else if(submaquina.temTransicao("EXPR")){
				// empilha (proxEstado, submaquina) e (0, expr)
				submaquina.percorre("EXPR");
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				pilha.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
				//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
				pilha.push(new PilhaEstadoSubmaquina(0, "EXPR"));
				//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
				
			}else{
				// TODO tratar erro
				System.out.println("[ERRO] N�o tem transi��o o NTERM " + token.getValor());
				return false;
			}
			
		}
		
		return true;
	}
	
	
	private void geraAPE(APE automato, String submaquinaWirth, Token token){
		
		// IN
		if(submaquinaWirth.equals("WIRTH") && token.getTipo() == TiposMeta.NTERM){
			submaquina = new AFD(token.getValor());
			this.cs = 0;
			this.ns = 1;
			this.pilhaGeraAPE.push(new PilhaEstados(this.cs, this.ns));
			this.ns ++;
			
		}else{
			switch(token.getTipo()){
			// A
			case TiposMeta.TERM:
				// Verifica se ja n�o existe o estado antes de criar
				if(submaquina.procuraEstado(this.cs) == -1){
					estado = new Estado(this.cs, false);
					estado.adicionaTransicao(new Transicao(this.ns, token.getValor()));
					submaquina.adicionaEstado(estado);
				}else{
					submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(this.ns, token.getValor()));
				}
				this.cs = this.ns;
				this.ns ++;
				break;
				
			// B
			case TiposMeta.NTERM:
				// Verifica se ja n�o existe o estado antes de criar
				if(submaquina.procuraEstado(this.cs) == -1){
					estado = new Estado(this.cs, false);
					estado.adicionaTransicao(new Transicao(this.ns, token.getValor()));
					submaquina.adicionaEstado(estado);
				}else{
					submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(this.ns, token.getValor()));
				}
				this.cs = this.ns;
				this.ns ++;
				break;
				
			case TiposMeta.ESPECIAL:
				
				// C
				if(token.getValor().equals("|")){
					PilhaEstados conteudoPilha = this.pilhaGeraAPE.peek();
					// Verifica se ja n�o existe o estado antes de criar
					if(submaquina.procuraEstado(this.cs) == -1){
						estado = new Estado(this.cs, false);
						estado.adicionaTransicao(new Transicao(conteudoPilha.getR(), "e"));	// gera transi��o vazia
						submaquina.adicionaEstado(estado);
					}else{
						submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(conteudoPilha.getR(), "e"));	// gera transi��o vazia
					}
					this.cs = conteudoPilha.getL();
					
				// D
				}else if(token.getValor().equals("(")){
					this.pilhaGeraAPE.push(new PilhaEstados(this.cs, this.ns));
					this.ns ++;
					
				// E
				}else if(token.getValor().equals("[")){
					// Verifica se ja n�o existe o estado antes de criar
					if(submaquina.procuraEstado(this.cs) == -1){
						estado = new Estado(this.cs, false);
						estado.adicionaTransicao(new Transicao(this.ns, "e"));	// gera transi��o vazia
						submaquina.adicionaEstado(estado);
					}else{
						submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(this.ns, "e"));	// gera transi��o vazia
					}
					this.pilhaGeraAPE.push(new PilhaEstados(this.cs, this.ns));
					this.ns ++;
					
				// F
				}else if(token.getValor().equals("{")){
					// Verifica se ja n�o existe o estado antes de criar
					if(submaquina.procuraEstado(this.cs) == -1){
						estado = new Estado(this.cs, false);
						estado.adicionaTransicao(new Transicao(this.ns, "e"));	// gera transi��o vazia
						submaquina.adicionaEstado(estado);
					}else{
						submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(this.ns, "e"));	// gera transi��o vazia
					}
					this.pilhaGeraAPE.push(new PilhaEstados(this.ns, this.ns));
					this.cs = this.ns;
					this.ns ++;
					
				// G
				}else if(token.getValor().equals(")") || token.getValor().equals("]") || token.getValor().equals("}")){
					// Verifica se ja n�o existe o estado antes de criar
					if(submaquina.procuraEstado(this.cs) == -1){
						estado = new Estado(this.cs, false);
						estado.adicionaTransicao(new Transicao(this.pilhaGeraAPE.peek().getR(), "e"));	// gera transi��o vazia
						submaquina.adicionaEstado(estado);
					}else{
						submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(this.pilhaGeraAPE.peek().getR(), "e"));	// gera transi��o vazia
					}
					this.cs = this.pilhaGeraAPE.pop().getR();
					
				// H
				}else if(token.getValor().equals(".")){
					
					// Verifica se ja n�o existe o estado antes de criar
					if(submaquina.procuraEstado(this.cs) == -1){
						estado = new Estado(this.cs, false);
						estado.adicionaTransicao(new Transicao(this.pilhaGeraAPE.peek().getR(), "e"));	// gera transi��o vazia
						submaquina.adicionaEstado(estado);
					}else{
						submaquina.getEstado(this.cs).adicionaTransicao(new Transicao(this.pilhaGeraAPE.peek().getR(), "e"));	// gera transi��o vazia
					}
					
					// Coloca o estado 1 como final
					if(submaquina.procuraEstado(1) == -1){
						estado = new Estado(1, true);
						submaquina.adicionaEstado(estado);
					}else{
						submaquina.getEstado(1).setAceitacao(true);
					}
					
					//System.out.println("ENTROU");
					automato.adicionaSubmaquina(submaquina);
				}
				break;
			}
		}
	}
	
}
