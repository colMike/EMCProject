/**
 * 
 */
package com.compulynx.erevenue.bal;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.PermitType;

import java.util.List;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public interface PermitTypeBal {
	ErevenueResponse UpdatePermitType(PermitType permitType);

	List<PermitType> GetAllPermitTypes();
}
