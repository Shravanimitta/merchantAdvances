package com.pc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pc.dao.FundingDetailsDAO;
import com.pc.ob.FundingDetails;

/**
 * Servlet implementation class HealthDetails
 */
@WebServlet("/fundingDetails")
public class FundingDetailsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FundingDetailsController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//initial value set to 0 for fundid
				int fundId = 0;
		//Retrieve fundid if set and assign it
				if(request.getParameter("fundId") != null) {
					fundId = Integer.parseInt(request.getParameter("fundId"));
				}
				FundingDetailsDAO fdDao = new FundingDetailsDAO();
				
				//Call to get amount and payment codes
				FundingDetails fundingDetails  = fdDao.getFundingDetails(fundId);
				
				//send data in response
				Gson gson = new Gson();
				String fdJson = gson.toJson(fundingDetails);
				response.getWriter().write(fdJson.toString());
	}
}
