package guilherme.tcc.DAO;

import java.sql.Date;
import java.util.List;

import guilherme.tcc.classes.Previsao;

public interface PrevisaoDAO {
	public List<Previsao> getAllPrevisao();
	public List<Previsao> getPrevisaoByDate(Date data);
	public List<Previsao> getPrevisaoByIntervalo(Date data_inicial, Date data_final);
	public void updatePrevisao(Previsao previsao);
	public void deletePrevisao(Previsao previsao);
}
