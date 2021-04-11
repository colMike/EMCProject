package com.compulynx.erevenue.models;

public class Device {

	public int id;
	public String devCode;


	public int regId;

	public String serialNo;
	public String macAddress;
	public String devType;
	public int userId;
	public String userName;
	public boolean status;
	public String last_active;
	public int createdBy;
	public int respCode;
	public int count;
	public int issueId;
	//issue device details
	public int devId;
	public int issuedTo;
	public String dateIssued;
	public String dateReturned;
	public String name;
	public String marketName;
	
	

	
	public Device() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Device(int id, String serialNo,boolean status, int createdBy,int count,
			int respCode) {
		super();
		this.id = id;
		this.serialNo = serialNo;
		this.status=status;
		this.createdBy = createdBy;
		this.count= count;
		this.respCode = respCode;
	}
			
	public Device(int issueId,int devId, String serialNo, int userId,  String userName,
			int createdBy,boolean status,int respCode,int count,String name,String marketName) {
		super();
		this.issueId = issueId;
		this.devId = devId;
		this.serialNo = serialNo;
		this.issuedTo = userId;
		this.userName=userName;
		this.createdBy = createdBy;
		this.status=status;
		this.respCode = respCode;
		this.count=count;
		this.name=name;
		this.marketName=marketName;
		
	}
	


}
