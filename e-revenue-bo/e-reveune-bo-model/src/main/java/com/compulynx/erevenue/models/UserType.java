/**
 * 
 */
package com.compulynx.erevenue.models;

/**
 * @author Anita
 *
 */
public class UserType {

	public int userTypeId;
	public String userTypeName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public UserType(int userTypeId, String userTypeName) {
		super();
		this.userTypeId = userTypeId;
		this.userTypeName = userTypeName;
	}
	public UserType() {
		super();
		// TODO Auto-generated constructor stub
	}
}
