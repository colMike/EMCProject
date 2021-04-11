/**
 * 
 */
package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */
public interface MarketDal {
	
	ErevenueResponse UpdateMarket(Market market);

	List<Market> GetAllMarkets();
	
	List<Ward> GetActiveWards();
}
