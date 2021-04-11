package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.Device;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;

public interface DeviceBal {
	boolean checkDevicedSerialNo(String serialNo);

	ErevenueResponse UpdateDeviceInfo(Device deviceInfo);

	List<Device> GetAllDevicesInfo();

	ErevenueResponse UpdateIssueDeviceInfo(Device deviceInfo);

	List<Device> GetAllIssueDevicesInfo();
	
	 List<User> GetActiveUsers();
	 
	 List<Device> GetActiveDevicesInfo();
	 List<User> GetAllActiveUsers() ;
	 //List<EntryPoint> GetGateByLocation(int locationId);

}
