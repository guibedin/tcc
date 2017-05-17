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

	 // Matriz de sinapses (pesos)
	public double[][] pesosEntradaIntermediaria1;
	public double[][] pesosIntermediaria1Intermediaria2; 
	public double[][] pesosIntermediaria2Saida; 
	
	public double[][] pesosEntradaIntermediaria1Delta;
	public double[][] pesosIntermediaria1Intermediaria2Delta;
	public double[][] pesosIntermediaria2SaidaDelta;
	
	public double[][] pesosEntradaIntermediaria1DeltaAntigo;
	public double[][] pesosIntermediaria1Intermediaria2DeltaAntigo;
	public double[][] pesosIntermediaria2SaidaDeltaAntigo;
	
	// Bias da rede
	public double biasIntermediaria; 
	public double biasSaida;
	
	public double[][] dadosEntrada; // Dados de entrada da rede
	public double[][] dadosSaida; // Dados de saida da rede
	
	public double[][] saidaTreino; // Dados usados para treinar rede (Comparar dadosSaida com essa matriz)
	
	public double[][] dadosEntradaIntermediaria1;
	public double[][] dadosEntradaIntermediaria1Transformada;
	
	//public double[][] dadosEntradaIntermediariaBP;
	public double[] dadosEntradaIntermediaria1BP;
	public double[][] dadosEntradaIntermediaria1BPTransformada;
	
	public double[] dadosIntermediaria1Intermediaria2BP;
	public double[][] dadosIntermediaria1Intermediaria2BPTransformada;	
	
	public double[][] dadosIntermediaria1Intermediaria2;
	public double[][] dadosIntermediaria1Intermediaria2Transformada;	
	
	public double[][] dadosIntermediaria2Saida;
	public double[][] dadosIntermediaria2SaidaTransformada;
	
	//public double[][] dadosIntermediariaSaidaBP;
	public double[] dadosIntermediaria2SaidaBP;
	public double[][] dadosIntermediaria2SaidaBPTransformada;
	
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
		this.dadosEntradaIntermediaria1 = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.dadosIntermediaria1Intermediaria2 = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.dadosIntermediaria2Saida = new double[tamanhoIntermediaria2][tamanhoSaida];
		
		// Matrizes Backpropagation
		//this.dadosEntradaIntermediariaBP = new double[tamanhoEntrada][tamanhoIntermediaria];
		this.dadosEntradaIntermediaria1BP = new double[tamanhoEntrada];
		this.dadosEntradaIntermediaria1BPTransformada = new double[tamanhoIntermediaria1][tamanhoEntrada];
		
		this.dadosIntermediaria1Intermediaria2BP = new double[tamanhoIntermediaria1];
		this.dadosIntermediaria1Intermediaria2BPTransformada = new double[tamanhoIntermediaria2][tamanhoIntermediaria1];
		
		//this.dadosIntermediariaSaidaBP = new double[tamanhoIntermediaria][tamanhoSaida];
		this.dadosIntermediaria2SaidaBP = new double[tamanhoIntermediaria2];
		this.dadosIntermediaria2SaidaBPTransformada = new double[tamanhoSaida][tamanhoIntermediaria2];
		
		// Matrizes de peso das sinapses (entrada -> intermediaria / intermediaria -> saida)
		this.pesosEntradaIntermediaria1 = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.pesosIntermediaria1Intermediaria2 = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.pesosIntermediaria2Saida = new double[tamanhoIntermediaria2][tamanhoSaida];

		this.pesosEntradaIntermediaria1Delta = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.pesosIntermediaria1Intermediaria2Delta = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.pesosIntermediaria2SaidaDelta = new double[tamanhoIntermediaria2][tamanhoSaida];
		
		this.pesosEntradaIntermediaria1DeltaAntigo = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.pesosIntermediaria1Intermediaria2DeltaAntigo = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.pesosIntermediaria2SaidaDeltaAntigo = new double[tamanhoIntermediaria2][tamanhoSaida];
		
		this.elementoID = elementoID;
		
		this.gerarArquivosDePesos();
		
		this.lerArquivos(entrada);
		this.lerArquivos("pesos1" + elementoID + ".txt");
		this.lerArquivos("pesos2" + elementoID + ".txt");
		this.lerArquivos("pesos3" + elementoID + ".txt");
		//this.lerArquivos("biasIntermediaria" + elementoID + ".txt");
		//this.lerArquivos("biasSaida" + elementoID + ".txt");
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
		this.dadosEntradaIntermediaria1 = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.dadosIntermediaria1Intermediaria2 = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.dadosIntermediaria2Saida = new double[tamanhoIntermediaria2][tamanhoSaida];
		
		
		// Matrizes de peso das sinapses (entrada -> intermediaria / intermediaria -> saida)
		this.pesosEntradaIntermediaria1 = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.pesosIntermediaria1Intermediaria2 = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.pesosIntermediaria2Saida = new double[tamanhoIntermediaria2][tamanhoSaida];
		
		this.elementoID = 0;
		String linha;
		
		// Abre arquivo passado como parametro
		try{
			int contador = 0;
			
			InputStream entradaIntermediariaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "pesosEntradaIntermediaria1" + rede + ".txt");
			InputStreamReader entradaIntermediariaISR = new InputStreamReader(entradaIntermediariaIS);
			BufferedReader bufferEntradaIntermediaria = new BufferedReader(entradaIntermediariaISR);
			
			
			InputStream intermediaria1Intermediaria2IS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "pesosIntermediaria1Intermediaria2" + rede + ".txt");
			InputStreamReader intermediaria1Intermediaria2ISR = new InputStreamReader(intermediaria1Intermediaria2IS);
			BufferedReader bufferIntermediaria1Intermediaria2 = new BufferedReader(intermediaria1Intermediaria2ISR);
			
			// Abre arquivo passado como parametro
			InputStream intermediaria2SaidaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "pesosIntermediaria2Saida" + rede + ".txt");
			InputStreamReader intermediaria2SaidaISR = new InputStreamReader(intermediaria2SaidaIS);
			BufferedReader bufferIntermediaria2Saida = new BufferedReader(intermediaria2SaidaISR);
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferEntradaIntermediaria.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
				for(int i = 0; i < tamanhoIntermediaria1; i++){
					this.pesosEntradaIntermediaria1[contador][i] = Double.parseDouble(numeros[i]);
				}
				contador++;
			}
			
			bufferEntradaIntermediaria.close();
			contador = 0;
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferIntermediaria1Intermediaria2.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
				for(int i = 0; i < tamanhoIntermediaria2; i++){
					this.pesosIntermediaria1Intermediaria2[contador][i] = Double.parseDouble(numeros[i]);
				}
				contador++;
			}
			
			bufferIntermediaria1Intermediaria2.close();
			contador = 0;
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferIntermediaria2Saida.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
				for(int i = 0; i < tamanhoSaida; i++){
					this.pesosIntermediaria2Saida[contador][i] = Double.parseDouble(numeros[i]);
				}
				contador++;
			}
			bufferIntermediaria2Saida.close();
			
			// Arquivos de BIAS
			InputStream biasIntermediariaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "biasIntermediaria" + rede + ".txt");
			InputStreamReader biasIntermediariaISR = new InputStreamReader(biasIntermediariaIS);
			BufferedReader bufferBiasIntermediaria = new BufferedReader(biasIntermediariaISR);
			
			// Abre arquivo passado como parametro
			InputStream biasSaidaIS = new FileInputStream("/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/"
					+ "biasSaida" + rede + ".txt");
			InputStreamReader biasSaidaISR = new InputStreamReader(biasSaidaIS);
			BufferedReader bufferBiasSaida = new BufferedReader(biasSaidaISR);
			
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferBiasIntermediaria.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche vetor Bias Intermediaria
				this.biasIntermediaria = Double.parseDouble(numeros[0]);				
			}
			bufferBiasIntermediaria.close();
			
			// Comeca leitura do arquivo, linha por linha
			while ((linha = bufferBiasSaida.readLine()) != null){
				String[] numeros = linha.split("\\s+");
				// Preenche vetor Bias Saida
				this.biasSaida = Double.parseDouble(numeros[0]);			
			}
			bufferBiasSaida.close();
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
			for(int j = 0; j < tamanhoIntermediaria1; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosEntradaIntermediaria1[i][j] -=  this.gerarPeso();
					//System.out.println("mutou1");
				}
			}
		}
		
		// Muta bias Intermediaria
		if(r.nextDouble() < chanceMutacao){
			this.biasIntermediaria -= this.gerarPeso();
		}
		
		// Muta matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoIntermediaria1; i++){
			for(int j = 0; j < tamanhoIntermediaria2; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosIntermediaria1Intermediaria2[i][j] -=  this.gerarPeso();
					//System.out.println("mutou2");
				}
			}
		}
		
		// Muta matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria2; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosIntermediaria2Saida[i][j] -=  this.gerarPeso();
					//System.out.println("mutou3");
				}
			}
		}
		
		// Muta bias Saida
		if(r.nextDouble() < chanceMutacao){
			this.biasSaida -=  this.gerarPeso();
		}
	}
	
	// Cria arquivos de pesos e bias
	public void gerarArquivosDePesos(){
		
		String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Treinamento/";
		
		try{
		    PrintWriter writer = new PrintWriter(path + "pesos1" + this.elementoID + ".txt", "UTF-8");
		    for(int i = 0; i < tamanhoEntrada; i++){
		    	for(int j = 0; j < tamanhoIntermediaria1; j++){
			    	writer.print(gerarPeso() + " ");
			    }
		    	writer.println();
		    }
		    writer.close();
		    
		    // Bias Intermediaria
		    writer = new PrintWriter(path + "biasIntermediaria" + this.elementoID + ".txt", "UTF-8");
		    writer.print(gerarPeso());
		    writer.close();
		    
		    
		    writer = new PrintWriter(path + "pesos2" + this.elementoID + ".txt", "UTF-8");
		    for(int i = 0; i < tamanhoIntermediaria1; i++){
		    	for(int j = 0; j < tamanhoIntermediaria2; j++){
			    	writer.print(gerarPeso() + " ");
			    }
		    	writer.println();
		    }
		    writer.close();
		    
		    
		    writer = new PrintWriter(path + "pesos3" + this.elementoID + ".txt", "UTF-8");
		    for(int i = 0; i < tamanhoIntermediaria2; i++){
		    	for(int j = 0; j < tamanhoSaida; j++){
			    	writer.print(gerarPeso() + " ");
			    }
		    	writer.println();
		    }
		    writer.close();
		    
		    // Bias Saida
		    writer = new PrintWriter(path + "biasSaida" + this.elementoID + ".txt", "UTF-8");
		    writer.print(gerarPeso());
		    writer.close();
		    
		} catch (IOException e) {
		   e.printStackTrace();
		}

	}

	// Gera um peso aleatorio
	public double gerarPeso(){
		Random r = new Random();
		
		if(!pesoGaussian){
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
					for(int i = 0; i < tamanhoIntermediaria1; i++){
						this.pesosEntradaIntermediaria1[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}
				
				if(arquivo.equals("pesos2" + this.elementoID + ".txt")){
					// Preenche matriz de peso 1 - Camada de entrada -> Intermediaria
					for(int i = 0; i < tamanhoIntermediaria2; i++){
						this.pesosIntermediaria1Intermediaria2[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}
				
				if(arquivo.equals("pesos3" + this.elementoID + ".txt")){
					// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
					for(int i = 0; i < tamanhoSaida; i++){
						this.pesosIntermediaria2Saida[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}
				
				if(arquivo.equals("biasIntermediaria" + this.elementoID + ".txt")){
					this.biasIntermediaria = Double.parseDouble(numeros[0]);					
				}
				
				if(arquivo.equals("biasSaida" + this.elementoID + ".txt")){
					this.biasSaida = Double.parseDouble(numeros[0]);					
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
