package compilador.estruturas;

import java.util.Vector;

/**
 * APE: Estrutura utilizada para representar um autômato de pilha estruturado. 
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
	 * Método construtor.
	 */
	public APE(){
		this.submaquinas = new Vector<AFD>();
	}
	
	/**
	 * Adiciona uma submaquina ao autômato de pilha estruturado.
	 * @param nova	submáquina (AFD) que sera adicionada. 
	 */
	public void adicionaSubmaquina (AFD nova) {
		this.submaquinas.add(nova);
	}
	
	/**
	 * Recupera a submáquina através do seu nome.
	 * @param nome	nome da submáquina que se deseja recuperar.
	 * @return	submáquina (AFD) desejada, ou nulo
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
	 * Imprime as submáquinas, seus estados e transições
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
	 * Minimiza o autômato retirando as transições em vazio
	 */
	public void minimiza(){
		
		// Minimiza
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			
			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				
				// Enquanto tiver transição vazia
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
		
		// Eliminação do não-determinismo
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
	}
	
}
