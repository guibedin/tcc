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
		/*
		System.out.println("\nMatriz SaidaTreino:");
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(this.saidaTreino[i][j] + ", ");
			}
			System.out.println();
		}*/
	}
	
	// Calcula fitness do elemento
	public void calcularFitness(){
		double erro = 0;
		
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				//System.out.println("ERRO: " + dadosSaida[i][j] + " "  + saidaTreino[i][j]);
				// Erro percentual medio
				if(saidaTreino[i][j] != 0){
					erro += Math.abs((saidaTreino[i][j] - dadosSaida[i][j]))/saidaTreino[i][j];
				}
				
				// Erro quadratico medio
				//erro += Math.pow((saidaTreino[i][j] - dadosSaida[i][j]), 2);
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
		    //writer.println(gerarPeso() + " " + gerarPeso() + " " + gerarPeso());
		    //writer.println(gerarPeso() + " " + gerarPeso() + " " + gerarPeso());
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
				
				if(arquivo.equals("entradasMaxima.txt")){
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
				
				if(arquivo.equals("saidasMaxima.txt")){
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
