/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Programme {

	public Programme(int programmeId, String programmeDesc, boolean isActive,
			int createdBy, int respCode) {
		super();
		this.programmeId = programmeId;
		this.programmeDesc = programmeDesc;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		
	}
	public int programmeId;
	public String programmeCode;
	public String programmeDesc;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public List<Service> services;
	
	/**
	 * 
	 */
	public Programme() {
		super();
	}
	public Programme(int respCode) {
		super();
		this.respCode=respCode;
	}
	public Programme(int programmeId, String programmeCode,
			String programmeDesc, boolean active, int createdBy, int respCode) {
		super();
		this.programmeId = programmeId;
		this.programmeCode = programmeCode;
		this.programmeDesc = programmeDesc;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}
	
}
