package guilherme.tcc.classes;

import java.io.PrintWriter;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.python.antlr.ast.Slice.Slice___init___exposer;

import com.mysql.jdbc.log.Slf4JLogger;

import guilherme.tcc.DAO.MedicaoDAO;
import guilherme.tcc.DAO.MedicaoDAOImpl;
import static util.Constantes.*;

/**
 * 
 * @author guilherme
 * @see Camada
 * @see Neuronio
 * 
 * Classe que representa a Rede Neural Artificial (RNA).
 * 
 * A RNA utilizada neste projeto possui tres camadas: entrada,
 * intermediaria e saida.
 * 	
 */
public class RedeNeural {
	
	
	
	private Camada entrada; // Camada de entrada
	private Camada intermediaria; // Camada intermediaria
	private Camada saida; /// Camada de saida
	
	private List<Elemento> populacao;
	private Elemento elementoExecucao;
	
	private int maiorFitness; // ID do elemento de maior fitness
	private int segundoMaiorFitness; // ID do elemento com segundo maior fitness
	private int menorFitness;
	private int segundoMenorFitness;
	
	// Matrizes para auxiliar a criacao dos arquivos de entrada de treino
	private double maximasEntrada[][] = new double[tamanhoEntrada][numeroEntradas];
	private double maximasEntradaInvertida[][] = new double[numeroEntradas][tamanhoEntrada];
	
	private double minimasEntrada[][] = new double[tamanhoEntrada][numeroEntradas];
	private double minimasEntradaInvertida[][] = new double[numeroEntradas][tamanhoEntrada];
	
	private double mediasEntrada[][] = new double[tamanhoEntrada][numeroEntradas];
	private double mediasEntradaInvertida[][] = new double[numeroEntradas][tamanhoEntrada];
	
	private double precipitacaoEntrada[][] = new double[tamanhoEntrada][numeroEntradas];
	private double precipitacaoEntradaInvertida[][] = new double[numeroEntradas][tamanhoEntrada];
	
	// Matrizes para auxiliar a criacao do arquivo de saida de treino
	private double maximasSaida[][] = new double[tamanhoSaida][numeroEntradas];
	private double maximasSaidaInvertida[][] = new double[numeroEntradas][tamanhoSaida];
	
	private double minimasSaida[][] = new double[tamanhoSaida][numeroEntradas];
	private double minimasSaidaInvertida[][] = new double[numeroEntradas][tamanhoSaida];
	
	private double mediasSaida[][] = new double[tamanhoSaida][numeroEntradas];
	private double mediasSaidaInvertida[][] = new double[numeroEntradas][tamanhoSaida];
	
	private double precipitacaoSaida[][] = new double[tamanhoEntrada][numeroEntradas];
	private double precipitacaoSaidaInvertida[][] = new double[numeroEntradas][tamanhoEntrada];
	
	// Maximos e Minimos usados para normalizacao dos dados
	private double minEntradaMax;
	private double maxEntradaMax;
	private double minEntradaMin;
	private double maxEntradaMin;
	private double minEntradaMed;
	private double maxEntradaMed;
	private double minEntradaPrec;
	private double maxEntradaPrec;
	
	private double minSaidaMax;
	private double maxSaidaMax;
	private double minSaidaMin;
	private double maxSaidaMin;
	private double minSaidaMed;
	private double maxSaidaMed;
	private double minSaidaPrec;
	private double maxSaidaPrec;
	
	public RedeNeural(){
		// Instanciacao das camadas, passando o tamanho delas (numero de neuronios)
		this.entrada = new Camada(tamanhoEntrada);
		this.intermediaria = new Camada(tamanhoIntermediaria);
		this.saida = new Camada(tamanhoSaida);
		
		// Inicializacao da populacao
		this.populacao = new ArrayList<Elemento>();
		
		this.maiorFitness = 0;
		this.segundoMaiorFitness = 0;
		this.menorFitness = 0;
		this.segundoMenorFitness = 0;	
	}
	
