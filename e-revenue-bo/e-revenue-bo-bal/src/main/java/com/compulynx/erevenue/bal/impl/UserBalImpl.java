/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.dal.impl.UserDalImpl;
import com.compulynx.erevenue.bal.UserBal;
import com.compulynx.erevenue.bal.UserGroupBal;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;
import com.compulynx.erevenue.models.UserGroup;
import com.compulynx.erevenue.models.UserType;

/**
 * @author Anita
 *
 */
@Component
public class UserBalImpl implements UserBal{
	@Autowired
	UserDalImpl userDal;

	
	@Override
	public ErevenueResponse UpdateUser(User user) {
		// TODO Auto-generated method stub
		return userDal.UpdateUser(user);
	}

	@Override
	public List<User> GetAllUsers() {
		// TODO Auto-generated method stub
		return userDal.GetAllUsers();
	}

	@Override
	public List<UserGroup> GetActiveGroups() {
		// TODO Auto-generated method stub
		return userDal.GetActiveGroups();
	}

	@Override
	public List<UserType> GetUserTypes() {
		// TODO Auto-generated method stub
		return userDal.GetUserTypes();
	}


}
