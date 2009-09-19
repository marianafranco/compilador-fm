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