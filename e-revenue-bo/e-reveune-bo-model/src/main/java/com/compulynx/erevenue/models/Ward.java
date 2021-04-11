/**
 * 
 */
package com.compulynx.erevenue.models;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */
public class Ward {

	public int wardId;
	public String wardCode;
	public String wardName;
	public int subCountyId;
	public String subCountyName;
	public boolean active;
	public String status;
	public int createdBy;
	public int respCode;
	public int count;
	public double serviceValue;
	
	public Ward() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ward(int wardId,String wardCode, String wardName, int subCountyId,String subCountyName, boolean active,
			int respCode,int count,String status) {
		super();
		this.wardId = wardId;
		this.wardCode=wardCode;
		this.wardName = wardName;
		this.subCountyId = subCountyId;
		this.subCountyName=subCountyName;
		this.active = active;
		this.respCode = respCode;
		this.count=count;
		this.status=status;
	}

	//constructor for getting active wards
	public Ward(int wardId, String wardName) {
		super();
		this.wardId = wardId;
		this.wardName = wardName;
	}
}
