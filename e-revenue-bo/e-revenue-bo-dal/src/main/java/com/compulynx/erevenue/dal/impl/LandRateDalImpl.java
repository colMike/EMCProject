package com.compulynx.erevenue.dal.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.compulynx.erevenue.dal.LandRateDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.dal.operations.Utility;
import com.compulynx.erevenue.models.Application;
import com.compulynx.erevenue.models.CompasResponse;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LandRate;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.PermitYear;
import com.compulynx.erevenue.models.Reports;

public class LandRateDalImpl implements LandRateDal {
	private DataSource dataSource;

	public LandRateDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * Function for Landrate creating and updating registration form
	 */
	@SuppressWarnings("resource")
	@Override
	public ErevenueResponse CreateRegistrationForm(LandRate lr) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet rs;
		int id = 0;
		int regId = 0;
		try {
			connection = dataSource.getConnection();
			if (lr.id == 0) {

				
				 if (checkPlotNumber(lr.plotNumber)) { return new ErevenueResponse(201,
				 "Plot Number already exists"); }
				
				if (checkTitleDeedNumber(lr.titleDeedNumber)) {
					return new ErevenueResponse(201,
							"title deed number already exists");
				}
				if (lr.linkId == 4) {

					if ((ValidateNationalId(lr.nationalIdNumber)) == false) {
						return new ErevenueResponse(201,
								"National id doesn't match with sign up user's national Id");
					}
				}
				preparedStatement = connection.prepareStatement(
						Queryconstants.insertRegistrationForm,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, lr.plotNumber);
				preparedStatement.setString(2, lr.mapSheetNumber);
				preparedStatement.setString(3, lr.location);
				preparedStatement.setString(4, lr.acreage);
				preparedStatement.setString(5, lr.titleDeedNumber);
				preparedStatement.setInt(6, lr.permitTypeId);
				preparedStatement.setString(7, lr.name);
				preparedStatement.setString(8, lr.krapin);
				preparedStatement.setString(9, lr.nationalIdNumber);
				preparedStatement.setInt(10, lr.wardId);
				preparedStatement.setInt(11, lr.subCountyId);
				preparedStatement.setString(12, lr.sublocation);
				preparedStatement.setString(13, lr.address);
				preparedStatement.setString(14, lr.code);
				preparedStatement.setString(15, lr.phone);
				preparedStatement.setString(16, lr.createdBy);
				preparedStatement.setTimestamp(17, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() > 0) {
					if (lr.id == 0) {
						rs = preparedStatement.getGeneratedKeys();
						rs.next();
						id = rs.getInt(1);
					}

					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateLandNo);
					preparedStatement.setString(1, GetLandNo(id));
					preparedStatement.setInt(2, id);
					// @year form current date
					Calendar year2 = Calendar.getInstance();
					int year = year2.get(Calendar.YEAR);
					if (preparedStatement.executeUpdate() > 0) {
						preparedStatement1 = connection.prepareStatement(
								Queryconstants.insertlandregistrationDtl,
								Statement.RETURN_GENERATED_KEYS);
						preparedStatement1.setInt(1, id);

						preparedStatement1.setDouble(2, GetLandFee(lr.permitTypeId));

						preparedStatement1.setDouble(3, 0.00);
						preparedStatement1.setString(4, "New");
						preparedStatement1.setString(5, lr.status);
						preparedStatement1.setString(6, lr.createdBy);
						preparedStatement1.setTimestamp(
								7,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement1.setString(8, "");
						preparedStatement1.setInt(9, year);
						if(lr.permitTypeId==14){
							preparedStatement1.setDouble(10, GetLandFee(lr.permitTypeId));
						}else{
						preparedStatement1.setDouble(10,GetLandFee(lr.permitTypeId) * Integer.valueOf(lr.acreage));
						}
						//preparedStatement1.setDouble(11,0);

						/*
						 * if(lr.permitTypeId==15){ double balance=(lr.fee *
						 * lr.acreage)-lr.amount;
						 * preparedStatement1.setDouble(10, balance-lr.balance);
						 * preparedStatement1.setDouble(11, (lr.amount)); }else{
						 * double balance=(lr.fee)-lr.amount;
						 * preparedStatement1.setDouble(10, balance-lr.balance);
						 * preparedStatement1.setDouble(11, (lr.amount)); }
						 */
						if (preparedStatement1.executeUpdate() > 0) {
							if (lr.regId == 0) {
								rs = preparedStatement1.getGeneratedKeys();
								rs.next();
								regId = rs.getInt(1);
							}
							DBOperations.DisposeSql(preparedStatement1);
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.updateRegNo);
							preparedStatement1.setString(1, GetRegNo(regId));
							preparedStatement1.setInt(2, regId);
							if (preparedStatement1.executeUpdate() > 0) {
								return new ErevenueResponse(200,
										"Records Updated");
							} else {

							}
						}
					}
				}
				return new ErevenueResponse(201, "Records Updated");

			}

