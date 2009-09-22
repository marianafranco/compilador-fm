package compilador.lexico;

import java.io.IOException;
import java.io.Reader;

import compilador.lexico.estruturas.AFD;
import compilador.lexico.estruturas.FluxoTokens;
import compilador.lexico.estruturas.TabelaSimbolos;
import compilador.lexico.estruturas.Tipo;

public class PercorreAFD {

	
	public PercorreAFD(){
		
	}
	
	
	public boolean executa(AFD automato, Reader arquivoFonte, FluxoTokens tokensTokens, 
			TabelaSimbolos tabelaSimbolos){
		
		try{
			int atual; // caractere atual
			int proximo; // proximo caractere
			
			String token = ""; // token
			
			// Linha e coluna que estam sendo processadas
			int linha = 1;
			int coluna = 1;
			
			// Le o primeiro caractere
			atual = arquivoFonte.read();
					
			// Enquanto nao chegamos ao fim do arquivo
			while(atual != -1) {
				
				// Guarda o proximo caractere
				proximo = arquivoFonte.read();
				
				// Se é caractere de final de linha
				if (atual == '\n') {
					linha++;
					coluna = 0;
					// volta para o estado inicial
					automato.setEstadoAtivo(0);
				
				}else {
					// Se não é espaco ou tabulacao
					if (atual != ' ' && atual != '\t'){
						
						// Coloca mais um caracter no token
						token = token + (char) atual;
						
						// Verifica se existe transicao recebendo o caractere atual
						if (automato.temTransicao((char) atual)) {
							
							// Faz o automato andar para o proximo estado
							automato.percorre((char) atual);
							
							// Verifica se tem transicao com o proximo caracteres
							if (automato.temTransicao((char) proximo)) {
								
								// Se a transicao nao é para um estado final
								if(!automato.transicaoFinal((char) proximo)){
									// gera token e volta para o estado inicial
									adicionaToken(tokensTokens, tabelaSimbolos, token, automato.getTipo(), linha, coluna);
									token = "";
									automato.setEstadoAtivo(0);
								}
							}
							else {
								//gera token e volta para o estado inicial
								adicionaToken(tokensTokens, tabelaSimbolos, token, automato.getTipo(), linha, coluna);
								token = "";
								automato.setEstadoAtivo(0);
							}
						}
						
						// Se não existe transicao com o caractere atual
						else {
							//gera token e volta para o estado inicial
							adicionaToken(tokensTokens, tabelaSimbolos, token, automato.getTipo(), linha, coluna);
							token = "";
							automato.setEstadoAtivo(0);
						}
					}
				}
				
				// Pega proximo caracter
				atual = proximo;
				
				// Atualiza coluna
				coluna++;
			}
			return true;
		
		}catch(Exception e){
			//e.printStackTrace();
			return false;
		}	
	}
	
	public void adicionaToken(FluxoTokens tokensTokens, TabelaSimbolos tabelaSimbolos, String token, 
			int tipo, int linha, int coluna){
		
		//System.out.println("TOKEN = " + token);
		
		// Caso seja um numero ou um caracter, nao e necessaria uma entrada na tabela
		if (tipo == Tipo.NUMERO || tipo == Tipo.ESPECIAL) {
			tokensTokens.adicionaToken(token, -1);
		}
		// Se for uma string, colocamos na tabela
		else if (tipo == Tipo.NOME) {
			int posicao = tabelaSimbolos.getEntradas();
			tokensTokens.adicionaToken(token, posicao);
			tabelaSimbolos.adicionaEntrada (posicao, token.toString(), tipo, linha, coluna);
		}
	}
	
}
