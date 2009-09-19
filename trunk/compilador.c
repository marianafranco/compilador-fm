#include <stdio.h>
#include <stdlib.h>
#include "estruturas.h"
#include "simulador_AFD.h"
#include "montador_AFD.h"

// Recebe codigo fonte, arquivo de sintaxe e retorna fluxo de tokens e tabela de simbolos
void lexico(FILE *cod_fonte, fluxo_tokens *tokens, tabela_simbolos *tabela) {
	
	// Automato que processara o codigo fonte
	AFD *automato;
	
	// Arquivo que descreve o automato
  FILE *automato_configs;
  automato_configs = fopen("gramatica.xml", "r");
  
  // Verifica se o arquivo de configuracoes do automato existe
  if(!automato_configs) {
      printf("[ERRO] O arquivo de configuracao do automato nao pode ser encontrado!\n");
        
  }else{
      printf("[INFO] O arquivo de sintaxe foi encontrado!\n");
      
      // Monta Automato Finito Deterministico
    	monta_AFD(automato_configs, automato);
    	
    	// Simula Automato Finito Deterministico com a entrada
    	simula_AFD(automato, cod_fonte, tokens, tabela);        
    	
    	fclose(automato_configs);
  }
}

// Chama sequencialmente as etapas do compilador
int main (int argc, const char *argv[]) {

	// Caso o numero de argumentos passados seja diferente de 3
 if (argc != 2) { // Mudar depois para (argc != 3)
		printf("Forma de uso: compilador <fonte> <objeto>\n");
	
  }else {
		// Codigo fonte
		FILE *cod_fonte;
		cod_fonte = fopen(argv[1], "r");
		printf("[INFO] argv[1] = %s\n", argv[1]);
		
		// Caso o arquivo nao fonte exista
		if (!cod_fonte) {
           printf("[ERRO] O arquivo de entrada nao pode ser encontrado!\n");
        
    }else{
           printf("[INFO] O arquivo entrada <%s> foi encontrado!\n", argv[1]);
           
           // Fluxo de tokens, produto do analisador lexico
				   fluxo_tokens *tokens;
				   // Tabela de simbolos, produto do analisador lexico
				   tabela_simbolos *tabela;
				
				   // Chama o analisador lexico
				   lexico(cod_fonte, tokens, tabela);
				   
		       fclose(cod_fonte);		
		}
  }
  //system("pause");
  return 0;
}
