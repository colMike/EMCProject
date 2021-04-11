/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;



/**
 * @author Anita
 *
 */
public class LoginUser {
	public int userId;
	public int respCode;
	public String respMessage;
	public int userBioID;

	
	public LoginUser(int userId, int respCode) {
		super();
		this.userId = userId;
		this.respCode = respCode;
	}
	
	public LoginUser(int respCode) {
		super();
		this.respCode = respCode;
	}

	public LoginUser() {
		super();
	}
}
