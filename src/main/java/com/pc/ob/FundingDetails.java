package com.pc.ob;

import java.util.List;

public class FundingDetails {
	private List<String> pmtCodes;
	private List<Double> amounts;
	private String status;
	private String sector;
	private String industry;
	private int zip;
	private double paybackAmount;
	
	public List<String> getPmtCodes() {
		return pmtCodes;
	}
	public void setPmtCodes(List<String> pmtCodes) {
		this.pmtCodes = pmtCodes;
	}
	public List<Double> getAmounts() {
		return amounts;
	}
	public void setAmounts(List<Double> amounts) {
		this.amounts = amounts;
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
}
