/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import com.compulynx.erevenue.dal.PermitTypeDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.PermitType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * class to handle the functionality of permit types creation
 * @author Anita
 * @date Apr 14, 2016
 *
 */
public class PermitTypeDalImpl implements PermitTypeDal{
	private DataSource dataSource;

	public PermitTypeDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	/**
	 * update the details of permit types
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	@Override
	public ErevenueResponse UpdatePermitType(PermitType permitType) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (permitType.permitTypeId == 0) {
				if (CheckPermitTypeName(permitType.permitTypeName)) {
					return new ErevenueResponse(201,
							"Permit type name already exists");
				}
				if (CheckPermitTypeCode(permitType.permitTypeCode)) {
					return new ErevenueResponse(201,
							"Permit type code already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertPermitType);
				preparedStatement.setString(1, permitType.permitTypeCode);
				preparedStatement.setString(2, permitType.permitTypeName);
				preparedStatement.setDouble(3, permitType.permitFee);
				preparedStatement.setBoolean(4, permitType.active);
				preparedStatement.setInt(5, permitType.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(7, permitType.permitType);

			} else {
				if (CheckPermitTypeNameByCode(permitType.permitTypeName,permitType.permitTypeCode)) {
					return new ErevenueResponse(201,
							"Permit Type name already exists");
				}
				preparedStatement = 
						connection
						.prepareStatement(Queryconstants.updatePermitType);
				preparedStatement.setString(1, permitType.permitTypeName);
				preparedStatement.setDouble(2, permitType.permitFee);
				preparedStatement.setBoolean(3, permitType.active);
				preparedStatement.setInt(4, permitType.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(6, permitType.permitType);
				preparedStatement.setInt(6, permitType.permitTypeId);

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
		}
	}

	/**
	 * gets the list of all permit types
	 * @return list of permit types
	 */
	@Override
	public List<PermitType> GetAllPermitTypes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getPermitTypes);
			resultSet = preparedStatement.executeQuery();
			List<PermitType> permittypes = new ArrayList<PermitType>();
			while (resultSet.next()) {
				PermitType permitType=new PermitType();

				permitType.permitTypeId=resultSet.getInt("ID");
				permitType.permitTypeCode=resultSet.getString("permit_type_Code");
				permitType.permitTypeName=resultSet.getString("permit_type_Name");
				permitType.permitType=resultSet.getString("permit_type_code");
				permitType.permitFee=resultSet.getDouble("permit_fee");
				permitType.active=resultSet.getBoolean("active");
				permitType.respCode=200;
				permitType.count=count;
				permittypes.add(permitType);
				count++;
			}
			return permittypes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


	/**
	 * checks the permit type name when creating new application
	 * @param permitTypeName
	 * @return true if exists else false
	 */
	public boolean CheckPermitTypeName(String permitTypeName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPermitTypeName);
			preparedStatement.setString(1, permitTypeName);

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
	 * checks if the permit type code exists when creating new application
	 * @param permitTypeCode
	 * @return true if exists else false
	 */
	public boolean CheckPermitTypeCode(String permitTypeCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPermitTypecode);
			preparedStatement.setString(1, permitTypeCode);

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
	 * checks the if the permit type name exists when updating details
	 * @param permitTypeName
	 * @param permitTypeCode
	 * @return true if exists else false
	 */
	public boolean CheckPermitTypeNameByCode(String permitTypeName, String permitTypeCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPermitNameByCode);
			preparedStatement.setString(1, permitTypeName);
			preparedStatement.setString(2, permitTypeCode);

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
