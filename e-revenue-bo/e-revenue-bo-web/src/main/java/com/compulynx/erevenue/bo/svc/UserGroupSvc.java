/**
 * 
 */
package com.compulynx.erevenue.bo.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.bal.impl.UserGroupBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.RightsDetail;
import com.compulynx.erevenue.models.UserGroup;

/**
 * @author anita
 *
 */
@Component
@Path("/userGroups")
public class UserGroupSvc {
	
	@Autowired
	UserGroupBalImpl userGroupBal;
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGroups")
	public Response GetUserGroups() {
		
			List<UserGroup> userGroups = userGroupBal.GetUserGroups();
			return Response.status(200).entity(userGroups).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtRights")
	public Response GetRights() {
		
			List<RightsDetail> rights = userGroupBal.GetRights();
			return Response.status(201).entity(rights).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updGroup")
	public Response UpdateGroup(UserGroup group) {
		
			ErevenueResponse response = userGroupBal.UpdateUserGroup(group);
			return Response.status(200).entity(response).build();
	}
	
}
