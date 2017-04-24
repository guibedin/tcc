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

	private double[] entrada = {0, 0, 0, 0};; // Valores das entradas do neuronio
	private double[] saida; // Valor de saida = valor * peso
	
	private double valor; // Valor final do neuronio = sigmoide da soma das entradas
	//private List<Sinapse> sinapse; // Tamanha da camada anterior
	
	public Neuronio(){
		//sinapse = new ArrayList<Sinapse>();
		//this.entrada = new double[4];		
	}
	
	public Neuronio(double[] entradas){
		//sinapse = new ArrayList<Sinapse>();
		this.entrada = entradas;
	}
	
	
	public void calcularValor(int tamanho){
		this.valor = 0;
		
		for(int i = 0; i < tamanho; i++){
			this.valor += this.entrada[i];
		}
		
		this.valor = sigmoide(this.valor);
	}
	
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
		/*System.out.println("\nEntrada: ");
		for(int i = 0; i < this.entrada.length; i++){
			System.out.println(this.entrada[i]);
		}*/
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

	/*public List<Sinapse> getSinapse() {
		return sinapse;
	}

	public void setSinapse(List<Sinapse> sinapse) {
		this.sinapse = sinapse;
	}*/
	
	
	
}
