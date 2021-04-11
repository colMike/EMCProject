package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.RightsDetail;
import com.compulynx.erevenue.models.UserGroup;

public interface UserGroupDal {

ErevenueResponse UpdateUserGroup (UserGroup group);
	
	boolean checkGroupByName (String groupName);
	
	List<UserGroup> GetUserGroups ();
	
	List<RightsDetail> GetRights ();
}
