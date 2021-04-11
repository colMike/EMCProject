/**
 * 
 */
package com.compulynx.erevenue.models;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public class PermitType {

	public int permitTypeId;
	public String permitTypeCode;
	public String permitTypeName;
	public String permitType;
	public double permitFee;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public PermitType() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PermitType(int permitTypeId, String permitTypeCode,
			String permitTypeName, double permitFee,boolean active, int respCode, int count) {
		super();
		this.permitTypeId = permitTypeId;
		this.permitTypeCode = permitTypeCode;
		this.permitTypeName = permitTypeName;
		this.permitFee=permitFee;
		this.active = active;
		this.respCode = respCode;
		this.count = count;
	}
	public PermitType(int permitTypeId, String permitTypeName) {
		super();
		this.permitTypeId = permitTypeId;
		this.permitTypeName = permitTypeName;
	}
}
