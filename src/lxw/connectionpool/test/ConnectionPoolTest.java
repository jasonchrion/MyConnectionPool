package lxw.connectionpool.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import lxw.connectionpool.ConnectionPool;

public class ConnectionPoolTest {
	
	public static void main(String[] args) throws Exception {
		String sql = "select count(*) from user";
		long start_time = System.currentTimeMillis();
		ConnectionPool pool = null;
		
		for (int i = 0; i < 1000; i++) {
			pool = ConnectionPool.getInstance();
			Connection connection = pool.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
			}
			rs.close();
			stmt.close();
			pool.release(connection);
		}
		pool.closePool();
		long end_time = System.currentTimeMillis();
		System.out.println("使用连接池后耗费时间： " + (end_time - start_time) + " 秒");
		
		String driverClass = "com.mysql.jdbc.Driver";
		String user = "root";
		String password = "sa123456";
		String url = "jdbc:mysql://localhost:3306/mysql";
		start_time = System.currentTimeMillis();
		
		for (int i = 0; i < 1000; i++) {
			Class.forName(driverClass);
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				
			}
			rs.close();
			stmt.close();
			connection.close();
		}
		
		end_time = System.currentTimeMillis();
		System.out.println("不使用连接池后耗费时间： " + (end_time - start_time) + " 秒");
	}
}
