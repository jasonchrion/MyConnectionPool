package lxw.connectionpool;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.Vector;

public class ConnectionPool {
	private Vector<Connection> pool;
	
	private String url;
	
	private String username;
	
	private String password;
	
	private String driverClassName;
	
	//连接池默认大小
	private int poolSize = 1;
	
	private static ConnectionPool instance = null;
	
	//单例懒加载
    public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
    	return instance;
	}
	
	private ConnectionPool(){
    	init();
    }

    //初始化连接池，读取属性文件
	private void init() {
		pool =  new Vector<Connection>(poolSize);
		readConfig();
		addConnection();
	}
	
	//用户获取Connection
	public synchronized Connection getConnection() {
		if (pool.size() > 0) {
			Connection connection = pool.get(0);
			pool.remove(connection);
			return connection;
		} else {
			return null;
		}
	}

	//用户使用完返回到连接池
	public synchronized void release(Connection connection){
		pool.add(connection);
	}
	
	//关闭连接池中所有连接
	public synchronized void closePool(){
		for (int i = 0; i < pool.size(); i++) {
			try {
				pool.get(i).close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			pool.remove(i);
		}
	}
	
	//读取配置文件
	private void readConfig() {
		try {
			String path = System.getProperty("user.dir") + "\\bin\\dbpool.properties";
			FileInputStream is = new FileInputStream(path);
			Properties properties = new Properties();
			properties.load(is);
			this.driverClassName = properties.getProperty("driverClassName");
			this.username = properties.getProperty("username");
			this.password = properties.getProperty("password");
			this.url = properties.getProperty("url");
			this.poolSize = Integer.parseInt(properties.getProperty("poolSize"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	//初始化连接池
	private void addConnection() {
		Connection connection = null;
		for (int i = 0; i < poolSize; i++) {
			try {
				Class.forName(driverClassName);
				connection = DriverManager.getConnection(url, username, password);
				pool.add(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
