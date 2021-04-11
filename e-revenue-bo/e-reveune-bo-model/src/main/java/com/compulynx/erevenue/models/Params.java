/**
 * 
 */
package com.compulynx.erevenue.models;

/**
 * @author Anita
 *
 */
public class Params {
	public int paramId;
	public int serviceId;
	public String paramName;
	public String paramType;
	public boolean active;
	public String status;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public String paramValue;
	public int counter;
	

	public Params() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Params(int paramId, String paramName,String paramType, boolean active, int createdBy,int respCode,int counter,String status){
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		this.paramType=paramType;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.counter=counter;
		this.status=status;
		
	}

	public Params(int paramId, String paramName,
			boolean isActive, int respCode) {
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		this.isActive = isActive;
		this.respCode = respCode;
	}
	public Params(int paramId, String paramName) {
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		
	}
}
