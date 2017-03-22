/**
 * 
 */
package com.zinnia.model;

import java.util.Date;

import org.hibernate.envers.Audited;

import com.zinnia.core.NamedEntity;
import com.zinnia.model.account.LegalEntity;
import com.zinnia.model.account.MarketGroup;
import com.zinnia.model.account.MarketSegment;
import com.zinnia.model.account.Segment;
import com.zinnia.model.branding.Brand;
import com.zinnia.model.charge.Currency;
import com.zinnia.model.geography.Address;
import com.zinnia.model.geography.Market;
import com.zinnia.model.geography.TimeZones;
import com.zinnia.model.inventory.offer.Offer;
import com.zinnia.model.localization.Language;
import com.zinnia.model.payment.ContactPerson;
import com.zinnia.model.payment.PaymentDetails;

/**
 * @author Chandan 14th November 2012
 *
 */
@SuppressWarnings("serial")
@Audited
public class Customer extends NamedEntity{

	//Priority.1: General information
	//private String CKRNumber; 
	//private String COCNumber;
	//private CocRegistrationType COCRegistrationType;

	//Priority.2: Self service access information
//	private String userName;

	//Priority.3: Corporate information
	//private String BIKCode;
	private String organizationName;
	//private String partyName;
	//private String branchName;	
	//private MarketGroup marketGroup;
	//private MarketSegment marketSegment;
	//private TelecomRegion telecomRegion;
	//private FinancialStatus financialStatus;
	//private Market market;
	//private boolean taxExemption;
	//private String VATNumber;
	//private boolean top30;
	//private boolean invoiceConsolidation;
	//private CustomerStatus customerStatus;

	//private CustomerSpecification customerSpecification;
	
	private ContactPerson contactPerson;
	private Address billingAddress;
	private Address customerAddress;

	/*
	Inserted By Nishant
		
	*/
	/*private String OCMCustomerId;

	
	private String directoreInfo1;
	private String directoreInfo2;
	private String shareHolderInfo1;
	private String shareHolderINfo2;
	private Date startDate;
	private Date endDate;
	private String mobileNumber;
	private String shareHolderSalutation;
	private String shareHolderName;
	private String shareHolderAddressline1;
	private String shareHolderAddressline2;
	private String shareHolderAddressline3;
	private String shareHolderAddressline4;
	private String shareHolderPostCode;
	private String shareHolderCountry;
	private String shareHolderPhoneNumber;
	private String shareHolderEmailId;
	
	private String directorSalutation;
	private String directorName;
	private String directorAddressline1;
	private String directorAddressline2;
	private String directorAddressline3;
	private String directorAddressline4;
	private String directorPostCode;
	private String directorCountry;
	private String directorPhoneNumber;
	private String directorEmailId;
	private String dissConnectionReason;*/
	
	


	
    
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	

	public ContactPerson getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public Address getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(Address customerAddress) {
		this.customerAddress = customerAddress;
	}
	
	
}
