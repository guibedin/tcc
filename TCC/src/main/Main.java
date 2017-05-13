package main;

import guilherme.tcc.classes.Elemento;
import guilherme.tcc.classes.RedeNeural;
import static util.Constantes.*;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Main {
	
	private static int geracoes;
	private static long inicioTreino;
	private static long fimTreino;
	
	private static RedeNeural redeMaxima;
	private static RedeNeural redeMinima;
	private static RedeNeural redeMedia;
	private static RedeNeural redePrecipitacao;

	private static Elemento eMaxima;
	private static Elemento eMinima;
	private static Elemento eMedia;
	private static Elemento ePrecipitacao;
	
	public static void main(String[] args) {
		
		String entradaMaxima = "entradasMaxima.txt";
		String saidaMaxima = "saidasMaxima.txt";
		
		String entradaMinima = "entradasMinima.txt";
		String saidaMinima = "saidasMinima.txt";
		
		String entradaMedia = "entradasMedia.txt";
		String saidaMedia = "saidasMedia.txt";
		
		String entradaPrecipitacao = "entradasPrecipitacao.txt";
		String saidaPrecipitacao = "saidasPrecipitacao.txt";
		
		redeMaxima = new RedeNeural();
		redeMinima = new RedeNeural();
		redeMedia = new RedeNeural();
		redePrecipitacao = new RedeNeural();
		
		
		// Gera arquivos pra todas as redes

		
		redeMaxima.gerarArquivosDeTreino();
		redeMinima.gerarArquivosDeTreino();
		redeMedia.gerarArquivosDeTreino();
		redePrecipitacao.gerarArquivosDeTreino();
		
		if(!genetico){
			inicioTreino = System.currentTimeMillis();
			Elemento e = redeMaxima.treinar(entradaMaxima, saidaMaxima);
			fimTreino = System.currentTimeMillis();
			redeMaxima.desnormalizarDados(e, 0);
			System.out.println("Rede Treinada: Temperatura Maxima");
			System.out.println("Numero de Entradas: " + numeroEntradas);
			redeMaxima.printarMatrizes(e);
			System.out.println("Erro Quadratico Medio final: " + e.getFitness());
			System.out.println("\nTempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
			redeMaxima.salvarArquivosDePesos("Maxima", e);
			
			inicioTreino = System.currentTimeMillis();
			e = redeMinima.treinar(entradaMinima, saidaMinima);
			fimTreino = System.currentTimeMillis();
			redeMaxima.desnormalizarDados(e, 1);
			System.out.println("Rede Treinada: Temperatura Minima");
			System.out.println("Numero de Entradas: " + numeroEntradas);
			redeMaxima.printarMatrizes(e);
			System.out.println("Erro Quadratico Medio final: " + e.getFitness());
			System.out.println("\nTempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
			redeMaxima.salvarArquivosDePesos("Minima", e);
			
			inicioTreino = System.currentTimeMillis();
			e = redeMaxima.treinar(entradaMaxima, saidaMaxima);
			fimTreino = System.currentTimeMillis();
			redeMedia.desnormalizarDados(e, 2);
			System.out.println("Rede Treinada: Temperatura Media");
			System.out.println("Numero de Entradas: " + numeroEntradas);
			redeMaxima.printarMatrizes(e);
			System.out.println("Erro Quadratico Medio final: " + e.getFitness());
			System.out.println("\nTempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
			redeMaxima.salvarArquivosDePesos("Media", e);
			
			inicioTreino = System.currentTimeMillis();
			e = redePrecipitacao.treinar(entradaPrecipitacao, saidaPrecipitacao);
			fimTreino = System.currentTimeMillis();
			redeMaxima.desnormalizarDados(e, 3);
			System.out.println("Rede Treinada: Precipitacao");
			System.out.println("Numero de Entradas: " + numeroEntradas);
			redeMaxima.printarMatrizes(e);
			System.out.println("Erro Quadratico Medio final: " + e.getFitness());
			System.out.println("\nTempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
			redeMaxima.salvarArquivosDePesos("Precipitacao", e);
		}
		else{
			System.out.println("Trienando rede: Temperatura Maxima");
			inicioTreino = System.currentTimeMillis(); // Tempo inicial de execucao
			redeMaxima.treinar(entradaMaxima, saidaMaxima); // Treina rede neural Temp Maxima
			fimTreino = System.currentTimeMillis(); // Tempo final de execucao
			eMaxima = redeMaxima.getMaiorFitness();
			printarTreino(redeMaxima, "Temp Maxima", 0, eMaxima);
			redeMaxima.salvarArquivosDePesos("Maxima", eMaxima);
			
			
			System.out.println("Trienando rede: Temperatura Minima");
			inicioTreino = System.currentTimeMillis(); // Tempo inicial de execucao
			redeMinima.treinar(entradaMinima, saidaMinima); // Treina rede neural Temp Minima
			fimTreino = System.currentTimeMillis(); // Tempo final de execucao
			eMinima = redeMinima.getMaiorFitness();
			printarTreino(redeMinima, "Temp Minima", 1, eMinima);
			redeMinima.salvarArquivosDePesos("Minima", eMinima);
	
			
			System.out.println("Trienando rede: Temperatura Media");
			inicioTreino = System.currentTimeMillis(); // Tempo inicial de execucao
			redeMedia.treinar(entradaMedia, saidaMedia); // Treina rede neural Temp Media
			fimTreino = System.currentTimeMillis(); // Tempo final de execucao
			eMedia = redeMedia.getMaiorFitness();
			printarTreino(redeMedia, "Temp Media", 2, eMedia);
			redeMedia.salvarArquivosDePesos("Media", eMedia);
			
			
			/*
			System.out.println("Trienando rede: Precipitacao");
			inicioTreino = System.currentTimeMillis(); // Tempo inicial de execucao
			geracoes = redePrecipitacao.treinar(entradaPrecipitacao, saidaPrecipitacao); // Treina rede neural Precipitacao
			fimTreino = System.currentTimeMillis(); // Tempo final de execucao
			ePrecipitacao = redePrecipitacao.getMaiorFitness();
			printarTreino(redePrecipitacao, "Precipitacao", 3, ePrecipitacao);	
			redePrecipitacao.salvarArquivosDePesos("Precipitacao", ePrecipitacao);
			*/
		}
	}
	
	private static void printarTreino(RedeNeural rede, String treino, int temp, Elemento e){
		
		System.out.println();
		System.out.println("\nRede Treinada: " + treino);
		if(normalizacaoDesvio){
			System.out.println("Tipo de normalizacao: Media e Desvio Padrao");
		}else{
			System.out.println("Tipo de normalizacao: Minimo Maximo");
		}
		if(!pesoGaussian){
			System.out.println("Tipo de Peso: nextDouble");
		}else{
			System.out.println("Tipo de Peso: nextGaussian");
		}
		System.out.println("Numero de Entradas: " + numeroEntradas);
		System.out.println("Tamanho Camada Intermediaria: " + tamanhoIntermediaria);
		System.out.println("Tamanho Populacao: " + tamanhoPopulacao);
		System.out.println("Chance de Mutacao: " + chanceMutacao + " = " + chanceMutacao*100 + "%");
		System.out.println("Chance de CrossOver: " + chanceCross+ " = " + chanceCross*100 + "%");
		System.out.println("Geracoes: " + geracoes);
		if(!erroQuadratico){
			System.out.println("Erro Percentual Medio final: " + e.getFitness()*100 + "%");
		}else{
			System.out.println("Erro Quadratico Medio final: " + e.getFitness());
		}
		System.out.println("Tempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
		rede.printarMatrizes(e);
		rede.desnormalizarDados(e, temp);
		System.out.println("\nMatriz de Saida Desnormalizada:");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(e.dadosSaida[i][j] + ", ");	
			}
		}
		System.out.println();
	}
}
