package com.compulynx.erevenue.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.erevenue.dal.UserGroupDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.RightsDetail;
import com.compulynx.erevenue.models.UserGroup;

/**
 * The Class UserGroupDalImpl.
 */
public class UserGroupDalImpl implements UserGroupDal{
	
	private DataSource dataSource;
	public UserGroupDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * gets the list of usergroups created and rights for those groups
	 * @return list of usergroups
	 */
	public List<UserGroup> GetUserGroups() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int counter=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroups);

			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				UserGroup objGroup = new UserGroup();
				objGroup.count=counter;
				objGroup.groupId = resultSet.getInt("ID");
				objGroup.groupName = resultSet.getString("Group_Name");
				objGroup.active = resultSet.getBoolean("status");
				objGroup.createdBy = resultSet.getInt("created_By");
				objGroup.roleTypeId = resultSet.getInt("group_type");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getGroupById);
				preparedStatement.setInt(1, objGroup.groupId);
				preparedStatement.setInt(2, objGroup.groupId);
				preparedStatement.setInt(3, objGroup.groupId);
				resultSet2 = preparedStatement.executeQuery();
				List<RightsDetail> rightList = new ArrayList<RightsDetail>();
				while (resultSet2.next()) {
					rightList
							.add(new RightsDetail(
									resultSet2.getInt("Right_ID"),
									resultSet2.getString("Right_Display_Name"),
									resultSet2.getBoolean("RightAdd"),
									resultSet2.getBoolean("RightEdit"),
									resultSet2.getBoolean("RightDelete"),
									resultSet2.getBoolean("RightView"),
									((resultSet2.getBoolean("Allow_View") == false) ? false
											: resultSet2.getBoolean("Allow_Add")),
									((resultSet2.getBoolean("Allow_View") == false) ? false
											: resultSet2
													.getBoolean("Allow_Edit")),
									((resultSet2.getBoolean("Allow_View") == false) ? false
											: resultSet2
													.getBoolean("Allow_Delete")),
									resultSet2.getBoolean("Allow_View"), 200));
				}
				objGroup.rights = rightList;
				groups.add(objGroup);
				counter++;
			}
			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * updates the usergroup details at the time of creating new group and editing the group details
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	public ErevenueResponse UpdateUserGroup(UserGroup group) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int groupId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (group.active == false) {
				if (CheckUserAssignGroup(group.groupId)) {
					return new ErevenueResponse(201, "Group Already Assign to user,cannot deactivate");
				}
			}
			if (group.groupId == 0) {

				if (checkGroupByName(group.groupName)) {
					return new ErevenueResponse(201, "Group Name is Already Exists");
				}
				preparedStatement = connection.prepareStatement(
						Queryconstants.insertUserGroup,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, group.groupName);
				preparedStatement.setBoolean(2, group.active);
				preparedStatement.setInt(3, group.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, group.roleTypeId);

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateUserGroup);
				preparedStatement.setString(1, group.groupName);
				preparedStatement.setBoolean(2, group.active);
				preparedStatement.setInt(3, group.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, group.roleTypeId);
				preparedStatement.setInt(6, group.groupId);
				groupId = group.groupId;

			}
			if (preparedStatement.executeUpdate() > 0) {

				// Dispose
				if (group.groupId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					groupId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteGroupRights);
				preparedStatement.setInt(1, groupId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertGroupRights);
				for (int i = 0; i < group.rights.size(); i++) {
					preparedStatement.setInt(1, group.rights.get(i).rightId);
					preparedStatement.setInt(2, groupId);
					preparedStatement.setBoolean(3,
							group.rights.get(i).rightView);
					preparedStatement.setBoolean(4,
							group.rights.get(i).rightAdd);
					preparedStatement.setBoolean(5,
							group.rights.get(i).rightEdit);
					preparedStatement.setBoolean(6,
							group.rights.get(i).rightDelete);
					preparedStatement.setInt(7, group.rights.get(i).createdBy);
					preparedStatement.setTimestamp(8, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Right Id "
								+ group.rights.get(i).rightId);
					}
				}
				connection.commit();
				return new ErevenueResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new ErevenueResponse(201, "Nothing To Update");
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ErevenueResponse(201, "Group Name is Already Exists");
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
	 * checks the group name if exists or not at the time of creating new group
	 * @param groupName
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	public boolean checkGroupByName(String groupName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupByName);
			preparedStatement.setString(1, groupName);
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
	 * checks if the group is already assign to the user then cannot deactivate.
	 * @param groupId
	 * @return true if exists or false
	 */
	public boolean CheckUserAssignGroup(int groupId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkUserAssignGroup);
			preparedStatement.setInt(1, groupId);
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
	 * gets the list of rights 
	 * @return list of rights
	 */
	public List<RightsDetail> GetRights() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRights);

			resultSet = preparedStatement.executeQuery();
			List<RightsDetail> rights = new ArrayList<RightsDetail>();
			while (resultSet.next()) {
				rights.add(new RightsDetail(resultSet.getInt("ID"), resultSet
						.getString("Right_Display_Name"), resultSet
						.getBoolean("Allow_View"), ((resultSet
						.getBoolean("Allow_View") == false) ? false : resultSet
						.getBoolean("Allow_Add")), ((resultSet
						.getBoolean("Allow_View") == false) ? false : resultSet
						.getBoolean("Allow_Edit")), ((resultSet
						.getBoolean("Allow_View") == false) ? false : resultSet
						.getBoolean("Allow_Delete")), 200

				));
			}
			return rights;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}
