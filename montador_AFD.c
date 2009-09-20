#include "montador_AFD.h"

// Funcao de teste da montagem do automato
void imprime_automato (AFD *automato) {
	
	estado *ultimo;
	ultimo = automato->primeiro;
	
	// Percorre todos os estados
	for (int i = 0; automato->numero_estados < i; i++) {
		ultimo = ultimo->proximo;
	}
}

// Retorna o último estado da lista de um automato
void ultimo_estado (AFD *automato, estado *ultimo) {
	
	ultimo = automato->primeiro;
	
	if (automato->numero_estados != 0) {
		// Percorre todos os estados
		for (int i = 0; automato->numero_estados < i; i++) {
			ultimo = ultimo->proximo;
		}
	}
}

// Adiciona novo estado
void adiciona_estado (int id, int final, AFD *automato) {
	
	estado *temp;
	ultimo_estado (automato, temp);
	temp = (estado *) malloc(sizeof(estado));
	
	temp->id = id;
	temp->final = final;
	
}

// Adiciona nova transição
void adiciona_transicao (char entradas[], int proximo, AFD *automato) {
	//printf("%s  ->  %d\n", entradas, proximo); 
}

// Entra diversos tipos de token e monta um AFD
void monta_AFD (AFD *automato) {
	
	// Abrindo o arquivo que contém a descrição do autômato
	ezxml_t arquivo = ezxml_parse_file("../../automato.xml"), state, transition;
	
	// Para todos os estados
	for (state = ezxml_child(arquivo, "estado"); state; state = state->next) {
		
		// Adiciona estado
		adiciona_estado(atoi(ezxml_child(state, "id")->txt), atoi(ezxml_child(state, "final")->txt), automato);
		
		// Para cada transicao do estado
		for (transition = ezxml_child(state, "transicao"); transition; transition = transition->next) {
			
			// Adiciona transicao
			adiciona_transicao(ezxml_child(transition, "entradas")->txt, atoi(ezxml_child(transition, "proximo")->txt), automato);
		}
	}
	ezxml_free(arquivo);

}
