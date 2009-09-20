#include "simulador_AFD.h"

void get_token(){
     
}


void simula_AFD (AFD *automato, FILE *cod_fonte, fluxo_tokens *tokens, tabela_simbolos *tabela) {
	
	// Numero do estado atual
	int estado_atual = 0;
	
	// Caractere atual e proximo
	char atual;
	char proximo;
	
	// Array que vai montando o token
	char token [100];
  int token_id = 0; 
  
  int coluna = 0;
	int linha = 0;
	
	
	// Pega o primeiro caractere do arquivo fonte
	atual = fgetc(cod_fonte);
	
	printf("\n[INFO] Inicio da analise lexica.\n");
	// Enquanto nao chegarmos ao fim do arquivo
	while (atual != EOF) {
        
    printf("%c", atual);
    
		// Caso tivermos um espaco ou uma quebra de linha
		proximo = fgetc(cod_fonte);
		
		if(atual == '\n'){
        coluna = 0;
        linha++;
    
    }else if(atual == ' ' || atual == '\t'){
        coluna++;
    
    }else {    
        token[token_id] = atual;
		    
		    
		
    }
    
    atual = proximo;
	}
	printf("\n[INFO] Fim da analise lexica.\n");
}
