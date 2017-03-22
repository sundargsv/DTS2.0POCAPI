package com.dts.Manger;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.dts.DAO.PersisteanceManager;
import com.zinnia.model.Customer;



public class CustomerManager 
{
	@SuppressWarnings("unchecked")
	public List<Customer> check()
	{
		List<Customer> cust=null;
		try{
			System.out.println("i came in check method");
		/*Configuration config= new Configuration().configure("hibernateNew.cfg.xml");
		System.out.println("Configuration done");
		SessionFactory sessionFactory= config.buildSessionFactory();
		Session session= sessionFactory.openSession();*/
		PersisteanceManager pm= new PersisteanceManager();
		SessionFactory sessionFactory=pm.getOpenSession();
		Session session= sessionFactory.openSession();
		session.beginTransaction();
		String qyery="from com.zinnia.model.Customer ";
		cust= (List<Customer>) pm.executeCustomeQuery(session, qyery);
		//org.hibernate.Query query= session.createQuery(qyery);
		//cust= query.list();
		
		//session.save(cust);
		session.getTransaction().commit();
		//session.getTransaction().commit();
		pm.closeSession(session, sessionFactory);
		if(cust!=null && cust.size()>0)
		{
			return cust;
		}
		//return cust;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cust;
	}
	@SuppressWarnings("unchecked")
	public int noOfRowsOfCust()
	{
		List<Customer> cust=null;
		try{
			System.out.println("i came in check method");
		/*Configuration config= new Configuration().configure("hibernateNew.cfg.xml");
		System.out.println("Configuration done");
		SessionFactory sessionFactory= config.buildSessionFactory();
		Session session= sessionFactory.openSession();*/
		PersisteanceManager pm= new PersisteanceManager();
		SessionFactory sessionFactory=pm.getOpenSession();
		Session session= sessionFactory.openSession();
		session.beginTransaction();
		int count = pm.featchTotalNoOfRows(session,new Customer());
		//cust= (List<Customer>) pm.executeCustomeQuery(session, qyery);
		//org.hibernate.Query query= session.createQuery(qyery);
		//cust= query.list();
		
		//session.save(cust);
		session.getTransaction().commit();
		//session.getTransaction().commit();
		pm.closeSession(session, sessionFactory);
		
			return count;
		
		//return cust;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	public List<Customer> searchCustomerByLimit(String startRow,String limit)
	{
		List<Customer> cust=null;
		try{
			System.out.println("i came in check method");
		/*Configuration config= new Configuration().configure("hibernateNew.cfg.xml");
		System.out.println("Configuration done");
		SessionFactory sessionFactory= config.buildSessionFactory();
		Session session= sessionFactory.openSession();*/
		PersisteanceManager pm= new PersisteanceManager();
		SessionFactory sessionFactory=pm.getOpenSession();
		Session session= sessionFactory.openSession();
		session.beginTransaction();
		System.out.println("value of class"+Customer.class.getCanonicalName());
		cust=(List<Customer>) pm.featchResultWithLimit(session, new Customer(), startRow, limit);
		session.getTransaction().commit();
		//session.getTransaction().commit();
		pm.closeSession(session, sessionFactory);
		if(cust!=null && cust.size()>0)
		{
			return cust;
		}
		//return cust;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return cust;
	}
	@SuppressWarnings("unchecked")
	public List<Customer> searchCustomerByCondition(String id)
	
	{
		List<Customer> cust=null;
		try{
			System.out.println("i came in  method searchCustomerById");
		//Configuration config= new Configuration().configure("hibernateNew.cfg.xml");
		//System.out.println("Configuration done");
		//SessionFactory sessionFactory= config.buildSessionFactory();
			PersisteanceManager pm= new PersisteanceManager();
			SessionFactory sessionFactory=pm.getOpenSession();
		Session session= sessionFactory.openSession();
		session.beginTransaction();
		String qyery="from com.zinnia.model.Customer where "+id+"";
		cust= (List<Customer>) pm.executeCustomeQuery(session, qyery);
		//org.hibernate.Query query= session.createQuery(qyery);
		//cust= query.list();
		
		//session.save(cust);
		session.getTransaction().commit();
		pm.closeSession(session, sessionFactory);
		if(cust!=null && cust.size()>0)
		{
			return cust;
		}
		//return cust;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Customer SaveCustomer(Customer object)
	{
		
		try{
			System.out.println("i came in check method");
		/*Configuration config= new Configuration().configure("hibernateNew.cfg.xml");
		System.out.println("Configuration done");
		SessionFactory sessionFactory= config.buildSessionFactory();
		Session session= sessionFactory.openSession();*/
			PersisteanceManager pm= new PersisteanceManager();
			SessionFactory sessionFactory=pm.getOpenSession();
			Session session= sessionFactory.openSession();
		session.beginTransaction();
		//String qyery="from com.zinnia.model.Customer ";
		//org.hibernate.Query query= session.createQuery(qyery);
		//cust= query.list();
		Customer cust= (Customer) pm.InserDetails(object, session);
		System.out.println("Customer Saved"+ cust.getId());
		//session.save(object);
		session.getTransaction().commit();
		pm.closeSession(session, sessionFactory);
		return cust;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
