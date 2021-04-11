package com.compulynx.erevenue.bo.svc;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.compulynx.erevenue.bal.impl.LandRateBalImpl;
import com.compulynx.erevenue.models.CompasResponse;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LandRate;
import com.compulynx.erevenue.models.Reports;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Component
@Path("/landrate")
public class LandRateSvc {
	@Autowired
	LandRateBalImpl landrateBal;
	
	String TOMCAT_HOME = "";
	String fileName = "";
	String PATH = "";
	CompasResponse response = null;
	
	@POST
	@Path("/uploadPlotRentDetails/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFilePlot(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("createdBy") String createdBy, @FormDataParam("branchId") String branchId,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		TOMCAT_HOME = System.getProperty("catalina.base");
		fileName = fileDetail.getFileName();
		PATH = TOMCAT_HOME + "/plotrent_uploads/";
		//logger.info("Uploading file now Backend##" + PATH + fileName);
		String uploadedFileLocation = PATH + fileName;
		if(!(fileName.trim().split("\\.")[1].equalsIgnoreCase("xlsx")) || fileName.length() ==0){
			return Response.status(200).entity(new CompasResponse(201,"Wrong file format uploaded, Please try again")).build();
		}
		// save it
		if (writeToFile(uploadedInputStream, uploadedFileLocation)) {

			response = new CompasResponse();

			response = landrateBal.UploadPlot(uploadedFileLocation, fileName, createdBy, Integer.parseInt(branchId));
		} else {
			return Response.status(200).entity(new CompasResponse(201, "Error uploading file, Please try again"))
					.build();
		}
		return Response.status(200).entity(response).build();
	}
	@POST
	@Path("/uploadLandRateDetails/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFileLand(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("createdBy") String createdBy, @FormDataParam("branchId") String branchId,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		TOMCAT_HOME = System.getProperty("catalina.base");
		fileName = fileDetail.getFileName();
		PATH = TOMCAT_HOME + "/landrate_uploads/";
		//logger.info("Uploading file now Backend##" + PATH + fileName);
		String uploadedFileLocation = PATH + fileName;
		if(!(fileName.trim().split("\\.")[1].equalsIgnoreCase("xlsx")) || fileName.length() ==0){
			return Response.status(200).entity(new CompasResponse(201,"Wrong file format uploaded, Please try again")).build();
		}
		System.out.println("============here1=================");
		// save it
		if (writeToFile(uploadedInputStream, uploadedFileLocation)) {

			response = new CompasResponse();

			response = landrateBal.UploadLand(uploadedFileLocation, fileName, createdBy, Integer.parseInt(branchId));
		} else {
			return Response.status(200).entity(new CompasResponse(201, "Error uploading file, Please try again"))
					.build();
		}
		return Response.status(200).entity(response).build();
	}
	
	private boolean writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		//logger.info("#Writing to file function##");
		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();

			//logger.info("##Sucessfully written to file");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		//logger.info("##Failed to write to file");
		return false;
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtRegs")
	public Response GetAllRegs(){
		List<LandRate> lr = landrateBal.GetAllRegs();
		return Response.status(200).entity(lr).build();
		
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updRegistration")
	public Response CreateRegistrationForm(LandRate lr) {
		ErevenueResponse response = landrateBal.CreateRegistrationForm(lr);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtlandInvoices")
	public Response GetAllLandInvoices() {
			List<LandRate> landinvoices = landrateBal.GetAllLandInvoices();
			return Response.status(200).entity(landinvoices).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updlandInvoiceStatus")
	public Response UpdateLandInvocieStatus(LandRate lr) {
		ErevenueResponse response = landrateBal.UpdateLandInvoiceStatus(lr);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtPaidlands")
	public Response GetPaidlands() {
			List<LandRate> lr = landrateBal.GetPaidLandDetails();
			return Response.status(200).entity(lr).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtPermitlands")
	public Response GetPermitlands() {
			List<LandRate> lr = landrateBal.GetAllLandPermits();
			return Response.status(200).entity(lr).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updLandRenew")
	public Response UpdateLandPermitRenewal(LandRate lr) {
		ErevenueResponse response = landrateBal.UpdateLandPermitRenewal(lr);
		return Response.status(200).entity(response).build();
	}
	/*
	 * added by cyrus
	 */
	@POST
	 @Produces(MediaType.APPLICATION_JSON)
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Path("/gtLandRatesByDate")
	 public Response GetAllRegs(Reports lr) {
	   List<LandRate> lrates = landrateBal.GetLandRatesByDate(lr);
	   return Response.status(200).entity(lrates).build();
	 }
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLandsByLinkId/{linkId},{nationalIdNo},{agentId}")
	public Response GetAllLandsByLinkId(@PathParam("linkId") int linkId,@PathParam("nationalIdNo") String nationalIdNo,@PathParam("agentId") int agentId) {
			List<LandRate> lrs = landrateBal.GetAllLandsByLinkId(linkId, nationalIdNo,agentId);
			return Response.status(200).entity(lrs).build();
	}
	
                  @GET
                  @Produces(MediaType.APPLICATION_JSON)
                  @Path("/gtLandRate/{landId}")
                  public Response GetLandFee(@PathParam("landId") int landId){
                      double amount = landrateBal.GetLandFee(landId);
                      
                      return Response.status(200).entity(amount).build();
                  }
                  
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLandInvoicesByLinkId/{linkId},{nationalIdNo},{agentId}")
	public Response GetAllLandInvoicesByLinkId(@PathParam("linkId") int linkId,@PathParam("nationalIdNo") String nationalIdNo,@PathParam("agentId") int agentId) {
			List<LandRate> lrs = landrateBal.GetAllLandInvoicesByLinkId(linkId, nationalIdNo,agentId);
			return Response.status(200).entity(lrs).build();
	}
}
