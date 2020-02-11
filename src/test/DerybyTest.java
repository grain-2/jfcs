package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.edu.jfcs.model.Users;
import cn.edu.jfcs.sys.HibernateSessionFactory;

public class DerybyTest {

	String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String url="jdbc:derby:jfcsdb;create=true";
	String closeUrl="jdbc:derby:jfcsdb;shutdown=true";
	Connection conn;
	
	public void con(){
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url, "jfcs", "007");
			String sql="select USERNAME,PASSWORD,USERTAG from USERS";
			PreparedStatement ps = conn.prepareStatement(sql);
			Statement st = conn.createStatement();
			st.executeUpdate("insert into USERS(USERNAME,PASSWORD,USERTAG) values('xcc','1','2')");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String name = rs.getString("USERNAME");
				String pwd = rs.getString("PASSWORD");
				String usertag = rs.getString("USERTAG");
				
				System.out.println("name:"+name+";pwd:"+pwd+";usertag:"+usertag);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				DriverManager.getConnection(closeUrl);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new DerybyTest().con();
	}
}
