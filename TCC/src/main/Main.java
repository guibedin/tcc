package main;

import guilherme.tcc.classes.Elemento;
import guilherme.tcc.classes.RedeNeural;
import static util.Constantes.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int geracoes;
		long inicioTreino;
		long fimTreino;
		String entradaMaxima = "entradasMaxima.txt";
		String saidaMaxima = "saidasMaxima.txt";
		RedeNeural rede = new RedeNeural();
		
		inicioTreino = System.currentTimeMillis(); // Tempo inicial de execucao
		geracoes = rede.treinar(entradaMaxima, saidaMaxima); // Treina rede neural
		fimTreino = System.currentTimeMillis(); // Tempo final de execucao
		
		rede.printarMatrizes(rede.getMaiorFitness());
		rede.desnormalizarDados(rede.getMaiorFitness(), 0);
		Elemento e = rede.getMaiorFitness();
		
		System.out.println("\nMatriz de Saida Desnormalizada:");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(e.dadosSaida[i][j] + ", ");	
			}			
		}
		
		System.out.println("\nNumero de Entradas: " + numeroEntradas);
		System.out.println("Tamanho Camada Intermediaria: " + tamanhoIntermediaria);
		System.out.println("Tamanho Populacao: " + tamanhoPopulacao);
		System.out.println("Chance de Mutacao: " + chanceMutacao + " = " + chanceMutacao*100 + "%");
		System.out.println("Chance de CrossOver: " + chanceCross+ " = " + chanceCross*100 + "%");
		System.out.println("Geracoes: " + geracoes);
		System.out.println("Erro Quadratico Medio final: " + e.getFitness());
		System.out.println("Tempo de execucao (s): " + (fimTreino - inicioTreino) / 1000);		
	}
}
