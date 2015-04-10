package com.pc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	//establish connection to database using connection factory
	public static ConnectionFactory instance = new ConnectionFactory();
	String url = "jdbc:mysql://127.7.102.130:3306/jbossews";
	String user = "adminESu7ZNr";
	String password = "ntinIQ5QkGY6";
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
	ClassNotFoundException {
		Connection connection = 
			DriverManager.getConnection(url, user, password);
		return connection;
	}
	
}
