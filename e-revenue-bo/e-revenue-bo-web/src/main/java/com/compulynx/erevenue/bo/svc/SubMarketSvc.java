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

import com.compulynx.erevenue.bal.impl.SubMarketBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.SubMarket;

/**
 * @author Anita
 * @date Apr 14, 2016
 *
 */
@Component
@Path("/submarket")
public class SubMarketSvc {
	@Autowired
	SubMarketBalImpl subMarketBal;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSubMarkets")
	public Response GetSubMarkets() {
			List<SubMarket> submarkets = subMarketBal.GetAllSubMarkets();
			return Response.status(200).entity(submarkets).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveMarkets")
	public Response GetActiveMarkets() {
			List<Market> markets = subMarketBal.GetActiveMarkets();
			return Response.status(200).entity(markets).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updSubMarket")
	public Response UpdateMarket(SubMarket submarket) {
		ErevenueResponse response = subMarketBal.UpdateSubMarket(submarket);
		return Response.status(200).entity(response).build();
	}
}
