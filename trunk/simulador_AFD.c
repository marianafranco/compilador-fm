#include "simulador_AFD.h"

void simula_AFD (AFD *automato, FILE *cod_fonte, fluxo_tokens *tokens, tabela_simbolos *tabela) {
	
	// Numero do estado atual
	int estado_atual = 0;
	// Buffer do ultimo caracter lido
	char buffer;
	
	// Coloca caracter no buffer
	buffer = fgetc(cod_fonte);
	
	// Enquanto nao chegarmos ao fim do arquivo
	while (buffer != EOF) {
		// Caso tivermos um espaço ou uma quebra de linha
		while (0) {
			// Próximo caracter
			buffer = fgetc(cod_fonte);
		}
		
		// Muda de estado
		
		// Caso o token for completado com sucesso
		if (0) {
			// Grava token
		}
		
		// Próximo caracter
		buffer = fgetc(cod_fonte);
	}
}
