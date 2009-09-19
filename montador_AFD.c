#include "montador_AFD.h"

// Adiciona novo estado
void adiciona_estado () {
}

// Adiciona nova transição
void adiciona_transicao () {
}

// Entra diversos tipos de token e monta um AFD
void monta_AFD (FILE *sintaxe, AFD *automato) {
	
	// Mantém o último caracter recebido
	char buffer;
	
	// Pega o primeiro caracter
	buffer = fgetc(sintaxe);
	
	// Armazena qual a coluna atualmente processada
	int coluna;
	
	// Percorrendo os caracteres enquanto não acabar o arquivo
	while (buffer != EOF) {
		
		// Quando há uma nova linha, reinicia coluna
		if (buffer == '\n') {
			coluna = 0;
		}
		// Quando há um espaço, muda de coluna
		else if (buffer == ' ') {
			coluna++;
		}
		// Quando se recebe um caracter válido
		else {
			// A primeira coluna cria um novo estado
			if (coluna == 0) {
			}
			// Seta a coluna como sendo final ou não final
			else if (coluna == 1) {
				// Final
				if (buffer == 1) {
				}
				// Nao final
				else {
				}
				
			}
			// No caso de transicoes
			else {
				// Inclui transicao
				if (coluna % 2 == 0) {
				}
				// Proximo estado para todas as transicoes nao setadas do estado atual
				else {
				}
				
			}
		}
		buffer = fgetc(sintaxe);
	}
}
