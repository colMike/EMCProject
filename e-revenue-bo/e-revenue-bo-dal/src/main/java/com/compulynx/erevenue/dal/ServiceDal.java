/**
 * 
 */
package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Params;
import com.compulynx.erevenue.models.Service;

/**
 * @author Anita
 * @date Apr 26, 2016
 *
 */
public interface ServiceDal {
	

	ErevenueResponse UpdateService(Service service);

	List<Service> GetAllServices();

	Service GetServiceById(int serviceId);

	List<Params> GetAllParams();

	List<Service> GetSubServiceById(int serviceId);

	ErevenueResponse UpdateParam(Params param);

	List<Service> GetAllSubServices();

	ErevenueResponse UpdateSerPrice(Service ser);
	
	List<Service> GetMarketSubServices(int marketId);
	
}
