package com.pc.ob;

import java.util.List;

//Class that holds the payment list as well as other details of a funding 
public class FundingDetails {
	private int totalCount;
	private String status;
	private String sector;
	private String industry;
	private int zip;
	private double paybackAmount;
	private List<Payment> payments;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public double getPaybackAmount() {
		return paybackAmount;
	}
	public void setPaybackAmount(double paybackAmount) {
		this.paybackAmount = paybackAmount;
	}
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
}