			else {
				if (checkPlotByTitleDeed(lr.plotNumber, lr.titleDeedNumber,
						lr.id)) {
					return new ErevenueResponse(201,
							"Plot number already exists");
				}
				if (lr.linkId == 4) {

					if ((ValidateNationalId(lr.nationalIdNumber)) == false) {
						return new ErevenueResponse(201,
								"National id doesn't match with sign up user's national Id");
					}
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateRegistrationForm);
				preparedStatement.setString(1, lr.plotNumber);
				preparedStatement.setString(2, lr.mapSheetNumber);
				preparedStatement.setString(3, lr.location);
				preparedStatement.setString(4, lr.acreage);
				preparedStatement.setString(5, lr.titleDeedNumber);
				preparedStatement.setString(6, lr.name);
				preparedStatement.setString(7, lr.krapin);
				preparedStatement.setString(8, lr.nationalIdNumber);
				preparedStatement.setInt(9, lr.wardId);
				preparedStatement.setInt(10, lr.subCountyId);
				preparedStatement.setString(11, lr.createdBy);
				preparedStatement.setTimestamp(12, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(13, lr.sublocation);
				preparedStatement.setString(14, lr.address);
				preparedStatement.setString(15, lr.code);
				preparedStatement.setString(16, lr.phone);
				preparedStatement.setInt(17, lr.id);
				if (preparedStatement.executeUpdate() > 0)
                                                                        {
					DBOperations.DisposeSql(preparedStatement);
					if (lr.status.equalsIgnoreCase("AM")) 
                                                                                          {
                                                                                                            Connection cn2  =   dataSource.getConnection();
						preparedStatement2 = cn2.prepareStatement(Queryconstants.updateLandAmendedStatus);
						preparedStatement2.setString(1, lr.status);
						preparedStatement2.setString(2, lr.createdBy);
						preparedStatement2.setTimestamp(
								3,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						/*
						 * if(lr.permitTypeId==15){
						 * preparedStatement1.setDouble(4, (lr.amount)); }else{
						 * preparedStatement1.setDouble(4, (lr.amount)); }
						 * preparedStatement1.setDouble(5,
						 * lr.balance-lr.amount);
						 */
						preparedStatement2.setInt(4, lr.id);
                                                
                                                                                            if(preparedStatement2.executeUpdate() > 0){
                                                                                                return new ErevenueResponse(200, "Records Updated");
                                                                                            }
                                                                                            else{
                                                                                                return new ErevenueResponse(201, "Nothing To Update");
                                                                                            }
					}
                                                                                        else{
                                                                                           return null;
                                                                                        }
				}
                                                                        else
                                                                        {
                                                                            return new ErevenueResponse(201, "Could not update record.");
                                                                        }
                                                                        
                                                                }

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception occured!");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	/**
	 * check national id is exist or not
	 * 
	 * @param nationalIdNumber
	 * @return true if it exists
	 */
	public boolean ValidateNationalId(String nationalIdNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateLandNationalId);
			preparedStatement.setString(1, nationalIdNumber);

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
	 * check plot number,titleDeedNumber,id is exist or not
	 * 
	 * @param plotNumber
	 * @param titleDeedNumber
	 * @param id
	 * @return true if it exists
	 */
	public boolean checkPlotByTitleDeed(String plotNumber,
			String titleDeedNumber, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPlotNumberByTitleDeed);
			preparedStatement.setString(1, plotNumber);
			preparedStatement.setString(2, titleDeedNumber);
			preparedStatement.setInt(3, id);

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
	 * gets the list of permits for the report purpose
	 * @return list of permits
	 */
	public List<LandRate> GetLandRatesByDate(Reports lr) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		ResultSet resultSet = null;

		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLandRatesByDate);

			preparedStatement.setString(1, lr.fromDate == null ? today
					: lr.fromDate);
			preparedStatement.setString(2, lr.toDate == null ? today
					: lr.toDate);

			resultSet = preparedStatement.executeQuery();
			List<LandRate> apps = new ArrayList<LandRate>();

			while (resultSet.next()) {
				LandRate objApp = new LandRate();
				// objApp.appId = resultSet.getInt("id");
//				objApp.permitNo = resultSet.getString("permit_no");
//				objApp.businessNo = resultSet.getString("business_No");
//				objApp.validity = resultSet.getInt("validity");
//				objApp.expiryDate = resultSet.getString("expiry_date");
//				objApp.businessName = resultSet.getString("business_name");
//				objApp.businessDesc = resultSet.getString("business_Desc");
//				objApp.permitFee = resultSet.getDouble("fee");
//				objApp.postalAdd = resultSet.getString("postal_add");
//				objApp.postalCode = resultSet.getString("postal_code");
//				objApp.email = resultSet.getString("email");
//				objApp.appNo = resultSet.getString("app_no");
//				objApp.plotNo = resultSet.getString("plot_no");
//				objApp.permitUser = resultSet.getString("username");
//				objApp.permitQr = generateQrImageBase64String(objApp.permitNo);
//				objApp.status = resultSet.getString("permit_status");
//				objApp.appType = resultSet.getString("app_type");
//				objApp.count = count;
//				
//				objApp.businessId = resultSet.getInt("business_id");
//				objApp.permitStatus = resultSet.getString("newStatus");
//				objApp.nationalId=resultSet.getString("national_id");
//				objApp.applicant=resultSet.getString("applicant");
//				int feeValue = (int) Math.round(objApp.permitFee);
//				objApp.amountInWords = Utility.convertNumberToWords(feeValue);

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

//				ArrayList<PermitYear> yearList = new ArrayList<PermitYear>();
//				yearList.add(new PermitYear(sdf.format(endDate.getTime())));
//				yearList.add(new PermitYear(sdf.format(secondyear.getTime())));
//				objApp.yearList = yearList;
//				apps.add(objApp);
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
	 * check number is exist or not
	 * 
	 * @param titleDeedNumber
	 * @return true if it exists
	 */
	private boolean checkTitleDeedNumber(String titleDeedNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkTitleDeedNumber);
			preparedStatement.setString(1, titleDeedNumber);

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
	 * generating for unique landNo
	 * 
	 * @param id
	 * @return landNo
	 */
	public String GetLandNo(int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String landNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getlandWard);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				System.out.println("landno###" + dateFormat.format(date)
						+ String.format("%06d", id));
				landNo = dateFormat.format(date) + String.format("%06d", id);
			}
			return landNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return landNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}
	public String GetLandNoForUpload(int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String landNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getlandWard);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				System.out.println("landno###" + dateFormat.format(date)
						+ String.format("%06d", id));
				landNo = dateFormat.format(date) + String.format("%06d", id);
			}
			return landNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return landNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}
	/**
	 * generating for unique regId
	 * 
	 * @param regId
	 * @return regNo
	 */
	private String GetRegNo(int regId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String landNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getlandCountyCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				System.out.println("regno###" + dateFormat.format(date)
						+ String.format("%06d", regId));
				landNo = resultSet.getString("value") + dateFormat.format(date)
						+ String.format("%06d", regId);
			}
			return landNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return landNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);

		}
	}

                  
	/**
	 * check plot number is exist or not when creating new registration form
	 * 
	 * @param b
	 * @return true if it exists
	 */
	public boolean checkPlotNumber(String b) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			System.out.println(b + "============");
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPlotNumber);
			preparedStatement.setString(1, b);

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
	 * get all landrate registration forms
	 */
	public List<LandRate> GetAllRegs() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllRegistrations);
			resultSet = preparedStatement.executeQuery();
			List<LandRate> lr = new ArrayList<LandRate>();
			while (resultSet.next()) {
				lr.add(new LandRate(resultSet.getInt("id"), resultSet
						.getString("plot_number"), resultSet
						.getString("mapsheet_number"), resultSet
						.getString("location"), resultSet.getString("acreage"),
						resultSet.getString("title_deed_number"), resultSet
								.getInt("land_type_id"), resultSet
								.getString("name"), resultSet
								.getString("kra_pin"), resultSet
								.getString("national_id_number"), resultSet
								.getInt("ward_id"), resultSet
								.getInt("subCounty_id"), 200, count, resultSet
								.getString("invoice_status"), resultSet
								.getString("created_by"), resultSet
								.getDouble("paid_amount"), resultSet
								.getDouble("balance")));
				count++;
			}
			return lr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all land approval invoices
	 */
	@Override
	public List<LandRate> GetAllLandInvoices() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLandInvoices);
			resultSet = preparedStatement.executeQuery();
			List<LandRate> lr = new ArrayList<LandRate>();
			while (resultSet.next()) {
				LandRate objlr = new LandRate();
				objlr.id = resultSet.getInt("id");
				objlr.landNo = resultSet.getString("land_no");
				objlr.plotNumber = resultSet.getString("plot_number");
				objlr.mapSheetNumber = resultSet.getString("mapsheet_number");
				objlr.location = resultSet.getString("location");
				objlr.acreage = resultSet.getString("acreage");
				objlr.titleDeedNumber = resultSet
						.getString("title_deed_number");
				objlr.name = resultSet.getString("owner");
				objlr.krapin = resultSet.getString("kra_pin");
				objlr.nationalIdNumber = resultSet
						.getString("national_id_number");
				objlr.wardId = resultSet.getInt("ward_id");
				// objlr.applicant = resultSet.getString("username");
				objlr.applicant = resultSet.getString("username");
				objlr.appliedOn = resultSet.getString("created_on");
				objlr.regNo = resultSet.getString("reg_no");
				objlr.landId = resultSet.getInt("land_id");
				objlr.status = resultSet.getString("invoice_status");
				objlr.balance = resultSet.getDouble("balance");
				objlr.amount = resultSet.getDouble("paid_amount");
				objlr.fee = resultSet.getDouble("fee");
                                                                       
				//double tmpAmt = objlr.fee * objlr.acreage;
				//double tmpBal = objlr.balance;
                               
				if (resultSet.getString("paid_status").equalsIgnoreCase("A")) {
					
						objlr.paidStatus = "Pending Payment";
					
				} else if(resultSet.getString("paid_status").equalsIgnoreCase("Paid")){
					if(resultSet.getDouble("balance")==0) {
						objlr.paidStatus = "Paid";
					}
					else if(resultSet.getDouble("balance")>0 && resultSet.getDouble("paid_amount")>0){
                                                                                objlr.paidStatus = "Paid Partially";
                                                                            }
                                                                            else{
                                                                                objlr.paidStatus="Pending Payment";
                                                                            }
//					if (tmpBal < tmpAmt && tmpBal!=0) {
//						objlr.paidStatus = "Paid Partially";
//						objlr.status="Pending Payment";
//					}else if (tmpAmt == tmpBal) {
//						objlr.paidStatus = "Paid";
//						objlr.status="Paid";
//					}
					
				}
                                
                                                                         if(resultSet.getString("invoice_status").equals("Rejected")){
                                                                            objlr.paidStatus    =   "Rejected";
                                                                        }
                                                                        else{
                                                                             if(resultSet.getString("paid_status").equalsIgnoreCase("Paid")){
					if(resultSet.getDouble("balance")==0) {
						objlr.paidStatus = "Paid";
					}
					else if(resultSet.getDouble("balance")>0 && resultSet.getDouble("paid_amount")>0){
                                                                                objlr.paidStatus = "Paid Partially";
                                                                            }
                                                                            else{
                                                                                objlr.paidStatus="Pending Payment";
                                                                            }
//					if (tmpBal < tmpAmt && tmpBal!=0) {
//						objlr.paidStatus = "Paid Partially";
//						objlr.status="Pending Payment";
//					}else if (tmpAmt == tmpBal) {
//						objlr.paidStatus = "Paid";
//						objlr.status="Paid";
//					}
					
				}
                                                                             else{
                                                                                 objlr.paidStatus="Pending Payment";
                                                                             }
                                                                        }
                                                                         
				objlr.subCountyId = resultSet.getInt("subCounty_id");
				objlr.approvedBy = resultSet.getInt("approved_by");
				objlr.approvedOn = resultSet.getString("approved_On");
				objlr.rejectReason = resultSet.getString("rejected_reason");

				objlr.penalty = resultSet.getDouble("penalty");
				objlr.paidDate = resultSet.getString("paid_date");
				objlr.permitTypeId = resultSet.getInt("land_type_id");
				objlr.appliedFor = resultSet.getInt("applied_for");
				objlr.subCountyName = resultSet.getString("name");
				objlr.wardName = resultSet.getString("ward_name");
				objlr.preLr = resultSet.getString("previous_lr");
				objlr.landTypeName = resultSet.getString("permit_type_name");
				objlr.landType = resultSet.getString("land_type");

				if (objlr.approvedBy > 0) {
					preparedStatement1 = connection
							.prepareStatement(Queryconstants.getLandApprovedUser);
					preparedStatement1.setInt(1, objlr.approvedBy);
					resultSet2 = preparedStatement1.executeQuery();
					if (resultSet2.next()) {
						objlr.approvedUser = resultSet2.getString("username");
						// objlr.approvedUser = resultSet2.getString("name");
					}
				}
				if (resultSet.getInt("receipt_by") > 0) {
					preparedStatement1 = connection
							.prepareStatement(Queryconstants.getPaidLandUser);
					preparedStatement1
							.setInt(1, resultSet.getInt("receipt_by"));
					resultSet2 = preparedStatement1.executeQuery();
					if (resultSet2.next()) {
						objlr.paidUser = resultSet2.getString("username");
						// objlr.paidUser = resultSet2.getString("name");
					}
				}
				objlr.count = count;
				lr.add(objlr);
				count++;
			}
			return lr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get landrate invoices list for individual signup and agent signup
	 * 
	 * @param linkId
	 *            - type of user, like agent or individual
	 * @param nationalIdNo
	 *            - nationalid number of individual user
	 * @param agentId
	 *            -userid of agent user
	 * @return list of applications
	 */
	// added by anita
	@SuppressWarnings("resource")
	public List<LandRate> GetAllLandInvoicesByLinkId(int linkId,
			String nationalIdNo, int agentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			List<LandRate> lr = new ArrayList<LandRate>();
			if (linkId == 3) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getLandInvoicesByAgentId);
				preparedStatement.setInt(1, agentId);
				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					LandRate objlr = new LandRate();
					objlr.id = resultSet.getInt("id");
					objlr.landNo = resultSet.getString("land_no");
					objlr.plotNumber = resultSet.getString("plot_number");
					objlr.mapSheetNumber = resultSet
							.getString("mapsheet_number");
					objlr.location = resultSet.getString("location");
					objlr.acreage = resultSet.getString("acreage");
					objlr.titleDeedNumber = resultSet
							.getString("title_deed_number");
					objlr.name = resultSet.getString("ownername");
					objlr.krapin = resultSet.getString("kra_pin");
					objlr.nationalIdNumber = resultSet
							.getString("national_id_number");
					objlr.wardId = resultSet.getInt("ward_id");
					// objlr.applicant = resultSet.getString("username");
					objlr.applicant = resultSet.getString("username");
					objlr.appliedOn = resultSet.getString("created_on");
					objlr.regNo = resultSet.getString("reg_no");
					objlr.landId = resultSet.getInt("land_id");
					objlr.status = resultSet.getString("invoice_status");
					objlr.paidStatus = resultSet.getString("paid_status");
					objlr.subCountyId = resultSet.getInt("subCounty_id");
					objlr.approvedBy = resultSet.getInt("approved_by");
					objlr.approvedOn = resultSet.getString("approved_On");
					objlr.rejectReason = resultSet.getString("rejected_reason");
					objlr.fee = resultSet.getDouble("fee");
					objlr.penalty = resultSet.getDouble("penalty");
					objlr.paidDate = resultSet.getString("paid_date");
					objlr.permitTypeId = resultSet.getInt("land_type_id");
					objlr.appliedFor = resultSet.getInt("applied_for");
					objlr.subCountyName = resultSet.getString("name");
					objlr.wardName = resultSet.getString("ward_name");
					objlr.preLr = resultSet.getString("previous_lr");
					objlr.landTypeName = resultSet
							.getString("permit_type_name");
					if (objlr.approvedBy > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getLandApprovedUser);
						preparedStatement1.setInt(1, objlr.approvedBy);
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objlr.approvedUser = resultSet2
									.getString("username");
							// objlr.approvedUser =
							// resultSet2.getString("name");
						}
					}
					if (resultSet.getInt("receipt_by") > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getPaidLandUser);
						preparedStatement1.setInt(1,
								resultSet.getInt("receipt_by"));
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objlr.paidUser = resultSet2.getString("username");
							// objlr.paidUser = resultSet2.getString("name");
						}
					}
					objlr.count = count;
					lr.add(objlr);
					count++;
				}
			}
			if (linkId == 4) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getLandInvoicesByIndividual);
				preparedStatement.setString(1, nationalIdNo);
				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					LandRate objlr = new LandRate();
					objlr.id = resultSet.getInt("id");
					objlr.landNo = resultSet.getString("land_no");
					objlr.plotNumber = resultSet.getString("plot_number");
					objlr.mapSheetNumber = resultSet
							.getString("mapsheet_number");
					objlr.location = resultSet.getString("location");
					objlr.acreage = resultSet.getString("acreage");
					objlr.titleDeedNumber = resultSet
							.getString("title_deed_number");
					objlr.name = resultSet.getString("ownername");
					objlr.krapin = resultSet.getString("kra_pin");
					objlr.nationalIdNumber = resultSet
							.getString("national_id_number");
					objlr.wardId = resultSet.getInt("ward_id");
					// objlr.applicant = resultSet.getString("username");
					objlr.applicant = resultSet.getString("username");
					objlr.appliedOn = resultSet.getString("created_on");
					objlr.regNo = resultSet.getString("reg_no");
					objlr.landId = resultSet.getInt("land_id");
					objlr.status = resultSet.getString("invoice_status");
					objlr.paidStatus = resultSet.getString("paid_status");
					objlr.subCountyId = resultSet.getInt("subCounty_id");
					objlr.approvedBy = resultSet.getInt("approved_by");
					objlr.approvedOn = resultSet.getString("approved_On");
					objlr.rejectReason = resultSet.getString("rejected_reason");
					objlr.fee = resultSet.getDouble("fee");
					objlr.penalty = resultSet.getDouble("penalty");
					objlr.paidDate = resultSet.getString("paid_date");
					objlr.permitTypeId = resultSet.getInt("land_type_id");
					objlr.appliedFor = resultSet.getInt("applied_for");
					objlr.subCountyName = resultSet.getString("name");
					objlr.wardName = resultSet.getString("ward_name");
					objlr.preLr = resultSet.getString("previous_lr");
					System.out.println(objlr.preLr = resultSet.getString("previous_lr"));
					objlr.landTypeName = resultSet
							.getString("permit_type_name");
					if (objlr.approvedBy > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getLandApprovedUser);
						preparedStatement1.setInt(1, objlr.approvedBy);
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objlr.approvedUser = resultSet2
									.getString("username");
							// objlr.approvedUser =
							// resultSet2.getString("name");
						}
					}
					if (resultSet.getInt("receipt_by") > 0) {
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.getPaidLandUser);
						preparedStatement1.setInt(1,
								resultSet.getInt("receipt_by"));
						resultSet2 = preparedStatement1.executeQuery();
						if (resultSet2.next()) {
							objlr.paidUser = resultSet2.getString("username");
							// objlr.paidUser = resultSet2.getString("name");
						}
					}
					objlr.count = count;
					lr.add(objlr);
					count++;
				}
			}
			return lr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * update land invoice status based on the status passed like
	 * inspection,approved,rejected,paid,refer to user,renewal
	 * 
	 * @return 200 on successful update, 201 on failed,202 on exception
	 */
	@Override
	public ErevenueResponse UpdateLandInvoiceStatus(LandRate lr) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		Double totalPaidAmount=0.0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (lr.status.equalsIgnoreCase("A")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateLandApprovedStatus);
				preparedStatement.setString(1, lr.status);
				preparedStatement.setString(2, lr.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				preparedStatement.setInt(4, lr.id);
			} else if (lr.status.equalsIgnoreCase("P")) {
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getAmount);
				preparedStatement1.setInt(1, lr.id);
				resultSet = preparedStatement1.executeQuery();
				while(resultSet.next()){
					Double paid_amount=resultSet.getDouble("paid_amount");
					System.out.println(".......... "+paid_amount);
					totalPaidAmount=paid_amount+lr.amount;
				}
				
				Calendar calendarEnd = Calendar.getInstance();
				int year = calendarEnd.get(Calendar.YEAR);
				calendarEnd.set(Calendar.YEAR, year);
				calendarEnd.set(Calendar.MONTH, 11);
				calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
				
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateLandPaidStatus);
                                                                        System.out.println("The status: "+lr.status);
                                                                        lr.status = "A";
				preparedStatement.setString(1, lr.status);
				preparedStatement.setString(2, lr.mpesaCode);
				preparedStatement.setBoolean(3, true);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(5, lr.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(7, lr.bankName);
				preparedStatement.setString(8, lr.accNo);
				preparedStatement.setString(9, lr.transNo);
				preparedStatement.setDouble(10, totalPaidAmount);
				preparedStatement.setDouble(11, lr.balance);
				preparedStatement.setInt(12, year);
				preparedStatement.setInt(13, lr.id);
				
				System.out.println(".......... "+totalPaidAmount);
				System.out.println("............."+lr.balance);
				System.out.println(".............."+year);

				if (preparedStatement.executeUpdate() > 0) {

					Calendar calendarEnd1 = Calendar.getInstance();
					int year1 = calendarEnd.get(Calendar.YEAR);
					calendarEnd.set(Calendar.YEAR, year);
					calendarEnd.set(Calendar.MONTH, 11);
					calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
					// returning the last date
					Date endDate = calendarEnd.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// / System.out.println(sdf.format(endDate.getTime()));
					DBOperations.DisposeSql(preparedStatement);
					/*
					 * preparedStatement1 = connection
					 * .prepareStatement(Queryconstants.validateMpesaCode);
					 * preparedStatement1.setString(1, lr.mpesaCode); resultSet
					 * = preparedStatement1.executeQuery();
					 */
					if (lr.landType.equalsIgnoreCase("Renew")) {
						System.out.println("............. "+lr.appliedFor);
						System.out.println("............. "+lr.createdBy);
						System.out.println(">>>>>>>"+lr.regNo);
						preparedStatement = connection
								.prepareStatement(Queryconstants.updatelandPermit);
						preparedStatement.setInt(1, lr.appliedFor);
						/*
						 * preparedStatement.setString(2,
						 * sdf.format(endDate.getTime()));
						 */
						preparedStatement.setString(2,"RW" );
						preparedStatement.setString(3, lr.createdBy);
						preparedStatement.setTimestamp(
								4,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement.setString(5, lr.regNo);
						
						
					} else {
						//if (lr.balance == 0) {
//							preparedStatement = connection
//									.prepareStatement(Queryconstants.insertlandPermit);
//							preparedStatement.setString(1,
//									GetPermitNo(lr.landId));
//							preparedStatement.setInt(2, lr.landId);
//							preparedStatement.setInt(3, lr.id);
//							preparedStatement.setInt(4, year);
//							/*
//							 * preparedStatement.setString(5,
//							 * sdf.format(endDate.getTime()));
//							 */
//							preparedStatement.setBoolean(5, true);
//							preparedStatement.setInt(6, lr.createdBy);
//							preparedStatement.setTimestamp(
//									7,
//									new java.sql.Timestamp(new java.util.Date()
//											.getTime()));
							// } else {
							// return new ErevenueResponse(201,
							// "Invalid Mpesa code");
							// }
						//} else {
							connection.commit();
							return new ErevenueResponse(200, "Records Updated");
						//}
					}
				}
			} else if (lr.status.equalsIgnoreCase("R")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateLandRejectedStatus);
				preparedStatement.setString(1, lr.status);
				preparedStatement.setString(2, lr.rejectReason);
				preparedStatement.setString(3, lr.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, lr.id);
			} else if (lr.status.equalsIgnoreCase("I")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateLandInspectedStatus);
				preparedStatement.setString(1, lr.status);
				preparedStatement.setString(2, lr.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(4, lr.id);
			} else if (lr.status.equalsIgnoreCase("RU")) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateLandReferUser);
				preparedStatement.setString(1, lr.status);
				preparedStatement.setString(2, lr.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(4, lr.id);
			}
			/*
			 * return (preparedStatement.executeUpdate() > 0) ? new
			 * ErevenueResponse( 200, "Records Updated") : new
			 * ErevenueResponse(201, "Nothing To Update");
			 */
			if (preparedStatement.executeUpdate() > 0) {
				connection.commit();
				return new ErevenueResponse(200, "Records Updated");
			} else {
				connection.rollback();
				return new ErevenueResponse(201, "Nothing To Update");
			}
		} catch (SQLException sqlEx) {
                                                    System.out.println("Error: "+sqlEx.getMessage());
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
	 * generate unique permit number
	 * 
	 * @param landId
	 * @return permit number
	 */
	public String GetPermitNo(int landId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String permitNo = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			connection = dataSource.getConnection();
			System.out.println("landpermitno###" + dateFormat.format(date)
					+ landId);
			permitNo = dateFormat.format(date) + String.format("%06d", landId);

			return permitNo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return permitNo = "";
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);

		}

	}

	/**
	 * update land permit renewal once its renewed and calculates the penalty
	 */
	public ErevenueResponse UpdateLandPermitRenewal(LandRate lr) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String penaltyPer = "";
		String penaltyMonth = "";
		BufferedImage image = null;
		ResultSet rs = null;
		int id = 0;
		int regId = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "004");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyPer = resultSet.getString("value");

			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigValue);
			preparedStatement.setString(1, "005");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				penaltyMonth = resultSet.getString("value");
			}

			Date date = new SimpleDateFormat("MMMM").parse(penaltyMonth);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int deadlineMonth = cal.get(Calendar.MONTH) + 1;
			System.out.println("Penalty Month##" + deadlineMonth);

			java.util.Date date1 = new Date();
			Calendar cal1 = Calendar.getInstance();
			cal.setTime(date1);
			int currentmonth = cal1.get(Calendar.MONTH) + 1;
			System.out.println("current month##" + currentmonth);

			int noOfMonths = currentmonth - deadlineMonth;
			BigDecimal d = new BigDecimal(penaltyPer.trim().replace("%", ""))
					.divide(BigDecimal.valueOf(100));

			double penalty = calculate(lr.fee, d.doubleValue(), noOfMonths);
			System.out.println("penalty##" + penalty);
			if (ValidateRenewLandId(lr.id)) {
				return new ErevenueResponse(201,
						"Landrates Application is already in renewal state");
			}
			// @year form current date
			Calendar year2 = Calendar.getInstance();
			int year = year2.get(Calendar.YEAR);
			if (ValidateRenewBusinessId(lr.landId)) {
				return new ErevenueResponse(201,
						"Application is already in renewal state");
			}
			preparedStatement = connection.prepareStatement(
					Queryconstants.insertlandregistrationDtl,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, lr.id);
			preparedStatement.setDouble(2, lr.fee);
			preparedStatement.setDouble(3, penalty);
			preparedStatement.setString(4, "Renew");
			preparedStatement.setString(5, "RW");
			preparedStatement.setString(6, lr.createdBy);
			preparedStatement.setTimestamp(7, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(8, lr.permitNo);
			preparedStatement.setInt(9, lr.appliedFor);
			if(lr.permitTypeId==14){
				preparedStatement.setDouble(10, lr.fee);
			}else{
			preparedStatement.setDouble(10, lr.fee * Integer.valueOf(lr.acreage));
			}
			//preparedStatement.setDouble(11,0);
			if (preparedStatement.executeUpdate() > 0) {
				if (lr.regId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					regId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateRegNo);
				preparedStatement.setString(1, GetRegNo(regId));
				preparedStatement.setInt(2, regId);
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
	 * validate renewal id
	 * 
	 * @param land_id
	 * @return true if alredy on renewal status
	 */
	public boolean ValidateRenewLandId(int land_id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateRenewLandId);
			preparedStatement.setInt(1, land_id);

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

	/*
	 * Calculates the compound interest rate per month p- initial value of
	 * amount r - compound rate n- no of months
	 */
	public double calculate(double p, double r, int n) {

		double amount = p * Math.pow(1 + (r), n);

		double interest = amount - p;

		System.out.println("Compond Interest is " + interest);

		return interest;

	}

	/**
	 * get lands where status is paid
	 */
	public List<LandRate> GetPaidLandDetails() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ResultSet resultSet = null;

		int count = 1;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLandDetails);
			preparedStatement.setString(1, "P");
			resultSet = preparedStatement.executeQuery();
			List<LandRate> lr = new ArrayList<LandRate>();
			while (resultSet.next()) {
				LandRate objlr = new LandRate();
				objlr.plotNumber = resultSet.getString("plot_number");
				objlr.titleDeedNumber = resultSet
						.getString("title_deed_number");
				objlr.validity = resultSet.getInt("validity");
				// objlr.expiryDate = resultSet.getString("expiry_date");
				objlr.acreage = resultSet.getString("acreage");
				objlr.landNo = resultSet.getString("land_no");
				objlr.fee = resultSet.getDouble("fee");
				objlr.name = resultSet.getString("name");
				objlr.regNo = resultSet.getString("reg_no");
				objlr.status = resultSet.getString("status");
				objlr.permitUser = resultSet.getString("username");
				objlr.permitTypeId = resultSet.getInt("land_type_id");
				objlr.appliedFor = resultSet.getInt("applied_for");
				// objlr.permitQr = generateQrImageBase64String(objlr.permitNo);
				objlr.count = count;
				objlr.id = resultSet.getInt("land_id");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				// @year form current date
				Calendar year1 = Calendar.getInstance();
				int year = year1.get(Calendar.YEAR);
				year1.set(Calendar.YEAR, year + 1);
				year1.set(Calendar.MONTH, 11);
				year1.set(Calendar.DAY_OF_MONTH, 31);
				Date endDate = year1.getTime();

				System.out.println(sdf.format(endDate.getTime())+",,,,,,,,,,,,,,,,,,,,,,,,");
				
				

				/*
				 * // @year form current date Calendar year2 =
				 * Calendar.getInstance(); int nyear = year2.get(Calendar.YEAR);
				 * year2.set(Calendar.YEAR, year + 2); year2.set(Calendar.MONTH,
				 * 11); year2.set(Calendar.DAY_OF_MONTH, 31); Date secondyear =
				 * year2.getTime();
				 * 
				 * System.out.println(sdf.format(secondyear.getTime()));
				 */
				Calendar year2 = Calendar.getInstance();
				int nyear = year2.get(Calendar.YEAR);
				year2.set(Calendar.YEAR, year + 2);
				year2.set(Calendar.MONTH, 11);
				year2.set(Calendar.DAY_OF_MONTH, 31);
				Date secondyear = year2.getTime();

				ArrayList<PermitYear> yearList = new ArrayList<PermitYear>();
				yearList.add(new PermitYear(sdf.format(endDate.getTime())));
				System.out.println(yearList);
				// yearList.add(new
				// PermitYear(sdf.format(secondyear.getTime())));
				objlr.yearList = yearList;
				lr.add(objlr);
				count++;
			}

			return lr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all land permits
	 * 
	 */
	public List<LandRate> GetAllLandPermits() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ResultSet resultSet = null;

		int count = 1;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLandPermitDetails);

			resultSet = preparedStatement.executeQuery();
			List<LandRate> lr = new ArrayList<LandRate>();

			while (resultSet.next()) {
				LandRate objlr = new LandRate();
				// objApp.appId = resultSet.getInt("id");
				//objlr.permitNo = resultSet.getString("land_permit_no");
				objlr.landNo = resultSet.getString("land_no");
				objlr.validity = resultSet.getInt("validity");
				objlr.fee = resultSet.getDouble("fee");
				// objlr.expiryDate = resultSet.getString("expiry_date");
				objlr.plotNumber = resultSet.getString("plot_number");
				objlr.mapSheetNumber = resultSet.getString("mapsheet_number");
				objlr.location = resultSet.getString("location");
				objlr.acreage = resultSet.getString("acreage");
				objlr.titleDeedNumber = resultSet
						.getString("title_deed_number");
				objlr.name = resultSet.getString("name");
				objlr.regNo = resultSet.getString("reg_no");
				objlr.krapin = resultSet.getString("kra_pin");
				objlr.permitUser = resultSet.getString("username");
				objlr.status = resultSet.getString("permit_status");
				objlr.permitTypeId = resultSet.getInt("land_type_id");
				objlr.landType = resultSet.getString("land_type");
				//objlr.permitStatus = resultSet.getString("newStatus");
				objlr.count = count;
				objlr.id = resultSet.getInt("land_id");

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				// @year form current date
				Calendar year1 = Calendar.getInstance();
				int year = year1.get(Calendar.YEAR);
				year1.set(Calendar.YEAR, year + 1);
				year1.set(Calendar.MONTH, 11);
				year1.set(Calendar.DAY_OF_MONTH, 31);
				Date endDate = year1.getTime();

				System.out.println(sdf.format(endDate.getTime()));

				/*
				 * // @year form current date Calendar year2 =
				 * Calendar.getInstance(); int nyear = year2.get(Calendar.YEAR);
				 * year2.set(Calendar.YEAR, year + 2); year2.set(Calendar.MONTH,
				 * 11); year2.set(Calendar.DAY_OF_MONTH, 31); Date secondyear =
				 * year2.getTime();
				 * 
				 * System.out.println(sdf.format(secondyear.getTime()));
				 */	Calendar year2 = Calendar.getInstance();
				int nyear = year2.get(Calendar.YEAR);
				year2.set(Calendar.YEAR, year + 2);
				year2.set(Calendar.MONTH, 11);
				year2.set(Calendar.DAY_OF_MONTH, 31);
				Date secondyear = year2.getTime();

				System.out.println(sdf.format(secondyear.getTime()));

				ArrayList<PermitYear> yearList = new ArrayList<PermitYear>();
				yearList.add(new PermitYear(sdf.format(endDate.getTime())));
				// yearList.add(new
				// PermitYear(sdf.format(secondyear.getTime())));
				objlr.yearList = yearList;
				lr.add(objlr);
				count++;
			}

			// File fout = new File(filePath.toUpperCase());
			// FileOutputStream fos = new FileOutputStream(fout);
			//
			// BufferedWriter bw = new BufferedWriter(new
			// OutputStreamWriter(fos));
			// bw.write(bufferedImage);
			return lr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get landrate application list for individual signup and agent signup
	 * 
	 * @param linkId
	 *            - type of user, like agent or individual
	 * @param nationalIdNo
	 *            - nationalid number of individual user
	 * @param agentId
	 *            -userid of agent user
	 * @return list of land applications
	 */
	@SuppressWarnings("resource")
	public List<LandRate> GetAllLandsByLinkId(int linkId, String nationalIdNo,
			int agentId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			List<LandRate> lrs = new ArrayList<LandRate>();
			if (linkId == 3) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAllLandByAgentId);
				preparedStatement.setInt(1, agentId);
				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					lrs.add(new LandRate(resultSet.getInt("id"), resultSet
							.getString("plot_number"), resultSet
							.getString("mapsheet_number"), resultSet
							.getString("location"), resultSet
							.getString("acreage"), resultSet
							.getString("title_deed_number"), resultSet
							.getInt("land_type_id"), resultSet
							.getString("name"), resultSet.getString("kra_pin"),
							resultSet.getString("national_id_number"),
							resultSet.getInt("ward_id"), resultSet
									.getInt("subCounty_id"), 200, count,
							resultSet.getString("invoice_status"), resultSet
									.getString("created_by"), resultSet
									.getDouble("fee"), resultSet
									.getDouble("balance")));
					count++;
				}

			}
			if (linkId == 4) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.getAllLandsForIndividual);
				preparedStatement.setString(1, nationalIdNo);
				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					lrs.add(new LandRate(resultSet.getInt("id"), resultSet
							.getString("plot_number"), resultSet
							.getString("mapsheet_number"), resultSet
							.getString("location"), resultSet
							.getString("acreage"), resultSet
							.getString("title_deed_number"), resultSet
							.getInt("land_type_id"), resultSet
							.getString("name"), resultSet.getString("kra_pin"),
							resultSet.getString("national_id_number"),
							resultSet.getInt("ward_id"), resultSet
									.getInt("subCounty_id"), 200, count,
							resultSet.getString("invoice_status"), resultSet
									.getString("created_by"), resultSet
									.getDouble("fee"), resultSet
									.getDouble("balance")));
					count++;
				}

			}
			return lrs;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
        
        public double GetLandFee(int landId){
            Connection connection   =   null;
            PreparedStatement preparedStatement =   null;
            ResultSet resultSet =   null;
            
            try
            {
                connection  =   dataSource.getConnection();
                preparedStatement   =   connection.prepareStatement(Queryconstants.getLandFee);
                preparedStatement.setInt(1, landId);
                resultSet   =   preparedStatement.executeQuery();
               if(resultSet != null)
               {
                   resultSet.next();
                   
                   return resultSet.getDouble("permit_fee");
               }
               else
               {
                   return 0;
               }
            }
            catch(SQLException e)
            { 
                return 0;
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
    					.prepareStatement(Queryconstants.validateRenewLand);
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

		public CompasResponse UploadPlot(String filePath, String fileName, String uploadedBy, int branchId) {
			// TODO Auto-generated method stub
			List<LandRate> list = new ArrayList<LandRate>();
			List<LandRate> usrUpload = new ArrayList<LandRate>();
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				connection = dataSource.getConnection();
				if (checkifFileExists(fileName)) {
					return new CompasResponse(204, "File Already Uploaded!!");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertfileDtl);
				preparedStatement.setString(1, fileName);
				preparedStatement.setString(2, uploadedBy);
				preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));

				if (preparedStatement.executeUpdate() > 0) {
					list = getAccountsExcel(filePath, uploadedBy, branchId);
				}
					for (LandRate detail : list) {
						usrUpload.add(detail);
					}
					
					if (usrUpload.size() > 0) {
						for (LandRate detail : usrUpload) {
				
						  if (UpdateImportUser(detail).respCode != 200) {
						  System.out.println(UpdateImportLand(detail).respCode + "detail==============");
						 // System.out.println(UpdateImportLand(detail).respCode+ "UpdateImportLand(detail).respCode");
						  
						  return new CompasResponse(207, "Plot number"+detail.plotNumber+" already exist");
					
						 
						 }
					
						  
						
						 
						
						}
				
						}
					return new CompasResponse(200, "Uploaded Successfully  ");
			} catch (Exception ex) {
				return new CompasResponse(201, "Server Error occurred, Please try again");
			} finally {
				DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			}
		}

		private ArrayList<LandRate> getAccountsExcel(String pathToFile, String createdBy, int branchId) throws IOException {
			ArrayList<LandRate> userObjs = new ArrayList<LandRate>();

			try {

				//logger.info("FileNameInUploadExcel##" + pathToFile);
				FileInputStream file = new FileInputStream(new File(pathToFile));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				workbook.getNumberOfSheets();
				XSSFSheet sheet = workbook.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();
				
				DataFormatter df = new DataFormatter();
				for (int r = 1; r < rows; r++) {
					
					XSSFRow row = sheet.getRow(r);
					XSSFCell cell;
					LandRate userr = new LandRate();
					
					userr.name = df.formatCellValue(row.getCell(0));
					userr.nationalIdNumber = df.formatCellValue(row.getCell(1));
					userr.krapin = df.formatCellValue(row.getCell(2));
					userr.subCountyName = df.formatCellValue(row.getCell(3));
					userr.location = df.formatCellValue(row.getCell(4));
					userr.plotNumber = df.formatCellValue(row.getCell(5));
					//userr.mapSheetNumber = df.formatCellValue(row.getCell(2));
					
					
					
					
					userr.address =  df.formatCellValue(row.getCell(6));
					userr.phone =  df.formatCellValue(row.getCell(7));
					userr.rates = df.formatCellValue(row.getCell(8));
					//userr.acreage = df.formatCellValue(row.getCell(10));
					//userr.titleDeedNumber = df.formatCellValue(row.getCell(11));
					userr.arrears = df.formatCellValue(row.getCell(9));
					userr.total = df.formatCellValue(row.getCell(10));
					userr.createdBy=createdBy;
					userr.subCountyId=branchId;
					userObjs.add(userr);

				}
				
				file.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return userObjs;
		}

		public CompasResponse UpdateImportUser(LandRate user) {

		
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			//String userNameFullName = user.firstName + " " + user.surname + " " + user.otherName;
			String usernamee = "";
			//String password = randomPassword();
			try {
				connection = dataSource.getConnection();

			
			  if (checkPlotNumber(user.plotNumber)) { return new CompasResponse(207,
			  "Plot Number already exists"); }
			 
			/*
			 * if (checkTitleDeedNumber(user.titleDeedNumber)) { return new
			 * CompasResponse(201, "title deed number already exists"); }
			 */
				preparedStatement = connection.prepareStatement(Queryconstants.insertUploadedPlot,Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, user.subCountyName);
				
				preparedStatement.setString(2, user.location);
				//preparedStatement.setString(3, user.mapSheetNumber);
				preparedStatement.setString(3, user.plotNumber );
				preparedStatement.setString(4, user.name);
				preparedStatement.setString(5, user.nationalIdNumber );
				preparedStatement.setString(6, user.krapin );
				preparedStatement.setString(7, user.address );
				preparedStatement.setString(8, user.phone);
				preparedStatement.setString(9, user.rates);
				//preparedStatement.setString(11, user.acreage);
				//preparedStatement.setString(12, user.titleDeedNumber);
				preparedStatement.setString(10, user.arrears);
				preparedStatement.setString(11, user.total);
				preparedStatement.setString(12, "14");
				preparedStatement.setTimestamp(13, new Timestamp(new Date().getTime()));
				preparedStatement.setString(14, user.createdBy);
				preparedStatement.setInt(15, user.subCountyId);

			if(preparedStatement.executeUpdate()>0) {

				ResultSet rs;
				int id=1;
				
				 if (user.id == 0) { rs = preparedStatement.getGeneratedKeys(); rs.next(); id
				 = rs.getInt(1); }
				 

			
				 //DBOperations.DisposeSql(preparedStatement); 
				 preparedStatement = connection
				  .prepareStatement(Queryconstants.updateLandNo);
				 preparedStatement.setString(1, user.titleDeedNumber);
				preparedStatement.setInt(2, id);
				 
				// @year form current date
				Calendar year2 = Calendar.getInstance();
				int year = year2.get(Calendar.YEAR);
				if (preparedStatement.executeUpdate() > 0) {
					PreparedStatement preparedStatement1 = connection.prepareStatement(
							Queryconstants.insertlandregistrationDtlUpload,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement1.setInt(1, id);

					preparedStatement1.setDouble(2, GetLandFee(14));

					preparedStatement1.setDouble(3, 0.00);
					preparedStatement1.setString(4, "New");
					preparedStatement1.setString(5, "N");
					preparedStatement1.setString(6, user.createdBy);
					preparedStatement1.setTimestamp(
							7,
							new java.sql.Timestamp(new java.util.Date()
									.getTime()));
					preparedStatement1.setString(8, "");
					preparedStatement1.setInt(9, year);
				
						//preparedStatement1.setDouble(10, GetLandFee(user.permitTypeId));
			
					
					preparedStatement1.setString(10,user.arrears);
					
					preparedStatement1.setString(11,user.total);
					if (user.arrears.equals("0")) {
						preparedStatement1.setInt(12,1);
						preparedStatement1.setString(13,user.createdBy);
					}else {
						preparedStatement1.setInt(12,0);
						preparedStatement1.setString(13,null);
					}
					//preparedStatement1.setDouble(11,0);

					/*
					 * if(lr.permitTypeId==15){ double balance=(lr.fee *
					 * lr.acreage)-lr.amount;
					 * preparedStatement1.setDouble(10, balance-lr.balance);
					 * preparedStatement1.setDouble(11, (lr.amount)); }else{
					 * double balance=(lr.fee)-lr.amount;
					 * preparedStatement1.setDouble(10, balance-lr.balance);
					 * preparedStatement1.setDouble(11, (lr.amount)); }
					 */
					int regId=0;
					if (preparedStatement1.executeUpdate() > 0) {
						
						if (user.regId == 0) {
							rs = preparedStatement1.getGeneratedKeys();
							rs.next();
							regId = rs.getInt(1);
						}
						DBOperations.DisposeSql(preparedStatement1);
						preparedStatement1 = connection
								.prepareStatement(Queryconstants.updateRegNo);
						preparedStatement1.setString(1, GetRegNo(regId));
						preparedStatement1.setInt(2, regId);
						if (preparedStatement1.executeUpdate() > 0) {
							return new CompasResponse(200,
									"Records Updated");
						} 
							return new CompasResponse(201,
									"fail");
						
					}
					
				}
			
			}
			return new CompasResponse(200,
					"Records Updated");
			} catch (Exception ex) {
				ex.printStackTrace();

				return new CompasResponse(404, "Exception Occured");
			} finally {
				DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			}
			
			
		}
		public boolean checkifFileExists(String fileName) {
			String sql = "Select id from file_import where file_name=? ";
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {

				connection = dataSource.getConnection();
				preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				preparedStatement.setString(1, fileName);
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

		public CompasResponse UploadLand(String filePath, String fileName, String uploadedBy, int branchId) {
			// TODO Auto-generated method stub
			ArrayList<LandRate> list = new ArrayList<LandRate>();
			ArrayList<LandRate> usrUpload = new ArrayList<LandRate>();
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			System.out.println(branchId +"branch id");
			try {
				connection = dataSource.getConnection();
				if (checkifFileExists(fileName)) {
					return new CompasResponse(204, "File Already Uploaded!!");
				}
				System.out.println("============here3=================");
				preparedStatement = connection.prepareStatement(Queryconstants.insertfileDtl);
				preparedStatement.setString(1, fileName);
				preparedStatement.setString(2, uploadedBy);
				preparedStatement.setTimestamp(3, new Timestamp(new Date().getTime()));

				if (preparedStatement.executeUpdate() > 0) {
					list = getAccountsLandExcel(filePath, uploadedBy, branchId);
				}
					for (LandRate detail : list) {
					
						usrUpload.add(detail);
					}
					
					if (usrUpload.size() > 0) {
						//System.out.println(branchId +" subcounty id");
						for (LandRate detail : usrUpload) {
				
						  if (UpdateImportLand(detail).respCode != 200) {
						 // System.out.println(UpdateImportLand(detail).respCode + "detail==============");
						 // System.out.println(UpdateImportLand(detail).respCode+ "UpdateImportLand(detail).respCode");
						  
						  return new CompasResponse(207, "Plot number"+detail.plotNumber+" already exist");
					
						 
						 }
					
						  
						
						 
						
						}
				
						}
					return new CompasResponse(200, "Uploaded Successfully  ");

					
					
			} catch (Exception ex) {
				return new CompasResponse(201, "Server Error occurred, Please try again");
			} 
			
			
			
		}
		private ArrayList<LandRate> getAccountsLandExcel(String pathToFile, String createdBy, int branchId) throws IOException {
			ArrayList<LandRate> userObjs = new ArrayList<LandRate>();

			try {

				//logger.info("FileNameInUploadExcel##" + pathToFile);
				FileInputStream file = new FileInputStream(new File(pathToFile));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				workbook.getNumberOfSheets();
				XSSFSheet sheet = workbook.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();
				
				DataFormatter df = new DataFormatter();
				for (int r = 1; r < rows; r++) {
					
					XSSFRow row = sheet.getRow(r);
					XSSFCell cell;
					LandRate userr = new LandRate();
					
					userr.name = df.formatCellValue(row.getCell(0));
					userr.nationalIdNumber = df.formatCellValue(row.getCell(1));
					userr.krapin = df.formatCellValue(row.getCell(2));
					userr.address = df.formatCellValue(row.getCell(3));
					userr.phone = df.formatCellValue(row.getCell(4));
					userr.blocknumber = df.formatCellValue(row.getCell(5));
					userr.parcelNumber =  df.formatCellValue(row.getCell(6));
					userr.acreage= df.formatCellValue(row.getCell(7));
					userr.titleDeedNumber = df.formatCellValue(row.getCell(8));
					userr.titleNature = df.formatCellValue(row.getCell(9));
					//userr.plotNumber = df.formatCellValue(row.getCell(10));
					userr.mapSheetNumber = df.formatCellValue(row.getCell(10));
					userr.rates = df.formatCellValue(row.getCell(11));
					userr.arrears = df.formatCellValue(row.getCell(12));
					userr.total = df.formatCellValue(row.getCell(13));
					userr.createdBy=createdBy;
					userr.subCountyId=branchId;
					
					userObjs.add(userr);

				}
				
				file.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return userObjs;
		}
		public ErevenueResponse UpdateImportLand(LandRate user) {

			
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			PreparedStatement preparedStatement3 = null;
			ResultSet resultSet = null;
			ResultSet rs=null;
			//String userNameFullName = user.firstName + " " + user.surname + " " + user.otherName;
			String usernamee = "";
			//String password = randomPassword();
			try {
				connection = dataSource.getConnection();
				System.out.println("============================1  "+ user.plotNumber);
			
			
			
			
				
			
			  if (checkPlotNumber(user.plotNumber)) { 
				  System.out.println("============================ 2 "+ user.mapSheetNumber);
				  return new ErevenueResponse(207,"Plot number already exists");
				  }
			  System.out.println("============================ 3 "+ user.plotNumber);

				System.out.println("============here4=================");
				preparedStatement = connection.prepareStatement(Queryconstants.insertUploadedLand,Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, user.name);
				
				preparedStatement.setString(2, user.nationalIdNumber );
				preparedStatement.setString(3, user.krapin );
				preparedStatement.setString(4, user.address);
				preparedStatement.setString(5, user.phone );
				preparedStatement.setString(6, user.blocknumber );
				preparedStatement.setString(7, user.parcelNumber );
				preparedStatement.setString(8, user.acreage);
				preparedStatement.setString(9, user.titleDeedNumber);
				preparedStatement.setString(10, user.titleNature);
				preparedStatement.setString(11, user.mapSheetNumber);
				preparedStatement.setString(12, user.mapSheetNumber);
				preparedStatement.setString(13, user.rates);
				preparedStatement.setString(14, user.arrears);
				preparedStatement.setString(15, user.total);
				preparedStatement.setString(16, "15");
				preparedStatement.setTimestamp(17, new Timestamp(new Date().getTime()));
		

				preparedStatement.setString(18, user.createdBy);
				System.out.println(user.subCountyId +"qwetu");
				preparedStatement.setInt(19, user.subCountyId);

				if(preparedStatement.executeUpdate()>0) {

					
					int id=1;
				
				  if (user.id == 0) { 
					  System.out.println(user.id +" ##############################"); 
				 
				  rs =preparedStatement.getGeneratedKeys(); 
				  rs.next(); id = rs.getInt(1); 
				  }
				 
				  DBOperations.DisposeSql(preparedStatement); 
				  System.out.println(id+" ##################3 @@@@@@@@@"); 
				  preparedStatement = connection.prepareStatement(Queryconstants.updateLandNo);
				 
				 preparedStatement.setString(1, user.titleDeedNumber);
				 preparedStatement.setInt(2, id);
				 
					System.out.println("is it skipping ");
					// @year form current date
					Calendar year2 = Calendar.getInstance();
					int year = year2.get(Calendar.YEAR);
					if (preparedStatement.executeUpdate() > 0) {
						PreparedStatement preparedStatement1 = connection.prepareStatement(
								Queryconstants.insertlandregistrationDtlUpload,
								Statement.RETURN_GENERATED_KEYS);
						preparedStatement1.setInt(1, id);

						preparedStatement1.setDouble(2, GetLandFee(15));

						preparedStatement1.setDouble(3, 0.00);
						preparedStatement1.setString(4, "New");
						preparedStatement1.setString(5, "N");
						preparedStatement1.setString(6, user.createdBy);
						preparedStatement1.setTimestamp(
								7,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement1.setString(8, "");
						preparedStatement1.setInt(9, year);
					
							//preparedStatement1.setDouble(10, GetLandFee(user.permitTypeId));
				
						
						preparedStatement1.setString(10,user.arrears);
						
						preparedStatement1.setString(11,user.total);
						if (user.arrears.equals("0")) {
							preparedStatement1.setInt(12,1);
							preparedStatement1.setString(13,user.createdBy);
						}else {
							preparedStatement1.setInt(12,0);
							preparedStatement1.setString(13,null);
						}
				
						//preparedStatement1.setDouble(11,0);

						/*
						 * if(lr.permitTypeId==15){ double balance=(lr.fee *
						 * lr.acreage)-lr.amount;
						 * preparedStatement1.setDouble(10, balance-lr.balance);
						 * preparedStatement1.setDouble(11, (lr.amount)); }else{
						 * double balance=(lr.fee)-lr.amount;
						 * preparedStatement1.setDouble(10, balance-lr.balance);
						 * preparedStatement1.setDouble(11, (lr.amount)); }
						 */
						int regId=0;
						if (preparedStatement1.executeUpdate() > 0) {
							
							if (user.regId == 0) {
								rs = preparedStatement1.getGeneratedKeys();
								rs.next();
								regId = rs.getInt(1);
							}
							DBOperations.DisposeSql(preparedStatement1);
							preparedStatement1 = connection
									.prepareStatement(Queryconstants.updateRegNo);
							preparedStatement1.setString(1, GetRegNo(regId));
							preparedStatement1.setInt(2, regId);
							if (preparedStatement1.executeUpdate() > 0) {
								return new ErevenueResponse(200,
										"Records Updated");
							} 
								return new ErevenueResponse(201,
										"fail");
							
						}
						
					}
				}
				return new ErevenueResponse(201,
						"fail");
				 
			} catch (Exception ex) {
				ex.printStackTrace();

				return new ErevenueResponse(404, "Exception Occured");
			} finally {
				DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			}
		}

}
