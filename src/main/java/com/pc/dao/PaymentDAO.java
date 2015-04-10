package com.pc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pc.db.ConnectionFactory;
import com.pc.ob.Payment;
//Service provider for payments
public class PaymentDAO {
	Connection connection;
	Statement stmt;
		
	private static Connection getConnection() 
			throws SQLException, 
				ClassNotFoundException 
	{
		Connection con = ConnectionFactory.
				//get connection instance
				getInstance().getConnection();
		return con;
	}
	
	
	//returns the total count of records for the corresponding query
	public int getTotalCount(String filterQuery){
		String query;
		//Construct the query for database search, based on the existence of filter conditions
		if(filterQuery != null && filterQuery != "") {
			query = "select count(*) total from payments where " + filterQuery ;
		}
		else {
			query = "select count(*) total from payments";
		}
		int total = 0;
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			//execute query and retrieve the result set
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				//get the total number of records for that query
				total = rs.getInt("total");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally
		{
			try {
				//close open db connections
				if(stmt != null)
					stmt.close();
				if(connection != null)
					connection.close();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return total;
	}
	
	//get payments for a fund Id
	public List<Payment> getPaymentsForFunding(int fundId, int skip, int rowCount, String sortQuery, String filterQuery)
	{
		String query = "";
		if(sortQuery != null && sortQuery != ""){
			if(filterQuery != null && filterQuery != "") {
				query = "select paymentId, systemDate, pmtCode, amount from payments where " + filterQuery + " and fundId =" + fundId+ " " + sortQuery + " limit " + skip + ", " + rowCount;
			}
			else {
				query = "select paymentId, systemDate, pmtCode, amount from payments where fundId="+ fundId + sortQuery + " limit " + skip + ", " + rowCount ;
			}
		}
		else{
			query = "select paymentId, systemDate, pmtCode, amount from payments where fundId="+ fundId+" limit " + skip + ", " + rowCount;
		}

		List<Payment> list = new ArrayList<Payment>();
		
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			//build payments list from result set
			while (rs.next()) {
				Payment p = new Payment();
				p.setPaymentId(rs.getInt("paymentId"));
				p.setSystemDate(rs.getDate("systemDate"));
				p.setSystemDateUnformatted(rs.getString("systemDate"));
				p.setPmtCode(rs.getString("pmtCode"));
				p.setAmount(rs.getDouble("amount"));
				list.add(p);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally
		{
			try {
				//close open db connections
				if(stmt != null)
					stmt.close();
				if(connection != null)
					connection.close();
				} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//return the list
		return list;
	}
}
