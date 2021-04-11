/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.bal.MarketBal;
import com.compulynx.erevenue.dal.impl.MarketDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */
@Component
public class MarketBalImpl implements MarketBal{
	
	@Autowired
	MarketDalImpl marketDal;
	@Override
	public ErevenueResponse UpdateMarket(Market market) {
		// TODO Auto-generated method stub
		return marketDal.UpdateMarket(market);
	}

	@Override
	public List<Market> GetAllMarkets() {
		// TODO Auto-generated method stub
		return marketDal.GetAllMarkets();
	}

	@Override
	public List<Ward> GetActiveWards() {
		// TODO Auto-generated method stub
		return marketDal.GetActiveWards();
	}

	

}
