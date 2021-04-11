/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Service {

	
	public int parentServiceId;
	public int level;
	public boolean hasChild;
	public int serviceId;
	public String serviceCode;
	public String serviceName;
	public double quantiy;
	public double price;
	public double serviceValue;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public List<Params> params;
	public int count;
	public String level2;
	public String active2;
	public int serviceType;
	public boolean parentType;
	public List<SubCounty> subCountyList;
	public List<Ward> wardList;
	public List<Market> marketList;
	public boolean isDisabled;
	public String iconStyle;
	public List<Service> serviceList;
	public int marketId;

	public Service() {
		super();
	}
	public Service(int respCode) {
		super();
		this.respCode=respCode;
	}

	public Service(int serviceId, String serviceCode, String serviceName,
			boolean active, int createdBy, int respCode,int level,boolean hasChild,double serviceValue) {
		super();
		this.serviceId = serviceId;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.level=level;
		this.hasChild=hasChild;
		this.serviceValue=serviceValue;
	}

	public Service(int serviceId, String serviceCode, String serviceName,boolean active,int createdBy, int respCode, String active2,int count,boolean isDisabled,String iconStyle) {
		super();
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
		this.serviceName = serviceName;
		this.active=active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.active2=active2;
		this.count=count;
		this.isDisabled=isDisabled;
		this.iconStyle=iconStyle;
	}
	public Service(String level2, int serviceId, String serviceName,String active2, boolean active,int count,boolean isDisabled,String iconStyle) {
		super();
		this.level2 = level2;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.active2=active2;
		this.active = active;
		this.count=count;
		this.isDisabled=isDisabled;
		this.iconStyle=iconStyle;
	}
	
	public Service(int serviceId, String serviceCode, String serviceName,boolean active,int createdBy, int respCode, String active2,int count) {
		super();
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
		this.serviceName = serviceName;
		this.active=active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.active2=active2;
		this.count=count;
	}

	public Service(int serviceId, String serviceName, boolean isActive, int createdBy,
			int respCode) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		
	}

	public Service(int serviceId,String serviceName,double serviceValue
			) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceValue=serviceValue;
		
	}
	public Service(int serviceId,String serviceName, double serviceValue,String active2, boolean active,int count,boolean isDisabled,String iconStyle) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceValue = serviceValue;
		this.active2=active2;
		this.active = active;
		this.count=count;
		this.isDisabled=isDisabled;
		this.iconStyle=iconStyle;
	}

}
