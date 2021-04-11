/**
 * 
 */
package com.compulynx.erevenue.bo.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compulynx.erevenue.bal.impl.UserBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;
import com.compulynx.erevenue.models.UserGroup;
import com.compulynx.erevenue.models.UserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Anita
 * @date May 11,2016
 */

@Component
@Path("/user")
public class UserSvc {

	@Autowired
	UserBalImpl userBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUsers/")
	public Response GetUsers() {
		List<User> users = userBal.GetAllUsers();
		return Response.status(200).entity(users).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updUser")
	public Response UpdateUser(User user) {
		ErevenueResponse response = userBal.UpdateUser(user);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUserTypes/")
	public Response GetUserTypes() {
		List<UserType> userTypes = userBal.GetUserTypes();
		return Response.status(200).entity(userTypes).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveGroups/")
	public Response GetActiveGroups() {
		List<UserGroup> groups = userBal.GetActiveGroups();
		return Response.status(200).entity(groups).build();
	}
	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtUserById/{userId}")
//	public Response GetUserById(@PathParam("userId") int userId) {
//		try {
//			User user = userBal.GetUserById(userId);
//			if (!(user == null)) {
//				return Response.status(200).entity(user).build();
//			} else {
//				return Response.status(201).entity(null).build();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return Response.status(404).entity(null).build();
//		}
//	}
}
