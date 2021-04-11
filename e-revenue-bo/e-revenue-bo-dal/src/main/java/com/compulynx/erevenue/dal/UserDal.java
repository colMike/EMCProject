/**
 * 
 */
package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;
import com.compulynx.erevenue.models.UserGroup;
import com.compulynx.erevenue.models.UserType;

/**
 * @author Anita
 *
 */
public interface UserDal {
	boolean checkUserName(String userName);

	ErevenueResponse UpdateUser(User user);

	List<User> GetAllUsers();

	List<UserGroup> GetActiveGroups();

	List<UserType> GetUserTypes();
}
