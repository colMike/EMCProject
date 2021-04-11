/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import com.compulynx.erevenue.dal.ApplicationDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.dal.operations.Utility;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.sql.DataSource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.compulynx.erevenue.models.Application;
import com.compulynx.erevenue.models.CompasProperties;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.PermitType;
import com.compulynx.erevenue.models.PermitYear;
import com.compulynx.erevenue.models.Reports;
import com.compulynx.erevenue.models.Ward;
import com.mysql.jdbc.util.Base64Decoder;


/**
 * class to handle the functionality of sbp 
 * @author Anita
 * @date Apr 15, 2016
 *
 */
public class ApplicationDalImpl implements ApplicationDal {
	CompasProperties compasProperties = new CompasProperties();
	private DataSource dataSource;

	public ApplicationDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

        public double getItemPrice(int id)
                {
                    Connection connection   =   null;
                    PreparedStatement preparedStatement =   null;
                    ResultSet   resultSet   =   null;
                    try
                    {
                        connection  =   dataSource.getConnection();
                        preparedStatement   =   connection.prepareStatement(Queryconstants.getItemPrice);
                        preparedStatement.setInt(1, id);
                        resultSet   =   preparedStatement.executeQuery();
                        resultSet.next();
                        
                        return resultSet.getDouble("permit_fee");
                    }
                    catch(Exception ex)
                    {
                        return 0;
                    }
                     finally 
                    {
                        DBOperations.DisposeSql(connection, preparedStatement, resultSet);
	  }
                }
        
	/**
	 * Function to save the new sbp application and update the details for the same
	 * @return 200 on successful update,201 on failed ,202 on exception
	 * 
	 */
	@Override
	public ErevenueResponse UpdateApplication(Application app) 
	{
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int businessId = 0;
		int appId = 0;
		try 
		{
			connection = dataSource.getConnection();
			System.out.println(app.businessId +"===================");
			if (app.businessId == 0) 
			{

				if (checkBusinessName(app.businessName)) 
				{
					return new ErevenueResponse(201,
							"Business name already exists");
				}
				
				if (app.linkId == 4) 
				{
					if ((ValidateNationalId(app.nationalId)) == false) 
					{
						return new ErevenueResponse(201,
								"National id doesn't match with sign up user's national Id");
					}
				}

				preparedStatement = connection.prepareStatement(
						Queryconstants.insertApplication,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, app.businessName);
				preparedStatement.setInt(2, app.noOfEmployees);
				preparedStatement.setDouble(3, app.area);
				preparedStatement.setString(4, app.electricityAccNo);
				preparedStatement.setString(5, app.waterAccNo);
				preparedStatement.setString(6, app.regNo);
				preparedStatement.setInt(7, app.permitTypeId);
				preparedStatement.setString(8, app.businessDesc);
				preparedStatement.setString(9, app.postalAdd);
				preparedStatement.setString(10, app.postalCode);
				preparedStatement.setInt(11, app.wardId);
				preparedStatement.setString(12, app.landZone);
				preparedStatement.setString(13, app.plotNo);
				preparedStatement.setString(14, app.mobileNo);
				preparedStatement.setString(15, app.landLineNo);
				preparedStatement.setString(16, app.fax);
				preparedStatement.setString(17, app.email);
				preparedStatement.setBoolean(18, app.active);
				preparedStatement.setInt(19, app.createdBy);
				preparedStatement.setTimestamp(20, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(21, app.nationalId);
				preparedStatement.setString(22, app.applicant);
				preparedStatement.setInt(23, app.marketId);
				preparedStatement.setString(24, app.road);
				preparedStatement.setString(25, app.pinNumber);
				preparedStatement.setString(26, app.vatNumber);
				preparedStatement.setString(27, app.detailDesc);
				preparedStatement.setString(28, app.town);
				preparedStatement.setString(29, app.building);
				preparedStatement.setString(30, app.floor);
				preparedStatement.setString(31, app.room);
				preparedStatement.setString(32, app.appFee);
				preparedStatement.setString(33, app.conservancy_fee);
				if (preparedStatement.executeUpdate() > 0) 
				{
					Calendar calendarEnd = Calendar.getInstance();
					int year = calendarEnd.get(Calendar.YEAR);
					if (app.businessId == 0) 
					{
						rs = preparedStatement.getGeneratedKeys();
						rs.next();
						businessId = rs.getInt(1);
					}

					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateBusinessNo);
					preparedStatement.setString(1, GetBusinessNo(businessId));
					preparedStatement.setInt(2, businessId);
					
					if (preparedStatement.executeUpdate() > 0) 
					{
						preparedStatement1 = connection.prepareStatement(
								Queryconstants.insertApplicationDtl,
								Statement.RETURN_GENERATED_KEYS);
						preparedStatement1.setInt(1, businessId);
						preparedStatement1.setDouble(2, app.permitFee);
						preparedStatement1.setDouble(3, 0.00);
						preparedStatement1.setString(4, "New");
						preparedStatement1.setString(5, app.status);
						preparedStatement1.setInt(6, app.createdBy);
						preparedStatement1.setTimestamp(
								7,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement1.setString(8, "");
						preparedStatement1.setInt(9, year);
						
						if (preparedStatement1.executeUpdate() > 0) 
						{
							if (app.appId == 0) 
							{
								rs = preparedStatement1.getGeneratedKeys();
								rs.next();
								appId = rs.getInt(1);
							}
							
							DBOperations.DisposeSql(preparedStatement1);
							preparedStatement1 = connection.prepareStatement(Queryconstants.updateAppNo);
							preparedStatement1.setString(1,GetApplicationNo(appId));
							preparedStatement1.setInt(2, appId);
							
							if (preparedStatement1.executeUpdate() > 0) 
							{
								return new ErevenueResponse(200,
										"Records Updated");
							} 
							else 
							{
								return new ErevenueResponse(200,
										"Could not update record!");
							}
						}
					}
					
					return new ErevenueResponse(201, "Records Updated");
				}
				else
				{
					return new ErevenueResponse(200, "Could not update record!");
				}
				

			}
			else 
			{
				System.out.println(app.pinNumber + "ssssssssssssssssssss");
				if (checkBusinessNameById(app.businessName, app.businessId)) 
				{
					return new ErevenueResponse(201,"Business name already exists");
				}
				if (app.linkId == 4) {

					if ((ValidateNationalId(app.nationalId)) == false) {
						return new ErevenueResponse(201,
								"National id doesn't match with sign up user's national Id");
					}
				}
				
				
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateApplication);
				preparedStatement.setString(1, app.businessName);
				preparedStatement.setInt(2, app.noOfEmployees);
				preparedStatement.setInt(3, app.area);
				preparedStatement.setString(4, app.electricityAccNo);
				preparedStatement.setString(5, app.waterAccNo);
				preparedStatement.setString(6, app.regNo);
				preparedStatement.setInt(7, app.permitTypeId);
				preparedStatement.setString(8, app.businessDesc);
				preparedStatement.setString(9, app.postalAdd);
				preparedStatement.setString(10, app.postalCode);
				preparedStatement.setInt(11, app.wardId);
				preparedStatement.setString(12, app.landZone);
				preparedStatement.setString(13, app.plotNo);
				preparedStatement.setString(14, app.mobileNo);
				preparedStatement.setString(15, app.landLineNo);
				preparedStatement.setString(16, app.fax);
				preparedStatement.setString(17, app.email);
				preparedStatement.setBoolean(18, app.active);
				preparedStatement.setInt(19, app.createdBy);
				preparedStatement.setTimestamp(20, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(21, app.nationalId);
				preparedStatement.setString(22, app.applicant);
				preparedStatement.setInt(23, app.marketId);
				
				preparedStatement.setString(24, app.road);
				preparedStatement.setString(25, app.pinNumber);
				System.out.println(app.pinNumber + "ssssssssssssssssssss");
				preparedStatement.setString(26, app.vatNumber);
				preparedStatement.setString(27, app.detailDesc);
				preparedStatement.setString(28, app.town);
				preparedStatement.setString(29, app.building);
				preparedStatement.setString(30, app.floor);
				preparedStatement.setString(31, app.room);
				preparedStatement.setString(32, app.appFee);
				System.out.println(app.appFee + "app.appFee");
				
				preparedStatement.setString(33, app.conservancy_fee);
				preparedStatement.setInt(34, app.businessId);

				if (preparedStatement.executeUpdate() > 0) 
				{
					DBOperations.DisposeSql(preparedStatement);
					if (app.status.equalsIgnoreCase("N") || app.status.equalsIgnoreCase("RU")) 
					{
						try
						{
							preparedStatement1 = connection.prepareStatement(Queryconstants.updateAmendedStatus);
							preparedStatement1.setString(1, app.status);
							preparedStatement1.setInt(2, app.createdBy);
							preparedStatement1.setTimestamp(3,new java.sql.Timestamp(new java.util.Date().getTime()));
							preparedStatement1.setInt(4, app.businessId);
							
							if(preparedStatement1.executeUpdate() > 0)
							{
								return new ErevenueResponse(200, "Record updated successfully!");
							}
							else
							{
								return new ErevenueResponse(201, "Could not update record!");
							}
						}
						catch(SQLException sqlEx)
						{
							return new ErevenueResponse(201, "An exception occurred!");
						}
					}
					else
					{
						return new ErevenueResponse(201, String.valueOf(app.businessId)); 
					}
				}
				else
				{
					return new ErevenueResponse(201, "Could not update the record!");
				}
			}

		} catch (SQLException sqlEx) 
		{
			sqlEx.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");

		} catch (Exception ex) 
		{
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement1, resultSet);
		} 
	}
	
