/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.bal.WardBal;
import com.compulynx.erevenue.dal.impl.WardDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */

@Component
public class WardBalImpl implements WardBal{

	@Autowired
	WardDalImpl wardDal;
	@Override
	public ErevenueResponse UpdateWard(Ward ward) {
		// TODO Auto-generated method stub
		return wardDal.UpdateWard(ward);
	}

	@Override
	public List<Ward> GetAllWards() {
		// TODO Auto-generated method stub
		return wardDal.GetAllWards();
	}

	@Override
	public List<SubCounty> GetActiveSubCounties() {
		// TODO Auto-generated method stub
		return wardDal.GetActiveSubCounties();
	}

	public List<Ward> gtWardBySubCounty(int subcountyId) {
		// TODO Auto-generated method stub
		return wardDal.gtWardBySubCounty(subcountyId);
	}


}
