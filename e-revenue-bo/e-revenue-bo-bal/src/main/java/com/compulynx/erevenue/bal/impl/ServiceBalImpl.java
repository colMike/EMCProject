package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.models.Params;
import com.compulynx.erevenue.bal.ServiceBal;
import com.compulynx.erevenue.dal.impl.ServiceDalImpl;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Service;
/**
 * @author Anita
 * @date Apr 26, 2016
 *
 */
@Component
public class ServiceBalImpl implements ServiceBal {

	@Autowired
	ServiceDalImpl serviceDal;

	public ErevenueResponse UpdateService(Service service) {

		return serviceDal.UpdateService(service);
	}

	@Override
	public List<Service> GetAllServices() {

		return serviceDal.GetAllServices();
	}

	public List<Params> GetAllParams() {
		// TODO Auto-generated method stub
		return serviceDal.GetAllParams();
	}

	@Override
	public List<Service> GetSubServiceById(int serviceId) {
		// TODO Auto-generated method stub
		return serviceDal.GetSubServiceById(serviceId);
	}

	@Override
	public ErevenueResponse UpdateParam(Params param) {
		// TODO Auto-generated method stub
		return serviceDal.UpdateParam(param);
	}

	@Override
	public List<Service> GetAllSubServices() {
		// TODO Auto-generated method stub
		return serviceDal.GetAllSubServices();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.erevenue.bal.ServiceBal#UpdateSer(com.compulynx.erevenue.models.Service)
	 */
	@Override
	public ErevenueResponse UpdateSerPrice(Service ser) {
		// TODO Auto-generated method stub
		return serviceDal.UpdateSerPrice(ser);
	}

	@Override
	public List<Service> GetMarketSubServices(int marketId) {
		// TODO Auto-generated method stub
		return serviceDal.GetMarketSubServices(marketId);
	}



}
