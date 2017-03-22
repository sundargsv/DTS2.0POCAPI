package com.dts.DTSAPI;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;




import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.DeleteRequest;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

public class LDAPUtil {

	static Properties props = new CommonFunctions().loadProperties();
	static String domain=props.getProperty("ldap.domain");
	static LDAPConnection connection;
	
	
	public static LDAPConnection connect(String server,Integer port,String userName, String password)
	{
		try {
			connection = new LDAPConnection(server,port,userName,password);
		} catch (NumberFormatException | LDAPException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static LDAPUser getGroups(String groupOu) throws ApplicationException
	{
		LDAPUser ldapUser = new LDAPUser();
		SearchResult results;
		try {
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
			results = connection.search("ou="+groupOu+domain, SearchScope.SUB, "(objectclass=*)", "*");
			if(results.getEntryCount() > 0)
			{
				for(SearchResultEntry entry:results.getSearchEntries())
				{
					if(entry.getAttribute("cn") != null)
						System.out.println(entry.getAttribute("cn").getValue());
				}

			}
		} catch (LDAPSearchException e) {
			System.out.println("No Groups under " + groupOu);
			return null;
		} catch (LDAPException e) {
			throw new ApplicationException("Error when searching for groups:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}		
		return ldapUser;
	}
	public static LDAPGroup getGroup(String groupCn,String groupOu) throws ApplicationException
	{
		LDAPGroup ldapGroup = new LDAPGroup();
		SearchResult results;
		try {
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
			results = connection.search("cn="+groupCn+",ou="+groupOu+domain, SearchScope.BASE, "(objectclass=*)", "*");
			if(results.getEntryCount() > 0)
			{
				for(SearchResultEntry entry:results.getSearchEntries())
				{
					ldapGroup.setCn(entry.getAttribute("cn").getValue());
					ldapGroup.setGidNumber(entry.getAttribute("gidNumber").getValue());
				}

			}
		}catch (LDAPSearchException e) {
			System.out.println("Group " + groupCn + " not found under " + groupOu);
			return null;
		}catch (LDAPException e) {
			throw new ApplicationException("Error when searching for group:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}
		return ldapGroup;
	}
	public static LDAPUser getUser(String userName) throws ApplicationException
	{
		LDAPUser ldapUser = new LDAPUser();
		SearchResult results;
		try {
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
			results = connection.search("cn="+userName+",ou=User"+domain, SearchScope.BASE, "(objectclass=*)", "*");
			if(results.getEntryCount() == 1)
			{
				for(SearchResultEntry entry:results.getSearchEntries())
				{
					ldapUser.setCn(entry.getAttribute("cn").getValue());
					ldapUser.setBusinessCategory(entry.getAttribute("businessCategory").getValue());
					ldapUser.setGivenName(entry.getAttribute("givenName").getValue());
					ldapUser.setUid(entry.getAttribute("uid").getValue());
					ldapUser.setUserPassword(entry.getAttributeValue("userPassword"));
				}
			}
		} catch (LDAPSearchException e) {
			System.out.println("User " + userName + " not found");
			return null;
		} catch (LDAPException e) {
			throw new ApplicationException("Error in connecting to LDAP Server:"+ e.getMessage());
		}
		catch (Exception e) {
			throw new ApplicationException("Error when searching for user:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}
		return ldapUser;
	}
	public static Boolean modifyUser(LDAPUser ldapUser) throws ApplicationException
	{
		String dn = "cn="+ldapUser.getCn()+",ou=User"+domain;
		List<Modification> modList = new ArrayList<Modification>();
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(ldapUser.getClass()).getPropertyDescriptors()) {
				if (pd.getReadMethod() != null && !"class".equals(pd.getName()) && pd.getReadMethod().invoke(ldapUser) != null)
				{
					modList.add(new Modification(ModificationType.REPLACE,pd.getName(),pd.getReadMethod().invoke(ldapUser).toString()));
					System.out.println("\t"+pd.getName()+":"+pd.getReadMethod().invoke(ldapUser));
				}
			}
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("+")+domain, props.getProperty("ldap.password"));
			ModifyRequest modifyRequest = new ModifyRequest(dn,modList);
			LDAPResult result = connection.modify(modifyRequest);
			connection.close();
			if(result.getResultCode() == ResultCode.SUCCESS)
			{
				System.out.println("Modified LDAP User:"+ldapUser.getCn());
				return true;
			}
			else
			{	
				System.out.println("Error when modifying LDAP User:"+ldapUser.getCn()+"."+result.getDiagnosticMessage());
				connection.close();
				return true;
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IntrospectionException | LDAPException e) {
			throw new ApplicationException("Error when modifying user:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}
	}
//	public static Boolean changePassword(String userName,String newPwd) throws ApplicationException
//	{
//		String dn = "cn="+userName+",ou=User"+domain;
//		try {
//			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
//			PasswordModifyExtendedRequest passwordModifyRequest = new PasswordModifyExtendedRequest(dn,null,newPwd);
//			PasswordModifyExtendedResult passwordModifyResult = (PasswordModifyExtendedResult) connection.processExtendedOperation(passwordModifyRequest);
//			if (passwordModifyResult.getResultCode() != ResultCode.SUCCESS) {
//				logger.error("Password Change Failed:"+ passwordModifyResult.getDiagnosticMessage());
//				return false;
//			}
//			else
//			{
//				logger.info("Password Changed");
//				return true;
//			}
//		} catch (LDAPException e) {
//			throw new ApplicationException("Error when changing password:"+ e.getMessage());
//		}
//		finally
//		{
//			connection.close();
//		}	
//	}
	public static Boolean changePassword(String userName,String newPwd) throws ApplicationException
	{
		String dn = "cn="+userName+",ou=User"+domain;
		ModifyRequest modifyRequest = new ModifyRequest(dn,new Modification(ModificationType.REPLACE,"userPassword",newPwd));
		try {
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
			LDAPResult result = connection.modify(modifyRequest);
			if (result.getResultCode() != ResultCode.SUCCESS) {
				System.out.println("Password Change Failed:"+ result.getDiagnosticMessage());
				return false;
			}
			else
			{
				System.out.println("Password Changed");
				return true;
			}
		} catch (LDAPException e) {
			throw new ApplicationException("Error when changing password:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}	
	}
	public static Boolean verifyOldPassword(String userName,String oldPwd) throws ApplicationException
	{
		SearchResult results;
		try {
			System.out.println("vv"+props.getProperty("ldap.ip"));
			System.out.println("vv"+props.getProperty("ldap.username"));
			System.out.println("vv"+props.getProperty("ldap.domain"));
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),"cn="+userName+domain, oldPwd);
			System.out.println("connection created");
			//System.out.println(connection);
			
			results = connection.search("cn="+userName+domain, SearchScope.BASE, "(objectclass=*)", "userPassword");
			
			System.out.println("conencetionsearca");
			if(results.getEntryCount() == 1)
			{
				for(SearchResultEntry entry:results.getSearchEntries())
				{
					System.out.println("inside"+entry.getAttributeValue("userPassword"));

					if(entry.getAttributeValue("userPassword").equals(oldPwd))
					{
						System.out.println("inside"+entry.getAttributeValue("userPassword"));
						connection.close();
						return true;
					}
				}
			}
			
		} catch (LDAPSearchException e) {
			System.out.println("User " + userName + " not found");
			e.printStackTrace();
			return null;
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		finally
		{
			if(connection!=null)
			connection.close();
			else
				return false;
		}
		return true;
	}
	public static Integer getMaxUIDNumber() throws ApplicationException
	{
		Integer uidNumber=0;
		String dn = "ou=User"+domain;
		try {
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
			SearchResult results = connection.search(dn, SearchScope.SUB, "(objectclass=posixAccount)", "*");
			for(SearchResultEntry entry:results.getSearchEntries())
			{
				if(Integer.parseInt(entry.getAttribute("uidNumber").getValue()) > uidNumber)
					uidNumber=Integer.parseInt(entry.getAttribute("uidNumber").getValue());
			}

		} catch (LDAPException e) {
			throw new ApplicationException("Error when getting Max UID Number:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}
		System.out.println("UID Number="+uidNumber+1);
		return uidNumber+1;
	}
	public static Boolean createUser(LDAPUser ldapUser) throws ApplicationException
	{
		String userDn = "cn="+ldapUser.getCn()+",ou=User"+domain;
		String groupDn = "cn="+ldapUser.getGroupCn()+",ou="+ldapUser.getGroupOu()+domain;
		Collection<Attribute> attribList = new ArrayList<Attribute>();
		Modification mod = new Modification(ModificationType.ADD,"memberUid",userDn);

		attribList.add(new Attribute("objectClass","inetOrgPerson","posixAccount","top"));
		ldapUser.setUidNumber(String.valueOf(getMaxUIDNumber()));
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(ldapUser.getClass()).getPropertyDescriptors()) {
				if (pd.getReadMethod() != null && !"class".equals(pd.getName()) && pd.getReadMethod().invoke(ldapUser) != null)
				{
					if(pd.getName().equalsIgnoreCase("groupOu") || pd.getName().equalsIgnoreCase("groupCn"))
					{}
					else
					{
						attribList.add(new Attribute(pd.getName(),pd.getReadMethod().invoke(ldapUser).toString()));
						System.out.println("\t"+pd.getName()+":"+pd.getReadMethod().invoke(ldapUser));
					}
				}
			}

			AddRequest addRequest = new AddRequest(userDn,attribList);
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));

			LDAPResult result = connection.add(addRequest);
			if(result.getResultCode() == ResultCode.SUCCESS)
			{
				ModifyRequest modifyRequest = new ModifyRequest(groupDn,mod);
				result = connection.modify(modifyRequest);
				if(result.getResultCode() == ResultCode.SUCCESS)
				{
					connection.close();
					return true;
				}
				else
				{
					connection.delete(userDn);
					connection.close();
					return false;
				}
			}
			else
			{
				throw new ApplicationException("Error when creating user:"+ result.getDiagnosticMessage());
			}
				
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | IntrospectionException | LDAPException e) {
			throw new ApplicationException("Error when creating user:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}
	}
	public static Boolean deleteUser(String cn) throws ApplicationException
	{
		String userDn = "cn="+cn+",ou=User"+domain;
		try {
			Modification mod = new Modification(ModificationType.DELETE,"memberUid",userDn);
			ModifyRequest modifyRequest = new ModifyRequest(getUser(cn).getBusinessCategory(),mod);
			connection = new LDAPConnection(props.getProperty("ldap.ip"), Integer.parseInt(props.getProperty("ldap.port")),props.getProperty("ldap.username")+domain, props.getProperty("ldap.password"));
			LDAPResult result = connection.modify(modifyRequest);
			if(result.getResultCode() == ResultCode.SUCCESS)
				System.out.println("Deleted user from LDAP Group");
			
			DeleteRequest deleteRequest = new DeleteRequest(userDn);
			result = connection.delete(deleteRequest);
			if(result.getResultCode() == ResultCode.SUCCESS)
			{
				connection.close();
				return true;
			}
			else
			{
				System.out.println(result.getDiagnosticMessage());
				connection.close();
				return false;
			}
		} catch (NumberFormatException | LDAPException e) {
			throw new ApplicationException("Error when deleting user:"+ e.getMessage());
		}
		finally
		{
			connection.close();
		}
	}

}
