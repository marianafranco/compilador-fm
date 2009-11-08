package compilador.metacompilador;

import java.util.Stack;

import compilador.metacompilador.estruturas.AFD;
import compilador.metacompilador.estruturas.APE;
import compilador.metacompilador.estruturas.FluxoTokens;
import compilador.metacompilador.estruturas.Pilha;
import compilador.metacompilador.estruturas.Tipo;
import compilador.metacompilador.estruturas.Token;

public class PercorreMetaAPE {
	
	public PercorreMetaAPE(){
		
	}
	
	public boolean executa(APE automato, FluxoTokens tokensTokens){
		
		Token token;
		Token proxToken;
		
		Stack pilha = new Stack<Pilha>();
		
		if(tokensTokens.getTamanho() < 2){
			return false;
		}
		
		token = tokensTokens.recuperaToken();
		
		AFD wirth = automato.getSubmaquina("WIRTH");
		AFD expr = automato.getSubmaquina("EXPR");
		
		AFD submaquina;
		String tokenValor = "";
		Pilha conteudoPilha; 
		
		// Inicializa pilha
		pilha.push(new Pilha(0, "WIRTH"));
		
		// Enquanto não chegamos ao final dos tokens
		while(tokensTokens.getTamanho() >= 0 && !pilha.empty()){
			//proxToken = tokensTokens.recuperaToken();
			
			conteudoPilha = (Pilha) pilha.pop();
			System.out.println("pilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
			System.out.println("token " + token.getValor());
			
			// se autômato WIRTH
			if(conteudoPilha.getSubmaquina().equals("WIRTH")){
				submaquina = wirth;
				
			// se autômato EXPR
			}else if(conteudoPilha.getSubmaquina().equals("EXPR")){
				submaquina = expr;
				
			// se não é WIRTH nem EXPR -> ERRO
			}else{
				// TODO Acrescentar mensagem de erro 
				return false;
			}
			
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			
			switch(token.getTipo()){
			
			case Tipo.NTERM:
				tokenValor = "NTERM";
				break;
			case Tipo.TERM:
				tokenValor = "TERM";
				break;
			case Tipo.ESPECIAL:
				tokenValor = token.getValor();
				break;
			case Tipo.DESCONHECIDO:
				// TODO tratar erro
				System.out.println("[ERRO] Não tem transição com " + token.getValor());
				return false;
			}
			
			if(submaquina.temTransicao(tokenValor)){
				submaquina.percorre(tokenValor);
				
				// verifica se é final
				if(!submaquina.estadoAtivoFinal()){
					// senão atualiza topo com o novo estado
					conteudoPilha.setEstado(submaquina.getEstadoAtivo());
					pilha.push(new Pilha(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
					//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
					
					if(tokensTokens.getTamanho() > 0){
						token = tokensTokens.recuperaToken();
					}
					
				}else{
					
					if(tokensTokens.getTamanho() > 0){
						token = tokensTokens.recuperaToken();
						
						switch (token.getTipo()){
						case Tipo.NTERM:
							tokenValor =  "NTERM";
							break;
						case Tipo.TERM:
							tokenValor = "TERM";
							break;
						case Tipo.ESPECIAL:
							tokenValor = token.getValor();
							break;
						case Tipo.DESCONHECIDO:
							// TODO tratar erro
							System.out.println("[ERRO] Não tem transição com " + token.getValor());
							return false;
						}
						
						// Se existir transição com o próximo, reempilha o estado atual
						if(expr.temTransicao(tokenValor)){
							conteudoPilha.setEstado(expr.getEstadoAtivo());
							pilha.push(new Pilha(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
							//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
						
						// Senão, reincializa a pilha se pilha vazia  
						}else{
							if(pilha.empty()){
								pilha.push(new Pilha(0, "WIRTH"));
								//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
							}else{
								System.out.println("entrou no final sem transição com o proximo");
								//pilha.pop();
							}
						}
					}
				}
				
			}else if(submaquina.temTransicao("EXPR")){
				// empilha (proxEstado, submaquina) e (0, expr)
				submaquina.percorre("EXPR");
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				pilha.push(new Pilha(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
				//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
				pilha.push(new Pilha(0, "EXPR"));
				//System.out.println("empilha: (" + conteudoPilha.getEstado() + ", " + conteudoPilha.getSubmaquina() + ")");
				
			}else{
				// TODO tratar erro
				System.out.println("[ERRO] Não tem transição o NTERM " + token.getValor());
				return false;
			}
			
		}
		
		return true;
	}
}
