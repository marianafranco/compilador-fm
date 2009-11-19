package compilador.estruturas;

import java.util.Vector;

public class APE {
	
	private Vector<AFD> submaquinas;
	
	public APE(){
		this.submaquinas = new Vector<AFD>();
	}
	
	public void adicionaSubmaquina (AFD nova) {
		this.submaquinas.add(nova);
	}
	
	public AFD getSubmaquina(String nome){
		for (int i = this.submaquinas.size() - 1; i >= 0; i--){
			if(this.submaquinas.get(i).getNome().equals(nome)){
				return this.submaquinas.get(i);
			}
		}
		return null;
	}
	
	// Imprime as subm�quinas, seus estados e transi��es
	public void imprime(){
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
	
	// Minimiza o aut�mato retirando as transi��es em vazio
	void minimiza(){
		for(int i=0 ; i < this.submaquinas.size(); i++){
			AFD submaquina = this.submaquinas.get(i);
			
			for(int j=0; j < submaquina.getTamanho(); j++){
				Estado estado = submaquina.getEstadoIndice(j);
				
				for(int k=0; k < estado.getTamanho(); k++){
					
					// Se transi��o vazia
					if(estado.getTransicao(k).getEntrada().toString().equals("e")){
						
						estado.removeTransicao(k);
						Estado proximoEstado = submaquina.getEstadoIndice(estado.getTransicao(k).getProximo());
						
						while(proximoEstado.proximoEstado("e")!= -1){
							
							
							proximoEstado = submaquina.getEstado(proximoEstado.proximoEstado("e"));
						}
					}
				}
			}
		}
	}
	
}
