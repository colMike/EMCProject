/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */
public class Market {
	
	public int marketId;
	public String marketCode;
	public String marketName;
	public int wardId;
	public String wardName;
	public int subCountyId;
	public String subCountyName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public List<Market> marketDetails;
	public String status;
	public double serviceValue;
	
	public Market() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Market(int marketId, String marketCode,String marketName,int wardId, String wardName,
			int subCountyId, String subCountyName, boolean active,
			int respCode, int count,String status) {
		super();
		this.marketId = marketId;
		this.marketCode = marketCode;
		this.marketName=marketName;
		this.wardId = wardId;
		this.wardName = wardName;
		this.subCountyId = subCountyId;
		this.subCountyName = subCountyName;
		this.active = active;
		this.respCode = respCode;
		this.count = count;
		this.status=status;
	}

	//constructor for getting active markets
	public Market(int marketId, String marketName) {
		super();
		this.marketId = marketId;
		this.marketName = marketName;
	}
	
}
