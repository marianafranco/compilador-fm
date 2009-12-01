package compilador.estruturas;

import java.util.Vector;

/**
 * APE: Estrutura utilizada para representar um autÙmato de pilha estruturado. 
 * 
 * @author Felipe Yoshida, Mariana R. Franco
 *
 */
public class APE {
	
	/**
	 * vetor de submaquinas.
	 */
	private Vector<AFD> submaquinas;
	
	
	/**
	 * MÈtodo construtor.
	 */
	public APE(){
		this.submaquinas = new Vector<AFD>();
	}
	
	/**
	 * Adiciona uma submaquina ao autÙmato de pilha estruturado.
	 * @param nova	subm·quina (AFD) que sera adicionada. 
	 */
	public void adicionaSubmaquina (AFD nova) {
		this.submaquinas.add(nova);
	}
	
	/**
	 * Recupera a subm·quina atravÈs do seu nome.
	 * @param nome	nome da subm·quina que se deseja recuperar.
	 * @return	subm·quina (AFD) desejada, ou nulo
	 */
	public AFD getSubmaquina(String nome){
		for (int i = this.submaquinas.size() - 1; i >= 0; i--){
			if(this.submaquinas.get(i).getNome().equals(nome)){
				return this.submaquinas.get(i);
			}
		}
		return null;
	}
	
	
	/**
	 * Imprime as subm·quinas, seus estados e transiÁıes
	 */
	public void imprime(){
		System.out.println("## Automato ##");
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			System.out.println((i+1) + "." + submaquina.getNome());
			
			System.out.print("\t final: ");
			for(int j=0; j < submaquina.getTamanho(); j++){
				if(submaquina.getEstadoIndice(j).getAceitacao()){
					System.out.print( submaquina.getEstadoIndice(j).getId() + ", ");
				}
			}
			System.out.println();
			
			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				
				for(int k=0; k < estado.getTamanho(); k++){
					System.out.print("\t (" + estado.getId() + ", " + estado.getTransicao(k).getEntrada() +")");
					System.out.print(" -> " + estado.getTransicao(k).getProximo());
					System.out.println();
				}
			}
		}
	}
	
	/**
	 * Imprime as subm·quinas, seus estados e transiÁıes para o graphviz
	 */
	public void imprime_desenho(){
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			System.out.println("digraph " + submaquina.getNome() + " {");
			System.out.println("");
			System.out.println("\tnull [shape = plaintext label=\"\"];");
			System.out.print("\tnode [shape = doublecircle]");
			for(int j=0; j < submaquina.getTamanho(); j++){
				if(submaquina.getEstadoIndice(j).getAceitacao()){
					System.out.print(" " + submaquina.getEstadoIndice(j).getId());
				}
			}
			System.out.println(";");
			System.out.println("\tnode [shape = circle];");
			System.out.println("");
			System.out.println("\tnull -> 0;");

			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				
				for(int k=0; k < estado.getTamanho(); k++){
					System.out.print("\t" + estado.getId() + " -> " + estado.getTransicao(k).getProximo());
					System.out.print(" [ label = " + estado.getTransicao(k).getEntrada() + " ];");
					System.out.println();
				}
			}
			System.out.println("}");
		}
	}
	
	/**
	 * Minimiza o autÙmato retirando as transiÁıes em vazio
	 */
	public void minimiza(){
		
		// Minimiza
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			
			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				
				// Enquanto tiver transiÁ„o vazia
				while(estado.proximoEstado("e") != -1){
					Estado proximoEstado = submaquina.getEstado(estado.proximoEstado("e"));
					estado.removeTransicao("e");
					
					if(proximoEstado.getAceitacao()){
						estado.setAceitacao(true);
					}
					
					for(int t=0; t < proximoEstado.getTamanho(); t++){
						estado.adicionaTransicao(proximoEstado.getTransicao(t));
					}
				}
			}
		}
		
		// EliminaÁ„o do n„o-determinismo
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			
			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				
				if(estado.naoDeterminismo()){
					//System.out.println("Estado (" + estado.getId() + ", " + submaquina.getNome() +") nao-deterministico!!");
					
					Vector<Transicao> transicoes = estado.getNaoDeterminismo();
					
					Transicao a = transicoes.elementAt(0);
					Transicao b = transicoes.elementAt(1);
					
					//System.out.println("Entrada: " + a.getEntrada() + " / " + b.getEntrada());
					
					Estado novoEstado = new Estado(submaquina.getNextEstadoID(), false);
					
					//System.out.println("Proximo A: " + submaquina.getEstado(a.getProximo()).getId());
					//System.out.println("Proximo B: " + submaquina.getEstado(b.getProximo()).getId());
					
					novoEstado.adicionaTransicao(submaquina.getEstado(a.getProximo()).getTransicoes());
					novoEstado.adicionaTransicao(submaquina.getEstado(b.getProximo()).getTransicoes());
					
					estado.removeTransicao(a);
					estado.removeTransicao(b);
					estado.adicionaTransicao(new Transicao(novoEstado.getId(), b.getEntrada()));
					
					if(submaquina.getEstado(a.getProximo()).getAceitacao() || submaquina.getEstado(a.getProximo()).getAceitacao()){
						novoEstado.setAceitacao(true);
					}
					
					submaquina.adicionaEstado(novoEstado);
					j--;
					
					//System.out.println("Criou o estado: " + novoEstado.getId());
				}
			}
		}
		
		// Elimina estados não alcançáveis
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			boolean[] alcancaveis = new boolean[submaquina.getTamanho()];
			// Inicializando o vetor
			for (int j = 0; j < submaquina.getTamanho(); j++) {
				alcancaveis[j] = false;
			}
			
			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				Vector<Transicao> transicoes = estado.getTransicoes();
				
				for (int k = 0; k < transicoes.size(); k++) {
					alcancaveis[transicoes.elementAt(k).getProximo()] = true;
				}
			}
			alcancaveis[0] = true;
			int tamanhoOriginal = submaquina.getTamanho();
			for(int j=0; j < tamanhoOriginal; j++){
				if (alcancaveis[j] == false) {
					submaquina.removeEstado(j);
				}
			}
		}
		
	}
	
}
