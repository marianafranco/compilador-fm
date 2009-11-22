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
	
	public boolean geraCodigo(APE automato, FluxoTokens tokensTokens) {
		
		Token token;
		
		Stack pilha = new Stack<PilhaEstadoSubmaquina>();
		
		if(tokensTokens.getTamanho() < 2){
			return false;
		}
		
		token = tokensTokens.recuperaToken();
		
		AFD progr = automato.getSubmaquina("programa");
		AFD comando = automato.getSubmaquina("comando");
		AFD expr = automato.getSubmaquina("expressao");
		AFD tipo = automato.getSubmaquina("tipo");
		AFD ident = automato.getSubmaquina("identificador");
		AFD numero = automato.getSubmaquina("numero");
		AFD booleano = automato.getSubmaquina("booleano");
		AFD letra = automato.getSubmaquina("letra");
		AFD digito = automato.getSubmaquina("digito");
		
		AFD submaquina = new AFD();
		PilhaEstadoSubmaquina conteudoPilha; 
		
		// Inicializa pilha
		pilha.push(new PilhaEstadoSubmaquina(0, "programa"));
		
		boolean fimTokensEFinal = true;
		
		// Enquanto não chegamos ao final dos tokens
		while(fimTokensEFinal) {
			
			conteudoPilha = (PilhaEstadoSubmaquina) pilha.pop();
			
			// se autômato PROGR
			if(conteudoPilha.getSubmaquina().equals("programa")){
				submaquina = progr;
			// se autômato COMANDO
			}else if(conteudoPilha.getSubmaquina().equals("comando")){
				submaquina = comando;
			}// se autômato EXPR
			else if(conteudoPilha.getSubmaquina().equals("expressao")){
				submaquina = expr;
			// se não é WIRTH nem EXPR -> ERRO
			}else if(conteudoPilha.getSubmaquina().equals("tipo")){
				submaquina = tipo;
			}// se autômato EXPR
			else if(conteudoPilha.getSubmaquina().equals("identificador")){
				submaquina = ident;
			// se não é WIRTH nem EXPR -> ERRO
			}else if(conteudoPilha.getSubmaquina().equals("numero")){
				submaquina = numero;
			}// se autômato EXPR
			else if(conteudoPilha.getSubmaquina().equals("booleano")){
				submaquina = booleano;
			// se não é WIRTH nem EXPR -> ERRO
			}else if(conteudoPilha.getSubmaquina().equals("letra")){
				submaquina = letra;
			}// se autômato EXPR
			else if(conteudoPilha.getSubmaquina().equals("digito")){
				submaquina = digito;
			// se não é WIRTH nem EXPR -> ERRO
			}
			else{
				// TODO Acrescentar mensagem de erro 
				return false;
			}
			
			submaquina.setEstadoAtivo(conteudoPilha.getEstado());
			boolean getNewToken = false;
			String aPercorrer = "";
			
			// Checagem do numero de possiveis mudanas de estado
			int possiveisTransicoes = 0;
			if (submaquina.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = '"' + token.getValor() + '"';
				
				if(tokensTokens.getTamanho() > 0){
					getNewToken = true;
				}
			}
			if (submaquina.temTransicao("programa") && progr.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "programa";
			}
			if (submaquina.temTransicao("comando") && comando.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "comando";
			}
			if (submaquina.temTransicao("expressao") && expr.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "expressao";
			}
			if (submaquina.temTransicao("tipo") && tipo.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "tipo";
			}
			if (submaquina.temTransicao("identificador") && ident.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "identificador";
			}
			if (submaquina.temTransicao("numero") && numero.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "numero";
			}
			if (submaquina.temTransicao("booleano") && booleano.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "booleano";
			}
			if (submaquina.temTransicao("letra") && letra.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "letra";
			}
			if (submaquina.temTransicao("digito") && digito.temTransicao('"' + token.getValor() + '"')) {
				possiveisTransicoes++;
				aPercorrer = "digito";
			}
			
			if (possiveisTransicoes == 1) {
				submaquina.percorre(aPercorrer);
				conteudoPilha.setEstado(submaquina.getEstadoAtivo());
				// Caso
				if (!submaquina.estadoAtivoFinal() || submaquina.temTransicao('"' + token.getValor() + '"')) {
					pilha.push(new PilhaEstadoSubmaquina(conteudoPilha.getEstado(), conteudoPilha.getSubmaquina()));
					if (aPercorrer == "programa" || aPercorrer == "comando" || aPercorrer == "expressao" || 
							aPercorrer == "tipo" || aPercorrer == "identificador" || aPercorrer == "numero" || 
							aPercorrer == "booleano" || aPercorrer == "letra" || aPercorrer == "digito") {
						pilha.push(new PilhaEstadoSubmaquina(0, aPercorrer));
					}
				}
				if (getNewToken == true) {
					token = tokensTokens.recuperaToken();
				}
			}
			else {
				System.out.println("[ERRO] N‹o h‡ transicoes possiveis ou exite mais de uma transicao");
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