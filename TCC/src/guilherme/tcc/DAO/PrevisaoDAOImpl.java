package guilherme.tcc.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import guilherme.tcc.classes.Previsao;
import util.ConectarBanco;

public class PrevisaoDAOImpl implements PrevisaoDAO {

	@Override
	public List<Previsao> getAllPrevisao() {
		try{
			Connection con = ConectarBanco.getConnection();
			Statement stmt = con.createStatement();
			String query = "SELECT * FROM PREVISAO";
			ResultSet rs = stmt.executeQuery(query);
			List<Previsao> previsaoResult = new ArrayList<Previsao>();
			
			while(rs.next()){
				Previsao p = new Previsao();
				p.setId(rs.getInt(1));
				p.setData(rs.getDate(2));
				p.setPrecipitacao(rs.getFloat(3));
				p.setTemperatura_maxima(rs.getFloat(4));
				p.setTemperatura_minima(rs.getFloat(5));
				p.setCidade(rs.getString(7));
				p.setVelocidade_vento(rs.getFloat(8));
				
				previsaoResult.add(p);
			}
			stmt.close();
			return previsaoResult;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<Previsao> getPrevisaoByDate(Date data){
		try{
			Connection con = ConectarBanco.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM PREVISAO WHERE DATA = ?");
			stmt.setDate(1, data);
			ResultSet rs = stmt.executeQuery();
			List<Previsao> previsaoResult = new ArrayList<Previsao>();
			System.out.println(data.toString());
			
			while(rs.next()){
				Previsao p = new Previsao();
				p.setId(rs.getInt(1));
				p.setData(rs.getDate(2));
				p.setPrecipitacao(rs.getFloat(3));
				p.setTemperatura_maxima(rs.getFloat(4));
				p.setTemperatura_minima(rs.getFloat(5));
				p.setTemperatura_media(rs.getFloat(6));
				p.setCidade(rs.getString(7));
				//p.setVelocidade_vento(rs.getFloat(7));
				
				previsaoResult.add(p);
			}
			stmt.close();
			return previsaoResult;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Previsao> getPrevisaoByIntervalo(Date data_inicial, Date data_final){
		try{
			Connection con = ConectarBanco.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM Previsao WHERE DATA BETWEEN ? and ?");
			stmt.setDate(1, data_inicial);
			stmt.setDate(2, data_final);
			ResultSet rs = stmt.executeQuery();
			List<Previsao> previsaoResult = new ArrayList<Previsao>();
			
			while(rs.next()){
				Previsao p = new Previsao();
				p.setId(rs.getInt(1));
				p.setData(rs.getDate(2));
				p.setPrecipitacao(rs.getFloat(3));
				p.setTemperatura_maxima(rs.getFloat(4));
				p.setTemperatura_minima(rs.getFloat(5));
				p.setTemperatura_media(rs.getFloat(6));
				p.setCidade(rs.getString(7));
				//p.setVelocidade_vento(rs.getFloat(7));
				
				previsaoResult.add(p);
			}
			stmt.close();
			return previsaoResult;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void updatePrevisao(Previsao previsao) {
		try{
			Connection con = ConectarBanco.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO PREVISAO (DATA, PRECIPITACAO, TEMPERATURA_MAXIMA, TEMPERATURA_MINIMA, TEMPERATURA_MEDIA, CIDADE) "
				+ "VALUES (?,?,?,?,?,?)");
			
			stmt.setDate(1, new Date(2016-1900, 5, 14));
			stmt.setFloat(2, previsao.getPrecipitacao());
			stmt.setFloat(3, previsao.getTemperatura_maxima());
			stmt.setFloat(4, previsao.getTemperatura_minima());	
			stmt.setFloat(5, previsao.getTemperatura_media());
			stmt.setString(6, previsao.getCidade());
			//stmt.setFloat(6, previsao.getVelocidade_vento());
			
			stmt.execute();
			stmt.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void deletePrevisao(Previsao previsao) {
		// TODO Auto-generated method stub
		
	}

}
