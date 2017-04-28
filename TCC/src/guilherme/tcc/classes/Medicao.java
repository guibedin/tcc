package guilherme.tcc.classes;

import java.sql.Date;

public class Medicao {

	private int id;
	private Date data;
	private float precipitacao;
	private float temperatura_maxima;
	private float temperatura_minima;
	private float temperatura_media;
	private String cidade;
	private float velocidade_vento;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getTemperatura_maxima() {
		return temperatura_maxima;
	}
	public void setTemperatura_maxima(float temperatura_maxima) {
		this.temperatura_maxima = temperatura_maxima;
	}
	public float getTemperatura_minima() {
		return temperatura_minima;
	}
	public void setTemperatura_minima(float temperatura_minima) {
		this.temperatura_minima = temperatura_minima;
	}
	public float getTemperatura_media() {
		return temperatura_media;
	}
	public void setTemperatura_media(float temperatura_media) {
		this.temperatura_media = temperatura_media;
	}
	public float getVelocidade_vento() {
		return velocidade_vento;
	}
	public void setVelocidade_vento(float velocidade_vento) {
		this.velocidade_vento = velocidade_vento;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public float getPrecipitacao() {
		return precipitacao;
	}
	public void setPrecipitacao(float precipitacao) {
		this.precipitacao = precipitacao;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

}
