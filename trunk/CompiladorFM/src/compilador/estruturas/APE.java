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
	
	// Imprime as submáquinas, seus estados e transições
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
	
	// Minimiza o autômato retirando as transições em vazio
	public void minimiza(){
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
	}
	
}
