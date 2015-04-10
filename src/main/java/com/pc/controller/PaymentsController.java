package com.pc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pc.dao.PaymentDAO;
import com.pc.ob.Payment;

/**
 * Servlet implementation class PaymentServlet
 */
@WebServlet("/payments")
public class PaymentsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public PaymentsController() {
        	super();
	}

	public void doGet(HttpServletRequest request, 
			HttpServletResponse response) 
			throws ServletException, IOException {
		//initial value set to 0 for fundid
		int fundId = 0;
		
		//default set to 10 rows per page
		int startingPosition = 1;
		int recordsPerPage = 10;
		
		//Holds the sort clause part of the query
		String sortQuery;
		
		//Holds the filter clause of the query
		String filterQuery="";
		
		//Retrieve fundid, page number and records per page if set and assign them
		if(request.getParameter("fundId") != null) 
			fundId = Integer.parseInt(request.getParameter("fundId"));
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
			filterQuery = filterQuery + " payments." + request.getParameter("filterBy"+i) + " LIKE " + "'%" + request.getParameter("filterBy"+i+"Text") + "%'";
			i++;
		}
		
		//Construct the sorting field and order
		sortQuery = " order by payments." + request.getParameter("sortBy") + " " + request.getParameter("sortOrder");
			
		PaymentDAO paymentDao = new PaymentDAO();
		//making sure starting record number is not less than 1
		if(startingPosition < 1){
			startingPosition = 1;
		}
		
		PaymentsWrapper pw = new PaymentsWrapper();
		//Call to get payments list
		pw.paymentsList = paymentDao.getPaymentsForFunding(fundId, (startingPosition-1)*recordsPerPage,recordsPerPage, sortQuery, filterQuery);
		//If fundId is provided then add to filter clause
		if(fundId != 0) {
			if(filterQuery != null && filterQuery != "") {
				filterQuery = " payments.fundId =" + fundId + " And " + filterQuery;
			}
			else {
				filterQuery = " payments.fundId =" + fundId ;
			}
		}
		pw.totalCount = paymentDao.getTotalCount(filterQuery);
		Gson gson = new Gson();
		String pwJson = gson.toJson(pw);
		response.getWriter().write(pwJson.toString());
	}
	
	//Class that holds the payment list as well as the count of totals records. 
	class PaymentsWrapper {
		List<Payment> paymentsList;
		int totalCount;
	}
}
