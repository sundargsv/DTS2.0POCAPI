package com.dts.DTSAPI;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dts.Manger.CustomerManager;
import com.dts.Manger.Test;
import com.zinnia.model.Customer;
import com.zinnia.model.SavedResponse;
import com.zinnia.model.login;
import com.zinnia.model.ptelogin;
import com.zinnia.model.geography.Address;
import com.zinnia.model.payment.ContactPerson;
import com.google.common.base.Strings;
import com.google.gson.Gson;

/**
 * Root resource (exposed at "DTS" path)
 */
@Path("DTS")
public class MyResource {

	@GET
	@Path("/getJson")
	@Produces(MediaType.APPLICATION_JSON)
	public SavedResponse getIt() {
		Customer customer = new Customer();
		SavedResponse s = new SavedResponse();
		s.setMessage("ABC");
		s.setStatus("123");
		CustomerManager cust = new CustomerManager();
		Address address = new Address();
		customer.setBillingAddress(address);
		address.setCityName("CBE");
		address.setCountry("INDIA");
		address.setHouseNumber("houseNumber");
		address.setHouseNumberExtension("Extension");
		address.setStreetName("Eachanari");
		address.setZipCode("641021");

		customer.setCustomerAddress(address);
		ContactPerson contact = new ContactPerson();
		contact.setContactNumber("9887835909");
		contact.setDepartment("Depart");
		contact.setEmailId("nishant.kr.iet@gmail.com");
		contact.setFaxNumber("9887835909");
		contact.setInitials("Mr.");
		contact.setSalutation("Singh");
		contact.setSurname("Rajput");
		contact.setSurnamePrefix("Nishant");
		contact.setTitle("Job");
		customer.setContactPerson(contact);
		// cust.SaveCustomer(customer);

		return s;

	}

	@POST
	@Path("/createCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) {
		CustomerManager cust = new CustomerManager();
		Customer savedCustomer = cust.SaveCustomer(customer);
		SavedResponse res = new SavedResponse();
		if (savedCustomer != null) {
			res.setMessage("Customer Saved SuccessFully.");
			res.setStatus("True");
		} else {
			res.setMessage("Customer not Saved SuccessFully.");
			res.setStatus("False");
		}
		Gson gson = new Gson();
		String json = gson.toJson(res);

		return Response.status(200).entity(json).build();

		// return savedCustomer;

	}

	@GET
	@Path("/CustomerSearch")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomer() {
		List<Test> test = new ArrayList<>();
		CustomerManager cust = new CustomerManager();
		List<Customer> customer = cust.check();
		if (customer != null && customer.size() > 0) {
			for (int i = 0; i < customer.size(); i++) {
				Test t = new Test();
				t.setId(customer.get(i).getId());
				t.setName(customer.get(i).getName());
				t.setEmail("email");
				t.setPhoneNumber("45576876");
				test.add(t);
			}
			Gson gson = new Gson();
			String json = gson.toJson(customer);

			return Response
					.status(200)
					.entity(json)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"GET, POST, DELETE, PUT").allow("OPTIONS").build();
		}
		return null;
	}

	@POST
	@Path("/CustomerSearchWithLimit")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomerWithLimit(SavedResponse data) {
		// List<Test> test = new ArrayList<>();
		CustomerManager cust = new CustomerManager();
		System.out.println("data start row" + data.getMessage());
		System.out.println("data limit row" + data.getStatus());
		String start = data.getMessage();
		String limit = data.getStatus();
		List<Customer> customer = cust.searchCustomerByLimit(start, limit);
		int noOfRows = 0;
		if (start.equalsIgnoreCase("0")) {
			noOfRows = cust.noOfRowsOfCust();
		}
		if (customer != null && customer.size() > 0) {
			SavedResponse res = new SavedResponse();
			res.setMessage("No. Of Rows");
			res.setStatus(Integer.toString(noOfRows));
			Gson gson = new Gson();
			String json = gson.toJson(customer);
			String noofrows = gson.toJson(res);
			String totalString = "[" + json + ", " + noofrows + "]";

			return Response
					.status(200)
					.entity(totalString)
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods",
							"GET, POST, DELETE, PUT").allow("OPTIONS").build();
		}
		return null;
	}
	
	
	@SuppressWarnings("unused")
	@GET
	@Path("/CustomerSearchWithCondition")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerById(@QueryParam("id") String id,
			@QueryParam("name") String name) {
		System.out.println("id" + id);
		System.out.println("name" + name);
		StringBuilder query = new StringBuilder();
		if (Strings.isNullOrEmpty(id) && Strings.isNullOrEmpty(name)) {

		} else {
			if (Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(name)) {
				query.append("name like '%" + name + "%'");
			}
			if (!Strings.isNullOrEmpty(id) && Strings.isNullOrEmpty(name)) {
				query.append("id=" + id + "");
			}
			if (!Strings.isNullOrEmpty(id) && !Strings.isNullOrEmpty(name)) {
				query.append("id= " + id + " and " + "name like '%" + name + "%'");
			}
		}
		/*
		 * if(Strings.isNullOrEmpty(name)) {
		 * 
		 * } else { query.append("name='"+name+"'"); }
		 */
		List<Test> test = new ArrayList<>();
		CustomerManager cust = new CustomerManager();
		System.out.println("Query final-----> " + query.toString());
		List<Customer> customer = cust.searchCustomerByCondition(query
				.toString());
		Gson gson = new Gson();
		String json;
		if (customer != null && customer.size() > 0) {
			json = gson.toJson(customer);
		} else {
			json = gson.toJson(customer);
			SavedResponse res = new SavedResponse();
			res.setMessage("Customer Not Found");
			res.setStatus("False");
			//json = gson.toJson(res);
		}
		// gson.toJson(customer);

		return Response
				.status(200)
				.entity(json)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods",
						"GET, POST, DELETE, PUT").allow("OPTIONS").build();
	}

	// return null;

	@POST
	@Path("/Ptelogin")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPteLogin(ptelogin ptelogin) {
		boolean check = false;
		System.out.println("File requested is : " + ptelogin.getUserName());
		System.out.println("password is " + ptelogin.getPassword());
		try {
			System.out.println("verify"
					+ LDAPUtil.verifyOldPassword(ptelogin.getUserName(),
							ptelogin.getPassword()));
			check = LDAPUtil.verifyOldPassword(ptelogin.getUserName(),
					ptelogin.getPassword());
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			// TODO Generated By Nishant
			e.printStackTrace();
		}
		login b = new login();
		if (check == true) {

			b.setCode("1");
			b.setMessage("user exist");
		} else {
			b.setCode("0");
			b.setMessage("user does not exist");
		}
		Gson gson = new Gson();

		String json = gson.toJson(b);
		return Response.status(200).entity(json).build();
	}
}
