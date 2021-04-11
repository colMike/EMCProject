package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.bal.SubCountyBal;
import com.compulynx.erevenue.dal.impl.SubCountyDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;

/**
 * The Class SubCountyBalImpl.
 * @author Anita 
 * @date Apr 12, 2016
 */
@Component
public class SubCountyBalImpl implements SubCountyBal{
	
	@Autowired
	SubCountyDalImpl subCountyDal;
	@Override
	public ErevenueResponse UpdateSubCounty(SubCounty subCounty) {
		// TODO Auto-generated method stub
		return subCountyDal.UpdateSubCounty(subCounty);
	}

	@Override
	public List<SubCounty> GetAllSubCounties() {
		// TODO Auto-generated method stub
		return subCountyDal.GetAllSubCounties();
	}

}
