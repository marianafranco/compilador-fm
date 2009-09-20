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
			int tipo = 0; // Tipo do token
			int fechouToken = -1; // Indica se um token foi finalizado
			int linha = 1; // Guarda a linha sendo processada
			int coluna = 1; // Guarda a coluna sendo processada
			
			// Le o primeiro caracter
			atual = arquivoFonte.read();
					
			// Enquanto nao chegamos ao fim do arquivo
			while(atual != -1) {
				
				System.out.println((char)atual);
				
				// Guarda o proximo caracter
				proximo = arquivoFonte.read();
				
				if (atual == '\n') {
					linha++;
					coluna = 0;
				}
				// Se o caracter nao for um espaco, uma quebra de linha ou uma tabulacao
				else if (atual != ' ' && atual != '\t') {
					
					// Coloca mais um caracter no token
					token = token + (char) atual;
					
					// Simula o automato com a entrada
					fechouToken = automato.recebeEntrada((char) atual, (char) proximo, tipo);
					
					// Caso o token tenha acabado
					if (fechouToken != -1) {
						
						tipo = fechouToken;
						
						// Caso seja um numero ou um caracter, nao e necessaria uma entrada na tabela
						if (tipo == 1 || tipo == 3) {
							tokensTokens.adicionaToken(token, -1);
						}
						// Se for uma string, colocamos na tabela
						else if (tipo == 2) {
							int posicao = tabelaSimbolos.ultimaPosicao();
							tokensTokens.adicionaToken(token, posicao);
							tabelaSimbolos.adicionaEntrada (posicao, token.toString(), tipo, linha, coluna);
						}
						
						// Zera token
						token = "";
					}
				}
				
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
