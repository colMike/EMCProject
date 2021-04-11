/**
 * 
 */
package com.compulynx.erevenue.models;

/**
 * @author Anita
 *
 */
public class SubCounty {

	public int subCountyId;
	public String subCountyCode;
	public String subCountyName;
	public boolean active;
	public String status;
	public int createdBy;
	public int respCode;
	public int count;
	public double serviceValue;
	
	public SubCounty(int subCountyId, String subCountyCode,
			String subCountyName, boolean active, int respCode,
			int count,String status) {
		super();
		this.subCountyId = subCountyId;
		this.subCountyCode = subCountyCode;
		this.subCountyName = subCountyName;
		this.active = active;
		this.respCode = respCode;
		this.count = count;
		this.status=status;
	}
	public SubCounty() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SubCounty(int subCountyId, String subCountyName) {
		super();
		this.subCountyId = subCountyId;
		this.subCountyName = subCountyName;
	}
}
