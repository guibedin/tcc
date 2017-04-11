package guilherme.tcc.classes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author guilherme
 * @see Camada
 * @see Neuronio
 * 
 * Classe que representa a Rede Neural Artificial (RNA).
 * 
 * A RNA utilizada neste projeto possui tres camadas: entrada,
 * intermediaria e saida.
 * 	
 */
public class RedeNeural {
	
	// Numero de entradas (Testes / Linhas do arquivo)
	// Cada linha do arquivo representa uma entrada
	private static final int numeroEntradas = 3;
	
	// Tamanho das camadas
	private static final int tamanhoEntrada = 2;
	private static final int tamanhoIntermediaria = 3;
	private static final int tamanhoSaida = 1;
	
	private Camada entrada; // Camada de entrada
	private Camada intermediaria; // Camada intermediaria
	private Camada saida; /// Camada de saida
	private double[][] pesosEntradaIntermediaria; // Matriz de sinapses (pesos)
	private double[][] pesosIntermediariaSaida; // Matriz de sinapses (pesos)
	
	private double[][] dadosEntrada; // Dados de entrada da rede
	private double[][] dadosSaida; // Dados de saida da rede
	
	private double[][] saidaTreino; // Dados usados para treinar rede (Comparar dadosSaida com essa matriz)
	
	private double[][] dadosEntradaIntermediaria;
	private double[][] dadosEntradaIntermediariaTransformada;
	private double[][] dadosIntermediariaSaida;
	private double[][] dadosIntermediariaSaidaTransformada;
	
