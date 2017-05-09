package guilherme.tcc.classes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;

import static util.Constantes.*;

public class Elemento implements Comparable<Elemento> {

	public double[][] pesosEntradaIntermediaria; // Matriz de sinapses (pesos)
	public double[][] pesosIntermediariaSaida; // Matriz de sinapses (pesos)
	
	public double[][] dadosEntrada; // Dados de entrada da rede
	public double[][] dadosSaida; // Dados de saida da rede
	
	public double[][] saidaTreino; // Dados usados para treinar rede (Comparar dadosSaida com essa matriz)
	
	public double[][] dadosEntradaIntermediaria;
	public double[][] dadosEntradaIntermediariaTransformada;
	public double[][] dadosIntermediariaSaida;
	public double[][] dadosIntermediariaSaidaTransformada;
	
	private double fitness;
	private int elementoID;
	
	// Inicia cada elemento da populacao
	// Elementos sao compostos pelos dados de entrada, matrizes de peso e dados de saida
	public Elemento(int elementoID, String entrada, String saida){
	
		// Matriz de dados de entrada
		this.dadosEntrada = new double[numeroEntradas][tamanhoEntrada];
		// Matriz de dados de saida
		this.dadosSaida = new double[numeroEntradas][tamanhoSaida];
		// Matriz de dados de saida de treino
		this.saidaTreino = new double[numeroEntradas][tamanhoSaida];
		
		// Matrizes de saida de uma camada -> entrada da proxima
		this.dadosEntradaIntermediaria = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.dadosIntermediariaSaida = new double[tamanhoIntermediaria][tamanhoSaida];
		
		
		// Matrizes de peso das sinapses (entrada -> intermediaria / intermediaria -> saida)
		this.pesosEntradaIntermediaria = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.pesosIntermediariaSaida = new double[tamanhoIntermediaria][tamanhoSaida];
		
		this.elementoID = elementoID;
		
		this.gerarArquivosDePesos();
		
		
		this.lerArquivos(entrada);
		this.lerArquivos("pesos1" + elementoID + ".txt");
		this.lerArquivos("pesos2" + elementoID + ".txt");
		this.lerArquivos(saida);
	}
	
