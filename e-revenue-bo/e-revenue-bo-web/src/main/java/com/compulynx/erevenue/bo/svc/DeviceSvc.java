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

import com.compulynx.erevenue.bal.impl.DeviceBalImpl;
import com.compulynx.erevenue.models.Device;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;

@Component
@Path("/device")
public class DeviceSvc {
	@Autowired
	DeviceBalImpl deviceBal;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDevices")
	public Response GetDevices() {
			List<Device> devices = deviceBal.GetAllDevicesInfo();
			return Response.status(200).entity(devices).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveUsers")
	public Response GetActiveUsers() {
			List<User> users = deviceBal.GetActiveUsers();
			return Response.status(200).entity(users).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAllActiveUsers")
	public Response GetAllActiveUsers() {
			List<User> users = deviceBal.GetAllActiveUsers();
			return Response.status(200).entity(users).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveDevices")
	public Response GetActiveDevices() {
			List<Device> devices = deviceBal.GetActiveDevicesInfo();
			return Response.status(200).entity(devices).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updDevice")
	public Response UpdateDevice(Device device) {
		ErevenueResponse response = deviceBal.UpdateDeviceInfo(device);
			return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtIssueDevices")
	public Response GetIssueDevices() {
			List<Device> issueDevices = deviceBal.GetAllIssueDevicesInfo();
			return Response.status(200).entity(issueDevices).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updIssueDevice")
	public Response UpdateIssueDevice(Device device) {
			ErevenueResponse response = deviceBal.UpdateIssueDeviceInfo(device);
			return Response.status(200).entity(response).build();
	}
	
}
