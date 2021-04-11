/**
 * 
 */
package com.compulynx.erevenue.dal;

import com.compulynx.erevenue.models.Application;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.Market;
import com.compulynx.erevenue.models.PermitType;
import com.compulynx.erevenue.models.Reports;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.Ward;

import java.util.List;

/**
 * @author ANita
 * @date Apr 15, 2016
 *
 */
public interface ApplicationDal {

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

}
