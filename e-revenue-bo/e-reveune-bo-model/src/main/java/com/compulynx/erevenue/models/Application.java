/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 * @date Apr 15, 2016
 *
 */
public class Application {

	public int businessId;
	public String businessName;
	public int noOfEmployees;
	public int permitTypeId;
	public double permitFee;
	public String waterAccNo;
	public String electricityAccNo;
	public int area;
	public String regNo;
	public String businessDesc;
	public String postalAdd;
	public String postalCode;
	public String email;
	public String fax;
	public String mobileNo;
	public String landLineNo;
	public int wardId;
	public String landZone;
	public String plotNo;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public List<PermitYear> yearList;

	//Application Details
	public int appId;
	public String appNo;
	public String mpesaCode;
	public String businessNo;
	public String preSbp;
	public double fee;
	public boolean paid;
	public String paidDate;
	public int appliedFor;
	public String appliedOn;
	public double penalty;
	public String appType;
	public String remarks;
	public String status;
	public String permitStatus;
	public String amountInWords;
	public int approvedBy;
	public String approvedOn;
	public int receiptBy;
	public String receiptOn;
	public String applicant;
	public String approvedUser;
	public String paidStatus;
	public String rejectReason;
	public int validity;
	public String expiryDate;
	public String permitNo;
	public String permitUser;
	public String permitQr;
	public int paidBy;
	public String paidUser;
	public String nationalId;
	public int linkId;
	public String bankName;
	public String accNo;
	public String transNo;
	public String name;
	public int marketId;
	public String mktName;
	public String wardName;
                  public String paymentMode;
				public String road;

				public String pinNumber;
				public String vatNumber;
				public String detailDesc;
				public String town;
				public String building;
				public String floor;
				public String room;
				public String appFee;
				public String conservancy_fee;

	public Application() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Application(int businessId,String businessNo, String businessName, int noOfEmployees,
			int permitTypeId, double permitFee, String electricityAccNo,String waterAccNo,
			int area, String regNo,String postalAdd, 
			String postalCode, String email, String fax, String mobileNo,	String landLineNo,String room,
			String road,String businessDesc, String pinNumber, String vatNumber, String detailDesc, String town, String building, String floor,
		 int wardId, String landZone, String plotNo,
			boolean active,int i, int count2, String appStatus,int createdBy,String nationalId,int marketId ,String applicant,String appFee, String paymentMode) {
		super();
		this.room=room;
		this.road=road;
		this.pinNumber=pinNumber;
		this.vatNumber=vatNumber;
		this.detailDesc=detailDesc;
		this.town=town;
		this.building=building;
		this.floor=floor;
		this.businessId = businessId;
		this.businessNo=businessNo;
		this.businessName = businessName;
		this.noOfEmployees = noOfEmployees;
		this.permitTypeId = permitTypeId;
		this.permitFee=permitFee;
		this.waterAccNo = waterAccNo;
		this.electricityAccNo = electricityAccNo;
		this.area = area;
		this.regNo = regNo;
		this.businessDesc = businessDesc;
		this.postalAdd = postalAdd;
		this.postalCode = postalCode;
		this.email = email;
		this.fax = fax;
		this.mobileNo = mobileNo;
		this.landLineNo = landLineNo;
		this.wardId = wardId;
		this.landZone = landZone;
		this.plotNo = plotNo;
		this.active = active;
		//this.respCode=respCode;
		//this.count=count;
		this.status=appStatus;
		this.createdBy=createdBy;
		this.nationalId=nationalId;
		this.marketId=marketId;
		this.applicant=applicant;
                                    this.paymentMode    =   paymentMode;
	}
}
