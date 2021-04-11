package com.compulynx.erevenue.bo.svc;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compulynx.erevenue.bal.impl.DashBoardBalImpl;
import com.compulynx.erevenue.models.DashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/dashBoard")
public class DashBoardSvc {
	@Autowired
	DashBoardBalImpl dashBoardBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCountDetail")
	public Response GetDashBoardCountDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardCountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtFlowChartDetail")
	public Response GetFlowChartDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetFlowChartCountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAmountDetail")
	public Response GetDashBoardAmountDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardAmountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtTransChartDetail")
	public Response GetTransChartDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetTransChartDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMonthStatisticDetail")
	public Response GetMonthStatisticsDetail() {
		try {
			DashBoard detail = dashBoardBal.GetMonthStatisticsDetails();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUserStatisticsDetail")
	public Response GetUserStatisticsDetail() {
		try {
			DashBoard detail = dashBoardBal.GetUserStatisticsDetails();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCollectionDetail")
	public Response GetCollectionDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardCollectionDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}
