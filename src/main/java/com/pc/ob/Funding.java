package com.pc.ob;

import java.sql.Date;
//Class for funding objects. 
public class Funding {
	private int fundId ;
	private int fundLegId;
	private Date fundDate;
	private int merchId;
	private int term;
	private double factorPnt;
	private int fundedAmount;
	private double paybackAmount;
	private String status;
	private int zipCode;
	private String sector;
	private String industry;
	
	//initialize funding object variables using setter methods and retrieve them for operations using getter methods
	//set fundId
	public int getFundId() {
		return fundId;
	}
	
	//get fundid
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	
	//set fundLegId
	public int getFundLegId() {
		return fundLegId;
	}
	
	//get fundLegId
	public void setFundLegId(int fundLegId) {
		this.fundLegId = fundLegId;
	}
	
	//set fundDate
	public Date getFundDate() {
		return fundDate;
	}
	
	//set fundDate
	public void setFundDate(Date fundDate) {
		this.fundDate = fundDate;
	}
	
	//get merchantId
	public int getMerchantId() {
		return merchId;
	}
	
	//set merchantId
	public void setMerchantId(int merchId) {
		this.merchId = merchId;
	}
	
	//get Term
	public int getTerm() {
		return term;
	}
	
	//set Term
	public void setTerm(int term) {
		this.term = term;
	}
	
	//get FactorPnt
	public double getFactorPnt() {
		return factorPnt;
	}
	
	//set FactorPnt
	public void setFactorPnt(double factorPnt) {
		this.factorPnt = factorPnt;
	}
	
	//get Funded Amount
	public int getFundedAmount() {
		return fundedAmount;
	}
	
	//set Funded Amount
	public void setFundedAmount(int fundedAmount) {
		this.fundedAmount = fundedAmount;
	}
	
	//get Payback Amount
	public double getPaybackAmount() {
		return paybackAmount;
	}
	
	//set Payback Amount
	public void setPaybackAmount(double paybackAmount) {
		this.paybackAmount = paybackAmount;
	}
	
	//get Status
	public String getStatus() {
		return status;
	}
	//set Status
	public void setStatus(String status) {
		this.status = status;
	}
	
	//get Zip Code
	public int getZipCode() {
		return zipCode;
	}
	
	//set Zip Code
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	
	//get Sector
	public String getSector() {
		return sector;
	}
	//set Sector
	public void setSector(String sector) {
		this.sector = sector;
	}
	
	//get Industry
	public String getIndustry() {
		return industry;
	}
	
	//set Industry
	public void setIndustry(String industry) {
		this.industry = industry;
	}
}
