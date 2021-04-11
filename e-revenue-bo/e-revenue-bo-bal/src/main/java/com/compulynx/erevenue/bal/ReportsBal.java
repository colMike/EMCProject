/**
 * 
 */
package com.compulynx.erevenue.bal;

import java.util.List;

import com.compulynx.erevenue.models.Reports;
import com.compulynx.erevenue.models.Service;
import com.compulynx.erevenue.models.Transaction;
import com.compulynx.erevenue.models.ZDetails;

/**
 * @author Anita
 *
 */
public interface ReportsBal {
	List<Reports> GetAllUserTxn();

	List<Reports> GetUserTxnsDetails(Reports txnDetails);

	List<Reports> GetAllDeviceTxn();

	List<Reports> GetDeviceTxnsDetails(Reports txnDetails);

	//List<Reports> GetAllGateTxn();

	//List<Reports> GetGateTxnsDetails(Reports txnDetails);

	List<Reports> GetAllTxnsDetails(Reports txnDetails);

	List<ZDetails> GetZDetails(ZDetails zDetails);

	List<Reports> GetCurrentTxnsDetails();

	//List<Service> GetParentServices();
	
	//List<Service> GetSubServices(int parentServiceId);
	//List<Reports> GetServiceTxnsDetails(Reports txnDetails);
	//List<Reports> GetAllServiceTxn() ;
}
