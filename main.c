#include <stdio.h>
#include <stdlib.h>

typedef struct fluxo_tokens {
	char nome[10];
	int valor_atributo;
	struct fluxo_tokens *proximo;
} fluxo_tokens;

typedef struct tabela_simbolos {
	int valor_atributo;
	char nome[10];
	char tipo[10];
	int linha;
	int coluna;
	struct tabela_simbolos *proxima;
} tabela_simbolos;

typedef struct transicao {
	int proximo;
	int entrada;
	struct transicao *proxima;
} transicao;

typedef struct estado {
	int id;
	int final;
	int transicoes;
	struct estado *proximo;
	struct transicao *primeiro;
} estado;

typedef struct AFD {
	int numero_estados;
	struct estado *primeiro;
} AFD;

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

void simula_AFD (AFD *automato, FILE *cod_fonte, fluxo_tokens *tokens, tabela_simbolos *tabela) {
	
	// Número do estado atual
	int estado_atual = 0;
	// Buffer do ultimo caracter lido
	char buffer;
	
	// Coloca caracter no buffer
	buffer = fgetc(cod_fonte);
	
	// Enquanto não chegarmos ao fim do arquivo
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
