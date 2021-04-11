/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.erevenue.dal.ReportsDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.Reports;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.ZDetails;


/**
 * class for handling reports details
 * @author Anita
 * @date May 11,2016
 *
 */
public class ReportsDalImpl implements ReportsDal{
	private DataSource dataSource;

	public ReportsDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	/**
	 * gets the user transaction summary
	 * @return list of user transaction details
	 */
	public List<Reports> GetAllUserTxn() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllUserTransactions);

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports objReport= new Reports();
				objReport.count=counter;
				objReport.userName=resultSet.getString("userName");
				objReport.tickets=resultSet.getString("tickets");
				objReport.totalAmount=df.format(resultSet.getDouble("txnTotal"));
				reports.add(objReport);
				counter++;
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the device transaction summary
	 * @return list of device transaction details
	 */
	public List<Reports> GetAllDeviceTxn() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllDeviceTransactions);

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports objReport= new Reports();
				objReport.count=counter;
				objReport.serialNo=resultSet.getString("serialNo");
				objReport.tickets=resultSet.getString("tickets");
				objReport.totalAmount=df.format(resultSet.getDouble("txnTotal"));
				reports.add(objReport);
				counter++;

			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the user detailed transaction 
	 * @return list of user transaction details
	 */
	public List<Reports> GetUserTxnsDetails(Reports txnDetails) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		int counter=1;
		try {
			connection = dataSource.getConnection();
			callableStatement = connection
					.prepareCall(Queryconstants.getUserTxnsDetails);
			callableStatement.setInt(1,txnDetails.userId);
			callableStatement.setString(2, txnDetails.fromDate);
			callableStatement.setString(3, txnDetails.toDate);
			resultSet = callableStatement.executeQuery();
			List<Reports> txnList = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports objReport= new Reports();
				objReport.count=counter;
				objReport.userName=resultSet.getString("userName");
				objReport.totalAmount=df.format(resultSet.getDouble("totalCollected"));
				objReport.invalidAmount=df.format(resultSet.getDouble("totalInvalid"));
				objReport.voidAmount=df.format(resultSet.getDouble("voidAmount"));
				objReport.netAmount=df.format(resultSet.getDouble("NETAmount"));
				txnList.add(objReport);
				counter++;
			}
			return txnList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, callableStatement, resultSet);
		}
	}

	/**
	 * gets the device detailed transactions 
	 * @return list of user transaction details
	 */
	public List<Reports> GetDeviceTxnsDetails(Reports txnDetails) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		int counter=1;
		try {
			connection = dataSource.getConnection();
			callableStatement = connection
					.prepareCall(Queryconstants.getDeviceTxnsDetails);
			callableStatement.setInt(1,txnDetails.deviceId);
			callableStatement.setString(2, txnDetails.fromDate);
			callableStatement.setString(3, txnDetails.toDate);
			resultSet = callableStatement.executeQuery();
			List<Reports> txnList = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports objReport= new Reports();
				objReport.count=counter;
				objReport.totalAmount=df.format(resultSet.getDouble("totalCollected"));
				objReport.invalidAmount=df.format(resultSet.getDouble("totalInvalid"));
				objReport.voidAmount=df.format(resultSet.getDouble("voidAmount"));
				objReport.netAmount=df.format(resultSet.getDouble("NETAmount"));
				objReport.serialNo=resultSet.getString("serialNo");
				txnList.add(objReport);
				counter++;
			}
			return txnList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, callableStatement, resultSet);
		}
	}

