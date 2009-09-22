package compilador.lexico;

import java.io.IOException;
import java.io.Reader;

import compilador.lexico.estruturas.AFD;
import compilador.lexico.estruturas.FluxoTokens;
import compilador.lexico.estruturas.TabelaSimbolos;

public class PercorreAFD {

	
	public PercorreAFD(){
		
	}
	
	public boolean executa(AFD automato, Reader arquivoFonte, FluxoTokens tokensTokens, TabelaSimbolos tabelaSimbolos){
		
		try{
			int atual = -1; // Buffer de caracteres lidos
			int proximo = -1; // Buffer de caracteres lidos
			String token = ""; // Valor to token
			int tipo = -1; // Indica se um token foi finalizado
			int linha = 1; // Guarda a linha sendo processada
			int coluna = 1; // Guarda a coluna sendo processada
			
			// Le o primeiro caracter
			atual = arquivoFonte.read();
					
			// Enquanto nao chegamos ao fim do arquivo
			while(atual != -1) {
				
				//System.out.println((char)atual);
				
				// Guarda o proximo caracter
				proximo = arquivoFonte.read();
				
				if (atual == '\n') {
					linha++;
					coluna = 0;
					
					// volta para o estado inicial
					automato.setEstadoAtivo(0);
				}
				else {
				if (atual != ' ' && atual != '\t'){
					
					// Coloca mais um caracter no token
					token = token + (char) atual;
					
					if (automato.temTransicao((char) atual)) {
						
						// Faz o automato andar para o proximo estado
						automato.percorre((char) atual);
						
						// Verifica se tem transicao com o proximo
						if (automato.temTransicao((char) proximo)) {
							
							// Verifica se a transicao é para um estado final
							if(automato.transicaoFinal((char) proximo)){
								// nao gera token
							}else{
								// gera token e volta para o estado inicial
								System.out.println("TOKEN = " + token);
								token = "";
								automato.setEstadoAtivo(0);
							}
							
						}
						else {
							//gera token e volta para o estado inicial
							System.out.println("TOKEN = " + token);
							token = "";
							automato.setEstadoAtivo(0);
						}
					}
					else {
						//gera token e volta para o estado inicial
						System.out.println("TOKEN = " + token);
						token = "";
						automato.setEstadoAtivo(0);
						
					}
				}
				}
				
				
					
					// Caso seja um numero ou um caracter, nao e necessaria uma entrada na tabela
					//if (tipo == 1 || tipo == 3) {
				//		tokensTokens.adicionaToken(token, -1);
					//}
					// Se for uma string, colocamos na tabela
					//else if (tipo == 2) {
				//		int posicao = tabelaSimbolos.getEntradas();
						//tokensTokens.adicionaToken(token, posicao);
						//tabelaSimbolos.adicionaEntrada (posicao, token.toString(), tipo, linha, coluna);
					//}
					
					// Zera token
					//token = "";
				
				// Pega proximo caracter
				atual = proximo;
				
				// Atualiza coluna
				coluna++;
			}
					
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
}
