/**
 * 
 */
package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Params;
import com.compulynx.erevenue.models.Service;

/**
 * @author Anita
 *
 */
public interface ServiceBal {
	ErevenueResponse UpdateService(Service service);

	List<Service> GetAllServices();

	List<Params> GetAllParams();

	List<Service> GetSubServiceById(int serviceId);

	ErevenueResponse UpdateParam(Params param);

	List<Service> GetAllSubServices();

	ErevenueResponse UpdateSerPrice(Service ser);
	
	List<Service> GetMarketSubServices(int marketId);
}
