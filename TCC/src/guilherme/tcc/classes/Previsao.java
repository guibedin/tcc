package guilherme.tcc.classes;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import guilherme.tcc.DAO.PrevisaoDAO;
import guilherme.tcc.DAO.PrevisaoDAOImpl;


public class Previsao {

	private int id;
	private Date data;
	private double precipitacao;
	private double temperatura_maxima;
	private double temperatura_minima;
	private double temperatura_media;
	//private String cidade;
	//private float velocidade_vento;

	
	public String fazerPrevisao(Date data){
		PrevisaoDAO previsaoDAO = new PrevisaoDAOImpl();
		
		Previsao previsao = new Previsao();
		previsao.setData(data);
		previsao.setTemperatura_maxima(5);
		previsao.setTemperatura_minima(3);
		previsao.setTemperatura_media(4);
		previsao.setPrecipitacao(0.3);
		
		return previsaoToJSONString();
		
		//List<Previsao> p = previsaoDAO.getPrevisaoByDate(data);
		//return previsaoToJSONString(p.get(0));
	}
	
	public String previsaoToJSONString(){
		return String.format("{data:%s,  precipitacao:%s, temperatura_maxima:%s, temperatura_minima:%s, temperatura_media:%s}",
				this.getData(), this.getPrecipitacao(), this.getTemperatura_maxima(), this.getTemperatura_minima(), this.getTemperatura_media());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTemperatura_maxima() {
		return temperatura_maxima;
	}
	public void setTemperatura_maxima(double temperatura_maxima) {
		this.temperatura_maxima = temperatura_maxima;
	}
	public double getTemperatura_minima() {
		return temperatura_minima;
	}
	public void setTemperatura_minima(double temperatura_minima) {
		this.temperatura_minima = temperatura_minima;
	}
	public double getTemperatura_media() {
		return temperatura_media;
	}
	public void setTemperatura_media(double temperatura_media) {
		this.temperatura_media = temperatura_media;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public double getPrecipitacao() {
		return precipitacao;
	}
	public void setPrecipitacao(double precipitacao) {
		this.precipitacao = precipitacao;
	}
}
