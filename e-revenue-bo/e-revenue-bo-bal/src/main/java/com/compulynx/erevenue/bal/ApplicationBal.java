/**
 * 
 */
package com.compulynx.erevenue.bal;

import com.compulynx.erevenue.models.*;

import java.util.List;

/**
 * @author Anita
 * @date Apr 15, 2016
 *
 */
public interface ApplicationBal {
	ErevenueResponse UpdateApplication(Application app);

	List<PermitType> GetActivePermitTypes(String permitType);

	List<Application> GetAllApplications();

	List<Application> GetAllInvoices();

	ErevenueResponse UpdateInvocieStatus(Application app);

	List<Application> GetAllPermits();

	ErevenueResponse UpdateQrImagePath(Application app);

	ErevenueResponse UpdatePermitRenewal(Application app);

	List<Application> GetAllAppsByLinkId(int linkId, String nationalIdNo,
			int agentId);

	ErevenueResponse UpdatePermitStatus(Application app);

	List<Ward> GetWardsBySubCounty(int subCountyId);

	List<Application> GetPermitsByDate(Reports permit);

	List<Application> GetAllInvoicesByLinkId(int linkId, String nationalIdNo,
			int agentId);

	List<Market> GetMarketsByWard(int wardId);
        
                  double getItemPrice(int id);
}
