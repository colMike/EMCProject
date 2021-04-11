package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;

/**
 * The Class SubCountyDal.
 * @author Anita 
 * @date   Apr 12, 2016
 */
public interface SubCountyDal {

	ErevenueResponse UpdateSubCounty(SubCounty subCounty);

	List<SubCounty> GetAllSubCounties();
	
}
