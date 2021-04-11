package com.compulynx.erevenue.dal.impl;

import com.compulynx.erevenue.dal.DashBoardDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.DashBoard;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * class for handling dashboard functionality
 * @author Anita
 * @date May 10, 2016
 *
 */
public class DashBoardDalImpl implements DashBoardDal {


	private DataSource dataSource;

	public DashBoardDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	/**
	 *gets the count details for users,markets,devices and services
	 *@return list of count details 
	 */
	public List<DashBoard> GetDashBoardCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDashBoardDetailCount);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getInt("COUNTNO"), resultSet
						.getString("Name"),
						 200));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the details of monthly statistics for subcounty
	 * @return the details of current,last and two months ago details for month
	 */
	public DashBoard GetMonthStatisticsDetails() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCurrentMonthStatistics);

			resultSet = preparedStatement.executeQuery();
			DashBoard detail = new DashBoard();
			List<DashBoard> currentMonth = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				DashBoard obj= new DashBoard();
				obj.subCountyName=resultSet.getString("name");
				obj.amount=df.format(resultSet.getDouble("total"));
				currentMonth.add(obj);
			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getPreviousMonthStatistics);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> previousMonth = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				DashBoard obj= new DashBoard();
				obj.subCountyName=resultSet.getString("name");
				obj.amount=df.format(resultSet.getDouble("total"));
				previousMonth.add(obj);
			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTwoMonthStatistics);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> twoMonth = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				DashBoard obj= new DashBoard();
				obj.subCountyName=resultSet.getString("name");
				obj.amount=df.format(resultSet.getDouble("total"));
				twoMonth.add(obj);
			}
			detail.currentMonth=currentMonth;
			detail.previousMonth=previousMonth;
			detail.twoMonth=twoMonth;
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the top user statistics in terms of collection
	 * @return the detail list of user statistics 
	 */
	public DashBoard GetUserStatisticsDetails() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserDscStatistics);

			resultSet = preparedStatement.executeQuery();
			DashBoard detail = new DashBoard();
			List<DashBoard> userDsc = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				DashBoard obj= new DashBoard();
				obj.userName=resultSet.getString("name");
				obj.tickets=resultSet.getInt("tickets");
				obj.amount=df.format(resultSet.getDouble("amount"));
				userDsc.add(obj);
			}
			DBOperations.DisposeSql(preparedStatement, resultSet);
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserAscStatistics);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> userAsc = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				DashBoard obj= new DashBoard();
				obj.userName=resultSet.getString("name");
				obj.tickets=resultSet.getInt("tickets");
				obj.amount=df.format(resultSet.getDouble("amount"));
				userAsc.add(obj);
			}
			
			detail.userDsc=userDsc;
			detail.userAsc=userAsc;
			
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the service details for piechart
	 * @return list of services and the percentage of transaction done
	 */
	public List<DashBoard> GetFlowChartCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getFlowChartDetails);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getString("service"), resultSet
						.getInt("trans_count")));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets details of transactions
	 * @return valid,invalid,void,net amounts for current date
	 */
	public List<DashBoard> GetDashBoardAmountDetail() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAmountDetails);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
			DashBoard obj =  new DashBoard();
			obj.detailDescription=resultSet.getString("name");
			obj.amount=df.format(resultSet.getDouble("countno"));
			//System.out.println("Amount##"+obj.amount);
			detail.add(obj);
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets the details of transactions bar chart for the current month
	 * @return valid,invalid,void,net amounts details
	 */
	public List<DashBoard> GetTransChartDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTransChartDetail);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> chartDetail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				chartDetail.add(new DashBoard(resultSet.getString("month"),
						resultSet.getDouble("TOTALAMOUNT"),
						resultSet.getDouble("VOIDAMOUNT"),
						resultSet.getDouble("INVALIDAMOUNT"),
						resultSet.getDouble("NETAMOUNT")));
			}
			return chartDetail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets details of total collections
	 * @return list of total collections 
	 */
	public List<DashBoard> GetDashBoardCollectionDetail() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTotalCollectionInfo);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
			DashBoard obj =  new DashBoard();
			obj.detailDescription=resultSet.getString("name");
			obj.amount=df.format(resultSet.getDouble("collection"));
			obj.totalTxns=resultSet.getDouble("collection");
			detail.add(obj);
			}
			
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}
