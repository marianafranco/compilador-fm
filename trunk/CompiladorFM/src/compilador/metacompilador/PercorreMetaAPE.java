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
		
		// Enquanto n�o chegamos ao final dos tokens
		while(tokensTokens.getTamanho() > 0 && !pilha.empty()){
			proxToken = tokensTokens.recuperaToken();
			
			// percorre aut�mato
			conteudoPilha = (Pilha)pilha.pop();
			
			// se aut�mato WIRTH
			if(conteudoPilha.getSubmaquina().equals("WIRTH")){
				wirth.setEstadoAtivo(conteudoPilha.getEstado());
				
				switch (token.getTipo()){
				
				case Tipo.NTERM:
					if(wirth.temTransicao("NTERM")){
						wirth.percorre("NTERM");
					}else{
						System.out.println("[ERRO] N�o tem transi��o com " + token.getValor());
					}
					break;
				case Tipo.TERM:	
				
					
				case Tipo.ESPECIAL:
				
				
					
				case Tipo.DESCONHECIDO:
				
				}
				
				// se for nterminal ou terminal, n�o usa o valor direto
				wirth.percorre(token.getValor());
			
				
			// se aut�mato EXPR
			}else if (conteudoPilha.getSubmaquina().equals("EXPR")){
				
			
				
			// se n�o � WIRTH nem EXPR -> ERRO
			}else{
				return false;
			}
			
			
			// verifica se � final
			
			// se � final retira da pilha
			
			// sen�o atualiza topo com o novo estado
		}
		
		
		
		return false;
	}
}
