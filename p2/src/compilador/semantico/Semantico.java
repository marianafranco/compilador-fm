package compilador.semantico;

import java.io.FileWriter;
import java.io.PrintWriter;

import compilador.estruturas.Token;
import compilador.exceptions.SemanticoException;

public class Semantico {
	
	private String arquivo;
	private String output;
	private String main;
	
	public Semantico(String arquivoC) {
		this.arquivo = arquivoC;
		this.output = "";
		this.main = "";
	}
	
	public void geraCodigo(Token token, String aPercorrer) throws SemanticoException{
		
		//System.out.println("[DEBUG] Token: " + token.getValor() + " submaquina: " + submaquina.getNome());
		//System.out.println("[DEBUG] nextToken: " + nextToken.getValor());
		
		// Chamada de submáquina
		if(aPercorrer.equals("Expr")){
			// Não faz nada
			
		}else{
			if(token.getValor().equals("S")){
				main = main + "\tentra('S');\n";
				main = main + "\ttenta_reduzir();\n";
				
			}else if(token.getValor().equals("K")){
				main = main + "\tentra('K');\n";
				main = main + "\ttenta_reduzir();\n";
				
			}else if(token.getValor().equals("I")){
				main = main + "\tentra('I');\n";
				main = main + "\ttenta_reduzir();\n";
				
			}else if(token.getValor().equals("(")){
				main = main +  "\tnovo_escopo();\n";
				
				
			}else if(token.getValor().equals(")")){
				main = main + "\tfecha_escopo();\n";
				main = main + "\ttenta_reduzir();\n";
				
			}else{
				throw new SemanticoException("Entrada '" + token.getValor() + "' não valida.", token.getLinha());
			}
			
		}
		
	}
	
	public void criaArquivo(){
		try{
			FileWriter writer = new FileWriter(this.arquivo);
			PrintWriter saida = new PrintWriter(writer);
			
			// includes
			this.output = this.output + "#include \"environment.h\"\n";
			this.output = this.output + "#include <stdio.h>\n\n";
			
			// main
			this.output = this.output + "int main(void) {\n";
			this.output = this.output + this.main;
			
			// fim
			this.output = this.output + "\n\timprime();\n";
			this.output = this.output + "\treturn 0;\n";
			this.output = this.output + "}\n";
			
			// imprime na tela
			System.out.println("ARQUIVO DE SAIDA\n"+ this.output);
			saida.print(this.output);
			
			// fecha o arquivo
			saida.close();
			writer.close(); 
		}catch(Exception e){
			System.out.println("[ERRO] Foi impossivel criar o arquivo de saida.");
			e.printStackTrace();
		}
	}
}