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
		
		Pilha conteudoPilha = new Pilha(0, "WIRTH");
		
		pilha.push(conteudoPilha);
		
		// Enquanto não chegamos ao final dos tokens
		while(tokensTokens.getTamanho() > 0 && !pilha.empty()){
			proxToken = tokensTokens.recuperaToken();
			
			// percorre autômato
			conteudoPilha = (Pilha)pilha.pop();
			
			// se autômato WIRTH
			if(conteudoPilha.getSubmaquina().equals("WIRTH")){
				wirth.setEstadoAtivo(conteudoPilha.getEstado());
				
				switch (token.getTipo()){
				
				case Tipo.NTERM:
					if(wirth.temTransicao("NTERM")){
						wirth.percorre("NTERM");
					}else{
						System.out.println("[ERRO] Não tem transição com " + token.getValor());
					}
					break;
				case Tipo.TERM:	
				
					
				case Tipo.ESPECIAL:
				
				
					
				case Tipo.DESCONHECIDO:
				
				}
				
				// se for nterminal ou terminal, não usa o valor direto
				wirth.percorre(token.getValor());
			
				
			// se autômato EXPR
			}else if (conteudoPilha.getSubmaquina().equals("EXPR")){
				
			
				
			// se não é WIRTH nem EXPR -> ERRO
			}else{
				return false;
			}
			
			
			// verifica se é final
			
			// se é final retira da pilha
			
			// senão atualiza topo com o novo estado
		}
		
		
		
		return false;
	}
}