//	/**
//	 * gets the user transaction summary
//	 * @return list of user transaction details
//	 */
//	public List<Reports> GetAllGateTxn() {
//		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		int counter=1;
//		try {
//			connection = dataSource.getConnection();
//			preparedStatement = connection
//					.prepareStatement(Queryconstants.getAllGateTransactions);
//
//			resultSet = preparedStatement.executeQuery();
//			List<Reports> reports = new ArrayList<Reports>();
//			while (resultSet.next()) {
//				Reports objReport= new Reports();
//				objReport.count=counter;
//				objReport.gateName=resultSet.getString("gateName");
//				objReport.tickets=resultSet.getString("tickets");
//				objReport.totalAmount=df.format(resultSet.getDouble("txnTotal"));
//				reports.add(objReport);
//				counter++;
//
//			}
//			return reports;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		} finally {
//			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
//		}
//	}
//
//	public List<Reports> GetGateTxnsDetails(Reports txnDetails) {
//		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
//		Connection connection = null;
//		CallableStatement callableStatement = null;
//		ResultSet resultSet = null;
//		int counter=1;
//		try {
//			connection = dataSource.getConnection();
//			callableStatement = connection
//					.prepareCall(Queryconstants.getGateTxnsDetails);
//			callableStatement.setInt(1,txnDetails.gateId);
//			callableStatement.setString(2, txnDetails.fromDate);
//			callableStatement.setString(3, txnDetails.toDate);
//
//
//			resultSet = callableStatement.executeQuery();
//			List<Reports> txnList = new ArrayList<Reports>();
//			while (resultSet.next()) {
//				Reports objReport= new Reports();
//				objReport.count=counter;
//				objReport.totalAmount=df.format(resultSet.getDouble("totalCollected"));
//				objReport.invalidAmount=df.format(resultSet.getDouble("totalInvalid"));
//				objReport.voidAmount=df.format(resultSet.getDouble("voidAmount"));
//				objReport.netAmount=df.format(resultSet.getDouble("NETAmount"));
//				objReport.gateName=resultSet.getString("gatename");
//				txnList.add(objReport);
//				counter++;
//			}
//			return txnList;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		} finally {
//			DBOperations.DisposeSql(connection, callableStatement, resultSet);
//		}
//	}

	/**
	 * gets the transaction details for selected date range
	 * @return list of transaction
	 */
	public List<Reports> GetAllTxnsDetails(Reports txnDetails) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		int counter=1;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		try {
			connection = dataSource.getConnection();
			callableStatement = connection
					.prepareCall(Queryconstants.getAllTransactions);

			callableStatement.setString(1, txnDetails.fromDate == null ? today : txnDetails.fromDate);
			callableStatement.setString(2, txnDetails.toDate == null ? today : txnDetails.toDate);
			callableStatement.setString(3, txnDetails.subcounty);


			resultSet = callableStatement.executeQuery();
			List<Reports> txnList = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports objReport= new Reports();
				objReport.count=counter;
				objReport.userName=resultSet.getString("agent");
				objReport.tickets=resultSet.getString("tickets"); // bill no
				objReport.totalAmount=df.format(resultSet.getDouble("txntotal"));
				objReport.billNo=resultSet.getString("tickets");
				objReport.gateName=resultSet.getString("market");
				objReport.serviceName=resultSet.getString("servicename");
				objReport.txnDate=resultSet.getString("transaction_date");
				objReport.serialNo=resultSet.getString("serial_no");
				objReport.status=resultSet.getString("status");
				objReport.subcounty=resultSet.getString("id");
				objReport.reprintStatus=resultSet.getString("reprint_status");
				txnList.add(objReport);
				counter++;
			}
			return txnList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, callableStatement, resultSet);
		}
	}
	
	/**
	 * gets the zdetails
	 * @return list of zdetails
	 */
	public List<ZDetails> GetZDetails(ZDetails zDetails) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		try {
			connection = dataSource.getConnection();
			
			callableStatement = connection
					.prepareCall(Queryconstants.getZDetails);

			callableStatement.setString(1, zDetails.fromDate == null ? today : zDetails.fromDate);
			callableStatement.setString(2, zDetails.toDate == null ? today : zDetails.toDate);
			callableStatement.setString(3, zDetails.subcounty);
System.out.println(zDetails.subcounty+")))))))))))");

			resultSet = callableStatement.executeQuery();
			List<ZDetails> zList = new ArrayList<ZDetails>();
			while (resultSet.next()) {
				ZDetails objZ= new ZDetails();
				objZ.zNumber=resultSet.getString("z_number");
				objZ.gateName=resultSet.getString("market");
				objZ.serialNo=resultSet.getString("device_serial");
				objZ.totOnlineTxns=resultSet.getInt("total_txn_count");
				objZ.totOnlineAmount=df.format(resultSet.getDouble("total_amount"));
				objZ.totOfflineTxns=resultSet.getInt("total_offline_txns");
				objZ.totOfflineAmount=df.format(resultSet.getDouble("total_offline_amount"));
				objZ.totInvalidTxns=resultSet.getInt("failed_txn_count");
				objZ.totInvalidAmount=df.format(resultSet.getDouble("failed_amount"));
				objZ.totVoidTxns=resultSet.getInt("void_txn_count");
				objZ.totVoidAmount=df.format(resultSet.getDouble("void_amount"));
				objZ.totNetTxns=resultSet.getInt("net_txn_count");
				objZ.totNetAmount=df.format(resultSet.getDouble("net_amount"));
				objZ.userName=resultSet.getString("username");
				objZ.zDate=resultSet.getString("created_at");
				objZ.overAllAmount=df.format(resultSet.getDouble("total_amount"));
				
				zList.add(objZ);

			}
			return zList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, callableStatement, resultSet);
		}
	}

	/**
	 * gets the transaction summary for the current date
	 * which displays on all the report at the top as tags
	 * @return list of current date transaction summary
	 */
	public List<Reports> GetCurrentTxnsDetails() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCurrentDateTxns);

			resultSet = preparedStatement.executeQuery();
			List<Reports> txnList = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports objReport= new Reports();
				objReport.totalAmount=df.format(resultSet.getDouble("totalCollected"));
				objReport.voidAmount=df.format(resultSet.getDouble("voidAmount"));
				objReport.invalidAmount=df.format(resultSet.getDouble("totalInvalid"));

				double netamount=((resultSet.getDouble("totalCollected")-resultSet.getDouble("voidAmount"))-resultSet.getDouble("totalInvalid"));
				objReport.netAmount=df.format(netamount);
				objReport.billNo=resultSet.getString("totalBills");
				txnList.add(objReport);
			}
			return txnList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

