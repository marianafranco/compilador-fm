#include <stdio.h>
#include <stdlib.h>
#include "estruturas.h"
#include "simulador_AFD.h"
#include "montador_AFD.h"

// Recebe código fonte, arquivo de sintaxe e retorna fluxo de tokens e tabela de símbolos
void lexico(FILE *cod_fonte, FILE *sintaxe, fluxo_tokens *tokens, tabela_simbolos *tabela) {
	
	// Automato que processará o código fonte
	AFD *automato;
	
	// Monta Automato Finito Determinístico
	monta_AFD(sintaxe, automato);
	// Simula Automato Finito Determinístico com a entrada
	simula_AFD(automato, cod_fonte, tokens, tabela);
}

// Chama sequencialmente as etapas do compilador
int main (int argc, const char *argv[]) {

	// Caso o número de argumentos passados seja diferente de 3
	if (0) { // Mudar depois para (argc != 3)
		printf("Forma de uso: compilador <fonte> <sintaxe> <objeto>\n");
	}
	else {
		// Código fonte
		FILE *cod_fonte;
		cod_fonte = fopen("../../fonte.fm", "r"); // Mudar depois para o 1o argumento
		// Arquivo de sintaxe
		FILE *sintaxe;
		sintaxe = fopen("../../gramatica.txt", "r"); // Mudar depois para o 2o argumento
		
		// Caso o arquivo fonte exista
		if (cod_fonte != NULL) {
			// Caso o arquivo de sintaxe exista
			if (sintaxe != NULL) {
				
				// Fluxo de tokens, produto do analisador lexico
				fluxo_tokens *tokens;
				// Tabela de símbolos, produto do analisador lexico
				tabela_simbolos *tabela;
				
				// Chama o analisador léxico
				lexico(cod_fonte, sintaxe, tokens, tabela);
				fclose(sintaxe);
			}
			else {
				printf("O arquivo de sintaxe não pode ser encontrado!\n");
			}
			fclose(cod_fonte);
		}
		else {
			printf("O arquivo de entrada não pode ser encontrado!\n");
		}
		// Imprime fluxo de tokens e tabela de símbolos para teste da primeira etapa
	}
	
    return 0;
}
