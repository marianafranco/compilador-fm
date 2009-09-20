#include "simulador_AFD.h"


// Funcao para inicializar o automato para exemplo
void inicializa_automato(AFD *automato){
     automato->numero_estados = 0;
}

void init_automato(AFD *automato){
     inicializa_automato(automato);
     
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
	
	// Automato que processara o codigo fonte
	AFD automato_mari;
	
	init_automato(&automato_mari);
	
	
	// Pega o primeiro caractere do arquivo fonte
	atual = fgetc(cod_fonte);
	
	printf("\n[INFO] Inicio da analise lexica.\n");
	// Enquanto nao chegarmos ao fim do arquivo
	while (atual != EOF) {
        
    printf("%c", atual);
    
		proximo = fgetc(cod_fonte);
		
		// Caso tivermos uma quebra de linha
		if(atual == '\n'){
        coluna = 0;
        linha++;
    
    // Caso tivermos uma espaço ou sinal de tabulação
    }else if(atual == ' ' || atual == '\t'){
        coluna++;
    
    }else {    
        token[token_id] = atual;
		    
		    // Faz o automato ir pro proximo estado recebendo atual
		    // estado atual =  proximo estado
		    
		    // Verifica se existe transicao usando o proximo
		    
		    // se nao existe, coloca o token no fluxo de tokens e reinicia o token_id
		    // ver antes se o estado atual eh final
		    
		    // se existe, 
		    
		
    }
    
    atual = proximo;
	}
	printf("\n[INFO] Fim da analise lexica.\n");
}
