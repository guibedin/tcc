package guilherme.tcc.classes;

public class Sinapse {

	private Neuronio proximo;
	private Neuronio anterior;
	private double peso; // Peso da sinapse
	
	public Sinapse(Neuronio prox){
		this.proximo = prox;
	}
	
	public Neuronio getProximo() {
		return proximo;
	}
	public void setProximo(Neuronio proximo) {
		this.proximo = proximo;
	}
	public Neuronio getAnterior() {
		return anterior;
	}
	public void setAnterior(Neuronio anterior) {
		this.anterior = anterior;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
}
