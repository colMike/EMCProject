/**
 * 
 */
package com.compulynx.erevenue.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.erevenue.bal.ReportsBal;
import com.compulynx.erevenue.dal.impl.ReportsDalImpl;
import com.compulynx.erevenue.models.Reports;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.ZDetails;


/**
 * @author Aniat
 *
 */
public class ReportsBalImpl implements ReportsBal{

	@Autowired 
	ReportsDalImpl reportsDal;

	@Override
	public List<Reports> GetAllUserTxn() {
		// TODO Auto-generated method stub
		return reportsDal.GetAllUserTxn();
	}

	@Override
	public List<Reports> GetUserTxnsDetails(Reports txnDetails) {
		// TODO Auto-generated method stub
		return reportsDal.GetUserTxnsDetails(txnDetails);
	}

	@Override
	public List<Reports> GetAllDeviceTxn() {
		// TODO Auto-generated method stub
		return reportsDal.GetAllDeviceTxn();
	}

	@Override
	public List<Reports> GetDeviceTxnsDetails(Reports txnDetails) {
		// TODO Auto-generated method stub
		return reportsDal.GetDeviceTxnsDetails(txnDetails);
	}

//	@Override
//	public List<Reports> GetAllGateTxn() {
//		// TODO Auto-generated method stub
//		return reportsDal.GetAllGateTxn();
//	}
//
//	@Override
//	public List<Reports> GetGateTxnsDetails(Reports txnDetails) {
//		// TODO Auto-generated method stub
//		return reportsDal.GetGateTxnsDetails(txnDetails);
//	}

	@Override
	public List<Reports> GetAllTxnsDetails(Reports txnDetails) {
		// TODO Auto-generated method stub
		return reportsDal.GetAllTxnsDetails(txnDetails);
	}

	@Override
	public List<ZDetails> GetZDetails(ZDetails zDetails) {
		// TODO Auto-generated method stub
		return reportsDal.GetZDetails(zDetails);
	}

	@Override
	public List<Reports> GetCurrentTxnsDetails() {
		// TODO Auto-generated method stub
		return reportsDal.GetCurrentTxnsDetails();
	}

//	@Override
//	public List<Service> GetParentServices() {
//		// TODO Auto-generated method stub
//		return reportsDal.GetParentServices();
//	}
//
//	@Override
//	public List<Service> GetSubServices(int parentServiceId) {
//		// TODO Auto-generated method stub
//		return reportsDal.GetSubServices(parentServiceId);
//	}
//
//	@Override
//	public List<Reports> GetServiceTxnsDetails(Reports txnDetails) {
//		// TODO Auto-generated method stub
//		return reportsDal.GetServiceTxnsDetails(txnDetails);
//	}

//	@Override
//	public List<Reports> GetAllServiceTxn() {
//		// TODO Auto-generated method stub
//		return reportsDal.GetAllServiceTxn();
//	}
	

}