	public RedeNeural(){
		// Instanciacao das camadas, passando o tamanho delas (numero de neuronios)
		entrada = new Camada(tamanhoEntrada);
		intermediaria = new Camada(tamanhoIntermediaria);
		saida = new Camada(tamanhoSaida);
		
		// Definicao das camadas anteriors e proximas
		// Isso e necessario para a definicao das sinapses
		entrada.setAnterior(null);
		entrada.setProxima(intermediaria);
		
		intermediaria.setAnterior(entrada);
		intermediaria.setProxima(saida);
		
		saida.setAnterior(intermediaria);
		saida.setProxima(null);
		
		// Matriz de dados de entrada
		this.dadosEntrada = new double[numeroEntradas][tamanhoEntrada];
		this.dadosSaida = new double[numeroEntradas][tamanhoSaida];
		
		// Matrizes de saida de uma camada -> entrada da proxima
		this.dadosEntradaIntermediaria = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.dadosIntermediariaSaida = new double[tamanhoIntermediaria][tamanhoSaida];
		
		
		// Matrizes de peso das sinapses (entrada -> intermediaria / intermediaria -> saida)
		this.pesosEntradaIntermediaria = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.pesosIntermediariaSaida = new double[tamanhoIntermediaria][tamanhoSaida];
		
		this.lerArquivos("entradas.txt");
		this.lerArquivos("pesos1.txt");
		this.lerArquivos("pesos2.txt");
		
		this.printarMatrizes();
		
		this.executar();
		
		System.out.println("\nMatriz de Saidas:");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(this.dadosSaida[i][j] + ", ");	
			}			
		}
		System.out.println();
	}
	
	// Gera populacao inicial - valores aleatorios para os pesos
	// A populacao eh formada por um conjunto de matrizes de peso
	// Cada "membro" da populacao eh uma matriz inteira, e nao somente um peso
	public void gerarPopulacaoInicial(){
		int i;
		int j;
		
		for(i = 0; i < tamanhoEntrada; i++){
			for(j = 0; j < tamanhoIntermediaria; j++){
				this.pesosEntradaIntermediaria[i][j] = 0;
			}
		}
		
		for(i = 0; i < tamanhoIntermediaria; i++){
			for(j = 0; j < tamanhoSaida; j++){
				this.pesosIntermediariaSaida[i][j] = 0;
			}
		}
	}
	
	/*
	 * Calcula o fitness da populacao atual
	 * Duvida sobre fitness - como calcular?
	 * Possivel solucao: 
	 * 1 - Calcular Fitness - calcular distancia do valor gerado pela rede em relacao ao valor de teste, quanto menor a distancia, maior seu 'fitness';
	 * 2 - Reproduzir - seleciona 2 pais (2 matrizes de peso) de uma forma que os com maior 'fitness' tenham mais chances de serem escolhidos;
	 * 3 - Crossover - criar um novo elemento  pegando os pesos de entrada de um pai e os pesos de saida do outro pai;
	 * 4 - Mutacao - definir chance de mutacao (ex: cada peso tem 1% de chance de ser mudado) e aplicar essa mutacao
	 */ 
	public void calcularFitness(){
		
	}
	
	public void reproduzir(){
		
	}
	
	public void crossover(){
		
	}
	
	public void mutacao(){
		
	}
	
	// Executa a rede neural - feedforward
	public void executar(){
		
		int i = 0;
		int j = 0;
		
		// CALCULAR UMA LINHA DE ENTRADA DE CADA VEZ
		for(i = 0; i < numeroEntradas; i++){
			System.out.println("INICIO DA ENTRADA " + (i+1));
			// Calcular sinapses Entrada -> Intermediaria
			// Colocar o valor de entrada nos neuronios de entrada
			// Calcular a saida usando a funcao de ativacao + pesos
			
			// Iterar por cada neuronio da camada de Entrada, setando seu vetor de entrada e calculando sua saida			
			for (Neuronio n : entrada.getNeuronios()){
				n.setEntrada(this.dadosEntrada[i][j], i);
				n.calcularValor(this.dadosEntrada[i].length - 1);
				this.dadosEntradaIntermediaria[j] = n.calcularSaida(this.pesosEntradaIntermediaria[j]);
				
				System.out.println("\nSaidas N" + (j+1));
				for(int k = 0; k < tamanhoIntermediaria; k++){
					System.out.print(this.dadosEntradaIntermediaria[j][k] + " ");
				}
				System.out.println();
				j++;
			}			
			j = 0;
			
			this.dadosEntradaIntermediariaTransformada = transformaMatriz(this.dadosEntradaIntermediaria);
			// Calcular sinapses Intermediaria -> Saida
			// O valor de entrada da camada Intermediaria eh composto pelas saidas da camada de Entrada
			// Calcular a saida usando a funcao de ativacao + pesos
			
			// Iterar por cada neuronio da camada Intermediaria, setando seu vetor de entrada e calculando suas saidas
			for(Neuronio n : intermediaria.getNeuronios()){
				n.setEntrada(this.dadosEntradaIntermediariaTransformada[j]);
				n.calcularValor(this.dadosEntradaIntermediariaTransformada[j].length);
				System.out.print("\nValor N" + (j+3) + "\n" + n.getValor());
				this.dadosIntermediariaSaida[j] = n.calcularSaida(this.pesosIntermediariaSaida[j]);
				
				System.out.println("\nSaidas N" + (j+3));
				for(int k = 0; k < tamanhoSaida; k++){
					System.out.print(this.dadosIntermediariaSaida[j][k] + " ");
				}
				System.out.println();
				j++;
			}
			j = 0;
			
			
			this.dadosIntermediariaSaidaTransformada = transformaMatriz(this.dadosIntermediariaSaida);
			// Calcular a Saida final
			// Colocar o valor de entrada nos neuronios de entrada
			// Calcular a saida usando a funcao de ativacao + pesos
					
			// Iterar por cada neuronio da camada Saida, setando seu vetor de entrada e calculando suas saidas
			for(Neuronio n : saida.getNeuronios()){
				n.setEntrada(this.dadosIntermediariaSaidaTransformada[j]);
				n.calcularValor(this.dadosIntermediariaSaidaTransformada[j].length);
				this.dadosSaida[i][j] = n.getValor();
				
				System.out.println("\nSaidas N" + (j+6));
				for(int k = 0; k < tamanhoSaida; k++){
					System.out.print(this.dadosSaida[i][j] + " ");
				}
				System.out.println();
				j++;
			}
			j = 0;
			System.out.println("\nFIM DA ENTRADA " + (i+1) + "\n");
		}
	}
	
	// Transforma colunas em linhas
	public double[][] transformaMatriz(double[][] m1){
		
		
		double[][] m2 = new double[m1[0].length][m1.length];
		
		//System.out.println("Inversao, x = " + m1[0][0] + " y = " + m1[1][0]);
		for(int i = 0; i < m1[0].length; i++){
			for(int j = 0; j < m1.length; j++){
				m2[i][j] = m1[j][i];
		//		System.out.print(m2[i][j] + " ");
			}
		//	System.out.println();
		}
		return m2;
	}
	
	public void lerArquivos(String arquivo){
		String linha;
		int contador = 0;
		
		try{
			// Abre arquivo passado como parametro
			InputStream entradaIS = new FileInputStream("/home/guilherme/Desktop/" + arquivo);
			InputStreamReader entradaISR = new InputStreamReader(entradaIS);
			BufferedReader buffer = new BufferedReader(entradaISR);
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = buffer.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				
				//If's para determinar o que fazer com cada linha do arquivo, de acordo com o seu nome
				
				if(arquivo.equals("entradas.txt")){
					// Preenche matriz com dados de entrada
					for(int i = 0; i < tamanhoEntrada; i++){
						this.dadosEntrada[contador][i] = Double.parseDouble(numeros[i]);						
					}
					contador++;
				}
				
				if(arquivo.equals("pesos1.txt")){
					// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
					for(int i = 0; i < tamanhoIntermediaria; i++){
						this.pesosEntradaIntermediaria[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}
				
				if(arquivo.equals("pesos2.txt")){
					// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
					for(int i = 0; i < tamanhoSaida; i++){
						this.pesosIntermediariaSaida[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}


	public void printarMatrizes(){
		// Printa Matriz de Entrada
		System.out.println("Matriz de Entrada: ");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoEntrada; j++){				
				System.out.print(this.dadosEntrada[i][j] + ", ");
			}
			System.out.println();
		}
		
		// Printa Matriz de Peso 1
		System.out.println("\nMatriz de Peso 1 (Entrada -> Intermediaria):");
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				System.out.print(this.pesosEntradaIntermediaria[i][j] + ", ");				
			}
			System.out.println();
		}
		
		// Printa Matriz de Peso 2
		System.out.println("\nMatriz de Peso 2 (Intermediaria -> Saida):");
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(this.pesosIntermediariaSaida[i][j] + ", ");
			}
			System.out.println();
		}
		
		// Printa Matriz da Saida
		
	}
	
	public void iniciarSinapses(Sinapse[][] sin, int linhas, int colunas){
		for(int i = 0; i < linhas; i++){
			for(int j = 0; j < colunas; j++){
				//sin[i][j] = new Sinapse();
			}
		}
	}
	
	public Camada getEntrada() {
		return entrada;
	}

	public void setEntrada(Camada entrada) {
		this.entrada = entrada;
	}

	public Camada getIntermediaria() {
		return intermediaria;
	}

	public void setIntermediaria(Camada intermediaria) {
		this.intermediaria = intermediaria;
	}

	public Camada getSaida() {
		return saida;
	}

	public void setSaida(Camada saida) {
		this.saida = saida;
	}

}
