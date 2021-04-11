package com.compulynx.erevenue.dal;

import java.util.List;

import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LandRate;
import com.compulynx.erevenue.models.Reports;

public interface LandRateDal {
	ErevenueResponse CreateRegistrationForm(LandRate lr);
	List<LandRate> GetAllRegs();
	List<LandRate> GetAllLandInvoices();

	ErevenueResponse UpdateLandInvoiceStatus(LandRate lr);
	ErevenueResponse UpdateLandPermitRenewal(LandRate lr);
	List<LandRate> GetPaidLandDetails();
	List<LandRate> GetAllLandPermits();
	List<LandRate> GetAllLandsByLinkId(int linkId,String nationalIdNo,int agentId);
	//Added by Anita
	List<LandRate> GetAllLandInvoicesByLinkId(int linkId,String nationalIdNo, int agentId);
	//added by cyrus
	List<LandRate> GetLandRatesByDate(Reports lr);
}
