package com.pc.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	//establish connection to database using connection factory
	public static ConnectionFactory instance = new ConnectionFactory();
	String driverClass = "com.mysql.jdbc.Driver"; 
	
	private ConnectionFactory() {
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static ConnectionFactory getInstance()	{
		return instance;
	}
	
	public Connection getConnection() throws SQLException, 
	ClassNotFoundException, IOException {
		
		Properties props = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
		   
		   if(is != null){
			   props.load(is);
		   }
		   is.close();
		   if (driverClass != null) {
		       Class.forName(driverClass) ;
		   }
		   String url = props.getProperty("jdbc.url");
		   String username = props.getProperty("jdbc.username");
		   String password = props.getProperty("jdbc.password");

		   Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
	
}
