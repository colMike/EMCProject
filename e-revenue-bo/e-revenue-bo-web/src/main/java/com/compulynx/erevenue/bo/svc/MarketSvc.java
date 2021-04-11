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

import com.compulynx.erevenue.bal.impl.MarketBalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.Ward;

/**
 * @author Anita
 * @date Apr 13, 2016
 *
 */
@Component
@Path("/market")
public class MarketSvc {
	@Autowired
	MarketBalImpl marketBal;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMarkets")
	public Response GetMarkets() {
			List<Market> markets = marketBal.GetAllMarkets();
			return Response.status(200).entity(markets).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveWards")
	public Response GetActiveWards() {
			List<Ward> wards = marketBal.GetActiveWards();
			return Response.status(200).entity(wards).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updMarket")
	public Response UpdateMarket(Market market) {
		ErevenueResponse response = marketBal.UpdateMarket(market);
		return Response.status(200).entity(response).build();
	}
	
	
}
