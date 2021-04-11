package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.Device;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;

public interface DeviceDal {
	boolean checkDevicedSerialNo(String serialNo);

	ErevenueResponse UpdateDeviceInfo(Device deviceInfo);

	List<Device> GetAllDevicesInfo();

	ErevenueResponse UpdateIssueDeviceInfo(Device deviceInfo);

	List<Device> GetAllIssueDevicesInfo();
	
	 List<User> GetActiveUsers();
	 
	 List<Device> GetActiveDevicesInfo();
	 List<User> GetAllActiveUsers() ;
	

}
