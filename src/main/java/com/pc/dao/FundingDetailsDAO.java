package com.pc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pc.db.ConnectionFactory;
import com.pc.ob.FundingDetails;
import com.pc.ob.Payment;
import com.pc.ob.HealthInfo;

public class FundingDetailsDAO {
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
	
	public FundingDetails getFundingDetails(int fundId, int skip, int rowCount, String sortQuery, String filterQuery)
	{
		String query = "";
		if(sortQuery != null && sortQuery != ""){
			if(filterQuery != null && filterQuery != "") {
				query = "select payments.paymentId, payments.systemDate, payments.pmtCode, payments.amount, fundings.status, fundings.sector, fundings.industry, fundings.zipCode, fundings.paybackAmount from payments, fundings where " + filterQuery + " and payments.fundId = fundings.fundId and payments.fundId=" + fundId+ " " + sortQuery + " limit " + skip + ", " + rowCount ;
			}
			else {
				query = "select payments.paymentId, payments.systemDate, payments.pmtCode, payments.amount, fundings.status, fundings.sector, fundings.industry, fundings.zipCode, fundings.paybackAmount from payments, fundings where payments.fundId = fundings.fundId and payments.fundId="+ fundId + sortQuery + " limit " + skip + ", " + rowCount ;
			}
		}
		else{
			query = "select payments.paymentId, payments.systemDate, payments.pmtCode, payments.amount, fundings.status, fundings.sector, fundings.industry, fundings.zipCode, fundings.paybackAmount from payments, fundings where payments.fundId="+ fundId+" limit " + skip + ", " + rowCount;
		}

		FundingDetails fd = null;
		List<Payment> list = new ArrayList<Payment>();
		
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			//get result set from query execution
			ResultSet rs = stmt.executeQuery(query);
			//add the result set data to an arrayList element
			int i=0;
			fd = new FundingDetails();
			while (rs.next()) {
				Payment p = new Payment();
				if(i==0){
					fd.setStatus(rs.getString("status"));
					fd.setSector(rs.getString("sector"));
					fd.setIndustry(rs.getString("industry"));
					fd.setZip(rs.getInt("zipCode"));
					fd.setPaybackAmount(rs.getDouble("paybackAmount"));
				}
				p.setPaymentId(rs.getInt("paymentId"));
				p.setSystemDate(rs.getDate("systemDate"));
				p.setSystemDateUnformatted(rs.getString("systemDate"));
				p.setPmtCode(rs.getString("pmtCode"));
				p.setAmount(rs.getDouble("amount"));
				list.add(p);
				i++;
			}
			fd.setPayments(list);
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
		return fd;
	}
	
	public List<HealthInfo> getHealthDetails(int fundId){
		String query = "";
		
		query = "select pmtCode, amount from payments where fundId =" + fundId;
		
		HealthInfo hi = null;
		List<HealthInfo> list = new ArrayList<HealthInfo>();
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			//get result set from query execution
			ResultSet rs = stmt.executeQuery(query);
			//add the result set data to an arrayList element
			while (rs.next()) {
				hi = new HealthInfo();
				hi.setPmtCode(rs.getString("pmtCode"));
				hi.setAmount(rs.getDouble("amount"));
				list.add(hi);
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
