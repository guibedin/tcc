package main;

import static util.Constantes.chanceCross;
import static util.Constantes.chanceMutacao;
import static util.Constantes.erroQuadratico;
import static util.Constantes.genetico;
import static util.Constantes.normalizacaoDesvio;
import static util.Constantes.numeroEntradas;
import static util.Constantes.pesoGaussian;
import static util.Constantes.tamanhoIntermediaria1;
import static util.Constantes.tamanhoIntermediaria2;
import static util.Constantes.tamanhoPopulacao;
import static util.Constantes.tamanhoSaida;
import static util.Constantes.treino;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import guilherme.tcc.DAO.MedicaoDAO;
import guilherme.tcc.DAO.MedicaoDAOImpl;
import guilherme.tcc.classes.Elemento;
import guilherme.tcc.classes.Medicao;
import guilherme.tcc.classes.RedeNeural;

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
		
		Elemento eMaxima;
		Elemento eMinima;
		Elemento eMedia;
		
		// Gera arquivos pra todas as redes

		if(treino){
			redeMaxima.gerarArquivosDeTreino();
			redeMinima.gerarArquivosDeTreino();
			redeMedia.gerarArquivosDeTreino();
			redePrecipitacao.gerarArquivosDeTreino();
			
			if(!genetico){
				Elemento e;
				/*
				inicioTreino = System.currentTimeMillis();
				e = redeMaxima.treinar(entradaMaxima, saidaMaxima);
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
				redeMinima.printarMatrizes(e);
				System.out.println("Erro Quadratico Medio final: " + e.getFitness());
				System.out.println("\nTempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
				redeMinima.salvarArquivosDePesos("Minima", e);
				*/
				
				inicioTreino = System.currentTimeMillis();
				e = redeMedia.treinar(entradaMedia, saidaMedia);
				fimTreino = System.currentTimeMillis();
				redeMedia.desnormalizarDados(e, 2);
				System.out.println("Rede Treinada: Temperatura Media");
				System.out.println("Numero de Entradas: " + numeroEntradas);
				redeMedia.printarMatrizes(e);
				System.out.println("Erro Quadratico Medio final: " + e.getFitness());
				System.out.println("\nTempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);
				redeMedia.salvarArquivosDePesos("Media", e);
				/*
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
				*/
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
		}else{ // Executar testes
			MedicaoDAO medicaoDAO = new MedicaoDAOImpl();
			
			ArrayList<List<Medicao>> medicoes = new ArrayList<List<Medicao>>();
			
			
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2015-04-01"), Date.valueOf("2015-04-30")));
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2015-05-01"), Date.valueOf("2015-05-31")));
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2015-10-01"), Date.valueOf("2015-10-31")));
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2015-11-01"), Date.valueOf("2015-11-30")));
			
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2016-06-01"), Date.valueOf("2016-06-30")));
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2016-07-01"), Date.valueOf("2016-07-31")));
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2016-08-01"), Date.valueOf("2016-08-31")));
			medicoes.add(medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2016-09-01"), Date.valueOf("2016-09-30")));

			String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/ExecutarTestes/";
			DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
			ponto.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("00.0000", ponto);
			
			try {
				PrintWriter writerTeste = new PrintWriter(path + "Teste.csv", "UTF-8");
				
				for(List<Medicao> l : medicoes){
					for(Medicao m : l){
						eMaxima = redeMaxima.prepararExecucao(m.getData(), "Maxima");
						redeMaxima.normalizarDadosExecucao(eMaxima, "Maxima");
						redeMaxima.executar(eMaxima);
						redeMaxima.desnormalizarDados(eMaxima, 0);
						
						eMinima = redeMinima.prepararExecucao(m.getData(), "Minima");
						redeMinima.normalizarDadosExecucao(eMinima, "Minima");
						redeMinima.executar(eMinima);
						redeMinima.desnormalizarDados(eMinima, 1);
						
						eMedia = redeMedia.prepararExecucao(m.getData(), "Media");
						redeMedia.normalizarDadosExecucao(eMedia, "Media");
						redeMedia.executar(eMedia);
						redeMedia.desnormalizarDados(eMedia, 2);
						
						writerTeste.print(m.getData() + ", ");
						writerTeste.print(df.format(eMaxima.dadosSaida[0][0]) + ", ");
						writerTeste.print(df.format(eMinima.dadosSaida[0][0]) + ", ");
						writerTeste.println(df.format(eMedia.dadosSaida[0][0]));
					}
				}
				writerTeste.close();
			} catch (Exception ex) {
				
				ex.printStackTrace();
			}
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
		System.out.println("Tamanho Camada Intermediaria 1: " + tamanhoIntermediaria1);
		System.out.println("Tamanho Camada Intermediaria 2: " + tamanhoIntermediaria2);
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
