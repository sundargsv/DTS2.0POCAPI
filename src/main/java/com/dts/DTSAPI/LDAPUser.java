package com.dts.DTSAPI;

public class LDAPUser {

	private String cn;
	private String gidNumber;
	private String homeDirectory;
	private String sn;
	private String uid;
	private String uidNumber;
	private String businessCategory;
	private String givenName;
	private String mail;
	private String userPassword;
	private String groupCn;
	private String groupOu;
	
	private String[] parts;
	private String domain=",dc=mtrade,dc=com,dc=my";

	
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getGidNumber() {
		return gidNumber;
	}
	public void setGidNumber(String gidNumber) {
		this.gidNumber = gidNumber;
	}
	public String getHomeDirectory() {
		return homeDirectory;
	}
	public void setHomeDirectory() {
		this.homeDirectory = "/home/users/"+cn;
	}
	public String getSn() {
		return sn;
	}
	public void setSn() {
		parts = groupCn.split("_");
		this.sn = parts[0];
	}
	public String getUid() {
		return uid;
	}
	public void setUid() {
		this.uid = parts[1];
	}
	public String getUidNumber() {
		return uidNumber;
	}
	public void setUidNumber(String uidNumber) {
		this.uidNumber = uidNumber;
	}
	public String getBusinessCategory() {
		return businessCategory;
	}
	public void setBusinessCategory() {
		this.businessCategory = "cn="+groupCn+",ou="+groupOu+domain;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getMail() {
		return mail;
	}
	public void setMail() {
		this.mail = cn+"@mtradeasia.com";
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getGroupCn() {
		return groupCn;
	}
	public void setGroupCn(String groupCn) {
		this.groupCn = groupCn;
	}
	public String getGroupOu() {
		return groupOu;
	}
	public void setGroupOu(String groupOu) {
		this.groupOu = groupOu;
	}
	public void setBusinessCategory(String businessCategory) {
		this.businessCategory=businessCategory;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}



}
