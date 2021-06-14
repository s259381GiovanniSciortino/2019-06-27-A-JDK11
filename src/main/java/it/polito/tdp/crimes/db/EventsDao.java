package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Adiacenza> getArchi(String offense, int year){
		String sql = "select e.offense_type_id as id1,e1.offense_type_id as id2, count(distinct e.district_id) as peso "
				+ "from events e, events e1 "
				+ "where year(e.reported_date) =? "
				+ "AND e.offense_category_id=? "
				+ "AND year(e1.reported_date) =? "
				+ "AND e1.offense_category_id=? "
				+ "AND e.district_id= e1.district_id "
				+ "AND e.offense_type_id>e1.offense_type_id "
				+ "group by e.offense_type_id,e1.offense_type_id";
		List<Adiacenza> result  = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			st.setString(2, offense);
			st.setInt(3, year);
			st.setString(4, offense);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Adiacenza a = new Adiacenza(res.getNString("id1"), res.getNString("id2"), res.getInt("peso"));
				result.add(a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getVertici(String offense, int year){
		String sql= "select distinct offense_type_id as id "
				+ "from events "
				+ "where year(reported_date) =? "
				+ "AND offense_category_id=?";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, year);
			st.setString(2, offense);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("id"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Integer> listAllYears(){
		String sql="select distinct year(reported_date) as y "
				+ "from events";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getInt("y"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String> listAllOffense(){
		String sql="select distinct offense_category_id as off "
				+ "from events";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("off"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

}
