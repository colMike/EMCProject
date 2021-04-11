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

import com.compulynx.erevenue.dal.WardDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.SubCounty;
import com.compulynx.erevenue.models.Ward;

/**
 * class to handle the functionalities of ward
 * @author Anita
 * @date Apr 13, 2016
 *
 */
public class WardDalImpl implements WardDal {
	private DataSource dataSource;

	public WardDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	/**
	 * updates the ward details at the time of creating the new ward and editing the ward deatils
	 * Note:if the wards is deactivated/activated then the markets under those
	 * wards will be deactivated/activated
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	@Override
	public ErevenueResponse UpdateWard(Ward ward) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (ward.wardId == 0) {
				if (checkWardName(ward.wardName)) {
					return new ErevenueResponse(201, "Ward name already exists");
				}
				if (checkWardCode(ward.wardCode)) {
					return new ErevenueResponse(201, "Ward code already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertWard);
				preparedStatement.setString(1, ward.wardCode);
				preparedStatement.setString(2, ward.wardName);
				preparedStatement.setInt(3, ward.subCountyId);
				preparedStatement.setBoolean(4, ward.active);
				preparedStatement.setInt(5, ward.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (checkWardNameByCode(ward.wardName, ward.wardCode)) {
					return new ErevenueResponse(201, "Ward name already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateWard);
				preparedStatement.setString(1, ward.wardName);
				preparedStatement.setInt(2, ward.subCountyId);
				preparedStatement.setBoolean(3, ward.active);
				preparedStatement.setInt(4, ward.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, ward.wardId);
				/**
				 * deactivates/activates the markets under the ward if ward is deactivated
				 */
				//if (preparedStatement.executeUpdate() > 0) {
					//DBOperations.DisposeSql(preparedStatement, resultSet);
					preparedStatement1 = connection
							.prepareStatement(Queryconstants.actiDeactiMarketsByWard);
					preparedStatement1.setBoolean(1, ward.active);
					preparedStatement1.setInt(2, ward.createdBy);
					preparedStatement1.setTimestamp(3, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement1.setInt(4, ward.wardId);
					preparedStatement1.executeUpdate();
				//}

			}
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
			DBOperations.DisposeSql(preparedStatement1);
		}
	}
	
	/**
	 *gets the list of all wards
	 *@return list of wards 
	 */
	@Override
	public List<Ward> GetAllWards() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getWards);
			resultSet = preparedStatement.executeQuery();
			List<Ward> wards = new ArrayList<Ward>();
			while (resultSet.next()) {
				wards.add(new Ward(resultSet.getInt("ID"), resultSet
						.getString("ward_Code"), resultSet
						.getString("ward_Name"), resultSet
						.getInt("sub_county_id"), resultSet.getString("name"),
						resultSet.getBoolean("active"), 200, count, resultSet
								.getString("status")));
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
	 * gets the list of active subcounties
	 * @return list of subcounties
	 */
                
	public List<SubCounty> GetActiveSubCounties() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveSubCounties);
			resultSet = preparedStatement.executeQuery();
			List<SubCounty> subcounties = new ArrayList<SubCounty>();
			while (resultSet.next()) {
				subcounties.add(new SubCounty(resultSet.getInt("ID"), resultSet
						.getString("name")));
				count++;
			}
			return subcounties;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * checks if the ward name is exists at the time of creating new ward
	 * @param wardName
	 * @return true if exists else false
	 */
	public boolean checkWardName(String wardName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkWardName);
			preparedStatement.setString(1, wardName);
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
	 * checks if the ward code is exists at the time of creating new ward
	 * @param wardCode
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	public boolean checkWardCode(String wardCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkWardCode);
			preparedStatement.setString(1, wardCode);

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
	 * checks if the wardname is exists at the time of edting the wards details
	 * @param wardName
	 * @param Code
	 * @return
	 */
	public boolean checkWardNameByCode(String wardName, String Code) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkWardNameByCode);
			preparedStatement.setString(1, wardName);
			preparedStatement.setString(2, Code);

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

	public List<Ward> gtWardBySubCounty(int subcountyId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getWardbySubCounty);
			preparedStatement.setInt(1, subcountyId);
			resultSet = preparedStatement.executeQuery();
			List<Ward> subSer = new ArrayList<Ward>();
			while (resultSet.next()) {
				Ward wa= new Ward();
				wa.wardName=resultSet.getString("ward_name");
				wa.wardId=resultSet.getInt("id");
				subSer.add(wa);
		
				count++;
			}
			return subSer;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}

	

