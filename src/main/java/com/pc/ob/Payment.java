package com.pc.ob;

import java.sql.Date;

//Class for payment objects. 
public class Payment {

	private int paymentId;
	private int fundId;
	private int merchId;
	private Date systemDate;
	private String systemDateUnformatted;
	private String pmtCode;
	private String achCode;
	private double amount;
	private String status;
	
	//initialize payment object variables using setter methods and retrieve them for operations using getter methods
	public int getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	
	public int getMerchId() {
		return merchId;
	}
	public void setMerchId(int merchId) {
		this.merchId = merchId;
	}
	
	public String getPmtCode() {
		return pmtCode;
	}
	public void setPmtCode(String pmtCode) {
		this.pmtCode = pmtCode;
	}
	
	public String getAchCode() {
		return achCode;
	}
	public void setAchCode(String achCode) {
		this.achCode = achCode;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Date getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}
	public String getSystemDateUnformatted() {
		return systemDateUnformatted;
	}
	public void setSystemDateUnformatted(String systemDateUnformatted) {
		this.systemDateUnformatted = systemDateUnformatted;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