	/**
	 * checks the business name if exists at the time of editing the application details
	 * @param businessName
	 * @param businessId
	 * @return true is exists or false
	 */
	public boolean checkBusinessNameById(String businessName, int businessId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkBusinessNameById);
			preparedStatement.setString(1, businessName);
			preparedStatement.setInt(2, businessId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * generates the unique business number 
	 * @param businessId
	 * @return business number
	 */
	public String GetBusinessNo(int businessId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String businessNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getWardCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				businessNo = dateFormat.format(date)
						+ String.format("%06d", businessId);
			}
			return businessNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return businessNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * generates the unique application number
	 * @param appId
	 * @return application number
	 */
	public String GetApplicationNo(int appId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String businessNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "001");
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				System.out.println("appno###" + dateFormat.format(date)
						+ String.format("%06d", appId));
				businessNo = resultSet.getString("value")
						+ dateFormat.format(date)
						+ String.format("%06d", appId);
			}
			return businessNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return businessNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);

		}
	}

	/**
	 * generates the unique permit number
	 * @param appId
	 * @return permit number
	 */
	public String GetPermitNo(int appId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String permitNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			System.out.println("permitno###" + dateFormat.format(date) + appId);
			permitNo = dateFormat.format(date) + String.format("%06d", appId);

			return permitNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return permitNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);

		}
	}

	/**
	 * check if the business name is exists when inserting the new application
	 * @param businessName
	 * @return true if exists else false
	 */
	public boolean checkBusinessName(String businessName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkBusinessName);
			preparedStatement.setString(1, businessName);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * validates national-id for individual user when they creates the new application
	 * against the nationalid used at the time of signup
	 * @param nationalId
	 * @return true if exists else false
	 */
	public boolean ValidateNationalId(String nationalId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateNationalId);
			preparedStatement.setString(1, nationalId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get the active permit types
	 * @param permitType
	 * @return the list of active permits
	 */
	public List<PermitType> GetActivePermitTypes(String permitType) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActivePermitTypes);
			preparedStatement.setString(1,permitType);
			resultSet = preparedStatement.executeQuery();
			List<PermitType> types = new ArrayList<PermitType>();
			while (resultSet.next()) {
				types.add(new PermitType(resultSet.getInt("id"), resultSet
						.getString("permit_type_name")
						+ "--"
						+ "(Ksh. "
						+ resultSet.getDouble("permit_Fee") + ")"));
				count++;
			}
			return types;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
        
	/**
	 * get the details of all apllication created
	 * @return list of applications
	 */
	public List<Application> GetAllApplications() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
            System.out.println();
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllApplications);
			resultSet = preparedStatement.executeQuery();
			List<Application> apps = new ArrayList<Application>();
			while (resultSet.next()) {
				Application obj= new Application();
				obj.businessId=resultSet.getInt("ID");
				obj.businessName=resultSet.getString("business_name");
				obj.businessNo=resultSet.getString("business_No");
				obj.noOfEmployees=resultSet.getInt("no_of_employees");
				obj.permitTypeId=resultSet.getInt("permit_type_id");
				obj.permitFee=resultSet.getInt("permit_fee");
				obj.electricityAccNo=resultSet.getString("electricity_no");
				obj.waterAccNo=resultSet.getString("water_acc_no");
				obj.area=resultSet.getInt("area");
				obj.regNo=resultSet.getString("reg_no");
				obj.postalAdd=resultSet.getString("postal_add");
				obj.postalCode=resultSet.getString("postal_code");
				obj.email=resultSet.getString("email");
				obj.fax=resultSet.getString("fax");
				obj.mobileNo=resultSet.getString("mobile_no");
				obj.landLineNo=resultSet.getString("land_line_no");
				obj.room=resultSet.getString("room");
				obj.road=resultSet.getString("road");
				obj.businessDesc=resultSet.getString("business_desc");
				obj.pinNumber=resultSet.getString("pinNumber");
				obj.vatNumber=resultSet.getString("vatNumber");
				obj.detailDesc=resultSet.getString("detailDesc");
				obj.town=resultSet.getString("town");
				obj.building=resultSet.getString("building");
				
				obj.floor=resultSet.getString("floor");
				obj.wardId=resultSet.getInt("ward_id");
				obj.landZone=resultSet.getString("land_zone");
				obj.plotNo=resultSet.getString("plot_no");
				obj.active=resultSet.getBoolean("active");
				obj.status=resultSet.getString("invoice_status");
				obj.createdBy=resultSet.getInt("created_by");
				obj.nationalId=resultSet.getString("national_id");
				obj.marketId=resultSet.getInt("market_id");
				obj.applicant=resultSet.getString("name");
				
				obj.appFee=resultSet.getString("application_fee");
				obj.conservancy_fee=resultSet.getString("conservancy_fee");
				obj.paymentMode=resultSet.getString("payment_method");
	
		
				System.out.print(resultSet.getString("application_fee") + "++++++++++++++++++++====");
				count++;
				apps.add(obj);
			}

			return apps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get application list for individual signup and agent signup
	 * @param linkId- type of user, like agent or individual
	 * @param nationalIdNo- nationalid number of individual user
	 * @param agentId-userid of agent user
	 * @return list of applications
	 */
	public List<Application> GetAllAppsByLinkId(int linkId,
			String nationalIdNo, int agentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			List<Application> apps = new ArrayList<Application>();
			if (linkId == 3) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAllAppByAgentId);
				preparedStatement.setInt(1, agentId);
				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					apps.add(new Application(resultSet.getInt("ID"), resultSet
							.getString("business_No"), resultSet
							.getString("business_name"), resultSet
							.getInt("no_of_employees"), resultSet
							.getInt("permit_type_id"), resultSet
							.getDouble("permit_fee"), resultSet
							.getString("electricity_no"), resultSet
							.getString("water_acc_no"), resultSet
							.getInt("area"), resultSet.getString("reg_no"),
							resultSet.getString("business_desc"), resultSet
							.getString("room"), resultSet
							.getString("road"), resultSet
							.getString("pinNumber"), resultSet
							.getString("vatNumber"), resultSet
							.getString("detailDesc"), resultSet
							.getString("town"), resultSet
							.getString("building"), resultSet
							.getString("floor"), resultSet
									.getString("postal_add"), resultSet
									.getString("postal_code"), resultSet
									.getString("email"), resultSet
									.getString("fax"), resultSet
									.getString("mobile_no"), resultSet
									.getString("land_line_no"), resultSet
									.getInt("ward_id"), resultSet
									.getString("land_zone"), resultSet
									.getString("plot_no"), resultSet
									.getBoolean("active"), 200, count,
							resultSet.getString("invoice_status"), resultSet
									.getInt("created_by"), resultSet
									.getString("national_id"),resultSet
									.getInt("market_id"),resultSet
									.getString("name"),resultSet.getString("payment_method"),
									resultSet.getString("application_fee")));
					count++;
				}

			}
			if (linkId == 4) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAllAppsForIndividual);
				preparedStatement.setString(1, nationalIdNo);
				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					apps.add(new Application(resultSet.getInt("ID"), resultSet
							.getString("business_No"), resultSet
							.getString("business_name"), resultSet
							.getInt("no_of_employees"), resultSet
							.getInt("permit_type_id"), resultSet
							.getDouble("permit_fee"), resultSet
							.getString("electricity_no"), resultSet
							.getString("water_acc_no"), resultSet
							.getInt("area"), resultSet.getString("reg_no"),
							resultSet.getString("business_desc"), resultSet
									.getString("postal_add"), resultSet
									.getString("postal_code"), resultSet
									.getString("email"), resultSet
									.getString("fax"), resultSet
									.getString("room"), resultSet
									.getString("road"), resultSet
									.getString("pinNumber"), resultSet
									.getString("vatNumber"), resultSet
									.getString("detailDesc"), resultSet
									.getString("town"), resultSet
									.getString("building"), resultSet
									.getString("floor"), resultSet
									.getString("mobile_no"), resultSet
									.getString("land_line_no"), resultSet
									.getInt("ward_id"), resultSet
									.getString("land_zone"), resultSet
									.getString("plot_no"), resultSet
									.getBoolean("active"), 200, count,
							resultSet.getString("invoice_status"), resultSet
									.getInt("created_by"), resultSet
									.getString("national_id"),resultSet
									.getInt("market_id"),resultSet
									.getString("name"),resultSet.getString("payment_method")
									,resultSet.getString("application_fee")));
					count++;
				}

			}
			return apps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the details of all invoices
	 * @return list of invoices
	 */
	public List<Application> GetAllInvoices() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getInvoices);
			resultSet = preparedStatement.executeQuery();
			List<Application> apps = new ArrayList<Application>();
			while (resultSet.next()) {
				Application objApp = new Application();
				objApp.appId = resultSet.getInt("id");
				objApp.businessNo = resultSet.getString("business_No");
				objApp.businessName = resultSet.getString("business_name");
				objApp.appliedOn = resultSet.getString("created_on");
				objApp.status = resultSet.getString("invoice_Status");
                                                                        if(resultSet.getString("invoice_Status").equals("Rejected"))
                                                                        {
                                                                            objApp.paidStatus = "Rejected";
                                                                        }
                                                                        else
                                                                        {
                                                                            objApp.paidStatus = resultSet.getString("paid_status");
                                                                        }
				objApp.permitFee = resultSet.getDouble("fee");
				objApp.penalty = resultSet.getDouble("penalty");
				objApp.postalAdd = resultSet.getString("postal_add");
				objApp.postalCode = resultSet.getString("postal_code");
				objApp.email = resultSet.getString("email");
				objApp.mobileNo = resultSet.getString("mobile_no");
				objApp.appNo = resultSet.getString("app_no");
				objApp.applicant = resultSet.getString("username");
				objApp.noOfEmployees = resultSet.getInt("no_of_employees");
				objApp.area = resultSet.getInt("area");
				objApp.waterAccNo = resultSet.getString("water_acc_no");
				objApp.regNo = resultSet.getString("reg_no");
				objApp.electricityAccNo = resultSet.getString("electricity_no");
				objApp.businessId = resultSet.getInt("business_id");
				objApp.approvedBy = resultSet.getInt("approved_by");
				objApp.approvedOn = resultSet.getString("approved_On");
				objApp.rejectReason = resultSet.getString("rejected_reason");
				objApp.paidDate = resultSet.getString("paid_date");
				objApp.appType = resultSet.getString("app_type");
				objApp.preSbp = resultSet.getString("previous_sbp");
				objApp.appliedFor = resultSet.getInt("applied_for");
				if (objApp.approvedBy > 0) {
					preparedStatement1 = connection
							.prepareStatement(Queryconstants.getApprovedUser);
					preparedStatement1.setInt(1, objApp.approvedBy);
					resultSet2 = preparedStatement1.executeQuery();
					if (resultSet2.next()) {
						objApp.approvedUser = resultSet2.getString("username");
					}
				}
				if (resultSet.getInt("receipt_by") > 0) {
					preparedStatement1 = connection
							.prepareStatement(Queryconstants.getApprovedUser);
					preparedStatement1
							.setInt(1, resultSet.getInt("receipt_by"));
					resultSet2 = preparedStatement1.executeQuery();
					if (resultSet2.next()) {
						objApp.paidUser = resultSet2.getString("username");
					}
				}
                                                                        objApp.paymentMode  =   resultSet.getString("payment_method");
				objApp.count = count;
				apps.add(objApp);
				count++;
			}
			return apps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * get invoices list for individual signup and agent signup
	 * @param linkId- type of user, like agent or individual
	 * @param nationalIdNo- nationalid number of individual user
	 * @param agentId-userid of agent user
	 * @return list of invoices
	 */
	public List<Application> GetAllInvoicesByLinkId(int linkId,String nationalIdNo, int agentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			List<Application> apps = new ArrayList<Application>();
			if (linkId == 3) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getInvoicesByAgentId);
				preparedStatement.setInt(1, agentId);
				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next()) {
					Application objApp = new Application();
					objApp.appId = resultSet.getInt("id");
					objApp.businessNo = resultSet.getString("business_No");
					objApp.businessName = resultSet.getString("business_name");
					objApp.appliedOn = resultSet.getString("created_on");
					objApp.status = resultSet.getString("invoice_Status");
					objApp.paidStatus = resultSet.getString("paid_status");
					objApp.permitFee = resultSet.getDouble("fee");
					objApp.penalty = resultSet.getDouble("penalty");
					objApp.postalAdd = resultSet.getString("postal_add");
					objApp.postalCode = resultSet.getString("postal_code");
					objApp.email = resultSet.getString("email");
					objApp.mobileNo = resultSet.getString("mobile_no");
					objApp.appNo = resultSet.getString("app_no");
					objApp.applicant = resultSet.getString("username");
					objApp.noOfEmployees = resultSet.getInt("no_of_employees");
					objApp.area = resultSet.getInt("area");
					objApp.waterAccNo = resultSet.getString("water_acc_no");
					objApp.regNo = resultSet.getString("reg_no");
					objApp.electricityAccNo = resultSet
							.getString("electricity_no");
					objApp.businessId = resultSet.getInt("business_id");
					objApp.approvedBy = resultSet.getInt("approved_by");
					objApp.approvedOn = resultSet.getString("approved_On");
					objApp.rejectReason = resultSet
							.getString("rejected_reason");
					objApp.paidDate = resultSet.getString("paid_date");
					objApp.appType = resultSet.getString("app_type");
					objApp.preSbp = resultSet.getString("previous_sbp");
					objApp.appliedFor = resultSet.getInt("applied_for");
					if (objApp.approvedBy > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getApprovedUser);
						preparedStatement1.setInt(1, objApp.approvedBy);
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objApp.approvedUser = resultSet2
									.getString("username");
						}
					}
					if (resultSet.getInt("receipt_by") > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getApprovedUser);
						preparedStatement1.setInt(1,
								resultSet.getInt("receipt_by"));
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objApp.paidUser = resultSet2.getString("username");
						}
					}
                                                                                          objApp.paymentMode  =   resultSet.getString("payment_method");
					objApp.count = count;
					apps.add(objApp);
					count++;
				}
			}
			if (linkId == 4) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getInvoicesByIndividual);
				preparedStatement.setString(1, nationalIdNo);
				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next()) {
					Application objApp = new Application();
					objApp.appId = resultSet.getInt("id");
					objApp.businessNo = resultSet.getString("business_No");
					objApp.businessName = resultSet.getString("business_name");
					objApp.appliedOn = resultSet.getString("created_on");
					objApp.status = resultSet.getString("invoice_Status");
					objApp.paidStatus = resultSet.getString("paid_status");
					objApp.permitFee = resultSet.getDouble("fee");
					objApp.penalty = resultSet.getDouble("penalty");
					objApp.postalAdd = resultSet.getString("postal_add");
					objApp.postalCode = resultSet.getString("postal_code");
					objApp.email = resultSet.getString("email");
					objApp.mobileNo = resultSet.getString("mobile_no");
					objApp.appNo = resultSet.getString("app_no");
					objApp.applicant = resultSet.getString("username");
					objApp.noOfEmployees = resultSet.getInt("no_of_employees");
					objApp.area = resultSet.getInt("area");
					objApp.waterAccNo = resultSet.getString("water_acc_no");
					objApp.regNo = resultSet.getString("reg_no");
					objApp.electricityAccNo = resultSet
							.getString("electricity_no");
					objApp.businessId = resultSet.getInt("business_id");
					objApp.approvedBy = resultSet.getInt("approved_by");
					objApp.approvedOn = resultSet.getString("approved_On");
					objApp.rejectReason = resultSet
							.getString("rejected_reason");
					objApp.paidDate = resultSet.getString("paid_date");
					objApp.appType = resultSet.getString("app_type");
					objApp.preSbp = resultSet.getString("previous_sbp");
					objApp.appliedFor = resultSet.getInt("applied_for");
					if (objApp.approvedBy > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getApprovedUser);
						preparedStatement1.setInt(1, objApp.approvedBy);
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objApp.approvedUser = resultSet2
									.getString("username");
						}
					}
					if (resultSet.getInt("receipt_by") > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getApprovedUser);
						preparedStatement1.setInt(1,
								resultSet.getInt("receipt_by"));
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objApp.paidUser = resultSet2.getString("username");
						}
					}
                                                                                          objApp.paymentMode  =   resultSet.getString("payment_method");
					objApp.count = count;
					apps.add(objApp);
					count++;
				}
			}
			return apps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * updates the invoices status based on the condition passed
	 * like inspection,approved,rejected,paid,refer to user,renewal
	 * @return 200 on successful update, 201 on failed,202 on exception
	 */
	@Override
	public ErevenueResponse UpdateInvocieStatus(Application app) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (app.status.equalsIgnoreCase("A")) 
                                                     {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateApprovedStatus);
				preparedStatement.setString(1, app.status);
				preparedStatement.setInt(2, app.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				preparedStatement.setInt(4, app.businessId);
			} 
                                                      else if (app.status.equalsIgnoreCase("P")) 
                                                      {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updatePaidStatus);
				preparedStatement.setString(1, app.status);
				preparedStatement.setString(2, app.mpesaCode);
				preparedStatement.setInt(3, 1);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, app.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(7, app.bankName);
				preparedStatement.setString(8, app.accNo);
				preparedStatement.setString(9, app.transNo);
                                                                        preparedStatement.setString(10, app.paymentMode);
				preparedStatement.setInt(11, app.businessId);
				if (preparedStatement.executeUpdate() > 0) {

					Calendar calendarEnd = Calendar.getInstance();
					int year = calendarEnd.get(Calendar.YEAR);
					calendarEnd.set(Calendar.YEAR, year);
					calendarEnd.set(Calendar.MONTH, 11);
					calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
					// returning the last date
					Date endDate = calendarEnd.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// / System.out.println(sdf.format(endDate.getTime()));
					DBOperations.DisposeSql(preparedStatement);
					// preparedStatement1 = connection
					// .prepareStatement(Queryconstants.validateMpesaCode);
					// preparedStatement1.setString(1, app.mpesaCode);
					// resultSet = preparedStatement1.executeQuery();
					// if (resultSet.next()) {
					if (app.appType.equalsIgnoreCase("Renew")) {
						
						preparedStatement = connection
								.prepareStatement(Queryconstants.updatePermit);
						preparedStatement.setInt(1, app.appliedFor);
						preparedStatement.setString(2,
								sdf.format(endDate.getTime()));
						preparedStatement.setBoolean(3, true);
						preparedStatement.setInt(4, app.createdBy);
						preparedStatement.setTimestamp(
								5,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement.setString(6, app.preSbp);
					} else {
						preparedStatement = connection
								.prepareStatement(Queryconstants.insertPermit);
						preparedStatement.setString(1, GetPermitNo(app.appId));
						preparedStatement.setInt(2, app.appId);
						preparedStatement.setInt(3, app.businessId);
						preparedStatement.setInt(4, year);
						preparedStatement.setString(5,
								sdf.format(endDate.getTime()));
						preparedStatement.setBoolean(6, true);
						preparedStatement.setInt(7, app.createdBy);
						preparedStatement.setTimestamp(
								8,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						// } else {
						// return new ErevenueResponse(201,
						// "Invalid Mpesa code");
						// }
					}
				}
			} 
                                                      else if (app.status.equalsIgnoreCase("R")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateRejectedStatus);
				preparedStatement.setString(1, app.status);
				preparedStatement.setString(2, app.rejectReason);
				preparedStatement.setInt(3, app.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, app.businessId);
                                                                       
			} else if (app.status.equalsIgnoreCase("I")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateInspectedStatus);
				preparedStatement.setString(1, app.status);
				preparedStatement.setInt(2, app.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(4, app.businessId);
			} else if (app.status.equalsIgnoreCase("RU")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateReferUserStatus);
				preparedStatement.setString(1, app.status);
				preparedStatement.setInt(2, app.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(4, app.businessId);
			}

			if (preparedStatement.executeUpdate() > 0) {
				connection.commit();
				return new ErevenueResponse(200, "Records Updated");
			} else {
				connection.rollback();
				return new ErevenueResponse(201, "Nothing To Update");
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ErevenueResponse(202, "Exception Occured");

		} catch (Exception ex) {

			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the list of permits details
	 * @return list of permits
	 */
	public List<Application> GetAllPermits() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ResultSet resultSet = null;

		int count = 1;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getPermitDetails);

			resultSet = preparedStatement.executeQuery();
			List<Application> apps = new ArrayList<Application>();

			while (resultSet.next()) {
				Application objApp = new Application();
				// objApp.appId = resultSet.getInt("id");
				objApp.applicant = resultSet.getString("name");
				objApp.permitNo = resultSet.getString("permit_no");
				objApp.businessNo = resultSet.getString("business_No");
				objApp.validity = resultSet.getInt("validity");
				objApp.expiryDate = resultSet.getString("expiry_date");
				objApp.businessName = resultSet.getString("business_name");
				objApp.businessDesc = resultSet.getString("business_Desc");
				objApp.permitFee = resultSet.getDouble("fee");
				objApp.postalAdd = resultSet.getString("postal_add");
				objApp.postalCode = resultSet.getString("postal_code");
				objApp.email = resultSet.getString("email");
				objApp.appNo = resultSet.getString("app_no");
				objApp.plotNo = resultSet.getString("plot_no");
				objApp.permitUser = resultSet.getString("username");
				//objApp.permitQr = generateQrImageBase64String(objApp.permitNo);
                                                                        objApp.permitQr = generateQrImageBase64String(objApp.permitNo);
				objApp.status = resultSet.getString("permit_status");
				objApp.appType = resultSet.getString("app_type");
				objApp.count = count;
				objApp.businessId = resultSet.getInt("business_id");
				objApp.permitStatus = resultSet.getString("newStatus");
				objApp.nationalId = resultSet.getString("national_id");
				objApp.mktName = resultSet.getString("mkt_name");
				objApp.wardName = resultSet.getString("ward_name");
				int feeValue = (int) Math.round(objApp.permitFee);
				objApp.amountInWords = Utility.convertNumberToWords(feeValue);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				// @year form current date
				Calendar year1 = Calendar.getInstance();
				int year = year1.get(Calendar.YEAR);
				year1.set(Calendar.YEAR, year + 1);
				year1.set(Calendar.MONTH, 11);
				year1.set(Calendar.DAY_OF_MONTH, 31);
				Date endDate = year1.getTime();

				System.out.println(sdf.format(endDate.getTime()));

				// @year form current date
				Calendar year2 = Calendar.getInstance();
				int nyear = year2.get(Calendar.YEAR);
				year2.set(Calendar.YEAR, year + 2);
				year2.set(Calendar.MONTH, 11);
				year2.set(Calendar.DAY_OF_MONTH, 31);
				Date secondyear = year2.getTime();

				System.out.println(sdf.format(secondyear.getTime()));

				ArrayList<PermitYear> yearList = new ArrayList<PermitYear>();
				yearList.add(new PermitYear(sdf.format(endDate.getTime())));
				//yearList.add(new PermitYear(sdf.format(secondyear.getTime())));
				objApp.yearList = yearList;
				apps.add(objApp);
				count++;
			}

			return apps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	

	/**
	 * generates qr image based on permit no
	 * @param permitNo
	 * @return base64 Qr code string
	 * 
	 */
	public String generateQrImageBase64String(String permitNo) {
		String imageString = null;
		String qrText = permitNo;
		// String qrText =
		// voucher.getVoucher()+"$"+voucher.getVoucher()+voucher.getVoucher()+voucher.getVoucher();

		qrText = getBase64QrCodeText(qrText);
		// System.out.println("QrTextLength$$" + qrText.length());
		ByteArrayOutputStream bos = QRCode.from(qrText).to(ImageType.PNG)
				.stream();
		byte[] imageBytes = bos.toByteArray();

		BASE64Encoder encoder = new BASE64Encoder();
		
		imageString = "data:image/png;base64," + encoder.encode(imageBytes);
		Base64Decoder decoder=new Base64Decoder();
		String img=imageString.split(",")[1];
		System.out.println(img+"  imageString");
		imageBytes=decoder.decode(imageBytes,11,11);  
		//String dStr = new String(decoder.decode(imageBytes)); 
		System.out.println(imageBytes+"  imageBytes");

		return imageString;
	}

	/**
	 * converts the qr image to encrypted string
	 * @param qrText-plain qr text
	 * @return encrypted qrCode;
	 */
	public String getBase64QrCodeText(String qrText) {
		String imageString = "";
		byte[] stringBytes = qrText.getBytes();
		BASE64Encoder encoder = new BASE64Encoder();
		imageString = encoder.encode(stringBytes);
		  Base64.Decoder decoder = Base64.getDecoder();
	        // Decoding string
	        String dStr = new String(decoder.decode(imageString));
	        System.out.println("Decoded string: "+dStr);

		return dStr;
	}

	/**
	 * Decode string to image
	 * @param imageString- The string to decode
	 * @return decoded image
	 */
	public static BufferedImage decodeToImage(String imageString) {

		BufferedImage image = null;
		byte[] imageByte;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			imageByte = decoder.decodeBuffer(imageString);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			image = ImageIO.read(bis);
			bis.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Encode image to string
	 * @param image-The image to encode
	 * @param type-jpeg, bmp, ...
	 * @return encoded string
	 */
	public static String encodeToString(BufferedImage image, String type) {
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	/**
	 * updates the qrimage path to database for selected permit
	 * @return 200 on successful update, 201 on failed,202 on exception
	 */
	@Override
	public ErevenueResponse UpdateQrImagePath(Application app) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String qrImage = "";
		BufferedImage image = null;
		try {
			connection = dataSource.getConnection();
			// String reportPath="D:\\MRM\\"+app.permitNo+".jpg";
			String param = "qr.filePathQrCode";
			Utility util = new Utility();
			compasProperties = util.getCompasProperties(param);

			// TOMCAT_HOME = System.getProperty("catalina.base");
			// fileName = fileDetail.getFileName();

			//PATH = compasProperties.bnfUploadFilePath;
			//String JAVAPATH = System.getProperty("catalina.base");
			String path = compasProperties.bnfUploadFilePath + app.permitNo
					+ ".jpg";
			System.out.println("qrpath##" + path);
			//System.out.println(image+" image");
			//System.out.println(app.permitQr+" app.permitQr");
			System.out.println(app.amountInWords+" app.permitQr");
			String base64Image = app.permitQr.split(",")[1];
			image = decodeToImage(base64Image);
			File outputfile = new File(path);
			ImageIO.write(image, "jpg", outputfile);

			int feeValue = (int) Math.round(app.permitFee);
			System.out.println(feeValue);
			String amountInWords = Utility.convertNumberToWords(feeValue);
			preparedStatement = connection
					.prepareStatement(Queryconstants.updQrImagePath);
			preparedStatement.setString(1, path);
			preparedStatement.setString(2, app.amountInWords);
			preparedStatement.setString(3, app.permitNo);

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

	/**
	 * update permit status once its printed
	 * @return 200 on successful update, 201 on failed,202 on exception
	 */
	@Override
	public ErevenueResponse UpdatePermitStatus(Application app) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String qrImage = "";
		BufferedImage image = null;
		try {
			connection = dataSource.getConnection();
			int feeValue = (int) Math.round(app.permitFee);
			String amountInWords = Utility.convertNumberToWords(feeValue);
			preparedStatement = connection
					.prepareStatement(Queryconstants.updPermitStatus);

			preparedStatement.setString(1, app.permitStatus);
			// preparedStatement.setString(2, amountInWords);
			preparedStatement.setString(2, app.permitNo);

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

	/**
	 * updates the permit renewal status once its renewed and calculates the penalty
	 * for more comments-go through function
	 * @return 200 on successful update, 201 on failed,202 on exception
	 */
	public ErevenueResponse UpdatePermitRenewal(Application app) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		ResultSet rs = null;
		String penaltyPer1 = "";
		String penaltyMonth1 = "";
		String penaltyPer2 = "";
		String penaltyMonth2 = "";
		String penaltyPer3 = "";
		String penaltyMonth3 = "";
		int deadlineMonth1 =0;
		int deadlineMonth2 =0;
		int deadlineMonth3 =0;
		double penalty =0;
		int appId = 0;
		try {
			connection = dataSource.getConnection();
			//gets the current month in number format which is
			java.util.Date date1 = new Date();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			int currentmonth = cal1.get(Calendar.MONTH) + 1;
			
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "018");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyPer1 = resultSet.getString("value");

			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "019");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyMonth1 = resultSet.getString("value");
				Calendar calendarEnd = Calendar.getInstance();
				int year = calendarEnd.get(Calendar.YEAR);
				Date date = new SimpleDateFormat("MMMM").parse(penaltyMonth1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				 deadlineMonth1 = cal.get(Calendar.MONTH);
			}
		
			
			/**
			 * Second phase penalty
			 */
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "020");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyPer2 = resultSet.getString("value");

			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "021");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyMonth2 = resultSet.getString("value");
				Calendar calendarEnd = Calendar.getInstance();
				int year = calendarEnd.get(Calendar.YEAR);
				Date date = new SimpleDateFormat("MMMM").parse(penaltyMonth2);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				 deadlineMonth2 = cal.get(Calendar.MONTH);
			}
			/**
			 * Third phase penalty
			 */
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "022");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyPer3 = resultSet.getString("value");

			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "023");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyMonth3 = resultSet.getString("value");
				Calendar calendarEnd = Calendar.getInstance();
				int year = calendarEnd.get(Calendar.YEAR);
				Date date = new SimpleDateFormat("MMMM").parse(penaltyMonth3);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				 deadlineMonth3 = cal.get(Calendar.MONTH);
			}
			if(currentmonth>deadlineMonth1 && currentmonth<deadlineMonth2){
				/*get the difference between two months and calculates the penalty
				 * function for calculating the penalty defined in utility class
				 * also the information available on forum of erevenue project-open poject 
				 */
				
				int noOfMonths =  1;//currentmonth-deadlineMonth1;
				BigDecimal d = new BigDecimal(penaltyPer1.trim().replace("%", ""))
						.divide(BigDecimal.valueOf(100));
				 penalty = Utility.calculate(app.permitFee, d.doubleValue(),
						noOfMonths);
			}else if(currentmonth>deadlineMonth2 && currentmonth<deadlineMonth3){
				/*get the difference between two months and calculates the penalty
				 * function for calculating the penalty defined in utility class
				 * also the information available on forum of erevenue project-open poject 
				 */
				
				int noOfMonths = 1;//currentmonth-deadlineMonth2;
				BigDecimal d = new BigDecimal(penaltyPer2.trim().replace("%", ""))
						.divide(BigDecimal.valueOf(100));
				 penalty = Utility.calculate(app.permitFee, d.doubleValue(),
						noOfMonths);
			}else if(currentmonth>deadlineMonth3){
				/*get the difference between two months and calculates the penalty
				 * function for calculating the penalty defined in utility class
				 * also the information available on forum of erevenue project-open poject 
				 */
				
				int noOfMonths =  1;//currentmonth-deadlineMonth3;
				BigDecimal d = new BigDecimal(penaltyPer3.trim().replace("%", ""))
						.divide(BigDecimal.valueOf(100));
				 penalty = Utility.calculate(app.permitFee, d.doubleValue(),
						noOfMonths);
			}
			
			
			if (ValidateRenewBusinessId(app.businessId)) {
				return new ErevenueResponse(201,
						"Application is already in renewal state");
			}
			preparedStatement = connection.prepareStatement(
					Queryconstants.insertApplicationDtl,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, app.businessId);
			preparedStatement.setDouble(2, app.permitFee);
			preparedStatement.setDouble(3, penalty);
			preparedStatement.setString(4, "Renew");
			preparedStatement.setString(5, "RW");
			preparedStatement.setInt(6, app.createdBy);
			preparedStatement.setTimestamp(7, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(8, app.permitNo);
			preparedStatement.setInt(9, app.validity);
			if (preparedStatement.executeUpdate() > 0) {
				if (app.appId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					appId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateAppNo);
				preparedStatement.setString(1, GetApplicationNo(appId));
				preparedStatement.setInt(2, appId);
				if (preparedStatement.executeUpdate() > 0) {
					return new ErevenueResponse(200, "Records Updated");
				} else {

				}
			}
			return new ErevenueResponse(200, "Records Updated");
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

	/**
	 * validates if the business is already on renewed status 
	 * @param businessId
	 * @return true if already on renewal status else false
	 */
	public boolean ValidateRenewBusinessId(int businessId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateRenewBusinessId);
			preparedStatement.setInt(1, businessId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the list of wards by subcounty 
	 * @param subCountyId 
	 * @return list of wards
	 */
	@Override
	public List<Ward> GetWardsBySubCounty(int subCountyId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getWardsBySubCounty);
			preparedStatement.setInt(1, subCountyId);
			resultSet = preparedStatement.executeQuery();
			List<Ward> wards = new ArrayList<Ward>();
			while (resultSet.next()) {
				wards.add(new Ward(resultSet.getInt("id"), resultSet
						.getString("ward_Name")));
				count++;
			}
			return wards;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets the list of markets by ward 
	 * @param subCountyId 
	 * @return list of wards
	 */
	@Override
	public List<Market> GetMarketsByWard(int wardId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMarketsByWard);
			preparedStatement.setInt(1, wardId);
			resultSet = preparedStatement.executeQuery();
			List<Market> markets = new ArrayList<Market>();
			while (resultSet.next()) {
				markets.add(new Market(resultSet.getInt("id"), resultSet
						.getString("mkt_Name")));
				count++;
			}
			return markets;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets the list of permits for the report purpose
	 * @return list of permits
	 */
	public List<Application> GetPermitsByDate(Reports permit) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		ResultSet resultSet = null;

		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getPermitDetailsByDate);

			preparedStatement.setString(1, permit.fromDate == null ? today
					: permit.fromDate);
			preparedStatement.setString(2, permit.toDate == null ? today
					: permit.toDate);

			resultSet = preparedStatement.executeQuery();
			List<Application> apps = new ArrayList<Application>();

			while (resultSet.next()) {
				Application objApp = new Application();
				// objApp.appId = resultSet.getInt("id");
				objApp.permitNo = resultSet.getString("permit_no");
				objApp.businessNo = resultSet.getString("business_No");
				objApp.validity = resultSet.getInt("validity");
				objApp.expiryDate = resultSet.getString("expiry_date");
				objApp.businessName = resultSet.getString("business_name");
				objApp.businessDesc = resultSet.getString("business_Desc");
				objApp.permitFee = resultSet.getDouble("fee");
				objApp.postalAdd = resultSet.getString("postal_add");
				objApp.postalCode = resultSet.getString("postal_code");
				objApp.email = resultSet.getString("email");
				objApp.appNo = resultSet.getString("app_no");
				objApp.plotNo = resultSet.getString("plot_no");
				objApp.permitUser = resultSet.getString("username");
				objApp.vatNumber = resultSet.getString("vatNumber");
                objApp.permitQr = generateQrImageBase64String(objApp.permitNo);

				//objApp.permitQr = generateQrImageBase64String(objApp.permitNo);
				objApp.status = resultSet.getString("permit_status");
				objApp.appType = resultSet.getString("app_type");
				objApp.count = count;
				objApp.businessId = resultSet.getInt("business_id");
				objApp.permitStatus = resultSet.getString("newStatus");
				objApp.nationalId=resultSet.getString("national_id");
				objApp.applicant=resultSet.getString("applicant");
				int feeValue = (int) Math.round(objApp.permitFee);
				objApp.amountInWords = Utility.convertNumberToWords(feeValue);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				// @year form current date
				Calendar year1 = Calendar.getInstance();
				int year = year1.get(Calendar.YEAR);
				year1.set(Calendar.YEAR, year + 1);
				year1.set(Calendar.MONTH, 11);
				year1.set(Calendar.DAY_OF_MONTH, 31);
				Date endDate = year1.getTime();

				System.out.println(sdf.format(endDate.getTime()));

				// @year form current date
				Calendar year2 = Calendar.getInstance();
				int nyear = year2.get(Calendar.YEAR);
				year2.set(Calendar.YEAR, year + 2);
				year2.set(Calendar.MONTH, 11);
				year2.set(Calendar.DAY_OF_MONTH, 31);
				Date secondyear = year2.getTime();

				System.out.println(sdf.format(secondyear.getTime()));

				ArrayList<PermitYear> yearList = new ArrayList<PermitYear>();
				yearList.add(new PermitYear(sdf.format(endDate.getTime())));
				yearList.add(new PermitYear(sdf.format(secondyear.getTime())));
				objApp.yearList = yearList;
				apps.add(objApp);
				count++;
			}

			return apps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
