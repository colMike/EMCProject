package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.erevenue.bal.LandRateBal;
import com.compulynx.erevenue.dal.impl.ApplicationDalImpl;
import com.compulynx.erevenue.dal.impl.LandRateDalImpl;
import com.compulynx.erevenue.models.Application;
import com.compulynx.erevenue.models.CompasResponse;
import com.compulynx.erevenue.models.ErevenueResponse;
import com.compulynx.erevenue.models.LandRate;
import com.compulynx.erevenue.models.Reports;

@Component
public class LandRateBalImpl implements LandRateBal{
	@Autowired
	LandRateDalImpl landrateDal;
	
    
	public ErevenueResponse CreateRegistrationForm(LandRate lr) {
		// TODO Auto-generated method stub
		return landrateDal.CreateRegistrationForm(lr);
	}


	public List<LandRate> GetAllRegs() {
		// TODO Auto-generated method stub
		return landrateDal.GetAllRegs();
	}
	
	public List<LandRate> GetAllLandInvoices(){
		return landrateDal.GetAllLandInvoices();
		
	}
	
	public ErevenueResponse UpdateLandInvoiceStatus(LandRate lr){
		return landrateDal.UpdateLandInvoiceStatus(lr);
	}
	public ErevenueResponse UpdateLandPermitRenewal(LandRate lr) {
		// TODO Auto-generated method stub
		return landrateDal.UpdateLandPermitRenewal(lr);
	}
    public List<LandRate> GetPaidLandDetails(){
	  return landrateDal.GetPaidLandDetails();
		
	}
    

    
    
   public List<LandRate> GetAllLandsByLinkId(int linkId,String nationalIdNo,int agentId){
	   return landrateDal.GetAllLandsByLinkId(linkId, nationalIdNo, agentId);
   }
   
   public double GetLandFee(int landId){
       return landrateDal.GetLandFee(landId);
   }



@Override
public List<LandRate> GetAllLandInvoicesByLinkId(int linkId,
		String nationalIdNo, int agentId) {
	// TODO Auto-generated method stub
	return landrateDal.GetAllLandInvoicesByLinkId(linkId, nationalIdNo, agentId);
}


@Override
public ErevenueResponse UpdateLandInvocieStatus(LandRate lr) {
	// TODO Auto-generated method stub
	return null;
}


public List<LandRate> GetAllLandPermits() {
	// TODO Auto-generated method stub
	return landrateDal.GetAllLandPermits();
}

//added by cyrus
public List<LandRate> GetLandRatesByDate(Reports lr){
	// TODO Auto-generated method stub
	return landrateDal.GetLandRatesByDate(lr);
}


public CompasResponse UploadPlot(String filePath,String fileName, String uploadedBy,int branchId) {
	return landrateDal.UploadPlot(filePath,fileName,uploadedBy,branchId);
}
public CompasResponse UploadLand(String filePath,String fileName, String uploadedBy,int branchId) {
	System.out.println("============here2=================");
	return landrateDal.UploadLand(filePath,fileName,uploadedBy,branchId);
}
}
