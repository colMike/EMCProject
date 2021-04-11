/**
 * 
 */
package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.SubMarket;


/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public interface SubMarketDal {
	ErevenueResponse UpdateSubMarket(SubMarket submarket);

	List<SubMarket> GetAllSubMarkets();
	
	List<Market> GetActiveMarkets();
}
