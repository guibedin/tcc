package guilherme.tcc.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author guilherme
 * @see RedeNeural
 * @see Camada
 * 
 * Classe que representa os neuronios da Rede Neural Artificial.
 * 
 * Os neuronios pertencem a uma camada dessa rede;
 * Cada neuronio possui um vetor de entradas, um vetor de saidas, um vetor de pesos e um valor final.
 * 
 * Vetor de entradas: Valores de entrada do neuronio, saidas camada anterior;
 * Vetor de saidas: Valores de saida, calculados como (Valor do neuronio) * (Peso da saida);
 * Vetor de pesos: Valores que sao ajustados de acordo com o aprendizado da rede, e utilizados para calcular as saidas;
 * Valor final: Valor calculado aplicando a funcao de ativacao na soma das entradas.
 */
public class Neuronio {

	private double[] entrada = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};; // Valores das entradas do neuronio
	private double[] saida; // Valor de saida = valor * peso
	
	private double valor; // Valor final do neuronio = sigmoide da soma das entradas
	
	public Neuronio(){
		
	}
	
	public Neuronio(double[] entradas){
		this.entrada = entradas;
	}
	
	
	// Calcula o valor do neuronio
	// Esse valor eh multiplicado pelo vetor de pesos para calcular o vetor de saidas do neuronio 
	public void calcularValor(int tamanho, boolean isCamadaSaida){
		this.valor = 0;
		
		for(int i = 0; i < tamanho; i++){
			this.valor += this.entrada[i];
		}
		
		if(isCamadaSaida){
			this.valor = sigmoide(this.valor);
		}
	}
	
	// Calcula o vetor de saida do neuronio
	// Possui 1 saida para cada neuronio da proxima camada
	public double[] calcularSaida(double[] pesos){
		this.saida = new double[pesos.length];
		
		for(int i = 0; i < pesos.length; i++){
			this.saida[i] = this.valor * pesos[i];
			//System.out.print("\nValor: " + this.valor + " Peso: " + pesos[i] + " Saida: "+ this.saida[i]);
		}
		return this.saida;
	}
	
	public double sigmoide(double valor){
		return 1 / (1 + Math.exp(-valor));
	}

	public double[] getEntrada() {
		return entrada;
	}

	public void setEntrada(double[] entrada) {
		this.entrada = entrada;
	}
	
	public void setEntrada(double entrada, int i){
		this.entrada[0] = entrada;
	}

	public double[] getSaida() {
		return saida;
	}

	public void setSaida(double[] saida) {
		this.saida = saida;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}	
}
