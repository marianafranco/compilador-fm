#include "environment.h"
#include <stdio.h>

int main(void) {
	entra('I');
	tenta_reduzir();
	novo_escopo();
	entra('K');
	tenta_reduzir();
	entra('K');
	tenta_reduzir();
	entra('I');
	tenta_reduzir();
	fecha_escopo();
	tenta_reduzir();
	novo_escopo();
	entra('I');
	tenta_reduzir();
	entra('I');
	tenta_reduzir();
	entra('K');
	tenta_reduzir();
	entra('S');
	tenta_reduzir();
	entra('I');
	tenta_reduzir();
	fecha_escopo();
	tenta_reduzir();

	imprime();
	return 0;
}
