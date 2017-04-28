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

	public double[][] pesosEntradaIntermediaria1; // Matriz de sinapses (pesos)
	public double[][] pesosIntermediaria1Intermediaria2; 
	public double[][] pesosIntermediaria2Saida; // Matriz de sinapses (pesos)
	
	public double[][] dadosEntrada; // Dados de entrada da rede
	public double[][] dadosSaida; // Dados de saida da rede
	
	public double[][] saidaTreino; // Dados usados para treinar rede (Comparar dadosSaida com essa matriz)
	
	public double[][] dadosEntradaIntermediaria1;
	public double[][] dadosEntradaIntermediaria1Transformada;
	public double[][] dadosIntermediaria1Intermediaria2;
	public double[][] dadosIntermediaria1Intermediaria2Transformada;	
	public double[][] dadosIntermediaria2Saida;
	public double[][] dadosIntermediaria2SaidaTransformada;
	
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
		
		// Matrizes de peso das sinapses (entrada -> intermediaria / intermediaria -> saida)
		this.pesosEntradaIntermediaria1 = new double[tamanhoEntrada][tamanhoIntermediaria1];
		this.pesosIntermediaria1Intermediaria2 = new double[tamanhoIntermediaria1][tamanhoIntermediaria2];
		this.pesosIntermediaria2Saida = new double[tamanhoIntermediaria2][tamanhoSaida];
		
		this.elementoID = elementoID;
		
		this.gerarArquivosDePesos();
		
		
		this.lerArquivos(entrada);
		this.lerArquivos("pesos1" + elementoID + ".txt");
		this.lerArquivos("pesos2" + elementoID + ".txt");
		this.lerArquivos("pesos3" + elementoID + ".txt");
		this.lerArquivos(saida);
	}
	
	// Calcula fitness do elemento
	public void calcularFitness(){
		double erro = 0;
		
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				//System.out.println("ERRO: " + dadosSaida[i][j] + " "  + saidaTreino[i][j]);
				// Erro percentual medio
				//erro += (Math.abs(saidaTreino[i][j] - dadosSaida[i][j]))/saidaTreino[i][j];
				
				// Erro quadratico medio
				erro += Math.pow((saidaTreino[i][j] - dadosSaida[i][j]), 2);
			}
		}
		
		this.fitness = erro/(numeroEntradas*tamanhoSaida);
		//System.out.println("FITNESS: " + this.fitness + " " + erro);
	}
	
	// Faz a mutacao do elemento
	public void mutacao(){
		Random r = new Random();
		
		// Muta matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria1; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosEntradaIntermediaria1[i][j] = this.gerarPeso();
					//System.out.println("mutou1");
				}
			}
		}
		// Muta matriz de peso 2 (Intermediaria1 -> Intermediaria2)
		for(int i = 0; i < tamanhoIntermediaria1; i++){
			for(int j = 0; j < tamanhoIntermediaria2; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosIntermediaria1Intermediaria2[i][j] = this.gerarPeso();
					//System.out.println("mutou2");
				}
			}
		}
				
		// Muta matriz de peso 3 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria2; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceMutacao){
					this.pesosIntermediaria2Saida[i][j] = this.gerarPeso();
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
		    	for(int j = 0; j < tamanhoIntermediaria1; j++){
			    	writer.print(gerarPeso() + " ");
			    }
		    	writer.println();
		    }
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
		} catch (IOException e) {
		   // do something
		}

	}

	// Gera um peso aleatorio
	public double gerarPeso(){
		Random r = new Random();
		return r.nextGaussian();
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
				
				if(arquivo.equals("entradas2.txt")){
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
					// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
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
				
				if(arquivo.equals("saidas2.txt")){
					// Preenche matriz de peso 2 - Camada Intermediaria -> Saida
					for(int i = 0; i < tamanhoSaida; i++){
						this.saidaTreino[contador][i] = Double.parseDouble(numeros[i]);
					}
					contador++;
				}		
			}
			
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
