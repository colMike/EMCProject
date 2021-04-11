/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;






import com.compulynx.erevenue.bal.LoginBal;
import com.compulynx.erevenue.dal.impl.LoginDalImpl;
import com.compulynx.erevenue.models.ConfigParams;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LoginSession;
import com.compulynx.erevenue.models.LoginUser;


/**
 * @author Anita
 *
 */
@Component
public class LoginBalImpl implements LoginBal{
	@Autowired
	LoginDalImpl loginDal;
	
	public LoginUser ValidateManualLogin(String userName, String password) {
		return loginDal.GetUserIdManual(userName, password);
	}

	@Override
	public LoginSession GetUserAssgnRightsList(int userId) {
		// TODO Auto-generated method stub
		return loginDal.GetUserAssgnRightsList(userId);
	}

	@Override
	public List<ConfigParams> GetConfigParams() {
		// TODO Auto-generated method stub
		return loginDal.GetConfigParams();
	}

	@Override
	public ErevenueResponse UpdateConfigParam(ConfigParams params) {
		// TODO Auto-generated method stub
		return loginDal.UpdateConfigParam(params);
	}

	
	
}
