package compilador.metacompilador.lexico;

import java.io.FileReader;
import java.io.IOException;

import compilador.metacompilador.estruturas.FluxoTokens;
import compilador.metacompilador.estruturas.AFD;

public class PercorreAFD {

	
	public PercorreAFD(){
		
	}
	
	
	public boolean executa(AFD automato, FileReader arquivoFonte, FluxoTokens tokensTokens){
		
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
			while(atual != -1 ) {
				
				// Guarda o proximo caractere
				proximo = arquivoFonte.read();
				
				// Se é caractere de final de linha
				if (atual == '\n') {
					linha++;
					coluna = 0;
					token = "";
					// volta para o estado inicial
					automato.setEstadoAtivo(0);
				
				// Se não é espaco ou tabulacao
				}else if (atual != '\r' && atual != '\f' && atual != ' ' && atual != '\t'){
						
					// Coloca mais um caracter no token
					token = token + (char) atual;
					//System.out.println("CHAR = (" + (char)atual + ")");
					
					// Verifica se existe transicao recebendo o caractere atual
					if (automato.temTransicao((char) atual)) {
						
						// Faz o automato andar para o proximo estado
						automato.percorre((char) atual);
						
						// Se o estado atual é final
						if(automato.estadoAtivoFinal()){
							
							// Verifica se não tem transicao com o proximo caracteres
							if (!automato.temTransicao((char) proximo)) {
								
								//gera token e volta para o estado inicial
								tokensTokens.adicionaToken(token, automato.getTipo(), linha, coluna);
								token = "";
								automato.setEstadoAtivo(0);
							}
						}
					
					// Se não existe transicao com o caractere atual
					}else {
						// ERRO
						System.out.println("[ERRO] Caractere invalido <" + (char)atual + "> na linha: " + linha);
						return false;
					}
				}
				
				// Pega proximo caracter
				atual = proximo;
				//System.out.println("CHAR = (" + (char)atual + ")");
				
				// Atualiza coluna
				coluna++;
			}
			return true;
		
		}catch(Exception e){
		//	e.printStackTrace();
			return false;
		}	
	}
	
}
