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

import com.compulynx.erevenue.bal.impl.ReportsBalImpl;
import com.compulynx.erevenue.models.Agent;
import com.compulynx.erevenue.models.Application;
import com.compulynx.erevenue.models.CompasResponse;
import com.compulynx.erevenue.models.PermitType;
import com.compulynx.erevenue.models.Reports;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.ZDetails;

@Component
@Path("/report")
public class ReportSvc {

	@Autowired
	ReportsBalImpl reportBal;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtUserTxns")
	public Response GetUserTxns(Reports report) {
		List<Reports> userTxns=reportBal.GetAllUserTxn();
		return Response.status(200).entity(userTxns).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtUserTxnDetail")
	public Response GetUserTxnDetails(Reports txnDetails) {
		List<Reports> userTxns=reportBal.GetUserTxnsDetails(txnDetails);
		return Response.status(200).entity(userTxns).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDeviceTxns")
	public Response GetDeviceTxns() {
		List<Reports> userTxns=reportBal.GetAllDeviceTxn();
		return Response.status(200).entity(userTxns).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtDeviceTxnDetail")
	public Response GetDeviceTxnDetails(Reports txnDetails) {
		List<Reports> userTxns=reportBal.GetDeviceTxnsDetails(txnDetails);
		return Response.status(200).entity(userTxns).build();
	}
	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtGateTxns")
//	public Response GetGateTxns() {
//		List<Reports> userTxns=reportBal.GetAllGateTxn();
//		return Response.status(200).entity(userTxns).build();
//	}
//	
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Path("/gtGateTxnDetail")
//	public Response GetGateTxnDetails(Reports txnDetails) {
//		List<Reports> userTxns=reportBal.GetGateTxnsDetails(txnDetails);
//		return Response.status(200).entity(userTxns).build();
//	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllTxnDetail")
	public Response GetAllTxnDetails(Reports txnDetails) {
		List<Reports> userTxns=reportBal.GetAllTxnsDetails(txnDetails);
		return Response.status(200).entity(userTxns).build();
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtZDetails")
	public Response GetZDetails(ZDetails zDetails) {
		List<ZDetails> zList=reportBal.GetZDetails(zDetails);
		return Response.status(200).entity(zList).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/permits")
	public Response getPermits(Reports permitDetails) {
		List<Application> permits = null;
		return Response.status(200).entity(permits).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtCurrTxnDetail")
	public Response GetCurrentTxnDetails() {
		List<Reports> userTxns=reportBal.GetCurrentTxnsDetails();
		return Response.status(200).entity(userTxns).build();
	}
	
//	//service wiser report
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtParentSer")
//	public Response GetParentServices() {
//		List<Service> services=reportBal.GetParentServices();
//		return Response.status(200).entity(services).build();
//	}
//	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtSubSer/{parentServiceId}")
//	public Response GetSUbServices(@PathParam("parentServiceId") int parentServiceId) {
//		List<Service> services=reportBal.GetSubServices(parentServiceId);
//		return Response.status(200).entity(services).build();
//	}
//	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/gtServiceTxns")
//	public Response GetServiceTxns() {
//		List<Reports> userTxns=reportBal.GetAllServiceTxn();
//		return Response.status(200).entity(userTxns).build();
//	}
//	
//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Path("/gtServiceTxnDetail")
//	public Response GetServiceTxnDetails(Reports txnDetails) {
//		List<Reports> userTxns=reportBal.GetServiceTxnsDetails(txnDetails);
//		return Response.status(200).entity(userTxns).build();
//	}
}
