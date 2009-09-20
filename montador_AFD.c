#include "montador_AFD.h"

// Adiciona novo estado
void adiciona_estado (int id, int final) {
	printf("Adicionando estado %d, %d final\n", id, final); 
}

// Adiciona nova transição
void adiciona_transicao (char entradas[], int proximo) {
	printf("%s  ->  %d\n", entradas, proximo); 
}

// Entra diversos tipos de token e monta um AFD
void monta_AFD (AFD *automato) {
	
	// Abrindo o arquivo que contém a descrição do autômato
	ezxml_t arquivo = ezxml_parse_file("../../automato.xml"), state, transition;
	
	// Para todos os estados
	for (state = ezxml_child(arquivo, "estado"); state; state = state->next) {
		
		// Adiciona estado
		adiciona_estado(atoi(ezxml_child(state, "id")->txt), atoi(ezxml_child(state, "final")->txt));
		
		// Para cada transicao do estado
		for (transition = ezxml_child(state, "transicao"); transition; transition = transition->next) {
			
			// Adiciona transicao
			adiciona_transicao(ezxml_child(transition, "entradas")->txt, atoi(ezxml_child(transition, "proximo")->txt));
		}
	}
	ezxml_free(arquivo); 

}
