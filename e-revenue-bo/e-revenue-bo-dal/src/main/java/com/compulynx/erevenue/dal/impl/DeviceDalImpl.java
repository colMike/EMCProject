package com.compulynx.erevenue.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.erevenue.dal.DeviceDal;
import com.compulynx.erevenue.dal.operations.DBOperations;
import com.compulynx.erevenue.dal.operations.Queryconstants;
import com.compulynx.erevenue.models.Device;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;

/**
 * class for handling device settings
 * @author Anita
 * @date May 10,2016
 *
 */
public class DeviceDalImpl implements DeviceDal{
	private DataSource dataSource;

	public DeviceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	/**
	 * checks the device serial no if exists
	 * @param serialNo
	 * @return true if exists else false
	 */
	public boolean checkDevicedSerialNo(String serialNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDeviceSerialNo);
			preparedStatement.setString(1, serialNo);

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
	 * checks the device serial no at the time of updating the device details
	 * @param serialNo
	 * @param id
	 * @return true if exists else false
	 */
	public boolean checkDevicedSerialNoById(String serialNo,int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDeviceSerialNoById);
			preparedStatement.setString(1, serialNo);
			preparedStatement.setInt(2, id);
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
	
//	/**
//	 * 
//	 * @param userId
//	 * @param deviceId
//	 * @return
//	 */
//	public boolean checkUserDeviceSerialNo(int userId,int deviceId) {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = dataSource.getConnection();
//			preparedStatement = connection
//					.prepareStatement(Queryconstants.getUserDeviceSerialNo);
//			preparedStatement.setInt(1, userId);
//			preparedStatement.setInt(2, deviceId);
//			resultSet = preparedStatement.executeQuery();
//
//			if (resultSet.next()) {
//				return true;
//			} else {
//				return false;
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return false;
//		} finally {
//			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
//		}
//	}
	
//	/**
//	 * 
//	 * @param devId
//	 * @return
//	 */
//	public boolean checkDeviceExists(int devId) {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = dataSource.getConnection();
//			preparedStatement = connection
//					.prepareStatement(Queryconstants.getDeviceExists);
//			preparedStatement.setInt(1, devId);
//
//			resultSet = preparedStatement.executeQuery();
//
//			if (resultSet.next()) {
//				return true;
//			} else {
//				return false;
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return false;
//		} finally {
//			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
//		}
//	}
	
