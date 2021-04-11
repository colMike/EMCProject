package com.compulynx.erevenue.bo.servlet;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.dal.operations.Utility;
import com.compulynx.erevenue.models.CompasProperties;
import com.compulynx.erevenue.models.CompasResponse;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.mysql.jdbc.Statement;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;







import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.mysql.jdbc.Statement;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servlet implementation class Reports
 */
@Component
public class Reports extends HttpServlet {

	private static final long serialVersionUID = 1L;
	//private static String PATH = "/home/erevenueuser/erevenue/apache-tomcat-7.0.55/bin/erevenue_reports";
//	private static String PATH = "emc_reports";
	private static final Logger logger = Logger.getLogger(Reports.class
			.getName());
	String fileName = "";
	String PATH = "";
	CompasResponse response = null;
	/**
	 * Default constructor.
	 */
	public Reports() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private DataSource dataSource;
	private Connection connection;
	private Statement statement;

	public void init() throws ServletException {
		try {
			// Get DataSource
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/erevenueDS");

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	// public Connection getDbConnection() {
	// Connection connection = null;
	// try {
	// //dataSource =
	// (DataSource)ServletActionContext.getServletContext().getAttribute("ds");
	// connection = dataSource.getConnection();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return connection;
	// }

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

//		System.out.println(System.getenv("windir").split(":")[0]
//				+ ":\\reports\\");

                                    logger.info("testing paths");

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		JasperDesign jasperDesign = null;
		Map<String, String> parameters = new HashMap();
		ServletOutputStream outstream = null;
		Connection connection = null;
		String reportType = request.getParameter("type");
		String exportType = request.getParameter("eType");
		CompasProperties compasProperties = new CompasProperties();
		Utility util = new Utility();
		String param = "report.filepath";
		compasProperties = util.getCompasProperties(param);
		logger.info("Done Reading Props File##"
				+ compasProperties.bnfUploadFilePath);
		// TOMCAT_HOME = System.getProperty("catalina.base");
		// fileName = fileDetail.getFileName();
		PATH = compasProperties.bnfUploadFilePath;
		logger.info("Reports File Path##" + PATH);
		// String catalinaPath=System.getProperty("catalina.base");
		// String
		// reportPath=System.getenv("windir").split(":")[0]+":\\fkfreports\\";
		try {
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) context.lookup("jdbc/erevenueDS");
			connection = ds.getConnection();

			if (reportType.equalsIgnoreCase("I")) {
				String businessNo = request.getParameter("businessNo");
				String approvedUser = request.getParameter("approvedUser");
				String year = request.getParameter("year");
				parameters.put("businessNo", businessNo);
				parameters.put("approvedUser", approvedUser);
				parameters.put("year", year);
				System.out.println("calling servlet## Invoice Pdf");


				jasperDesign = JRXmlLoader
						.load(PATH+"RptInvoice.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}

			}
			if (reportType.equalsIgnoreCase("P")) {

				String permitNo = request.getParameter("permitNo");
				// String fee = request.getParameter("fee");
				parameters.put("permitNo", permitNo);

				jasperDesign = JRXmlLoader
						.load(PATH+"RptPermit.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);
					ErevenueResponse response2 = UpdatePermitStatus(permitNo,
							"PR");
					if (response2.respCode == 200) {
						Response.status(200).entity(response2).build();
						return;
					} else {
						Response.status(201).entity(response2).build();
						return;
					}

				} else {
					System.out.println("No data");
				}

			}

			if (reportType.equalsIgnoreCase("R")) {
				String businessNo = request.getParameter("businessNo");
				String paidUser = request.getParameter("paidUser");
				String year = request.getParameter("year");
				parameters.put("businessNo", businessNo);
				parameters.put("paidUser", paidUser);
				parameters.put("year", year);
				
				System.out.println("-------------------"+businessNo);
				System.out.println("-------------------"+paidUser);
				System.out.println("-------------------"+year);
				System.out.println("calling servlet## Invoice Receipt Pdf");
				jasperDesign = JRXmlLoader
						.load(PATH+"RptInvoiceReceipt.jrxml");

				
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

		

					if (jasperPrint.getPages().size() != 0) {

						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}

				

			}
			if (reportType.equalsIgnoreCase("LRInv")) {
				String plotNumber = request.getParameter("plotNumber");
				String approvedUser = request.getParameter("approvedUser");
				String year = request.getParameter("year");
				parameters.put("plotNumber", plotNumber);
				parameters.put("approvedUser", approvedUser);
				parameters.put("year", year);

				System.out.println("calling servlet## LandInvoice Pdf");
				jasperDesign = JRXmlLoader
						.load(PATH+"RptLandRateInvoice.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}

			}
	
			if (reportType.equalsIgnoreCase("LRRec")) {
				String plotNumber = request.getParameter("plotNumber");
				String paidUser = request.getParameter("paidUser");
				String year = request.getParameter("year");
				parameters.put("plotNumber", plotNumber);
				parameters.put("paidUser", paidUser);
				parameters.put("year", year);

				System.out.println("calling servlet##Land Invoice Receipt Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"RptLandReceipt.jrxml");

					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {

						if (jasperPrint.getPages().size() != 0) {

							byte[] pdfasbytes = JasperExportManager
									.exportReportToPdf(jasperPrint);
							outstream = response.getOutputStream();
							response.setContentType("application/pdf");
							response.setContentLength(pdfasbytes.length);
							outstream.write(pdfasbytes, 0, pdfasbytes.length);

						} else {
							System.out.println("No data");
						}

					}

				}
			//landRates Report
			if (reportType.equalsIgnoreCase("LRP")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String subcounty = request.getParameter("subcounty");
			
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				// String memberNo = request.getParameter("MemNo");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("subcounty", subcounty);
				// parameters.put("MemNo", memberNo);
				//parameters.put("dtFrm", dtFrm.print(from).toString());
				//parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet##Transaction Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rptLandRate.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rptLandRate_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
									
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "Landrate report"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("TX")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String subcounty=request.getParameter("subcounty");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				// String memberNo = request.getParameter("MemNo");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("subcounty", subcounty);
				// parameters.put("MemNo", memberNo);
				//parameters.put("dtFrm", dtFrm.print(from).toString());
				//parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet##Transaction Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_transaction_details.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_transaction_details_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
									
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "TransactionDetails"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("S")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String ward=request.getParameter("ward");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("yyyy-MM-dd");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("ward", ward);
//				parameters.put("dtFrm", dtFrm.print(from).toString());
//				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet##Service Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_service_summary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_service_summary_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "ServiceWiseSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("M")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet##Market Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_marketwise_summary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet##Market Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_marketwise_summary_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "MarketWiseSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("D")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet## Device Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_device_wise_summary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_device_wise_summary_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "DeviceWiseSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("U")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet##User Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_userwise_summary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_userwise_summary_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "UserWiseSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("W")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet## Ward Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_wardwise_summary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_wardwise_summary_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "WardWiseSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("SB")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet## Sub county Summary Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_subcountywise_summary.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_subcountywise_summary_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "SubCountyWiseSummary"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
				
			}
			if (reportType.equalsIgnoreCase("UX")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String user =request.getParameter("UserId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTimeFormatter dtFrm = DateTimeFormat
						.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("dtFrm", dtFrm.print(from).toString());
				parameters.put("toFrm", dtFrm.print(to).toString());
				parameters.put("UserId", user);

				if (exportType.equalsIgnoreCase("P")) {
					System.out.println("calling servlet##User Transction Pdf");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_usertransaction_details.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				}else{
					System.out.println("calling servlet## User Transaction Excel");
					jasperDesign = JRXmlLoader
							.load(PATH+"rpt_usertransaction_details_xls.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
					       ByteArrayOutputStream baos = new ByteArrayOutputStream();
					                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					             exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					             exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill byte array output stream

					             exporter.exportReport();
					             
					             response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					             response.setHeader("Content-disposition", "attachment; filename=" + "UserTransactionDetails"+".xlsx");
					             response.setContentLength(baos.size());
					             response.getOutputStream().write(baos.toByteArray());      

					} else {
						System.out.println("No data");
					}
				}
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private JRXlsxExporter getCommonXlsxExporter() {
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE,
				Boolean.TRUE);

		// exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
		// Boolean.TRUE);

		return exporter;
	}

	public ErevenueResponse UpdatePermitStatus(String permitNo, String status) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String qrImage = "";
		BufferedImage image = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.updPermitStatus);

			preparedStatement.setString(1, status);
			// preparedStatement.setString(2, amountInWords);
			preparedStatement.setString(2, permitNo);

			return (preparedStatement.executeUpdate() > 0) ? new ErevenueResponse(
					200, "Records Updated") : new ErevenueResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}



}
