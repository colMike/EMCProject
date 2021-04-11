/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import com.compulynx.erevenue.bal.PermitTypeBal;
import com.compulynx.erevenue.dal.impl.PermitTypeDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.PermitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
@Component
public class PermitTypeBalImpl implements PermitTypeBal{
	
	@Autowired
	PermitTypeDalImpl permitTypeDal;


	@Override
	public ErevenueResponse UpdatePermitType(PermitType permitType) {
		// TODO Auto-generated method stub
		return permitTypeDal.UpdatePermitType(permitType);
	}

	@Override
	public List<PermitType> GetAllPermitTypes() {
		// TODO Auto-generated method stub
		return permitTypeDal.GetAllPermitTypes();
	}



}
