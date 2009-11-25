package compilador.lexico;

import java.io.IOException;
import java.io.Reader;

import compilador.estruturas.AFD;
import compilador.estruturas.FluxoTokens;
import compilador.estruturas.PalavrasReservadas;
import compilador.estruturas.TabelaSimbolos;
import compilador.estruturas.TiposLexico;

public class PercorreAFD {

	
	public PercorreAFD(){
		
	}
	
	
	public boolean executa(AFD automato, Reader arquivoFonte, FluxoTokens tokensTokens){
		
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
							
							// Se tem transição com espaço (string), adiciona o proximo ao token
							if (proximo == ' ' && automato.temTransicao((char) proximo)){
								token = token + (char) proximo;
							}
							
							// Verifica se tem transicao com o proximo caractere
							if (automato.temTransicao((char) proximo)) {
								
								// Se a transicao nao é para um estado final
								//if(!automato.transicaoFinal((char) proximo)){
									// gera token e volta para o estado inicial
								//	adicionaToken(tokensTokens, tabelaSimbolos, token, automato.getTipo(), linha, coluna);
								//	token = "";
								//	automato.setEstadoAtivo(0);
								//}
							}
							else {
								//gera token e volta para o estado inicial
								adicionaToken(tokensTokens, token, automato.getTipo(), linha, coluna);
								token = "";
								automato.setEstadoAtivo(0);
							}
						}
						
						// Se não existe transicao com o caractere atual
						else {
							//gera token e volta para o estado inicial
							adicionaToken(tokensTokens, token, automato.getTipo(), linha, coluna);
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
	
	public void adicionaToken(FluxoTokens tokensTokens, String token, 
			int tipo, int linha, int coluna){
		
		//System.out.println("TOKEN = " + token);
		//System.out.println("TIPO = " + tipo);
		
		// Caso seja um numero ou um caracter, nao e necessaria uma entrada na tabela
		if (tipo == TiposLexico.NUMERO) {
			tokensTokens.adicionaToken(token, tipo, linha, coluna);
		}
		else if (tipo == TiposLexico.ESPECIAL) {
			tokensTokens.adicionaToken(token, tipo, linha, coluna);
		}
		
		// Se for uma string, talvez colocamos na tabela
		else if (tipo == TiposLexico.NOME) {
			
			// Verifica se é palavra reservada
			if (PalavrasReservadas.reservada(token) == false) {
				tokensTokens.adicionaToken(token, tipo, linha, coluna);
			}
			else {
				tokensTokens.adicionaToken(token, TiposLexico.RESERVADO, linha, coluna);
			}
			
		// Se for uma string
		}else if (tipo == TiposLexico.STRING){
			tokensTokens.adicionaToken(token, tipo);
		}
	}
	
}
