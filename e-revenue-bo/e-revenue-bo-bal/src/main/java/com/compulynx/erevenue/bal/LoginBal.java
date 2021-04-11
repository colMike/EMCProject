/**
 * 
 */
package com.compulynx.erevenue.bal;

import java.util.List;




import com.compulynx.erevenue.models.ConfigParams;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LoginSession;
import com.compulynx.erevenue.models.LoginUser;


/**
 * @author Anita
 *
 */
public interface LoginBal {

	LoginUser ValidateManualLogin(String userName, String password);
	
	LoginSession GetUserAssgnRightsList(int userId);
	
	List<ConfigParams> GetConfigParams();

	ErevenueResponse UpdateConfigParam(ConfigParams params);
	
}