	/**
	 * check if the device is already assign to the user so cannot deactivate
	 * @param deviceId
	 * @return true if exists else false
	 */
	public boolean checkUserAssignDevice(int deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkUserAssignDevice);
			preparedStatement.setInt(1, deviceId);

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
	 * Update the device registration info 
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	public ErevenueResponse UpdateDeviceInfo(Device deviceInfo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (deviceInfo.status == false) {
				if (checkUserAssignDevice(deviceInfo.id)) {
					return new ErevenueResponse(201, "Device Already Assign to user cannot deactivate");
				}
			}
			if (deviceInfo.id == 0) {
				if (checkDevicedSerialNo(deviceInfo.serialNo)) {
					return new ErevenueResponse(201, "SerialNo Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertDeviceInfo);
				preparedStatement.setString(1, deviceInfo.serialNo);
				preparedStatement.setBoolean(2, deviceInfo.status);
				preparedStatement.setInt(3, deviceInfo.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {
				if (checkDevicedSerialNoById(deviceInfo.serialNo,deviceInfo.id)) {
					return new ErevenueResponse(201, "SerialNo Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateDeviceInfo);
				preparedStatement.setString(1, deviceInfo.serialNo);
				preparedStatement.setBoolean(2, deviceInfo.status);
				preparedStatement.setInt(3, deviceInfo.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, deviceInfo.id);
			}
			return (preparedStatement.executeUpdate() > 0) ? new ErevenueResponse(
					200, "Records Updated") : new ErevenueResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ErevenueResponse(201, "Serial No Already Exists");
			} else {
				return new ErevenueResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ErevenueResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection,preparedStatement,resultSet);
		}
	}

	/**
	 * gets the list of all devices details
	 * @return list of devices details
	 */
	public List<Device> GetAllDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count =1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDevices);

			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("id"),  resultSet
						.getString("serial_no"),resultSet
						.getBoolean("status"), resultSet.getInt("created_by"),count,
						200));
				count++;
				
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the list of active devices
	 * @return list of devices
	 */
	public List<Device> GetActiveDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveDevices);

			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("id"), resultSet
						.getString("serial_no"),resultSet
						.getBoolean("status"), resultSet.getInt("created_by"),count,
						200));
				count++;
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * update the issued device details
	 * @return 200 on successful update,201 on failed ,202 on exception
	 */
	@Override
	public ErevenueResponse UpdateIssueDeviceInfo(Device deviceInfo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
//			if(deviceInfo.devId!=0)
//			{
//			
//				if (checkUserDeviceSerialNo(deviceInfo.userId,deviceInfo.devId)) {
//					
//				}
//				
//		
//			}
			if (deviceInfo.issueId == 0) {
				
				if (ValidateUserId(deviceInfo.userId)) {
					return new ErevenueResponse(201,
							"User already assigned to another device");
				}
				//if (ValidateDeviceId(deviceInfo.devId)) {
					//return new ErevenueResponse(201,
						//	"Device already assigned to another user");
				//}

				/*if(CheckUserIdReturnedState(deviceInfo.issuedTo)){
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateReturnedUser);
					preparedStatement.setInt(1, deviceInfo.devId);
					preparedStatement.setBoolean(2, deviceInfo.status);
					preparedStatement.setInt(3, deviceInfo.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setInt(5, deviceInfo.issuedTo);
				
					
				}*/ /*if(CheckDeviceIdReturnedState(deviceInfo.devId)){
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateReturnedDevice);
					preparedStatement.setInt(1, deviceInfo.issuedTo);
					preparedStatement.setBoolean(2, deviceInfo.status);
					preparedStatement.setInt(3, deviceInfo.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setInt(5, deviceInfo.devId);
				
					
				}*/
				//else{
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertIssueDeviceInfo);
				preparedStatement.setInt(1, deviceInfo.devId);
				preparedStatement.setInt(2, deviceInfo.issuedTo);
				preparedStatement.setBoolean(3, deviceInfo.status);
				preparedStatement.setInt(4, deviceInfo.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			//}
			} else {
//				if(deviceInfo.regId!=0){
//				if (checkDeviceExists(deviceInfo.regId)) {
//					return new ErevenueResponse(201, "Device already assign to another user");
//				}
//				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateIssueDeviceInfo);
				preparedStatement.setInt(1, deviceInfo.issuedTo);
				preparedStatement.setBoolean(2, deviceInfo.status);
				preparedStatement.setInt(3, deviceInfo.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, deviceInfo.issueId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new ErevenueResponse(
					200, "Records Updated") : new ErevenueResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ErevenueResponse(201, "Serial No Already Exists");
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
	

	public boolean CheckUserIdReturnedState(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkUserReturnedStatus);
			preparedStatement.setInt(1, userId);
		
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
	public boolean CheckDeviceIdReturnedState(int devId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkDeviceReturnedStatus);
			preparedStatement.setInt(1, devId);
		
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
	 * validate the device id if its already issued
	 * @param devId
	 * @return true if exists else false
	 */

	public boolean ValidateDeviceId(int devId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateDevId);
			preparedStatement.setInt(1, devId);
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
	 * validate the user id if its already issued
	 * @param devId
	 * @return true if exists else false
	 */
	public boolean ValidateUserId(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.validateUserId);
			preparedStatement.setInt(1, userId);
			//preparedStatement.setBoolean(2, status);
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
	 * gets the list of all device isssued info
	 * @return list of issued devices
	 */
	@Override
	public List<Device> GetAllIssueDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count =1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getIssueDevices);

			resultSet = preparedStatement.executeQuery();
			List<Device> issueDevices = new ArrayList<Device>();
			while (resultSet.next()) {
				issueDevices.add(new Device(resultSet
						.getInt("id"),resultSet
						.getInt("dev_id"), resultSet
						.getString("serial_no"),resultSet
						.getInt("issued_to"), resultSet
						.getString("UserName"), resultSet
						.getInt("created_by"),resultSet
						.getBoolean("status"),
						200,count,resultSet
						.getString("name"),resultSet
						.getString("mkt_name")));
				count++;
			}
			return issueDevices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * gets the list of active users.
	 * @return list of active users
	 */
	public List<User> GetActiveUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveUsers);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				User objUser = new User();
				objUser.userId = resultSet.getInt("ID");
				objUser.userName = resultSet.getString("UserName");
				objUser.userFullName=resultSet.getString("name");
				
				users.add(objUser);
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
	public List<User> GetAllActiveUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAllActiveUsers);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				User objUser = new User();
				objUser.userId = resultSet.getInt("ID");
				objUser.userName = resultSet.getString("UserName");
				objUser.userFullName=resultSet.getString("name");
				
				users.add(objUser);
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
}
