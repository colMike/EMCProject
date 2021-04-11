package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;
import com.compulynx.erevenue.models.UserGroup;
import com.compulynx.erevenue.models.UserType;

/**
 * @author anita
 *
 */
public interface UserBal {

	ErevenueResponse UpdateUser(User user);

	List<User> GetAllUsers();
	
	List<UserGroup> GetActiveGroups();

	List<UserType> GetUserTypes();
}
