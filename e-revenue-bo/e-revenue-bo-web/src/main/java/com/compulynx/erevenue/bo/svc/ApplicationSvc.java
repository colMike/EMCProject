/**
 * 
 */
package com.compulynx.erevenue.bo.svc;

import com.compulynx.erevenue.bal.impl.ApplicationBalImpl;
import com.compulynx.erevenue.models.*;
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
@Path("/application")
public class ApplicationSvc {

	@Autowired
	ApplicationBalImpl applicationBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtApps")
	public Response GetAllApplications() {
		List<Application> apps = applicationBal.GetAllApplications();
		return Response.status(200).entity(apps).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtWardsBySubCounty/{subCountyId}")
	public Response GetWardsBySubCounty(
			@PathParam("subCountyId") int subCountyId) {
		List<Ward> apps = applicationBal.GetWardsBySubCounty(subCountyId);
		return Response.status(200).entity(apps).build();
	}//

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMarketsByWard/{wardId}")
	public Response GetMarketsByWard(@PathParam("wardId") int wardId) {
		List<Market> apps = applicationBal.GetMarketsByWard(wardId);
		return Response.status(200).entity(apps).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAppsByLinkId/{linkId},{nationalIdNo},{agentId}")
	public Response GetAllAppsByLinkId(@PathParam("linkId") int linkId,
			@PathParam("nationalIdNo") String nationalIdNo,
			@PathParam("agentId") int agentId) {
		List<Application> apps = applicationBal.GetAllAppsByLinkId(linkId,
				nationalIdNo, agentId);
		return Response.status(200).entity(apps).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updApp")
	public Response UpdateApplication(Application app) {
		ErevenueResponse response = applicationBal.UpdateApplication(app);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActivePermitTypes/{permitType}")
	public Response GetActivePermitTypes(
			@PathParam("permitType") String permitType) {
		List<PermitType> permittypes = applicationBal
				.GetActivePermitTypes(permitType);
		return Response.status(200).entity(permittypes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtInvoices")
	public Response GetAllInvoices() {
		List<Application> invoices = applicationBal.GetAllInvoices();
		return Response.status(200).entity(invoices).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updInvoiceStatus")
	public Response UpdateInvocieStatus(Application app) {
		ErevenueResponse response = applicationBal.UpdateInvocieStatus(app);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtPermits")
	public Response GetPermits() {
		List<Application> apps = applicationBal.GetAllPermits();
		return Response.status(200).entity(apps).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updQrImagePath")
	public Response UpdateQrImagePath(Application app) {
		ErevenueResponse response = applicationBal.UpdateQrImagePath(app);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updPermitRenew")
	public Response UpdatePermitRenewal(Application app) {
		ErevenueResponse response = applicationBal.UpdatePermitRenewal(app);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updPermitStatus")
	public Response UpdatePermitStatus(Application app) {
		ErevenueResponse response = applicationBal.UpdatePermitStatus(app);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtPermitsByDate")
	public Response GetPermits(Reports permit) {
		List<Application> apps = applicationBal.GetPermitsByDate(permit);
		return Response.status(200).entity(apps).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtInvoicesByLinkId/{linkId},{nationalIdNo},{agentId}")
	public Response GetAllInvoicesByLinkId(@PathParam("linkId") int linkId,
			@PathParam("nationalIdNo") String nationalIdNo,
			@PathParam("agentId") int agentId) {
		List<Application> invoices = applicationBal.GetAllInvoicesByLinkId(
				linkId, nationalIdNo, agentId);
		return Response.status(200).entity(invoices).build();
	}
        
                @GET
                @Produces(MediaType.APPLICATION_JSON)
                @Path("/getPrices/{id}")
                public Response getPrice(@PathParam("id")  int id)
                {
                    double price    =   applicationBal.getItemPrice(id);
                    
                    return Response.ok(200).entity(price).build();
                }
}
