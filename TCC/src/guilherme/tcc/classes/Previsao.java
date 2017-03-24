package guilherme.tcc.classes;

import java.sql.Date;
import java.util.List;

import guilherme.tcc.DAO.PrevisaoDAO;
import guilherme.tcc.DAO.PrevisaoDAOImpl;

public class Previsao {

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
	
	public String fazerPrevisao(Date data){
		PrevisaoDAO previsaoDAO = new PrevisaoDAOImpl();
		/*
		Previsao previsao = new Previsao();
		medicao.setData(new Date(2016-1900, 6, 13));
		medicao.setTemperatura_maxima(5);
		medicao.setTemperatura_minima(3);
		medicao.setPrecipitacao(precipitacao);
		medicaoDAO.updateMedicao(medicao);
		List<Medicao> medicoes = medicaoDAO.getAllMedicao();
		System.out.println(medicoes.get(0).getTemperatura_maxima());
		return this.previsaoToJSONString(medicoes.get(0));
		*/
		System.out.println(data.toString());
		List<Previsao> p = previsaoDAO.getPrevisaoByDate(data);
		return previsaoToJSONString(p.get(0));
	}
	
	private String previsaoToJSONString(Previsao previsao){
		return String.format("{data:%s,  precipitacao:%s, temperatura_maxima:%s, temperatura_minima:%s, temperatura_media:%s, cidade:%s}",
				previsao.getData(), previsao.getPrecipitacao(), previsao.getTemperatura_maxima(), previsao.getTemperatura_minima(), previsao.getTemperatura_media(), previsao.getCidade());
	}
}
