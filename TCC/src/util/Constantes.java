package util;

public final class Constantes {
	
	public static final boolean treino = false; // define se eh treino ou execucao normal
	public static final boolean normalizacaoDesvio = false; // define tipo de normalizacao utilizada
	public static final boolean erroQuadratico = true; // define tipo de erro calculado
	public static final boolean pesoGaussian = true; // se true, peso entra -1 e 1
	public static final boolean genetico = false; // define se GA ou Backpropagation
	
	
	// Numero de entradas (Testes / Linhas do arquivo)
	// Cada linha do arquivo representa uma entrada
	public static final int numeroEntradas = 10;
	
	public static final int numeroEntradasExecucao = 1;
	
	// Tamanho das camadas
	public static final int tamanhoEntrada = 10;
	public static final int tamanhoIntermediaria = 4;
	public static final int tamanhoSaida = 1;
	
	// Tamanho da populacao
	public static final int tamanhoPopulacao = 500;
	
	// Chance de Crossover
	public static final double chanceCross = 0.30;
	
	//Numero maximo de geracoes
	public static final int geracoes = 1000;
	
	// Chance de mutacao
	public static final double chanceMutacao = 0.05;
	
	// Parametros backpropagation
	public static final double learningRate = 0.7;
	public static final double momento = 0.1;
	public static final double lambda = 0.002;
	public static final double erroMinimo = 0.0005;
	public static final long maxIteracoes = 50000;
	
}
