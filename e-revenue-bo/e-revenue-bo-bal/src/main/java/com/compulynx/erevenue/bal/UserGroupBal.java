/**
 * 
 */
package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.RightsDetail;
import com.compulynx.erevenue.models.UserGroup;

/**
 * @author Anita
 *
 */
public interface UserGroupBal {
	
    ErevenueResponse UpdateUserGroup (UserGroup group);
    
	List<UserGroup> GetUserGroups ();
	
	List<RightsDetail> GetRights ();
}
