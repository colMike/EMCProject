/**
 * 
 */
package com.compulynx.erevenue.bo.svc;

import com.compulynx.erevenue.bal.impl.PermitTypeBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.PermitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Anita
 * @date Apr 16, 2016
 *
 */
@Component
@Path("/permittype")
public class PermitTypeSvc {
	@Autowired
	PermitTypeBalImpl permitTypeBal;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtPermitTypes/")
	public Response GetPermitTypes() {
			List<PermitType> permittypes = permitTypeBal.GetAllPermitTypes();
			return Response.status(200).entity(permittypes).build();
	}


	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updPermitType")
	public Response UpdatePermitType(PermitType permitType) {
		ErevenueResponse response = permitTypeBal.UpdatePermitType(permitType);
		return Response.status(200).entity(response).build();
	}
}