	/*
	 * Calcula o fitness da populacao atual
	 * Possivel solucao: 
	 * 1 - Calcular Fitness - calcular distancia do valor gerado pela rede em relacao ao valor de teste, quanto menor a distancia, maior seu 'fitness';
	 * 2 - Reproduzir - seleciona 2 pais (2 matrizes de peso) de uma forma que os com maior 'fitness' tenham mais chances de serem escolhidos;
	 * 3 - Crossover - criar um novo elemento  pegando os pesos de entrada de um pai e os pesos de saida do outro pai;
	 * 4 - Mutacao - definir chance de mutacao (ex: cada peso tem 1% de chance de ser mudado) e aplicar essa mutacao
	 *
	 * Metodo para treinar a rede neural
	 * Executar a rede para cada elemento
	 * Calcular fitness de cada elemento
	 * Executar Crossover baseado em fitness
	 * Executar Mutacao
	 * Repetir ate erro satisfatorio
	 * 
	 * Retorna numero de rodadas de treino
	 */
	public int treinar(String entrada, String saida){
		double fit; // Valor do fitness
		int iteracoes = 0;
		
		this.gerarPopulacaoInicial(entrada, saida);
		
		//while(true){
		while(iteracoes < geracoes){
			/*
			this.maiorFitness = 0;
			this.segundoMaiorFitness = 0;
			*/
			this.menorFitness = 0;
			this.segundoMenorFitness = 0;
			 
			
			//System.out.println("RODADA DE TREINO: " + (iteracoes+1));
			// Primeira parte - executar a rede para todo elemento, definindo os 2 com maior fitness
			for(Elemento e : this.populacao){
				//System.out.println("ELEMENTO: " + e.getElementoID());
				this.executar(e); // Executa rede calculando saidas do elemento
				e.calcularFitness(); // Calucla fitness do elemento
				fit = e.getFitness(); // Pega fitness do elemento
				
				// If's para determinar os 2 elementos com maior fitness - serao usados para crossover
				if(fit < this.populacao.get(this.maiorFitness).getFitness()){
					//System.out.println("MAIOR " + e.getElementoID() + " " + this.maiorFitness);
					this.segundoMaiorFitness = this.maiorFitness;
					this.maiorFitness = e.getElementoID();
					//System.out.println("Geracao: " + (iteracoes+1) + "\nFitness: " + this.populacao.get(this.maiorFitness).getFitness());
				}
				else{
					if(fit < this.populacao.get(this.segundoMaiorFitness).getFitness()){
						//System.out.println("SEGUNDO MAIOR " + e.getElementoID());
						this.segundoMaiorFitness = e.getElementoID();
					}
				}
				// If's para determinar os elementos com menor fitness
				if(fit > this.populacao.get(this.menorFitness).getFitness()){
					//System.out.println("MENOR " + e.getElementoID() + " " + this.menorFitness);
					this.segundoMenorFitness = this.menorFitness;
					this.menorFitness = e.getElementoID();
				}else{
					if(fit > this.populacao.get(this.segundoMenorFitness).getFitness()){
						//System.out.println("SEGUNDO MENOR " + e.getElementoID());
						this.segundoMenorFitness = e.getElementoID();
					}
				}
				
			}
			
			/*
			System.out.println("Maior Fitness: " + populacao.get(maiorFitness).getFitness() + " = Elemento: " + populacao.get(maiorFitness).getElementoID());
			System.out.println("Segundo Maior Fitness: " + populacao.get(segundoMaiorFitness).getFitness() + " = Elemento: " + populacao.get(segundoMaiorFitness).getElementoID());
			System.out.println("Menor Fitness: " + populacao.get(menorFitness).getFitness() + " = Elemento: " + populacao.get(menorFitness).getElementoID());
			System.out.println("Segundo Menor Fitness: " + populacao.get(segundoMenorFitness).getFitness()+ " = Elemento: " + populacao.get(segundoMenorFitness).getElementoID());
			*/
			
			iteracoes++;
			// Condicao de parada
			/*
			if(populacao.get(maiorFitness).getFitness() < 0.00018){
				System.out.println("\n 0.00018 RODADA DE TREINO: " + (iteracoes+1));
				//return;
			}
			*/
			if(this.populacao.get(this.maiorFitness).getFitness() < 0.0001){
				System.out.println("\n 0.0001 RODADA DE TREINO: " + (iteracoes+1));
				//printarMatrizes(populacao.get(maiorFitness));
				return iteracoes+1;
			}
			
			
			this.crossover();
			
			// Mutacao - nao afeta os elemenots de maior fitness
			for(Elemento e : this.populacao){
				if(e.getElementoID() !=this.maiorFitness && e.getElementoID() != this.segundoMaiorFitness){
						//&& e.getElementoID() != menorFitness && e.getElementoID() != segundoMenorFitness){
					e.mutacao();
				}
			}
			
		}
		return iteracoes;
	}
	
