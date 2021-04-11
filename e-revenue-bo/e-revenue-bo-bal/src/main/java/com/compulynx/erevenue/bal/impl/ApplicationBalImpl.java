/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import com.compulynx.erevenue.bal.ApplicationBal;
import com.compulynx.erevenue.dal.impl.ApplicationDalImpl;
import com.compulynx.erevenue.models.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Anita
 * @date Apr 15, 2016
 *
 */
@Component
public class ApplicationBalImpl implements ApplicationBal{
	
	@Autowired
	ApplicationDalImpl applicationDal;
	@Override
	public ErevenueResponse UpdateApplication(Application app) {
		// TODO Auto-generated method stub
		return applicationDal.UpdateApplication(app);
	}
	@Override
	public List<PermitType> GetActivePermitTypes(String permitType) {
		// TODO Auto-generated method stub
		return applicationDal.GetActivePermitTypes(permitType);
	}
	@Override
	public List<Application> GetAllApplications() {
		// TODO Auto-generated method stub
		return applicationDal.GetAllApplications();
	}
	@Override
	public List<Application> GetAllInvoices() {
		// TODO Auto-generated method stub
		return applicationDal.GetAllInvoices();
	}
	@Override
	public ErevenueResponse UpdateInvocieStatus(Application app) {
		// TODO Auto-generated method stub
		return applicationDal.UpdateInvocieStatus(app);
	}
	@Override
	public List<Application> GetAllPermits() {
		// TODO Auto-generated method stub
		return applicationDal.GetAllPermits();
	}
	
	@Override
	public ErevenueResponse UpdateQrImagePath(Application app) {
		// TODO Auto-generated method stub
		return applicationDal.UpdateQrImagePath(app);
	}
	@Override
	public ErevenueResponse UpdatePermitRenewal(Application app) {
		// TODO Auto-generated method stub
		return applicationDal.UpdatePermitRenewal(app);
	}
	@Override
	public List<Application> GetAllAppsByLinkId(int linkId, String nationalIdNo,int agentId) {
		// TODO Auto-generated method stub
		return applicationDal.GetAllAppsByLinkId(linkId, nationalIdNo,agentId);
	}
	@Override
	public ErevenueResponse UpdatePermitStatus(Application app) {
		// TODO Auto-generated method stub
		return applicationDal.UpdatePermitStatus(app);
	}
	@Override
	public List<Ward> GetWardsBySubCounty(int subCountyId) {
		// TODO Auto-generated method stub
		return applicationDal.GetWardsBySubCounty(subCountyId);
	}
	@Override
	public List<Application> GetPermitsByDate(Reports permit) {
		// TODO Auto-generated method stub
		return applicationDal.GetPermitsByDate(permit);
	}
	@Override
	public List<Application> GetAllInvoicesByLinkId(int linkId,
			String nationalIdNo, int agentId) {
		// TODO Auto-generated method stub
		return applicationDal.GetAllInvoicesByLinkId(linkId, nationalIdNo, agentId);
	}
	@Override
	public List<Market> GetMarketsByWard(int wardId) {
		// TODO Auto-generated method stub
		return applicationDal.GetMarketsByWard(wardId);
	}

                @Override
                public double getItemPrice(int id) {
                    return applicationDal.getItemPrice(id);
                }
        
        

}
