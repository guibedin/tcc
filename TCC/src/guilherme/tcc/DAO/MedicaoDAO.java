package guilherme.tcc.DAO;

import java.sql.Date;
import java.util.List;

import guilherme.tcc.classes.Medicao;

public interface MedicaoDAO {
	public List<Medicao> getAllMedicao();
	public List<Medicao> getMedicaoByDate(Date data);
	public List<Medicao> getMedicaoByIntervalo(Date data_inicial, Date data_final);
	public void updateMedicao(Medicao medicao);
	public void deleteMedicao(Medicao medicao);
}
