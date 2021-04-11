package com.compulynx.erevenue.models;

import java.util.List;

public class User {
	
	public int userId;
	public String userName;
	public String userFullName;
	public String userPwd;
	public String userEmail;
	public String userPhone;
	public int groupId;
	public String groupName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public String deviceId;
	public int marketId;
	public long userLinkedID;
	public long userBioID;
	public int userTypeId;
	public String userNationalId;
	public int count;
	public String status;
	
	public User(int userId, String userName, String userFullName, String userPwd,
			String userEmail, String userPhone, int groupId,
			 boolean active, int createdBy,
			int respCode,String userNationalId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userPwd = userPwd;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.groupId = groupId;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.userNationalId=userNationalId;
	}
	public User(int userId,String userName, String userFullName, String userPwd,
			int userGroupId,int agentId,int branchId, String userEmail, String userPhone,
			int userSecretQuestionId, String userSecretAns,
			boolean userBioLogin, long userLinkedID, long userBioID,
			boolean active, int createdBy, int respCode) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userPwd = userPwd;
		this.groupId = userGroupId;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.userLinkedID = userLinkedID;
		this.userBioID = userBioID;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}
	
	

	public User(int respCode) {
		super();
		this.respCode = respCode;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
}