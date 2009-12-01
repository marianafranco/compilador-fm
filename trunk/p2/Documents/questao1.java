pilha Operando = new Pilha();
pilha Operador = new Pilha();

int main () {
	while (token != EOF) {
		if (token == "(") {
			Operando.empilha(token);
			token.proximoToken;
		}
		else if (token == numero) {
			Operando.empilha(token);
			token.proximoToken;
		}
		else if (token == operador) {
			if (token == "/" || token == "*") {
				Operador.empilha(token);
				token.proximoToken;
				if (token == "(") {
					Operando.empilha(token);
					token.proximoToken;
				}
				else if (token == numero) {
					Operador.empilha(token);
					resolve();
				}
				else {
					erro();
				}
			}
			if (token == "+" || token == "-") {
				Operador.empilha(token);
				token.proximoToken;
				Operando.empilha(token);
				resolve();
			}
		}
		else if (token == ")"){
			while (Operando.topo() != "(") {
				resolve();
			}
			Operando.desempilha();
		}
	}
}

public void resolve() {
	
}


