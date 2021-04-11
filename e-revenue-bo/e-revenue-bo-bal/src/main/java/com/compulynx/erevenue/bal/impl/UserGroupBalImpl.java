/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.compulynx.erevenue.bal.UserGroupBal;
import com.compulynx.erevenue.dal.impl.UserGroupDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.RightsDetail;
import com.compulynx.erevenue.models.UserGroup;

/**
 * @author Anita
 *
 */
@Component
public class UserGroupBalImpl implements UserGroupBal{

	@Autowired
	UserGroupDalImpl userGroupDal;

	@Override
	public ErevenueResponse UpdateUserGroup(UserGroup group) {
		// TODO Auto-generated method stub
		return userGroupDal.UpdateUserGroup(group);
	}

	@Override
	public List<UserGroup> GetUserGroups() {
		// TODO Auto-generated method stub
		return userGroupDal.GetUserGroups();
	}

	@Override
	public List<RightsDetail> GetRights() {
		// TODO Auto-generated method stub
		return userGroupDal.GetRights();
	}
	
}
