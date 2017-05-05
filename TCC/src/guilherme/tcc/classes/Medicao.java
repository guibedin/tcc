package guilherme.tcc.classes;

import java.sql.Date;

public class Medicao {

	private int id;
	private Date data;
	private double precipitacao;
	private double temperatura_maxima;
	private double temperatura_minima;
	private double temperatura_media;
	//private String cidade;
	//private float velocidade_vento;
	
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
