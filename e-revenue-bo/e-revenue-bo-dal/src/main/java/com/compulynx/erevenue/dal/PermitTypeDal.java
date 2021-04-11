package com.compulynx.erevenue.dal;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.PermitType;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface PermitTypeDal.
 * @author Anita
 * @date Apr 14, 2016 
 */
public interface PermitTypeDal {

	ErevenueResponse UpdatePermitType(PermitType permitType);

	List<PermitType> GetAllPermitTypes();

}
