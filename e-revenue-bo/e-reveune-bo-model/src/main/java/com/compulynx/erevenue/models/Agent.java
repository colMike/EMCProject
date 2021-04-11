/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Agent {

	public int agentId;
	public String agentDesc;
	public int branchId;
	public boolean active;
	public int createdBy;
	public int respCode;
	
	public Agent(int agentId, String agentDesc, int branchId, boolean active,
			int createdBy, int respCode) {
		super();
		this.agentId = agentId;
		this.agentDesc = agentDesc;
		this.branchId = branchId;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Programme> programmes;
}
