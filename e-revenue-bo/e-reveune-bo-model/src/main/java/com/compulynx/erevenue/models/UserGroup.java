package com.compulynx.erevenue.models;

import java.util.List;

public class UserGroup{
	
	public int groupId;
	public String groupCode;
	public String groupName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public List<RightsDetail> rights;
	public int groupClassId;
	public int groupLinkId;
	public int count;
	public int roleTypeId;
	public UserGroup(int groupId, String groupCode, String groupName,boolean active ,int createdBy,int respCode) {
		super();
		this.groupId = groupId;
		this.groupCode = groupCode;
		this.groupName = groupName;
		this.active=active;
		this.createdBy=createdBy;	
		this.respCode=respCode;
	}
	public UserGroup(int groupId, String groupName, int respCode) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.respCode = respCode;
	}
	
	public UserGroup(int respCode){
		super();
		this.respCode=respCode;
	}
	
	public UserGroup() {
		super();
	}
	
}