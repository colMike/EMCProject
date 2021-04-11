/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.erevenue.dal.SubCountyDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.SubCounty;
import com.compulynx.erevenue.models.Ward;

/**
 * class to handle the functionality of subcounty
 * 
 * @author Anita
 * @date Apr 12, 2016
 *
 */
public class SubCountyDalImpl implements SubCountyDal {

	private DataSource dataSource;

	public SubCountyDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * updates the subcounty details Note:if the suncounty is
	 * deactivated/activated automatically the wards under that subcounty will
	 * be deactivated/activated and also the markets under those wards will be
	 * deactivated/activated
	 * 
	 * @return 200 on successful,201 on failed and 202 on exception
	 */
	@Override
	public ErevenueResponse UpdateSubCounty(SubCounty subCounty) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		int wardId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (subCounty.subCountyId == 0) {
				if (checkSubCountyName(subCounty.subCountyName)) {
					return new ErevenueResponse(201,
							"Sub-County name already exists");
				}
				if (checkSubCountyCode(subCounty.subCountyCode)) {
					return new ErevenueResponse(201,
							"Sub-County code already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertSubCounty);
				preparedStatement.setString(1, subCounty.subCountyCode);
				preparedStatement.setString(2, subCounty.subCountyName);
				preparedStatement.setBoolean(3, subCounty.active);
				preparedStatement.setInt(4, subCounty.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				//preparedStatement.executeUpdate();
			} else {
				if (checkSubCountyNameByCode(subCounty.subCountyName,
						subCounty.subCountyCode)) {
					return new ErevenueResponse(201,
							"Sub-County name already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateSubCounty);
				preparedStatement.setString(1, subCounty.subCountyName);
				preparedStatement.setBoolean(2, subCounty.active);
				preparedStatement.setInt(3, subCounty.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, subCounty.subCountyId);

				
				/**
				 * activates/deactivates the wards under subcounty
				 */
				preparedStatement1 = connection 
						.prepareStatement(Queryconstants.actiDeactiWardsBzySubCounty);
				preparedStatement1.setBoolean(1, subCounty.active);
				preparedStatement1.setInt(2, subCounty.createdBy);
				preparedStatement1.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement1.setInt(4, subCounty.subCountyId);
				preparedStatement1.executeUpdate();
				/**
				 * activates/deactivates the markets under wards
				 */
				DBOperations.DisposeSql(preparedStatement1);
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.getWardBySubCountyId);
				preparedStatement2.setInt(1, subCounty.subCountyId);
				resultSet = preparedStatement2.executeQuery();
				ArrayList<Ward> wardList = new ArrayList<Ward>();
				while (resultSet.next()) {
					Ward objWard = new Ward();
					objWard.wardId = resultSet.getInt("id");
					wardList.add(objWard);
				}
				DBOperations.DisposeSql(preparedStatement2, resultSet);
				for (int i = 0; i < wardList.size(); i++) {
					preparedStatement3 = connection
							.prepareStatement(Queryconstants.actiDeactiMarketsByWard);
					preparedStatement3.setBoolean(1, subCounty.active);
					preparedStatement3.setInt(2, subCounty.createdBy);
					preparedStatement3.setTimestamp(3, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement3.setInt(4, wardList.get(i).wardId);
					preparedStatement3.executeUpdate();

				}
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
			return new ErevenueResponse(202, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			DBOperations.DisposeSql(preparedStatement3);
		}
	}

	/**
	 * gets the list of all the subcounties
	 *
	 * @return list of subcounties
	 */
	@Override
	public List<SubCounty> GetAllSubCounties() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getSubcounties);
			resultSet = preparedStatement.executeQuery();
			List<SubCounty> subCounties = new ArrayList<SubCounty>();
			while (resultSet.next()) {
				subCounties.add(new SubCounty(resultSet.getInt("ID"), resultSet
						.getString("Code"), resultSet.getString("Name"),
						resultSet.getBoolean("active"), 200, count, resultSet
								.getString("status")));
				count++;
			}
			return subCounties;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checks the subcounty name if exists at the time of subcounty creation
	 * 
	 * @param subCountyName
	 * @return true if exists or false
	 */
	public boolean checkSubCountyName(String subCountyName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkSubCountyName);
			preparedStatement.setString(1, subCountyName);

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
	 * checks the subcounty code if exists at the time of subcounty creation
	 * 
	 * @param subCountyName
	 * @return true if exists or false
	 */
	public boolean checkSubCountyCode(String subCountyCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkSubCountycode);
			preparedStatement.setString(1, subCountyCode);

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
	 * checks the subcounty name if exists at the time of editing subcounty
	 * 
	 * @param subCountyName
	 * @param subCountyCode
	 * @return true if exists or false
	 */
	public boolean checkSubCountyNameByCode(String subCountyName,
			String subCountyCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkSubCountyNameByCode);
			preparedStatement.setString(1, subCountyName);
			preparedStatement.setString(2, subCountyCode);

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

}
