package util;

public final class Constantes {
	
	public static final boolean treino = false; // define se eh treino ou execucao normal
	public static final boolean normalizacaoDesvio = false; // define tipo de normalizacao utilizada
	public static final boolean erroQuadratico = true; // define tipo de erro calculado
	public static final boolean pesoGaussian = true; // se true, peso entra -1 e 1
	public static final boolean genetico = true; // define se GA ou Backpropagation
	
	
	// Numero de entradas (Testes / Linhas do arquivo)
	// Cada linha do arquivo representa uma entrada
	// 4 meses = 124
	// Ano todo = 360
	public static final int numeroEntradas = 360;
	
	public static final int numeroEntradasExecucao = 1;
	
	// Tamanho das camadas
	public static final int tamanhoEntrada = 10;
	public static final int tamanhoIntermediaria1 = 15;
	public static final int tamanhoIntermediaria2 = 15;
	public static final int tamanhoSaida = 1;
	
	// Tamanho da populacao
	public static final int tamanhoPopulacao = 400;
	
	// Chance de Crossover
	public static final double chanceCross = 0.30;
	
	//Numero maximo de geracoes
	public static final int geracoes = 30000;
	
	// Chance de mutacao
	public static final double chanceMutacao = 0.05;
	
	// Parametros backpropagation
	public static final double learningRate = 0.5;
	public static final double momento = 0.2;
	public static final double lambda = 0.002;
	public static final double erroMinimo = 0.03;
	public static final long maxIteracoes = 50000;
	
}
