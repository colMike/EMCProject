package com.compulynx.erevenue.models;

import java.util.List;

public class LandRate {
	//land details
	public int id;
	public String plotNumber;
	public String mapSheetNumber;
	public String location;
	public String acreage; 
	public String titleDeedNumber;
	public int permitTypeId;
	
	//owner details
	public String name;
	public String krapin;
	public String nationalIdNumber;
	public String createdBy;
	public String created_at;
	public int respCode;
	public int count;
	
	//land location details
	public int wardId;
	public int subCountyId;
	
	//details
	
	public int landId;
	public String landNo;
	public int approvedBy;
	public String approvedOn;
	public String rejectReason;
	public String approvedUser;
	public String status;
	public String mpesaCode;
	public String paidStatus;
	public double fee;
	public double penalty;
	public int regId;
	public String regNo;
	public String applicant;
	public String appliedOn;
	public String paidDate;
	public int paidBy;
	public String paidUser;
	public int receiptBy;
	public int validity;
	public String permitUser;
	public List<PermitYear> yearList;
	public int amendedBy;
	public int appliedFor;
	public int linkId;
	public String subCountyName;
	public String wardName;
	public String bankName;
	public String accNo;
	public String transNo;
	public String landType;
	public String permitNo;
	public String preLr;
	public String landTypeName;
	public String permitStatus;
	public double balance;
	public double amount;
	public String sublocation;
	public String address;
	public String phone;
	public String rates;
	public String arrears;
	public String total;
	public String titleNature;
	public String parcelNumber;
	public String blocknumber;
	public String code;
	
	public LandRate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LandRate(int id, String plotNumber, String mapSheetNumber,
			String location, String acreage, String titleDeedNumber,int permitTypeId,
			String name, String krapin, String nationalIdNumber,
			int wardId, int subCountyId,int respCode, int count,String landStatus,String createdBy,double amount,double balance) {
		super();
		this.id = id;
		this.plotNumber = plotNumber;
		this.mapSheetNumber = mapSheetNumber;
		this.location = location;
		this.acreage = acreage;
		this.titleDeedNumber = titleDeedNumber;
		this.permitTypeId=permitTypeId;
		this.name = name;
		this.krapin = krapin;
		this.nationalIdNumber = nationalIdNumber;
		this.wardId = wardId;
		this.subCountyId = subCountyId;
		this.respCode = respCode;
		this.count = count;
		this.status=landStatus;
		this.createdBy=createdBy;
		this.amount=amount;
		this.balance=balance;
	}

	
	

}
