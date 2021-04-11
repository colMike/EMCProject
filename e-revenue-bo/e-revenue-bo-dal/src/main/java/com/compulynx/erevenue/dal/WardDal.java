/**
 * 
 */
package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */
public interface WardDal {

	ErevenueResponse UpdateWard(Ward ward);

	List<Ward> GetAllWards();
	List<SubCounty> GetActiveSubCounties();
}