	public void crossover(){
		Random r = new Random();
		
		// Crossover - Troca matrizes de peso Entrada->Intermediaria e Intermediaria->Saida entre os 2 elementos de maior fitness
		// e coloca nos 2 elementos de menor fitness
		Elemento e1 = populacao.get(maiorFitness);
		Elemento e2 = populacao.get(segundoMaiorFitness);
		Elemento e3 = populacao.get(menorFitness);
		Elemento e4 = populacao.get(segundoMenorFitness);
		
		// Matrizes de peso auxiliares para fazer o crossover
		// 1 e 2 -> maiorFitness
		// 3 e 4 -> segundoMaiorFitness
		double[][] auxiliar1 = new double[tamanhoEntrada][tamanhoIntermediaria];
		double[][] auxiliar2 = new double[tamanhoIntermediaria][tamanhoSaida];		
		double[][] auxiliar3 = new double[tamanhoEntrada][tamanhoIntermediaria];
		double[][] auxiliar4 = new double[tamanhoIntermediaria][tamanhoSaida];
		
		// Copia (Entrada -> Intermediaria) de maiorFitness e segundoMaiorFitness para menorFitness e segundoMenorFitness
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				e3.pesosEntradaIntermediaria[i][j] = e1.pesosEntradaIntermediaria[i][j];
				e4.pesosEntradaIntermediaria[i][j] = e2.pesosEntradaIntermediaria[i][j];
				auxiliar1[i][j] = e1.pesosEntradaIntermediaria[i][j];
				auxiliar3[i][j] = e2.pesosEntradaIntermediaria[i][j];
			}
		}
		
		
		// Copia (Intermediaria -> Saida) de maiorFitness e segundoMaiorFitness para menorFitness e segundoMenorFitness
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				e3.pesosIntermediariaSaida[i][j] = e1.pesosIntermediariaSaida[i][j];
				e4.pesosIntermediariaSaida[i][j] = e2.pesosIntermediariaSaida[i][j];
				auxiliar2[i][j] = e1.pesosIntermediariaSaida[i][j];
				auxiliar4[i][j] = e2.pesosIntermediariaSaida[i][j];
			}
		}
		
		
		// Crossover matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				if(r.nextDouble() < chanceCross){
					e3.pesosEntradaIntermediaria[i][j] = auxiliar1[i][j];
					//e3.pesosEntradaIntermediaria[i][j] = e3.pesosEntradaIntermediaria[i][j] + r.nextDouble();
				}
			}
		}
		
		
		// Crossover matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceCross){
					e3.pesosIntermediariaSaida[i][j] = auxiliar2[i][j];
					//e3.pesosIntermediariaSaida[i][j] = e3.pesosIntermediariaSaida[i][j] + r.nextDouble();
				}
			}
		}
		
		// Crossover matriz de peso 1 (Entrada -> Intermediaria)
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				if(r.nextDouble() < chanceCross){
					e4.pesosEntradaIntermediaria[i][j] = auxiliar3[i][j];
					//e4.pesosEntradaIntermediaria[i][j] = e4.pesosEntradaIntermediaria[i][j] + r.nextDouble();
				}
			}
		}
		
		
		// Crossover matriz de peso 2 (Intermediaria -> Saida)
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(r.nextDouble() < chanceCross){
					e4.pesosIntermediariaSaida[i][j] = auxiliar4[i][j];
					//e4.pesosIntermediariaSaida[i][j] = e4.pesosIntermediariaSaida[i][j] + r.nextDouble();
				}
			}
		}
		
		//e1.pesosIntermediariaSaida = e2.pesosIntermediariaSaida;
		//e2.pesosEntradaIntermediaria = e1.pesosEntradaIntermediaria;
		
		
		Collections.sort(this.populacao);
	}
	
	// Gera populacao inicial - valores aleatorios para os pesos
	// A populacao eh formada por um conjunto de matrizes de peso
	// Cada "membro" da populacao eh um conjunto de matrizes de peso
	public void gerarPopulacaoInicial(String entrada, String saida){
		for(int i = 0; i < tamanhoPopulacao; i++){
			this.populacao.add(new Elemento(i, entrada, saida));
		}
	}
	
	// Gera arquivos de entrada e saida que serao usados para treinar a rede
	// Os arquivos de entrada contem medidas de um mesmo dia nos ultimos 10 anos
	// O arquivo de saida contem medidas deste dia no ano seguinte
	// Ex: Arquivo de Entrada: Medidas de 01 de Janeiro ate 31 de Marco por 10 anos (Arquivo com 10 linhas (anos) e 91 colunas (dias))
	//     Arquivo de Saida: Medidas de 01 de Janeiro de 2010 (Uma linha do arquivo, 1 saida)
	public void gerarArquivosDeTreino(){
		MedicaoDAO medicaoDAO = new MedicaoDAOImpl();
		
		// Inicialmente seleciona 3 meses inteiros (~90 linhas)
		// Anos 2004 ate 2013 - Entradas de treino
		List<Medicao> m2004 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2004-01-01"), Date.valueOf("2004-03-31"));
		List<Medicao> m2005 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2005-01-01"), Date.valueOf("2005-03-31"));
		List<Medicao> m2006 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2006-01-01"), Date.valueOf("2006-03-31"));
		List<Medicao> m2007 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2007-01-01"), Date.valueOf("2007-03-31"));
		List<Medicao> m2008 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2008-01-01"), Date.valueOf("2008-03-31"));
		List<Medicao> m2009 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2009-01-01"), Date.valueOf("2009-03-31"));
		List<Medicao> m2010 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2010-01-01"), Date.valueOf("2010-03-31"));
		List<Medicao> m2011 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2011-01-01"), Date.valueOf("2011-03-31"));
		List<Medicao> m2012 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2012-01-01"), Date.valueOf("2012-03-31"));
		List<Medicao> m2013 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2013-01-01"), Date.valueOf("2013-03-31"));
		
		// Ano 2014 - Saida de treino
		List<Medicao> m2014 = medicaoDAO.getMedicaoByIntervalo(Date.valueOf("2014-01-01"), Date.valueOf("2014-03-31"));
		
		this.gerarEntradaAuxiliar(m2004, 0);
		this.gerarEntradaAuxiliar(m2005, 1);
		this.gerarEntradaAuxiliar(m2006, 2);
		this.gerarEntradaAuxiliar(m2007, 3);
		this.gerarEntradaAuxiliar(m2008, 4);
		this.gerarEntradaAuxiliar(m2009, 5);
		this.gerarEntradaAuxiliar(m2010, 6);
		this.gerarEntradaAuxiliar(m2011, 7);
		this.gerarEntradaAuxiliar(m2012, 8);
		this.gerarEntradaAuxiliar(m2013, 9);
		
		this.gerarSaidaAuxiliar(m2014, 0);
		
		maximasEntradaInvertida = this.transformaMatriz(maximasEntrada);
		minimasEntradaInvertida = this.transformaMatriz(minimasEntrada);
		mediasEntradaInvertida = this.transformaMatriz(mediasEntrada);
		precipitacaoEntradaInvertida = this.transformaMatriz(precipitacaoEntrada);
		
		maximasSaidaInvertida = this.transformaMatriz(maximasSaida);
		minimasSaidaInvertida = this.transformaMatriz(minimasSaida);
		mediasSaidaInvertida = this.transformaMatriz(mediasSaida);
		precipitacaoSaidaInvertida = this.transformaMatriz(precipitacaoSaida);
		
		this.normalizarDados();
		
		// Escreve arquivos de entrada e saida
		try{
			
			DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
			ponto.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("00.0000", ponto);
			
			String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Treinamento/";
			PrintWriter writerMaxima = new PrintWriter(path + "entradasMaxima.txt", "UTF-8");
			PrintWriter writerMinima = new PrintWriter(path + "entradasMinima.txt", "UTF-8");
			PrintWriter writerMedia = new PrintWriter(path + "entradasMedia.txt", "UTF-8");
			PrintWriter writerPrecipitacao = new PrintWriter(path + "entradasPrecipitacao.txt", "UTF-8");
			
			PrintWriter writerMaximaSaida = new PrintWriter(path + "saidasMaxima.txt", "UTF-8");
			PrintWriter writerMinimaSaida = new PrintWriter(path + "saidasMinima.txt", "UTF-8");
			PrintWriter writerMediaSaida = new PrintWriter(path + "saidasMedia.txt", "UTF-8");
			PrintWriter writerPrecipitacaoSaida = new PrintWriter(path + "saidasPrecipitacao.txt", "UTF-8");
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoEntrada; j++){
					//System.out.print(String.format("%.2f", maximasInvertida[i][j]));
					//System.out.print(" ");
				    writerMaxima.print(df.format(maximasEntradaInvertida[i][j]) + " ");
				    writerMinima.print(df.format(minimasEntradaInvertida[i][j]) + " ");
				    writerMedia.print(df.format(mediasEntradaInvertida[i][j]) + " ");
				    writerPrecipitacao.print(df.format(precipitacaoEntradaInvertida[i][j]) + " ");
				}
				writerMaxima.println();
				writerMinima.println();
				writerMedia.println();
				writerPrecipitacao.println();
			}
			writerMaxima.close();
			writerMinima.close();
			writerMedia.close();
			writerPrecipitacao.close();
			
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					//System.out.print(String.format("%.2f", maximasInvertida[i][j]));
					//System.out.print(" ");
					writerMaximaSaida.print(df.format(maximasSaidaInvertida[i][j]) + " ");
					writerMinimaSaida.print(df.format(minimasSaidaInvertida[i][j]) + " ");
				    writerMediaSaida.print(df.format(mediasSaidaInvertida[i][j]) + " ");
				    writerPrecipitacaoSaida.print(df.format(precipitacaoSaidaInvertida[i][j]) + " ");
				}
				writerMaximaSaida.println();
				writerMinimaSaida.println();
				writerMediaSaida.println();
				writerPrecipitacaoSaida.println();
			}
			
			// Fecha todos os writers
			
			writerMaximaSaida.close();
			writerMinimaSaida.close();
			writerMediaSaida.close();
			writerPrecipitacaoSaida.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void normalizarDados(){
		
		minEntradaMax = 100;
		maxEntradaMax = 0;
		minEntradaMin = 100;
		maxEntradaMin = 0;
		minEntradaMed = 100;
		maxEntradaMed = 0;
		minEntradaPrec = 100;
		maxEntradaPrec = 0;
		
		minSaidaMax = 100;
		maxSaidaMax = 0;
		minSaidaMin = 100;
		maxSaidaMin = 0;
		minSaidaMed = 100;
		maxSaidaMed = 0;
		minSaidaPrec = 100;
		maxSaidaPrec = 0;
		
		// Loop para determinar os valores minimos e maximos do dataset
		// Esses valores sao usados na normalizacao
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoEntrada; j++){
				if(maximasEntradaInvertida[i][j] > maxEntradaMax){
					maxEntradaMax = maximasEntradaInvertida[i][j];
				}
				if(maximasEntradaInvertida[i][j] < minEntradaMax){
					minEntradaMax = maximasEntradaInvertida[i][j];
				}
				
				if(minimasEntradaInvertida[i][j] > maxEntradaMin){
					maxEntradaMin = minimasEntradaInvertida[i][j];
				}
				if(minimasEntradaInvertida[i][j] < minEntradaMin){
					minEntradaMin = minimasEntradaInvertida[i][j];
				}
				
				if(mediasEntradaInvertida[i][j] > maxEntradaMed){
					maxEntradaMed = mediasEntradaInvertida[i][j];
				}
				if(mediasEntradaInvertida[i][j] < minEntradaMed){
					minEntradaMed = mediasEntradaInvertida[i][j];
				}
				if(precipitacaoEntradaInvertida[i][j] > maxEntradaPrec){
					maxEntradaPrec = precipitacaoEntradaInvertida[i][j];
				}
				if(precipitacaoEntradaInvertida[i][j] < minEntradaPrec){
					minEntradaPrec = precipitacaoEntradaInvertida[i][j];
				}
				
			}
			for(int j = 0; j < tamanhoSaida; j++){
				if(maximasSaidaInvertida[i][j] > maxSaidaMax){
					maxSaidaMax = maximasSaidaInvertida[i][j];
				}
				if(maximasSaidaInvertida[i][j] < minSaidaMax){
					minSaidaMax = maximasSaidaInvertida[i][j];
				}
				
				if(minimasSaidaInvertida[i][j] > maxSaidaMin){
					maxSaidaMin = minimasSaidaInvertida[i][j];
				}
				if(minimasSaidaInvertida[i][j] < minSaidaMin){
					minSaidaMin = minimasSaidaInvertida[i][j];
				}
				
				if(mediasSaidaInvertida[i][j] > maxSaidaMed){
					maxSaidaMed = mediasSaidaInvertida[i][j];
				}
				if(mediasSaidaInvertida[i][j] < minSaidaMed){
					minSaidaMed = mediasSaidaInvertida[i][j];
				}
				if(precipitacaoSaidaInvertida[i][j] > maxSaidaPrec){
					maxSaidaPrec = precipitacaoSaidaInvertida[i][j];
				}
				if(precipitacaoSaidaInvertida[i][j] < minSaidaPrec){
					minSaidaPrec = precipitacaoSaidaInvertida[i][j];
				}
			}
		}
		//System.out.println(minEntradaMax + "\n" + maxEntradaMax);
		
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoEntrada; j++){
				maximasEntradaInvertida[i][j] = (maximasEntradaInvertida[i][j] - minEntradaMax)/(maxEntradaMax - minEntradaMax);
				minimasEntradaInvertida[i][j] = (minimasEntradaInvertida[i][j] - minEntradaMin)/(maxEntradaMin - minEntradaMin);
				mediasEntradaInvertida[i][j] = (mediasEntradaInvertida[i][j] - minEntradaMed)/(maxEntradaMed - minEntradaMed);
				precipitacaoEntradaInvertida[i][j] = (precipitacaoEntradaInvertida[i][j] - minEntradaPrec)/(maxEntradaPrec - minEntradaPrec);
			}
		}
		
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				maximasSaidaInvertida[i][j] = (maximasSaidaInvertida[i][j] - minSaidaMax)/(maxSaidaMax - minSaidaMax);
				minimasSaidaInvertida[i][j] = (minimasSaidaInvertida[i][j] - minSaidaMin)/(maxSaidaMin - minSaidaMin);
				mediasSaidaInvertida[i][j] = (mediasSaidaInvertida[i][j] - minSaidaMed)/(maxSaidaMed - minSaidaMed);
				precipitacaoSaidaInvertida[i][j] = (precipitacaoSaidaInvertida[i][j] - minSaidaPrec)/(maxSaidaPrec - minSaidaPrec);
			}
		}
		
	}
	
	
	public void salvarArquivosDePesos(String rede, Elemento e){
		// Escreve arquivos de entrada e saida
		try{
			DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
			ponto.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("00.0000", ponto);
			
			String path = "/home/guilherme/Desktop/TCC Real/Semestre 2/ArquivosNN/Executar/";
			PrintWriter writerEntradaIntermediaria = new PrintWriter(path + "pesosEntradaIntermediaria" + rede + ".txt", "UTF-8");
			PrintWriter writerIntermediariaSaida = new PrintWriter(path + "pesosIntermediariaSaida" + rede + ".txt", "UTF-8");
			
			for(int i = 0; i < tamanhoEntrada; i++){
				for(int j = 0; j < tamanhoIntermediaria; j++){
					writerEntradaIntermediaria.print(e.pesosEntradaIntermediaria[i][j] + " ");
				}
				writerEntradaIntermediaria.println();
			}
			writerEntradaIntermediaria.close();
			
			for(int i = 0; i < tamanhoIntermediaria; i++){
				for(int j = 0; j < tamanhoSaida; j++){
					writerIntermediariaSaida.print(e.pesosIntermediariaSaida[i][j] + " ");
				}
				writerIntermediariaSaida.println();
			}
			writerIntermediariaSaida.close();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	// Desnomarliza os dados para serem apresentados na saida
	// temp define qual tipo de temperatura
	// 0 = Max, 1 = Min, 2 = Med, 3 = precipitacao
	public void desnormalizarDados(Elemento e, int temp){
		
		for(int i = 0; i < numeroEntradas; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				if(temp == 0){
					e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMax - minSaidaMax) + minSaidaMax;
				}
				if(temp == 1){
					e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMin - minSaidaMin) + minSaidaMin;
				}
				if(temp == 2){
					e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaMed - minSaidaMed) + minSaidaMed;
				}
				if(temp == 3){
					e.dadosSaida[i][j] = e.dadosSaida[i][j] *(maxSaidaPrec - minSaidaPrec) + minSaidaPrec;
				}
			}
		}
	}
	
	public void gerarEntradaAuxiliar(List<Medicao> medicoes, int i){
		int j = 0;
		for(Medicao m : medicoes){
			if(j < numeroEntradas){
				maximasEntrada[i][j] = m.getTemperatura_maxima();
				minimasEntrada[i][j] = m.getTemperatura_minima();
				mediasEntrada[i][j] = m.getTemperatura_media();
				precipitacaoEntrada[i][j] = m.getPrecipitacao();
			}
			j++;
		}
	}
	
	public void gerarSaidaAuxiliar(List<Medicao> medicoes, int i){
		int j = 0;
		for(Medicao m : medicoes){
			if(j < numeroEntradas){
				maximasSaida[i][j] = m.getTemperatura_maxima();
				minimasSaida[i][j] = m.getTemperatura_minima();
				mediasSaida[i][j] = m.getTemperatura_media();
				precipitacaoSaida[i][j] = m.getPrecipitacao();
			}
			j++;
		}
	}
	
	// Metodo chamado pelo ServicePrevisao quando um request e' recebido
	// Este metodo prepara a rede neural para ser executada, nao treinada
	// Forma matriz de entrada -- selects no banco -- parte dificil
	// Le arquivos de pesos salvos
	// Retorna o elemento que sera usado na execucao
	public Elemento prepararExecucao(Date data, String rede){
		
		// Instancia elemento de execucao, nao utiliza populacao
		// Gera elemento com matrizes de peso
		elementoExecucao = new Elemento(rede);
		
		// A partir da data, gerar matriz de entrada 1x10 (1 linha, 10 dias)
		MedicaoDAO medicaoDAO = new MedicaoDAOImpl();
		
		
		int anoPrevisao; // Ano da Data a ser prevista
		int anoMedicao; // Ano da Data medida
		Calendar calPrevisao = Calendar.getInstance(); // Calendario Data a ser prevista
		Calendar calMedicao = Calendar.getInstance(); // Calenadrio Data medida
		Date dataMedicao;
		
		calPrevisao.setTime(data); 
		anoPrevisao = calPrevisao.get(Calendar.YEAR); 
		
		anoMedicao = anoPrevisao - 10; // Ultimos 10 anos
		calMedicao.setTime(data);
		calMedicao.set(Calendar.YEAR, anoMedicao); // Calendario do primeiro ano a ser medido (Ano a ser previsto - 11)
		
		ArrayList<List<Medicao>> medicoes = new ArrayList<List<Medicao>>();
		int contadorPosicao = 0; // Conta posicao da lista
		int contadorAno = 0; // Menor ano medido ate o momento
		
		for(int i = anoMedicao; i < anoPrevisao; i++, contadorPosicao++){
			List<Medicao> m = new ArrayList<Medicao>();
			
			calMedicao.set(Calendar.YEAR, i);
			dataMedicao = new Date(calMedicao.getTimeInMillis());
			//System.out.println(dataMedicao.toString());
			
			m = medicaoDAO.getMedicaoByDate(dataMedicao);
			
			while(m.isEmpty()){
				contadorAno++;
				calMedicao.set(Calendar.YEAR, (anoMedicao - contadorAno));
				dataMedicao = new Date(calMedicao.getTimeInMillis());
				//System.out.println(dataMedicao.toString() + "CONTADOR ANO: " + contadorAno);
				
				m = medicaoDAO.getMedicaoByDate(dataMedicao);
			}
			
			medicoes.add(m);
			
			//System.out.println("Ano: " + i + "\n" + medicoes.get(contadorPosicao).get(0).getTemperatura_maxima());
		}
		
		int i = 0;
		for(List<Medicao> m : medicoes){
			if(rede.equals("Maxima")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getTemperatura_maxima();
			}
			if(rede.equals("Minima")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getTemperatura_minima();
			}
			if(rede.equals("Media")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getTemperatura_media();
			}
			if(rede.equals("Precipitacao")){
				elementoExecucao.dadosEntrada[0][i] = m.get(0).getPrecipitacao();
			}
			i++;
		}
		
		return elementoExecucao;
		//return null;
	}
	
	// Executa a rede neural - feedforward
	public void executar(Elemento e){
		
		int i = 0;
		int j = 0;
		
		// CALCULAR UMA LINHA DE ENTRADA DE CADA VEZ
		for(i = 0; i < numeroEntradasExecucao; i++){
			//System.out.println("INICIO DA ENTRADA " + (i+1));
			// Calcular sinapses Entrada -> Intermediaria
			// Colocar o valor de entrada nos neuronios de entrada
			// Calcular a saida usando a funcao de ativacao + pesos
			
			// Iterar por cada neuronio da camada de Entrada, setando seu vetor de entrada e calculando sua saida			
			for (Neuronio n : entrada.getNeuronios()){
				n.setEntrada(e.dadosEntrada[i][j], i);
				n.calcularValor(e.dadosEntrada[i].length, false);
				e.dadosEntradaIntermediaria[j] = n.calcularSaida(e.pesosEntradaIntermediaria[j]);
				/*
				System.out.println("\nSaidas N" + (j+1));
				for(int k = 0; k < tamanhoIntermediaria; k++){
					System.out.print(e.dadosEntradaIntermediaria[j][k] + " ");
				}
				System.out.println();
				*/
				j++;
			}			
			j = 0;
			
			e.dadosEntradaIntermediariaTransformada = transformaMatriz(e.dadosEntradaIntermediaria);
			// Calcular sinapses Intermediaria -> Saida
			// O valor de entrada da camada Intermediaria eh composto pelas saidas da camada de Entrada
			// Calcular a saida usando a funcao de ativacao + pesos
			
			// Iterar por cada neuronio da camada Intermediaria, setando seu vetor de entrada e calculando suas saidas
			for(Neuronio n : intermediaria.getNeuronios()){
				n.setEntrada(e.dadosEntradaIntermediariaTransformada[j]);
				n.calcularValor(e.dadosEntradaIntermediariaTransformada[j].length, false);
				//System.out.print("\nValor N" + (j+3) + "\n" + n.getValor());
				e.dadosIntermediariaSaida[j] = n.calcularSaida(e.pesosIntermediariaSaida[j]);
				/*
				System.out.println("\nSaidas N" + (j+3));
				for(int k = 0; k < tamanhoSaida; k++){
					System.out.print(e.dadosIntermediariaSaida[j][k] + " ");
				}
				System.out.println();*/
				j++;
			}
			j = 0;
			
			
			e.dadosIntermediariaSaidaTransformada = transformaMatriz(e.dadosIntermediariaSaida);
			// Calcular a Saida final
			// Colocar o valor de entrada nos neuronios de entrada
			// Calcular a saida usando a funcao de ativacao + pesos
					
			// Iterar por cada neuronio da camada Saida, setando seu vetor de entrada e calculando suas saidas
			for(Neuronio n : saida.getNeuronios()){
				n.setEntrada(e.dadosIntermediariaSaidaTransformada[j]);
				n.calcularValor(e.dadosIntermediariaSaidaTransformada[j].length, true);
				e.dadosSaida[i][j] = n.getValor();
				/*
				System.out.println("\nSaidas N" + (j+6));
				for(int k = 0; k < tamanhoSaida; k++){
					System.out.print(e.dadosSaida[i][j] + " ");
				}
				System.out.println();*/
				j++;
			}
			j = 0;
			//System.out.println("\nFIM DA ENTRADA " + (i+1) + "\n");
		}
	}
	
	// Transforma colunas em linhas
	public double[][] transformaMatriz(double[][] m1){
		
		
		double[][] m2 = new double[m1[0].length][m1.length];
		
		//System.out.println("Inversao, x = " + m1[0][0] + " y = " + m1[1][0]);
		for(int i = 0; i < m1[0].length; i++){
			for(int j = 0; j < m1.length; j++){
				m2[i][j] = m1[j][i];
		//		System.out.print(m2[i][j] + " ");
			}
		//	System.out.println();
		}
		return m2;
	}
	
	
	public void printarMatrizes(Elemento e){
		System.out.println("ELEMENTO " + e.getElementoID());
		
		
		// Printa Matriz de Entrada
		System.out.println("Matriz de Entrada: ");
		for(int i = 0; i < numeroEntradasExecucao; i++){
			for(int j = 0; j < tamanhoEntrada; j++){				
				System.out.print(e.dadosEntrada[i][j] + ", ");
			}
			System.out.println();
		}
		
		if(treino){
			// Printa Matriz de Saida Desejada
			System.out.println("\nMatriz de Saida Desejada (Treino): ");
			for(int i = 0; i < numeroEntradas; i++){
				for(int j = 0; j < tamanhoSaida; j++){				
					System.out.print(e.saidaTreino[i][j] + ", ");
				}
				System.out.println();
			}
		}
		
		// Printa Matriz de Peso 1
		System.out.println("\nMatriz de Peso 1 (Entrada -> Intermediaria):");
		for(int i = 0; i < tamanhoEntrada; i++){
			for(int j = 0; j < tamanhoIntermediaria; j++){
				System.out.print(e.pesosEntradaIntermediaria[i][j] + ", ");				
			}
			System.out.println();
		}
		
		// Printa Matriz de Peso 2
		System.out.println("\nMatriz de Peso 2 (Intermediaria -> Saida):");
		for(int i = 0; i < tamanhoIntermediaria; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(e.pesosIntermediariaSaida[i][j] + ", ");
			}
			System.out.println();
		}
		
		
		// Printa Matriz da Saida
		System.out.println("\nMatriz de Saida:");
		for(int i = 0; i < numeroEntradasExecucao; i++){
			for(int j = 0; j < tamanhoSaida; j++){
				System.out.print(e.dadosSaida[i][j] + ", ");	
			}			
		}
		System.out.println();
	}
	
	public void iniciarSinapses(Sinapse[][] sin, int linhas, int colunas){
		for(int i = 0; i < linhas; i++){
			for(int j = 0; j < colunas; j++){
				//sin[i][j] = new Sinapse();
			}
		}
	}
	
	public Camada getEntrada() {
		return entrada;
	}

	public void setEntrada(Camada entrada) {
		this.entrada = entrada;
	}

	public Camada getIntermediaria() {
		return intermediaria;
	}

	public void setIntermediaria(Camada intermediaria) {
		this.intermediaria = intermediaria;
	}

	public Camada getSaida() {
		return saida;
	}

	public void setSaida(Camada saida) {
		this.saida = saida;
	}

	public Elemento getMaiorFitness(){
		return this.populacao.get(maiorFitness);
	}
}
