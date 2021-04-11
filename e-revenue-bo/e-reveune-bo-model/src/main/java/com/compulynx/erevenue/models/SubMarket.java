/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public class SubMarket {

	public int subMarketId;
	public String subMarketCode;
	public String subMarketName;
	public int marketId;
	public String marketName;
	public boolean active;
	public String status;
	public int createdBy;
	public int respCode;
	public int count;
	public List<SubMarket> subMarketDetails;
	public SubMarket() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SubMarket(int subMarketId, String subMarketCode,
			String subMarketName, int marketId,String marketName, boolean active, int respCode,
			int count,String status) {
		super();
		this.subMarketId = subMarketId;
		this.subMarketCode = subMarketCode;
		this.subMarketName = subMarketName;
		this.marketId = marketId;
		this.marketName=marketName;
		this.active = active;
		this.respCode = respCode;
		this.count = count;
		this.status=status;
	}
}