//	//service wise report
//	public List<Service> GetParentServices() {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = dataSource.getConnection();
//			preparedStatement = connection
//					.prepareStatement(Queryconstants.getParentServices);
//
//			resultSet = preparedStatement.executeQuery();
//			List<Service> services = new ArrayList<Service>();
//			while (resultSet.next()) {
//				services.add(new Service(
//						resultSet.getInt("ID"),
//						resultSet.getString("ServiceCode"),
//						resultSet.getString("ServiceName"),
//						resultSet.getBoolean("Active"),
//						resultSet.getInt("CreatedBy"),
//						200,
//						(resultSet.getBoolean("Active") == true ? "Active" : "Inactive"),
//						0
//						));
//			}
//			return services;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		} finally {
//			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
//		}
//	}
//
//	//service wise report
//	public List<Service> GetSubServices(int parentServiceId) {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = dataSource.getConnection();
//			preparedStatement = connection
//					.prepareStatement(Queryconstants.getSubServices);
//			preparedStatement.setInt(1,parentServiceId);
//			resultSet = preparedStatement.executeQuery();
//			List<Service> services = new ArrayList<Service>();
//			while (resultSet.next()) {
//				/*services.add(new Service(resultSet.getInt("ID"), resultSet
//							.getString("subServiceName"), resultSet
//							.getString("subServiceName"), resultSet
//							.getBoolean("Active"), resultSet.getInt("CreatedBy"),
//							200,( resultSet
//									.getBoolean("Active")==true ? "Active" :"Inactive")));*/
//				services.add(new Service(
//						resultSet.getInt("ID"), 
//						resultSet.getString("subServiceName"), 
//						resultSet.getString("subServiceName"), 
//						resultSet.getBoolean("Active"), 
//						resultSet.getInt("CreatedBy"),
//						200,
//						(resultSet.getBoolean("Active")==true ? "Active" :"Inactive"),
//						0));
//			}
//			return services;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		} finally {
//			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
//		}
//	}

//	public List<Reports> GetAllServiceTxn() {
//		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		int counter=1;
//		try {
//			connection = dataSource.getConnection();
//			preparedStatement = connection
//					.prepareStatement(Queryconstants.getAllServiceTransactions);
//
//			resultSet = preparedStatement.executeQuery();
//			List<Reports> reports = new ArrayList<Reports>();
//			while (resultSet.next()) {
//				Reports objReport= new Reports();
//				objReport.count=counter;
//				objReport.serviceName=resultSet.getString("ServiceName");
//				objReport.tickets=resultSet.getString("tickets");
//				objReport.totalAmount=df.format(resultSet.getDouble("txnTotal"));
//				reports.add(objReport);
//				counter++;
//			}
//			return reports;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		} finally {
//			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
//		}
//	}
//
//	public List<Reports> GetServiceTxnsDetails(Reports txnDetails) {
//		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
//		Connection connection = null;
//		CallableStatement callableStatement = null;
//		ResultSet resultSet = null;
//		int counter=1;
//		try {
//			connection = dataSource.getConnection();
//			callableStatement = connection
//					.prepareCall(Queryconstants.getServiceTxnsDetails);
//			callableStatement.setInt(1,txnDetails.parentSerId);
//			callableStatement.setInt(2,txnDetails.subSerId);
//			callableStatement.setString(3, txnDetails.fromDate);
//			callableStatement.setString(4, txnDetails.toDate);
//
//
//			resultSet = callableStatement.executeQuery();
//			List<Reports> txnList = new ArrayList<Reports>();
//			while (resultSet.next()) {
//				Reports objReport= new Reports();
//				objReport.count=counter;
//				objReport.totalAmount=df.format(resultSet.getDouble("totalCollected"));
//				objReport.invalidAmount=df.format(resultSet.getDouble("totalInvalid"));
//				objReport.voidAmount=df.format(resultSet.getDouble("voidAmount"));
//				objReport.netAmount=df.format(resultSet.getDouble("NETAmount"));
//				objReport.tickets=resultSet.getString("bill_no");
//				//objReport.totalAmount=df.format(resultSet.getDouble("txnTotal"));
//				//objReport.billNo=resultSet.getString("tickets");
//				objReport.subSerName=resultSet.getString("SubserviceName");
//				objReport.serviceName=resultSet.getString("ParentSerName");
//				//objReport.txnDate=resultSet.getString("transaction_date");
//				//objReport.serialNo=resultSet.getString("serialNo");
//				//objReport.status=resultSet.getString("status");
//				txnList.add(objReport);
//				counter++;
//			}
//			return txnList;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		} finally {
//			DBOperations.DisposeSql(connection, callableStatement, resultSet);
//		}
//	}
	
}
