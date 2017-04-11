package guilherme.tcc.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author guilherme
 *
 * Classe que representa uma camada da Rede Neural Artificial.
 * 
 * @see RedeNeural
 * @see Neuronio
 * 
 * Uma camada possui N neuronios.
 */
public class Camada {
	
	private List<Neuronio> neuronios;
	//private Sinapse[][] sinapses;
	private Camada anterior;
	private Camada proxima;
	
	public Camada(int tamanho){
		this.neuronios = new ArrayList<Neuronio>();
		adicionarNeuronios(tamanho);
	}
	/*
	public void adicionarSinapse(){
		if(this.proxima != null){
			for(int i = 0; i < this.getNumeroNeuronios(); i++){
				for(int j = 0; j < this.proxima.getNumeroNeuronios(); j++){
					sinapses[i][j] = new Sinapse(this.proxima.get);
				}
			}
		}
	}
	*/
	public void adicionarNeuronios(int tamanho){
		for(int i = 0; i < tamanho; i++){
			this.neuronios.add(new Neuronio());
		}
	}
	
	public int getNumeroNeuronios(){
		return neuronios.size();
	}

	public List<Neuronio> getNeuronios() {
		return neuronios;
	}

	public void setNeuronios(List<Neuronio> neuronios) {
		this.neuronios = neuronios;
	}

	public Camada getAnterior() {
		return anterior;
	}

	public void setAnterior(Camada anterior) {
		this.anterior = anterior;
	}

	public Camada getProxima() {
		return proxima;
	}

	public void setProxima(Camada proxima) {
		this.proxima = proxima;
	}
	
	
}
