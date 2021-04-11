/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


import com.compulynx.erevenue.bal.SubMarketBal;
import com.compulynx.erevenue.dal.impl.SubMarketDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.SubMarket;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public class SubMarketBalImpl implements SubMarketBal{

	@Autowired
	SubMarketDalImpl subMarketDal;

	@Override
	public ErevenueResponse UpdateSubMarket(SubMarket submarket) {
		// TODO Auto-generated method stub
		return subMarketDal.UpdateSubMarket(submarket);
	}

	@Override
	public List<SubMarket> GetAllSubMarkets() {
		// TODO Auto-generated method stub
		return subMarketDal.GetAllSubMarkets();
	}

	@Override
	public List<Market> GetActiveMarkets() {
		// TODO Auto-generated method stub
		return subMarketDal.GetActiveMarkets();
	}
	

}
