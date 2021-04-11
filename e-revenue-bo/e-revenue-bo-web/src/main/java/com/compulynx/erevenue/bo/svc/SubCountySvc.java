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

import com.compulynx.erevenue.bal.impl.SubCountyBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;


/**
 * @author Anita
 * @date Apr 12, 2016
 */
@Component
@Path("/subcounty")
public class SubCountySvc {
	@Autowired
	SubCountyBalImpl subCountyBal;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSubCounties")
	public Response GetSubCounties() {
			List<SubCounty> subCounties = subCountyBal.GetAllSubCounties();
			return Response.status(200).entity(subCounties).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updSubCounty")
	public Response UpdateSubCounty(SubCounty subCounty) {
		ErevenueResponse response = subCountyBal.UpdateSubCounty(subCounty);
		return Response.status(200).entity(response).build();
	}
}
