package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.Application;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LandRate;
import com.compulynx.erevenue.models.Reports;

public interface LandRateBal {
	ErevenueResponse CreateRegistrationForm(LandRate lr);
	List<LandRate> GetAllRegs();
	List<LandRate> GetAllLandInvoices();

	ErevenueResponse UpdateLandInvocieStatus(LandRate lr);
	
	ErevenueResponse UpdateLandPermitRenewal(LandRate lr);
	
	List<LandRate> GetPaidLandDetails();
	
	List<LandRate> GetAllLandPermits();
	List<LandRate> GetLandRatesByDate(Reports lr);
	List<LandRate> GetAllLandsByLinkId(int linkId,String nationalIdNo,int agentId);
	
	//Added by Anita
	List<LandRate> GetAllLandInvoicesByLinkId(int linkId,String nationalIdNo, int agentId);
}
