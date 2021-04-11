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

import com.compulynx.erevenue.bal.impl.ServiceBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Params;
import com.compulynx.erevenue.models.Service;


/**
 * @author Anita
 *
 */
@Component
@Path("/service")
public class ServiceSvc {
	
	@Autowired
	ServiceBalImpl serviceBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtServices/")
	public Response GetServices() {
		List<Service> services = serviceBal.GetAllServices();
		return Response.status(200).entity(services).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtParams/")
	public Response GetParams() {
		List<Params> params = serviceBal.GetAllParams();
		return Response.status(200).entity(params).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updService")
	public Response UpdateService(Service service) {
		ErevenueResponse response = serviceBal.UpdateService(service);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSubServices/{parentServiceId}")
	public Response GetSubServices(@PathParam("parentServiceId") int parentServiceId) {
		 List<Service> services = serviceBal.GetSubServiceById(parentServiceId);
		return Response.status(200).entity(services).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updParam")
	public Response UpdateParam(Params param) {
		ErevenueResponse response = serviceBal.UpdateParam(param);
		return Response.status(200).entity(response).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSubServices/")
	public Response GetSubServices() {
		List<Service> services = serviceBal.GetAllSubServices();
		return Response.status(200).entity(services).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updSer")
	public Response UpdateSer(Service service) {
		ErevenueResponse response = serviceBal.UpdateSerPrice(service);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMarketServices/{marketId}")
	public Response GetMarketServices(@PathParam("marketId") int marketId) {
		List<Service> services = serviceBal.GetMarketSubServices(marketId);
		return Response.status(200).entity(services).build();
	}
}
