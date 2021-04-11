package com.compulynx.erevenue.models;

import java.util.List;

public class DashBoard {

	public int detailCount;
	public String detailDescription;
	public int load;
	public int purchase;
	public int totalTransCount;
	public String monthName;
	public int respCode;
	public double totalTxns;
	public double voidTxns;
	public double netTxns;
	public String amount;
	public double invalidTxns;
	public String subCountyName;
	public String userName;
	public List<DashBoard> currentMonth;
	public List<DashBoard> previousMonth;
	public List<DashBoard> twoMonth;
	public List<DashBoard> userDsc;
	public List<DashBoard> userAsc;
	public int tickets;
	
	
	public String service;
	public DashBoard(String service, int transCount) {
		super();
		this.service = service;
		this.transCount = transCount;
	}

	public int transCount;
	public DashBoard(double totalTxns, double voidTxns, double netTxns,String monthName,int respCode) {
		super();
		this.totalTxns = totalTxns;
		this.voidTxns = voidTxns;
		this.netTxns = netTxns;
		this.monthName = monthName;
		this.respCode = respCode;
	}

	public DashBoard(int respCode) {
		super();
		this.respCode = respCode;
	}
	
	public DashBoard() {
		super();
	}

	public DashBoard(int detailCount, String detailDescription, int respCode) {
		super();
		this.detailCount = detailCount;
		this.detailDescription = detailDescription;
		this.respCode = respCode;
	}
	public DashBoard(String amount, String detailDescription, int respCode) {
		super();
		this.amount= amount;
		this.detailDescription = detailDescription;
		this.respCode = respCode;
	}
	public DashBoard(int load, int purchase,
			int totalTransCount, String monthName, int respCode) {
		super();
		this.load = load;
		this.purchase = purchase;
		this.totalTransCount = totalTransCount;
		this.monthName = monthName;
		this.respCode = respCode;
	}

	public DashBoard(String monthName, double totalTxns, double voidTxns,double invalidTxns,
			double netTxns) {
		super();
		this.monthName = monthName;
		this.totalTxns = totalTxns;
		this.voidTxns = voidTxns;
		this.invalidTxns=invalidTxns;
		this.netTxns = netTxns;
	}
}
