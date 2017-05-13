package guilherme.tcc.classes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.python.antlr.ast.Slice.Slice___init___exposer;

import com.mysql.jdbc.log.Slf4JLogger;

import guilherme.tcc.DAO.MedicaoDAO;
import guilherme.tcc.DAO.MedicaoDAOImpl;
import static util.Constantes.*;

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
	
	private Camada entrada; // Camada de entrada
	private Camada intermediaria; // Camada intermediaria
	private Camada saida; /// Camada de saida
	
	private List<Elemento> populacao;
	private Elemento elementoExecucao;
	
	private int maiorFitness; // ID do elemento de maior fitness
	private int segundoMaiorFitness; // ID do elemento com segundo maior fitness
	private int menorFitness;
	private int segundoMenorFitness;
	
	// Matrizes para auxiliar a criacao dos arquivos de entrada de treino
	private double maximasEntrada[][];// = new double[tamanhoEntrada][numeroEntradas];
	private double maximasEntradaInvertida[][];// = new double[numeroEntradas][tamanhoEntrada];
	
	private double minimasEntrada[][];// = new double[tamanhoEntrada][numeroEntradas];
	private double minimasEntradaInvertida[][];// = new double[numeroEntradas][tamanhoEntrada];
	
	private double mediasEntrada[][];// = new double[tamanhoEntrada][numeroEntradas];
	private double mediasEntradaInvertida[][];// = new double[numeroEntradas][tamanhoEntrada];
	
	private double precipitacaoEntrada[][];// = new double[tamanhoEntrada][numeroEntradas];
	private double precipitacaoEntradaInvertida[][];// = new double[numeroEntradas][tamanhoEntrada];
	
	// Matrizes para auxiliar a criacao do arquivo de saida de treino
	private double maximasSaida[][];// = new double[tamanhoSaida][numeroEntradas];
	private double maximasSaidaInvertida[][];// = new double[numeroEntradas][tamanhoSaida];
	
	private double minimasSaida[][];// = new double[tamanhoSaida][numeroEntradas];
	private double minimasSaidaInvertida[][];// = new double[numeroEntradas][tamanhoSaida];
	
	private double mediasSaida[][];// = new double[tamanhoSaida][numeroEntradas];
	private double mediasSaidaInvertida[][];// = new double[numeroEntradas][tamanhoSaida];
	
	private double precipitacaoSaida[][];// = new double[tamanhoEntrada][numeroEntradas];
	private double precipitacaoSaidaInvertida[][];// = new double[numeroEntradas][tamanhoEntrada];
	
	// Maximos e Minimos usados para normalizacao dos dados
	private double minEntradaMax;
	private double maxEntradaMax;
	private double minEntradaMin;
	private double maxEntradaMin;
	private double minEntradaMed;
	private double maxEntradaMed;
	private double minEntradaPrec;
	private double maxEntradaPrec;
	
	private double minSaidaMax;
	private double maxSaidaMax;
	private double minSaidaMin;
	private double maxSaidaMin;
	private double minSaidaMed;
	private double maxSaidaMed;
	private double minSaidaPrec;
	private double maxSaidaPrec;
	
	
	// Media e Desvio - Normalizacao
	double mediaMaximaEntrada;
	double mediaMinimaEntrada;
	double mediaMedEntrada;
	double mediaPrecEntrada;
	double mediaMaximaSaida;
	double mediaMinimaSaida;
	double mediaMedSaida;
	double mediaPrecSaida;
	
	double desvioMaximaEntrada;
	double desvioMinimaEntrada;
	double desvioMedEntrada;
	double desvioPrecEntrada;
	double desvioMaximaSaida;
	double desvioMinimaSaida;
	double desvioMedSaida;
	double desvioPrecSaida;
	
	public RedeNeural(){
		// Instanciacao das camadas, passando o tamanho delas (numero de neuronios)
		this.entrada = new Camada(tamanhoEntrada);
		this.intermediaria = new Camada(tamanhoIntermediaria);
		this.saida = new Camada(tamanhoSaida);
		
		// Inicializacao da populacao
		this.populacao = new ArrayList<Elemento>();
		
		this.maiorFitness = 0;
		this.segundoMaiorFitness = 0;
		this.menorFitness = 0;
		this.segundoMenorFitness = 0;
		
		if(treino){
			maximasEntrada = new double[tamanhoEntrada][numeroEntradas];
			maximasEntradaInvertida = new double[numeroEntradas][tamanhoEntrada];
			
			minimasEntrada = new double[tamanhoEntrada][numeroEntradas];
			minimasEntradaInvertida = new double[numeroEntradas][tamanhoEntrada];
			
			mediasEntrada = new double[tamanhoEntrada][numeroEntradas];
			mediasEntradaInvertida = new double[numeroEntradas][tamanhoEntrada];
			
			precipitacaoEntrada = new double[tamanhoEntrada][numeroEntradas];
			precipitacaoEntradaInvertida = new double[numeroEntradas][tamanhoEntrada];
			
			// Matrizes para auxiliar a criacao do arquivo de saida de treino
			maximasSaida = new double[tamanhoSaida][numeroEntradas];
			maximasSaidaInvertida = new double[numeroEntradas][tamanhoSaida];
			
			minimasSaida = new double[tamanhoSaida][numeroEntradas];
			minimasSaidaInvertida = new double[numeroEntradas][tamanhoSaida];
			
			mediasSaida = new double[tamanhoSaida][numeroEntradas];
			mediasSaidaInvertida = new double[numeroEntradas][tamanhoSaida];
			
			precipitacaoSaida = new double[tamanhoEntrada][numeroEntradas];
			precipitacaoSaidaInvertida = new double[numeroEntradas][tamanhoEntrada];
		}else{
			maximasEntrada = new double[tamanhoEntrada][numeroEntradasExecucao];
			maximasEntradaInvertida = new double[numeroEntradas][tamanhoEntrada];
			
			minimasEntrada = new double[tamanhoEntrada][numeroEntradasExecucao];
			minimasEntradaInvertida = new double[numeroEntradasExecucao][tamanhoEntrada];
			
			mediasEntrada = new double[tamanhoEntrada][numeroEntradasExecucao];
			mediasEntradaInvertida = new double[numeroEntradasExecucao][tamanhoEntrada];
			
			precipitacaoEntrada = new double[tamanhoEntrada][numeroEntradasExecucao];
			precipitacaoEntradaInvertida = new double[numeroEntradasExecucao][tamanhoEntrada];
			
			// Matrizes para auxiliar a criacao do arquivo de saida de treino
			maximasSaida = new double[tamanhoSaida][numeroEntradasExecucao];
			maximasSaidaInvertida = new double[numeroEntradasExecucao][tamanhoSaida];
			
			minimasSaida = new double[tamanhoSaida][numeroEntradasExecucao];
			minimasSaidaInvertida = new double[numeroEntradasExecucao][tamanhoSaida];
			
			mediasSaida = new double[tamanhoSaida][numeroEntradasExecucao];
			mediasSaidaInvertida = new double[numeroEntradasExecucao][tamanhoSaida];
			
			precipitacaoSaida = new double[tamanhoEntrada][numeroEntradasExecucao];
			precipitacaoSaidaInvertida = new double[numeroEntradasExecucao][tamanhoEntrada];			
		}
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
	 * 
	 * Retorna numero de rodadas de treino
	 */
	public Elemento treinar(String entrada, String saida){
		double fit; // Valor do fitness
		int iteracoes = 0;
		
		
		
		if(genetico){
			this.gerarPopulacaoInicial(entrada, saida);
			
			while(iteracoes < geracoes){
				/*
				this.maiorFitness = 0;
				this.segundoMaiorFitness = 0;
				*/
				this.menorFitness = 0;
				this.segundoMenorFitness = 0;
				 
				
				//System.out.println("RODADA DE TREINO: " + (iteracoes+1));
				// Primeira parte - executar a rede para todo elemento, definindo os 2 com maior fitness
				for(Elemento e : this.populacao){
					//System.out.println("ELEMENTO: " + e.getElementoID());
					this.executar(e); // Executa rede calculando saidas do elemento
					e.calcularFitness(); // Calucla fitness do elemento
					fit = e.getFitness(); // Pega fitness do elemento
					
					// If's para determinar os 2 elementos com maior fitness - serao usados para crossover
					if(fit < this.populacao.get(this.maiorFitness).getFitness()){
						//System.out.println("MAIOR " + e.getElementoID() + " " + this.maiorFitness);
						this.segundoMaiorFitness = this.maiorFitness;
						this.maiorFitness = e.getElementoID();
						//System.out.println("Geracao: " + (iteracoes+1) + "\nFitness: " + this.populacao.get(this.maiorFitness).getFitness());
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
			
				
				/*
				System.out.println("Maior Fitness: " + populacao.get(maiorFitness).getFitness() + " = Elemento: " + populacao.get(maiorFitness).getElementoID());
				System.out.println("Segundo Maior Fitness: " + populacao.get(segundoMaiorFitness).getFitness() + " = Elemento: " + populacao.get(segundoMaiorFitness).getElementoID());
				System.out.println("Menor Fitness: " + populacao.get(menorFitness).getFitness() + " = Elemento: " + populacao.get(menorFitness).getElementoID());
				System.out.println("Segundo Menor Fitness: " + populacao.get(segundoMenorFitness).getFitness()+ " = Elemento: " + populacao.get(segundoMenorFitness).getElementoID());
				*/
				
				iteracoes++;
				// Condicao de parada
				/*
				if(populacao.get(maiorFitness).getFitness() < 0.00018){
					System.out.println("\n 0.00018 RODADA DE TREINO: " + (iteracoes+1));
					//return;
				}
				*/
				
				this.crossover();
				// Mutacao - nao afeta os elemenots de maior fitness
				for(Elemento e : this.populacao){
					if(e.getElementoID() != this.maiorFitness && e.getElementoID() != this.segundoMaiorFitness ){
							//&& e.getElementoID() != menorFitness && e.getElementoID() != segundoMenorFitness){
						e.mutacao();
					}
				}
				
				if(iteracoes%5000 == 0){
					System.out.println("Geracao: " + iteracoes);
				}
			}
			return null;
			//return e;
			//return iteracoes;
		}else{ // Treinamento via backpropagation
			Elemento e = new Elemento(0, entrada, saida);
			this.executar(e);
			return e;
			//return iteracoes;
		}			
	}
	
	
	
	public double derivadaValorSigmoide(double valor){
		return valor*(1 - valor);
	}
	
	public double derivadaSigmoide(double valor){
		return Math.exp(-valor)/Math.pow((1 + Math.exp(-valor)), 2);
	}
	
	public void backpropagation(Elemento e, int entrada){
		/*
		int i = 0;
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double ak = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double desiredOutput = expectedOutput[i];
 
                double partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }
 		*/
		
		//System.out.println("SAIDA:");
		for(Neuronio n : saida.getNeuronios()){
			for(int i = 0; i < tamanhoIntermediaria; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					double ak = n.getValor();
					double ai = e.dadosIntermediariaSaidaBP[i]; // MUDAR ESSA PARTE?????
					
					double derivadaParcial = -ak * (1 - ak) * ai *(e.saidaTreino[entrada][j] - ak);
					double deltaPeso = -learningRate * derivadaParcial;
					e.pesosIntermediariaSaidaDeltaAntigo[i][j] = e.pesosIntermediariaSaidaDelta[i][j];
					e.pesosIntermediariaSaidaDelta[i][j] = deltaPeso;
					
					double novoPeso = e.pesosIntermediariaSaida[i][j] + deltaPeso;
					double antigoDelta = e.pesosIntermediariaSaidaDeltaAntigo[i][j];
					
					double antigoPeso = e.pesosIntermediariaSaida[i][j];
					e.pesosIntermediariaSaida[i][j] = (novoPeso + momento * antigoDelta);
					
					// L2
					//e.pesosIntermediariaSaida[i][j] *= 1 - (learningRate * lambda);
					//e.pesosIntermediariaSaida[i][j] -= learningRate * derivadaParcial;
					
					/*
					if(i == 0 && j == 0){
						System.out.println(String.format("dadosIntermediariaSaida[%d][%d]: %f"
								, i, j, e.dadosIntermediariaSaidaTransformada[j][i]));
						
						System.out.println(String.format("ak[%d][%d]: %f\nai[%d][%d]: %f\nSaidaTreino[%d][%d]: %f"
								, i, j, ak, i, j, ai, entrada, j, e.saidaTreino[entrada][j]));
						
						System.out.println(String.format("Derivada Parcial[%d][%d]: %f", i, j, derivadaParcial));
						System.out.println(String.format("deltaPeso[%d][%d]: %f", i, j, deltaPeso));
						System.out.println(String.format("antigoDelta[%d][%d]: %f", i, j, antigoDelta));
						System.out.println(String.format("antigoPeso[%d][%d]: %f", i, j, antigoPeso));
						System.out.println(String.format("novoPeso[%d][%d]: %f", i, j, novoPeso));
						System.out.println();
					}*/
				}				
			}
			
		}
		
		/*
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double aj = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                    double wjk = out_neu.getConnection(n.id).getWeight();
                    double desiredOutput = (double) expectedOutput[j];
                    double ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
 
                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
		*/
		
		//System.out.println("INTERMEDIARIA:");
		for(Neuronio n : intermediaria.getNeuronios()){
			for(int i = 0; i < tamanhoEntrada; i++){
				for(int j = 0; j < tamanhoIntermediaria; j++){
					double aj = n.getValor();
					double ai = e.dadosEntradaIntermediariaBP[i];
					double somaOutputs = 0;
					
					int l = 0;
					for(Neuronio n2 : saida.getNeuronios()){
						double wjk = e.pesosIntermediariaSaida[j][l];
						double ak = n2.getValor();
						
						somaOutputs += (-(e.saidaTreino[entrada][l] - ak) * ak * (1 - ak) * wjk);
						//System.out.println("SOMA OUTPUTS: " + somaOutputs);
						l++;
					}
					//System.out.println(String.format("aj[%d][%d]: %f\nai[%d][%d]: %f", i, j, aj, i, j, ai));
					
					double derivadaParcial = aj * (1 - aj) * ai * somaOutputs;
					double deltaPeso = - learningRate * derivadaParcial;
					e.pesosEntradaIntermediariaDeltaAntigo[i][j] = e.pesosEntradaIntermediariaDelta[i][j];
					e.pesosEntradaIntermediariaDelta[i][j] = deltaPeso;
					
					double antigoPeso = e.pesosEntradaIntermediaria[i][j];
					double novoPeso = e.pesosEntradaIntermediaria[i][j] + deltaPeso;
					double antigoDelta = e.pesosEntradaIntermediariaDeltaAntigo[i][j];
					
					e.pesosEntradaIntermediaria[i][j] = (novoPeso + momento * antigoDelta);
					
					
					// L2
					
					//e.pesosEntradaIntermediaria[i][j] *= 1 - (learningRate * lambda);
					//e.pesosEntradaIntermediaria[i][j] -= learningRate * derivadaParcial;
					
					/*
					if(i == 0 && j == 0){
						System.out.println(String.format("ak[%d][%d]: %f\nai[%d][%d]: %f"
							, i, j, aj, i, j, ai));
						System.out.println(String.format("Derivada Parcial[%d][%d]: %f", i, j, derivadaParcial));
						System.out.println(String.format("deltaPeso[%d][%d]: %f", i, j, deltaPeso));
						System.out.println(String.format("antigoDelta[%d][%d]: %f", i, j, antigoDelta));
						System.out.println(String.format("antigoPeso[%d][%d]: %f", i, j, antigoPeso));
						System.out.println(String.format("novoPeso[%d][%d]: %f", i, j, novoPeso));
						System.out.println();
					}*/
				}
			}
		}
			
		/*
		try{
			TimeUnit.SECONDS.sleep(2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		*/
		
		
		/*
		// PARTE 1
		// Itera sobre camada de saida, calcula matriz de erro das saidas, uma para cada entrada
		// Calcula -(y - yHat)
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				// Calcula erro especifico de cada neuronio
				erroSaida[i][j] = -(e.saidaTreino[i][j] - e.dadosSaida[i][j]); // -y - yHat
				
				//erroSaida[i][j] *= derivadaSigmoide(e.dadosIntermediariaSaidaBPTransformada[i][j][0]); //sigmoidPrime z3
				System.out.println(String.format("erroSaida[%d][%d]: %f", i, j, erroSaida[i][j]));
			}
		}
		*/
		
	}
	
	// Executa a rede neural - feedforward
	public void executar(Elemento e){
		int i = 0;
		int j = 0;
		
		long it = 0;
		// CALCULAR UMA LINHA DE ENTRADA DE CADA VEZ
		
		if(treino){
			
			do{
			//for(int t = 0; t < maxIteracoes; t++){
				for(i = 0; i < numeroEntradas; i++){
					//System.out.println("INICIO DA ENTRADA " + (i+1));
					// Calcular sinapses Entrada -> Intermediaria
					// Colocar o valor de entrada nos neuronios de entrada
					// Calcular a saida usando a funcao de ativacao + pesos
					
					// Iterar por cada neuronio da camada de Entrada, setando seu vetor de entrada e calculando sua saida			
					for (Neuronio n : entrada.getNeuronios()){
						n.setEntrada(e.dadosEntrada[i][j], i);
						n.calcularValor(e.dadosEntrada[i].length, true, 0);
						
						e.dadosEntradaIntermediariaBP[j] = n.getValor(); // BP!!
						
						e.dadosEntradaIntermediaria[j] = n.calcularSaida(e.pesosEntradaIntermediaria[j]);
						j++;
					}
						
					j = 0;
					
					//e.dadosEntradaIntermediariaBPTransformada = transformaMatriz(e.dadosEntradaIntermediariaBP);
					e.dadosEntradaIntermediariaTransformada = transformaMatriz(e.dadosEntradaIntermediaria);
					// Calcular sinapses Intermediaria -> Saida
					// O valor de entrada da camada Intermediaria eh composto pelas saidas da camada de Entrada
					// Calcular a saida usando a funcao de ativacao + pesos
					
					// Iterar por cada neuronio da camada Intermediaria, setando seu vetor de entrada e calculando suas saidas
					for(Neuronio n : intermediaria.getNeuronios()){
						n.setEntrada(e.dadosEntradaIntermediariaTransformada[j]);
						n.calcularValor(e.dadosEntradaIntermediariaTransformada[j].length, false, e.biasIntermediaria);
						
						e.dadosIntermediariaSaidaBP[j] = n.getValor(); // BP!!
						
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
					
					
					//e.dadosIntermediariaSaidaBPTransformada = transformaMatriz(e.dadosIntermediariaSaidaBP);
					e.dadosIntermediariaSaidaTransformada = transformaMatriz(e.dadosIntermediariaSaida);
					// Calcular a Saida final
					// Colocar o valor de entrada nos neuronios de entrada
					// Calcular a saida usando a funcao de ativacao + pesos
							
					// Iterar por cada neuronio da camada Saida, setando seu vetor de entrada e calculando suas saidas
					for(Neuronio n : saida.getNeuronios()){
						n.setEntrada(e.dadosIntermediariaSaidaTransformada[j]);
						n.calcularValor(e.dadosIntermediariaSaidaTransformada[j].length, false, e.biasSaida);
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
					
					if(!genetico){ // Roda backpropagation uma vez pra cada input
						this.backpropagation(e, i);
						e.calcularFitness();
						
						/*
						if(it%200000 == 0){
							System.out.println("Iteracao: " + it);
							System.out.println("Erro: " + e.getFitness());
						}*/
						//System.out.println("Erro: " + e.getFitness());
					}
				}
				it++;
				//System.out.println(e.getFitness());
			//}
			}while(e.getFitness() > erroMinimo);
			System.out.println("ITERACOES: " + it);
		}else{
			for(i = 0; i < numeroEntradasExecucao; i++){
				//System.out.println("INICIO DA ENTRADA " + (i+1));
				// Calcular sinapses Entrada -> Intermediaria
				// Colocar o valor de entrada nos neuronios de entrada
				// Calcular a saida usando a funcao de ativacao + pesos
				
				// Iterar por cada neuronio da camada de Entrada, setando seu vetor de entrada e calculando sua saida			
				for (Neuronio n : entrada.getNeuronios()){
					n.setEntrada(e.dadosEntrada[i][j], i);
					n.calcularValor(e.dadosEntrada[i].length, true, 0);
					e.dadosEntradaIntermediaria[j] = n.calcularSaida(e.pesosEntradaIntermediaria[j]);
					/*
					System.out.println("\nSaidas N" + (j+1));
					for(int k = 0; k < tamanhoIntermediaria; k++){
						System.out.print(e.dadosEntradaIntermediaria[j][k] + " ");
					}
					System.out.println();
					*/
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
					n.calcularValor(e.dadosEntradaIntermediariaTransformada[j].length, false, e.biasIntermediaria);
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
					n.calcularValor(e.dadosIntermediariaSaidaTransformada[j].length, false, e.biasSaida);
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
			}
		}
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
					//e3.pesosEntradaIntermediaria[i][j] = e3.pesosEntradaIntermediaria[i][j] + r.nextDouble();
				}
			}
		}
		
		
		// Crossover matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceCross){
					e3.pesosIntermediariaSaida[i][j] = auxiliar2[i][j];
					//e3.pesosIntermediariaSaida[i][j] = e3.pesosIntermediariaSaida[i][j] + r.nextDouble();
				}
			}
		}
		
		// Crossover bias
		if(r.nextDouble() < chanceCross){
			e3.biasIntermediaria = e1.biasIntermediaria;
		}
		
		if(r.nextDouble() < chanceCross){
			e3.biasSaida = e1.biasSaida;
		}
		
		// Crossover matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				if(r.nextDouble() < chanceCross){
					e4.pesosEntradaIntermediaria[i][j] = auxiliar3[i][j];
					//e4.pesosEntradaIntermediaria[i][j] = e4.pesosEntradaIntermediaria[i][j] + r.nextDouble();
				}
			}
		}
		
		
		// Crossover matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceCross){
					e4.pesosIntermediariaSaida[i][j] = auxiliar4[i][j];
					//e4.pesosIntermediariaSaida[i][j] = e4.pesosIntermediariaSaida[i][j] + r.nextDouble();
				}
			}
		}
		
		
		// Crossover bias
		if(r.nextDouble() < chanceCross){
			e4.biasIntermediaria = e2.biasIntermediaria;
		}
		
		if(r.nextDouble() < chanceCross){
			e4.biasSaida = e2.biasSaida;
		}
		
		Collections.sort(this.populacao);
	}
	// Gera populacao inicial - valores aleatorios para os pesos
	// A populacao eh formada por um conjunto de matrizes de peso
	// Cada "membro" da populacao eh um conjunto de matrizes de peso
	public void gerarPopulacaoInicial(String entrada, String saida){
		for(int i = 0; i < tamanhoPopulacao; i++){
			this.populacao.add(new Elemento(i, entrada, saida));
		}
	}
	
	// Gera arquivos de entrada e saida que serao usados para treinar a rede
	// Os arquivos de entrada contem medidas de um mesmo dia nos ultimos 10 anos
	// O arquivo de saida contem medidas deste dia no ano seguinte
	// Ex: Arquivo de Entrada: Medidas de 01 de Janeiro ate 31 de Marco por 10 anos (Arquivo com 10 linhas (anos) e 91 colunas (dias))
	//     Arquivo de Saida: Medidas de 01 de Janeiro de 2010 (Uma linha do arquivo, 1 saida)
	public void gerarArquivosDeTreino(){
		MedicaoDAO medicaoDAO = new MedicaoDAOImpl();
		
		// Inicialmente seleciona 3 meses inteiros (~90 linhas)
		// Anos 2004 ate 2013 - Entradas de treino
		List<Medicao> m2004 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2004-01-01"), Date.valueOf("2004-03-31"));
		List<Medicao> m2005 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2005-01-01"), Date.valueOf("2005-03-31"));
		List<Medicao> m2006 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2006-01-01"), Date.valueOf("2006-03-31"));
		List<Medicao> m2007 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2007-01-01"), Date.valueOf("2007-03-31"));
		List<Medicao> m2008 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2008-01-01"), Date.valueOf("2008-03-31"));
		List<Medicao> m2009 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2009-01-01"), Date.valueOf("2009-03-31"));
		List<Medicao> m2010 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2010-01-01"), Date.valueOf("2010-03-31"));
		List<Medicao> m2011 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2011-01-01"), Date.valueOf("2011-03-31"));
		List<Medicao> m2012 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2012-01-01"), Date.valueOf("2012-03-31"));
		List<Medicao> m2013 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2013-01-01"), Date.valueOf("2013-03-31"));
		
		// Ano 2014 - Saida de treino
		List<Medicao> m2014 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2014-01-01"), Date.valueOf("2014-03-31"));
		
		this.gerarEntradaAuxiliar(m2004, 0);
		this.gerarEntradaAuxiliar(m2005, 1);
		this.gerarEntradaAuxiliar(m2006, 2);
		this.gerarEntradaAuxiliar(m2007, 3);
		this.gerarEntradaAuxiliar(m2008, 4);
		this.gerarEntradaAuxiliar(m2009, 5);
		this.gerarEntradaAuxiliar(m2010, 6);
		this.gerarEntradaAuxiliar(m2011, 7);
		this.gerarEntradaAuxiliar(m2012, 8);
		this.gerarEntradaAuxiliar(m2013, 9);
		
		this.gerarSaidaAuxiliar(m2014, 0);
		
		maximasEntradaInvertida = this.transformaMatriz(maximasEntrada);
		minimasEntradaInvertida = this.transformaMatriz(minimasEntrada);
		mediasEntradaInvertida = this.transformaMatriz(mediasEntrada);
		precipitacaoEntradaInvertida = this.transformaMatriz(precipitacaoEntrada);
		
		maximasSaidaInvertida = this.transformaMatriz(maximasSaida);
		minimasSaidaInvertida = this.transformaMatriz(minimasSaida);
		mediasSaidaInvertida = this.transformaMatriz(mediasSaida);
		precipitacaoSaidaInvertida = this.transformaMatriz(precipitacaoSaida);
		
		this.normalizarDados();
		
		// Escreve arquivos de entrada e saida
		try{
			
			DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
			ponto.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("00.0000", ponto);
			
			String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Treinamento/";
			PrintWriter writerMaxima = new PrintWriter(path + "entradasMaxima.txt", "UTF-8");
			PrintWriter writerMinima = new PrintWriter(path + "entradasMinima.txt", "UTF-8");
			PrintWriter writerMedia = new PrintWriter(path + "entradasMedia.txt", "UTF-8");
			PrintWriter writerPrecipitacao = new PrintWriter(path + "entradasPrecipitacao.txt", "UTF-8");
			
			PrintWriter writerMaximaSaida = new PrintWriter(path + "saidasMaxima.txt", "UTF-8");
			PrintWriter writerMinimaSaida = new PrintWriter(path + "saidasMinima.txt", "UTF-8");
			PrintWriter writerMediaSaida = new PrintWriter(path + "saidasMedia.txt", "UTF-8");
			PrintWriter writerPrecipitacaoSaida = new PrintWriter(path + "saidasPrecipitacao.txt", "UTF-8");
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					//System.out.print(String.format("%.2f", maximasInvertida[i][j]));
					//System.out.print(" ");
				    writerMaxima.print(df.format(maximasEntradaInvertida[i][j]) + " ");
				    writerMinima.print(df.format(minimasEntradaInvertida[i][j]) + " ");
				    writerMedia.print(df.format(mediasEntradaInvertida[i][j]) + " ");
				    writerPrecipitacao.print(df.format(precipitacaoEntradaInvertida[i][j]) + " ");
				}
				writerMaxima.println();
				writerMinima.println();
				writerMedia.println();
				writerPrecipitacao.println();
			}
			writerMaxima.close();
			writerMinima.close();
			writerMedia.close();
			writerPrecipitacao.close();
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					//System.out.print(String.format("%.2f", maximasInvertida[i][j]));
					//System.out.print(" ");
					writerMaximaSaida.print(df.format(maximasSaidaInvertida[i][j]) + " ");
					writerMinimaSaida.print(df.format(minimasSaidaInvertida[i][j]) + " ");
				    writerMediaSaida.print(df.format(mediasSaidaInvertida[i][j]) + " ");
				    writerPrecipitacaoSaida.print(df.format(precipitacaoSaidaInvertida[i][j]) + " ");
				}
				writerMaximaSaida.println();
				writerMinimaSaida.println();
				writerMediaSaida.println();
				writerPrecipitacaoSaida.println();
			}
			
			// Fecha todos os writers
			
			writerMaximaSaida.close();
			writerMinimaSaida.close();
			writerMediaSaida.close();
			writerPrecipitacaoSaida.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void normalizarDados(){
		
		int conta = 0;
		double totalMaxima = 0;
		double totalMinima = 0;
		double totalMed = 0;
		double totalPrec = 0;
		
		double auxMaxima = 0;
		double auxMinima = 0;
		double auxMed = 0;
		double auxPrec = 0;
		
		minEntradaMax = 100;
		maxEntradaMax = 0;
		minEntradaMin = 100;
		maxEntradaMin = 0;
		minEntradaMed = 100;
		maxEntradaMed = 0;
		minEntradaPrec = 100;
		maxEntradaPrec = 0;
		
		minSaidaMax = 100;
		maxSaidaMax = 0;
		minSaidaMin = 100;
		maxSaidaMin = 0;
		minSaidaMed = 100;
		maxSaidaMed = 0;
		minSaidaPrec = 100;
		maxSaidaPrec = 0;
		
		
		if(normalizacaoDesvio){
		// Loop para calcular total - achar media
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					totalMaxima += maximasEntradaInvertida[i][j];
					totalMinima += minimasEntradaInvertida[i][j];
					totalMed += mediasEntradaInvertida[i][j];
					totalPrec += precipitacaoEntradaInvertida[i][j];
					conta++;
				}
			}
			
			mediaMaximaEntrada = totalMaxima/conta;
			mediaMinimaEntrada = totalMinima/conta;
			mediaMedEntrada = totalMed/conta;
			mediaPrecEntrada = totalPrec/conta;
			
			// Calcula valor necessario para calcular desvio
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					auxMaxima += Math.pow((maximasEntradaInvertida[i][j] - mediaMaximaEntrada), 2);
					auxMinima += Math.pow((minimasEntradaInvertida[i][j] - mediaMinimaEntrada), 2);
					auxMed += Math.pow((mediasEntradaInvertida[i][j] - mediaMedEntrada), 2);
					auxPrec += Math.pow((precipitacaoEntradaInvertida[i][j] - mediaPrecEntrada), 2);
				}
			}
			
			desvioMaximaEntrada = Math.sqrt((auxMaxima/conta));
			desvioMinimaEntrada = Math.sqrt((auxMinima/conta));
			desvioMedEntrada = Math.sqrt((auxMed/conta));
			desvioPrecEntrada = Math.sqrt((auxPrec/conta));
			
			totalMaxima = 0;
			totalMinima = 0;
			totalMed = 0;
			totalPrec = 0;
			conta = 0;
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					totalMaxima += maximasSaidaInvertida[i][j];
					totalMinima += minimasSaidaInvertida[i][j];
					totalMed += mediasSaidaInvertida[i][j];
					totalPrec += precipitacaoSaidaInvertida[i][j];
					conta++;
				}
			}
			
			mediaMaximaSaida = totalMaxima/conta;
			mediaMinimaSaida = totalMinima/conta;
			mediaMedSaida = totalMed/conta;
			mediaPrecSaida = totalPrec/conta;
			
			// Calcula valor necessario para calcular desvio
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					auxMaxima += Math.pow((maximasSaidaInvertida[i][j] - mediaMaximaSaida), 2);
					auxMinima += Math.pow((minimasSaidaInvertida[i][j] - mediaMinimaSaida), 2);
					auxMed += Math.pow((mediasSaidaInvertida[i][j] - mediaMedSaida), 2);
					auxPrec += Math.pow((precipitacaoSaidaInvertida[i][j] - mediaPrecSaida), 2);
				}
			}
			
			desvioMaximaSaida = Math.sqrt((auxMaxima/conta));
			desvioMinimaSaida = Math.sqrt((auxMinima/conta));
			desvioMedSaida = Math.sqrt((auxMed/conta));
			desvioPrecSaida = Math.sqrt((auxPrec/conta));
			
			
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					maximasEntradaInvertida[i][j] = (maximasEntradaInvertida[i][j] - mediaMaximaEntrada)/desvioMaximaEntrada;
					minimasEntradaInvertida[i][j] = (minimasEntradaInvertida[i][j] - mediaMinimaEntrada)/desvioMinimaEntrada;
					mediasEntradaInvertida[i][j] = (mediasEntradaInvertida[i][j] - mediaMedEntrada)/desvioMedEntrada;
					precipitacaoEntradaInvertida[i][j] = (precipitacaoEntradaInvertida[i][j] - mediaPrecEntrada)/desvioPrecEntrada;
				}
			}
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					maximasSaidaInvertida[i][j] = (maximasSaidaInvertida[i][j] - mediaMaximaSaida)/desvioMaximaSaida;
					minimasSaidaInvertida[i][j] = (minimasSaidaInvertida[i][j] - mediaMinimaSaida)/desvioMinimaSaida;
					mediasSaidaInvertida[i][j] = (mediasSaidaInvertida[i][j] - mediaMedSaida)/desvioMedSaida;
					precipitacaoSaidaInvertida[i][j] = (precipitacaoSaidaInvertida[i][j] - mediaPrecSaida)/desvioPrecSaida;
				}
			}
			
			// SALVAR ARQUIVOS COM MEDIA E DESVIO PADRAO DE ENTRADA E SAIDA -- TREINO
			try{
				DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
				ponto.setDecimalSeparator('.');
				DecimalFormat df = new DecimalFormat("00.#######", ponto);
				
				String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/";
				PrintWriter writerNormalEntrada = new PrintWriter(path + "normalizacaoEntrada.txt", "UTF-8");
				PrintWriter writerNormalSaida = new PrintWriter(path + "normalizacaoSaida.txt", "UTF-8");
				
				for(int i = 0; i < numeroEntradasExecucao; i++){
					writerNormalEntrada.println(String.format("%s %s", df.format(mediaMaximaEntrada), df.format(desvioMaximaEntrada)));
					writerNormalEntrada.println(String.format("%s %s", df.format(mediaMinimaEntrada), df.format(desvioMinimaEntrada)));
					writerNormalEntrada.println(String.format("%s %s", df.format(mediaMedEntrada), df.format(desvioMedEntrada)));
					writerNormalEntrada.println(String.format("%s %s", df.format(mediaPrecEntrada), df.format(desvioPrecEntrada)));
				}
				for(int i = 0; i < tamanhoSaida; i++){
					writerNormalSaida.println(String.format("%s %s", df.format(mediaMaximaSaida), df.format(desvioMaximaSaida)));
					writerNormalSaida.println(String.format("%s %s", df.format(mediaMinimaSaida), df.format(desvioMinimaSaida)));
					writerNormalSaida.println(String.format("%s %s", df.format(mediaMedSaida), df.format(desvioMedSaida)));
					writerNormalSaida.println(String.format("%s %s", df.format(mediaPrecSaida), df.format(desvioPrecSaida)));
				}
				writerNormalEntrada.close();
				writerNormalSaida.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
		
			// Loop para determinar os valores minimos e maximos do dataset
			// Esses valores sao usados na normalizacao
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					if(maximasEntradaInvertida[i][j] > maxEntradaMax){
						maxEntradaMax = maximasEntradaInvertida[i][j];
					}
					if(maximasEntradaInvertida[i][j] < minEntradaMax){
						minEntradaMax = maximasEntradaInvertida[i][j];
					}
					
					if(minimasEntradaInvertida[i][j] > maxEntradaMin){
						maxEntradaMin = minimasEntradaInvertida[i][j];
					}
					if(minimasEntradaInvertida[i][j] < minEntradaMin){
						minEntradaMin = minimasEntradaInvertida[i][j];
					}
					
					if(mediasEntradaInvertida[i][j] > maxEntradaMed){
						maxEntradaMed = mediasEntradaInvertida[i][j];
					}
					if(mediasEntradaInvertida[i][j] < minEntradaMed){
						minEntradaMed = mediasEntradaInvertida[i][j];
					}
					
					if(precipitacaoEntradaInvertida[i][j] > maxEntradaPrec){
						maxEntradaPrec = precipitacaoEntradaInvertida[i][j];
					}
					if(precipitacaoEntradaInvertida[i][j] < minEntradaPrec){
						minEntradaPrec = precipitacaoEntradaInvertida[i][j];
					}
					
				}
				for(int j = 0; j < tamanhoSaida; j++){
					if(maximasSaidaInvertida[i][j] > maxSaidaMax){
						maxSaidaMax = maximasSaidaInvertida[i][j];
					}
					if(maximasSaidaInvertida[i][j] < minSaidaMax){
						minSaidaMax = maximasSaidaInvertida[i][j];
					}
					
					if(minimasSaidaInvertida[i][j] > maxSaidaMin){
						maxSaidaMin = minimasSaidaInvertida[i][j];
					}
					if(minimasSaidaInvertida[i][j] < minSaidaMin){
						minSaidaMin = minimasSaidaInvertida[i][j];
					}
					
					if(mediasSaidaInvertida[i][j] > maxSaidaMed){
						maxSaidaMed = mediasSaidaInvertida[i][j];
					}
					if(mediasSaidaInvertida[i][j] < minSaidaMed){
						minSaidaMed = mediasSaidaInvertida[i][j];
					}
					
					if(precipitacaoSaidaInvertida[i][j] > maxSaidaPrec){
						maxSaidaPrec = precipitacaoSaidaInvertida[i][j];
					}
					if(precipitacaoSaidaInvertida[i][j] < minSaidaPrec){
						minSaidaPrec = precipitacaoSaidaInvertida[i][j];
					}
				}
			}
			//System.out.println(minEntradaMax + "\n" + maxEntradaMax);
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					maximasEntradaInvertida[i][j] = (maximasEntradaInvertida[i][j] - minEntradaMax)/(maxEntradaMax - minEntradaMax);
					minimasEntradaInvertida[i][j] = (minimasEntradaInvertida[i][j] - minEntradaMin)/(maxEntradaMin - minEntradaMin);
					mediasEntradaInvertida[i][j] = (mediasEntradaInvertida[i][j] - minEntradaMed)/(maxEntradaMed - minEntradaMed);
					precipitacaoEntradaInvertida[i][j] = (precipitacaoEntradaInvertida[i][j] - minEntradaPrec)/(maxEntradaPrec - minEntradaPrec);
				}
			}
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					maximasSaidaInvertida[i][j] = (maximasSaidaInvertida[i][j] - minSaidaMax)/(maxSaidaMax - minSaidaMax);
					minimasSaidaInvertida[i][j] = (minimasSaidaInvertida[i][j] - minSaidaMin)/(maxSaidaMin - minSaidaMin);
					mediasSaidaInvertida[i][j] = (mediasSaidaInvertida[i][j] - minSaidaMed)/(maxSaidaMed - minSaidaMed);
					precipitacaoSaidaInvertida[i][j] = (precipitacaoSaidaInvertida[i][j] - minSaidaPrec)/(maxSaidaPrec - minSaidaPrec);
				}
			}
			
			// SALVAR ARQUIVOS COM MINIMOS E MAXIMOS DE ENTRADA E SAIDA -- TREINO
			try{
				String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/";
				PrintWriter writerNormalEntrada = new PrintWriter(path + "normalizacaoEntrada.txt", "UTF-8");
				PrintWriter writerNormalSaida = new PrintWriter(path + "normalizacaoSaida.txt", "UTF-8");
				
				DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
				ponto.setDecimalSeparator('.');
				DecimalFormat df = new DecimalFormat("00.#######", ponto);
				
				for(int i = 0; i < numeroEntradasExecucao; i++){
					writerNormalEntrada.println(String.format("%s %s", df.format(minEntradaMax), df.format(maxEntradaMax)));
					writerNormalEntrada.println(String.format("%s %s", df.format(minEntradaMin), df.format(maxEntradaMin)));
					writerNormalEntrada.println(String.format("%s %s", df.format(minEntradaMed), df.format(maxEntradaMed)));
					writerNormalEntrada.println(String.format("%s %s", df.format(minEntradaPrec), df.format(maxEntradaPrec)));
				}
				for(int i = 0; i < tamanhoSaida; i++){
					writerNormalSaida.println(String.format("%s %s", df.format(minSaidaMax), df.format(maxSaidaMax)));
					writerNormalSaida.println(String.format("%s %s", df.format(minSaidaMin), df.format(maxSaidaMin)));
					writerNormalSaida.println(String.format("%s %s", df.format(minSaidaMed), df.format(maxSaidaMed)));
					writerNormalSaida.println(String.format("%s %s", df.format(minSaidaPrec), df.format(maxSaidaPrec)));
				}
				writerNormalEntrada.close();
				writerNormalSaida.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}		
	}
	
	public void normalizarDadosExecucao(Elemento e, String rede){
		 
		try{
			String linha;
			int contador = 0;
			
			InputStream normalEntradaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "normalizacaoEntrada.txt");
			InputStreamReader normalEntradaISR = new InputStreamReader(normalEntradaIS);
			BufferedReader bufferNormalEntrada = new BufferedReader(normalEntradaISR);
			
			InputStream normalSaidaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "normalizacaoSaida.txt");
			InputStreamReader normalSaidaISR = new InputStreamReader(normalSaidaIS);
			BufferedReader bufferNormalSaida = new BufferedReader(normalSaidaISR);
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferNormalEntrada.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				
				// Preenche minimos e maximos utilizados na normalizacao de dados durante execucao
				switch(contador){
					case 0:
						minEntradaMax = Double.parseDouble(numeros[0]);
						maxEntradaMax = Double.parseDouble(numeros[1]);
						break;
					case 1:
						minEntradaMin = Double.parseDouble(numeros[0]);
						maxEntradaMin = Double.parseDouble(numeros[1]);
						break;
					case 2:
						minEntradaMed = Double.parseDouble(numeros[0]);
						maxEntradaMed = Double.parseDouble(numeros[1]);
						break;
					case 3:
						minEntradaPrec = Double.parseDouble(numeros[0]);
						maxEntradaPrec = Double.parseDouble(numeros[1]);
						break;
				}
				contador++;					
			}
			bufferNormalEntrada.close();
			
			contador = 0;
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferNormalSaida.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
				switch(contador){
					case 0:
						minSaidaMax = Double.parseDouble(numeros[0]);
						maxSaidaMax = Double.parseDouble(numeros[1]);							
					case 1:
						minSaidaMin = Double.parseDouble(numeros[0]);
						maxSaidaMin = Double.parseDouble(numeros[1]);
					case 2:
						minSaidaMed = Double.parseDouble(numeros[0]);
						maxSaidaMed = Double.parseDouble(numeros[1]);
					case 3:
						minSaidaPrec = Double.parseDouble(numeros[0]);
						maxSaidaPrec = Double.parseDouble(numeros[1]);
				}
				contador++;					
			}
			bufferNormalSaida.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		// NORMALIZA ENTRADAS DE EXECUCAO COM BASE NO MINIMO E MAXIMO DE TREINO!
		for(int i = 0; i < numeroEntradasExecucao; i++){
			for(int j = 0; j < tamanhoEntrada; j++){
				switch(rede){
					case "Maxima":
						e.dadosEntrada[i][j] = (e.dadosEntrada[i][j] - minEntradaMax)/(maxEntradaMax - minEntradaMax);
						break;
					case "Minima":
						e.dadosEntrada[i][j] = (e.dadosEntrada[i][j] - minEntradaMin)/(maxEntradaMin - minEntradaMin);
						break;
					case "Media":
						e.dadosEntrada[i][j] = (e.dadosEntrada[i][j] - minEntradaMed)/(maxEntradaMed - minEntradaMed);
						break;
					case "Precipitacao":
						e.dadosEntrada[i][j] = (e.dadosEntrada[i][j] - minEntradaPrec)/(maxEntradaPrec - minEntradaPrec);
						break;
				}
			}
		}
	}
	
	// Desnomarliza os dados para serem apresentados na saida
	// temp define qual tipo de temperatura
	// 0 = Max, 1 = Min, 2 = Med, 3 = precipitacao
	public void desnormalizarDados(Elemento e, int temp){
		if(treino){
			if(normalizacaoDesvio){
				for(int i = 0; i < numeroEntradas; i++){
					for(int j = 0; j < tamanhoSaida; j++){
						if(temp == 0){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioMaximaSaida) + mediaMaximaSaida;
						}
						if(temp == 1){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioMinimaSaida) + mediaMinimaSaida;
						}
						if(temp == 2){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioMedSaida) + mediaMedSaida;
						}
						if(temp == 3){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioPrecSaida) + mediaPrecSaida;
						}
					}
				}
			}else{
				for(int i = 0; i < numeroEntradas; i++){
					for(int j = 0; j < tamanhoSaida; j++){
						if(temp == 0){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMax - minSaidaMax) + minSaidaMax;
						}
						if(temp == 1){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMin - minSaidaMin) + minSaidaMin;
						}
						if(temp == 2){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMed - minSaidaMed) + minSaidaMed;
						}
						if(temp == 3){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaPrec - minSaidaPrec) + minSaidaPrec;
						}
					}
				}
			}
		}else{
			if(normalizacaoDesvio){
				for(int i = 0; i < numeroEntradasExecucao; i++){
					for(int j = 0; j < tamanhoSaida; j++){
						if(temp == 0){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioMaximaSaida) + mediaMaximaSaida;
						}
						if(temp == 1){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioMinimaSaida) + mediaMinimaSaida;
						}
						if(temp == 2){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioMedSaida) + mediaMedSaida;
						}
						if(temp == 3){
							e.dadosSaida[i][j] = (e.dadosSaida[i][j] * desvioPrecSaida) + mediaPrecSaida;
						}
					}
				}
			}else{
				for(int i = 0; i < numeroEntradasExecucao; i++){
					for(int j = 0; j < tamanhoSaida; j++){
						//System.out.println(String.format("MaxSaidaMax: %.4f\nMinSaidaMax: %.4f", maxSaidaMax, minSaidaMax));
						
						if(temp == 0){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMax - minSaidaMax) + minSaidaMax;
						}
						if(temp == 1){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMin - minSaidaMin) + minSaidaMin;
						}
						if(temp == 2){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMed - minSaidaMed) + minSaidaMed;
						}
						if(temp == 3){
							e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaPrec - minSaidaPrec) + minSaidaPrec;
						}
					}
				}
			}
		}
	}
	
	public void salvarArquivosDePesos(String rede, Elemento e){
		// Escreve arquivos de entrada e saida
		try{
			DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
			ponto.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("00.0000", ponto);
			
			String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/";
			PrintWriter writerEntradaIntermediaria = new PrintWriter(path + "pesosEntradaIntermediaria" + rede + ".txt", "UTF-8");
			PrintWriter writerIntermediariaSaida = new PrintWriter(path + "pesosIntermediariaSaida" + rede + ".txt", "UTF-8");
			PrintWriter writerBiasIntermediaria = new PrintWriter(path + "biasIntermediaria" + rede + ".txt", "UTF-8");
			PrintWriter writerBiasSaida = new PrintWriter(path + "biasSaida" + rede + ".txt", "UTF-8");
			
			for(int i = 0; i < tamanhoEntrada; i++){
				for(int j = 0; j < tamanhoIntermediaria; j++){
					writerEntradaIntermediaria.print(e.pesosEntradaIntermediaria[i][j] + " ");
				}
				writerEntradaIntermediaria.println();
			}
			writerEntradaIntermediaria.close();
			
			for(int i = 0; i < tamanhoIntermediaria; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					writerIntermediariaSaida.print(e.pesosIntermediariaSaida[i][j] + " ");
				}
				writerIntermediariaSaida.println();
			}
			writerIntermediariaSaida.close();
			
			writerBiasIntermediaria.print(e.biasIntermediaria);
			writerBiasIntermediaria.close();
			
			writerBiasSaida.print(e.biasSaida);
			writerBiasSaida.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void gerarEntradaAuxiliar(List<Medicao> medicoes, int i){
		int j = 0;
		for(Medicao m : medicoes){
			if(j < numeroEntradas){
				maximasEntrada[i][j] = m.getTemperatura_maxima();
				minimasEntrada[i][j] = m.getTemperatura_minima();
				mediasEntrada[i][j] = m.getTemperatura_media();
				precipitacaoEntrada[i][j] = m.getPrecipitacao();
			}
			j++;
		}
	}
	
	public void gerarSaidaAuxiliar(List<Medicao> medicoes, int i){
		int j = 0;
		for(Medicao m : medicoes){
			if(j < numeroEntradas){
				maximasSaida[i][j] = m.getTemperatura_maxima();
				minimasSaida[i][j] = m.getTemperatura_minima();
				mediasSaida[i][j] = m.getTemperatura_media();
				precipitacaoSaida[i][j] = m.getPrecipitacao();
			}
			j++;
		}
	}
	
	// Metodo chamado pelo ServicePrevisao quando um request e' recebido
	// Este metodo prepara a rede neural para ser executada apos ter sido treinada
	// Forma matriz de entrada -- selects no banco -- parte dificil
	// Le arquivos de pesos salvos
	// Retorna o elemento que sera usado na execucao
	public Elemento prepararExecucao(Date data, String rede){
		
		// Instancia elemento de execucao, nao utiliza populacao
		// Gera elemento com matrizes de peso
		elementoExecucao = new Elemento(rede);
		
		// A partir da data, gerar matriz de entrada 1x10 (1 linha, 10 dias)
		MedicaoDAO medicaoDAO = new MedicaoDAOImpl();
		
		int anoPrevisao; // Ano da Data a ser prevista
		int anoMedicao; // Ano da Data medida
		Calendar calPrevisao = Calendar.getInstance(); // Calendario Data a ser prevista
		Calendar calMedicao = Calendar.getInstance(); // Calenadrio Data medida
		Date dataMedicao;
		
		calPrevisao.setTime(data); 
		anoPrevisao = calPrevisao.get(Calendar.YEAR); 
		
		anoMedicao = anoPrevisao - 10; // Ultimos 10 anos
		calMedicao.setTime(data);
		calMedicao.set(Calendar.YEAR, anoMedicao); // Calendario do primeiro ano a ser medido (Ano a ser previsto - 11)
		
		ArrayList<List<Medicao>> medicoes = new ArrayList<List<Medicao>>();
		int contadorPosicao = 0; // Conta posicao da lista
		int contadorAno = 0; // Menor ano medido ate o momento
		
		for(int i = anoMedicao; i < anoPrevisao; i++, contadorPosicao++){
			List<Medicao> m = new ArrayList<Medicao>();
			
			calMedicao.set(Calendar.YEAR, i);
			dataMedicao = new Date(calMedicao.getTimeInMillis());
			//System.out.println(dataMedicao.toString());
			
			m = medicaoDAO.getMedicaoByDate(dataMedicao);
			
			while(m.isEmpty()){
				contadorAno++;
				calMedicao.set(Calendar.YEAR, (anoMedicao - contadorAno));
				dataMedicao = new Date(calMedicao.getTimeInMillis());
				//System.out.println(dataMedicao.toString() + "CONTADOR ANO: " + contadorAno);
				
				m = medicaoDAO.getMedicaoByDate(dataMedicao);
			}
			
			medicoes.add(m);
			
			//System.out.println("Ano: " + i + "\n" + medicoes.get(contadorPosicao).get(0).getTemperatura_maxima());
		}
		
		int i = 0;
		for(List<Medicao> m : medicoes){
			//System.out.println("DATA: " + m.get(0).getData());
			if(rede.equals("Maxima")){				
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getTemperatura_maxima();
				//System.out.println("TEMP: " + elementoExecucao.dadosEntrada[0][i]);
			}
			if(rede.equals("Minima")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getTemperatura_minima();
			}
			if(rede.equals("Media")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getTemperatura_media();
			}
			if(rede.equals("Precipitacao")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getPrecipitacao();
			}
			i++;
		}
		
		return elementoExecucao;
		//return null;
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
	
	// Multiplicar matrizes
	public double[][] multiplicarMatrizes(double[][] a, double[][] b){
		double c[][] = new double[a.length][b[0].length];
		/*
		if(a[0].length != b.length){
			try {
				throw new Exception("Impossivel Multiplicar");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}*/
		
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < b[0].length; j++){
				for(int k = 0; k < a[0].length; k++){
					c[i][j] += a[j][k] * b[k][j];
				}
			}
		}		
		return c;
	}
	
	public void printarMatrizes(Elemento e){
		System.out.println("ELEMENTO " + e.getElementoID());
		
		
		if(treino){
			// Printa Matriz de Entrada
			System.out.println("Matriz de Entrada: ");
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){				
					System.out.print(e.dadosEntrada[i][j] + ", ");
				}
				System.out.println();
			}
			// Printa Matriz de Saida Desejada
			System.out.println("\nMatriz de Saida Desejada (Treino): ");
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){				
					System.out.print(e.saidaTreino[i][j] + ", ");
				}
				System.out.println();
			}
		}else{
			// Printa Matriz de Entrada
			System.out.println("Matriz de Entrada: ");
			for(int i = 0; i < numeroEntradasExecucao; i++){
				for(int j = 0; j < tamanhoEntrada; j++){				
					System.out.print(e.dadosEntrada[i][j] + ", ");
				}
				System.out.println();
			}
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
		
		// Printa Biass
		System.out.println(String.format("\nBias Intermediaria: %f\nBias Saida: %f", e.biasIntermediaria, e.biasSaida));
		
		if(treino){
			// Printa Matriz da Saida
			System.out.println("\nMatriz de Saida:");
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					System.out.print(e.dadosSaida[i][j] + ", ");	
				}
			}
		}else{
			// Printa Matriz da Saida
			System.out.println("\nMatriz de Saida:");
			for(int i = 0; i < numeroEntradasExecucao; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					System.out.print(e.dadosSaida[i][j] + ", ");	
				}			
			}
		}
		System.out.println();
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

	public Elemento getMaiorFitness(){
		return this.populacao.get(maiorFitness);
	}
}
