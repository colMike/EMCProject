package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;

/**
 * The Class SubCountyBal.
 * @author Anita 
 * @date Apr 12, 2016
 */
public interface SubCountyBal {
	
	ErevenueResponse UpdateSubCounty(SubCounty subCounty);

	List<SubCounty> GetAllSubCounties();
}
