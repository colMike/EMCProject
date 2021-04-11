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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.bal.impl.WardBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.SubCounty;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 12, 2016
 *
 */

@Component
@Path("/ward")
public class WardSvc {

	@Autowired
	WardBalImpl wardBal;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtWards")
	public Response GetWards() {
			List<Ward> wards = wardBal.GetAllWards();
			return Response.status(200).entity(wards).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveSubCounties")
	public Response GetActiveWards() {
			List<SubCounty> wards = wardBal.GetActiveSubCounties();
			return Response.status(200).entity(wards).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updWard")
	public Response UpdateWard(Ward ward) {
		ErevenueResponse response = wardBal.UpdateWard(ward);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtWardBySubCounty/{subcountyId}")
	public Response gtWardBySubCounty(@PathParam("subcountyId") int subcountyId) {
		List<Ward> wards = wardBal.gtWardBySubCounty(subcountyId);
		return Response.status(200).entity(wards).build();
	}
}