	// Inicia elemento de execucao, diferente da populacao
	// Popula somente as matrizes de pesos, matriz de entrada e' populada antes
	public Elemento(String rede){
		
		// Matriz de dados de entrada
		this.dadosEntrada = new double[numeroEntradasExecucao][tamanhoEntrada];
		// Matriz de dados de saida
		this.dadosSaida = new double[numeroEntradasExecucao][tamanhoSaida];
		
		// Matrizes de saida de uma camada -> entrada da proxima
		this.dadosEntradaIntermediaria = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.dadosIntermediariaSaida = new double[tamanhoIntermediaria][tamanhoSaida];
		
		
		// Matrizes de peso das sinapses (entrada -> intermediaria / intermediaria -> saida)
		this.pesosEntradaIntermediaria = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.pesosIntermediariaSaida = new double[tamanhoIntermediaria][tamanhoSaida];
		
		this.elementoID = 0;
		String linha;
		
		// Abre arquivo passado como parametro
		try{
			int contador = 0;
			
			InputStream entradaIntermediariaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "pesosEntradaIntermediaria" + rede + ".txt");
			InputStreamReader entradaIntermediariaISR = new InputStreamReader(entradaIntermediariaIS);
			BufferedReader bufferEntradaIntermediaria = new BufferedReader(entradaIntermediariaISR);
			
			// Abre arquivo passado como parametro
			InputStream intermediariaSaidaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "pesosIntermediariaSaida" + rede + ".txt");
			InputStreamReader intermediariaSaidaISR = new InputStreamReader(intermediariaSaidaIS);
			BufferedReader bufferIntermediariaSaida = new BufferedReader(intermediariaSaidaISR);
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferEntradaIntermediaria.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
				for(int i = 0; i < tamanhoIntermediaria; i++){
					this.pesosEntradaIntermediaria[contador][i] = Double.parseDouble(numeros[i]);
				}
				contador++;
			}
			
			bufferEntradaIntermediaria.close();
			contador = 0;
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferIntermediariaSaida.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
				for(int i = 0; i < tamanhoSaida; i++){
					this.pesosIntermediariaSaida[contador][i] = Double.parseDouble(numeros[i]);
				}
				contador++;
			}
			bufferIntermediariaSaida.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	// Calcula fitness do elemento
	public void calcularFitness(){
		double erro = 0;
		
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				//System.out.println("ERRO: " + dadosSaida[i][j] + " "  + saidaTreino[i][j]);
				// Erro percentual medio
				if(!erroQuadratico){
					if(saidaTreino[i][j] != 0){
						erro += Math.abs(Math.abs((saidaTreino[i][j] - dadosSaida[i][j]))/saidaTreino[i][j]);
					}
				}
				else{
					// Erro quadratico medio
					erro += Math.pow((saidaTreino[i][j] - dadosSaida[i][j]), 2);
				}
			}
		}
		
		this.fitness = (erro/(numeroEntradas*tamanhoSaida));
		//System.out.println("FITNESS: " + this.fitness);
	}
	
	// Faz a mutacao do elemento
	public void mutacao(){
		Random r = new Random();
		
		// Muta matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosEntradaIntermediaria[i][j] = this.gerarPeso();
					//System.out.println("mutou1");
				}
			}
		}
		
		// Muta matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosIntermediariaSaida[i][j] = this.gerarPeso();
					//System.out.println("mutou2");
				}
			}
		}
	}
	
	// Cria arquivos de pesos
	public void gerarArquivosDePesos(){
		
		String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Treinamento/";
		
		try{
		    PrintWriter writer = new PrintWriter(path + "pesos1" + this.elementoID + ".txt", "UTF-8");
		    for(int i = 0; i < tamanhoEntrada; i++){
		    	for(int j = 0; j < tamanhoIntermediaria; j++){
			    	writer.print(gerarPeso() + " ");
			    }
		    	writer.println();
		    }
		    writer.close();
		    
		    writer = new PrintWriter(path + "pesos2" + this.elementoID + ".txt", "UTF-8");
		    for(int i = 0; i < tamanhoIntermediaria; i++){
		    	for(int j = 0; j < tamanhoSaida; j++){
			    	writer.print(gerarPeso() + " ");
			    }
		    	writer.println();
		    }
		    writer.close();
		} catch (IOException e) {
		   e.printStackTrace();
		}

	}

	// Gera um peso aleatorio
	public double gerarPeso(){
		Random r = new Random();
		
		if(tipoPeso){
			return r.nextDouble() * 2 - 1;
		}else{
			return r.nextGaussian();
		}
	}
	
	
	public void lerArquivos(String arquivo){
		String linha;
		int contador = 0;

		try{
			// Abre arquivo passado como parametro
			InputStream entradaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Treinamento/" + arquivo);
			InputStreamReader entradaISR = new InputStreamReader(entradaIS);
			BufferedReader buffer = new BufferedReader(entradaISR);
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = buffer.readLine()) != null && contador < numeroEntradas){
				String[] numeros = linha.split("\\s+");
				
				//If's para determinar o que fazer com cada linha do arquivo, de acordo com o seu nome
				
				//if(arquivo.equals("entradasMaxima.txt")){
				if(arquivo.contains("entradas")){
					// Preenche matriz com dados de entrada
					for(int i = 0; i < tamanhoEntrada; i++){
						this.dadosEntrada[contador][i] = Double.parseDouble(numeros[i]);						
					}
					contador++;
				}
				
				if(arquivo.equals("pesos1" + this.elementoID + ".txt")){
					// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
					for(int i = 0; i < tamanhoIntermediaria; i++){
						this.pesosEntradaIntermediaria[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}
				
				if(arquivo.equals("pesos2" + this.elementoID + ".txt")){
					// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
					for(int i = 0; i < tamanhoSaida; i++){
						this.pesosIntermediariaSaida[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}
				
				//if(arquivo.equals("saidasMaxima.txt")){
				if(arquivo.contains("saidas")){	
					// Preenche matriz com dados de Saida de Treino
					for(int i = 0; i < tamanhoSaida; i++){
						this.saidaTreino[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}		
			}
			buffer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public int getElementoID(){
		return this.elementoID;
	}
	
	public void setElementoID(int elementoID){
		this.elementoID = elementoID;
	}
	
	public double getFitness(){
		return this.fitness;
	}

	@Override
	public int compareTo(Elemento e) {
		return (this.elementoID - e.elementoID);
	}
	
	
}
