package com.pc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pc.dao.FundingDAO;
import com.pc.ob.Funding;
/**
 * Servlet implementation class FundingConroller
 */
@WebServlet("/funding")
public class FundingController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FundingController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//default set to 100 rows per page
		int startingPosition = 1;
		int recordsPerPage = 100;
		
		//Holds the sort clause part of the query
		String sortQuery;

		//Holds the filter clause of the query
		String filterQuery="";
		
		//Retrieve page number and records per page if set and reset defaults
		if(request.getParameter("pageNo") != null) 
			startingPosition = Integer.parseInt(request.getParameter("pageNo"));
		if(request.getParameter("perPage") != null)
			recordsPerPage = Integer.parseInt(request.getParameter("perPage"));
		
		int i = 1;
		//Construct the filter condition
		while(request.getParameter("filterBy"+i) != null) {
			if(i>1) {
				filterQuery = filterQuery + " AND";
			}
			filterQuery = filterQuery + " " + request.getParameter("filterBy"+i) + " LIKE " + "'%" + request.getParameter("filterBy"+i+"Text") + "%'";
			i++;
		}
		
		//Construct the sorting field and order
		sortQuery = " order by " + request.getParameter("sortBy") + " " + request.getParameter("sortOrder");
		
		FundingDAO dao = new FundingDAO();
		if(startingPosition < 1){
			startingPosition = 1;
		}	
		//Call videAllFundings to get all the fundings list
		List<Funding> list = dao.viewAllFundings((startingPosition-1)*recordsPerPage,recordsPerPage, sortQuery, filterQuery);
		FundingsWrapper fw = new FundingsWrapper();
		fw.fundingsList = list;
		fw.totalCount = dao.getTotalCount(filterQuery);
		Gson gson = new Gson();
		String jsonFundings = gson.toJson(fw);
		response.getWriter().write(jsonFundings.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	//Class that holds the funding list as well as the count of totals records.
	class FundingsWrapper {
		List<Funding> fundingsList;
		int totalCount;
	}
}


