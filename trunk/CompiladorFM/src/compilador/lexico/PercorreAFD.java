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
			int buffer[] = null; // Buffer de caracteres lidos
			char token[] = null; // Nome to token
			int tipo = 0; // Tipo do token
			boolean fechouToken = false; // Indica se um token foi finalizado
			int linha = 1; // Guarda a linha sendo processada
			int coluna = 1; // Guarda a coluna sendo processada
			
			// Le o primeiro caracter
			buffer[0] = arquivoFonte.read();
					
			// Enquanto nao chegamos ao fim do arquivo
			while(buffer[0] != -1) {
				
				// Guarda o pr—ximo caracter
				buffer[1] = arquivoFonte.read();
				
				if (buffer[0] == '\n') {
					linha++;
					coluna = 0;
				}
				// Se o caracter nao for um espaco, uma quebra de linha ou uma tabulacao
				else if (buffer[0] != ' ' && buffer[0] != '\t') {
					
					// Coloca mais um caracter no token
					token[token.length] = (char) buffer[0];
					
					// Simula o automato com a entrada
					automato.recebeEntrada((char) buffer[0], (char) buffer[1], tipo, fechouToken);
					
					// Caso o token tenha acabado
					if (fechouToken == true) {
						
						// Caso seja um numero ou um caracter, nao e necessaria uma entrada na tabela
						if (tipo == 1 || tipo == 3) {
							tokensTokens.adicionaToken(token.toString(), -1);
						}
						// Se for uma string, colcamos entrada na tabela
						else if (tipo == 2) {
							int posicao = tabelaSimbolos.ultimaPosicao();
							
							tokensTokens.adicionaToken(token.toString(), posicao);
							tabelaSimbolos.adicionaEntrada (posicao, token.toString(), tipo, linha, coluna);
						}
						
						// Zera token
						token = null;
					}
				}
				
				// Pega proximo caracter
				buffer[0] = buffer[1];
				
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
