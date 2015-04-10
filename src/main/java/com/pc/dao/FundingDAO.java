package com.pc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pc.db.ConnectionFactory;
import com.pc.ob.Funding;
//Service provider for funding servlet
public class FundingDAO {
	Connection connection;
	Statement stmt;
		
	public FundingDAO() { }
	
	private static Connection getConnection() 
			throws SQLException, 
				ClassNotFoundException 
	{
		//get connection instance
		Connection con = ConnectionFactory.
				getInstance().getConnection();
		return con;
	}
	
	//returns the total count of records for the corresponding query
	public int getTotalCount(String filterQuery){
		String query;
		//Construct the query for database search, based on the existence of filter conditions
		if(filterQuery != null && filterQuery != "") {
			query = "select count(*) total from fundings where " + filterQuery ;
		}
		else {
			query = "select count(*) total from fundings";
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
	
	public List<Funding> viewAllFundings(int skip, int rowCount, String sortQuery, String filterQuery)
	{
		String query;
		//construct query based on the existence of filter and sort conditions
		if(sortQuery != null && sortQuery != ""){
			if(filterQuery != null && filterQuery != "") {
				query = "select * from fundings where " + filterQuery + sortQuery + " limit " + skip + ", " + rowCount ;
			}
			else {
				query = "select * from fundings " + sortQuery + " limit " + skip + ", " + rowCount ;
			}
		}
		else{
			//if no filter or sort conditions exist, construct a query without them. skip and rowCount are to limit the total number of rows retrieved from a specified row.
			query = "select * from fundings limit "+ skip + ", " + rowCount;
		}
		List<Funding> list = new ArrayList<Funding>();
		Funding Funding = null;
		try {
			connection = getConnection();
			stmt = connection.createStatement();
			//get result set from query execution
			ResultSet rs = stmt.executeQuery(query);
			//add the result set data to an arrayList element
			while (rs.next()) {
				Funding = new Funding();
				Funding.setFundId(rs.getInt("fundId"));
				Funding.setFundLegId(rs.getInt("fundLegId"));
				Funding.setMerchantId(rs.getInt("merchId"));
				Funding.setFundDate(rs.getDate("fundDate"));
				Funding.setTerm(rs.getInt("term"));
				Funding.setFundedAmount(rs.getInt("fundedAmount"));
				Funding.setFactorPnt(rs.getDouble("factorPnt"));
				Funding.setPaybackAmount(rs.getDouble("paybackAmount"));
				Funding.setStatus(rs.getString("status"));
				Funding.setZipCode(rs.getInt("zipCode"));
				Funding.setSector(rs.getString("sector"));
				Funding.setIndustry(rs.getString("industry"));
				
				list.add(Funding);
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
