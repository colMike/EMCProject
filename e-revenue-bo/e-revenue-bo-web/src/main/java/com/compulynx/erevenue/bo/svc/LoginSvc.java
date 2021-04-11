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







import com.compulynx.erevenue.bal.impl.LoginBalImpl;
import com.compulynx.erevenue.models.ConfigParams;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LoginSession;
import com.compulynx.erevenue.models.LoginUser;



import com.compulynx.erevenue.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/login")
public class LoginSvc {

	@Autowired
	LoginBalImpl loginBal;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/manualAuth")
	public Response ValidateManualLogin(User user) {
		try {
			LoginUser loginUser = loginBal.ValidateManualLogin(user.userName,user.userPwd);
			return Response.status(loginUser.respCode).entity(loginUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUserRights/{usrId}")
	public Response GetUserLoginSession(@PathParam("usrId") int usrId) {
		try {
			LoginSession loginSession = loginBal.GetUserAssgnRightsList(usrId);
			return Response.status(loginSession.respCode).entity(loginSession)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtConfig")
	public Response GetConfigParams() {
		List<ConfigParams> params = loginBal.GetConfigParams();
		return Response.status(200).entity(params).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updConfig")
	public Response UpdateDevice(ConfigParams config) {
		ErevenueResponse response = loginBal.UpdateConfigParam(config);
			return Response.status(200).entity(response).build();
	}
	
}
