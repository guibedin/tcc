package guilherme.tcc.services;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import guilherme.tcc.classes.Previsao;

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
		
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		try{
			java.util.Date d = formatter.parse(data);
			Date sqlDate = new Date(d.getTime());
			resultado = p.fazerPrevisao(sqlDate);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		System.out.println("Resultado retornado para cliente: " + resultado);
		
		return resultado;
	}

} 
