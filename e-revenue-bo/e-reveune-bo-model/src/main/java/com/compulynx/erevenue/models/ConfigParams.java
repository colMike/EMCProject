/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author shree
 *
 */
public class ConfigParams {

	public ConfigParams(int configId, String name, String value, int createdBy,String oldValue) {
		super();
		this.configId = configId;
		this.name = name;
		this.value = value;
		this.createdBy = createdBy;
		this.oldValue=oldValue;
	}

	public int configId;

	public String name;
	public String value;
	public int createdBy;
	public String logo;
	public String oldValue;
	
	public ConfigParams() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<ConfigParams> configParams;
}
