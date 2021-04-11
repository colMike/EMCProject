/**
 * 
 */
package com.compulynx.erevenue.dal.impl;

import com.compulynx.erevenue.dal.UserDal;
import com.compulynx.erevenue.dal.operations.AES;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class to handle the user/signup  functionalities
 * @author Anita
 * @date May 11,2016
 *
 */
public class UserDalImpl implements UserDal {

	private DataSource dataSource;

	public UserDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * checks if the username is exists
	 * @param userName
	 * @return true if exists else false
	 */
	public boolean checkUserName(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserByName);
			preparedStatement.setString(1, userName);

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
	 * checks the nationalid if exists for the specified username 
	 * @param nationalId
	 * @param userName
	 * @return true if exists else false
	 */
	public boolean checkNationalId(String nationalId,String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkIdNumber);
			preparedStatement.setString(1, nationalId);
			preparedStatement.setString(2, userName);
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
	 *updates the user details at the time user creation and user editing
	 *same function is use for saving the signup user details as well
	 *@return 200 on successful update,201 on failed ,202 on exception 
	 */
	public ErevenueResponse UpdateUser(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int userId = 0;
		try {
			connection = dataSource.getConnection();
			// connection.setAutoCommit(false);
			if (user.userId == 0) {
				if (checkUserName(user.userName)) {
					return new ErevenueResponse(201,
							"User Name is Already Exists");
				}
				if (checkNationalId(user.userNationalId,user.userName)) {
					return new ErevenueResponse(201,
							"National id number is Already Exists");
				}
				if (user.userTypeId == 3 || user.userTypeId == 4) {
					if (ValidateSignUpUser(user.userName)) {
						return new ErevenueResponse(201,
								"Agent user name is already exists as system operation user");
					}
				}
				preparedStatement = connection.prepareStatement(
						Queryconstants.insertUserDetails,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, user.userName);
				preparedStatement.setString(2, user.userFullName);
				preparedStatement.setString(3, AES.encrypt(user.userPwd));
				if (user.userTypeId == 3) {
					preparedStatement.setInt(4, GetAgentGroupId(2));
				} else if(user.userTypeId == 4){
					preparedStatement.setInt(4, GetAgentGroupId(3));
				}else {
					preparedStatement.setInt(4, user.groupId);
				}
				preparedStatement.setString(5, user.userEmail);
				preparedStatement.setString(6, user.userPhone);
				preparedStatement.setBoolean(7, user.active);
				preparedStatement.setInt(8, user.createdBy);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(10, user.userTypeId);
				preparedStatement.setString(11, user.userNationalId);
				preparedStatement.setInt(12, user.marketId);
				preparedStatement.setString(13, user.userPwd);

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateUserDeatils);
				preparedStatement.setString(1, user.userName);
				preparedStatement.setString(2, user.userFullName);
				preparedStatement.setString(3, AES.encrypt(user.userPwd));
				if (user.userTypeId == 3) {
					preparedStatement.setInt(4, GetAgentGroupId(2));
				} else if(user.userTypeId == 4){
					preparedStatement.setInt(4, GetAgentGroupId(3));
				}else {
					preparedStatement.setInt(4, user.groupId);
				}
				preparedStatement.setString(5, user.userEmail);
				preparedStatement.setString(6, user.userPhone);
				preparedStatement.setBoolean(7, user.active);
				preparedStatement.setInt(8, user.createdBy);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				preparedStatement.setInt(10, user.userTypeId);

				preparedStatement.setString(11, user.userNationalId);
				preparedStatement.setInt(12, user.marketId);
				preparedStatement.setString(13, user.userPwd);
				preparedStatement.setInt(14, user.userId);

			}
			return (preparedStatement.executeUpdate() > 0) ? new ErevenueResponse(
					200, "Records Updated") : new ErevenueResponse(201,
							"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ErevenueResponse(201, "User Name is Already Exists");
			} else {
				return new ErevenueResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checks if the nationalid is exists or not at the time of user creation
	 * @param userName
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	public boolean ValidateSignUpUser(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateSignUpUser);
			preparedStatement.setString(1, userName);

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
	 * gets the default group id for only signup user 
	 * @param userType- if userType==3 then agent user,if userType=4 then individual user
	 * @return groupId
	 */
	public int GetAgentGroupId(int userType) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int groupId = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentGroupId);
			preparedStatement.setInt(1, userType);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return groupId = resultSet.getInt("id");
			} else {
				return groupId = 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return groupId = 0;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the list of users created
	 * @return list of users
	 */
	public List<User> GetAllUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUsers);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				User objUser = new User();
				objUser.count = counter;
				objUser.userId = resultSet.getInt("ID");
				objUser.userName = resultSet.getString("UserName");
				objUser.userFullName = resultSet.getString("Name");
				objUser.userPwd = AES.decrypt(resultSet.getString("Password"));
				objUser.userEmail = resultSet.getString("Email");
				objUser.userPhone = resultSet.getString("Phone");
				objUser.groupId = resultSet.getInt("User_Group_ID");
				objUser.groupName = resultSet.getString("Group_Name");
				objUser.active = resultSet.getBoolean("status");
				objUser.createdBy = resultSet.getInt("created_By");
				objUser.userTypeId = resultSet.getInt("user_Type_Id");
				objUser.userNationalId = resultSet.getString("National_Id_no");
				objUser.marketId = resultSet.getInt("market_Id");
				objUser.status = resultSet.getString("active");
				users.add(objUser);
				counter++;
			}
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * gets the list of userTypes that is portal/pos
	 * @return list of users
	 */
	public List<UserType> GetUserTypes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserTypes);

			resultSet = preparedStatement.executeQuery();
			List<UserType> userTypes = new ArrayList<UserType>();
			while (resultSet.next()) {
				userTypes.add(new UserType(resultSet.getInt("ID"), resultSet
						.getString("type_name")));
			}
			return userTypes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 *gets the list of active usergroups
	 *@return list of usergroups
	 */
	public List<UserGroup> GetActiveGroups() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveGroups);

			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				groups.add(new UserGroup(resultSet.getInt("ID"), resultSet
						.getString("Group_name"),200));
			}
			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
