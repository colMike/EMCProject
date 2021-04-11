/**
 * 
 */
package com.compulynx.erevenue.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class LoginSession {
	public int sessionId;
	public int linkId;
	public int userGroupId;
	public String linkName;
	public String sessionName;
	public String sessionFullName;
	public String linkExtInfo;
	public int userClassId;
	public String appName;

	public void setLinkExtInfo(String linkExtInfo) {
		this.linkExtInfo = linkExtInfo;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public void setSessionFullName(String sessionFullName) {
		this.sessionFullName = sessionFullName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public void setUserClassID(int userClassID) {
		this.userClassId = userClassID;
	}

	public List<Rights> rightsHeaderList;
	public int respCode;

	public LoginSession() {
		super();
	}

	public void setRightsList(List<Rights> rightsList) {
		this.rightsHeaderList = rightsList;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public LoginSession(int respCode) {
		super();
		this.respCode = respCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
