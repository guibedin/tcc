package util;

public final class Constantes {
	
	public static final boolean treino = true; // define se eh treino ou execucao normal
	public static final boolean normalizacaoDesvio = false; // define tipo de normalizacao utilizada
	public static final boolean erroQuadratico = true; // define tipo de erro calculado
	public static final boolean tipoPeso = true; // se true, peso entra -1 e 1
	public static final boolean genetico = true; // define se GA ou Backpropagation
	
	// Numero de entradas (Testes / Linhas do arquivo)
	// Cada linha do arquivo representa uma entrada
	public static final int numeroEntradas = 10;
	
	public static final int numeroEntradasExecucao = 1;
	
	// Tamanho das camadas
	public static final int tamanhoEntrada = 10;
	public static final int tamanhoIntermediaria = 7;
	public static final int tamanhoSaida = 1;
	
	// Tamanho da populacao
	public static final int tamanhoPopulacao = 500;
	
	// Chance de Crossover
	public static final double chanceCross = 0.30;
	
	//Numero maximo de geracoes
	public static final int geracoes = 50000;
	
	// Chance de mutacao
	public static final double chanceMutacao = 0.05;
	
	// Parametros backpropagation
	public static final double learningRate = 0.25;
	public static final double momento = 0.1;
}
