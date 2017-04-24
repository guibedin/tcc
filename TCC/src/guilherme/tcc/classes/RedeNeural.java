package guilherme.tcc.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	private static final int tamanhoIntermediaria = 8;
	private static final int tamanhoSaida = 1;
	// Tamanho da populacao
	private static final int tamanhoPopulacao = 100;
	
	// Chance de Crossover
	private static final double chanceCross = 0.2;
	
	//Numero maximo de geracoes
	private static final int geracoes = 20000;
	
	private Camada entrada; // Camada de entrada
	private Camada intermediaria; // Camada intermediaria
	private Camada saida; /// Camada de saida
	
	private List<Elemento> populacao;
	
	private int maiorFitness; // ID do elemento de maior fitness
	private int segundoMaiorFitness; // ID do elemento com segundo maior fitness
	private int menorFitness;
	private int segundoMenorFitness;
	
	
	public RedeNeural(){
		// Instanciacao das camadas, passando o tamanho delas (numero de neuronios)
		this.entrada = new Camada(tamanhoEntrada);
		this.intermediaria = new Camada(tamanhoIntermediaria);
		this.saida = new Camada(tamanhoSaida);
		
		// Definicao das camadas anteriors e proximas
		// Isso e necessario para a definicao das sinapses
		this.entrada.setAnterior(null);
		this.entrada.setProxima(intermediaria);
		
		this.intermediaria.setAnterior(entrada);
		this.intermediaria.setProxima(saida);
		
		this.saida.setAnterior(intermediaria);
		this.saida.setProxima(null);
		
		this.populacao = new ArrayList<Elemento>();
		
		this.maiorFitness = 0;
		this.segundoMaiorFitness = 0;
		this.menorFitness = 0;
		this.segundoMenorFitness = 0;

		
		this.gerarPopulacaoInicial();
		
		this.treinar();
		this.executar(populacao.get(maiorFitness));
		printarMatrizes(populacao.get(maiorFitness));
		
		/*for(Elemento e : this.populacao){
			printarMatrizes(e);		
		}*/
		
		//System.out.println("\nMatriz de Saidas:");
		
		System.out.println();
	}
	
	/*
	 * Calcula o fitness da populacao atual
	 * Possivel solucao: 
	 * 1 - Calcular Fitness - calcular distancia do valor gerado pela rede em relacao ao valor de teste, quanto menor a distancia, maior seu 'fitness';
	 * 2 - Reproduzir - seleciona 2 pais (2 matrizes de peso) de uma forma que os com maior 'fitness' tenham mais chances de serem escolhidos;
	 * 3 - Crossover - criar um novo elemento  pegando os pesos de entrada de um pai e os pesos de saida do outro pai;
	 * 4 - Mutacao - definir chance de mutacao (ex: cada peso tem 1% de chance de ser mudado) e aplicar essa mutacao
	 *
	 * Metodo para treinar a rede neural
	 * Executar a rede para cada elemento
	 * Calcular fitness de cada elemento
	 * Executar Crossover baseado em fitness
	 * Executar Mutacao
	 * Repetir ate erro satisfatorio
	 */
	public void treinar(){
		double fit; // Valor do fitness
		int iteracoes = 0;
		
		while(iteracoes < geracoes){
			this.maiorFitness = 0;
			this.segundoMaiorFitness = 0;
			this.menorFitness = 0;
			this.segundoMenorFitness = 0;

			
			System.out.println("\nRODADA DE TREINO: " + (iteracoes+1));
			// Primeira parte - executar a rede para todo elemento, definindo os 2 com maior fitness
			for(Elemento e : this.populacao){
				//System.out.println("ELEMENTO: " + e.getElementoID());
				executar(e); // Executa rede calculando saidas do elemento
				e.calcularFitness(); // Calucla fitness do elemento
				fit = e.getFitness(); // Pega fitness do elemento
				
				// AJUSTAR ERRO MEDIO
				
				// If's para determinar os 2 elementos com maior fitness - serao usados para crossover
				if(fit < this.populacao.get(this.maiorFitness).getFitness()){
					//System.out.println("MAIOR " + e.getElementoID() + " " + this.maiorFitness);
					this.segundoMaiorFitness = this.maiorFitness;
					this.maiorFitness = e.getElementoID();
				}
				else{
					if(fit < this.populacao.get(this.segundoMaiorFitness).getFitness()){
						//System.out.println("SEGUNDO MAIOR " + e.getElementoID());
						this.segundoMaiorFitness = e.getElementoID();
					}
				}
				// If's para determinar os elementos com menor fitness
				if(fit > this.populacao.get(this.menorFitness).getFitness()){
					//System.out.println("MENOR " + e.getElementoID() + " " + this.menorFitness);
					this.segundoMenorFitness = this.menorFitness;
					this.menorFitness = e.getElementoID();
				}else{
					if(fit > this.populacao.get(this.segundoMenorFitness).getFitness()){
						//System.out.println("SEGUNDO MENOR " + e.getElementoID());
						this.segundoMenorFitness = e.getElementoID();
					}
				}
				
			}
			
			System.out.println("Maior Fitness: " + populacao.get(maiorFitness).getFitness() + " = Elemento: " + populacao.get(maiorFitness).getElementoID());
			System.out.println("Segundo Maior Fitness: " + populacao.get(segundoMaiorFitness).getFitness() + " = Elemento: " + populacao.get(segundoMaiorFitness).getElementoID());
			System.out.println("Menor Fitness: " + populacao.get(menorFitness).getFitness() + " = Elemento: " + populacao.get(menorFitness).getElementoID());
			System.out.println("Segundo Menor Fitness: " + populacao.get(segundoMenorFitness).getFitness()+ " = Elemento: " + populacao.get(segundoMenorFitness).getElementoID());
			
			iteracoes++;
			// Condicao de parada
			if(populacao.get(maiorFitness).getFitness() < 0.00018){
				return;
			}
			
			this.crossover();
			
			// Mutacao
			for(Elemento e : this.populacao){
				if(e.getElementoID() != maiorFitness && e.getElementoID() != segundoMaiorFitness
						&& e.getElementoID() != menorFitness && e.getElementoID() != segundoMenorFitness){
					e.mutacao();
				}
			}
			
		}
		
	}
	
	// Gera populacao inicial - valores aleatorios para os pesos
	// A populacao eh formada por um conjunto de matrizes de peso
	// Cada "membro" da populacao eh um conjunto de matrizes de peso
	public void gerarPopulacaoInicial(){
		for(int i = 0; i < tamanhoPopulacao; i++){
			this.populacao.add(new Elemento(i));
		}
	}
	
	/*
	 * Calcula o fitness da populacao atual
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
		Random r = new Random();
		
		// Crossover - Troca matrizes de peso Entrada->Intermediaria e Intermediaria->Saida entre os 2 elementos de maior fitness
		// e coloca nos 2 elementos de menor fitness
		Elemento e1 = populacao.get(maiorFitness);
		Elemento e2 = populacao.get(segundoMaiorFitness);
		Elemento e3 = populacao.get(menorFitness);
		Elemento e4 = populacao.get(segundoMenorFitness);
		
		// Matrizes de peso auxiliares para fazer o crossover
		// 1 e 2 -> maiorFitness
		// 3 e 4 -> segundoMaiorFitness
		double[][] auxiliar1 = new double[tamanhoEntrada][tamanhoIntermediaria];
		double[][] auxiliar2 = new double[tamanhoIntermediaria][tamanhoSaida];		
		double[][] auxiliar3 = new double[tamanhoEntrada][tamanhoIntermediaria];
		double[][] auxiliar4 = new double[tamanhoIntermediaria][tamanhoSaida];
		
		// Copia (Entrada -> Intermediaria) de maiorFitness e segundoMaiorFitness para menorFitness e segundoMenorFitness
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				e3.pesosEntradaIntermediaria[i][j] = e1.pesosEntradaIntermediaria[i][j];
				e4.pesosEntradaIntermediaria[i][j] = e2.pesosEntradaIntermediaria[i][j];
				auxiliar1[i][j] = e1.pesosEntradaIntermediaria[i][j];
				auxiliar3[i][j] = e2.pesosEntradaIntermediaria[i][j];
			}
		}
		
		
		// Copia (Intermediaria -> Saida) de maiorFitness e segundoMaiorFitness para menorFitness e segundoMenorFitness
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				e3.pesosIntermediariaSaida[i][j] = e1.pesosIntermediariaSaida[i][j];
				e4.pesosIntermediariaSaida[i][j] = e2.pesosIntermediariaSaida[i][j];
				auxiliar2[i][j] = e1.pesosIntermediariaSaida[i][j];
				auxiliar4[i][j] = e2.pesosIntermediariaSaida[i][j];
			}
		}
		
		
		// Crossover matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				if(r.nextDouble() < chanceCross){
					e3.pesosEntradaIntermediaria[i][j] = auxiliar1[i][j];
				}
			}
		}
		
		
		// Crossover matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceCross){
					e3.pesosIntermediariaSaida[i][j] = auxiliar2[i][j];
				}
			}
		}
		
		// Crossover matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				if(r.nextDouble() < chanceCross){
					e4.pesosEntradaIntermediaria[i][j] = auxiliar3[i][j];
				}
			}
		}
		
		
		// Crossover matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceCross){
					e4.pesosIntermediariaSaida[i][j] = auxiliar4[i][j];
				}
			}
		}
		//e1.pesosIntermediariaSaida = e2.pesosIntermediariaSaida;
		//e2.pesosEntradaIntermediaria = e1.pesosEntradaIntermediaria;
		
		
		Collections.sort(this.populacao);
	}
	
	public void mutacao(){
		
	}
	
	// Executa a rede neural - feedforward
	public void executar(Elemento e){
		
		int i = 0;
		int j = 0;
		
		// CALCULAR UMA LINHA DE ENTRADA DE CADA VEZ
		for(i = 0; i < numeroEntradas; i++){
			//System.out.println("INICIO DA ENTRADA " + (i+1));
			// Calcular sinapses Entrada -> Intermediaria
			// Colocar o valor de entrada nos neuronios de entrada
			// Calcular a saida usando a funcao de ativacao + pesos
			
			// Iterar por cada neuronio da camada de Entrada, setando seu vetor de entrada e calculando sua saida			
			for (Neuronio n : entrada.getNeuronios()){
				n.setEntrada(e.dadosEntrada[i][j], i);
				n.calcularValor(e.dadosEntrada[i].length - 1);
				e.dadosEntradaIntermediaria[j] = n.calcularSaida(e.pesosEntradaIntermediaria[j]);
				/*
				System.out.println("\nSaidas N" + (j+1));
				for(int k = 0; k < tamanhoIntermediaria; k++){
					System.out.print(e.dadosEntradaIntermediaria[j][k] + " ");
				}
				System.out.println();*/
				j++;
			}			
			j = 0;
			
			e.dadosEntradaIntermediariaTransformada = transformaMatriz(e.dadosEntradaIntermediaria);
			// Calcular sinapses Intermediaria -> Saida
			// O valor de entrada da camada Intermediaria eh composto pelas saidas da camada de Entrada
			// Calcular a saida usando a funcao de ativacao + pesos
			
			// Iterar por cada neuronio da camada Intermediaria, setando seu vetor de entrada e calculando suas saidas
			for(Neuronio n : intermediaria.getNeuronios()){
				n.setEntrada(e.dadosEntradaIntermediariaTransformada[j]);
				n.calcularValor(e.dadosEntradaIntermediariaTransformada[j].length);
				//System.out.print("\nValor N" + (j+3) + "\n" + n.getValor());
				e.dadosIntermediariaSaida[j] = n.calcularSaida(e.pesosIntermediariaSaida[j]);
				/*
				System.out.println("\nSaidas N" + (j+3));
				for(int k = 0; k < tamanhoSaida; k++){
					System.out.print(e.dadosIntermediariaSaida[j][k] + " ");
				}
				System.out.println();*/
				j++;
			}
			j = 0;
			
			
			e.dadosIntermediariaSaidaTransformada = transformaMatriz(e.dadosIntermediariaSaida);
			// Calcular a Saida final
			// Colocar o valor de entrada nos neuronios de entrada
			// Calcular a saida usando a funcao de ativacao + pesos
					
			// Iterar por cada neuronio da camada Saida, setando seu vetor de entrada e calculando suas saidas
			for(Neuronio n : saida.getNeuronios()){
				n.setEntrada(e.dadosIntermediariaSaidaTransformada[j]);
				n.calcularValor(e.dadosIntermediariaSaidaTransformada[j].length);
				e.dadosSaida[i][j] = n.getValor();
				/*
				System.out.println("\nSaidas N" + (j+6));
				for(int k = 0; k < tamanhoSaida; k++){
					System.out.print(e.dadosSaida[i][j] + " ");
				}
				System.out.println();*/
				j++;
			}
			j = 0;
			//System.out.println("\nFIM DA ENTRADA " + (i+1) + "\n");
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
	
	


	public void printarMatrizes(Elemento e){
		System.out.println("\nELEMENTO " + e.getElementoID());
		
		// Printa Matriz de Entrada
		System.out.println("Matriz de Entrada: ");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoEntrada; j++){				
				System.out.print(e.dadosEntrada[i][j] + ", ");
			}
			System.out.println();
		}
		
		// Printa Matriz de Saida Desejada
		System.out.println("Matriz de Saida Desejada (Treino): ");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){				
				System.out.print(e.saidaTreino[i][j] + ", ");
			}
			System.out.println();
		}
		// Printa Matriz de Peso 1
		System.out.println("\nMatriz de Peso 1 (Entrada -> Intermediaria):");
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				System.out.print(e.pesosEntradaIntermediaria[i][j] + ", ");				
			}
			System.out.println();
		}
		
		// Printa Matriz de Peso 2
		System.out.println("\nMatriz de Peso 2 (Intermediaria -> Saida):");
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(e.pesosIntermediariaSaida[i][j] + ", ");
			}
			System.out.println();
		}
		
		// Printa Matriz da Saida
		System.out.println("\nMatriz de Saida:");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(e.dadosSaida[i][j] + ", ");	
			}			
		}
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
