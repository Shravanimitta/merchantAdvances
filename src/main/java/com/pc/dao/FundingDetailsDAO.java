package com.pc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pc.db.ConnectionFactory;
import com.pc.ob.FundingDetails;

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
	
	public FundingDetails getFundingDetails(int fundId){
		String query = "";
		query = "select payments.pmtCode, payments.amount, fundings.status, fundings.sector, fundings.industry, fundings.zipCode, fundings.paybackAmount from payments, fundings where payments.fundId = fundings.fundId and payments.fundId=" + fundId;
		
		FundingDetails fd = null;
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			List<String> pcList = new ArrayList<String>();
			List<Double> amtList = new ArrayList<Double>();
			//get data from result set and populate funding Details
			while (rs.next()) {
				if(fd == null){
					fd = new FundingDetails();
					fd.setStatus(rs.getString("status"));
					fd.setIndustry(rs.getString("industry"));
					fd.setZip(rs.getInt("zipCode"));
					fd.setPaybackAmount(rs.getDouble("paybackAmount"));
					fd.setSector(rs.getString("sector"));
				}
				pcList.add(rs.getString("pmtCode"));
				amtList.add(rs.getDouble("amount"));
			}
			fd.setPmtCodes(pcList);
			fd.setAmounts(amtList);
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
}
