package guilherme.tcc.services;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import guilherme.tcc.classes.Previsao;
import guilherme.tcc.classes.Elemento;
import guilherme.tcc.classes.RedeNeural;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /previsao
@Path("/previsao")
public class ServicePrevisao {
	
	public ServicePrevisao() {
		
	}
  
	// Metodo chamado quando cliente pede previsao
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPrevisao(@QueryParam("data") String data) {
		System.out.println("Requisicao recebida: " + data);
		Previsao p = new Previsao();
		String resultado;
		
		RedeNeural redeMaxima = new RedeNeural();
		RedeNeural redeMinima = new RedeNeural();
		RedeNeural redeMedia = new RedeNeural();
		RedeNeural redePrecipitacao = new RedeNeural();
	
		Elemento eMaxima;
		Elemento eMinima;
		Elemento eMedia;
		Elemento ePrecipitacao;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		DecimalFormatSymbols ponto = new DecimalFormatSymbols(Locale.US);
		ponto.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("00.0000", ponto);
		try{
			java.util.Date d = formatter.parse(data);
			Date sqlDate = new Date(d.getTime());
			
			eMaxima = redeMaxima.prepararExecucao(sqlDate, "Maxima");
			redeMaxima.normalizarDadosExecucao(eMaxima, "Maxima");
			redeMaxima.executar(eMaxima);
			redeMaxima.desnormalizarDados(eMaxima, 0);
			redeMaxima.printarMatrizes(eMaxima);
			
			
			eMinima = redeMinima.prepararExecucao(sqlDate, "Minima");
			redeMinima.normalizarDadosExecucao(eMinima, "Minima");
			redeMinima.executar(eMinima);
			redeMinima.desnormalizarDados(eMinima, 1);
			redeMinima.printarMatrizes(eMinima);
			
			eMedia = redeMedia.prepararExecucao(sqlDate, "Media");
			redeMedia.normalizarDadosExecucao(eMedia, "Media");
			redeMedia.executar(eMedia);
			redeMedia.desnormalizarDados(eMedia, 2);
			redeMedia.printarMatrizes(eMedia);
			
			/*
			ePrecipitacao = redePrecipitacao.prepararExecucao(sqlDate, "Precipitacao");
			redePrecipitacao.normalizarDadosExecucao(ePrecipitacao, "Precipitacao");
			redePrecipitacao.executar(ePrecipitacao);
			redePrecipitacao.desnormalizarDados(ePrecipitacao, 3);
			redePrecipitacao.printarMatrizes(ePrecipitacao);
			*/
			
			p.setData(sqlDate);
			p.setTemperatura_maxima(eMaxima.dadosSaida[0][0]);
			p.setTemperatura_minima(eMinima.dadosSaida[0][0]);
			p.setTemperatura_media(eMedia.dadosSaida[0][0]);
			//p.setPrecipitacao(ePrecipitacao.dadosSaida[0][0]);
			
			resultado = p.previsaoToJSONString();
			
			//resultado = p.fazerPrevisao(sqlDate);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		System.out.println("Resultado retornado para cliente: " + resultado);
		
		return resultado;
	}

} 
