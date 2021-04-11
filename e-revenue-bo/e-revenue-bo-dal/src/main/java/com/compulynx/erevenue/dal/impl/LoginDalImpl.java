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

import com.compulynx.erevenue.dal.LoginDal;
import com.compulynx.erevenue.dal.operations.AES;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ConfigParams;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LoginSession;
import com.compulynx.erevenue.models.LoginUser;
import com.compulynx.erevenue.models.Rights;
import com.compulynx.erevenue.models.RightsDetail;

/**
 * Login class
 * 
 * @author Anita Created on 09 Aprial 2016
 */
public class LoginDalImpl implements LoginDal {
	private DataSource dataSource;

	public LoginDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * User Manual Login
	 * 
	 * @param username
	 * @param password
	 * @return Loginuser returns the userid if successfull
	 */
	public LoginUser GetUserIdManual(String userName, String password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserCredentialManual);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, AES.encrypt(password));
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				if (resultSet.getBoolean("status") == false) {
					return new LoginUser(resultSet.getInt("UserID"), 202);
				} else {
					return new LoginUser(resultSet.getInt("UserID"), 200);
				}
			} else {
				return new LoginUser(201);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * Get user assigned rights Gets the rights of user and creates menu as per
	 * selected rights
	 * @param userid
	 * @return LoginSession the list of rights
	 */
	public LoginSession GetUserAssgnRightsList(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserGrpRights);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			List<Rights> objlist = new ArrayList<Rights>();
			LoginSession loginSession = new LoginSession();

			// Using to check Previous Header id if the ID's are the same, if
			// not create new header object

			int headerId = 0;
			Rights objRights = null;
			while (resultSet.next()) {
				// The Values Remain the Same, No Harm Reassigning
				if (resultSet.getBoolean("status") == false) {
					loginSession.setRespCode(202);
					return loginSession;
				}
				loginSession.setLinkId(resultSet.getInt("LinkId"));
				loginSession.setSessionId(resultSet.getInt("ID"));
				loginSession.setUserGroupId(resultSet.getInt("UserGroupID"));
				loginSession.setLinkName(resultSet.getString("LinkName"));
				loginSession.setSessionName(resultSet.getString("UserName"));
				loginSession.setSessionFullName(resultSet.getString("name"));
				loginSession.setLinkExtInfo(resultSet.getString("LinkExtInfo"));
				loginSession.setAppName(resultSet.getString("appname"));
				if (!(headerId == resultSet.getInt("HeaderID"))) {
					// Avoiding adding Null Object On First loop
					if (!(objRights == null)) {
						objlist.add(objRights);
					}
					objRights = new Rights(resultSet.getString("Header_Name"),
							resultSet.getString("Header_Icon_Css"),
							resultSet.getString("Header_Icon_Color"));
				}
				if (!(objRights == null)) {
					objRights.rightsList.add(new RightsDetail(resultSet
							.getString("Right_Display_Name"), resultSet
							.getString("Right_Short_Code"), resultSet
							.getString("Right_View_Name"), resultSet
							.getString("Right_Name"), resultSet
							.getBoolean("Allow_Add"), resultSet
							.getBoolean("Allow_Edit"), resultSet
							.getBoolean("Allow_Delete"), resultSet
							.getBoolean("Allow_View"), resultSet
							.getInt("Right_Max_Width")));
				}
				headerId = resultSet.getInt("HeaderID");
			}
			if (!(objRights == null)) {
				objlist.add(objRights);
			}
			loginSession.setRightsList(objlist);
			loginSession.setRespCode(200);
			return loginSession;
		} catch (Exception ex) {
			System.out.println(ex);
			return new LoginSession(500);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the list of config params
	 * @return list of config params
	 */
	public List<ConfigParams> GetConfigParams() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getConfigParams);

			resultSet = preparedStatement.executeQuery();
			List<ConfigParams> config = new ArrayList<ConfigParams>();
			while (resultSet.next()) {
				config.add(new ConfigParams(resultSet.getInt("Id"), resultSet
						.getString("name"), resultSet.getString("value"),
						resultSet.getInt("created_by"), resultSet
								.getString("value")));
			}
			return config;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * update the details of config params
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	public ErevenueResponse UpdateConfigParam(ConfigParams params) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			preparedStatement = connection
					.prepareStatement(Queryconstants.updateConfigParam);
			//preparedStatement2 = connection
					//.prepareStatement(Queryconstants.insertLogTable);
			for (int i = 0; i < params.configParams.size(); i++) {

				preparedStatement
						.setString(1, params.configParams.get(i).value);
				preparedStatement.setInt(2, params.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement
						.setInt(4, params.configParams.get(i).configId);
				if (preparedStatement.executeUpdate() <= 0) {
					throw new Exception("Failed to Insert Param Id "
							+ params.configParams.get(i).configId);
				}
			}
			DBOperations.DisposeSql(preparedStatement);
			preparedStatement = connection
					.prepareStatement(Queryconstants.updateConfigParamLogo);
			preparedStatement.setString(1, params.logo);
			preparedStatement.executeUpdate();
//			for (int j = 0; j < params.configParams.size(); j++) {
//				preparedStatement2.setString(1, "ConfigParams");
//				preparedStatement2
//						.setString(2, params.configParams.get(j).name);
//				preparedStatement2.setString(3,
//						params.configParams.get(j).oldValue);
//				preparedStatement2.setString(4,
//						params.configParams.get(j).value);
//				preparedStatement2.setInt(5, params.createdBy);
//				preparedStatement2.setTimestamp(6, new java.sql.Timestamp(
//						new java.util.Date().getTime()));
//
//				if (preparedStatement2.executeUpdate() <= 0) {
//					throw new Exception("Failed to Insert Param Id "
//							+ params.configParams.get(j).configId);
//				}
//			}
			connection.commit();
			return new ErevenueResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ErevenueResponse(201, "Failed to update");
			} else {
				return new ErevenueResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	

	
}
