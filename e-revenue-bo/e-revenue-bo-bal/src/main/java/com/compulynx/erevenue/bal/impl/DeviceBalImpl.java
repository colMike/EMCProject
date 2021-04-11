package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.erevenue.bal.DeviceBal;
import com.compulynx.erevenue.dal.impl.DeviceDalImpl;
import com.compulynx.erevenue.models.Device;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.User;

public class DeviceBalImpl implements DeviceBal{
	
	@Autowired
	DeviceDalImpl deviceDal;
	
	@Override
	public boolean checkDevicedSerialNo(String serialNo) {
		// TODO Auto-generated method stub
		return deviceDal.checkDevicedSerialNo(serialNo);
	}

	@Override
	public ErevenueResponse UpdateDeviceInfo(Device deviceInfo) {
		// TODO Auto-generated method stub
		return deviceDal.UpdateDeviceInfo(deviceInfo);
	}

	@Override
	public List<Device> GetAllDevicesInfo() {
		// TODO Auto-generated method stub
		return deviceDal.GetAllDevicesInfo();
	}

	@Override
	public ErevenueResponse UpdateIssueDeviceInfo(Device deviceInfo) {
		// TODO Auto-generated method stub
		return deviceDal.UpdateIssueDeviceInfo(deviceInfo);
	}

	
	public List<Device> GetAllIssueDevicesInfo() {
		// TODO Auto-generated method stub
		return deviceDal.GetAllIssueDevicesInfo();
	}

	public List<User> GetActiveUsers() {
		// TODO Auto-generated method stub
		return deviceDal.GetActiveUsers();
	}

	@Override
	public List<Device> GetActiveDevicesInfo() {
		// TODO Auto-generated method stub
		return deviceDal.GetActiveDevicesInfo();
	}

	@Override
	public List<User> GetAllActiveUsers() {
		// TODO Auto-generated method stub
		return deviceDal.GetAllActiveUsers();
	}

	/*@Override
	public List<EntryPoint> GetGateByLocation(int locationId) {
		// TODO Auto-generated method stub
		return deviceDal.GetGateByLocation(locationId);
	}*/

}
