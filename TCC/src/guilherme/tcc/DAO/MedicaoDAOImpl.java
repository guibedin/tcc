package guilherme.tcc.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import guilherme.tcc.classes.Medicao;
import util.ConectarBanco;

public class MedicaoDAOImpl implements MedicaoDAO {

	public MedicaoDAOImpl(){
		
	}
	
	@Override
	public List<Medicao> getAllMedicao() {
		try{
			Connection con = ConectarBanco.getConnection();
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM MEDICAO";
			ResultSet rs = stmt.executeQuery(query);
			List<Medicao> medicaoResult = new ArrayList<Medicao>();
			
			while(rs.next()){
				Medicao m = new Medicao();
				m.setId(rs.getInt(1));
				m.setData(rs.getDate(2));
				m.setPrecipitacao(rs.getFloat(3));
				m.setTemperatura_maxima(rs.getFloat(4));
				m.setTemperatura_minima(rs.getFloat(5));
				m.setTemperatura_media(rs.getFloat(6));
				//m.setCidade(rs.getString(7));
				//m.setVelocidade_vento(rs.getFloat(8));
				
				medicaoResult.add(m);
			}
			stmt.close();
			con.close();
			return medicaoResult;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("all")
	@Override
	public List<Medicao> getMedicaoByDate(Date data){
		try{
			Connection con = ConectarBanco.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM MEDICAO WHERE DATA = ?");
			stmt.setDate(1, data);
			ResultSet rs = stmt.executeQuery();
			List<Medicao> medicaoResult = new ArrayList<Medicao>();
			
			while(rs.next()){
				Medicao m = new Medicao();
				m.setId(rs.getInt(1));
				m.setData(rs.getDate(2));
				m.setPrecipitacao(rs.getFloat(3));
				m.setTemperatura_maxima(rs.getFloat(4));
				m.setTemperatura_minima(rs.getFloat(5));
				m.setTemperatura_media(rs.getFloat(6));
				//m.setCidade(rs.getString(6));
				//m.setVelocidade_vento(rs.getFloat(7));
				
				medicaoResult.add(m);
			}
			stmt.close();
			con.close();
			return medicaoResult;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Medicao> getMedicaoByIntervalo(Date data_inicial, Date data_final){
		try{
			Connection con = ConectarBanco.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM MEDICAO WHERE DATA BETWEEN ? and ?");
			stmt.setDate(1, data_inicial);
			stmt.setDate(2, data_final);
			ResultSet rs = stmt.executeQuery();
			List<Medicao> medicaoResult = new ArrayList<Medicao>();
			
			while(rs.next()){
				Medicao m = new Medicao();
				m.setId(rs.getInt(1));
				m.setData(rs.getDate(2));
				m.setPrecipitacao(rs.getFloat(3));
				m.setTemperatura_maxima(rs.getFloat(4));
				m.setTemperatura_minima(rs.getFloat(5));
				m.setTemperatura_media(rs.getFloat(6));
				//m.setCidade(rs.getString(6));
				//m.setVelocidade_vento(rs.getFloat(7));
				
				medicaoResult.add(m);
			}
			stmt.close();
			con.close();
			return medicaoResult;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void updateMedicao(Medicao medicao) {
		try{
			Connection con = ConectarBanco.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO MEDICAO (DATA, PRECIPITACAO, TEMPERATURA_MAXIMA, TEMPERATURA_MINIMA, TEMERATURA_MEDIA) "
				+ "VALUES (?,?,?,?,?)");
			
			//stmt.setDate(1, new Date(2016-1900, 5, 14));
			stmt.setDate(1, medicao.getData());
			stmt.setDouble(2, medicao.getPrecipitacao());
			stmt.setDouble(3, medicao.getTemperatura_maxima());
			stmt.setDouble(4, medicao.getTemperatura_minima());
			stmt.setDouble(5, medicao.getTemperatura_media());
			//stmt.setString(5, medicao.getCidade());
			//stmt.setFloat(6, medicao.getVelocidade_vento());
			
			stmt.execute();
			stmt.close();
			con.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMedicao(Medicao medicao) {
		// TODO Auto-generated method stub
		
	}

	
	
}
